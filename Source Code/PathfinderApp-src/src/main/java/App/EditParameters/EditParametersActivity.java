package App.EditParameters;

import javax.swing.*;

public class EditParametersActivity {
    private JPanel panel;
    private JTable parametersTable;
    private JTextField name;
    private JTextArea description;
    private JButton refresh;
    private JButton save;

    // region accessors
    public JPanel getPanel() {
        return panel;
    }

    public JTable getParametersTable() {
        return parametersTable;
    }

    public JTextField getName() {
        return name;
    }

    public JTextArea getDescription() {
        return description;
    }

    public JButton getRefresh() {
        return refresh;
    }

    public JButton getSave() {
        return save;
    }
    // endregion

    public EditParametersActivity() {
        parametersTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        Controller controller = new Controller(this, new ParametersTable(parametersTable));
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("EditParametersActivity");
        frame.setContentPane(new EditParametersActivity().panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        ParameterScannerLocal.scan();
    }


}
