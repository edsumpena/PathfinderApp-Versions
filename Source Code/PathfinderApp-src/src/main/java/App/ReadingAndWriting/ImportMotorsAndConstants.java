package App.ReadingAndWriting;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

public class ImportMotorsAndConstants {
    public static double[] getDriveConstants(String path){
        double[] values = {-1,-1,-1,-1};
        String rawString = "Error Reading File";
        try{
            FileReader fr = new FileReader(path);   //"res/Constants/DriveConstants.txt"
            BufferedReader br = new BufferedReader(fr);
            while(br.ready()){
                rawString = br.readLine();
            }
        }catch(Exception e){
            System.out.println(e);
        }

        try {
            values[0] = Double.valueOf(rawString.substring(rawString.indexOf(":") + 1, rawString.indexOf(";") - 8));
        } catch (Exception e){
            System.out.println("ERROR READING 'MaxVel' in 'DriveConstants.txt' (DO NOT RENAME OR CHANGE FILE FORMAT)!");
        }

        try {
            values[1] = Double.valueOf(rawString.substring(rawString.indexOf(";") + 1, rawString.indexOf("=") - 11));
        } catch (Exception e){
            System.out.println("ERROR READING 'MaxAcc' in 'DriveConstants.txt' (DO NOT RENAME OR CHANGE FILE FORMAT)!");
        }

        try {
            values[2] = Double.valueOf(rawString.substring(rawString.indexOf("=") + 1, rawString.indexOf("-") - 11));
        } catch (Exception e){
            System.out.println("ERROR READING 'MaxAngVel' in 'DriveConstants.txt' (DO NOT RENAME OR CHANGE FILE FORMAT)!");
        }

        try {
            values[3] = Double.valueOf(rawString.substring(rawString.indexOf("-") + 1, rawString.indexOf("|") - 1));
        } catch (Exception e){
            System.out.println("ERROR READING 'MaxAngAcc' in 'DriveConstants.txt' (DO NOT RENAME OR CHANGE FILE FORMAT)!");
        }
        return values;
    }

    public static ArrayList<ArrayList<String>> getMotors(String path){
        ArrayList<String> names = new ArrayList<>();
        ArrayList<String> types = new ArrayList<>();
        String rawString = "Error Reading File";
        try{
            FileReader fr = new FileReader(path);   //"res/Constants/Motors.txt"
            BufferedReader br = new BufferedReader(fr);
            while(br.ready()){
                rawString = br.readLine();
            }
        }catch(Exception e){
            System.out.println(e);
        }
        int charsLong;
        for(int i = rawString.indexOf(":") + 2; i <= rawString.indexOf(";") - 9; i += charsLong){
            charsLong = 0;
            i += 1;
            for(int x = i; !String.valueOf(rawString.charAt(x)).equals("\""); x++){
                charsLong += 1;
            }
            names.add(rawString.substring(i, i + charsLong));
            charsLong += 2;
        }

        for(int i = rawString.indexOf(";") + 2; i <= rawString.indexOf("|") - 3; i += charsLong){
            charsLong = 0;
            i += 1;
            for(int x = i; !String.valueOf(rawString.charAt(x)).equals("\""); x++){
                charsLong += 1;
            }
            types.add(rawString.substring(i, i + charsLong));
            charsLong += 2;
        }
        ArrayList<ArrayList<String>> output = new ArrayList<>();
        output.add(names);
        output.add(types);

        return output;
    }
}
