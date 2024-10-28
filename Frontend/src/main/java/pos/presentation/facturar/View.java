package pos.presentation.facturar;

import pos.Application;
import pos.logic.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import java.util.Locale;

public class View implements PropertyChangeListener {


    private JComboBox<Cliente> comboBoxClientes;
    private JComboBox<Cajero> comboBoxCajero;
    private JTable listaFactura;
    private JTextField searchProductoText;
    private JButton agregarButton;
    private JButton cobrarButton;
    private JButton buscarButton;
    private JButton cantidadButton;
    private JButton quitarButton;
    private JButton descuentoButton;
    private JButton cancelarButton;

    public JPanel getPanel() {
        return panel;
    }
    Factura f= new Factura();

    private ViewListaProductos viewListaProductos = new ViewListaProductos();
    private ViewFacturarCobrar viewFacturarCobrar = new ViewFacturarCobrar();

    private JPanel panel;
    private JLabel jLabelSubTotalPrecio;
    private JLabel LbalCantidadArticulos;
    private JLabel jPrecioDescuento;
    private JLabel jLabelPrecioFinal;
    private JLabel CodigoAgregar;


    // MVC
    Model model;
    Controller controller;

    public void setModel(Model model) {
        this.model = model;
        model.addPropertyChangeListener((PropertyChangeListener) this);
        viewListaProductos.setModel(model);
        viewFacturarCobrar.setModel(model);
    }

    public void setController(Controller controller) {
        this.controller = controller;
        viewListaProductos.setController(controller);
        viewFacturarCobrar.setController(controller);

    }

    public void updateComboBoxCajeros(){
        List<Cajero> nombresCajeros = controller.getCajero();
            addItemsToComboBoxCajero(comboBoxCajero, nombresCajeros);
    }

    public void updateComboBoxClientes(){
        List<Cliente> nombresClientes = controller.getClientes();
        addItemsToComboBoxCliente(comboBoxClientes, nombresClientes);
    }

    private void addItemsToComboBoxCliente(JComboBox<Cliente> comboBox, List<Cliente> items) {
        comboBox.removeAllItems();
        for (Cliente item : items) {
            comboBox.addItem(item);
        }
    }

    private void addItemsToComboBoxCajero(JComboBox<Cajero> comboBox, List<Cajero> items) {
        comboBox.removeAllItems();
        for (Cajero item : items) {
            comboBox.addItem(item);
        }
    }

    public void resetView() {
        // Reset all fields and components
        comboBoxClientes.setSelectedIndex(-1);
        comboBoxCajero.setSelectedIndex(-1);
        searchProductoText.setText("");
        LbalCantidadArticulos.setText("0");
        jLabelSubTotalPrecio.setText("0.00");
        jPrecioDescuento.setText("0.00");
        jLabelPrecioFinal.setText("0.00");
        controller.removeAllLineas();
        listaFactura.setModel(new DefaultTableModel());
    }

    public JComboBox<Cajero> getComboBoxCajero() {
        return comboBoxCajero;
    }

    public JComboBox<Cliente> getComboBoxClientes() {
        return comboBoxClientes;
    }


