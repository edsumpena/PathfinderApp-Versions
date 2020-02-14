package GUI;

import com.acmerobotics.roadrunner.geometry.Pose2d;

import javax.swing.*;
import java.rmi.server.ExportException;

public class CoordinatesPopup {
    private static double inputX;
    private static double inputY;
    private static double heading;

    public static Pose2d dispPopupMenu(boolean reqHeading, String[] hint){
        boolean NAN = true;
        boolean forceQuit = false;

        final JFrame parent = new JFrame();
        parent.pack();
        parent.setVisible(false);

        inputX = Double.MAX_VALUE;
        inputY = Double.MAX_VALUE;
        heading = Double.MAX_VALUE;

        while(NAN && !forceQuit) {
            String output = "";
            try {
                output = JOptionPane.showInputDialog(parent,
                        "Please enter the X-coordinate:", hint[0]);
                inputX = Double.valueOf(output);
                NAN = false;
            } catch(Exception e){
                if(output == null)
                    forceQuit = true;
            }
        }
        NAN = true;
        while(NAN && !forceQuit) {
            String output = "";
            try {
                output = JOptionPane.showInputDialog(parent,
                        "Please enter the Y-coordinate:", hint[1]);
                inputY = Double.valueOf(output);
                NAN = false;
            } catch (Exception e){
                if(output == null)
                    forceQuit = true;
            }
        }
        NAN = true;
        while(NAN && reqHeading && !forceQuit) {
            String output = "";
            try {
                output = JOptionPane.showInputDialog(parent,
                        "Please enter the Heading:", hint[2]);
                heading = Double.valueOf(output);
                NAN = false;
            } catch (Exception e){
                if(output == null)
                    forceQuit = true;
            }
        }
        if(forceQuit){
            inputX = Double.MAX_VALUE;
            inputY = Double.MAX_VALUE;
            heading = Double.MAX_VALUE;
        }

        if(!reqHeading && !forceQuit)
            heading = 0.0;

        inputX = roundThousandths(inputX);
        inputY = roundThousandths(inputY);
        heading = roundThousandths(heading);

        //System.out.println(inputX + ", " + inputY + ", " + heading);

        return new Pose2d(inputX, inputY, heading);
    }

    private static double roundThousandths(double val){
        return Math.round(val * 1000d) / 1000d;
    }
}
