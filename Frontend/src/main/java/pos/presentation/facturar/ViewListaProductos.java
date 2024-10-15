package pos.presentation.facturar;

import pos.logic.Linea;
import pos.logic.Producto;
import pos.presentation.producto.TableModel;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;


public class ViewListaProductos implements PropertyChangeListener {
    private JTextField textFieldDescripcion;
    private JButton cancelButton;
    private JButton OKButton;
    private JTable table1;
    private JPanel JPanelPrincipal;
    // MVC
    Model model;
    Controller controller;

    private Timer searchTimer;

    private boolean productSelected = false;

    public JPanel getPanel() {
        return JPanelPrincipal;
    }

    public void setModel(Model model) {
        this.model = model;
        model.addPropertyChangeListener(this);
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public ViewListaProductos() {
        searchTimer = new Timer(300, e -> updateProductList()); // 300 ms debounce
        searchTimer.setRepeats(false);

        table1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                productSelected = true;
                if (e.getClickCount() == 2) {
                    if(controller.getModel().getCurrent().getCliente().getId().isEmpty()){
                        JOptionPane.showMessageDialog(JPanelPrincipal, "Seleccione un cliente", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    Linea current = new Linea();
                    Producto currentProd = controller.getCurrentProducto();
                    int selectedRow = table1.getSelectedRow();
                    currentProd.setCodigo(table1.getValueAt(selectedRow, 0).toString());
                    try {
                        current.setProducto(controller.leerProducto(currentProd));

                        if(current.getProducto().getExistencia() == 0){
                            JOptionPane.showMessageDialog(JPanelPrincipal, "Producto sin existencias", "Error", JOptionPane.ERROR_MESSAGE);
                            return;
                        }

                        current.setDescuento(controller.getModel().getCurrent().getCliente().getDescuento());
                        controller.createLinea(current);

                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(JPanelPrincipal, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                    Window window = SwingUtilities.getWindowAncestor(JPanelPrincipal);
                    if (window != null) {
                        window.dispose();
                    }
                }
            }
        });

        cancelButton.addActionListener(e -> {
            Window window = SwingUtilities.getWindowAncestor(JPanelPrincipal);
            if (window != null) {
                window.dispose();
            }
            resetFields();
        });

        textFieldDescripcion.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                startSearchTimer();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                startSearchTimer();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                startSearchTimer();
            }

            private void startSearchTimer() {
                if (!productSelected) {
                    if (searchTimer.isRunning()) {
                        searchTimer.restart();
                    } else {
                        searchTimer.start();
                    }
                }
            }
        });
        OKButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Linea current = new Linea();
                Producto currentProd = controller.getCurrentProducto();
                int selectedRow = table1.getSelectedRow();
                currentProd.setCodigo(table1.getValueAt(selectedRow, 0).toString());

                try {
                    current.setProducto(controller.leerProducto(currentProd));
                    controller.createLinea(current);

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(JPanelPrincipal, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
                Window window = SwingUtilities.getWindowAncestor(JPanelPrincipal);
                if (window != null) {
                    window.dispose();
                }
            }
        });
    }

    private void updateProductList() {
        SwingUtilities.invokeLater(() -> {
            try {

                String descripcion = textFieldDescripcion.getText();
                Producto filter = new Producto();
                filter.setCodigo(descripcion);


                controller.search(filter);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(JPanelPrincipal, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private void resetFields() {
        textFieldDescripcion.setText("");
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        SwingUtilities.invokeLater(() -> {
            switch (evt.getPropertyName()) {
                case Model.LISTAPRODUCTOS:
                    int[] cols = {TableModel.ID, TableModel.DESCRIPCION,
                            TableModel.PRECIO, TableModel.CATEGORIA,
                            TableModel.EXISTENCIAS};
                    table1.setModel(new TableModel(cols, model.getListP()));
                    table1.setRowHeight(30);
                    TableColumnModel columnModel = table1.getColumnModel();
                    columnModel.getColumn(1).setPreferredWidth(150);
                    columnModel.getColumn(3).setPreferredWidth(150);
                    break;
                case Model.FILTERPRODUCTO:
                    textFieldDescripcion.setText("");
                    break;
            }

            this.JPanelPrincipal.revalidate();
        });
    }
}