    public View() {

        cobrarButton.addActionListener(new ActionListener() {
            private JFrame window = null;


            @Override
            public void actionPerformed(ActionEvent e) {
                if (validateComboBox()) {
                    if (listaFactura.getRowCount() != 0) {
                        if (window == null || !window.isVisible()) {
                            window = new JFrame();
                            window.setContentPane(viewFacturarCobrar.getPanel());
                            window.setSize(500, 300);
                            window.setResizable(false);
                            window.setIconImage((new ImageIcon(Application.class.getResource("/pos/presentation/icons/shopping.png"))).getImage());
                            window.setTitle("Facturar - Cobrar");
                            window.setVisible(true);
                            viewFacturarCobrar.getTextFieldImportePagar().setText(String.format("%.2f", controller.getFinalTotal()));
                            window.addWindowListener(new WindowAdapter() {
                                @Override
                                public void windowClosing(WindowEvent windowEvent) {
                                    window = null;
                                    resetView();
                                }
                            });
                        } else {
                            window.toFront();
                            window.requestFocus();
                        }
                    }
                }
            }
        });


        buscarButton.addActionListener(new ActionListener() {
            private JFrame window = null;

            @Override
            public void actionPerformed(ActionEvent e) {
                if(comboBoxClientes.getSelectedIndex()==-1){
                    JOptionPane.showMessageDialog(panel, "Seleccione un cliente", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (window == null || !window.isVisible()) {
                    try {
                        controller.search(new Producto());
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                    window = new JFrame();
                    window.setContentPane(viewListaProductos.getPanel());
                    window.setSize(500, 300);//Tamanno
                    window.setResizable(false);
                    window.setIconImage((new ImageIcon(Application.class.getResource("/pos/presentation/icons/plus.png"))).getImage());
                    window.setTitle("Facturar - Cantidad");
                    window.setVisible(true);
                    window.addWindowListener(new WindowAdapter() {
                        @Override
                        public void windowClosing(WindowEvent windowEvent) {
                            window = null;
                        }
                    });
                } else {
                    window.toFront();
                    window.requestFocus();
                }

            }
        });
        cantidadButton.addActionListener(new ActionListener() {
            private JFrame window = null;

            @Override
            public void actionPerformed(ActionEvent e) {
               int selectedRow = listaFactura.getSelectedRow();
               if(selectedRow>=0){

                String nombreProducto =model.getNombreProducto(selectedRow);
                   if (window == null || !window.isVisible()) {
                       ImageIcon icon = new ImageIcon("/pos/presentation/icons/client.png"); // Cambia por la ruta de tu imagen,
                       String cantidadStr = JOptionPane.showInputDialog(panel, "Cantidad?:", nombreProducto, JOptionPane.PLAIN_MESSAGE);


                       if (cantidadStr != null && !cantidadStr.isEmpty()) {
                           try {
                               int nuevaCantidad = Integer.parseInt(cantidadStr);
                               Producto filter = new Producto();
                               filter.setCodigo(listaFactura.getValueAt(selectedRow, 0).toString());

                               if (nuevaCantidad > 0 && nuevaCantidad <= controller.leerProducto(filter).getExistencia()) {
                                   controller.updateLineas(selectedRow,nuevaCantidad);
                                   updateTotals();
                               } else {

                                   if(nuevaCantidad < 0){
                                       JOptionPane.showMessageDialog(panel, "La cantidad debe ser mayor a 0", "Error", JOptionPane.ERROR_MESSAGE);
                                   }
                                   else {
                                       JOptionPane.showMessageDialog(panel, "La cantidad debe ser menor o igual a la existencia", "Error", JOptionPane.ERROR_MESSAGE);
                                   }
                               }
                           } catch (NumberFormatException ex) {
                               JOptionPane.showMessageDialog(panel, "Por favor ingrese un numero valido", "Error", JOptionPane.ERROR_MESSAGE);
                           } catch (Exception ex) {
                               throw new RuntimeException(ex);
                           }
                       }
                   } else {
                       JOptionPane.showMessageDialog(panel, "Seleccione un producto para cambiar la cantidad", "Error", JOptionPane.ERROR_MESSAGE);
                   }
               }else{
                   JOptionPane.showMessageDialog(panel, "Seleccione un producto para cambiar la cantidad", "Error", JOptionPane.ERROR_MESSAGE);
               }

            }
        });


        quitarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = listaFactura.getSelectedRow();
                if (selectedRow != -1) {
                    controller.removeLinea(selectedRow);
                    updateTotals();
                } else {
                    JOptionPane.showMessageDialog(panel, "Seleccione un producto para quitar", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        descuentoButton.addActionListener(new ActionListener() {
            private JFrame window = null;

            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = listaFactura.getSelectedRow();
                if(selectedRow>=0){
                String nombreProducto =model.getNombreProducto(selectedRow);
                    if (window == null || !window.isVisible()) {
                        String descuentoStr = JOptionPane.showInputDialog(panel, "Descuento?:", nombreProducto, JOptionPane.PLAIN_MESSAGE);

                        if (descuentoStr != null && !descuentoStr.isEmpty()) {
                            try {
                                int nuevoDescuento = Integer.parseInt(descuentoStr);
                                if (nuevoDescuento > 0) {
                                    controller.updateLineasDescuento(selectedRow,nuevoDescuento);
                                    updateTotals();
                                } else {
                                    JOptionPane.showMessageDialog(panel, "La cantidad debe ser mayor a 0", "Error", JOptionPane.ERROR_MESSAGE);
                                }
                            } catch (NumberFormatException ex) {
                                JOptionPane.showMessageDialog(panel, "Por favor ingrese un numero valido", "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    } else {
                        JOptionPane.showMessageDialog(panel, "Seleccione un producto para cambiar la cantidad", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }else{
                    JOptionPane.showMessageDialog(panel, "Seleccione un producto para cambiar la cantidad", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        agregarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(comboBoxClientes.getSelectedIndex()==-1){
                    JOptionPane.showMessageDialog(panel, "Seleccione un cliente", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                Linea current= new Linea();
                Producto currentProd = new Producto();
                currentProd.setCodigo(searchProductoText.getText());
                Cliente cliente = (Cliente) comboBoxClientes.getSelectedItem();

                try {
                    if(validate()){
                    current.setProducto(controller.leerProducto(currentProd));
                    current.setDescuento(cliente.getDescuento());
                    controller.createLinea(current);
                    updateTotals();
                    JOptionPane.showMessageDialog(panel, "REGISTRO APLICADO", "", JOptionPane.INFORMATION_MESSAGE);
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(panel, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        cancelarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.removeAllLineas();
                updateTotals();
            }
        });

        comboBoxClientes.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                updateComboBoxClientes();
            }
        });
        comboBoxCajero.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
               updateComboBoxCajeros();
            }
        });
        cobrarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

        comboBoxCajero.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Cajero currentCajero= (Cajero)comboBoxCajero.getSelectedItem();
                model.getCurrent().setCajero(currentCajero);
            }
        });
        comboBoxClientes.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    Cliente currentCliente = (Cliente) e.getItem();
                    model.getCurrent().setCliente(currentCliente);

                }
            }
        });

        comboBoxClientes.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                comboBoxClientes.setSelectedItem(null);
            }
        });
    }
  boolean validate(){
        boolean valid = true;
        if(searchProductoText.getText().isEmpty()) {
          valid = false;
          agregarButton.setBorder(Application.BORDER_ERROR);
      }
        return valid;
  }

