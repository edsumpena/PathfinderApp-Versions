package App.Debugger;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.swing.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;

public class cmdLine {
    public static class debugger {

//---------------------------------------------------------------------------------------------------------

        public static void viewAllComponents(JLayeredPane layeredPane) {
            int q = 0;
            while (layeredPane.getComponentCount() > q) {
                System.out.println("DEBUGGER:  searched component: " + layeredPane.getComponent(q));
                q = q + 1;
            }
        }

//---------------------------------------------------------------------------------------------------------

        public static boolean checkForComponent(JLayeredPane layeredPane, String componentType, int xCoord1, int yCoord1,
                                                int xCoord2, int yCoord2, boolean dispDebug) {
            int q = 0;
            int compFound = 0;
            String comp = null;
            if (componentType.equals("paintComponent")) {
                comp = "$draw$";
            } else if (componentType.equals("label")) {
                comp = "JLabel";
            } else if (componentType.equals("combobox")) {
                comp = "JComboBox";
            } else if (componentType.equals("textfield")) {
                comp = "JTextField";
            } else if (componentType.equals("button")) {
                comp = "JButton";
            } else if (componentType.equals("passwordfield")) {
                comp = "JPasswordField";
            } else if (componentType.equals("radio")) {
                comp = "JRadioButton";
            } else if (componentType.equals("menu")) {
                comp = "JMenu";
            } else if (componentType.equals("checkbox")) {
                comp = "JCheckBox";
            } else if (componentType.equals("spinner")) {
                comp = "JSpinner";
            } else if (componentType.equals("slider")) {
                comp = "JSlider";
            } else if (componentType.equals("list")) {
                comp = "JList";
            } else {
                if (dispDebug)
                    System.out.println("DEBUGGER:  Component Type '" + componentType + "' is not a valid. Use 'dispComponentList()' to show list" +
                            "of valid component arguments.");
                compFound = 2;
            }
            while (layeredPane.getComponentCount() > q && comp != null) {
                if (layeredPane.getComponent(q).toString().contains("," + xCoord1 + "," + yCoord1 + "," + xCoord2 + "x" + yCoord2 + ",") &&
                        layeredPane.getComponent(q).toString().contains(comp)) {
                    if (dispDebug)
                        System.out.println("DEBUGGER:  Component Type = '" + componentType + "' (" + comp + "), Found in JLayeredPane = True, " +
                                "Component index = " + q + ", Component Coordinates = {" + xCoord1 + "," + yCoord1 + "," + xCoord2 + "x" + yCoord2 + "}");
                    compFound = 1;
                }
                q = q + 1;
            }
            if (compFound == 0) {
                if (dispDebug)
                    System.out.println("DEBUGGER:  Defined Component Type = '" + componentType + "' (" + comp + "), Found in JLayeredPane = False, " +
                            "Defined Component Coordinates = {" + xCoord1 + "," + yCoord1 + "," + xCoord2 + "x" + yCoord2 + "}");
                return true;
            } else {
                return false;
            }
        }

        public static boolean checkForComponent(JLayeredPane layeredPane, String componentType, double xCoord1, double yCoord1,
                                                double xCoord2, double yCoord2, boolean dispDebug) {
            int q = 0;
            int compFound = 0;
            String comp = null;
            if (componentType.equals("paintComponent")) {
                comp = "$draw$";
            } else if (componentType.equals("label")) {
                comp = "JLabel";
            } else if (componentType.equals("combobox")) {
                comp = "JComboBox";
            } else if (componentType.equals("textfield")) {
                comp = "JTextField";
            } else if (componentType.equals("button")) {
                comp = "JButton";
            } else if (componentType.equals("passwordfield")) {
                comp = "JPasswordField";
            } else if (componentType.equals("radio")) {
                comp = "JRadioButton";
            } else if (componentType.equals("menu")) {
                comp = "JMenu";
            } else if (componentType.equals("checkbox")) {
                comp = "JCheckBox";
            } else if (componentType.equals("spinner")) {
                comp = "JSpinner";
            } else if (componentType.equals("slider")) {
                comp = "JSlider";
            } else if (componentType.equals("list")) {
                comp = "JList";
            } else {
                if (dispDebug)
                    System.out.println("DEBUGGER:  Component Type '" + componentType + "' is not a valid. Use 'dispComponentList()' to show list" +
                            "of valid component arguments.");
                compFound = 2;
            }
            while (layeredPane.getComponentCount() > q && comp != null) {
                if (layeredPane.getComponent(q).toString().contains("," + xCoord1 + "," + yCoord1 + "," + xCoord2 + "x" + yCoord2 + ",") &&
                        layeredPane.getComponent(q).toString().contains(comp)) {
                    if (dispDebug)
                        System.out.println("DEBUGGER:  Component Type = '" + componentType + "' (" + comp + "), Found in JLayeredPane = True, " +
                                "Component index = " + q + ", Component Coordinates = {" + xCoord1 + "," + yCoord1 + "," + xCoord2 + "x" + yCoord2 + "}");
                    compFound = 1;
                }
                q = q + 1;
            }
            if (compFound == 0) {
                if (dispDebug)
                    System.out.println("DEBUGGER:  Defined Component Type = '" + componentType + "' (" + comp + "), Found in JLayeredPane = False, " +
                            "Defined Component Coordinates = {" + xCoord1 + "," + yCoord1 + "," + xCoord2 + "x" + yCoord2 + "}");
                return true;
            } else {
                return false;
            }
        }
        public static void dispComponentList() {
            System.out.println("DEBUGGER:  List of component arguments: 'paintComponent', 'label', 'combobox', 'textfield', " +
                    "'button', 'passwordfield', 'radio', 'menu', 'checkbox', 'spinner', 'slider', & 'list'.");
        }

//---------------------------------------------------------------------------------------------------------

        public static void dispVar(String name, String variable) {
            System.out.println("DEBUGGER:  Name = " + name + ", Value = " + variable + ", Type: String");
        }

        public static void dispVar(String name, int variable) {
            System.out.println("DEBUGGER:  Name = " + name + ", Value = " + variable + ", Type: int");
        }

        public static void dispVar(String name, double variable) {
            System.out.println("DEBUGGER:  Name = " + name + ", Value = " + variable + ", Type: double");
        }

