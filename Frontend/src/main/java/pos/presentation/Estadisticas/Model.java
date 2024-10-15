package pos.presentation.Estadisticas;

import pos.logic.Categoria;
import pos.presentation.AbstractModel;

import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

public class Model extends AbstractModel{

    int mode;
    private List<Categoria> allCategorias;
    private List<Categoria> categoriasSeleccionadas;
    Rango rango;
    String[] rows;
    String[] cols;
    double [][] data;

    public Model() {
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        super.addPropertyChangeListener(listener);
        firePropertyChange(ALLCATEGORIAS);
        firePropertyChange(CATEGORIASSELECCIONADAS);
        firePropertyChange(RANGO);
        firePropertyChange(ROWS);
        firePropertyChange(COLS);
        firePropertyChange(DATA);
    }

    public void init(List<Categoria> allCategorias) {
        this.allCategorias = allCategorias;
        this.categoriasSeleccionadas = new ArrayList<Categoria>();
        this.rango = new Rango();
        this.rows = new String[0];
        this.cols = new String[0];
        this.data = new double[0][0];
        mode = 0;
    }


    public void setAllCategorias(List<Categoria> allCategorias) {
        this.allCategorias = allCategorias;
        firePropertyChange(ALLCATEGORIAS);
    }

    public void setAllcategoriasSeleccionadas(List<Categoria> allCategorias){
        this.categoriasSeleccionadas= allCategorias;
        firePropertyChange(CATEGORIASSELECCIONADAS);
    }

    public void limpiarCategorias() {
        this.categoriasSeleccionadas.clear(); // Limpia las categorías seleccionadas
        this.categoriasSeleccionadas= new ArrayList<>();
        firePropertyChange(ALLCATEGORIAS); // Notifica el cambio
    }
    public void clearSeleccionCategoria(String nombre){
        // Busca la categoría por su nombre en la lista de categorías seleccionadas
        Categoria categoriaAEliminar = null;
        for (Categoria categoria : categoriasSeleccionadas) {
            if (categoria.getNombre().equals(nombre)) {
                categoriaAEliminar = categoria; // Guarda la categoría a eliminar
                break; // Sale del bucle una vez encontrada
            }
        }
        // Si se encontró la categoría, la elimina
        if (categoriaAEliminar != null) {
            categoriasSeleccionadas.remove(categoriaAEliminar);
            firePropertyChange(CATEGORIASSELECCIONADAS); // Notifica el cambio en categorías seleccionadas
        }
    }

    public List<Categoria> getAllCategorias() {
        return allCategorias;
    }

    public void setCategoriasSeleccionadas(List<Categoria> categoriasSeleccionadas) {
        this.categoriasSeleccionadas = categoriasSeleccionadas;
        firePropertyChange(CATEGORIASSELECCIONADAS);
    }


    public List<Categoria> getCategoriasSeleccionadas() {
        return categoriasSeleccionadas;
    }


    public void setRango(Rango rango) {
        this.rango = rango;
        firePropertyChange(RANGO);
    }


    public Rango getRango() {
        return rango;
    }

    public void setRows(String[] rows) {
        this.rows = rows;
        firePropertyChange(ROWS);
    }


    public String[] getRows() {
        return rows;
    }


    public void setCols(String[] cols) {
        this.cols = cols;
        firePropertyChange(COLS);
    }



    public String[] getCols() {
        return cols;
    }


    public void setData(double[][] data) {
        this.data = data;
        firePropertyChange(DATA);
    }


    public double[][] getData() {
        return data;
    }


    public void setMode(int mode) {
        this.mode = mode;
    }

    public static final String RANGO = "rango";
    public static final String ROWS = "rows";
    public static final String DATA = "data";
    public static final String COLS = "cols";
    public static final String ALLCATEGORIAS = "allCategorias";
    public static final String CATEGORIASSELECCIONADAS = "categoriasSeleccionadas";

}
