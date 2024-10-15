package pos.presentation.Historico;

import pos.Application;
import pos.logic.Factura;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.ArrayList;

public class View implements PropertyChangeListener {

    private JPanel panel;
    private JLabel searchNombreLbl;
    private JTextField searchNombre;
    private JButton report;
    private JButton search;
    private JTable listaFacturasHistorico;
    private JTable listaLineaProductosHistorico;

    public View() {




        search.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (validate()) {
                    try {
                        Factura filter = new Factura();
                        filter.setId(searchNombre.getText());
                        controller.search(filter);

                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(panel, ex.getMessage(), "Información", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            }
        });
        listaFacturasHistorico.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    int selectedRow = listaFacturasHistorico.getSelectedRow();
                    if (selectedRow != -1) {
                        controller.setListaLineas(model.getListaFacturas().get(selectedRow).getId()); // Cambiado de getLinea() a getLineas()
                    }
                }
            }
        });
        report.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    controller.print();
                    if(Desktop.isDesktopSupported()){
                        File myFile = new File("facturas.pdf");
                        Desktop.getDesktop().open(myFile);
                    }
                }catch (Exception ex){}
            }
        });
        panel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                super.componentShown(e);
                updateTable();
            }
        });
    }

    public JPanel getPanel() {
        return panel;
    }

    public boolean validate() {
        boolean valid = true;
        if (searchNombre.getText().isEmpty()) {
            valid = false;
            searchNombreLbl.setBorder(Application.BORDER_ERROR);
            searchNombreLbl.setToolTipText("Codigo requerido");
        } else {
            searchNombreLbl.setBorder(null);
            searchNombreLbl.setToolTipText(null);
        }
        return valid;
    }

    Model model;
    Controller controller;

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public void setModel(Model model) {
        this.model = model;
        model.addPropertyChangeListener((PropertyChangeListener) this);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        switch (evt.getPropertyName()) {
            case Model.LISTAFACTURAS:
                int[] cols = {TableModel.ID, TableModel.IDCLIENTE,
                        TableModel.IDCAJERO, TableModel.FECHA,
                        TableModel.TOTAL};
                listaFacturasHistorico.setModel(new TableModel(cols, model.getListaFacturas() != null ? model.getListaFacturas() : new ArrayList<>()));
                listaFacturasHistorico.setRowHeight(30);
                TableColumnModel columnModel = listaFacturasHistorico.getColumnModel();
                columnModel.getColumn(0).setPreferredWidth(150);
                columnModel.getColumn(4).setPreferredWidth(150);
                break;
            case Model.LISTALINEAS:
                int[] linecols = {TableModelProductos.ID, TableModelProductos.NOMBRE, TableModelProductos.CATEGORIA,
                        TableModelProductos.CANTIDAD, TableModelProductos.PRECIO, TableModelProductos.DESCUENTO, TableModelProductos.NETO,
                        TableModelProductos.IMPORTE};
                listaLineaProductosHistorico.setModel(new TableModelProductos(linecols, model.getListaLineas() != null ? model.getListaLineas() : new ArrayList<>()));
                listaLineaProductosHistorico.setRowHeight(30);
                TableColumnModel columnModelLineas = listaLineaProductosHistorico.getColumnModel();
                columnModelLineas.getColumn(0).setPreferredWidth(150);
                columnModelLineas.getColumn(7).setPreferredWidth(150);
                updateTable();
                break;
        }
        this.panel.revalidate();
    }

    public void updateTable() {
        controller.search(new Factura());
    }



}