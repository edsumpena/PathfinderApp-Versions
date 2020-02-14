package GUI;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.profile.SimpleMotionConstraints;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.acmerobotics.roadrunner.trajectory.TrajectoryBuilder;
import com.acmerobotics.roadrunner.trajectory.constraints.DriveConstraints;
import com.acmerobotics.roadrunner.trajectory.constraints.TrajectoryConstraints;

import java.util.ArrayList;
import java.util.Arrays;

public class TrajectoryWrapper {
    private static TrajectoryBuilder builder;
    private static DriveConstraints constraints = new DriveConstraints(
            72.0, 38.0, 0.0,
            Math.toRadians(135.0), Math.toRadians(90.0), 0.0
    );

    public static Trajectory buildAndGetTrajectory(ArrayList<Pose2d> p2d, ArrayList<MoveOptions> moveOptions) {
        builder = new TrajectoryBuilder(p2d.get(0), constraints);
        for (int i = 1; i < p2d.size(); i++){
            switch(moveOptions.get(i)){
                case LineTo_Forward:
                    builder.setReversed(false).lineTo(new Vector2d(p2d.get(i).getX(), p2d.get(i).getY()));
                    break;
                case LineTo_Reversed:
                    builder.setReversed(true).lineTo(new Vector2d(p2d.get(i).getX(), p2d.get(i).getY()));
                    break;
                case StrafeTo:
                    builder.setReversed(false).strafeTo(new Vector2d(p2d.get(i).getX(), p2d.get(i).getY()));
                    break;
                case SplineTo_Forward:
                    builder.setReversed(false).splineTo(p2d.get(i));
                    break;
                case SplineTo_Reversed:
                    builder.setReversed(true).splineTo(p2d.get(i));
                    break;
            }
        }
        return builder.build();
    }

    public static int[][] getPolylineXY(Trajectory trajectory){
        double time = trajectory.duration();
        time = Math.round(time * 10d) / 10d;

        ArrayList<Integer> listX = new ArrayList<>();
        ArrayList<Integer> listY = new ArrayList<>();

        for(double i = 0; i < time; i += 0.1){
            listX.add((int) Math.round(trajectory.get(i).getX()));
            listY.add((int) Math.round(trajectory.get(i).getY()));
        }

        int[] x = new int[listX.size()];
        int[] y = new int[listY.size()];

        for(int i = 0; i < listX.size(); i++){
            x[i] = listX.get(i);
            y[i] = listY.get(i);
        }

        return new int[][] {x, y};
    }
}
