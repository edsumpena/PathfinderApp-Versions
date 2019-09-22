package App;

/*
 * List of current features:
 * N to create new dot
 * Click & Drag dot with mouse to move
 * Save Path File Output: .path compressed file containing .cir & .line text editor files
 * .cir & .line text files contain serialized "circles" & "lineSettingsAndParameters" arrays:
 * Example output for 2 circle + 1 line:
 *  -ArrayList "circles": {circle1XVal, circle1YVal, circle1Color, circle2XVal, circle2YVal, circle2Color}
 *  -ArrayList "lineSettingsAndParameters.get(0)": {line1Type}
 *  -ArrayList "lineSettingsAndParameters.get(1)": {lineX1, lineY1}
 *  -ArrayList "lineSettingsAndParameters.get(2)": {lineX2, lineY2}
 *  -ArrayList "lineSettingsAndParameters.get(3)": {lineX3, lineY3}
 */

import App.Converters.ArrayListConverters;
import App.Converters.FromAndToPose2D;
import App.Converters.LineArrayProcessor;
import App.Debugger.cmdLine;
import App.ReadingAndWriting.MotorSetup;
import App.ReadingAndWriting.SerializeAndDeserialize;
import App.ReadingAndWriting.ZipAndUnzip;
import App.Wrappers.DriverConstraintsWrapper;
import App.Wrappers.TrajBuilderWrapper;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.trajectory.TrajectoryBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;

import static App.Converters.LineArrayProcessor.getCurrentSetting;

public class MainActivity extends JPanel {
    static boolean line = false;
    static boolean curve = false;
    static boolean select = true;
    static boolean strafe = false;
    static boolean reverse = false;
    static boolean spline = false;
    static int mouseX = 0;
    static int mouseY = 0;
    static boolean nPressed = false;
    static boolean gPressed = false;
    static boolean alreadyAdded = false;
    static boolean mouseExited = true;
    static boolean mouseClicked = false;
    static boolean mouseClickedFocus = false;
    static int prevSelectedLine = -1;
    static int prevSelectedMotor = -1;
    public static int xOffset = -15;
    public static int yOffset = -40;
    static ArrayList<Integer> circles = new ArrayList<>();
    static ArrayList<ArrayList<String>> lineSettingsAndParameters = new ArrayList<>();
    static ArrayList<String> settings = new ArrayList<>();
    static ArrayList<String> params1 = new ArrayList<>();
    static ArrayList<String> params2 = new ArrayList<>();
    static ArrayList<String> params3 = new ArrayList<>();
    static ArrayList<Integer> motorExecutedLocation = new ArrayList<>();
    static ArrayList<String> motorNames = new ArrayList<>();
    static String pathName = "untitled path";
    static String currentlySelected = "[Select]";
    static int z = 0;
    static int v = 0;
    static int z2 = 0;
    static int v2 = 0;
    static boolean selected = false;
    static int clearX = 0;
    static int clearY = 0;
    static File prevFilePath;
    static ObjectMapper objectMapper = new ObjectMapper();
    static int[] selectedCircle = {-1000, -1000, -1000};

    public static class threads extends Thread {    //Threads to house infinite loops
        static boolean unstoppable = true;

