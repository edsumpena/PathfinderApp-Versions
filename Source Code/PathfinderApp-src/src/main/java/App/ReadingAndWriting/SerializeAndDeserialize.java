package App.ReadingAndWriting;

import java.io.*;
import java.util.ArrayList;

public class SerializeAndDeserialize {
    public static void serialize(ArrayList<Integer> circles, ArrayList<ArrayList<String>> lines, ArrayList<Integer> arm,
                                 String filePath, String pathName, String traj, String motors, String armParams) {
        File file = new File(filePath + "\\" + pathName + ".path");
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
            FileOutputStream fos = new FileOutputStream(filePath + "\\" + pathName + "Circles.cir");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(circles);
            oos.flush();
            oos.close();
            fos.flush();
            fos.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        try {
            FileOutputStream fos2 = new FileOutputStream(filePath + "\\" + pathName + "Traj.line");
            ObjectOutputStream oos2 = new ObjectOutputStream(fos2);
            oos2.writeObject(lines);
            oos2.flush();
            oos2.close();
            fos2.flush();
            fos2.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        try {
            FileOutputStream fos3 = new FileOutputStream(filePath + "\\" + pathName + "Params.arm");
            ObjectOutputStream oos3 = new ObjectOutputStream(fos3);
            oos3.writeObject(arm);
            oos3.flush();
            oos3.close();
            fos3.flush();
            fos3.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        try {
            File f = new File(filePath + "\\" + pathName + "Json.traj");
            FileWriter fw = new FileWriter(f, true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.newLine();
            bw.write("TRAJ:" + traj + ",MOTORS;" + motors + ",ARM_" + armParams);
            bw.flush();
            bw.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public static ArrayList deserialize(String filePathAndName, boolean stringArrayList) {

        FileInputStream fis;
        ObjectInputStream ois;

        // creating List reference to hold AL values
        // after de-serialization
        if (stringArrayList) {
            ArrayList<ArrayList<String>> deseralizedStringArrayList = new ArrayList<>();

            try {
                // reading binary data
                fis = new FileInputStream(filePathAndName);

                // converting binary-data to java-object
                ois = new ObjectInputStream(fis);

                // reading object's value and casting ArrayList<String>
                deseralizedStringArrayList = (ArrayList<ArrayList<String>>) ois.readObject();
            } catch (FileNotFoundException fnfex) {
                fnfex.printStackTrace();
            } catch (IOException ioex) {
                ioex.printStackTrace();
            } catch (ClassNotFoundException ccex) {
                ccex.printStackTrace();
            } catch (Exception e){
                e.printStackTrace();
            }
            return deseralizedStringArrayList;
        } else {
            ArrayList<Integer> deseralizedIntArrayList = null;

            try {
                // reading binary data
                fis = new FileInputStream(filePathAndName);

                // converting binary-data to java-object
                ois = new ObjectInputStream(fis);

                // reading object's value and casting ArrayList<String>
                deseralizedIntArrayList = (ArrayList<Integer>) ois.readObject();
            } catch (FileNotFoundException fnfex) {
                fnfex.printStackTrace();
            } catch (IOException ioex) {
                ioex.printStackTrace();
            } catch (ClassNotFoundException ccex) {
                ccex.printStackTrace();
            }
            return deseralizedIntArrayList;
        }
    }

    public static String readJson(String filePathAndName){
        String jsonTraj = "Error reading file!";
        try{
            FileReader fr = new FileReader(filePathAndName);
            BufferedReader br = new BufferedReader(fr);
            while(br.ready()){
                jsonTraj = br.readLine();
            }
        }catch(Exception e){
            System.out.println(e);
        }
        return jsonTraj;
    }
}
