package App.EditParameters;

import javax.script.Bindings;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class ParametersTable {
    CustomTableModel tableModel;
    JTable table;


    public ParametersTable(JTable table) {
        this.table = table;
        tableModel = new CustomTableModel();
        table.setModel(tableModel);
    }

    public void addTestData() {
        Parameter[] testData = {
                new Parameter(ParameterType.BOOLEAN, "Test2", "description", "true"),
                new Parameter(ParameterType.DOUBLE, "Test", "Description", "3")
        };

        for (int i = 0; i < testData.length; i++) {
            tableModel.addRow(testData[i]);
        }

    }

    public JTable getTable() {
        return table;
    }

    public CustomTableModel getTableModel() {
        return tableModel;
    }
}