    public boolean validateComboBox(){  boolean isValid = true;

        // Obtén el elemento seleccionado
        Object selectedItem = this.comboBoxCajero.getSelectedItem();
        Object selectedItem2 = this.comboBoxClientes.getSelectedItem();
        // Verifica que no sea nulo ni vacío
        if (selectedItem == null || selectedItem.toString().trim().isEmpty()) {
            isValid = false;
            // Puedes mostrar un mensaje de error si quieres, por ejemplo:
            JOptionPane.showMessageDialog(null, "Por favor seleccione un cajero válido", "Error", JOptionPane.ERROR_MESSAGE);
        }
        if (selectedItem2 == null || selectedItem2.toString().trim().isEmpty()) {
            isValid = false;
            JOptionPane.showMessageDialog(null,"Por favor seleccione un cliente valido","Error", JOptionPane.ERROR_MESSAGE);
        }

        return isValid;
    }
    public void setFacturaNueva(Factura factura){
        comboBoxClientes.setSelectedItem(factura.getCliente());
        comboBoxCajero.setSelectedItem(factura.getCajero());
        model.setListL(factura.getLinea());
    }
    public void reiniciarComboxes(){
        comboBoxClientes.setSelectedIndex(-1);
        comboBoxCajero.setSelectedIndex(-1);
        updateTotals();
    }
    public void updateTotals() {
        this.searchProductoText.setText("");
        LbalCantidadArticulos.setText(String.valueOf(model.getTotalQuantity()));
        jLabelSubTotalPrecio.setText(String.format(Locale.US,"%.2f", model.getSubtotal()));
        jPrecioDescuento.setText(String.format(Locale.US,"%.2f", model.getTotalDiscount()));
        jLabelPrecioFinal.setText(String.format(Locale.US,"%.2f", model.getFinalTotal()));
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        switch (evt.getPropertyName()) {
            case Model.LISTALINEAS:
                int[] cols = {TableModel.ID,  TableModel.NOMBRE, TableModel.CATEGORIA,
                        TableModel.CANTIDAD, TableModel.PRECIO, TableModel.DESCUENTO, TableModel.NETO
                        , TableModel.IMPORTE};
                listaFactura.setModel(new TableModel(cols, model.getCurrent().getLinea()));
                listaFactura.setRowHeight(30);
                TableColumnModel columnModel = listaFactura.getColumnModel();
                columnModel.getColumn(0).setPreferredWidth(150);
                columnModel.getColumn(7).setPreferredWidth(150);

                updateTotals();
                break;


        }

        this.panel.revalidate();
    }

    public void setClienteEspecifico(Cliente cliente) {
        updateComboBoxClientes();
        comboBoxClientes.setSelectedItem(cliente);
    }

    public void setCajeroEspecifico(Cajero cajero) {
        updateComboBoxCajeros();
        comboBoxCajero.setSelectedItem(cajero);
    }
}
