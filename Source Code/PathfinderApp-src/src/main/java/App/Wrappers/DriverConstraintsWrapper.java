package App.Wrappers;

import com.acmerobotics.roadrunner.trajectory.constraints.DriveConstraints;

import java.util.ArrayList;

public class DriverConstraintsWrapper {
    static double maxVeld;
    static double maxAccd;
    static double maxAngleVeld;
    static double maxAngleAccd;
    static ArrayList<Double> contraintsd = new ArrayList<>();

    public static void setDriveContraints(double maxVelocity, double maxAcceleration, double maxAngleVelocity, double maxAngleAcceleration) {
        maxVeld = maxVelocity;
        maxAccd = maxAcceleration;
        maxAngleVeld = maxAngleVelocity;
        maxAngleAccd = maxAngleAcceleration;
    }

    public static DriveConstraints getDriveConstraints(){
        contraintsd.clear();
        contraintsd.add(maxVeld);
        contraintsd.add(maxAccd);
        contraintsd.add(maxAngleVeld);
        contraintsd.add(maxAngleAccd);
        if(!contraintsd.isEmpty())
            if(contraintsd.size() == 4)
               return new DriveConstraints(contraintsd.get(0),contraintsd.get(1),contraintsd.get(2),contraintsd.get(3));
            else
                return new DriveConstraints(0,0,0,0);
        else
            return new DriveConstraints(0,0,0,0);
    }
}
