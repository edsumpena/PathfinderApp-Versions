package App;

import App.Converters.FromAndToPose2D;
import App.EditParameters.EditParametersActivity;

import javax.swing.*;
import java.io.IOException;

public class Main {
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
        JFrame frame = new JFrame("Pathfinding");  //Create and set up the window.
        MainActivity newContentPane = new MainActivity();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //EditParametersActivity editParametersActivity = new EditParametersActivity();
        newContentPane.setOpaque(false);
        frame.setContentPane(newContentPane);

        FromAndToPose2D.setCanvasSize(750.0);

        MainActivity.mouse.initListener(frame);
        MainActivity.key.initListener(frame);
        MainActivity.threads.executeFocus(frame);

        frame.pack();
        frame.setVisible(true);
        frame.requestFocusInWindow();   //Sets JFrame as main window
    }
}
