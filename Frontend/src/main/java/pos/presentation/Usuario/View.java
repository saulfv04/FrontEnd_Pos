package pos.presentation.Usuario;

import pos.Application;
import pos.logic.Factura;
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
                Factura currentFactura = Application.faturasController.getCurrent();
                if (currentFactura != null && currentFactura.getLinea() != null && !currentFactura.getLinea().isEmpty() &&
                        currentFactura.getCliente() != null && currentFactura.getCajero() != null) {

                    int selectedRow = TableUsuarios.getSelectedRow();
                    if (selectedRow != -1) {
                        Usuarios usuario = model.getListUsuarios().get(selectedRow);
                        controller.send(currentFactura, usuario);
                        Application.faturasController.setCurrent(new Factura());
                        Application.faturasController.facturaEnviadaReinicio();
                        TableUsuarios.clearSelection();
                    } else {
                        JOptionPane.showMessageDialog(panel1, "Por favor, seleccione un usuario de la tabla.");
                    }
                } else {
                    JOptionPane.showMessageDialog(panel1, "La factura actual no es válida. Verifique que tenga líneas, cliente y cajero.");
                    TableUsuarios.clearSelection();
                }
            }
        });

        recibirButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = TableUsuarios.getSelectedRow();
                if (selectedRow != -1) {
                    Usuarios usuario = model.getListUsuarios().get(selectedRow);
                    if (!usuario.getListaFacturas().isEmpty()) {
                        Factura factura = usuario.getListaFacturas().getFirst();
                        Application.faturasController.addFactura(factura);
                        usuario.getListaFacturas().removeFirst();
                        model.setUsuarioEspecifico(usuario);
                        TableUsuarios.clearSelection();
                    } else {
                        JOptionPane.showMessageDialog(panel1, "Esta persona no ha enviado más facturas.");
                    }
                } else {
                    JOptionPane.showMessageDialog(panel1, "Por favor, seleccione un usuario de la tabla.");
                }
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
                modelaux.marcarString(model.getListUsuarios());
                TableUsuarios.setModel(modelaux);
                TableUsuarios.setRowHeight(30);
                TableColumnModel columnModel1 = TableUsuarios.getColumnModel();
                columnModel1.getColumn(1).setPreferredWidth(150);
                break;
        }
    }


}
