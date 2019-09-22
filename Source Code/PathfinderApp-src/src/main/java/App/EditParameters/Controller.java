package App.EditParameters;

import javax.swing.event.ChangeEvent;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import java.awt.event.ActionEvent;
import java.util.List;
import java.util.Vector;

public class Controller {
    private EditParametersActivity view;
    private ParametersTable model;


    public Controller(EditParametersActivity view, ParametersTable model) {
        this.view = view;
        this.model = model;

        initialize();
        model.addTestData();
    }

    private void initialize() {
        model.getTable().getSelectionModel().addListSelectionListener(this::selectionChanged);
        view.getRefresh().addActionListener(this::refresh);
        view.getSave().addActionListener(this::save);
        SwingUtils.addChangeListener(view.getDescription(), this::descriptionChanged);
    }

    public void save(ActionEvent event) {
        System.out.println(Serialize.serialize(model.tableModel.parameters));

    }

    public void refresh(ActionEvent event) {
        List<Parameter> parameterList = ParameterScannerLocal.scan();

    }

    private void selectionChanged(ListSelectionEvent evt) {
        if (!evt.getValueIsAdjusting()) {
            // Selection changed

            int index = model.table.getSelectedRow();
            System.out.println("Selection changed: " + index);

            Parameter p = model.tableModel.getRow(index);

            view.getDescription().setText(p.description);
            view.getName().setText(p.name);
        }
    }

    private void descriptionChanged(ChangeEvent e) {
        model.tableModel.setValueAt(view.getDescription().getText(), model.table.getSelectedRow(), 2);
    }
}
