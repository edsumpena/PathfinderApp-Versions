package App.ReadingAndWriting;

import App.MainActivity;

import java.util.ArrayList;
import java.util.Collections;

public class MotorSettingsHandler {
    public static void panelManager(boolean show, int power, int timeRunning, int timeDelay){
        MainActivity.showing = show;
        MainActivity.pow = power;
        MainActivity.run = timeRunning;
        MainActivity.del = timeDelay;
    }

    public static int[] closestPoint(ArrayList<Integer> circles, int cirX, int cirY){
        ArrayList<Integer> distances = new ArrayList<>();
        int[] point = {0, 0, 0};
        for(int i = 0; i < circles.size(); i += 3){
            distances.add(distanceToPoint(circles.get(0), circles.get(1), cirX, cirY));
        }
        point[0] = circles.get(distances.indexOf(Collections.min(distances)) * 3);
        point[1] = circles.get(distances.indexOf(Collections.min(distances)) * 3 + 1);
        point[2] = distances.indexOf(Collections.min(distances));
        return point;
    }

    private static int distanceToPoint(int x1, int y1, int x2, int y2){
        return (int) (Math.round(Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2))));
    }
}
