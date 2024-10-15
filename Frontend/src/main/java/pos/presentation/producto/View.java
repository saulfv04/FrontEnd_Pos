package pos.presentation.producto;

import pos.Application;
import pos.logic.Categoria;
import pos.logic.Producto;

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
    private JPanel panel;
    private JLabel searchNombreLbl;
    private JTextField searchNombre;
    private JButton report;
    private JButton search;
    private JTable listProoductos;
    private JLabel idLbl;
    private JTextField id;
    private JLabel DescripcionLbl;
    private JTextField nombre;
    private JLabel UnidadLbl;
    private JTextField unidad;
    private JLabel preciolLbl;
    private JTextField JLabelPrecio;
    private JLabel CategoriaLbl;
    private JTextField categoria;
    private JButton save;
    private JButton delete;
    private JButton clear;

    private JPanel panel1;
    private JTextField textFExistencias;
    private JLabel existenciasLabel;
    private JComboBox<Categoria> comboBoxCategoria;


    public JPanel getPanel() {
        return panel1;
    }
    // MVC
    Model model;
    Controller controller;


    private boolean validate() {
        boolean valid = true;
        if (id.getText().isEmpty()) {
            valid = false;
            idLbl.setBorder(Application.BORDER_ERROR);
            idLbl.setToolTipText("Codigo requerido");
        } else {
            idLbl.setBorder(null);
            idLbl.setToolTipText(null);
        }

        if (nombre.getText().isEmpty()) {
            valid = false;
            DescripcionLbl.setBorder(Application.BORDER_ERROR);
            DescripcionLbl.setToolTipText("Descripcion requerida");
        } else {
            DescripcionLbl.setBorder(null);
            DescripcionLbl.setToolTipText(null);
        }

        if (unidad.getText().isEmpty()) {
            valid = false;
            UnidadLbl.setBorder(Application.BORDER_ERROR);
            UnidadLbl.setToolTipText("Unidad De Medida requerida");
        } else {
            UnidadLbl.setBorder(null);
            UnidadLbl.setToolTipText(null);
        }
        if (JLabelPrecio.getText().isEmpty()) {
            valid = false;
            preciolLbl.setBorder(Application.BORDER_ERROR);
            preciolLbl.setToolTipText("Precio requerido");
        } else if (Double.parseDouble(JLabelPrecio.getText()) < 0) {
            valid = false;
            preciolLbl.setBorder(Application.BORDER_ERROR);
            preciolLbl.setToolTipText("Precio no puede ser negativo");
        } else {
            preciolLbl.setBorder(null);
            preciolLbl.setToolTipText(null);
        }
        if (comboBoxCategoria.getSelectedItem() == null) {
            valid = false;
            CategoriaLbl.setBorder(Application.BORDER_ERROR);
            CategoriaLbl.setToolTipText("Categoria requerida");
        }else{
            CategoriaLbl.setBorder(null);
            CategoriaLbl.setToolTipText(null);
        }
        if (textFExistencias.getText().isEmpty()) {
            valid = false;
            existenciasLabel.setBorder(Application.BORDER_ERROR);
            existenciasLabel.setToolTipText("Existencia de productos requerida");
        } else if (Integer.parseInt(textFExistencias.getText()) <= 0) {
            valid = false;
            existenciasLabel.setBorder(Application.BORDER_ERROR);
            existenciasLabel.setToolTipText("Existencia no puede ser nula o negativa");
        } else {
            existenciasLabel.setBorder(null);
            existenciasLabel.setToolTipText(null);
        }
        try {
            Integer.parseInt(textFExistencias.getText());
            existenciasLabel.setBorder(null);
            existenciasLabel.setToolTipText(null);
        } catch (Exception e) {
            valid = false;
            existenciasLabel.setBorder(Application.BORDER_ERROR);
            existenciasLabel.setToolTipText("Existencias invalidas");
        }

        try {
            Float.parseFloat(JLabelPrecio.getText());
            preciolLbl.setBorder(null);
            preciolLbl.setToolTipText(null);
        } catch (Exception e) {
            valid = false;
            preciolLbl.setBorder(Application.BORDER_ERROR);
            preciolLbl.setToolTipText("Precio invalido");
        }

        return valid;
    }

    public Producto take() {
        Producto e = new Producto();
        e.setCodigo(id.getText());
        e.setDescripcion(nombre.getText());
        e.setUnidadDeMedidad(unidad.getText());
        e.setPrecioUnitario(Double.parseDouble(JLabelPrecio.getText())); // Asegúrate que sea un JTextField
        e.setExistencia(Integer.parseInt(textFExistencias.getText()));
        Object selectedItem = this.comboBoxCategoria.getSelectedItem();
        e.setCategoria((Categoria) selectedItem);
        return e;
    }
    public void setController( Controller controller) {
        this.controller = controller;
    }

    public void setModel(Model model) {
        this.model = model;
        model.addPropertyChangeListener(this);
    }


    public View() {
        search.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Producto filter = new Producto();
                    filter.setCodigo(searchNombre.getText());
                    controller.search(filter);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(panel, ex.getMessage(), "Información", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (validate()) {
                    Producto n = take();
                    try {
                        controller.save(n);
                        JOptionPane.showMessageDialog(panel, "REGISTRO APLICADO", "", JOptionPane.INFORMATION_MESSAGE);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(panel, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        listProoductos.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = listProoductos.getSelectedRow();
                controller.edit(row);
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

        report.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    controller.print();
                    if(Desktop.isDesktopSupported()){
                        File myFile = new File("productos.pdf");
                        Desktop.getDesktop().open(myFile);
                    }
                }catch (Exception ex){}
            }
        });
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        switch (evt.getPropertyName()) {
            case Model.LIST:
                int[] cols = {TableModel.ID,  TableModel.DESCRIPCION, TableModel.PRECIO, TableModel.CATEGORIA, TableModel.EXISTENCIAS};
                listProoductos.setModel(new TableModel(cols, model.getList()));
                listProoductos.setRowHeight(30);
                TableColumnModel columnModel = listProoductos.getColumnModel();
                columnModel.getColumn(1).setPreferredWidth(150);
                columnModel.getColumn(3).setPreferredWidth(150);
                break;
            case Model.CURRENT:
                id.setText(model.getCurrent().getCodigo());
                nombre.setText(model.getCurrent().getDescripcion());
                JLabelPrecio.setText(String.valueOf(model.getCurrent().getPrecioUnitario()));
                comboBoxCategoria.setSelectedItem(model.getCurrent().getCategoria());
                unidad.setText("" + model.getCurrent().getUnidadDeMedidad());
                textFExistencias.setText(String.valueOf(model.getCurrent().getExistencia()));

                if (model.getMode() == Application.MODE_EDIT) {
                    id.setEnabled(false);
                    delete.setEnabled(true);
                } else {
                    id.setEnabled(true);
                    delete.setEnabled(false);
                }

                idLbl.setBorder(null);
                idLbl.setToolTipText(null);
                DescripcionLbl.setBorder(null);
                DescripcionLbl.setToolTipText(null);
                CategoriaLbl.setBorder(null);
                CategoriaLbl.setToolTipText(null);
                UnidadLbl.setBorder(null);
                UnidadLbl.setToolTipText(null);
                JLabelPrecio.setBorder(null);
                JLabelPrecio.setToolTipText(null);
                textFExistencias.setBorder(null);
                break;
            case Model.CATEGORIAS:
                comboBoxCategoria.setModel(new DefaultComboBoxModel(model.getCategorias().toArray()));
                break;
            case Model.FILTER:
                searchNombre.setText(model.getFilter().getCodigo());
                break;
        }

        this.panel.revalidate();
    }



}
