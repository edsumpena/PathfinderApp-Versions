package GUI;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class RunPathfinderApp {
    public static JFrame frame;
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {  //Creating and showing this application's GUI
            public void run() {
                try {
                    createAndShowGUI();
                } catch (IOException ex) {
                    // handle exception...
                }
            }
        });
    }
    private static void createAndShowGUI() throws IOException {
        frame = new JFrame("Pathfinder App");  //Create and set up the window.
        PathfinderGUI newContentPane = new PathfinderGUI();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //EditParametersActivity editParametersActivity = new EditParametersActivity();
        newContentPane.setOpaque(false);
        frame.setContentPane(newContentPane);
        frame.setSize(new Dimension(1000,500));
        frame.setResizable(false);

        PathfinderGUI.initListener(frame);

        frame.pack();
        frame.setVisible(true);
        frame.requestFocusInWindow();   //Sets JFrame as main window
    }
}
