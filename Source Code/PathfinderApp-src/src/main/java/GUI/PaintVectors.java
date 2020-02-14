package GUI;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import javafx.geometry.Pos;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public class PaintVectors {
    private int radius;
    private int xOffset;
    private int yOffset;
    private int selected;

    private ArrayList<Integer> pointX;
    private ArrayList<Integer> pointY;
    private ArrayList<Color> colors;
    private ArrayList<Double> heading;

    private int[] lineX;
    private int[] lineY;

    public PaintVectors() {
        radius = 12;
        yOffset = -30;
        xOffset = -2;
        selected = -1;

        pointX = new ArrayList<>();
        pointY = new ArrayList<>();
        colors = new ArrayList<>();
        heading = new ArrayList<>();

        lineX = null;
        lineY = null;
    }

    public void loadData(ArrayList<Pose2d> p2ds){
        pointX.clear();
        pointY.clear();
        colors.clear();
        heading.clear();

        for(Pose2d pose2d : p2ds){
            colors.add(Color.CYAN);
            pointX.add((int) Math.round(pose2d.getX()));
            pointY.add((int) Math.round(pose2d.getY()));
            heading.add(pose2d.getHeading());
        }
    }

    public void clearTrajectory(){
        pointX.clear();
        pointY.clear();
        colors.clear();
        heading.clear();
    }

    public void addPoint(int x, int y, double theta, Color color) {
        pointX.add(x);
        pointY.add(y);
        colors.add(color);
        heading.add(theta);
    }

    public void setPolyline(int[] x, int[] y){
        lineX = x;
        lineY = y;
    }

    public void removeLast() {
        pointX.remove(pointX.size() - 1);
        pointY.remove(pointY.size() - 1);
        colors.remove(colors.size() - 1);
        heading.remove(heading.size() - 1);
    }

    public void removeIndex(int index) {
        pointX.remove(index);
        pointY.remove(index);
        colors.remove(index);
        heading.remove(index);
        if(selected >= pointX.size())
            selected = pointX.size() - 1;
    }

    public void setPoint(int index, int x, int y, double theta, Color color){
        pointX.set(index, x);
        pointY.set(index, y);
        colors.set(index, color);
        heading.set(index, theta);
        if(selected >= pointX.size())
            selected = pointX.size() - 1;
    }

    public int getSelectedIndex(){ return selected; }

    public ArrayList<Pose2d> getPose2dList(){
        ArrayList<Pose2d> pose = new ArrayList<>();
        for(int i = 0; i < pointX.size(); i++){
            if(heading.get(i) != Double.MAX_VALUE)
                pose.add(new Pose2d(pointX.get(i), pointY.get(i), heading.get(i)));
            else
                pose.add(new Pose2d(pointX.get(i), pointY.get(i)));
        }
        return pose;
    }

    public void paintPoints(Graphics g) {
        if (!pointX.isEmpty() && !pointY.isEmpty() && !colors.isEmpty()) {
            if(lineX != null && lineY != null && lineX.length >= 2 && lineY.length >= 2) {
                g.setColor(Color.MAGENTA);
                g.drawPolyline(lineX, lineY, lineX.length);
            }

            for (int i = 0; i < pointX.size(); i++) {
                if(i == selected)
                    g.setColor(colors.get(i).darker());
                else
                    g.setColor(colors.get(i));
                g.fillOval(pointX.get(i) - radius / 2 + xOffset, pointY.get(i) - radius / 2 + yOffset, radius, radius);
            }
        }
    }

    public String toString() {
        String output = "Current Circles: \n";
        if (!pointX.isEmpty() && !pointY.isEmpty() && !colors.isEmpty()) {
            for (int i = 0; i < pointX.size(); i++) {
                if (i == selected)
                    output += "Circle " + (i + 1) + " (CURRENTLY SELECTED): {" + (pointX.get(i) - radius / 2 + xOffset) +
                            ", " + (pointY.get(i) - radius / 2 + yOffset) + "} Color: '" + colors.get(i).toString() + "' \n";
                else
                    output += "Circle " + (i + 1) + ": {" + (pointX.get(i) - radius / 2 + xOffset) + ", " +
                            (pointY.get(i) - radius / 2 + yOffset) + "} Color: '" + colors.get(i).toString() + "' \n";
            }
            output += "PaintVectors.java \n ";
        } else
            output += "None";
        return output;
    }

    public void testForSelected(int mouseX, int mouseY) {
        if(!pointX.isEmpty() && !pointY.isEmpty())
            for (int i = 0; i < pointX.size(); i++) {
                if (mouseX <= pointX.get(i) + radius && mouseX >= pointX.get(i) &&
                        mouseY <= pointY.get(i) + radius - 5 && mouseY >= pointY.get(i) - 5) {
                    selected = i;
                    break;
                } else
                    selected = -1;
            }
        else
            selected = -1;
    }

    public int isGrabbingPoint(int mouseX, int mouseY){
        if(!pointX.isEmpty() && !pointY.isEmpty())
            for (int i = 0; i < pointX.size(); i++) {
                if (mouseX <= pointX.get(i) + radius && mouseX >= pointX.get(i) &&
                        mouseY <= pointY.get(i) + radius - 5 && mouseY >= pointY.get(i) - 5) {
                    return i;
                }
            }
        return -1;
    }
}