        public static void dispVar(String name, ArrayList<Integer> arraylist, int index, int containedValue) {
            try {
                if (index < 0) {
                    if (containedValue == -1000) {
                        System.out.println("DEBUGGER:  Name = " + name + ", Size = " + arraylist.size() + ", " + name + ".get(0,1,2,3,4,5,6) = " +
                                arraylist.get(0) + "," + arraylist.get(1) + "," + arraylist.get(2) + "," + arraylist.get(3) + "," + arraylist.get(4) +
                                "," + arraylist.get(5) + "," + arraylist.get(6) + ", Type: ArrayList"
                        );
                    } else {
                        System.out.println("DEBUGGER:  Name = " + name + ", Size = " + arraylist.size() + ", " + name + ".get(0,1,2,3,4,5,6) = " +
                                arraylist.get(0) + "," + arraylist.get(1) + "," + arraylist.get(2) + "," + arraylist.get(3) + "," + arraylist.get(4) +
                                "," + arraylist.get(5) + "," + arraylist.get(6) + ", " + name + ".contains(" + containedValue + ") = " +
                                arraylist.contains(containedValue) + " @ FirstIndexOf = " + arraylist.indexOf(containedValue) + ", LastIndexOf = " +
                                arraylist.lastIndexOf(containedValue) + ", Type: ArrayList");
                    }
                } else {
                    if (containedValue == -1000) {
                        System.out.println("DEBUGGER:  Name = " + name + ", Size = " + arraylist.size() + ", " + name + ".get(" + index + ") = " +
                                arraylist.get(index) + ", " + ", Type: ArrayList"
                        );
                    } else {
                        System.out.println("DEBUGGER:  Name = " + name + ", Size = " + arraylist.size() + ", " + name + ".get(" + index + ") = " +
                                arraylist.get(index) + ", " + name + ".contains(" + containedValue + ") = " +
                                arraylist.contains(containedValue) + " @ FirstIndexOf = " + arraylist.indexOf(containedValue) + ", LastIndexOf = " +
                                arraylist.lastIndexOf(containedValue) + ", Type: ArrayList"
                        );
                    }
                }
            } catch (Exception e) {
                if (e.toString().contains("OutOfBounds")) {
                    if (index >= 0) {
                        System.out.println("DEBUGGER:  ArrayList = " + name + ", Error = ArrayIndexOutOfBounds Exception, Array Size = "
                                + arraylist.size() + ", Index Requested = " + index);
                    } else if (index < 0) {
                        System.out.println("DEBUGGER:  ArrayList = " + name + ", Error = ArrayIndexOutOfBounds Exception, Array Size = "
                                + arraylist.size() + ", Index Requested = [0, 1, 2, 3, 4, 5, 6]");
                    }
                } else {
                    System.out.println("DEBUGGER:  ArrayList = " + name + ", Error = Unknown, Non-ArrayIndexOutOfBounds Exception)");
                }
            }
        }

        public static void dispVar(String name, ArrayList<Double> arraylist, int index, double containedValue) {
            try {
                if (index < 0) {
                    if (containedValue == -1000.0) {
                        System.out.println("DEBUGGER:  Name = " + name + ", Size = " + arraylist.size() + ", " + name + ".get(0,1,2,3,4,5,6) = " +
                                arraylist.get(0) + "," + arraylist.get(1) + "," + arraylist.get(2) + "," + arraylist.get(3) + "," + arraylist.get(4) +
                                "," + arraylist.get(5) + "," + arraylist.get(6) + ", Type: ArrayList"
                        );
                    } else {
                        System.out.println("DEBUGGER:  Name = " + name + ", Size = " + arraylist.size() + ", " + name + ".get(0,1,2,3,4,5,6) = " +
                                arraylist.get(0) + "," + arraylist.get(1) + "," + arraylist.get(2) + "," + arraylist.get(3) + "," + arraylist.get(4) +
                                "," + arraylist.get(5) + "," + arraylist.get(6) + ", " + name + ".contains(" + containedValue + ") = " +
                                arraylist.contains(containedValue) + " @ FirstIndexOf = " + arraylist.indexOf(containedValue) + ", LastIndexOf = " +
                                arraylist.lastIndexOf(containedValue) + ", Type: ArrayList");
                    }
                } else {
                    if (containedValue == -1000.0) {
                        System.out.println("DEBUGGER:  Name = " + name + ", Size = " + arraylist.size() + ", " + name + ".get(" + index + ") = " +
                                arraylist.get(index) + ", " + ", Type: ArrayList"
                        );
                    } else {
                        System.out.println("DEBUGGER:  Name = " + name + ", Size = " + arraylist.size() + ", " + name + ".get(" + index + ") = " +
                                arraylist.get(index) + ", " + name + ".contains(" + containedValue + ") = " +
                                arraylist.contains(containedValue) + " @ FirstIndexOf = " + arraylist.indexOf(containedValue) + ", LastIndexOf = " +
                                arraylist.lastIndexOf(containedValue) + ", Type: ArrayList"
                        );
                    }
                }
            } catch (Exception e) {
                if (e.toString().contains("OutOfBounds")) {
                    if (index >= 0) {
                        System.out.println("DEBUGGER:  ArrayList = " + name + ", Error = ArrayIndexOutOfBounds Exception, Array Size = "
                                + arraylist.size() + ", Index Requested = " + index);
                    } else if (index < 0) {
                        System.out.println("DEBUGGER:  ArrayList = " + name + ", Error = ArrayIndexOutOfBounds Exception, Array Size = "
                                + arraylist.size() + ", Index Requested = [0, 1, 2, 3, 4, 5, 6]");
                    }
                } else {
                    System.out.println("DEBUGGER:  ArrayList = " + name + ", Error = Unknown, Non-ArrayIndexOutOfBounds Exception)");
                }
            }
        }

        public static void dispVar(String name, ArrayList<String> arraylist, int index, String containedValue) {
            try {
                if (index < 0) {
                    if (containedValue.equals("N/A")) {
                        System.out.println("DEBUGGER:  Name = " + name + ", Size = " + arraylist.size() + ", " + name + ".get(0,1,2,3,4,5,6) = " +
                                arraylist.get(0) + "," + arraylist.get(1) + "," + arraylist.get(2) + "," + arraylist.get(3) + "," + arraylist.get(4) +
                                "," + arraylist.get(5) + "," + arraylist.get(6) + ", Type: ArrayList"
                        );
                    } else {
                        System.out.println("DEBUGGER:  Name = " + name + ", Size = " + arraylist.size() + ", " + name + ".get(0,1,2,3,4,5,6) = " +
                                arraylist.get(0) + "," + arraylist.get(1) + "," + arraylist.get(2) + "," + arraylist.get(3) + "," + arraylist.get(4) +
                                "," + arraylist.get(5) + "," + arraylist.get(6) + ", " + name + ".contains(" + containedValue + ") = " +
                                arraylist.contains(containedValue) + " @ FirstIndexOf = " + arraylist.indexOf(containedValue) + ", LastIndexOf = " +
                                arraylist.lastIndexOf(containedValue) + ", Type: ArrayList");
                    }
                } else {
                    if (containedValue.equals("N/A")) {
                        System.out.println("DEBUGGER:  Name = " + name + ", Size = " + arraylist.size() + ", " + name + ".get(" + index + ") = " +
                                arraylist.get(index) + ", " + ", Type: ArrayList"
                        );
                    } else {
                        System.out.println("DEBUGGER:  Name = " + name + ", Size = " + arraylist.size() + ", " + name + ".get(" + index + ") = " +
                                arraylist.get(index) + ", " + name + ".contains(" + containedValue + ") = " +
                                arraylist.contains(containedValue) + " @ FirstIndexOf = " + arraylist.indexOf(containedValue) + ", LastIndexOf = " +
                                arraylist.lastIndexOf(containedValue) + ", Type: ArrayList"
                        );
                    }
                }
            } catch (Exception e) {
                if (e.toString().contains("OutOfBounds")) {
                    if (index >= 0) {
                        System.out.println("DEBUGGER:  ArrayList = " + name + ", Error = ArrayIndexOutOfBounds Exception, Array Size = "
                                + arraylist.size() + ", Index Requested = " + index);
                    } else if (index < 0) {
                        System.out.println("DEBUGGER:  ArrayList = " + name + ", Error = ArrayIndexOutOfBounds Exception, Array Size = "
                                + arraylist.size() + ", Index Requested = [0, 1, 2, 3, 4, 5, 6]");
                    }
                } else {
                    System.out.println("DEBUGGER:  ArrayList = " + name + ", Error = Unknown, Non-ArrayIndexOutOfBounds Exception)");
                }
            }
        }

