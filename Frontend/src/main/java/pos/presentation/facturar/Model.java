package pos.presentation.facturar;

import pos.logic.*;
import pos.presentation.AbstractModel;

import java.beans.PropertyChangeListener;
import java.util.List;

public class Model extends AbstractModel {
    int mode;
    List<Factura> list;
    Factura current;
    Producto currentProducto;
    List<Producto> listaProductos;
    List<Cliente> listaClientes;
    List <Cajero>listaCajeros;
    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        super.addPropertyChangeListener(listener);
        firePropertyChange(LIST);
        firePropertyChange(CURRENT);
        firePropertyChange(FILTERPRODUCTO);
        firePropertyChange(LISTALINEAS);
        firePropertyChange(LISTAPRODUCTOS);
        firePropertyChange(CANTIDADVIEW);
        firePropertyChange(LISTAPRODUCTOS);
        firePropertyChange(LISTACAJEROS);
        firePropertyChange(CURRENTLINEA);
        firePropertyChange(CURRENTCAJERO);
    }

    public Model() {
    }

    public void init(List<Linea> listL, List<Producto> listP, List<Cajero>listC,List<Cliente> listCl) {
        this.current = new Factura();
        this.listaProductos = listP;
        this.currentProducto=new Producto();
        this.current.setLinea(listL);
        this.listaCajeros= listC;
        this.listaClientes = listCl;
    }
    public List<Factura> getList() {
        return list;
    }

    public void setList(List<Factura> list) {
        this.list = list;
        firePropertyChange(LIST);
    }

    public Factura getCurrent() {
        return current;
    }


    public Producto getCurrentP() {
        return currentProducto;
    }

    public void setListL(List<Linea> list) {
        this.current.setLinea(list);
        firePropertyChange(LISTALINEAS);
    }

    public Linea getFilterLinea() {
        return current.getCurrent();
    }

    public void setListaProducto(List<Producto> list) {
        this.listaProductos = list;
        firePropertyChange(LISTAPRODUCTOS);
    }
    public List<Producto> getListaProductos(){
        return listaProductos;
    }

    public List<Cajero> getCajero(){
        return listaCajeros;
    }

    public List<Producto> getListP() {
        return listaProductos;
    }


    public int getTotalQuantity() {
        return getCurrent().getLinea().stream().mapToInt(Linea::getCantidad).sum();
    }

    public double getSubtotal() {
        return getCurrent().getLinea().stream().mapToDouble(linea -> linea.getProducto().getPrecioUnitario() * linea.getCantidad()).sum();
    }

    public double getTotalDiscount() {
        return getCurrent().getLinea().stream().mapToDouble(linea -> linea.getProducto().getPrecioUnitario() * linea.getCantidad() * linea.getDescuento() / 100).sum();
    }

    public double getFinalTotal() {
        return getSubtotal() - getTotalDiscount();
    }

    public String getNombreProducto(int index){
        return getCurrent().getLinea().get(index).getProducto().getDescripcion();
    }
    public List<Cliente> getCliente(){
        return listaClientes;
    }
    public void setCliente(List<Cliente> list) {this.listaClientes = list;}


    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public void removeLinea(int index) {
        if (index >= 0 && index < getCurrent().getLinea().size()) {
            getCurrent().getLinea().remove(index);
            firePropertyChange(LISTALINEAS);
        }
    }


    public void updateLineaCant(int index, int nuevaCantidad) {
        if (index >= 0 && index < getCurrent().getLinea().size()) {
            Linea linea = getCurrent().getLinea().get(index);
            linea.setCantidad(nuevaCantidad);
            firePropertyChange(LISTALINEAS);
        }
    }

    public void updateLineaDesc(int index, int nuevoDesc) {
        if (index >= 0 && index < getCurrent().getLinea().size()) {
            Linea linea = getCurrent().getLinea().get(index);
            linea.setDescuento(nuevoDesc);
            firePropertyChange(LISTALINEAS);
        }
    }
    public static final String LIST = "list";
    public static final String CURRENT = "current";
    public static final String FILTERPRODUCTO = "filterProducto";
    public static final String LISTALINEAS = "listaLineas";
    public static final String LISTAPRODUCTOS = "listaProductos";
    public static final String LISTACAJEROS = "listaCajeros";
    public static final String CURRENTLINEA = "currentLinea";
    public static final String FILTERLINEA = "filterLinea";
    public static final String CANTIDADVIEW = "cantidadView";
    public static final String CURRENTCAJERO = "currentCajero";

}