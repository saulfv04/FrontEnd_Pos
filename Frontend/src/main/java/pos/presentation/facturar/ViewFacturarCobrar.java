package pos.presentation.facturar;

import pos.logic.Factura;
import pos.logic.Fecha;
import pos.logic.Linea;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Random;
public class ViewFacturarCobrar  {
    private JButton okButton;



    private JTextField textFieldImportePagar;
    private JTextField textFieldEfectivo;
    private JTextField JTextTarjeta;
    private JTextField JTextCheque;
    private JTextField JTextSinpe;
    private JPanel jPanelPrincipal;
    private JButton JButtonCancel;
    private JPanel panel;

    Controller controller;
    Model model;

    public ViewFacturarCobrar() {

        JButtonCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Window window = SwingUtilities.getWindowAncestor(jPanelPrincipal);
                if (window != null) {
                    window.dispose();
                }
                resetFields();
            }
        });
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (valide()) {
                    try {
                        double importe = (Double.parseDouble(textFieldImportePagar.getText()));
                        double efectivo = Double.parseDouble(textFieldEfectivo.getText());
                        double tarjeta = Double.parseDouble(JTextTarjeta.getText());
                        double cheque = Double.parseDouble(JTextCheque.getText());
                        double sinpe = Double.parseDouble(JTextSinpe.getText());

                        double totalPago = efectivo + tarjeta + cheque + sinpe;

                        if (totalPago >= importe) {

                            Factura nuevaFactura = takeFactura();

                            try {
                                controller.save(nuevaFactura);
                                controller.setExistencias(nuevaFactura.getLinea());

                                JOptionPane.showMessageDialog(panel, "Factura creada exitosamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                                Window window = SwingUtilities.getWindowAncestor(jPanelPrincipal);
                                if (window != null) {
                                    window.dispose();
                                }
                                resetFields();
                                controller.getView().resetView();

                            } catch (Exception ex) {
                                JOptionPane.showMessageDialog(panel, "Error al guardar la factura: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        } else {
                            JOptionPane.showMessageDialog(panel, "El total del pago debe ser igual o mayor al importe", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(panel, "Por favor ingrese valores válidos", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
    }

    public JPanel getPanel() {
        return jPanelPrincipal;
    }

    private void resetFields() {
        textFieldEfectivo.setText("0");
        textFieldImportePagar.setText("");
        JTextCheque.setText("0");
        JTextTarjeta.setText("0");
        JTextSinpe.setText("0");
    }

    public JTextField getTextFieldImportePagar() {
        return textFieldImportePagar;
    }

    public boolean valide() {
        boolean valide = true;
        if (textFieldImportePagar.getText().isEmpty() || textFieldEfectivo.getText().isEmpty() || JTextTarjeta.getText().isEmpty() || JTextCheque.getText().isEmpty() || JTextSinpe.getText().isEmpty()) {
            valide = false;
        }
        return valide;
    }


    public void setModel(Model model) {
        this.model = model;
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    private String generateFacturaId() {
        Random random = new Random();
        int randomId = random.nextInt(9000) + 1000;
        return String.valueOf(randomId);
    }

    public Factura takeFactura() {
        try {
            Factura factura = new Factura();
            factura.setCliente(controller.getModel().getCurrent().getCliente());
            StringBuilder metodoPago = getStringBuilder();
            factura.setMetodoPago(metodoPago.toString());
            factura.setTotal(Double.parseDouble(textFieldImportePagar.getText()));
            factura.setCajero(controller.leerCajero(controller.getModel().getCurrent().getCajero()));
            factura.setFecha(new Fecha());
            List<Linea> lineas = controller.getListaLineas();
            if (lineas != null) {
                for (Linea linea : lineas) {
                    factura.addLinea(linea);
                }
            } else {
                JOptionPane.showMessageDialog(panel, "No hay líneas para agregar a la factura.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            }

            return factura;
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(panel, "Error: Formato de número inválido.", "Error", JOptionPane.ERROR_MESSAGE);
            throw new RuntimeException("Formato de número inválido: " + e.getMessage(), e);
        } catch (NullPointerException e) {
            JOptionPane.showMessageDialog(panel, "Error: Valor nulo encontrado.", "Error", JOptionPane.ERROR_MESSAGE);
            throw new RuntimeException("Valor nulo encontrado: " + e.getMessage(), e);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(panel, "Error al crear la factura: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            throw new RuntimeException("Error al crear la factura: " + e.getMessage(), e);
        }
    }

    private StringBuilder getStringBuilder() {
        StringBuilder metodoPago = new StringBuilder();
        if (!textFieldEfectivo.getText().equals("0")) {
            metodoPago.append("Efectivo/");
        }
        if (!JTextTarjeta.getText().equals("0")) {
            metodoPago.append("Tarjeta/");
        }
        if (!JTextCheque.getText().equals("0")) {
            metodoPago.append("Cheque/");
        }
        if (!JTextSinpe.getText().equals("0")) {
            metodoPago.append("Sinpe/");
        }
        if (!metodoPago.isEmpty()) {
            metodoPago.setLength(metodoPago.length() - 1);
        }
        return metodoPago;
    }
}