        public static void dispVar(String name, ArrayList<Integer> arraylist, ArrayList<Integer> index, int containedValue) {
            try {
                while (index.size() < 7) {
                    index.add(0);
                }
                while (index.size() > 7) {
                    index.remove(index.size() - 1);
                }
                while (Collections.min(index) < 0 && Collections.min(index) != -1000) {
                    index.set(index.indexOf(Collections.min(index)), 0);
                }
                if (Collections.min(index) < 0) {
                    if (containedValue == -1000) {
                        System.out.println("DEBUGGER:  Name = " + name + ", Size = " + arraylist.size() + ", " + name + ".get(0,1,2,3,4,5,6) = " +
                                arraylist.get(0) + "," + arraylist.get(1) + "," + arraylist.get(2) + "," + arraylist.get(3) + "," + arraylist.get(4) +
                                "," + arraylist.get(5) + "," + arraylist.get(6) + ", Type: ArrayList"
                        );
                    } else {
                        System.out.println("DEBUGGER:  Name = " + name + ", Size = " + arraylist.size() + ", " + name + ".get(0,1,2,3,4,5,6) = " +
                                arraylist.get(0) + "," + arraylist.get(1) + "," + arraylist.get(2) + "," + arraylist.get(3) + "," + arraylist.get(4) +
                                "," + arraylist.get(5) + "," + arraylist.get(6) + ", " + name + ".contains(" + containedValue + ") = " +
                                arraylist.contains(containedValue) + " @ FirstIndexOf = " + arraylist.indexOf(containedValue) + ", LastIndexOf = " +
                                arraylist.lastIndexOf(containedValue) + ", Type: ArrayList");
                    }
                } else {
                    if (containedValue == -1000) {
                        System.out.println("DEBUGGER:  Name = " + name + ", Size = " + arraylist.size() + ", " + name + ".get(" + index.get(0)
                                + "," + index.get(1) + "," + index.get(2) + "," + index.get(3) + "," + index.get(4) + "," + index.get(5)
                                + "," + index.get(6) + ") = " + arraylist.get(index.get(0)) + "," + arraylist.get(index.get(1)) + "," +
                                arraylist.get(index.get(2)) + "," + arraylist.get(index.get(3)) + "," + arraylist.get(index.get(4))
                                + "," + arraylist.get(index.get(5)) + "," + arraylist.get(index.get(6)) + ", Type: ArrayList"
                        );
                    } else {
                        System.out.println("DEBUGGER:  Name = " + name + ", Size = " + arraylist.size() + ", " + name + ".get(" + index.get(0)
                                + "," + index.get(1) + "," + index.get(2) + "," + index.get(3) + "," + index.get(4) + "," + index.get(5)
                                + "," + index.get(6) + ") = " + arraylist.get(index.get(0)) + "," + arraylist.get(index.get(1)) + "," +
                                arraylist.get(index.get(2)) + "," + arraylist.get(index.get(3)) + "," + arraylist.get(index.get(4))
                                + "," + arraylist.get(index.get(5)) + "," + arraylist.get(index.get(6)) + name + ".contains(" + containedValue + ") = " +
                                arraylist.contains(containedValue) + " @ FirstIndexOf = " + arraylist.indexOf(containedValue) + ", LastIndexOf = " +
                                arraylist.lastIndexOf(containedValue) + ", Type: ArrayList"
                        );
                    }
                }
            } catch (Exception e) {
                if (e.toString().contains("OutOfBounds")) {
                    if (Collections.min(index) >= 0) {
                        System.out.println("DEBUGGER:  ArrayList = " + name + ", Error = ArrayIndexOutOfBounds Exception, Array Size = "
                                + arraylist.size() + ", Index Requested = " + index);
                    } else if (Collections.min(index) < 0) {
                        System.out.println("DEBUGGER:  ArrayList = " + name + ", Error = ArrayIndexOutOfBounds Exception, Array Size = "
                                + arraylist.size() + ", Index Requested = [0, 1, 2, 3, 4, 5, 6]");
                    }
                } else {
                    System.out.println("DEBUGGER:  ArrayList = " + name + ", Error = Unknown, Non-ArrayIndexOutOfBounds Exception)");
                }
            }
        }

        public static void dispVar(String name, ArrayList<Double> arraylist, ArrayList<Integer> index, double containedValue) {
            try {
                while (index.size() < 7) {
                    index.add(0);
                }
                while (index.size() > 7) {
                    index.remove(index.size() - 1);
                }
                while (Collections.min(index) < 0 && Collections.min(index) != -1000.0) {
                    index.set(index.indexOf(Collections.min(index)), 0);
                }
                if (Collections.min(index) < 0) {
                    if (containedValue == -1000.0) {
                        System.out.println("DEBUGGER:  Name = " + name + ", Size = " + arraylist.size() + ", " + name + ".get(0,1,2,3,4,5,6) = " +
                                arraylist.get(0) + "," + arraylist.get(1) + "," + arraylist.get(2) + "," + arraylist.get(3) + "," + arraylist.get(4) +
                                "," + arraylist.get(5) + "," + arraylist.get(6) + ", Type: ArrayList"
                        );
                    } else {
                        System.out.println("DEBUGGER:  Name = " + name + ", Size = " + arraylist.size() + ", " + name + ".get(0,1,2,3,4,5,6) = " +
                                arraylist.get(0) + "," + arraylist.get(1) + "," + arraylist.get(2) + "," + arraylist.get(3) + "," + arraylist.get(4) +
                                "," + arraylist.get(5) + "," + arraylist.get(6) + ", " + name + ".contains(" + containedValue + ") = " +
                                arraylist.contains(containedValue) + " @ FirstIndexOf = " + arraylist.indexOf(containedValue) + ", LastIndexOf = " +
                                arraylist.lastIndexOf(containedValue) + ", Type: ArrayList");
                    }
                } else {
                    if (containedValue == -1000.0) {
                        System.out.println("DEBUGGER:  Name = " + name + ", Size = " + arraylist.size() + ", " + name + ".get(" + index.get(0)
                                + "," + index.get(1) + "," + index.get(2) + "," + index.get(3) + "," + index.get(4) + "," + index.get(5)
                                + "," + index.get(6) + ") = " + arraylist.get(index.get(0)) + "," + arraylist.get(index.get(1)) + "," +
                                arraylist.get(index.get(2)) + "," + arraylist.get(index.get(3)) + "," + arraylist.get(index.get(4))
                                + "," + arraylist.get(index.get(5)) + "," + arraylist.get(index.get(6)) + ", " + ", Type: ArrayList"
                        );
                    } else {
                        System.out.println("DEBUGGER:  Name = " + name + ", Size = " + arraylist.size() + ", " + name + ".get(" + index.get(0)
                                + "," + index.get(1) + "," + index.get(2) + "," + index.get(3) + "," + index.get(4) + "," + index.get(5)
                                + "," + index.get(6) + ") = " + arraylist.get(index.get(0)) + "," + arraylist.get(index.get(1)) + "," +
                                arraylist.get(index.get(2)) + "," + arraylist.get(index.get(3)) + "," + arraylist.get(index.get(4))
                                + "," + arraylist.get(index.get(5)) + "," + arraylist.get(index.get(6)) + ", " + name + ".contains(" + containedValue + ") = " +
                                arraylist.contains(containedValue) + " @ FirstIndexOf = " + arraylist.indexOf(containedValue) + ", LastIndexOf = " +
                                arraylist.lastIndexOf(containedValue) + ", Type: ArrayList"
                        );
                    }
                }
            } catch (Exception e) {
                if (e.toString().contains("OutOfBounds")) {
                    if (Collections.min(index) >= 0) {
                        System.out.println("DEBUGGER:  ArrayList = " + name + ", Error = ArrayIndexOutOfBounds Exception, Array Size = "
                                + arraylist.size() + ", Index Requested = " + index);
                    } else if (Collections.min(index) < 0) {
                        System.out.println("DEBUGGER:  ArrayList = " + name + ", Error = ArrayIndexOutOfBounds Exception, Array Size = "
                                + arraylist.size() + ", Index Requested = [0, 1, 2, 3, 4, 5, 6]");
                    }
                } else {
                    System.out.println("DEBUGGER:  ArrayList = " + name + ", Error = Unknown, Non-ArrayIndexOutOfBounds Exception)");
                }
            }
        }

