package App.Converters;

import java.util.ArrayList;

public class ArrayListConverters {

//---------------------------------------------------------------------------------------------------------
    public static ArrayList<Integer> stringArrayToIntArray(ArrayList<String> stringArray){
        ArrayList<Integer> intArray = new ArrayList<>();
        int i = 0;
        while(stringArray.size() > i) {
            try {
                intArray.add(Integer.valueOf(stringArray.get(i)));
            } catch (Exception e){
                e.printStackTrace();
                break;
            }
            i += 1;
        }
        return intArray;
    }
    public static ArrayList<String> intArrayToStringArray(ArrayList<Integer> intArray){
        ArrayList<String> stringArray = new ArrayList<>();
        int i = 0;
        while(intArray.size() > i) {
            try {
                stringArray.add(String.valueOf(intArray.get(i)));
            } catch (Exception e){
                e.printStackTrace();
                break;
            }
            i += 1;
        }
        return stringArray;
    }

//---------------------------------------------------------------------------------------------------------

    public static ArrayList<Double> stringArrayToDoubleArray(ArrayList<String> stringArray){
        ArrayList<Double> doubleArray = new ArrayList<>();
        int i = 0;
        while(stringArray.size() > i) {
            try {
                doubleArray.add(Double.valueOf(stringArray.get(i)));
            } catch (Exception e){
                e.printStackTrace();
                break;
            }
            i += 1;
        }
        return doubleArray;
    }
    public static ArrayList<String> doubleArrayToStringArray(ArrayList<Double> doubleArray){
        ArrayList<String> stringArray = new ArrayList<>();
        int i = 0;
        while(doubleArray.size() > i) {
            try {
                stringArray.add(String.valueOf(doubleArray.get(i)));
            } catch (Exception e){
                e.printStackTrace();
                break;
            }
            i += 1;
        }
        return stringArray;
    }

//---------------------------------------------------------------------------------------------------------

    public static ArrayList<Double> intArrayToDoubleArray(ArrayList<Integer> intArray){
        ArrayList<Double> doubleArray = new ArrayList<>();
        int i = 0;
        while(intArray.size() > i) {
            try {
                doubleArray.add(Double.valueOf(intArray.get(i)));
            } catch (Exception e){
                e.printStackTrace();
                break;
            }
            i += 1;
        }
        return doubleArray;
    }
    public static ArrayList<Integer> doubleArrayToIntArray(ArrayList<Double> doubleArray){
        ArrayList<Integer> intArray = new ArrayList<>();
        int i = 0;
        while(doubleArray.size() > i) {
            try {
                intArray.add((int) ((double) doubleArray.get(i)));
            } catch (Exception e){
                e.printStackTrace();
                break;
            }
            i += 1;
        }
        return intArray;
    }
}
