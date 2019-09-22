package App.Converters;

import App.MainActivity;

import java.util.ArrayList;

public class LineArrayProcessor {
    static ArrayList<Integer> curvesIndexList = new ArrayList<>();

    public static String getCurrentSetting(int setting) {
        String mode = "null";
        switch (setting) {
            case 0:
                mode = "line";
                break;
            case 1:
                mode = "curve";
                break;
            case 2:
                mode = "reverse";
                break;
            case 3:
                mode = "strafe";
                break;
            case 4:
                mode = "spline";
                break;

        }
        return mode;
    }

    public static ArrayList<ArrayList<Integer>> polyLineList(ArrayList<ArrayList<String>> optionsAndCoords, boolean debugMsg) {
        int i = 0;
        int x = 0;
        int z = 0;
        curvesIndexList.clear();
        ArrayList<ArrayList<Integer>> output = new ArrayList<>();
        ArrayList<Integer> adder = new ArrayList<>();
        if (!optionsAndCoords.isEmpty()) {
            if (optionsAndCoords.get(0).size() >= 1) {
                output.add(new ArrayList<>());
                output.get(0).add(Integer.valueOf(optionsAndCoords.get(1).get(0)));
                output.get(0).add(Integer.valueOf(optionsAndCoords.get(1).get(1)));
                while (optionsAndCoords.get(0).size() > i) {
                    if (optionsAndCoords.get(0).get(i).equals("curve")) {
                        if (z != 0 && !output.get(z).isEmpty())
                            z += 1;
                        curvesIndexList.add(0);
                        output.add(new ArrayList<>());
                        output.get(z).add(Integer.valueOf(optionsAndCoords.get(3).get(x)));
                        output.get(z).add(Integer.valueOf(optionsAndCoords.get(3).get(x + 1)));
                        if (debugMsg)
                            System.out.println("post0: " + output + " " + z);
                        try {
                            if (!optionsAndCoords.get(0).get(i + 1).equals("curve")) {
                                z += 1;
                                if (curvesIndexList.isEmpty() || curvesIndexList.get(curvesIndexList.size() - 1) != -1)
                                    curvesIndexList.add(-1);
                                output.add(new ArrayList<>());
                            }
                        } catch (Exception e) {
                        }
                    } else {
                        try {
                            if (i + 1 <= optionsAndCoords.get(0).size() - 1 && optionsAndCoords.get(0).get(i + 1).equals("curve")) {
                                if (curvesIndexList.isEmpty() || curvesIndexList.get(curvesIndexList.size() - 1) != -1)
                                    curvesIndexList.add(-1);
                                output.get(z).add(Integer.valueOf(optionsAndCoords.get(3).get(x)));
                                output.get(z).add(Integer.valueOf(optionsAndCoords.get(3).get(x + 1)));
                                if (!output.get(z).isEmpty())
                                    z += 1;
                                output.add(new ArrayList<>());
                                if (debugMsg)
                                    System.out.println("post-1: " + output);
                            } else {
                                output.get(z).add(Integer.valueOf(optionsAndCoords.get(3).get(x)));
                                output.get(z).add(Integer.valueOf(optionsAndCoords.get(3).get(x + 1)));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    x += 2;
                    i += 1;
                }
                if (curvesIndexList.size() < output.get(0).size() / 2 - 1) {
                    curvesIndexList.add(-1);
                }
                output.add(curvesIndexList);
                int counter = 0;
                while (output.size() > counter) {
                    if (output.get(counter).isEmpty())
                        output.remove(counter);
                    counter += 1;
                    if (output.contains(new ArrayList<>()) && output.size() <= counter) {
                        counter = 0;
                    }
                }
                if (debugMsg) {
                    System.out.println("inputArray: " + optionsAndCoords);
                    System.out.println("outputArray: " + output);
                }
                return output;
            } else {
                adder.add(-1);
                adder.add(-1);
                adder.add(-1);
                output.add(adder);
                return output;
            }
        } else {
            adder.add(-2);
            adder.add(-2);
            adder.add(-2);
            output.add(adder);
            return output;
        }
    }

    public static int[] extractX(ArrayList<Integer> circles, int valsPerCircleX) {
        ArrayList<Integer> xVals = new ArrayList<>();
        int i = 0;
        while (circles.size() > i) {
            try {
                xVals.add(circles.get(i) + MainActivity.xOffset + 5);
            } catch (Exception e) {
            }
            i += valsPerCircleX;
        }

        int[] xValToArray = new int[xVals.size()];
        i = 0;
        while (xVals.size() > i) {
            xValToArray[i] = xVals.get(i);
            i += 1;
        }
        return xValToArray;
    }

    public static int[] extractY(ArrayList<Integer> circles, int valsPerCircleY) {
        ArrayList<Integer> yVals = new ArrayList<>();
        int i = 0;
        while (circles.size() > i) {
            try {
                yVals.add(circles.get(i + 1) + MainActivity.yOffset + 5);
            } catch (Exception e) {
            }
            i += valsPerCircleY;
        }

        int[] yValToArray = new int[yVals.size()];
        i = 0;
        while (yVals.size() > i) {
            yValToArray[i] = yVals.get(i);
            i += 1;
        }
        return yValToArray;
    }
}
