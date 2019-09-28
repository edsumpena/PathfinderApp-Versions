package ParamsGenerator;

/*
 * Main Parameters Generator that creates GUI
 */

import App.ReadingAndWriting.ImportMotorsAndConstants;
import App.ReadingAndWriting.ZipAndUnzip;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.ArrayList;

public class MainGenerator extends JComponent{
    static boolean unfocus = false;
    static File prevFilePath;
    static String pathName = "untitled";
    private static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("Parameters Generator");
        MainGenerator newContentPane = new MainGenerator();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        newContentPane.setOpaque(false);

        mouseDetect.initListener(frame);
        thread.executeFocus(frame);

        frame.setContentPane(newContentPane);

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    public MainGenerator() {
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        JLayeredPane layeredPane = new JLayeredPane();

        setPreferredSize(new Dimension(700, 700));
        layeredPane.setOpaque(false);

        JButton save = new JButton("Save Params");
        save.setBounds(450, 600, 150, 40);

        JButton open = new JButton("Open Params File");
        open.setBounds(100, 600, 150, 40);

        JLabel nameLabel = new JLabel("Motor Names:");
        nameLabel.setFont(nameLabel.getFont().deriveFont(15f));
        nameLabel.setBounds(300, 50, 130, 30);

        JLabel typeLabel = new JLabel("Motor Types:");
        typeLabel.setFont(nameLabel.getFont().deriveFont(15f));
        typeLabel.setBounds(300, 150, 130, 30);

        JLabel fyi = new JLabel("Note: Please separate motor names & types with commas");
        fyi.setFont(nameLabel.getFont().deriveFont(11f));
        fyi.setBounds(195, 230, 400, 30);

        JLabel fyiCont2 = new JLabel("Types of Motors: DCWheel, DCArm, or Servo");
        fyiCont2.setFont(nameLabel.getFont().deriveFont(11f));
        fyiCont2.setBounds(240, 250, 400, 30);

        JLabel driveLabel = new JLabel("Drive Constants:");
        driveLabel.setFont(driveLabel.getFont().deriveFont(15f));
        driveLabel.setBounds(300, 400, 130, 30);

        JLabel fyi2 = new JLabel("Note: Please separate constants with commas.");
        fyi2.setFont(fyi2.getFont().deriveFont(11f));
        fyi2.setBounds(220, 480, 270, 30);

        JLabel fyiCont = new JLabel("Format: MaxVelocity, MaxAcceleration, MaxAngleVelocity, MaxAngleAcceleration");
        fyiCont.setFont(fyiCont.getFont().deriveFont(11f));
        fyiCont.setBounds(130,500,500,30);

        JTextField constants = new JTextField();
        constants.setBounds(200,450,300,30);

        JTextField motorName = new JTextField();
        motorName.setBounds(200,100,300,30);

        JTextField motorType = new JTextField();
        motorType.setBounds(200,200,300,30);

        layeredPane.add(save, 1, 0);
        layeredPane.add(nameLabel, 2, 0);
        layeredPane.add(motorName,3,0);
        layeredPane.add(motorType,4,0);
        layeredPane.add(typeLabel,5,0);
        layeredPane.add(fyi,6,0);
        layeredPane.add(open, 7, 0);
        layeredPane.add(fyi2,8,0);
        layeredPane.add(driveLabel, 9, 0);
        layeredPane.add(constants,10,0);
        layeredPane.add(fyiCont,11,0);
        layeredPane.add(fyiCont2,12,0);

        add(layeredPane);

        //System.out.println(Params.genMotors("WheelDcMotors, ArmDcMotor, SmallServoArm", "DCWheel, DCArm, Servo"));
        //System.out.println(Params.genConstants("0.0, 0.0, 0.0, 0.0"));

        save.addActionListener(new ActionListener() {  //Button onClickListener
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser() {
                };
                fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                fileChooser.removeChoosableFileFilter(fileChooser.getFileFilter());  //Remove the default "All Files" filter
                FileFilter filter = new FileNameExtensionFilter("PARAMS file", "prms");
                fileChooser.addChoosableFileFilter(filter); //Add .path file type filter

                fileChooser.setDialogTitle("Save Params");
                fileChooser.setPreferredSize(new Dimension(800, 600));
                fileChooser.setSelectedFile(new File("untitled.prms"));
                if (prevFilePath != null) {
                    fileChooser.setCurrentDirectory(prevFilePath);
                }
                String fileNoExt = "";
                int userSelection = fileChooser.showSaveDialog(layeredPane);
                if (userSelection == JFileChooser.APPROVE_OPTION) {     //Save Button is pressed
                    File f = fileChooser.getSelectedFile();
                    pathName = String.valueOf(f).substring(String.valueOf(f).lastIndexOf("\\") + 1,
                            String.valueOf(f).indexOf("."));
                    if (!String.valueOf(f).contains(".prms")) {   //If file name does not contain .path
                        int dialogButton = JOptionPane.ERROR_MESSAGE;   //Error message pops up
                        JOptionPane.showMessageDialog(null, "Error: File must contain '.prms' extension", "Error", dialogButton);
                    } else if (f.exists()) {  //If file name already exists
                        int dialogButton2 = JOptionPane.YES_NO_OPTION;
                        int dialogResult = JOptionPane.showConfirmDialog(null,
                                "File Already Exists! Do you want to Replace?", "File Exists", dialogButton2);    //Ask if user wants to replace file
                        if (dialogResult == JOptionPane.YES_OPTION) {
                            System.gc();
                            f.setExecutable(false);
                            f.setReadable(true);
                            f.setWritable(true);
                            f.delete();
                            fileNoExt = String.valueOf(fileChooser.getSelectedFile());
                            fileNoExt = fileNoExt.substring(0, fileNoExt.lastIndexOf("\\"));
                            String name = String.valueOf(fileChooser.getSelectedFile());
                            name = name.substring(name.lastIndexOf("\\") + 1, name.lastIndexOf("."));
                            Params.createFile(fileNoExt, name, Params.genMotors(motorName.getText(), motorType.getText()),
                                    Params.genConstants(constants.getText()));
                        }
                    } else {    //If not a duplicate file name and contains .path ext in the name
                        fileNoExt = String.valueOf(fileChooser.getSelectedFile());
                        fileNoExt = fileNoExt.substring(0, fileNoExt.lastIndexOf("\\"));
                        String name = String.valueOf(fileChooser.getSelectedFile());
                        name = name.substring(name.lastIndexOf("\\") + 1, name.lastIndexOf("."));
                        Params.createFile(fileNoExt, name, Params.genMotors(motorName.getText(), motorType.getText()),
                                Params.genConstants(constants.getText()));
                    }
                    prevFilePath = fileChooser.getCurrentDirectory();
                } else if (userSelection == JFileChooser.CANCEL_OPTION) {   //Cancel button is pressed
                    prevFilePath = fileChooser.getCurrentDirectory();
                } else if (userSelection == JFileChooser.ERROR_OPTION) {    // X button is pressed
                    prevFilePath = fileChooser.getCurrentDirectory();
                }
            }
        });

