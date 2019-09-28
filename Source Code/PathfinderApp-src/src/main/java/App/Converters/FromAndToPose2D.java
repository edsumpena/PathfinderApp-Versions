package App.Converters;

import com.acmerobotics.roadrunner.Pose2d;

import java.util.ArrayList;

public class FromAndToPose2D {
    //424 field size
    static double CANVAS_SIZE = 0.0;

//---------------------------------------------------------------------------------------------------------

    public static ArrayList<Pose2d> pointsToPose2d(ArrayList<Integer> points, int firstXIndex,
                                                   int firstYIndex, int valsPerCircle){  //feed circles array into here
        ArrayList<Pose2d> pose2ds = new ArrayList<>();
        int i = 0;
        while(points.size() > i){
            try {
                pose2ds.add(canvasToFieldSpace(Double.valueOf(points.get(i + firstXIndex)), Double.valueOf(points.get(i + firstYIndex)),
                        Double.valueOf(points.get(i + 3 + firstXIndex)), Double.valueOf(points.get(i + 3 + firstYIndex))));
            } catch (Exception e){
                pose2ds.add(canvasToFieldSpace(Double.valueOf(points.get(i + firstXIndex)), Double.valueOf(points.get(i + firstYIndex)),
                        -1000,-1000));
                break;
            }
            i += valsPerCircle;
        }
        return pose2ds;
    }
    public static ArrayList<Integer> pose2dToPoints(ArrayList<Pose2d> poses, ArrayList<Integer> colors){
        ArrayList<Integer> points = new ArrayList<>();
        int i = 0;
        while(poses.size() > i){
            points.add((int) fieldToCanvasSpace(poses.get(i))[0]);
            points.add((int) fieldToCanvasSpace(poses.get(i))[1]);
            points.add(colors.get(i));
            i += 3;
        }
        return points;
    }
    public static double getHeading(double x1, double y1, double x2, double y2){    //https://math.stackexchange.com/questions/1596513/find-the-bearing-angle-between-two-points-in-a-2d-space
        return Math.atan2(x2 - x1, y1 - y2);
    }

//---------------------------------------------------------------------------------------------------------

    public static ArrayList<Integer> getColors(ArrayList<Integer> points, int firstColorIndex, int valsPerCircle){
        ArrayList<Integer> colors = new ArrayList<>();
        colors = null;
        int i = 0;
        while(points.size() > i){
            try {
                colors.add(points.get(i + firstColorIndex));
            } catch (Exception e){
                e.printStackTrace();
                break;
            }
            i += valsPerCircle;
        }
        return colors;
    }

//---------------------------------------------------------------------------------------------------------

    public static Pose2d canvasToFieldSpace(double vectorX, double vectorY, double headingX, double headingY) {
        double fieldY = -(vectorX - CANVAS_SIZE / 2.0) * (144.0 / CANVAS_SIZE);  //255.0 or 144.0?
        double fieldX = -(vectorY - CANVAS_SIZE / 2.0) * (144.0 / CANVAS_SIZE);
        double heading = 0;
        if(headingX != -1000 && headingY != -1000) {
            heading = getHeading(vectorX, vectorY, headingX, headingY);
        }
        return new Pose2d(fieldX, fieldY, heading);
    }
    public static double[] fieldToCanvasSpace(Pose2d pose) {
        double canvasY = -(pose.getX() * (CANVAS_SIZE / 144.0)) + CANVAS_SIZE / 2.0;
        double canvasX = -(pose.getY() * (CANVAS_SIZE / 144.0)) + CANVAS_SIZE / 2.0;
        double[] returnVals = {canvasX,canvasY,pose.getHeading()};
        return returnVals;
    }
    public static void setCanvasSize(double size){
        CANVAS_SIZE = size;
    }
}
