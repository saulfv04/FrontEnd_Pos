package pos.presentation.Cajeros;

import pos.Application;
import pos.logic.Cajero;

import javax.swing.*;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;

public class View implements PropertyChangeListener {
    private JLabel searchNombreLbl;
    private JTextField searchNombre;
    private JButton report;
    private JButton search;
    private JTable listCajeros;
    private JLabel idLbl;
    private JTextField idTetx;
    private JLabel NombreLbl;
    private JTextField JTextNombre;
    private JButton save;
    private JButton delete;
    private JButton clear;
    private JPanel panel;


    public JPanel getPanel() {
        return panel;
    }

    // MVC
    Model model;
    Controller controller;


    public View() {
        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (validate()) {
                    Cajero n = take();
                    try {
                        controller.save(n);
                        JOptionPane.showMessageDialog(panel, "REGISTRO APLICADO", "", JOptionPane.INFORMATION_MESSAGE);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(panel, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        delete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    controller.delete();
                    JOptionPane.showMessageDialog(panel, "REGISTRO BORRADO", "", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(panel, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        clear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.clear();
            }
        });
        search.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Cajero filter = new Cajero ();
                    filter.setId(searchNombre.getText());
                    controller.search(filter);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(panel, ex.getMessage(), "Información", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
        listCajeros.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = listCajeros.getSelectedRow();
                controller.edit(row);
            }
        });
        report.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    controller.print();
                    if(Desktop.isDesktopSupported()){
                        File myFile = new File("cajeros.pdf");
                        Desktop.getDesktop().open(myFile);
                    }
                }catch (Exception ex){}
            }
        });
    }

    private boolean validate() {
        boolean valid = true;
        if (idTetx.getText().isEmpty()) {
            valid = false;
            idLbl.setBorder(Application.BORDER_ERROR);
            idLbl.setToolTipText("Codigo requerido");
        } else {
            idLbl.setBorder(null);
            idLbl.setToolTipText(null);
        }

        if (JTextNombre.getText().isEmpty()) {
            valid = false;
            NombreLbl.setBorder(Application.BORDER_ERROR);
            NombreLbl.setToolTipText("Nombre requerido");
        } else {
            NombreLbl.setBorder(null);
            NombreLbl.setToolTipText(null);
        }


        return valid;
    }

    public Cajero take() {
        Cajero e = new Cajero();
        e.setId(idTetx.getText());
        e.setNombre(JTextNombre.getText());
        return e;
    }


    public void setModel(Model model ) {
        this.model = model;
        model.addPropertyChangeListener(this);
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }


    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        switch (evt.getPropertyName()) {
            case Model.LIST:
                int[] cols = {TableModel.ID, TableModel.NOMBRE};
                listCajeros.setModel(new TableModel(cols, model.getList()));
                listCajeros.setRowHeight(30);
                TableColumnModel columnModel = listCajeros.getColumnModel();
                columnModel.getColumn(0).setPreferredWidth(150);
                columnModel.getColumn(1).setPreferredWidth(150);
                break;
            case Model.CURRENT:
                idTetx.setText(model.getCurrent().getId());
                JTextNombre.setText(model.getCurrent().getNombre());

                if (model.getMode() == Application.MODE_EDIT) {
                    idTetx.setEnabled(false);
                    delete.setEnabled(true);
                } else {
                    idTetx.setEnabled(true);
                    delete.setEnabled(false);
                }

                idLbl.setBorder(null);
                idLbl.setToolTipText(null);
                NombreLbl.setBorder(null);
                NombreLbl.setToolTipText(null);
                break;
            case Model.FILTER:
                searchNombre.setText(model.getFilter().getId());
                break;
        }

        this.panel.revalidate();
    }
}
