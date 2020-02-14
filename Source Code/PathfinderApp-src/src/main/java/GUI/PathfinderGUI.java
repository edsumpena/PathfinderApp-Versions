package GUI;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.profile.SimpleMotionConstraints;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.acmerobotics.roadrunner.trajectory.TrajectoryBuilder;
import com.acmerobotics.roadrunner.trajectory.constraints.TrajectoryConstraints;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

public class PathfinderGUI extends JPanel implements MouseMotionListener, MouseListener, KeyListener {

    private int imgWidth = 650;
    private int imgHeight = 650;
    private static int mouseX = 0;
    private static int mouseY = 0;

    private static String coordString = "X: ---.---, Y: ---.---";
    private static boolean mouseDown = false;
    private static boolean blocker = false;
    private static boolean dispHeading = false;
    private static String headingString = "Undefined Heading";
    private static boolean updateHeading = true;
    private static boolean updateTrajString = false;
    private static String trajString = "";
    private static int selectedItemIndex = 0;
    private static boolean updateSelectedItem = false;
    private static boolean ctrlPressed = false;
    private static boolean zPressed = false;
    private static boolean shiftPressed = false;

    private static ArrayList<MoveOptions> options;
    private static MoveOptions currentOption;
    private static MoveOptions savedOption;

    private static PaintVectors pv;
    private JLayeredPane layeredPane;
    private JPanel paintPanel;
    private JLabel coordinates;
    private JTextField heading;
    private JTextField trajStringBox;
    private JComboBox jComboBox1;
    private JButton add;

    private static File prevFilePath;

    private static ArrayList<ArrayList<Pose2d>> poseUndoHistory;
    private static ArrayList<ArrayList<MoveOptions>> moveOptionsUndoHistory;
    private static ArrayList<ArrayList<Pose2d>> poseRedoHistory;
    private static ArrayList<ArrayList<MoveOptions>> moveOptionsRedoHistory;