        public static void dispVar(String name, ArrayList<String> arraylist, ArrayList<Integer> index, String containedValue) {
            try {
                while (index.size() < 7) {
                    index.add(0);
                }
                while (index.size() > 7) {
                    index.remove(index.size() - 1);
                }
                while (Collections.min(index) < 0 && Collections.min(index) != -1000) {
                    index.set(index.indexOf(Collections.min(index)), 0);
                }
                if (Collections.min(index) < 0) {
                    if (containedValue.equals("N/A")) {
                        System.out.println("DEBUGGER:  Name = " + name + ", Size = " + arraylist.size() + ", " + name + ".get(0,1,2,3,4,5,6) = " +
                                arraylist.get(0) + "," + arraylist.get(1) + "," + arraylist.get(2) + "," + arraylist.get(3) + "," + arraylist.get(4) +
                                "," + arraylist.get(5) + "," + arraylist.get(6) + ", Type: ArrayList"
                        );
                    } else {
                        System.out.println("DEBUGGER:  Name = " + name + ", Size = " + arraylist.size() + ", " + name + ".get(0,1,2,3,4,5,6) = " +
                                arraylist.get(0) + "," + arraylist.get(1) + "," + arraylist.get(2) + "," + arraylist.get(3) + "," + arraylist.get(4) +
                                "," + arraylist.get(5) + "," + arraylist.get(6) + ", " + name + ".contains(" + containedValue + ") = " +
                                arraylist.contains(containedValue) + " @ FirstIndexOf = " + arraylist.indexOf(containedValue) + ", LastIndexOf = " +
                                arraylist.lastIndexOf(containedValue) + ", Type: ArrayList");
                    }
                } else {
                    if (containedValue.equals("N/A")) {
                        System.out.println("DEBUGGER:  Name = " + name + ", Size = " + arraylist.size() + ", " + name + ".get(" + index.get(0)
                                + "," + index.get(1) + "," + index.get(2) + "," + index.get(3) + "," + index.get(4) + "," + index.get(5)
                                + "," + index.get(6) + ") = " + arraylist.get(index.get(0)) + "," + arraylist.get(index.get(1)) + "," +
                                arraylist.get(index.get(2)) + "," + arraylist.get(index.get(3)) + "," + arraylist.get(index.get(4))
                                + "," + arraylist.get(index.get(5)) + "," + arraylist.get(index.get(6)) + ", Type: ArrayList"
                        );
                    } else {
                        System.out.println("DEBUGGER:  Name = " + name + ", Size = " + arraylist.size() + ", " + name + ".get(" + index.get(0)
                                + "," + index.get(1) + "," + index.get(2) + "," + index.get(3) + "," + index.get(4) + "," + index.get(5)
                                + "," + index.get(6) + ") = " + arraylist.get(index.get(0)) + "," + arraylist.get(index.get(1)) + "," +
                                arraylist.get(index.get(2)) + "," + arraylist.get(index.get(3)) + "," + arraylist.get(index.get(4))
                                + "," + arraylist.get(index.get(5)) + "," + arraylist.get(index.get(6)) + ", " + name + ".contains(" + containedValue + ") = " +
                                arraylist.contains(containedValue) + " @ FirstIndexOf = " + arraylist.indexOf(containedValue) + ", LastIndexOf = " +
                                arraylist.lastIndexOf(containedValue) + ", Type: ArrayList"
                        );
                    }
                }
            } catch (Exception e) {
                if (e.toString().contains("OutOfBounds")) {
                    if (Collections.min(index) >= 0) {
                        System.out.println("DEBUGGER:  ArrayList = " + name + ", Error = ArrayIndexOutOfBounds Exception, Array Size = "
                                + arraylist.size() + ", Index Requested = " + index);
                    } else if (Collections.min(index) < 0) {
                        System.out.println("DEBUGGER:  ArrayList = " + name + ", Error = ArrayIndexOutOfBounds Exception, Array Size = "
                                + arraylist.size() + ", Index Requested = [0, 1, 2, 3, 4, 5, 6]");
                    }
                } else {
                    System.out.println("DEBUGGER:  ArrayList = " + name + ", Error = Unknown, Non-ArrayIndexOutOfBounds Exception)");
                }
            }
        }

//---------------------------------------------------------------------------------------------------------

