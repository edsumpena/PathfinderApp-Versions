package App.ReadingAndWriting;

import java.util.ArrayList;

public class MotorSetup {
    static ArrayList<ArrayList<String>> listOfMotors = new ArrayList<>();
    static ArrayList<String> motorNames = new ArrayList<>();
    static ArrayList<String> motorTypes = new ArrayList<>();

    public static void exportMotors(ArrayList<String> motors, ArrayList<String> types){
        if(motors.size() == types.size()){
            int i = 0;
            String x = "";
            boolean allowedToRun = true;
            while(motors.size() > i){
                x = motors.get(i);
                if(motors.indexOf(x) != motors.lastIndexOf(x)){
                    System.out.println("MOTOR_SETUP:  Error = Dupicate Names not Allowed (Name: " + x + ")");
                    allowedToRun = false;
                    break;
                }
                i = i + 1;
            }
            if(allowedToRun) {
                motorNames = motors;
                motorTypes = types;
                if(!checkMotorTypes(motorTypes).contains("Error")){
                    listOfMotors.add(motorNames);
                    listOfMotors.add(motorTypes);
                } else {
                    System.out.println("MOTOR_SETUP:  " + checkMotorTypes(motorTypes));
                }
            }
        } else {
            System.out.println("MOTOR_SETUP:  Error = Array sizes do not match! (" + motors.size() + " != " + types.size() + ")");
        }
    }

    public static ArrayList<ArrayList<String>> importMotors(){
        if(listOfMotors.isEmpty()){
            ArrayList<ArrayList<String>> nullList = new ArrayList<>();
            ArrayList<String> null1 = new ArrayList<>();
            ArrayList<String> null2 = new ArrayList<>();
            null1.add("Error: You must export a list of motors before importing!");
            null1.add("null");
            null1.add("null");
            null2.add("Error: You must export a list of motors before importing!");
            null2.add("null");
            null2.add("null");
            nullList.add(null1);
            nullList.add(null2);
            return nullList;
        } else {
            return listOfMotors;
        }
    }

    private static String checkMotorTypes(ArrayList<String> motorTypes){
        String status = "Success";
        int i = 0;
        if(!motorTypes.isEmpty()) {
            while (motorTypes.size() > i) {
                if (motorTypes.get(i).equalsIgnoreCase("DCWheel")) {
                    motorTypes.set(i,"DCWheel");
                } else if (motorTypes.get(i).equalsIgnoreCase("DCArm")) {
                    motorTypes.set(i,"DCArm");
                } else if (motorTypes.get(i).equalsIgnoreCase("Servo")) {
                    motorTypes.set(i,"Servo");
                } else {
                    status = "Error = Motor type '" + motorTypes.get(i) + "' not found! (Index: " + i + ")";
                    break;
                }
                i += 1;
            }
        } else {
            status = "Error = Input motorType ArrayList is empty!";
        }
        return status;
    }

    public static String checkMissingMotors(ArrayList<String> names){
        String missing = "You are missing the following Motors: None";
        for(int i = 0; i < names.size(); i++){
            if((!MotorSetup.importMotors().get(0).contains(names.get(i).substring(0, names.get(i).indexOf("(") - 1)) ||
                    !MotorSetup.importMotors().get(1).contains(names.get(i).substring(names.get(i).indexOf("(") + 1,
                            names.get(i).indexOf(")")))) && !missing.contains(names.get(i).substring(0, names.get(i).indexOf("(") - 1) +
                    ", Type = " + names.get(i).substring(names.get(i).indexOf("(") + 1, names.get(i).indexOf(")")))){
                if(missing.contains("None"))
                    missing = "You are missing the following Motors: Name = " + names.get(i).substring(0, names.get(i).indexOf("(") - 1) +
                            ", Type = " + names.get(i).substring(names.get(i).indexOf("(") + 1, names.get(i).indexOf(")"));
                else
                    missing = missing + "; Name = " + names.get(i).substring(0, names.get(i).indexOf("(") - 1) +
                            ", Type = " + names.get(i).substring(names.get(i).indexOf("(") + 1, names.get(i).indexOf(")"));
            }
        }
        return missing;
    }
}
