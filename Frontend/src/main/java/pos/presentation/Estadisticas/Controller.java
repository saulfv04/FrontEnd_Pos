package pos.presentation.Estadisticas;

import pos.logic.Categoria;
import pos.logic.Service;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Controller {
    private View view;
    private Model model;


    public Controller(View view, Model model) {
        List<Categoria> allCategorias = Service.instance().getCategorias();
        model.init(allCategorias);
        this.view = view;
        this.model = model;
        view.setController(this);
        view.setModel(model);
    }

    public List<Categoria> getCategoria() {
        return Service.instance().getCategorias();
    }




    public void actualizarData() throws Exception {
        Rango r = model.getRango();
        List<Categoria> categoriasSeleccionadas = model.getCategoriasSeleccionadas();
        int colCount = (r.getAnioFinal() - r.getAnioInicio()) * 12 + r.getMesFinal() - r.getMesInicio() + 1;
        int rowCount = categoriasSeleccionadas.size();

        String[] cols = new String[colCount];
        String[] rows = new String[rowCount];
        int year = r.getAnioInicio();
        int month = r.getMesInicio();

        for (int i = 0; i < colCount; i++) {
            cols[i] = year + "-" + (month < 10 ? "0" + month : month);  // Ejemplo formato "2023-05"
            month++;
            if (month > 12) {
                month = 1;
                year++;
            }
        }

        if (!categoriasSeleccionadas.isEmpty()) {
            List<String> colList = Arrays.asList(cols);
            float[][] estadisticasData = Service.instance.getEstadisticas(categoriasSeleccionadas, colList, r);

            double[][] data = new double[rowCount][colCount];
            for (int i = 0; i < rowCount; i++) {
                for (int j = 0; j < colCount; j++) {
                    data[i][j] = estadisticasData[i][j];
                }
            }

            for (int i = 0; i < rowCount; i++) {
                rows[i] = categoriasSeleccionadas.get(i).getNombre();
            }

            model.setCols(cols);
            model.setRows(rows);
            model.setData(data);
        } else {
            model.setData(new double[0][0]);
        }
    }

    public void addAllCategorias() throws Exception {
        List<Categoria> allCategorias = Service.instance.getCategorias();
        for (Categoria c : allCategorias) {
            if (!model.getCategoriasSeleccionadas().contains(c)) {
                model.getCategoriasSeleccionadas().add(c);
            }
        }
        actualizarData();
    }


    public void actualizarRango(int anioInicio, int anioFinal, int mesInicio, int mesFinal) throws Exception {
        model.setRango(new Rango(anioInicio,anioFinal,mesInicio,mesFinal));
        actualizarData();
    }


    public void addCategotiriaSeleccionada(Categoria c) throws Exception {
        if(!model.getCategoriasSeleccionadas().contains(c)) {
            model.getCategoriasSeleccionadas().add(c);
            actualizarData();
        }else{
            JOptionPane.showMessageDialog(view.getPanel(),"La categoria ya se encuentra seleccionada", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void clearAllCategorias(int anioInicio, int anioFinal, int mesInicio, int mesFinal) throws Exception {
        model.setCategoriasSeleccionadas(new ArrayList<Categoria>());
        if (model.getCategoriasSeleccionadas().isEmpty()) {
            model.init(Service.instance.getCategorias());
            //            Service.instance().getCategorias()
        }
        actualizarRango(anioInicio, anioFinal, mesInicio, mesFinal);
        actualizarData();
    }
    public void clearCategoriaEspecifica(int selectedRow,int anioInicio, int anioFinal, int mesInicio, int mesFinal) throws Exception {
        model.getCategoriasSeleccionadas().remove(selectedRow);
        if(model.getCategoriasSeleccionadas().isEmpty()){
            model.init(Service.instance.getCategorias());
//            Service.instance().getCategorias()
        }
        actualizarRango(anioInicio, anioFinal, mesInicio, mesFinal);
        actualizarData();
    }
}