    public PathfinderGUI(){
        pv = new PaintVectors();
        options = new ArrayList<>();
        poseUndoHistory = new ArrayList<>();
        moveOptionsUndoHistory = new ArrayList<>();
        poseRedoHistory = new ArrayList<>();
        moveOptionsRedoHistory = new ArrayList<>();

        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(1000, 650));
        BufferedImage image = null;
        try {
            image = ImageIO.read(new File("images/SkyStoneFieldv1.png"));  //Import FTC Field Image     //res/images/SkyStoneFieldv1.png
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
        Image newImage = image.getScaledInstance(imgWidth, imgHeight, java.awt.Image.SCALE_SMOOTH);  //Scale up the image in size
        ImageIcon imageIcon = new ImageIcon(newImage);
        JLabel jLabel = new JLabel();
        jLabel.setIcon(imageIcon);
        jLabel.setBounds(0, 0, imgWidth, imgHeight);

        paintPanel = new JPanel() {
            @Override
            public void paintComponent(Graphics g){
                super.paintComponent(g);
                if(!pv.getPose2dList().isEmpty() && !options.isEmpty() && pv.getPose2dList().size() >= 2 &&
                        options.size() >= 2) {
                    ArrayList<Pose2d> p2d = pv.getPose2dList();
                    for(int i = 0; i < p2d.size(); i++)
                        p2d.set(i, new Pose2d(imgToFieldCoordsX((int) Math.round(p2d.get(i).getY() - 30)),
                                imgToFieldCoordsY((int) Math.round(p2d.get(i).getX() - 2)), p2d.get(i).getHeading()));
                    Trajectory t = TrajectoryWrapper.buildAndGetTrajectory(p2d, options);
                    int[][] xy = TrajectoryWrapper.getPolylineXY(t);
                    for (int i = 0; i < xy[0].length; i++) {
                        int x = (int) Math.round(fieldToImgCoordsX(xy[1][i]));
                        int y = (int) Math.round(fieldToImgCoordsY(xy[0][i]));
                        xy[0][i] = x;
                        xy[1][i] = y;
                    }
                    pv.setPolyline(xy[0], xy[1]);
                } else
                    pv.setPolyline(null, null);
                pv.paintPoints(g);
                layeredPane.moveToFront(paintPanel);
            }
        };
        paintPanel.setOpaque(false);
        paintPanel.setVisible(true);
        paintPanel.setBounds(0, 0, 2000, 2000);

        coordinates = new JLabel("X: ---.--, Y: ---.--"){
            @Override
            public void paintComponent(Graphics g){
                super.paintComponent(g);
                ArrayList<Pose2d> p2d = pv.getPose2dList();
                int i = pv.getSelectedIndex();
                if(i == -1 || p2d.isEmpty())
                    coordinates.setText(coordString);
                else
                    coordinates.setText("X: " + Math.round(imgToFieldCoordsX((int) Math.round(p2d.get(i).getY() - 30)) * 1000d) / 1000d + ", Y: " +
                            Math.round(imgToFieldCoordsY((int) Math.round(p2d.get(i).getX() - 2)) * 1000d) / 1000d);
            }
        };
        coordinates.setFont(coordinates.getFont().deriveFont(13f));
        coordinates.setBounds(700, 5, 150, 50);
        coordinates.setOpaque(false);
        coordinates.setVisible(true);

        add = new JButton("+ Add Point"){
            @Override
            public void paintComponent(Graphics g){
                super.paintComponent(g);
                if(pv.getSelectedIndex() == -1)
                    add.setText("+ Add Point");
                else
                    add.setText("Set Point");

            }
        };
        add.setBounds(850, 20, 100, 25);
        add.setOpaque(false);
        add.setVisible(true);

        add.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                if(currentOption != null && pv.getSelectedIndex() == -1) {
                    boolean reqHeading = currentOption == MoveOptions.SplineTo_Forward ||
                            currentOption == MoveOptions.SplineTo_Reversed || options.isEmpty();
                    Pose2d input = CoordinatesPopup.dispPopupMenu(reqHeading, new String[]{null, null, null});
                    if(input.getX() <= 1000000000 && input.getY() <= 1000000000 &&
                            input.getHeading() <= 1000000000) {
                        pv.addPoint((int) Math.round(fieldToImgCoordsX((int) Math.round(input.getY()))),
                                (int) Math.round(fieldToImgCoordsY((int) Math.round(input.getX()))) + 28,
                                input.getHeading(), Color.CYAN);
                        options.add(currentOption);
                    }
                    layeredPane.repaint();
                } else if(currentOption != null && pv.getSelectedIndex() != -1){
                    boolean reqHeading = options.get(pv.getSelectedIndex()) == MoveOptions.SplineTo_Forward ||
                            currentOption == MoveOptions.SplineTo_Reversed || pv.getSelectedIndex() == 0;
                    Pose2d p2d = pv.getPose2dList().get(pv.getSelectedIndex());
                    p2d = new Pose2d(imgToFieldCoordsX((int) Math.round(p2d.getY() - 30)),
                            imgToFieldCoordsY((int) Math.round(p2d.getX() - 2)), p2d.getHeading());
                    Pose2d input = CoordinatesPopup.dispPopupMenu(reqHeading, new String[]{String.valueOf(p2d.getX()),
                            String.valueOf(p2d.getY()), String.valueOf(p2d.getHeading())});
                    if(input.getX() <= 1000000000 && input.getY() <= 1000000000 &&
                            input.getHeading() <= 1000000000) {
                        pv.setPoint(pv.getSelectedIndex(), (int) Math.round(fieldToImgCoordsX((int) Math.round(input.getY()))),
                                (int) Math.round(fieldToImgCoordsY((int) Math.round(input.getX()))) + 28,
                                input.getHeading(), Color.CYAN);
                        options.set(pv.getSelectedIndex(), currentOption);
                    }
                    layeredPane.repaint();
                }
            }
        });

        jComboBox1 = new JComboBox(){
            @Override
            public void paintComponent(Graphics g){
                super.paintComponent(g);
                if(updateSelectedItem) {
                    jComboBox1.setSelectedIndex(selectedItemIndex);
                    updateSelectedItem = false;
                }
            }
        };  //Dropdown box creator for Drive Options
        jComboBox1.addItem("[Select]");  //Dropdown options for Drive Options
        jComboBox1.addItem("Line To");
        jComboBox1.addItem("Strafe To");
        jComboBox1.addItem("Reverse To");
        jComboBox1.addItem("Spline To");
        jComboBox1.addItem("Reverse Spline To");
        jComboBox1.setFont(jComboBox1.getFont().deriveFont(13f));
        jComboBox1.addItemListener(new ItemChangeListener());
        jComboBox1.setBounds(720, 75, 200, 30);
        jComboBox1.setFocusable(false);

        heading = new JTextField(){
            @Override
            public void paintComponent(Graphics g){
                super.paintComponent(g);
                heading.setFocusable(dispHeading);
                if(updateHeading) {
                    heading.setText(headingString);
                    updateHeading = false;
                }
                headingString = heading.getText();
            }
        };
        heading.setFont(heading.getFont().deriveFont(13f));
        heading.setBounds(730, 150, 150, 25);
        heading.setOpaque(true);
        heading.setVisible(true);

        JButton saveHeading = new JButton("Save");
        saveHeading.setBounds(890, 150, 65, 25);
        saveHeading.setOpaque(false);
        saveHeading.setVisible(true);

        saveHeading.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                if(pv.getSelectedIndex() != -1 && (options.get(pv.getSelectedIndex()) == MoveOptions.SplineTo_Forward ||
                        options.get(pv.getSelectedIndex()) == MoveOptions.SplineTo_Reversed || pv.getSelectedIndex() == 0)) {
                    Pose2d selectedPose = pv.getPose2dList().get(pv.getSelectedIndex());
                    updateHistory();
                    try{
                        pv.setPoint(pv.getSelectedIndex(), (int) Math.round(selectedPose.getX()),
                                (int) Math.round(selectedPose.getY()), Double.valueOf(headingString), Color.CYAN);
                    } catch (Exception e){
                        moveOptionsUndoHistory.remove(moveOptionsUndoHistory.size() - 1);
                        poseUndoHistory.remove(poseUndoHistory.size() - 1);
                    }
                    headingString = String.valueOf(pv.getPose2dList().get(pv.getSelectedIndex()).getHeading());
                    updateHeading = true;
                }
            }
        });

        JButton button = new JButton("Generate Trajectory String");
        button.setBounds(725, 510, 200, 25);
        button.setOpaque(false);
        button.setVisible(true);

        button.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                if(!options.isEmpty() && !pv.getPose2dList().isEmpty()) {
                    ArrayList<Pose2d> p2d = pv.getPose2dList();
                    for (int i = 0; i < p2d.size(); i++)
                        p2d.set(i, new Pose2d(imgToFieldCoordsX((int) Math.round(p2d.get(i).getY() - 30)),
                                imgToFieldCoordsY((int) Math.round(p2d.get(i).getX() - 2)), p2d.get(i).getHeading()));
                    trajString = TrajectoryStringConverter.generateStringFromTrajectory(p2d, options);
                    updateTrajString = true;
                }
            }
        });

        JLabel headingDescription = new JLabel("Heading:");
        headingDescription.setBounds(665, 148, 50, 25);
        headingDescription.setOpaque(false);
        headingDescription.setVisible(true);

        trajStringBox = new JTextField(){
            @Override
            public void paintComponent(Graphics g){
                super.paintComponent(g);
                if(updateTrajString) {
                    trajStringBox.setText(trajString);
                    updateTrajString = false;
                }
                trajString = trajStringBox.getText();
            }
        };
        trajStringBox.setFont(trajStringBox.getFont().deriveFont(13f));
        trajStringBox.setBounds(675, 550, 300, 25);
        trajStringBox.setOpaque(true);
        trajStringBox.setVisible(true);

        JButton open = new JButton("Open Trajectory");
        open.setBounds(685, 585, 125, 25);
        open.setOpaque(false);
        open.setVisible(true);

        open.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Open Path");
                fileChooser.setPreferredSize(new Dimension(800, 600));
                fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                fileChooser.removeChoosableFileFilter(fileChooser.getFileFilter());  //Remove the default "All Files" filter
                FileFilter filter = new FileNameExtensionFilter("PATH file", "path");
                fileChooser.addChoosableFileFilter(filter); //Add .path file type filter

                if (prevFilePath != null) {
                    fileChooser.setCurrentDirectory(prevFilePath);
                }
                int result = fileChooser.showOpenDialog(layeredPane);
                if (result == JFileChooser.APPROVE_OPTION) {    //Save button is pressed
                    File fileToRead = fileChooser.getSelectedFile();
                    String inputData = "";
                    try {
                        ObjectInputStream outputStreamReader = new ObjectInputStream(new FileInputStream(fileToRead));
                        inputData = outputStreamReader.readObject().toString();
                        outputStreamReader.close();
                    } catch (Exception e) {
                        int dialogButton = JOptionPane.ERROR_MESSAGE;   //Error message pops up
                        JOptionPane.showMessageDialog(null, "Error: Failed to read file (" +
                                e.getClass().getCanonicalName() + ")\n'" + fileToRead + "'", "Error", dialogButton);
                    }

                    ArrayList<Pose2d> pose2ds;
                    ArrayList<MoveOptions> options;
                    updateHistory();

                    try {
                        pose2ds = TrajectoryStringConverter.generateP2dFromString(inputData);
                        options = TrajectoryStringConverter.generateMoveOptionsFromString(inputData);

                        for (int i = 0; i < pose2ds.size(); i++)
                            pose2ds.set(i, new Pose2d(fieldToImgCoordsX((int) Math.round(pose2ds.get(i).getY())),
                                    fieldToImgCoordsY((int) Math.round(pose2ds.get(i).getX())) + 28, pose2ds.get(i).getHeading()));

                        pv.loadData(pose2ds);
                        PathfinderGUI.options = options;
                    } catch (Exception e) {
                        int dialogButton = JOptionPane.ERROR_MESSAGE;   //Error message pops up
                        JOptionPane.showMessageDialog(null, "Error: Invalid file input string (" +
                                e.getClass().getCanonicalName() + ")\n'" + fileToRead + "'", "Error", dialogButton);
                        moveOptionsUndoHistory.remove(moveOptionsUndoHistory.size() - 1);
                        poseUndoHistory.remove(poseUndoHistory.size() - 1);
                    }
                    prevFilePath = fileChooser.getCurrentDirectory();
                } else if (result == JFileChooser.CANCEL_OPTION) {      //If cancel button is pressed
                    prevFilePath = fileChooser.getCurrentDirectory();
                } else if (result == JFileChooser.ERROR_OPTION) {       //If X button is pressed
                    prevFilePath = fileChooser.getCurrentDirectory();
                }
            }
        });

        JButton save = new JButton("Save Trajectory");
        save.setBounds(835, 585, 125, 25);
        save.setOpaque(false);
        save.setVisible(true);

        save.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JFileChooser fileChooser = new JFileChooser() {
                };
                fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                fileChooser.removeChoosableFileFilter(fileChooser.getFileFilter());  //Remove the default "All Files" filter
                FileFilter filter = new FileNameExtensionFilter("PATH file", "path");
                fileChooser.addChoosableFileFilter(filter); //Add .path file type filter

                fileChooser.setDialogTitle("Save Path");
                fileChooser.setPreferredSize(new Dimension(800, 600));
                fileChooser.setSelectedFile(new File("untitled.path"));
                if (prevFilePath != null) {
                    fileChooser.setCurrentDirectory(prevFilePath);
                }
                int userSelection = fileChooser.showSaveDialog(layeredPane);
                if (userSelection == JFileChooser.APPROVE_OPTION) {     //Save Button is pressed
                    File f = fileChooser.getSelectedFile();
                    if (!String.valueOf(f).contains(".path")) {   //If file name does not contain .path
                        int dialogButton = JOptionPane.ERROR_MESSAGE;   //Error message pops up
                        JOptionPane.showMessageDialog(null, "Error: File must contain '.path' extension", "Error", dialogButton);
                    } else if (f.exists()) {  //If file name already exists
                        int dialogButton2 = JOptionPane.YES_NO_OPTION;
                        int dialogResult = JOptionPane.showConfirmDialog(null,
                                "File Already Exists! Do you want to Replace?", "File Exists", dialogButton2);    //Ask if user wants to replace file
                        if (dialogResult == JOptionPane.YES_OPTION) {
                            if(!options.isEmpty() && !pv.getPose2dList().isEmpty()) {
                                System.gc();
                                f.delete();
                                ArrayList<Pose2d> p2d = pv.getPose2dList();
                                for (int i = 0; i < p2d.size(); i++)
                                    p2d.set(i, new Pose2d(imgToFieldCoordsX((int) Math.round(p2d.get(i).getY() - 30)),
                                            imgToFieldCoordsY((int) Math.round(p2d.get(i).getX() - 2)), p2d.get(i).getHeading()));
                                String traj = TrajectoryStringConverter.generateStringFromTrajectory(p2d, options);
                                try {
                                    ObjectOutputStream outputStreamWriter = new ObjectOutputStream(new FileOutputStream(f, true));
                                    outputStreamWriter.writeObject(traj);
                                    outputStreamWriter.close();
                                } catch (Exception e){
                                    int dialogButton = JOptionPane.ERROR_MESSAGE;   //Error message pops up
                                    JOptionPane.showMessageDialog(null, "Error: Failed to save file ("
                                            + e.getClass().getCanonicalName() + ")\n'" + f + "'", "Error", dialogButton);
                                }
                            } else {
                                int dialogButton = JOptionPane.ERROR_MESSAGE;   //Error message pops up
                                JOptionPane.showMessageDialog(null,
                                        "Error: No data to save!", "Error", dialogButton);
                            }
                        }
                    } else {    //If not a duplicate file name and contains .path ext in the name
                        if(!options.isEmpty() && !pv.getPose2dList().isEmpty()) {
                            ArrayList<Pose2d> p2d = pv.getPose2dList();
                            for (int i = 0; i < p2d.size(); i++)
                                p2d.set(i, new Pose2d(imgToFieldCoordsX((int) Math.round(p2d.get(i).getY() - 30)),
                                        imgToFieldCoordsY((int) Math.round(p2d.get(i).getX() - 2)), p2d.get(i).getHeading()));
                            String traj = TrajectoryStringConverter.generateStringFromTrajectory(p2d, options);
                            try {
                                ObjectOutputStream outputStreamWriter = new ObjectOutputStream(new FileOutputStream(f, true));
                                outputStreamWriter.writeObject(traj);
                                outputStreamWriter.close();
                            } catch (Exception e){
                                int dialogButton = JOptionPane.ERROR_MESSAGE;   //Error message pops up
                                JOptionPane.showMessageDialog(null, "Error: Failed to save file ("
                                        + e.getCause() + ")\n'" + f + "'", "Error", dialogButton);
                            }
                        } else {
                            int dialogButton = JOptionPane.ERROR_MESSAGE;   //Error message pops up
                            JOptionPane.showMessageDialog(null, "Error: No data to save!",
                                    "Error", dialogButton);
                        }
                    }
                } else if (userSelection == JFileChooser.CANCEL_OPTION) {   //Cancel button is pressed
                    prevFilePath = fileChooser.getCurrentDirectory();
                } else if (userSelection == JFileChooser.ERROR_OPTION) {    // X button is pressed
                    prevFilePath = fileChooser.getCurrentDirectory();
                }
            }
        });

        JButton clearPath = new JButton("Clear Trajectory");
        clearPath.setBounds(760, 620, 125, 25);
        clearPath.setOpaque(false);
        clearPath.setVisible(true);

        clearPath.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                int dialogButton2 = JOptionPane.YES_NO_OPTION;
                int dialogResult = JOptionPane.showConfirmDialog(null,
                        "Are you sure you want to clear trajectory?\n" +
                                "             This CANNOT be undone!", "Warning", dialogButton2);    //Ask if user wants to replace file
                if (dialogResult == JOptionPane.YES_OPTION) {
                    pv.clearTrajectory();
                    options.clear();
                }
            }
        });

        layeredPane.add(jLabel, 1, 0);
        layeredPane.add(coordinates, 2, 0);
        layeredPane.add(add, 3, 0);
        layeredPane.add(jComboBox1, 4, 0);
        layeredPane.add(heading, 5,0);
        layeredPane.add(saveHeading, 6, 0);
        layeredPane.add(button, 7, 0);
        layeredPane.add(headingDescription, 8, 0);
        layeredPane.add(trajStringBox, 9, 0);
        layeredPane.add(open, 10, 0);
        layeredPane.add(save, 11, 0);
        layeredPane.add(clearPath, 12, 0);
        layeredPane.add(paintPanel, 50, 0);
        add(layeredPane);
    }

    static class ItemChangeListener implements ItemListener {
        @Override
        public void itemStateChanged(ItemEvent event) {  //Which dropdown option is selected for Drive Options?
            if (event.getStateChange() == ItemEvent.SELECTED) {
                Object item = event.getItem();
                if (String.valueOf(item).equals("Line To"))      //Robot moves in straight line
                    currentOption = MoveOptions.LineTo_Forward;
                else if (String.valueOf(item).equals("[Select]"))
                    currentOption = null;
                else if (String.valueOf(item).equals("Reverse To"))     //Robot backs up to a location
                    currentOption = MoveOptions.LineTo_Reversed;
                else if (String.valueOf(item).equals("Strafe To"))       //Robot strafes to a location (mecanum wheels only)
                    currentOption = MoveOptions.StrafeTo;
                else if (String.valueOf(item).equals("Spline To"))
                    currentOption = MoveOptions.SplineTo_Forward;
                else if(String.valueOf(item).equals("Reverse Spline To"))
                    currentOption = MoveOptions.SplineTo_Reversed;
                if(pv.getSelectedIndex() != -1)
                    options.set(pv.getSelectedIndex(), currentOption);
            }
        }
    }

    public static void initListener(JFrame lp) {
        lp.addMouseMotionListener(new PathfinderGUI());
        lp.addMouseListener(new PathfinderGUI());
        lp.addKeyListener(new PathfinderGUI());
    }

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        RunPathfinderApp.frame.requestFocus();
        pv.testForSelected(e.getX(), e.getY());
        if(pv.getSelectedIndex() != -1 && (options.get(pv.getSelectedIndex()) == MoveOptions.SplineTo_Forward ||
                options.get(pv.getSelectedIndex()) == MoveOptions.SplineTo_Reversed || pv.getSelectedIndex() == 0)) {
            headingString = "" + pv.getPose2dList().get(pv.getSelectedIndex()).getHeading();
            dispHeading = true;
        } else {
            headingString = "Undefined Heading";
            dispHeading = false;
        }

        if(pv.getSelectedIndex() != -1){
            currentOption = options.get(pv.getSelectedIndex());
            selectedItemIndex = getSelectedItemIndex(currentOption);
        } else {
            currentOption = savedOption;
            selectedItemIndex = getSelectedItemIndex(savedOption);
        }
        updateSelectedItem = true;
        updateHeading = true;
        layeredPane.repaint();
    }

    @Override
    public void mousePressed(MouseEvent e) {
        mouseDown = true;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        mouseDown = false;
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        Thread t = new Thread() {
            public void run() {
                while (mouseDown && mouseX <= imgWidth + 1 && mouseY <= imgHeight + 26) {
                    pv.setPoint(pv.getSelectedIndex(), mouseX - 1, mouseY - 1, Double.MAX_VALUE, Color.CYAN);
                    updateFieldCoordDisp(mouseX, mouseY);
                    try {
                        Thread.sleep(20);
                    } catch (Exception ex) {
                    }
                }
                blocker = false;
            }
        };
        if (SwingUtilities.isLeftMouseButton(e)) {  //Left click is held down and mouse is moved
            mouseX = e.getX();
            mouseY = e.getY();
            if(pv.isGrabbingPoint(mouseX, mouseY) == pv.getSelectedIndex() && pv.getSelectedIndex() != -1 && !blocker) {
                updateHistory();
                t.start();
                blocker = true;
            }
            //System.out.println("mouseX = " + mouseX);
            //System.out.println("mouseY = " + mouseY);
        }

        if(pv.getSelectedIndex() == -1)
            savedOption = currentOption;
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();

        if(pv.getSelectedIndex() == -1)
            savedOption = currentOption;

        updateFieldCoordDisp(mouseX, mouseY);
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch(e.getKeyCode()){
            case KeyEvent.VK_N:
                if(currentOption != null && mouseX <= imgWidth && mouseY <= imgHeight + 25) {
                    updateHistory();
                    if(pv.getPose2dList().isEmpty() || options.isEmpty())
                        pv.addPoint(mouseX, mouseY, 0.0, Color.CYAN);
                    else
                        pv.addPoint(mouseX, mouseY, Double.MAX_VALUE, Color.CYAN);
                    options.add(currentOption);
                    layeredPane.repaint();
                }
                break;
            case KeyEvent.VK_DELETE:
                if(pv.getSelectedIndex() != -1) {
                    pv.removeIndex(pv.getSelectedIndex());
                    updateHistory();
                    if(options.get(pv.getSelectedIndex()) == MoveOptions.SplineTo_Forward ||
                            options.get(pv.getSelectedIndex()) == MoveOptions.SplineTo_Reversed || pv.getSelectedIndex() == 0) {
                        headingString = "" + pv.getPose2dList().get(pv.getSelectedIndex()).getHeading();
                        currentOption = options.get(pv.getSelectedIndex());
                        selectedItemIndex = getSelectedItemIndex(currentOption);
                        updateSelectedItem = true;
                        dispHeading = true;
                    } else {
                        headingString = "Undefined Heading";
                        if(currentOption != null) {
                            selectedItemIndex = getSelectedItemIndex(currentOption);
                            updateSelectedItem = true;
                        }
                        dispHeading = false;
                    }
                    updateHeading = true;
                    layeredPane.repaint();
                }
                break;
            case KeyEvent.VK_LEFT:
                if(pv.getSelectedIndex() != -1) {
                    Pose2d p2d = pv.getPose2dList().get(pv.getSelectedIndex());
                    p2d = new Pose2d(imgToFieldCoordsX((int) Math.round(p2d.getY() - 30)),
                            imgToFieldCoordsY((int) Math.round(p2d.getX() - 2)), p2d.getHeading());
                    int deltaY = (int) (p2d.getY() + 2 < 71.3 ? 2 : 71.3 - p2d.getY());
                    p2d = new Pose2d(p2d.getX(), p2d.getY() + deltaY, p2d.getHeading());
                    p2d = new Pose2d(fieldToImgCoordsX((int) Math.round(p2d.getY())),
                            fieldToImgCoordsY((int) Math.round(p2d.getX())) + 28, p2d.getHeading());
                    pv.setPoint(pv.getSelectedIndex(), (int) Math.round(p2d.getX()), (int) Math.round(p2d.getY()),
                            p2d.getHeading(), Color.CYAN);
                }
                break;
            case KeyEvent.VK_RIGHT:
                if(pv.getSelectedIndex() != -1) {
                    Pose2d p2d = pv.getPose2dList().get(pv.getSelectedIndex());
                    p2d = new Pose2d(imgToFieldCoordsX((int) Math.round(p2d.getY() - 30)),
                            imgToFieldCoordsY((int) Math.round(p2d.getX() - 2)), p2d.getHeading());
                    int deltaY = (int) (p2d.getY() - 2 > -71.3 ? -2 : -71.3 - p2d.getY());
                    p2d = new Pose2d(p2d.getX(), p2d.getY() + deltaY, p2d.getHeading());
                    p2d = new Pose2d(fieldToImgCoordsX((int) Math.round(p2d.getY())),
                            fieldToImgCoordsY((int) Math.round(p2d.getX())) + 28, p2d.getHeading());
                    pv.setPoint(pv.getSelectedIndex(), (int) Math.round(p2d.getX()), (int) Math.round(p2d.getY()),
                            p2d.getHeading(), Color.CYAN);
                }
                break;
            case KeyEvent.VK_UP:
                if(pv.getSelectedIndex() != -1) {
                    Pose2d p2d = pv.getPose2dList().get(pv.getSelectedIndex());
                    p2d = new Pose2d(imgToFieldCoordsX((int) Math.round(p2d.getY() - 30)),
                            imgToFieldCoordsY((int) Math.round(p2d.getX() - 2)), p2d.getHeading());
                    int deltaX = (int) (p2d.getX() + 2 < 71.3 ? 2 : 71.3 - p2d.getX());
                    p2d = new Pose2d(p2d.getX() + deltaX, p2d.getY(), p2d.getHeading());
                    p2d = new Pose2d(fieldToImgCoordsX((int) Math.round(p2d.getY())),
                            fieldToImgCoordsY((int) Math.round(p2d.getX())) + 28, p2d.getHeading());
                    pv.setPoint(pv.getSelectedIndex(), (int) Math.round(p2d.getX()), (int) Math.round(p2d.getY()),
                            p2d.getHeading(), Color.CYAN);
                }
                break;
            case KeyEvent.VK_DOWN:
                if(pv.getSelectedIndex() != -1) {
                    Pose2d p2d = pv.getPose2dList().get(pv.getSelectedIndex());
                    p2d = new Pose2d(imgToFieldCoordsX((int) Math.round(p2d.getY() - 30)),
                            imgToFieldCoordsY((int) Math.round(p2d.getX() - 2)), p2d.getHeading());
                    int deltaX = (int) (p2d.getX() - 2 > -71.3 ? -2 : -71.3 - p2d.getX());
                    p2d = new Pose2d(p2d.getX() + deltaX, p2d.getY(), p2d.getHeading());
                    p2d = new Pose2d(fieldToImgCoordsX((int) Math.round(p2d.getY())),
                            fieldToImgCoordsY((int) Math.round(p2d.getX())) + 28, p2d.getHeading());
                    pv.setPoint(pv.getSelectedIndex(), (int) Math.round(p2d.getX()), (int) Math.round(p2d.getY()),
                            p2d.getHeading(), Color.CYAN);
                }
                break;
        }

        if(e.getKeyCode() == KeyEvent.VK_CONTROL)
            ctrlPressed = true;
        if(e.getKeyCode() == KeyEvent.VK_Z)
            zPressed = true;
        if(e.getKeyCode() == KeyEvent.VK_SHIFT)
            shiftPressed = true;
        if(ctrlPressed && zPressed && !shiftPressed && !poseUndoHistory.isEmpty() && !moveOptionsUndoHistory.isEmpty()){
            poseRedoHistory.add(pv.getPose2dList());
            moveOptionsRedoHistory.add(options);
            pv.loadData(poseUndoHistory.remove(poseUndoHistory.size() - 1));
            options = moveOptionsUndoHistory.remove(moveOptionsUndoHistory.size() - 1);
        }
        if(ctrlPressed && zPressed && shiftPressed && !poseRedoHistory.isEmpty() && !moveOptionsRedoHistory.isEmpty()){
            poseUndoHistory.add(pv.getPose2dList());
            moveOptionsUndoHistory.add(options);
            pv.loadData(poseRedoHistory.remove(poseRedoHistory.size() - 1));
            options = moveOptionsRedoHistory.remove(moveOptionsRedoHistory.size() - 1);
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_CONTROL)
            ctrlPressed = false;
        if(e.getKeyCode() == KeyEvent.VK_Z)
            zPressed = false;
        if(e.getKeyCode() == KeyEvent.VK_SHIFT)
            shiftPressed = false;
    }

    private void updateFieldCoordDisp(int mouseX, int mouseY){
        if(mouseX <= imgWidth && mouseY <= imgHeight + 25)
            coordString = "X: " + imgToFieldCoordsX(mouseY - 30) + ", Y: " + imgToFieldCoordsY(mouseX - 2);
        else
            coordString = "X: ---.---, Y: ---.---";
        layeredPane.repaint();
    }

    private double fieldToImgCoordsX(int fieldY){
        double zeroX = imgHeight / 2d;
        double pxPerX = zeroX / 72d;
        return Math.round(-(fieldY * pxPerX - zeroX) * 1000d) / 1000d;
    }

    private double fieldToImgCoordsY(int fieldX){
        double zeroY = imgWidth / 2d;
        double pxPerY = zeroY / 72d;
        return Math.round(-(fieldX * pxPerY - zeroY) * 1000d) / 1000d;
    }

    private double imgToFieldCoordsY(int xPix){
        double zeroY = imgWidth / 2d;
        double pxPerY = zeroY / 72d;
        return Math.round((zeroY - xPix)  / pxPerY * 1000d) / 1000d;
    }

    private double imgToFieldCoordsX(int yPix){
        double zeroX = imgHeight / 2d;
        double pxPerX = zeroX / 72d;
        return Math.round((zeroX - yPix) / pxPerX * 1000d) / 1000d;
    }

    private int getSelectedItemIndex(MoveOptions moveOptions){
        if(moveOptions == null)
            return 0;
        switch(moveOptions){
            case LineTo_Forward:
                return 1;
            case LineTo_Reversed:
                return 3;
            case StrafeTo:
                return 2;
            case SplineTo_Forward:
                return 4;
            case SplineTo_Reversed:
                return 5;
        }
        return 0;
    }

    private void updateHistory(){
        moveOptionsUndoHistory.add(options);
        poseUndoHistory.add(pv.getPose2dList());
        moveOptionsRedoHistory.clear();
        poseRedoHistory.clear();
    }
}
