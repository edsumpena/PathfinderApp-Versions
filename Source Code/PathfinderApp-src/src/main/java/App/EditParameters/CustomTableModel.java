package App.EditParameters;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;

public class CustomTableModel extends AbstractTableModel {

    ArrayList<Parameter> parameters;

    public CustomTableModel() {
        parameters = new ArrayList<>();
    }

    @Override
    public int getRowCount() {
        return parameters.size();
    }

    @Override
    public int getColumnCount() {
        return 4;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Parameter p = parameters.get(rowIndex);
        switch(columnIndex){
            case 0:
                return p.type;
            case 1:
                return p.name;
            case 2:
                return p.description;
            case 3:
                return p.value;
        }

        return null;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex == 3; // Only value should be editable from table
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        Parameter p = parameters.get(rowIndex);
        switch(columnIndex){
            case 0:
                p.type = ParameterType.valueOf((String)aValue);
                break;
            case 1:
                p.name = aValue.toString();
                break;
            case 2:
                p.description = aValue.toString();
                break;
            case 3:
                p.value = aValue.toString();
                break;
        }

        fireTableCellUpdated(rowIndex, columnIndex);
    }

    public void addRow(Parameter p) {
        parameters.add(p);
    }

    public Parameter getRow(int index) {
        return parameters.get(index);
    }


    String[] columnNames = {
            "Type",
            "Name",
            "Description",
            "Value"
    };

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }
}