        public static void executeFocus(JFrame frame) {     //All JFrame related loops
            Thread one = new Thread() {
                public void run() {
                    while (unstoppable) {
                        if (mouseClickedFocus) {     //Mouse clicks away from JTextbox -> unfocus JTextbox (for keyPressedListener)
                            frame.requestFocus();
                            try {
                                Thread.sleep(100);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        try {
                            Thread.sleep(10);
                        } catch (InterruptedException ignore) {
                        }
                    }
                }
            };
            one.start();
        }

        public static void executeRepaintAndClear(JLayeredPane lp, JLabel jl) {    //All JLayeredPane related loops
            Thread one = new Thread() {
                public void run() {
                    while (unstoppable) {
                        lp.moveToBack(jl);
                        if (draw.redrawCircle || draw.redrawLine || draw.redrawCurve) {    //Check if redraw() called--Lets me call elsewhere without JLayeredPane parameter
                            draw.showAllCirclesAndLines(lp);
                            try {
                                Thread.sleep(100);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        try {
                            Thread.sleep(50);
                        } catch (InterruptedException ignore) {
                        }
                    }
                }
            };
            one.start();
        }

        public static void selectedListener(JComboBox cbLine, JComboBox cbMotor) {    //All JLayeredPane related loops
            Thread one = new Thread() {
                public void run() {
                    boolean oneTimeRun = false;
                    while (unstoppable) {
                        try {
                            if (selected && !oneTimeRun) {
                                prevSelectedLine = cbLine.getSelectedIndex();
                                prevSelectedMotor = cbMotor.getSelectedIndex();
                                System.out.println(selectedCircle[0] + " " + selectedCircle[1]);
                                if (!(lineSettingsAndParameters.get(0).get(circles.indexOf(selectedCircle[0]) / 3 - 1).substring(0, 1).toUpperCase() +
                                        lineSettingsAndParameters.get(0).get(circles.indexOf(selectedCircle[0]) / 3 - 1).substring(1)).contains("Spline"))
                                    cbLine.setSelectedItem(lineSettingsAndParameters.get(0).get(circles.indexOf(selectedCircle[0]) / 3 - 1).substring(0, 1).toUpperCase() +
                                            lineSettingsAndParameters.get(0).get(circles.indexOf(selectedCircle[0]) / 3 - 1).substring(1));
                                else
                                    cbLine.setSelectedItem(lineSettingsAndParameters.get(0).get(circles.indexOf(selectedCircle[0]) / 3 - 1).substring(0, 1).toUpperCase() +
                                            lineSettingsAndParameters.get(0).get(circles.indexOf(selectedCircle[0]) / 3 - 1).substring(1) + " To");
                                int i = 0;
                                while (cbMotor.getItemCount() > i) {
                                    if (cbMotor.getItemAt(i).toString().contains(motorNames.get(circles.indexOf(selectedCircle[0]) / 3 - 1))) {
                                        cbMotor.setSelectedIndex(i);
                                        System.out.println(i);
                                    }
                                    i += 1;
                                }
                                oneTimeRun = true;
                            } else if (!selected && oneTimeRun) {
                                cbLine.setSelectedIndex(prevSelectedLine);
                                cbMotor.setSelectedIndex(prevSelectedMotor);
                                oneTimeRun = false;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        try {
                            Thread.sleep(50);
                        } catch (InterruptedException ignore) {
                        }
                    }
                }
            };
            one.start();
        }
    }

    public MainActivity() {
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(1300, 750));
        BufferedImage image = null;
        ArrayList<String> name = new ArrayList<>();
        ArrayList<String> types = new ArrayList<>();
        name.add("WheelDcMotors");
        name.add("ArmDcMotor");
        name.add("SmallServoArm");
        types.add("DCWheel");
        types.add("DCArm");
        types.add("Servo");
        MotorSetup.exportMotors(name, types);
        try {
            image = ImageIO.read(new File("res/images/ruckus_field_lines.png"));  //Import FTC Field Image
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
        Image newImage = image.getScaledInstance(750, 750, java.awt.Image.SCALE_SMOOTH);  //Scale up the image in size
        ImageIcon imageIcon = new ImageIcon(newImage);
        JLabel jLabel = new JLabel();
        jLabel.setIcon(imageIcon);
        jLabel.setBounds(0, 0, 750, 750);

        JLabel l1 = new JLabel("Drive Options:");  //Labels
        l1.setFont(l1.getFont().deriveFont(15f));
        l1.setBounds(850, 50, 200, 30);
        l1.setFocusable(false);

        JLabel l2 = new JLabel("Motor Options:");
        l2.setFont(l1.getFont().deriveFont(15f));
        l2.setBounds(1100, 50, 200, 30);
        l2.setFocusable(false);

        JComboBox jComboBox1 = new JComboBox();  //Dropdown box creator for Drive Options
        jComboBox1.addItem("[Select]");  //Dropdown options for Drive Options
        jComboBox1.addItem("Line");
        jComboBox1.addItem("Curve");
        jComboBox1.addItem("Strafe");
        jComboBox1.addItem("Reverse");
        jComboBox1.addItem("Spline To");
        jComboBox1.setFont(jComboBox1.getFont().deriveFont(13f));
        jComboBox1.addItemListener(new ItemChangeListener());
        jComboBox1.setBounds(800, 100, 200, 30);
        jComboBox1.setFocusable(false);

        JComboBox jComboBox2 = new JComboBox();  //Dropdown box creator for Motor Options
        jComboBox2.addItem("[Select]");  //Dropdown options for Motor Options
        cmdLine.debugger.dispVar("motors", MotorSetup.importMotors().get(0), 0, "N/A");
        if (!MotorSetup.importMotors().get(0).get(0).contains("Error")) {     //Imports list of motors & motor types
            int i = 0;
            while (MotorSetup.importMotors().size() >= i) {
                jComboBox2.addItem(MotorSetup.importMotors().get(0).get(i) + " (" + MotorSetup.importMotors().get(1).get(i) + ")");
                i += 1;
            }
        }
        jComboBox2.setFont(jComboBox1.getFont().deriveFont(13f));
        jComboBox2.addItemListener(new ItemChangeListener2());
        jComboBox2.setBounds(1050, 100, 200, 30);
        jComboBox2.setFocusable(false);

        JButton button = new JButton("Get Path Data");  //"Get Path Data" button
        button.setBounds(925, 200, 200, 50);
        button.setFocusable(false);

        JButton saveButton = new JButton("Save Path");  //Creates a .path folder to save path data (only compatible with GUI)
        saveButton.setBounds(1100, 400, 100, 35);
        saveButton.setFocusable(false);

        JButton openPathButton = new JButton("Open Path");  //Opens and reads .path folder data
        openPathButton.setBounds(850, 400, 100, 35);
        openPathButton.setFocusable(false);

        JTextField tf = new JTextField();  //Output Text Field
        tf.setBounds(775, 325, 500, 30);
        tf.setFont(tf.getFont().deriveFont(13f));
        tf.setFocusable(true);

        layeredPane.moveToBack(jLabel);

        button.addActionListener(new ActionListener() {  //Button onClickListener
            @Override
            public void actionPerformed(ActionEvent arg0) {     //Encodes TrajectoryBuilder, Motor Names, and Motor locations in Base64
                try {
                    TrajBuilderWrapper driveTraj = new TrajBuilderWrapper(FromAndToPose2D.pointsToPose2d(circles,
                            0, 1, 3), lineSettingsAndParameters.get(0),
                            DriverConstraintsWrapper.getDriveConstraints(), pathName);
                    String motors = objectMapper.writeValueAsString(motorNames);
                    String moveMotorLocation = objectMapper.writeValueAsString(motorExecutedLocation);
                    String trajectory = objectMapper.writeValueAsString(driveTraj);
                    String encodedTraj = Base64.getEncoder().encodeToString(trajectory.getBytes());
                    String encodedLoc = Base64.getEncoder().encodeToString(moveMotorLocation.getBytes());
                    String encodedMotors = Base64.getEncoder().encodeToString(motors.getBytes());
                    tf.setText("TRAJ:" + encodedTraj + ",MOTORS;" + encodedMotors + ",LOCATION-" + encodedLoc);
                } catch (Exception e) {
                    System.err.println(e.toString());
                }
            }
        });
        openPathButton.addActionListener(new ActionListener() {  //Open Button onClickListener
            @Override
            public void actionPerformed(ActionEvent arg0) {
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
                    if (String.valueOf(fileToRead).contains(".path")) {
                        currentlySelected = String.valueOf(fileToRead).substring(String.valueOf(fileToRead).lastIndexOf("\\") + 1,
                                String.valueOf(fileToRead).indexOf("."));
                    }
                    ZipAndUnzip.unzipFolder(String.valueOf(fileToRead), String.valueOf(fileChooser.getCurrentDirectory()));
                    String name = String.valueOf(fileToRead).substring(String.valueOf(fileToRead).lastIndexOf("\\") + 1,
                            String.valueOf(fileToRead).indexOf("."));
                    pathName = name;
                    String cirDir = fileChooser.getCurrentDirectory() + "\\" + name + "Circles.cir";    //Sets .cir file containing serialized circle array as current directory
                    String lineDir = fileChooser.getCurrentDirectory() + "\\" + name + "Traj.line";     //Sets .line file containing serialized line array as current directory
                    String trajDir = fileChooser.getCurrentDirectory() + "\\" + name + "Json.traj";
                    draw.setDimension(15, 15);
                    draw.backgroundTransparent(true);
                    draw.visibility(true);
                    circles = SerializeAndDeserialize.deserialize(cirDir, false);    //Gets deserialized ArrayList and defines variable
                    lineSettingsAndParameters = SerializeAndDeserialize.deserialize(lineDir, true);  //Deserializes the arrays (see SerializeAndDeserialize class)

                    String trajectory = SerializeAndDeserialize.readJson(trajDir);
                    String motorString = trajectory.substring(trajectory.indexOf(";") + 1, trajectory.lastIndexOf("-") - 9);
                    motorString = motorString.replace("[\"", "");
                    motorString = motorString.replace("\"]", "");
                    motorNames = new ArrayList<>(Arrays.asList(motorString.split("\",\"")));

                    String motorIndexes = trajectory.substring(trajectory.lastIndexOf("-") + 1);
                    motorIndexes = motorIndexes.replace("[", "");
                    motorIndexes = motorIndexes.replace("]", "");
                    motorExecutedLocation = ArrayListConverters.stringArrayToIntArray(
                            new ArrayList<>(Arrays.asList(motorIndexes.split(","))));

                    settings = lineSettingsAndParameters.get(0);
                    params1 = lineSettingsAndParameters.get(1);
                    params2 = lineSettingsAndParameters.get(2);
                    params3 = lineSettingsAndParameters.get(3);
                    ZipAndUnzip.deleteAndOrRename(cirDir, "", "", true, false);
                    ZipAndUnzip.deleteAndOrRename(trajDir, "", "", true, false);
                    ZipAndUnzip.deleteAndOrRename(lineDir, "", "", true, false);    //Deletes temporary files
                    prevFilePath = fileChooser.getCurrentDirectory();
                } else if (result == JFileChooser.CANCEL_OPTION) {      //If cancel button is pressed
                    prevFilePath = fileChooser.getCurrentDirectory();
                } else if (result == JFileChooser.ERROR_OPTION) {       //If X button is pressed
                    prevFilePath = fileChooser.getCurrentDirectory();
                }
            }
        });
        saveButton.addActionListener(new ActionListener() {  //Save Button onClickListener
            @Override
            public void actionPerformed(ActionEvent arg0) {
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
                String fileNoExt = "";
                int userSelection = fileChooser.showSaveDialog(layeredPane);
                if (userSelection == JFileChooser.APPROVE_OPTION) {     //Save Button is pressed
                    File f = fileChooser.getSelectedFile();
                    pathName = String.valueOf(f).substring(String.valueOf(f).lastIndexOf("\\") + 1,
                            String.valueOf(f).indexOf("."));
                    if (!String.valueOf(f).contains(".path")) {   //If file name does not contain .path
                        int dialogButton = JOptionPane.ERROR_MESSAGE;   //Error message pops up
                        JOptionPane.showMessageDialog(null, "Error: File must contain '.path' extension", "Error", dialogButton);
                    } else if (f.exists()) {  //If file name already exists
                        int dialogButton2 = JOptionPane.YES_NO_OPTION;
                        int dialogResult = JOptionPane.showConfirmDialog(null,
                                "File Already Exists! Do you want to Replace?", "File Exists", dialogButton2);    //Ask if user wants to replace file
                        if (dialogResult == JOptionPane.YES_OPTION) {
                            f.setExecutable(false);
                            f.setReadable(true);
                            f.setWritable(true);
                            f.delete();
                            currentlySelected = String.valueOf(f);
                            File fileToSave = fileChooser.getSelectedFile();    //Selects the duplicate file, deletes it, and create new files
                            fileChooser.setCurrentDirectory(fileChooser.getSelectedFile());
                            if (String.valueOf(fileToSave).contains(".path")) {
                                fileNoExt = String.valueOf(fileToSave).substring(String.valueOf(fileToSave).lastIndexOf("\\") + 1,
                                        String.valueOf(fileToSave).indexOf("."));
                            }
                            prevFilePath = fileChooser.getCurrentDirectory();
                            String motors = "";
                            String loc = "";
                            String traj = "";
                            try {
                                TrajBuilderWrapper driveTraj = new TrajBuilderWrapper(FromAndToPose2D.pointsToPose2d(circles,
                                        0, 1, 3), lineSettingsAndParameters.get(0),
                                        DriverConstraintsWrapper.getDriveConstraints(), pathName);
                                motors = objectMapper.writeValueAsString(motorNames);
                                loc = objectMapper.writeValueAsString(motorExecutedLocation);
                                traj = objectMapper.writeValueAsString(driveTraj);
                            } catch (Exception e) {
                            }
                            SerializeAndDeserialize.serialize(circles, lineSettingsAndParameters, String.valueOf(fileToSave),
                                    fileNoExt, traj, loc, motors);
                            ZipAndUnzip.zipFolder(fileToSave.getAbsolutePath(), fileNoExt);
                        }
                    } else {    //If not a duplicate file name and contains .path ext in the name
                        File fileToSave = fileChooser.getSelectedFile();    //Saves the files
                        if (String.valueOf(fileToSave).contains(".path")) {
                            fileNoExt = String.valueOf(fileToSave).substring(String.valueOf(fileToSave).lastIndexOf("\\") + 1,
                                    String.valueOf(fileToSave).indexOf("."));
                        }
                        prevFilePath = fileChooser.getCurrentDirectory();
                        currentlySelected = fileNoExt;
                        String motors = "";
                        String loc = "";
                        String traj = "";
                        try {
                            System.out.println(lineSettingsAndParameters);
                            System.out.println(circles);
                            TrajBuilderWrapper driveTraj = new TrajBuilderWrapper(FromAndToPose2D.pointsToPose2d(circles,
                                    0, 1, 3), lineSettingsAndParameters.get(0),
                                    DriverConstraintsWrapper.getDriveConstraints(), pathName);
                            motors = objectMapper.writeValueAsString(motorNames);
                            loc = objectMapper.writeValueAsString(motorExecutedLocation);
                            traj = objectMapper.writeValueAsString(driveTraj);
                        } catch (Exception e) {
                        }
                        SerializeAndDeserialize.serialize(circles, lineSettingsAndParameters, String.valueOf(fileToSave),
                                fileNoExt, traj, loc, motors);

                        ZipAndUnzip.zipFolder(fileToSave.getAbsolutePath(), fileNoExt);
                    }
                } else if (userSelection == JFileChooser.CANCEL_OPTION) {   //Cancel button is pressed
                    prevFilePath = fileChooser.getCurrentDirectory();
                } else if (userSelection == JFileChooser.ERROR_OPTION) {    // X button is pressed
                    prevFilePath = fileChooser.getCurrentDirectory();
                }
            }
        });

        draw.backgroundTransparent(true);  //Change settings of the Dot (circle)
        draw.addOrSetColor("Yellow", new String[]{"Add"});
        draw.visibility(true);

        layeredPane.add(openPathButton, 10, 0);
        layeredPane.add(saveButton, 9, 0);
        layeredPane.add(l1, 8, 0);  //Add all components to layeredPane and set overlap sequence
        layeredPane.add(l2, 7, 0);
        layeredPane.add(jComboBox2, 6, 0);
        layeredPane.add(jLabel, 4, 0);
        layeredPane.add(jComboBox1, 3, 0);
        layeredPane.add(button, 2, 0);
        layeredPane.add(tf, 1, 0);

        draw.showAllCirclesAndLines(layeredPane);   //Draws invisible circle--Allows us to access + change paintComponent after runtime

        threads.executeRepaintAndClear(layeredPane, jLabel);    //See "threads" class

        threads.selectedListener(jComboBox1, jComboBox2);
        circles.clear();    //Reset ArrayList of circles

        add(layeredPane);       //Put layeredPane in MainActivity()

        draw.redraw();
    }

    static class ItemChangeListener implements ItemListener {
        @Override
        public void itemStateChanged(ItemEvent event) {  //Which dropdown option is selected for Drive Options?
            if (event.getStateChange() == ItemEvent.SELECTED) {
                Object item = event.getItem();
                if (String.valueOf(item).equals("Line")) {      //Robot moves in straight line
                    line = true;
                    select = false;
                    curve = false;
                    strafe = false;
                    reverse = false;
                    spline = false;
                    if (selected) {
                        lineSettingsAndParameters.get(0).set(circles.indexOf(selectedCircle[0]) / 3 - 1, "line");
                        circles.set(circles.indexOf(selectedCircle[0]) + 2 ,draw.getColorIndex("Red"));
                    }
                } else if (String.valueOf(item).equals("Curve")) {      //Robot turns to a location
                    curve = true;
                    line = false;
                    select = false;
                    strafe = false;
                    reverse = false;
                    spline = false;
                    if (selected) {
                        lineSettingsAndParameters.get(0).set(circles.indexOf(selectedCircle[0]) / 3 - 1, "curve");
                        circles.set(circles.indexOf(selectedCircle[0]) + 2 ,draw.getColorIndex("Magenta"));
                    }
                } else if (String.valueOf(item).equals("[Select]")) {
                    select = true;
                    line = false;
                    curve = false;
                    strafe = false;
                    reverse = false;
                    spline = false;
                } else if (String.valueOf(item).equals("Reverse")) {     //Robot backs up to a location
                    line = false;
                    select = false;
                    curve = false;
                    strafe = false;
                    reverse = true;
                    spline = false;
                    if (selected) {
                        lineSettingsAndParameters.get(0).set(circles.indexOf(selectedCircle[0]) / 3 - 1, "reverse");
                        circles.set(circles.indexOf(selectedCircle[0]) + 2 ,draw.getColorIndex("Cyan"));
                    }
                } else if (String.valueOf(item).equals("Strafe")) {       //Robot strafes to a location (mechinum wheels only)
                    line = false;
                    select = false;
                    curve = false;
                    strafe = true;
                    reverse = false;
                    spline = false;
                    if (selected) {
                        lineSettingsAndParameters.get(0).set(circles.indexOf(selectedCircle[0]) / 3 - 1, "strafe");
                        circles.set(circles.indexOf(selectedCircle[0]) + 2 ,draw.getColorIndex("Green"));
                    }
                } else if (String.valueOf(item).equals("Spline To")) {
                    spline = true;
                    line = false;
                    select = false;
                    curve = false;
                    strafe = false;
                    reverse = false;
                    if (selected) {
                        lineSettingsAndParameters.get(0).set(circles.indexOf(selectedCircle[0]) / 3 - 1, "spline");
                        circles.set(circles.indexOf(selectedCircle[0]) + 2 ,draw.getColorIndex("Pink"));
                    }
                }
            }
        }
    }

    static class ItemChangeListener2 implements ItemListener {
        @Override
        public void itemStateChanged(ItemEvent event) {  //Which dropdown option is selected for Motor Options?
            if (event.getStateChange() == ItemEvent.SELECTED) {
                Object item = event.getItem();
                if (String.valueOf(item).equals("[Select]")) {
                    currentlySelected = "[Select]";
                }
                if (!MotorSetup.importMotors().get(0).get(0).contains("Error")) {
                    int i = 0;
                    while (MotorSetup.importMotors().get(0).size() > i) {      //Gets which motor is selected
                        if (String.valueOf(item).contains(MotorSetup.importMotors().get(0).get(i))) {
                            currentlySelected = MotorSetup.importMotors().get(0).get(i);
                            if (selected) {
                                motorNames.set(circles.indexOf(selectedCircle[0]) / 3 - 1, currentlySelected);
                            }
                        }
                        i += 1;
                    }
                }
            }
        }
    }

    public static class mouse implements MouseMotionListener, MouseListener {   //Detect mouse location on picture (to draw points)
        public static void initListener(JFrame lp) {
            lp.addMouseMotionListener(new mouse());
            lp.addMouseListener(new mouse());
        }

        public void mouseClicked(MouseEvent evt) {
            z2 = 0;
            v2 = 0;
            if (!circles.isEmpty()) {
                while (z2 < circles.size() / 3) {
                    if (circles.get(v2) + 15 >= mouseX && circles.get(v2) - 15 <= mouseX &&
                            circles.get(v2 + 1) + 15 >= mouseY && circles.get(v2 + 1) - 15 <= mouseY) {
                        if (selected) {
                            selected = false;
                            selectedCircle[0] = -1000;
                            selectedCircle[1] = -1000;
                            selectedCircle[2] = -1000;
                            break;
                        } else if (!selected) {
                            selected = true;
                            selectedCircle[0] = circles.get(v2);
                            selectedCircle[1] = circles.get(v2 + 1);
                            selectedCircle[2] = circles.get(v2 + 2);
                            break;
                        }
                    }
                    z2 += 1;
                    v2 += 3;
                }
            }
        }

        public void mouseEntered(MouseEvent evt) {  //Mouse entered window
            mouseExited = false;
        }

        public void mouseExited(MouseEvent evt) {  //Mouse exited window
            mouseExited = true;
        }

        public void mousePressed(MouseEvent evt) {  //Mouse is pressed down -> Check if clicking on circle
            mouseClickedFocus = true;
            z = 0;
            v = 0;
            if (!circles.isEmpty()) {
                while (z < circles.size() / 3) {
                    if (circles.get(v) + 15 >= mouseX && circles.get(v) - 15 <= mouseX &&
                            circles.get(v + 1) + 15 >= mouseY && circles.get(v + 1) - 15 <= mouseY) {
                        mouseClicked = true;
                        break;
                    }
                    z = z + 1;
                    v = v + 3;
                }
            }
        }

        public void mouseReleased(MouseEvent evt) {   //Mouse released -> Draw circle at new location + remove old one
            mouseClickedFocus = false;
            if (mouseClicked && mouseX + xOffset <= 735 && mouseY + yOffset <= 735 &&
                    mouseX + xOffset >= 0 && mouseY + yOffset >= 0) {
                clearX = circles.get(v) + xOffset;
                clearY = circles.get(v + 1) + yOffset;
                mouseClicked = false;
                circles.set(v, mouseX);
                circles.set(v + 1, mouseY);
                selectedCircle[0] = circles.get(v);
                selectedCircle[1] = circles.get(v + 1);
                draw.addOrSetColor(draw.getColor(circles.get(v + 2)), new String[]{"Set", String.valueOf(v + 2)});
                selectedCircle[2] = circles.get(v + 2);
                try {
                    params1.set(v / 3 * 2, String.valueOf(circles.get(v)));
                    params1.set(v / 3 * 2 + 1, String.valueOf(circles.get(v + 1)));
                } catch (Exception e) {
                }
                try {
                    params3.set(v / 3 * 2 - 2, String.valueOf(circles.get(v)));
                    params3.set(v / 3 * 2 - 1, String.valueOf(circles.get(v + 1)));
                } catch (Exception e) {
                }
                lineSettingsAndParameters.clear();
                lineSettingsAndParameters.add(settings);
                lineSettingsAndParameters.add(params1);
                lineSettingsAndParameters.add(params2);
                lineSettingsAndParameters.add(params3);
            }
            v = 0;
            z = 0;
        }

        public void mouseDragged(MouseEvent evt) {
            if (SwingUtilities.isLeftMouseButton(evt)) {  //Left click is held down and mouse is moved
                mouseX = evt.getX();
                mouseY = evt.getY();
                //System.out.println("mouseX = " + mouseX);
                //System.out.println("mouseY = " + mouseY);
            }
        }

        public void mouseMoved(MouseEvent evt) {  //Mouse is moved
            mouseX = evt.getX();
            mouseY = evt.getY();
            //System.out.println("mouseX = " + mouseX);
            //System.out.println("mouseY = " + mouseY);
        }

        public void actionPerformed(ActionEvent e) {
        }
    }

    public static class key implements KeyListener {  //Keyboard listener (which key is pressed?)
        public static void initListener(JFrame lp) {
            lp.addKeyListener(new key());
        }

        @Override
        public void keyPressed(KeyEvent e)  //Key is pressed
        {
            if (e.getKeyCode() == KeyEvent.VK_N) {  //If N is pressed
                nPressed = true;
                gPressed = false;
                if (!mouseExited && mouseX + xOffset <= 735 && mouseY + yOffset <= 735 &&
                        mouseX + xOffset >= 0 && mouseY + yOffset >= 0 && !select &&
                        !currentlySelected.contains("Select")) {  //Checks if mouse is in the screen & in field image
                    motorNames.add(currentlySelected);
                    if (!currentlySelected.contains("DCWheel")) {
                        motorExecutedLocation.add(circles.size() / 3);
                    }
                    circles.add(mouseX);
                    circles.add(mouseY);
                    draw.setDimension(15, 15);
                    draw.backgroundTransparent(true);
                    draw.visibility(true);
                    if (line)
                        draw.addOrSetColor("Red", new String[]{"Add"});
                    else if (curve)
                        draw.addOrSetColor("Magenta", new String[]{"Add"});
                    else if (reverse)
                        draw.addOrSetColor("Cyan", new String[]{"Add"});
                    else if (strafe)
                        draw.addOrSetColor("Green", new String[]{"Add"});
                    else if (spline)
                        draw.addOrSetColor("Pink", new String[]{"Add"});
                }
            }
        }

        @Override
        public void keyReleased(KeyEvent e)  //Key is released
        {
            if (e.getKeyCode() == KeyEvent.VK_N) {  //If N is released
                nPressed = false;
                alreadyAdded = false;
                gPressed = false;
            } else if (e.getKeyCode() == KeyEvent.VK_G) {  //If G is released
                gPressed = false;
                alreadyAdded = false;
                nPressed = false;
            } else if (e.getKeyCode() == KeyEvent.VK_DELETE && selected) {
                if (circles.indexOf(selectedCircle[0]) > 2) {
                    circles.remove(circles.indexOf(selectedCircle[1]) + 1);
                    circles.remove(circles.indexOf(selectedCircle[0]));
                    circles.remove(circles.indexOf(selectedCircle[1]));
                    ArrayList<String> temp = params1;
                    temp.add(params3.get(params3.size() - 2));
                    temp.add(params3.get(params3.size() - 1));
                    if (settings.size() > 1)
                        settings.remove(temp.indexOf(String.valueOf(selectedCircle[0])) / 2 - 1);
                    else
                        settings.remove(0);
                    params1.remove(params1.indexOf(String.valueOf(selectedCircle[0])));
                    params1.remove(params1.indexOf(String.valueOf(selectedCircle[1])));
                    params3.remove(params3.indexOf(String.valueOf(selectedCircle[0])));
                    params3.remove(params3.indexOf(String.valueOf(selectedCircle[1])));
                    params2.remove(params2.size() - 1);
                    params2.remove(params2.size() - 1);

                    selected = false;
                    selectedCircle[0] = -1000;
                    selectedCircle[1] = -1000;
                    selectedCircle[2] = -1000;
                }
            }
        }

        @Override
        public void keyTyped(KeyEvent e) {
        }
    }


    private void createUIComponents() {
        // TODO: place custom component creation code here
    }

    public static class draw extends JPanel {  //Draw a dot
        static int wid = 0;
        static int hei = 0;
        static boolean opaque = true;
        public static JPanel paintPanel;
        static boolean redrawCircle = false;
        static boolean redrawLine = false;
        static boolean redrawCurve = false;
        static int loopStopper = 0;
        static int componentChecker = 0;
        static boolean isVisible = false;
        static int numOfIndexesRun = 0;
        static int numTimes2Run = 0;

        public static void setDimension(int width, int height) {        //Set width and height of circle
            wid = width;
            hei = height;
        }

        public static void visibility(boolean visible) {  //Change visibility of circle
            isVisible = visible;
        }       //Changes circle's visibility

        public static void backgroundTransparent(boolean transparent) {  //Change Opaque value
            if (transparent) {
                opaque = false;
            } else {
                opaque = true;
            }
        }

        public static void redraw() {  //Repaints the dot
            redrawCircle = true;
        }       //Allows paintComponent method to refresh after runtime

        public static int getColorIndex(String color) {  //Adds color choice to ArrayList of circles (Once again not very efficient)
            if (color.equalsIgnoreCase("white")) {
                return 0;
            } else if (color.equalsIgnoreCase("light gray")) {
                return 1;
            } else if (color.equalsIgnoreCase("gray")) {
                return 2;
            } else if (color.equalsIgnoreCase("dark gray")) {
                return 3;
            } else if (color.equalsIgnoreCase("black")) {
                return 4;
            } else if (color.equalsIgnoreCase("red")) {
                return 5;
            } else if (color.equalsIgnoreCase("pink")) {
                return 6;
            } else if (color.equalsIgnoreCase("orange")) {
                return 7;
            } else if (color.equalsIgnoreCase("yellow")) {
                return 8;
            } else if (color.equalsIgnoreCase("magenta")) {
                return 9;
            } else if (color.equalsIgnoreCase("cyan")) {
                return 10;
            } else if (color.equalsIgnoreCase("blue")) {
                return 11;
            } else if (color.equalsIgnoreCase("green")) {
                return 12;
            } else {
                return -1;
            }
        }

        public static String getColor(int index) {
            String result = "error";
            switch (index) {
                case 0:
                    result = "white";
                    break;
                case 1:
                    result = "light gray";
                    break;
                case 2:
                    result = "gray";
                    break;
                case 3:
                    result = "dark gray";
                    break;
                case 4:
                    result = "black";
                    break;
                case 5:
                    result = "red";
                    break;
                case 6:
                    result = "pink";
                    break;
                case 7:
                    result = "orange";
                    break;
                case 8:
                    result = "yellow";
                    break;
                case 9:
                    result = "magenta";
                    break;
                case 10:
                    result = "cyan";
                    break;
                case 11:
                    result = "blue";
                    break;
                case 12:
                    result = "green";
                    break;
            }
            return result;
        }

        public static void addOrSetColor(String color, String[] setColor) {
            if (color.equalsIgnoreCase("white")) {
                if (setColor[0].equalsIgnoreCase("add")) {
                    circles.add(0);
                } else if (setColor[0].equalsIgnoreCase("set")) {
                    circles.set((Integer.valueOf(setColor[1])), 0);
                }
            } else if (color.equalsIgnoreCase("light gray")) {
                if (setColor[0].equalsIgnoreCase("add")) {
                    circles.add(1);
                } else if (setColor[0].equalsIgnoreCase("set")) {
                    circles.set((Integer.valueOf(setColor[1])), 1);
                }
            } else if (color.equalsIgnoreCase("gray")) {
                if (setColor[0].equalsIgnoreCase("add")) {
                    circles.add(2);
                } else if (setColor[0].equalsIgnoreCase("set")) {
                    circles.set((Integer.valueOf(setColor[1])), 2);
                }
                ;
            } else if (color.equalsIgnoreCase("dark gray")) {
                if (setColor[0].equalsIgnoreCase("add")) {
                    circles.add(3);
                } else if (setColor[0].equalsIgnoreCase("set")) {
                    circles.set((Integer.valueOf(setColor[1])), 3);
                }
            } else if (color.equalsIgnoreCase("black")) {
                if (setColor[0].equalsIgnoreCase("add")) {
                    circles.add(4);
                } else if (setColor[0].equalsIgnoreCase("set")) {
                    circles.set((Integer.valueOf(setColor[1])), 4);
                }
            } else if (color.equalsIgnoreCase("red")) {
                if (setColor[0].equalsIgnoreCase("add")) {
                    circles.add(5);
                } else if (setColor[0].equalsIgnoreCase("set")) {
                    circles.set((Integer.valueOf(setColor[1])), 5);
                }
            } else if (color.equalsIgnoreCase("pink")) {
                if (setColor[0].equalsIgnoreCase("add")) {
                    circles.add(6);
                } else if (setColor[0].equalsIgnoreCase("set")) {
                    circles.set((Integer.valueOf(setColor[1])), 6);
                }
            } else if (color.equalsIgnoreCase("orange")) {
                if (setColor[0].equalsIgnoreCase("add")) {
                    circles.add(7);
                } else if (setColor[0].equalsIgnoreCase("set")) {
                    circles.set((Integer.valueOf(setColor[1])), 7);
                }
            } else if (color.equalsIgnoreCase("yellow")) {
                if (setColor[0].equalsIgnoreCase("add")) {
                    circles.add(8);
                } else if (setColor[0].equalsIgnoreCase("set")) {
                    circles.set((Integer.valueOf(setColor[1])), 8);
                }
            } else if (color.equalsIgnoreCase("magenta")) {
                if (setColor[0].equalsIgnoreCase("add")) {
                    circles.add(9);
                } else if (setColor[0].equalsIgnoreCase("set")) {
                    circles.set((Integer.valueOf(setColor[1])), 9);
                }
            } else if (color.equalsIgnoreCase("cyan")) {
                if (setColor[0].equalsIgnoreCase("add")) {
                    circles.add(10);
                } else if (setColor[0].equalsIgnoreCase("set")) {
                    circles.set((Integer.valueOf(setColor[1])), 10);
                }
            } else if (color.equalsIgnoreCase("blue")) {
                if (setColor[0].equalsIgnoreCase("add")) {
                    circles.add(11);
                } else if (setColor[0].equalsIgnoreCase("set")) {
                    circles.set((Integer.valueOf(setColor[1])), 11);
                }
            } else if (color.equalsIgnoreCase("green")) {
                if (setColor[0].equalsIgnoreCase("add")) {
                    circles.add(12);
                } else if (setColor[0].equalsIgnoreCase("set")) {
                    circles.set((Integer.valueOf(setColor[1])), 12);
                }
            }
        }

        public static void showAllCirclesAndLines(JLayeredPane lp) {
            loopStopper = 0;
            componentChecker = 0;
            numOfIndexesRun = 0;
            numTimes2Run = 0;
            lineSettingsAndParameters.clear();
            paintPanel = new JPanel() {  //Sets paintComponent as JPanel -> JPanel then set on layout
                @Override
                public void paintComponent(Graphics g) {  //Draws circle over JPanel
                    super.paintComponent(g);
                    paintPanel.setOpaque(opaque);
                    paintPanel.setVisible(isVisible);

                    g.setColor(Color.RED);
                    g.fillOval(800, 500, 15, 15);
                    g.setColor(Color.BLACK);
                    g.drawString(" = Line", 817, 512);

                    g.setColor(Color.MAGENTA);
                    g.fillOval(800, 530, 15, 15);
                    g.setColor(Color.BLACK);
                    g.drawString(" = Curve", 817, 542);

                    g.setColor(Color.CYAN);
                    g.fillOval(800, 560, 15, 15);
                    g.setColor(Color.BLACK);
                    g.drawString(" = Reverse", 817, 572);

                    g.setColor(Color.GREEN);
                    g.fillOval(800, 590, 15, 15);
                    g.setColor(Color.BLACK);
                    g.drawString(" = Strafe", 817, 602);

                    g.setColor(Color.PINK);
                    g.fillOval(800, 620, 15, 15);
                    g.setColor(Color.BLACK);
                    g.drawString(" = Spline", 817, 632);

                    g.setColor(Color.ORANGE);
                    g.fillOval(800, 650, 15, 15);
                    g.setColor(Color.BLACK);
                    g.drawString(" = Arm/Servo", 817, 662);

                    numOfIndexesRun = 0;
                    componentChecker = 0;
                    try {
                        if ((settings.isEmpty() && circles.size() / 3 == 2) ||
                                (settings.size() < circles.size() / 3 - 1 && circles.size() / 3 >= 2)) {
                            lineSettingsAndParameters.clear();

                            if (line)
                                settings.add(getCurrentSetting(0));
                            else if (curve)
                                settings.add(getCurrentSetting(1));
                            else if (reverse)
                                settings.add(getCurrentSetting(2));
                            else if (strafe)
                                settings.add(getCurrentSetting(3));
                            else if (spline)
                                settings.add(getCurrentSetting(4));
                            params1.add(String.valueOf(circles.get(circles.size() - 6)));
                            params1.add(String.valueOf(circles.get(circles.size() - 5)));
                            params2.add("N/A");
                            params2.add("N/A");
                            params3.add(String.valueOf(circles.get(circles.size() - 3)));
                            params3.add(String.valueOf(circles.get(circles.size() - 2)));

                            lineSettingsAndParameters.add(settings);
                            lineSettingsAndParameters.add(params1);
                            lineSettingsAndParameters.add(params2);
                            lineSettingsAndParameters.add(params3);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    ArrayList<ArrayList<Integer>> points2draw = LineArrayProcessor.polyLineList(lineSettingsAndParameters, false);
                    if (points2draw.get(0).get(0) != -1 && points2draw.get(0).get(0) != -2) {
                        numOfIndexesRun = 0;
                        componentChecker = 0;
                        Graphics2D g2d = (Graphics2D) g;
                        if (points2draw.get(points2draw.size() - 1).contains(0)) {
                            while (points2draw.size() - 1 > numOfIndexesRun) {
                                if (points2draw.get(points2draw.size() - 1).get(numOfIndexesRun) == 0) {
                                    try {
                                        g2d.setColor(Color.BLACK);
                                        QuadCurve2D curvedLine = new QuadCurve2D.Double();
                                        int midX;
                                        int midY;
                                        if (!points2draw.isEmpty() && (points2draw.get(0).size() / 2 > 1 || points2draw.size() > 1)) {
                                            if (points2draw.get(numOfIndexesRun - 1).get(points2draw.get(numOfIndexesRun - 1).size() - 2) <
                                                    points2draw.get(numOfIndexesRun).get(points2draw.get(numOfIndexesRun).size() - 2) &&
                                                    points2draw.get(numOfIndexesRun - 1).get(points2draw.get(numOfIndexesRun - 1).size() - 1) >=
                                                            points2draw.get(numOfIndexesRun).get(points2draw.get(numOfIndexesRun).size() - 1)) {
                                                midX = ((points2draw.get(numOfIndexesRun - 1).get(points2draw.get(numOfIndexesRun - 1).size() - 2) + xOffset) -
                                                        (points2draw.get(numOfIndexesRun).get(points2draw.get(numOfIndexesRun).size() - 2) + xOffset)) / 2 +
                                                        (points2draw.get(numOfIndexesRun).get(points2draw.get(numOfIndexesRun).size() - 2) + xOffset) +
                                                        ((points2draw.get(numOfIndexesRun).get(points2draw.get(numOfIndexesRun).size() - 2) + xOffset) -
                                                                (points2draw.get(numOfIndexesRun - 1).get(points2draw.get(numOfIndexesRun - 1).size() - 2) + xOffset)) * 2 / 3;
                                                midY = ((points2draw.get(numOfIndexesRun - 1).get(points2draw.get(numOfIndexesRun - 1).size() - 1) + yOffset) -
                                                        (points2draw.get(numOfIndexesRun).get(points2draw.get(numOfIndexesRun).size() - 1) + yOffset)) / 2 +
                                                        (points2draw.get(numOfIndexesRun).get(points2draw.get(numOfIndexesRun).size() - 1) + yOffset) +
                                                        ((points2draw.get(numOfIndexesRun - 1).get(points2draw.get(numOfIndexesRun - 1).size() - 1) + yOffset) -
                                                                (points2draw.get(numOfIndexesRun).get(points2draw.get(numOfIndexesRun).size() - 1) + yOffset)) / 3;
                                            } else if (points2draw.get(numOfIndexesRun - 1).get(points2draw.get(numOfIndexesRun - 1).size() - 2) >=
                                                    points2draw.get(numOfIndexesRun).get(points2draw.get(numOfIndexesRun).size() - 2) &&
                                                    points2draw.get(numOfIndexesRun - 1).get(points2draw.get(numOfIndexesRun - 1).size() - 1) >=
                                                            points2draw.get(numOfIndexesRun).get(points2draw.get(numOfIndexesRun).size() - 1)) {
                                                midX = ((points2draw.get(numOfIndexesRun).get(points2draw.get(numOfIndexesRun).size() - 2) + xOffset) -
                                                        (points2draw.get(numOfIndexesRun - 1).get(points2draw.get(numOfIndexesRun - 1).size() - 2) + xOffset)) / 2 +
                                                        (points2draw.get(numOfIndexesRun - 1).get(points2draw.get(numOfIndexesRun - 1).size() - 2) + xOffset) -
                                                        ((points2draw.get(numOfIndexesRun).get(points2draw.get(numOfIndexesRun).size() - 2) + xOffset) -
                                                                (points2draw.get(numOfIndexesRun - 1).get(points2draw.get(numOfIndexesRun - 1).size() - 2) + xOffset)) * 2 / 3;
                                                midY = ((points2draw.get(numOfIndexesRun).get(points2draw.get(numOfIndexesRun).size() - 1) + yOffset) -
                                                        (points2draw.get(numOfIndexesRun - 1).get(points2draw.get(numOfIndexesRun - 1).size() - 1) + yOffset)) / 2 +
                                                        (points2draw.get(numOfIndexesRun - 1).get(points2draw.get(numOfIndexesRun - 1).size() - 1) + yOffset) -
                                                        ((points2draw.get(numOfIndexesRun - 1).get(points2draw.get(numOfIndexesRun - 1).size() - 1) + yOffset) -
                                                                (points2draw.get(numOfIndexesRun).get(points2draw.get(numOfIndexesRun).size() - 1) + yOffset)) / 3;

                                            } else if (points2draw.get(numOfIndexesRun - 1).get(points2draw.get(numOfIndexesRun - 1).size() - 2) >=
                                                    points2draw.get(numOfIndexesRun).get(points2draw.get(numOfIndexesRun).size() - 2) &&
                                                    points2draw.get(numOfIndexesRun - 1).get(points2draw.get(numOfIndexesRun - 1).size() - 1) <
                                                            points2draw.get(numOfIndexesRun).get(points2draw.get(numOfIndexesRun).size() - 1)) {
                                                midX = ((points2draw.get(numOfIndexesRun).get(points2draw.get(numOfIndexesRun).size() - 2) + xOffset) -
                                                        (points2draw.get(numOfIndexesRun - 1).get(points2draw.get(numOfIndexesRun - 1).size() - 2) + xOffset)) / 2 +
                                                        (points2draw.get(numOfIndexesRun - 1).get(points2draw.get(numOfIndexesRun - 1).size() - 2) + xOffset) +
                                                        ((points2draw.get(numOfIndexesRun).get(points2draw.get(numOfIndexesRun).size() - 2) + xOffset) -
                                                                (points2draw.get(numOfIndexesRun - 1).get(points2draw.get(numOfIndexesRun - 1).size() - 2) + xOffset)) / 3;
                                                midY = ((points2draw.get(numOfIndexesRun - 1).get(points2draw.get(numOfIndexesRun - 1).size() - 1) + yOffset) -
                                                        (points2draw.get(numOfIndexesRun).get(points2draw.get(numOfIndexesRun).size() - 1) + yOffset)) / 2 +
                                                        (points2draw.get(numOfIndexesRun).get(points2draw.get(numOfIndexesRun).size() - 1) + yOffset) +
                                                        ((points2draw.get(numOfIndexesRun - 1).get(points2draw.get(numOfIndexesRun - 1).size() - 1) + yOffset) -
                                                                (points2draw.get(numOfIndexesRun).get(points2draw.get(numOfIndexesRun).size() - 1) + yOffset)) / 3;
                                            } else {
                                                midX = ((points2draw.get(numOfIndexesRun).get(points2draw.get(numOfIndexesRun).size() - 2) + xOffset) -
                                                        (points2draw.get(numOfIndexesRun - 1).get(points2draw.get(numOfIndexesRun - 1).size() - 2) + xOffset)) / 2 +
                                                        (points2draw.get(numOfIndexesRun - 1).get(points2draw.get(numOfIndexesRun - 1).size() - 2) + xOffset) -
                                                        ((points2draw.get(numOfIndexesRun).get(points2draw.get(numOfIndexesRun).size() - 2) + xOffset) -
                                                                (points2draw.get(numOfIndexesRun - 1).get(points2draw.get(numOfIndexesRun - 1).size() - 2) + xOffset)) * 2 / 3;
                                                midY = ((points2draw.get(numOfIndexesRun).get(points2draw.get(numOfIndexesRun).size() - 1) + yOffset) -
                                                        (points2draw.get(numOfIndexesRun - 1).get(points2draw.get(numOfIndexesRun - 1).size() - 1) + yOffset)) / 2 +
                                                        (points2draw.get(numOfIndexesRun - 1).get(points2draw.get(numOfIndexesRun - 1).size() - 1) + yOffset) -
                                                        ((points2draw.get(numOfIndexesRun - 1).get(points2draw.get(numOfIndexesRun - 1).size() - 1) + yOffset) -
                                                                (points2draw.get(numOfIndexesRun).get(points2draw.get(numOfIndexesRun).size() - 1) + yOffset)) / 3;
                                            }
                                            curvedLine.setCurve(points2draw.get(numOfIndexesRun - 1).get(points2draw.get(numOfIndexesRun - 1).size() - 2) + xOffset + 5,
                                                    points2draw.get(numOfIndexesRun - 1).get(points2draw.get(numOfIndexesRun - 1).size() - 1) + yOffset + 5,
                                                    midX + 5, midY + 5,
                                                    points2draw.get(numOfIndexesRun).get(points2draw.get(numOfIndexesRun).size() - 2) + xOffset + 5,
                                                    points2draw.get(numOfIndexesRun).get(points2draw.get(numOfIndexesRun).size() - 1) + yOffset + 5);
                                            g2d.draw(curvedLine);
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                                numOfIndexesRun += 1;
                            }
                        }
                        numOfIndexesRun = 0;
                        componentChecker = 0;
                        if (points2draw.get(points2draw.size() - 1).contains(-1)) {
                            while (points2draw.size() - 1 > numOfIndexesRun) {
                                if (points2draw.get(points2draw.size() - 1).get(numOfIndexesRun) == -1) {
                                    g.setColor(Color.BLACK);
                                    try {
                                        if (numOfIndexesRun > 0) {
                                            points2draw.get(numOfIndexesRun).add(0, points2draw.get(numOfIndexesRun - 1).get(0));
                                            points2draw.get(numOfIndexesRun).add(1, points2draw.get(numOfIndexesRun - 1).get(1));
                                        }
                                        if (points2draw.get(numOfIndexesRun).size() == 4) {
                                            g.drawLine(points2draw.get(numOfIndexesRun).get(0) + xOffset + 5,
                                                    points2draw.get(numOfIndexesRun).get(1) + yOffset + 5,
                                                    points2draw.get(numOfIndexesRun).get(2) + xOffset + 5,
                                                    points2draw.get(numOfIndexesRun).get(3) + yOffset + 5);
                                        } else {
                                            g.drawPolyline(LineArrayProcessor.extractX(points2draw.get(numOfIndexesRun), 2),
                                                    LineArrayProcessor.extractY(points2draw.get(numOfIndexesRun), 2),
                                                    LineArrayProcessor.extractX(points2draw.get(numOfIndexesRun), 2).length);
                                        }
                                        if (numOfIndexesRun > 0) {
                                            points2draw.get(numOfIndexesRun).remove(0);
                                            points2draw.get(numOfIndexesRun).remove(1);
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                                numOfIndexesRun += 1;
                            }
                        }
                    }
                    if (selected) {
                        g.setColor(Color.BLUE);
                        g.fillOval(selectedCircle[0] + xOffset - 2, selectedCircle[1] + yOffset - 2, 18, 18);
                    }
                    numOfIndexesRun = 0;
                    componentChecker = 0;
                    while (numOfIndexesRun < circles.size() / 3) {
                        switch (circles.get(componentChecker + 2)) {      //Sets color
                            case (0):
                                g.setColor(Color.WHITE);
                                break;
                            case (1):
                                g.setColor(Color.LIGHT_GRAY);
                                break;
                            case (2):
                                g.setColor(Color.GRAY);
                                break;
                            case (3):
                                g.setColor(Color.DARK_GRAY);
                                break;
                            case (4):
                                g.setColor(Color.BLACK);
                                break;
                            case (5):
                                g.setColor(Color.RED);
                                break;
                            case (6):
                                g.setColor(Color.PINK);
                                break;
                            case (7):
                                g.setColor(Color.ORANGE);
                                break;
                            case (8):
                                g.setColor(Color.YELLOW);
                                break;
                            case (9):
                                g.setColor(Color.MAGENTA);
                                break;
                            case (10):
                                g.setColor(Color.CYAN);
                                break;
                            case (11):
                                g.setColor(Color.BLUE);
                                break;
                            case (12):
                                g.setColor(Color.GREEN);
                        }
                        g.fillOval(circles.get(componentChecker) + xOffset, circles.get(componentChecker + 1) + yOffset, wid, hei);
                        numOfIndexesRun += 1;
                        componentChecker += 3;
                    }
                }
            };
            if (redrawCircle) {
                paintPanel.repaint();
                redrawCircle = false;
            }
            paintPanel.setOpaque(opaque);
            paintPanel.setBounds(0, 0, 2000, 2000);           //Inits/draws all circles
            lp.add(paintPanel, 50, 0);      //Sets object constraints, a value that determines layering
            lp.moveToFront(paintPanel);
        }
    }
}
