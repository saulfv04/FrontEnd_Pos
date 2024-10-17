package pos.presentation.Estadisticas;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import pos.logic.Categoria;

import javax.swing.*;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;




public class View implements PropertyChangeListener {
    private JPanel panel;
    private JComboBox anioInicio;
    private JComboBox mesFinal;
    private JComboBox mesInicio;
    private JComboBox<Categoria> comboBox5;
    private JButton button1;
    private JButton button2;
    private JTable listaCategoria;
    private JButton button3;
    private JButton button4;
    private JComboBox anioFinal;
    private JPanel grafico;

    // MVC
    Model model;
    Controller controller;

    private String previousMesInicio;
    private String previousMesFinal;

    public void setModel(Model model) {
        this.model = model;
        model.addPropertyChangeListener(this);
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public View() {
        comboBox5.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                updateComboBoxCategorias();
            }
        });
        anioInicio.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (validarFechas()) {
                    try {
                        controller.actualizarRango(
                                Integer.parseInt(anioInicio.getSelectedItem().toString()),
                                Integer.parseInt(anioFinal.getSelectedItem().toString()),
                                Integer.parseInt(mesInicio.getSelectedItem().toString().split("-")[0]),
                                Integer.parseInt(mesFinal.getSelectedItem().toString().split("-")[0]));
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        });
        anioFinal.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (validarFechas()) {
                    try {
                        controller.actualizarRango(
                                Integer.parseInt(anioInicio.getSelectedItem().toString()),
                                Integer.parseInt(anioFinal.getSelectedItem().toString()),
                                Integer.parseInt(mesInicio.getSelectedItem().toString().split("-")[0]),
                                Integer.parseInt(mesFinal.getSelectedItem().toString().split("-")[0]));
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        });
        mesFinal.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.DESELECTED) {
                    previousMesFinal = e.getItem().toString();
                } else if (e.getStateChange() == ItemEvent.SELECTED) {
                    if (validarFechas()) {
                        try {
                            controller.actualizarRango(
                                    Integer.parseInt(anioInicio.getSelectedItem().toString()),
                                    Integer.parseInt(anioFinal.getSelectedItem().toString()),
                                    Integer.parseInt(mesInicio.getSelectedItem().toString().split("-")[0]),
                                    Integer.parseInt(mesFinal.getSelectedItem().toString().split("-")[0]));
                        } catch (Exception ex) {
                            throw new RuntimeException(ex);
                        }
                    } else {
                        mesFinal.setSelectedItem(previousMesFinal);
                    }
                }
            }
        });
        mesInicio.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.DESELECTED) {
                    previousMesInicio = e.getItem().toString();
                } else if (e.getStateChange() == ItemEvent.SELECTED) {
                    if (validarFechas()) {
                        try {
                            controller.actualizarRango(
                                    Integer.parseInt(anioInicio.getSelectedItem().toString()),
                                    Integer.parseInt(anioFinal.getSelectedItem().toString()),
                                    Integer.parseInt(mesInicio.getSelectedItem().toString().split("-")[0]),
                                    Integer.parseInt(mesFinal.getSelectedItem().toString().split("-")[0]));
                        } catch (Exception ex) {
                            throw new RuntimeException(ex);
                        }
                    } else {
                        mesInicio.setSelectedItem(previousMesInicio);
                    }
                }
            }
        });
        button2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Object seleccion= comboBox5.getSelectedItem();
                if(seleccion!=null){
                    try {
                        controller.addCategotiriaSeleccionada((Categoria) comboBox5.getSelectedItem());
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                }else{
                    JOptionPane.showMessageDialog(panel, "Se debe seleccionar la categoria a ingresar", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        button3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow= listaCategoria.getSelectedRow();
                if(selectedRow>=0){
                    try {
                        controller.clearCategoriaEspecifica(selectedRow,Integer.parseInt(anioInicio.getSelectedItem().toString()),
                                Integer.parseInt(anioFinal.getSelectedItem().toString()),
                                Integer.parseInt(mesInicio.getSelectedItem().toString().split("-")[0]),
                                Integer.parseInt(mesFinal.getSelectedItem().toString().split("-")[0]));
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                    listaCategoria.revalidate();
                    listaCategoria.repaint();

                }else{
                    JOptionPane.showMessageDialog(panel, "Se debe seleccionar una categoria a eliminar.", "Error", JOptionPane.ERROR_MESSAGE);
                }

            }
        });
        button4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    controller.clearAllCategorias(Integer.parseInt(anioInicio.getSelectedItem().toString()),
                            Integer.parseInt(anioFinal.getSelectedItem().toString()),
                            Integer.parseInt(mesInicio.getSelectedItem().toString().split("-")[0]),
                            Integer.parseInt(mesFinal.getSelectedItem().toString().split("-")[0]));
                    listaCategoria.revalidate();
                    listaCategoria.repaint();
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }

            }
        });
        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    controller.addAllCategorias();
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        button2.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                try {
                    controller.actualizarRango(
                            Integer.parseInt(anioInicio.getSelectedItem().toString()),
                            Integer.parseInt(anioFinal.getSelectedItem().toString()),
                            Integer.parseInt(mesInicio.getSelectedItem().toString().split("-")[0]),
                            Integer.parseInt(mesFinal.getSelectedItem().toString().split("-")[0]));
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }

    private boolean validarFechas() {
        int anioInicioVal = Integer.parseInt(anioInicio.getSelectedItem().toString());
        int anioFinalVal = Integer.parseInt(anioFinal.getSelectedItem().toString());
        int mesInicioVal = Integer.parseInt(mesInicio.getSelectedItem().toString().split("-")[0]);
        int mesFinalVal = Integer.parseInt(mesFinal.getSelectedItem().toString().split("-")[0]);

        if (anioInicioVal > anioFinalVal || (anioInicioVal == anioFinalVal && mesInicioVal > mesFinalVal)) {
            JOptionPane.showMessageDialog(panel, "El rango de fechas seleccionado no es válido.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    public JPanel getPanel() {
        return panel;
    }

    public void updateComboBoxCategorias() {
        List<Categoria> nombresCategorias = controller.getCategoria();
        addItemsToComboBox5(comboBox5, nombresCategorias);
    }

    private void addItemsToComboBox5(JComboBox<Categoria> comboBox, List<Categoria> items) {
        comboBox.removeAllItems();
        for (Categoria item : items) {
            comboBox5.addItem(item);
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        switch (evt.getPropertyName()) {
            case Model.ALLCATEGORIAS:
                for (Categoria categoria : model.getAllCategorias()) {
                    comboBox5.addItem(categoria);
                }
                break;
            case Model.DATA:

                listaCategoria.setModel(new TableModel(model.getRows(), model.getCols(), model.getData()));
                listaCategoria.setRowHeight(30);
                TableColumnModel columnModel = listaCategoria.getColumnModel();
                if(model.getCols().length > 2) {
                    listaCategoria.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
                    for (int i = 0; i < columnModel.getColumnCount(); i++) {
                        if (i == 0) {
                            columnModel.getColumn(i).setPreferredWidth(150);
                        } else {
                            columnModel.getColumn(i).setPreferredWidth(100);
                        }
                    }
                    columnModel.getColumn(0).setPreferredWidth(150);
                }else{
                    listaCategoria.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
                }
                DefaultCategoryDataset dataset = createDataset();

                JFreeChart lineChart = ChartFactory.createLineChart(
                        "Ventas por mes",
                        "Categorías",
                        "Valores",
                        dataset,
                        PlotOrientation.VERTICAL,
                        true,
                        true,
                        false
                );

                ChartPanel chartPanel = new ChartPanel(lineChart);
                chartPanel.setPreferredSize(new Dimension(900, 600));
                grafico.removeAll();
                grafico.add(chartPanel);
                break;



        }
        this.panel.revalidate();
    }


    private DefaultCategoryDataset createDataset() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        for (int i = 0; i < model.getRows().length; i++) {
            for (int j = 0; j < model.getCols().length; j++) {
                dataset.addValue(model.getData()[i][j],model.getRows()[i],model.getCols()[j]);
            }
        }

        return dataset;
    }

    private void limpiarTabla() {
//        listaCategoria.setModel(new TableModel(new String[0], new String[0], new double[0][0]));
        listaCategoria.revalidate();
        listaCategoria.repaint();

//        anioInicio.setSelectedIndex(0);
//        anioFinal.setSelectedIndex(0);
//        mesInicio.setSelectedIndex(0);
//        mesFinal.setSelectedIndex(0);
    }


}