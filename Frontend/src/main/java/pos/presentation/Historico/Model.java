package pos.presentation.Historico;

import pos.logic.Factura;
import pos.logic.Linea;
import pos.presentation.AbstractModel;

import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

public class Model extends AbstractModel{
    int mode;
    Factura filter;
    List<Factura> list;
    Factura current;
    Linea currentLinea;
    List<Linea> listaLineas;
    Linea filterLinea;
    public Factura getCurrentCliente() {
        return current;
    }


    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        super.addPropertyChangeListener(listener);
        firePropertyChange(LIST);
        firePropertyChange(CURRENT);
        firePropertyChange(LISTAFACTURAS);
        firePropertyChange(LISTALINEAS);
    }

    public Model() {
    }

    public void init(List<Factura> list) {
        this.list = list;
        this.current = new Factura();
        this.filter = new Factura();

    }

    public void setListaLineas(List<Linea> listaLineas) {
        this.listaLineas = listaLineas;
        firePropertyChange(LISTALINEAS);
    }



    public List<Factura> getList() {
        return list;
    }

    public void setList(List<Factura> list) {
        this.list = list;
        firePropertyChange(LISTAFACTURAS);
    }

    public Factura getCurrent() {
        return current;
    }

    public void setCurrent(Factura current) {
        this.current = current;
        firePropertyChange(CURRENT);
    }


    public void setFilterLinea(Linea filterLinea) {
        this.filterLinea = filterLinea;
        firePropertyChange(FILTERLINEA);
    }



    public Linea getCurrentL() {
        return currentLinea;
    }

    public void setCurrentL(Linea currentLinea) {
        this.currentLinea = currentLinea;
        firePropertyChange(CURRENTLINEA);
    }

    public List<Linea> getListL() {
        return listaLineas;
    }

    public void setListL(List<Linea> list) {
        this.listaLineas = list;
        firePropertyChange(LISTALINEAS);
    }

    public Factura getFilter() {
        return filter;
    }


    public void setFilter(Factura filter) {
        this.filter = filter;
        firePropertyChange(FILTER);
    }



    public Linea getFilterLinea() {
        return filterLinea;
    }




    public void addLineaToFactura(Linea linea) {
        this.current.addLinea(linea);
        firePropertyChange(LISTALINEAS);
    }

    public int getTotalQuantity() {
        return listaLineas.stream().mapToInt(Linea::getCantidad).sum();
    }


    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }


    public List<Factura> getListaFacturas() {
        return list;
    }

    public List<Linea> getListaLineas() {
        return listaLineas != null ? listaLineas : new ArrayList<>();
    }



    public static final String LIST = "list";
    public static final String CURRENT = "current";
    public static final String FILTER = "filter";
    public static final String LISTALINEAS = "listaLineas";
    public static final String LISTAFACTURAS = "listaFacturas";
    public static final String FILTERLINEA = "filterLinea";
    public static final String CURRENTLINEA = "CurrentLinea";

}