        open.addActionListener(new ActionListener() {  //Button onClickListener
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Open Params File");
                fileChooser.setPreferredSize(new Dimension(800, 600));
                fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                fileChooser.removeChoosableFileFilter(fileChooser.getFileFilter());  //Remove the default "All Files" filter
                FileFilter filter = new FileNameExtensionFilter("PARAMS file", "prms");
                fileChooser.addChoosableFileFilter(filter); //Add .path file type filter

                if (prevFilePath != null) {
                    fileChooser.setCurrentDirectory(prevFilePath);
                }
                int result = fileChooser.showOpenDialog(layeredPane);
                if (result == JFileChooser.APPROVE_OPTION) {    //Save button is pressed
                    File fileToRead = fileChooser.getSelectedFile();
                    ZipAndUnzip.unzipFolder(String.valueOf(fileToRead), String.valueOf(fileChooser.getCurrentDirectory()));
                    String name = String.valueOf(fileToRead).substring(String.valueOf(fileToRead).lastIndexOf("\\") + 1,
                            String.valueOf(fileToRead).indexOf("."));
                    pathName = name;

                    String motorDir = fileChooser.getCurrentDirectory() + "\\" + name + "Motors.mtr";    //Sets .cir file containing serialized circle array as current directory
                    String constantsDir = fileChooser.getCurrentDirectory() + "\\" + name + "DriveConstants.csts";     //Sets .ln file containing serialized line array as current directory

                    ArrayList<ArrayList<String>> motorVals = ImportMotorsAndConstants.getMotors(motorDir);

                    String names = "";
                    for(int i = 0; i < motorVals.get(0).size(); i++){
                        if(i == 0)
                            names = motorVals.get(0).get(i);
                        else
                            names = names + ", " + motorVals.get(0).get(i);
                    }

                    String types = "";
                    for(int i = 0; i < motorVals.get(1).size(); i++){
                        if(i == 0)
                            types = motorVals.get(1).get(i);
                        else
                            types = types + ", " + motorVals.get(1).get(i);
                    }

                    double[] csts = ImportMotorsAndConstants.getDriveConstants(constantsDir);

                    motorName.setText(names);
                    motorType.setText(types);
                    constants.setText(csts[0] + ", " + csts[1] + ", " + csts[2] + ", " + csts[3]);

                    ZipAndUnzip.deleteAndOrRename(constantsDir, "", "", true, false);  //Deletes temporary files
                    ZipAndUnzip.deleteAndOrRename(motorDir,"","",true,false);

                    prevFilePath = fileChooser.getCurrentDirectory();
                } else if (result == JFileChooser.CANCEL_OPTION) {      //If cancel button is pressed
                    prevFilePath = fileChooser.getCurrentDirectory();
                } else if (result == JFileChooser.ERROR_OPTION) {       //If X button is pressed
                    prevFilePath = fileChooser.getCurrentDirectory();
                }
            }
        });
    }

    public static class mouseDetect implements MouseMotionListener, MouseListener {

        public static void initListener(JFrame lp) {
            lp.addMouseMotionListener(new mouseDetect());
            lp.addMouseListener(new mouseDetect());
        }

        @Override
        public void mouseClicked(MouseEvent e) {

        }

        @Override
        public void mousePressed(MouseEvent e) {
            unfocus = true;
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            unfocus = false;
        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }

        @Override
        public void mouseDragged(MouseEvent e) {

        }

        @Override
        public void mouseMoved(MouseEvent e) {

        }
    }

    public static class thread extends Thread {
        public static void executeFocus(JFrame frame) {     //All JFrame related loops
            Thread one = new Thread() {
                public void run() {
                    while (true) {
                        if (unfocus) {     //Mouse clicks away from JTextbox -> unfocus JTextbox (for keyPressedListener)
                            frame.requestFocus();
                            try {
                                Thread.sleep(100);
                            } catch (Exception e) {
                                //e.printStackTrace();
                            }
                        }
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException ignore) {
                        }
                    }
                }
            };
            one.start();
        }
    }

    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
}
