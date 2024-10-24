package pos.presentation.Usuario;

import pos.Application;
import pos.logic.Usuarios;

import javax.swing.*;
import javax.swing.table.TableColumnModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

public class View implements PropertyChangeListener {
    private JPanel panel1;
    private JTable TableUsuarios;
    private JButton buttonEnviar;
    private JButton recibirButton;

    Model model;
    Controller controller;

    public JPanel getPanel(){
        return panel1;
    }

    public void setPanel1(JPanel panel1) {
        this.panel1 = panel1;
    }

    public void setUsuarios(Usuarios usuario){
        this.model.setCurrent(usuario);
    }
    public void addListaUsuarios(Usuarios usuario){
        this.model.addUsuario(usuario);
    }

    public Model getModel() {
        return model;
    }

    public void setModel(Model model) {
        this.model = model;
        model.addPropertyChangeListener(this);
    }

    public Controller getController() {
        return controller;
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public View() {
        buttonEnviar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.send(Application.faturasController.getCurrent(),TableUsuarios.getValueAt(TableUsuarios.getSelectedRow(),0).toString());
            }
        });
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        switch (evt.getPropertyName()) {
            case Model.LISTUSUARIOS:
                int[] cols = {pos.presentation.Usuario.TableModel.ID, TableModel.FACTURA};
                TableUsuarios.setModel(new TableModel(cols,model.getListUsuarios()));
                TableUsuarios.setRowHeight(30);
                TableColumnModel columnModel = TableUsuarios.getColumnModel();
                columnModel.getColumn(1).setPreferredWidth(150);
                break;
            case Model.LISTA_FACTURAS:
                int[] cols1 = {pos.presentation.Usuario.TableModel.ID, TableModel.FACTURA};
                TableModel modelaux = new TableModel(cols1,model.getListUsuarios());
                modelaux.marcarString(model.getUsuarioEspecifico());
                TableUsuarios.setModel(modelaux);
                TableUsuarios.setRowHeight(30);
                TableColumnModel columnModel1 = TableUsuarios.getColumnModel();
                columnModel1.getColumn(1).setPreferredWidth(150);
                break;
        }
    }


}
