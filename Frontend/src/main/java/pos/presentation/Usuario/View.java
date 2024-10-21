package pos.presentation.Usuario;

import pos.Application;
import pos.logic.Usuarios;
import pos.presentation.Cajeros.TableModel;

import javax.swing.*;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

public class View implements PropertyChangeListener {
    private JPanel panel1;
    private JTable TableUsuarios;

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
    public void addListaUsuarios(String usuario){
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






    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        switch (evt.getPropertyName()) {
            case pos.presentation.Usuario.Model.LIST:
                int[] cols = {pos.presentation.Usuario.TableModel.ID};
                TableUsuarios.setModel(new TableModel(cols,new ArrayList<>()));
                TableUsuarios.setRowHeight(30);
                TableColumnModel columnModel = TableUsuarios.getColumnModel();
                columnModel.getColumn(0).setPreferredWidth(150);
                break;
            case pos.presentation.Cajeros.Model.CURRENT:
        }
    }
}