        public static int conditionChecker(String condition, boolean dispDebug) {
            int numOfOrs = countMatches(condition, "||");
            int numOfAnds = countMatches(condition, "&&");
            if (countMatches(condition.split("||", -2)[0], "&&") != 0 && numOfOrs != 0 && numOfAnds != 0) {
                numOfAnds = numOfAnds + 1;
            } else if (countMatches(condition.split("||", -2)[0], "&&") == 0 && numOfOrs != 0 && numOfAnds != 0){
                numOfOrs = numOfOrs + 1;
            } else if(numOfOrs == 0 && numOfAnds != 0){
                numOfAnds = numOfAnds + 1;
            } else if(numOfOrs != 0 && numOfAnds == 0){
                numOfOrs = numOfOrs + 1;
            }
            int numOfConditions = numOfAnds + numOfOrs;
            int returnValue = -2;
            if (numOfConditions <= 10) {
                ArrayList<String> andConditions = new ArrayList<>();
                ArrayList<String> andConditionComparers = new ArrayList<>();
                ArrayList<String> orConditions = new ArrayList<>();
                ArrayList<String> orConditionComparers = new ArrayList<>();
                String[] splitAnd = condition.split("&&", -2);
                int i = 0;
                while (splitAnd.length > i) {
                    if (!splitAnd[i].contains("||")) {
                        andConditions.add(splitAnd[i]);
                        splitAnd[i] = "null";
                    }
                    i = i + 1;
                }
                i = 0;
                while (splitAnd.length > i) {
                    if (!splitAnd[i].equals("null")) {
                        if (splitAnd[i].split("||", -2).length < 2) {
                            orConditions.add(splitAnd[i].split("||", -2)[0]);
                            orConditions.add(splitAnd[i].split("||", -2)[1]);
                        }
                    }
                    i = i + 1;
                }
                i = 0;
                while (andConditions.size() > i) {
                    if (andConditions.get(i).contains(">") && !andConditions.get(i).contains("=")) {
                        andConditionComparers.add(andConditions.get(i).split(">", -2)[0]);
                        andConditionComparers.add(andConditions.get(i).split(">", -2)[1]);
                        andConditionComparers.add("greaterThan");
                    } else if (andConditions.get(i).contains("<") && !andConditions.get(i).contains("=")) {
                        andConditionComparers.add(andConditions.get(i).split("<", -2)[0]);
                        andConditionComparers.add(andConditions.get(i).split("<", -2)[1]);
                        andConditionComparers.add("lessThan");
                    } else if (andConditions.get(i).contains("<=")) {
                        andConditionComparers.add(andConditions.get(i).split("<=", -2)[0]);
                        andConditionComparers.add(andConditions.get(i).split("<=", -2)[1]);
                        andConditionComparers.add("lessThanOrEqualTo");
                    } else if (andConditions.get(i).contains(">=")) {
                        andConditionComparers.add(andConditions.get(i).split(">=", -2)[0]);
                        andConditionComparers.add(andConditions.get(i).split(">=", -2)[1]);
                        andConditionComparers.add("greaterThanOrEqualTo");
                    } else if (andConditions.get(i).contains("==")) {
                        andConditionComparers.add(andConditions.get(i).split("==", -2)[0]);
                        andConditionComparers.add(andConditions.get(i).split("==", -2)[1]);
                        andConditionComparers.add("equalEqual");
                    } else if (andConditions.get(i).contains(".equals")) {
                        andConditionComparers.add(andConditions.get(i).split(".equals", -2)[0]);
                        andConditionComparers.add(andConditions.get(i).split(".equals", -2)[1]);
                        andConditionComparers.add(".equalsTo");
                    } else if (andConditions.get(i).contains(".equals") && andConditions.get(i).contains("!")) {
                        andConditionComparers.add(andConditions.get(i).split(".equals", -2)[0]);
                        andConditionComparers.add(andConditions.get(i).split(".equals", -2)[1]);
                        andConditionComparers.add("!.equalsTo");
                    } else {
                        if (!andConditions.get(i).contains("!")) {
                            andConditionComparers.add(andConditions.get(i));
                            andConditionComparers.add("N/A");
                            andConditionComparers.add("true");
                        } else {
                            andConditionComparers.add("N/A");
                            andConditionComparers.add("false");
                        }
                    }
                    i = i + 1;
                }
                i = 0;
                while (orConditions.size() > i) {
                    if (orConditions.get(i).contains(">")) {
                        orConditionComparers.add(orConditions.get(i).split(">", -2)[0]);
                        orConditionComparers.add(orConditions.get(i).split(">", -2)[1]);
                        orConditionComparers.add("greaterThan");
                    } else if (orConditions.get(i).contains("<")) {
                        orConditionComparers.add(orConditions.get(i).split("<", -2)[0]);
                        orConditionComparers.add(orConditions.get(i).split("<", -2)[1]);
                        orConditionComparers.add("lessThan");
                    } else if (andConditions.get(i).contains("<=")) {
                        orConditionComparers.add(orConditions.get(i).split("<=", -2)[0]);
                        orConditionComparers.add(orConditions.get(i).split("<=", -2)[1]);
                        orConditionComparers.add("lessThanOrEqualTo");
                    } else if (orConditions.get(i).contains(">=")) {
                        orConditionComparers.add(orConditions.get(i).split(">=", -2)[0]);
                        orConditionComparers.add(orConditions.get(i).split(">=", -2)[1]);
                        orConditionComparers.add("greaterThanOrEqualTo");
                    } else if (orConditions.get(i).contains("==")) {
                        orConditionComparers.add(orConditions.get(i).split("==", -2)[0]);
                        orConditionComparers.add(orConditions.get(i).split("==", -2)[1]);
                        orConditionComparers.add("equalEqual");
                    } else if (orConditions.get(i).contains(".equals")) {
                        orConditionComparers.add(orConditions.get(i).split(".equals", -2)[0]);
                        orConditionComparers.add(orConditions.get(i).split(".equals", -2)[1]);
                        orConditionComparers.add(".equalsTo");
                    } else if (orConditions.get(i).contains(".equals") && andConditions.get(i).contains("!")) {
                        orConditionComparers.add(orConditions.get(i).split(".equals", -2)[0]);
                        orConditionComparers.add(orConditions.get(i).split(".equals", -2)[1]);
                        orConditionComparers.add("!.equalsTo");
                    } else {
                        if (!orConditions.get(i).contains("!")) {
                            orConditionComparers.add(orConditions.get(i));
                            orConditionComparers.add("N/A");
                            orConditionComparers.add("true");
                        } else {
                            orConditionComparers.add(orConditions.get(i));
                            orConditionComparers.add("N/A");
                            orConditionComparers.add("false");
                        }
                    }
                    i = i + 1;
                }
                int getIndex = 0;
                ArrayList<String> conditionResults = new ArrayList<>();
                ScriptEngine engine = new ScriptEngineManager().getEngineByExtension("js");
                while (andConditionComparers.size() > getIndex) {
                    Object firstParameter = new Object();
                    Object secondParameter = new Object();
                    double intFirstParam = -1;
                    double intSecondParam = -1;
                    int comparerValue = -1;
                    try {
                        firstParameter = engine.eval(andConditionComparers.get(getIndex));
                        intFirstParam = (int) firstParameter;
                    } catch (Exception e) {
                        firstParameter = andConditionComparers.get(getIndex);
                    }
                    try {
                        if (!andConditionComparers.get(getIndex + 1).equals("N/A")) {
                            secondParameter = engine.eval(andConditionComparers.get(getIndex + 1));
                            intSecondParam = (int) secondParameter;
                        }
                    } catch (Exception e) {
                        secondParameter = andConditionComparers.get(getIndex + 1);
                    }
                    if (andConditionComparers.get(getIndex + 2).equals("greaterThan")) {
                        comparerValue = 0;
                    } else if (andConditionComparers.get(getIndex + 2).equals("lessThan")) {
                        comparerValue = 1;
                    } else if (andConditionComparers.get(getIndex + 2).equals("lessThanOrEqualTo")) {
                        comparerValue = 2;
                    } else if (andConditionComparers.get(getIndex + 2).equals("greaterThanOrEqualTo")) {
                        comparerValue = 3;
                    } else if (andConditionComparers.get(getIndex + 2).equals("equalEqual")) {
                        comparerValue = 4;
                    } else if (andConditionComparers.get(getIndex + 2).equals(".equalsTo")) {
                        comparerValue = 5;
                    } else if (andConditionComparers.get(getIndex + 2).equals("!.equalsTo")) {
                        comparerValue = 6;
                    } else if (andConditionComparers.get(getIndex + 2).equals("true")) {
                        comparerValue = 7;
                    } else if (andConditionComparers.get(getIndex + 2).equals("false")) {
                        comparerValue = 8;
                    }
                    if (intFirstParam != -1 && intSecondParam == -1 || intFirstParam == -1 && intSecondParam != -1) {
                        returnValue = -1;
                        if (dispDebug)
                            System.out.println("DEBUGGER:  Function = Condition Checker, Error = IncompatibleVariableTypes, " +
                                    "Cannot Compare int/double with java.lang.String (" + intFirstParam + " or " + intSecondParam +
                                    " = -1)");
                        break;
                    }
                    switch (comparerValue) {
                        case 0:
                            if (intFirstParam != -1 && intSecondParam != -1) {
                                if (intFirstParam > intSecondParam) {
                                    conditionResults.add("&&/greaterThan/true");
                                    if (dispDebug)
                                        System.out.println("&& " + intFirstParam + " > " + intSecondParam + ", Status: True");
                                } else {
                                    conditionResults.add("&&/greaterThan/false");
                                    if (dispDebug)
                                        System.out.println("&& " + intFirstParam + " > " + intSecondParam + ", Status: False");
                                }
                            } else if (intFirstParam == -1 && intSecondParam == -1) {
                                returnValue = -1;
                                if (dispDebug)
                                    System.out.println("DEBUGGER:  Function = Condition Checker, Error = IncompatibleVariableTypes, " +
                                            "Cannot apply operator '>' on java.lang.String ('" + firstParameter + "' > '" +
                                            secondParameter + "')");
                                break;
                            }
                            break;
                        case 1:
                            if (intFirstParam != -1 && intSecondParam != -1) {
                                if (intFirstParam < intSecondParam) {
                                    conditionResults.add("&&/lessThan/true");
                                    if (dispDebug)
                                        System.out.println("&& " + intFirstParam + " < " + intSecondParam + ", Status: True");
                                } else {
                                    conditionResults.add("&&/lessThan/false");
                                    if (dispDebug)
                                        System.out.println("&& " + intFirstParam + " < " + intSecondParam + ", Status: False");
                                }
                            } else if (intFirstParam == -1 && intSecondParam == -1) {
                                returnValue = -1;
                                if (dispDebug)
                                    System.out.println("DEBUGGER:  Function = Condition Checker, Error = IncompatibleVariableTypes, " +
                                            "Cannot apply operator '<' on java.lang.String ('" + firstParameter + "' < '" +
                                            secondParameter + "')");
                                break;
                            }
                            break;
                        case 2:
                            if (intFirstParam != -1 && intSecondParam != -1) {
                                if (intFirstParam <= intSecondParam) {
                                    conditionResults.add("&&/lessThanOrEqualTo/true");
                                    if (dispDebug)
                                        System.out.println("&& " + intFirstParam + " <= " + intSecondParam + ", Status: True");
                                } else {
                                    conditionResults.add("&&/lessThanOrEqualTo/false");
                                    if (dispDebug)
                                        System.out.println("&& " + intFirstParam + " <= " + intSecondParam + ", Status: False");
                                }
                            } else if (intFirstParam == -1 && intSecondParam == -1) {
                                returnValue = -1;
                                if (dispDebug)
                                    System.out.println("DEBUGGER:  Function = Condition Checker, Error = IncompatibleVariableTypes, " +
                                            "Cannot apply operator '<=' on java.lang.String ('" + firstParameter + "' <= '" +
                                            secondParameter + "')");
                                break;
                            }
                            break;
                        case 3:
                            if (intFirstParam != -1 && intSecondParam != -1) {
                                if (intFirstParam >= intSecondParam) {
                                    conditionResults.add("&&/greaterThanOrEqualTo/true");
                                    if (dispDebug)
                                        System.out.println("&& " + intFirstParam + " >= " + intSecondParam + ", Status: True");
                                } else {
                                    conditionResults.add("&&/greaterThanOrEqualTo/false");
                                    if (dispDebug)
                                        System.out.println("&& " + intFirstParam + " >= " + intSecondParam + ", Status: False");
                                }
                            } else if (intFirstParam == -1 && intSecondParam == -1) {
                                returnValue = -1;
                                if (dispDebug)
                                    System.out.println("DEBUGGER:  Function = Condition Checker, Error = IncompatibleVariableTypes, " +
                                            "Cannot apply operator '>=' on java.lang.String ('" + firstParameter + "' >= '" +
                                            secondParameter + "')");
                                break;
                            }
                            break;
                        case 4:
                            if (intFirstParam != -1 && intSecondParam != -1) {
                                if (intFirstParam == intSecondParam) {
                                    conditionResults.add("&&/equalEqual/true");
                                    if (dispDebug)
                                        System.out.println("&& " + intFirstParam + " == " + intSecondParam + ", Status: True");
                                } else {
                                    conditionResults.add("&&/equalEqual/false");
                                    if (dispDebug)
                                        System.out.println("&& " + intFirstParam + " == " + intSecondParam + ", Status: False");
                                }
                            } else if (intFirstParam == -1 && intSecondParam == -1) {
                                if (dispDebug)
                                    System.out.println("DEBUGGER:  Function = Condition Checker, Warning = Do not call '==' " +
                                            "when comparing object java.lang.String ('" + firstParameter + "' == '" +
                                            secondParameter + "')");
                                if (firstParameter == secondParameter) {
                                    conditionResults.add("&&/equalEqual/true");
                                    if (dispDebug)
                                        System.out.println("&& " + firstParameter + " == " + secondParameter + ", Status: True");
                                } else {
                                    conditionResults.add("&&/equalEqual/false");
                                    if (dispDebug)
                                        System.out.println("&& " + firstParameter + " > " + secondParameter + ", Status: False");
                                }
                            }
                            break;
                        case 5:
                            if (intFirstParam != -1 && intSecondParam != -1) {
                                returnValue = -1;
                                if (dispDebug)
                                    System.out.println("DEBUGGER:  Function = Condition Checker, Error = IncompatibleVariableTypes, " +
                                            "Cannot apply '.equals' on int/double ('" + intFirstParam + "' > '" +
                                            intSecondParam + "')");
                                break;
                            } else if (intFirstParam == -1 && intSecondParam == -1) {
                                if (firstParameter.equals(secondParameter)) {
                                    conditionResults.add("&&/.equalsTo/true");
                                    if (dispDebug)
                                        System.out.println("&& " + firstParameter + ".equals(" + secondParameter + "), Status: True");
                                } else {
                                    conditionResults.add("&&/.equalsTo/false");
                                    if (dispDebug)
                                        System.out.println("&& " + firstParameter + ".equals(" + secondParameter + "), Status: False");
                                }
                            }
                            break;
                        case 6:
                            if (intFirstParam != -1 && intSecondParam != -1) {
                                returnValue = -1;
                                if (dispDebug)
                                    System.out.println("DEBUGGER:  Function = Condition Checker, Warning = Do not call '.equals' " +
                                            "when comparing int/double ('" + intFirstParam + "'.equals('" +
                                            intSecondParam + "'))");
                                break;
                            } else if (intFirstParam == -1 && intSecondParam == -1) {
                                if (!firstParameter.equals(secondParameter)) {
                                    conditionResults.add("&&/!.equalsTo/true");
                                    if (dispDebug)
                                        System.out.println("&& !" + firstParameter + ".equals(" + secondParameter + "), Status: True");
                                } else {
                                    conditionResults.add("&&/!.equalsTo/false");
                                    if (dispDebug)
                                        System.out.println("&& !" + firstParameter + ".equals(" + secondParameter + "), Status: False");
                                }
                            }
                            break;
                        case 7:
                            if ((firstParameter.equals("true") || firstParameter.equals("false")) && secondParameter.equals("N/A")) {
                                if (firstParameter.equals("true")) {
                                    conditionResults.add("&&/true/true");
                                    if (dispDebug)
                                        System.out.println("&& " + firstParameter + ", Status: True");
                                } else {
                                    conditionResults.add("&&/true/false");
                                    if (dispDebug)
                                        System.out.println("&& " + firstParameter + ", Status: False");
                                }
                            } else if (!secondParameter.equals("N/A") && !firstParameter.equals("true") && !firstParameter.equals("false")) {
                                returnValue = -1;
                                if (dispDebug)
                                    System.out.println("DEBUGGER:  Function = Condition Checker, Error = InvalidVariableType, " +
                                            "Cannot do 'if(java.lang.String)' (Expected = 'if(boolean)')");
                                break;
                            }
                            break;
                        case 8:
                            if ((firstParameter.equals("true") || firstParameter.equals("false")) && secondParameter.equals("N/A")) {
                                if (firstParameter.equals("false")) {
                                    conditionResults.add("&&/false/true");
                                    if (dispDebug)
                                        System.out.println("&& !" + firstParameter + ", Status: True");
                                } else {
                                    conditionResults.add("&&/false/false");
                                    if (dispDebug)
                                        System.out.println("&& !" + firstParameter + ", Status: False");
                                }
                            } else if (!secondParameter.equals("N/A") && !firstParameter.equals("true") && !firstParameter.equals("false")) {
                                returnValue = -1;
                                if (dispDebug)
                                    System.out.println("DEBUGGER:  Function = Condition Checker, Error = InvalidVariableType, " +
                                            "Cannot do 'if(java.lang.String)' (Expected = 'if(boolean)')");
                                break;
                            }
                            break;
                    }
                    getIndex = getIndex + 3;
                }
                getIndex = 0;
                while (orConditionComparers.size() > getIndex) {
                    Object firstParameter = new Object();
                    Object secondParameter = new Object();
                    double intFirstParam = -1;
                    double intSecondParam = -1;
                    int comparerValue = -1;
                    try {
                        firstParameter = engine.eval(orConditionComparers.get(getIndex));
                        intFirstParam = (int) firstParameter;
                    } catch (Exception e) {
                        firstParameter = orConditionComparers.get(getIndex);
                    }
                    try {
                        if (!orConditionComparers.get(getIndex + 1).equals("N/A")) {
                            secondParameter = engine.eval(orConditionComparers.get(getIndex + 1));
                            intSecondParam = (int) secondParameter;
                        }
                    } catch (Exception e) {
                        secondParameter = orConditionComparers.get(getIndex + 1);
                    }
                    if (orConditionComparers.get(getIndex + 2).equals("greaterThan")) {
                        comparerValue = 0;
                    } else if (orConditionComparers.get(getIndex + 2).equals("lessThan")) {
                        comparerValue = 1;
                    } else if (orConditionComparers.get(getIndex + 2).equals("lessThanOrEqualTo")) {
                        comparerValue = 2;
                    } else if (orConditionComparers.get(getIndex + 2).equals("greaterThanOrEqualTo")) {
                        comparerValue = 3;
                    } else if (orConditionComparers.get(getIndex + 2).equals("equalEqual")) {
                        comparerValue = 4;
                    } else if (orConditionComparers.get(getIndex + 2).equals(".equalsTo")) {
                        comparerValue = 5;
                    } else if (orConditionComparers.get(getIndex + 2).equals("!.equalsTo")) {
                        comparerValue = 6;
                    } else if (orConditionComparers.get(getIndex + 2).equals("true")) {
                        comparerValue = 7;
                    } else if (orConditionComparers.get(getIndex + 2).equals("false")) {
                        comparerValue = 8;
                    }
                    if (intFirstParam != -1 && intSecondParam == -1 || intFirstParam == -1 && intSecondParam != -1) {
                        returnValue = -1;
                        if (dispDebug)
                            System.out.println("DEBUGGER:  Function = Condition Checker, Error = IncompatibleVariableTypes, " +
                                    "Cannot Compare int/double with java.lang.String (" + intFirstParam + " or " + intSecondParam +
                                    " = -1)");
                        break;
                    }
                        switch (comparerValue) {
                        case 0:
                            if (intFirstParam != -1 && intSecondParam != -1) {
                                if (intFirstParam > intSecondParam) {
                                    conditionResults.add("||/greaterThan/true");
                                    if (dispDebug)
                                        System.out.println("|| " + intFirstParam + " > " + intSecondParam + ", Status: True");
                                } else {
                                    conditionResults.add("||/greaterThan/false");
                                    if (dispDebug)
                                        System.out.println("|| " + intFirstParam + " > " + intSecondParam + ", Status: False");
                                }
                            } else if (intFirstParam == -1 && intSecondParam == -1) {
                                returnValue = -1;
                                if (dispDebug)
                                    System.out.println("DEBUGGER:  Function = Condition Checker, Error = IncompatibleVariableTypes, " +
                                            "Cannot apply operator '>' on java.lang.String ('" + firstParameter + "' > '" +
                                            secondParameter + "')");
                                break;
                            }
                            break;
                        case 1:
                            if (intFirstParam != -1 && intSecondParam != -1) {
                                if (intFirstParam < intSecondParam) {
                                    conditionResults.add("||/lessThan/true");
                                    if (dispDebug)
                                        System.out.println("|| " + intFirstParam + " < " + intSecondParam + ", Status: True");
                                } else {
                                    conditionResults.add("||/lessThan/false");
                                    if (dispDebug)
                                        System.out.println("|| " + intFirstParam + " < " + intSecondParam + ", Status: False");
                                }
                            } else if (intFirstParam == -1 && intSecondParam == -1) {
                                returnValue = -1;
                                if (dispDebug)
                                    System.out.println("DEBUGGER:  Function = Condition Checker, Error = IncompatibleVariableTypes, " +
                                            "Cannot apply operator '<' on java.lang.String ('" + firstParameter + "' < '" +
                                            secondParameter + "')");
                                break;
                            }
                            break;
                        case 2:
                            if (intFirstParam != -1 && intSecondParam != -1) {
                                if (intFirstParam <= intSecondParam) {
                                    conditionResults.add("||/lessThanOrEqualTo/true");
                                    if (dispDebug)
                                        System.out.println("|| " + intFirstParam + " <= " + intSecondParam + ", Status: True");
                                } else {
                                    conditionResults.add("||/lessThanOrEqualTo/false");
                                    if (dispDebug)
                                        System.out.println("|| " + intFirstParam + " <= " + intSecondParam + ", Status: False");
                                }
                            } else if (intFirstParam == -1 && intSecondParam == -1) {
                                returnValue = -1;
                                if (dispDebug)
                                    System.out.println("DEBUGGER:  Function = Condition Checker, Error = IncompatibleVariableTypes, " +
                                            "Cannot apply operator '<=' on java.lang.String ('" + firstParameter + "' <= '" +
                                            secondParameter + "')");
                                break;
                            }
                            break;
                        case 3:
                            if (intFirstParam != -1 && intSecondParam != -1) {
                                if (intFirstParam >= intSecondParam) {
                                    conditionResults.add("||/greaterThanOrEqualTo/true");
                                    if (dispDebug)
                                        System.out.println("|| " + intFirstParam + " >= " + intSecondParam + ", Status: True");
                                } else {
                                    conditionResults.add("||/greaterThanOrEqualTo/false");
                                    if (dispDebug)
                                        System.out.println("|| " + intFirstParam + " >= " + intSecondParam + ", Status: False");
                                }
                            } else if (intFirstParam == -1 && intSecondParam == -1) {
                                returnValue = -1;
                                if (dispDebug)
                                    System.out.println("DEBUGGER:  Function = Condition Checker, Error = IncompatibleVariableTypes, " +
                                            "Cannot apply operator '>=' on java.lang.String ('" + firstParameter + "' >= '" +
                                            secondParameter + "')");
                                break;
                            }
                            break;
                        case 4:
                            if (intFirstParam != -1 && intSecondParam != -1) {
                                if (intFirstParam == intSecondParam) {
                                    conditionResults.add("||/equalEqual/true");
                                    if (dispDebug)
                                        System.out.println("|| " + intFirstParam + " == " + intSecondParam + ", Status: True");
                                } else {
                                    conditionResults.add("||/equalEqual/false");
                                    if (dispDebug)
                                        System.out.println("|| " + intFirstParam + " == " + intSecondParam + ", Status: False");
                                }
                            } else if (intFirstParam == -1 && intSecondParam == -1) {
                                if (dispDebug)
                                    System.out.println("DEBUGGER:  Function = Condition Checker, Warning = Do not call '==' " +
                                            "when comparing object java.lang.String ('" + firstParameter + "' == '" +
                                            secondParameter + "')");
                                if (firstParameter == secondParameter) {
                                    conditionResults.add("||/equalEqual/true");
                                    if (dispDebug)
                                        System.out.println("|| " + firstParameter + " == " + secondParameter + ", Status: True");
                                } else {
                                    conditionResults.add("||/equalEqual/false");
                                    if (dispDebug)
                                        System.out.println("|| " + firstParameter + " == " + secondParameter + ", Status: False");
                                }
                            }
                            break;
                        case 5:
                            if (intFirstParam != -1 && intSecondParam != -1) {
                                returnValue = -1;
                                if (dispDebug)
                                    System.out.println("DEBUGGER:  Function = Condition Checker, Error = IncompatibleVariableTypes, " +
                                            "Cannot apply '.equals' on int/double ('" + intFirstParam + "' > '" +
                                            intSecondParam + "')");
                                break;
                            } else if (intFirstParam == -1 && intSecondParam == -1) {
                                if (firstParameter.equals(secondParameter)) {
                                    conditionResults.add("||/.equalsTo/true");
                                    if (dispDebug)
                                        System.out.println("|| " + firstParameter + ".equals(" + secondParameter + "), Status: True");
                                } else {
                                    conditionResults.add("||/.equalsTo/false");
                                    if (dispDebug)
                                        System.out.println("|| " + firstParameter + ".equals(" + secondParameter + "), Status: False");
                                }
                            }
                            break;
                        case 6:
                            if (intFirstParam != -1 && intSecondParam != -1) {
                                returnValue = -1;
                                if (dispDebug)
                                    System.out.println("DEBUGGER:  Function = Condition Checker, Warning = Do not call '.equals' " +
                                            "when comparing int/double ('" + intFirstParam + "'.equals('" +
                                            intSecondParam + "'))");
                                break;
                            } else if (intFirstParam == -1 && intSecondParam == -1) {
                                if (!firstParameter.equals(secondParameter)) {
                                    conditionResults.add("||/!.equalsTo/true");
                                    if (dispDebug)
                                        System.out.println("|| !" + firstParameter + ".equals(" + secondParameter + "), Status: True");
                                } else {
                                    conditionResults.add("||/!.equalsTo/true");
                                    if (dispDebug)
                                        System.out.println("|| !" + firstParameter + ".equals(" + secondParameter + "), Status: False");
                                }
                            }
                            break;
                        case 7:
                            if ((firstParameter.equals("true") || firstParameter.equals("false")) && secondParameter.equals("N/A")) {
                                if (firstParameter.equals("true")) {
                                    conditionResults.add("||/true/true");
                                    if (dispDebug)
                                        System.out.println("|| " + firstParameter + ", Status: True");
                                } else {
                                    conditionResults.add("||/true/false");
                                    if (dispDebug)
                                        System.out.println("|| " + firstParameter + ", Status: False");
                                }
                            } else if (!secondParameter.equals("N/A") && !firstParameter.equals("true") && !firstParameter.equals("false")) {
                                returnValue = -1;
                                if (dispDebug)
                                    System.out.println("DEBUGGER:  Function = Condition Checker, Error = InvalidVariableType, " +
                                            "Cannot do 'if(java.lang.String)' (Expected = 'if(boolean)')");
                                break;
                            }
                            break;
                        case 8:
                            if ((firstParameter.equals("true") || firstParameter.equals("false")) && secondParameter.equals("N/A")) {
                                if (firstParameter.equals("false")) {
                                    conditionResults.add("||/false/true");
                                    if (dispDebug)
                                        System.out.println("|| !" + firstParameter + ", Status: True");
                                } else {
                                    conditionResults.add("||/false/false");
                                    if (dispDebug)
                                        System.out.println("|| !" + firstParameter + ", Status: False");
                                }
                            } else if (!secondParameter.equals("N/A") && !firstParameter.equals("true") && !firstParameter.equals("false")) {
                                returnValue = -1;
                                if (dispDebug)
                                    System.out.println("DEBUGGER:  Function = Condition Checker, Error = InvalidVariableType, " +
                                            "Cannot do 'if(java.lang.String)' (Expected = 'if(boolean)')");
                                break;
                            }
                            break;
                    }
                    getIndex = getIndex + 3;
                }
                if (returnValue != -1) {
                    int q = 0;
                    int falseOrCounter = 0;
                    while (conditionResults.size() > q) {
                        if (conditionResults.get(q).contains("&&") && conditionResults.get(q).contains("false")) {
                            returnValue = 0;
                            break;
                        }
                        if (conditionResults.get(q).contains("||") && conditionResults.get(q).contains("false")) {
                            falseOrCounter = falseOrCounter + 1;
                            if (falseOrCounter >= numOfOrs) {
                                returnValue = 0;
                                break;
                            }
                        }
                        q = q + 1;
                    }
                    if (returnValue != 0) {
                        returnValue = 1;
                    }
                }
                switch (returnValue) {
                    case -2:
                        if (dispDebug)
                            System.out.println("DEBUGGER: Output = Error, No output value determined (Return Value = " + returnValue + ")");
                        break;
                    case -1:
                        if (dispDebug)
                            System.out.println("DEBUGGER: Output = Error, An Exception was thrown (Return Value = " + returnValue + ")");
                        break;
                    case 0:
                        if (dispDebug)
                            System.out.println("DEBUGGER: Output = False (Return Value = " + returnValue + ")");
                        break;
                    case 1:
                        if (dispDebug)
                            System.out.println("DEBUGGER: Output = True (Return Value = " + returnValue + ")");
                        break;
                }
                return returnValue;
            } else {
                if (dispDebug)
                    System.out.println("DEBUGGER:  Function = Condition Checker, Error = TooManyInputConditions, " +
                            "Max Number of Conditions = 7, " + "Specified Number of Conditions = " + numOfConditions);
                return -1;
            }
        }
        private static int countMatches(String str, String sub) {
            if (str.equals("") || sub.equals("")) {
                return 0;
            }
            int count = 0;
            int idx = 0;
            while ((idx = str.indexOf(sub, idx)) != -1) {
                count++;
                idx += sub.length();
            }
            return count;
        }
    }
}
