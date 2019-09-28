package ParamsGenerator;

import java.io.*;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Params {
    public static String genMotors(String mtrNames, String mtrTypes) {
        ArrayList<String> names = new ArrayList<>();
        ArrayList<String> types = new ArrayList<>();
        int index = 0;
        String output = "Unknown Error occurred while Generating String";
        try {
            mtrNames = mtrNames.replaceAll(" ", "");
            for (int i = 0; i <= mtrNames.length() - 1; i++) {
                if (String.valueOf(mtrNames.charAt(i)).equals(",") || i == mtrNames.length() - 1) {
                    if (i != mtrNames.length() - 1)
                        names.add("\"" + mtrNames.substring(index, i) + "\"");
                    else
                        names.add("\"" + mtrNames.substring(index) + "\"");
                    index = i + 1;
                }
            }
            index = 0;
            for (int i = 0; i <= mtrTypes.length() - 1; i++) {
                if (String.valueOf(mtrTypes.charAt(i)).equals(",") || i == mtrTypes.length() - 1) {
                    if (i != mtrTypes.length() - 1)
                        types.add("\"" + mtrTypes.substring(index, i) + "\"");
                    else
                        types.add("\"" + mtrTypes.substring(index) + "\"");
                    index = i + 1;
                }
            }
        } catch (Exception e){
            e.printStackTrace();
            return output;
        }
        if(!names.isEmpty() && !types.isEmpty()) {
            if (names.size() == types.size()) {
                output = "Names:" + String.valueOf(names).replaceAll(" ", "") +
                        ",Types;" + String.valueOf(types).replaceAll(" ", "") +
                        "|  *Only change/add values. DO NOT CHANGE \":\", \";\", \"|\", or spaces! " +
                        "Types of motors should be one of three values: \"DCWheel\",\"DCArm\", or \"Servo\".";
                return output;
            } else {
                output = "MismatchArraySizeError-" + names.size() + "!=" + types.size();
                return output;
            }
        } else {
            output = "EmptyArrayError" + names.isEmpty() + " " + types.isEmpty();
            return output;
        }
    }

    public static String genConstants(String driveConstants) {
        int index = 0;
        ArrayList<String> constants = new ArrayList<>();
        String output = "Unknown Error occurred while Generating String";
        try {
            driveConstants = driveConstants.replaceAll(" ", "");
            for (int i = 0; i <= driveConstants.length() - 1; i++) {
                if (String.valueOf(driveConstants.charAt(i)).equals(",") || i == driveConstants.length() - 1) {
                    if (i != driveConstants.length() - 1)
                        constants.add(driveConstants.substring(index, i));
                    else
                        constants.add(driveConstants.substring(index));
                    index = i + 1;
                }
            }
        } catch (Exception e){
            e.printStackTrace();
            return output;
        }
        if(!constants.isEmpty()) {
            if(constants.size() == 4) {
                output = "MaxVel:" + constants.get(0) + ",MaxAcc;" + constants.get(1) + ",MaxAngVel=" + constants.get(2) +
                        ",MaxAngAcc-" + constants.get(3) + "|  *Only change numbers. DO NOT CHANGE \":\", \";\", \"=\", \"-\", \"|\", or ANY spaces!";
                return output;
            } else {
                output = "MismatchArraySizeError-Size " + constants.size() + " is not == 4";
                return output;
            }
        } else {
            output = "EmptyArrayError " + constants.isEmpty();
            return output;
        }
    }

    public static void createFile(String path, String name, String motor, String constants){
        File file = new File(path + "\\" + name + ".prm");
        file.getParentFile().mkdir();
        file.setExecutable(true);
        file.setReadable(true);
        file.setWritable(true);
        try {
            file.createNewFile();
        } catch (Exception e) {

        }
        if (!file.exists()) {
            if (file.mkdir()) {
            } else {
            }
        }
        try {
            if (file.delete()) {
            } else {
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            File f = new File(path + "\\" + name + "Motors.mtr");
            FileWriter fw = new FileWriter(f, true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(motor);
            bw.flush();
            bw.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        try {
            File f = new File(path + "\\" + name + "DriveConstants.csts");
            FileWriter fw = new FileWriter(f, true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(constants);
            bw.flush();
            bw.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        byte[] buffer = new byte[1024];

        try{
            FileOutputStream fos = new FileOutputStream(path + "\\" + name + ".prms");
            ZipOutputStream zos = new ZipOutputStream(fos);
            ZipEntry ze= new ZipEntry(name + "Motors.mtr");
            zos.putNextEntry(ze);
            FileInputStream in = new FileInputStream(path + "\\" + name + "Motors.mtr");

            int len;
            while ((len = in.read(buffer)) > 0) {
                zos.write(buffer, 0, len);
            }
            ze= new ZipEntry(name + "DriveConstants.csts");
            zos.putNextEntry(ze);
            in = new FileInputStream(path + "\\" + name + "DriveConstants.csts");

            int len2;
            while ((len2 = in.read(buffer)) > 0) {
                zos.write(buffer, 0, len2);
            }
            in.close();
            zos.closeEntry();

            //remember close it
            zos.close();
        }catch(IOException ex){
            ex.printStackTrace();
        }
        System.gc();
        File tempFolder = new File(path + "\\" + name + "Motors.mtr");
        tempFolder.setExecutable(true);
        tempFolder.setReadable(true);
        tempFolder.setWritable(true);
        tempFolder.delete();
        File tempFolder2 = new File(path + "\\" + name + "DriveConstants.csts");
        tempFolder2.setExecutable(true);
        tempFolder2.setReadable(true);
        tempFolder2.setWritable(true);
        tempFolder2.delete();
        File tempFolder3 = new File(path + "\\" + name + ".prm");
        tempFolder3.setExecutable(true);
        tempFolder3.setReadable(true);
        tempFolder3.setWritable(true);
        tempFolder3.delete();
    }
}
