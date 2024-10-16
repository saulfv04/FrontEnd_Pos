package pos.presentation.facturar;

import pos.logic.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Controller {
    private View view;
    private Model model;


    public Controller(View view, Model model) {
        model.init(new ArrayList<Linea>(),  Service.instance().search(new Producto()),  Service.instance().search(new Cajero()), Service.instance().search(new Cliente()));
        this.view = view;
        this.model = model;
        view.setController(this);
        view.setModel(model);
    }

    public void search(Producto filter) throws Exception {
        model.setListaProducto( Service.instance().search(filter));
    }

    public void searchLinea(Linea filter) throws Exception {
        model.setListL(model.getCurrent().getLinea());
    }

    public void searchFactura(Factura filter) throws Exception {
        model.setList(Service.instance().search(filter));
    }

    public void save(Factura e) throws Exception {
        Service.instance().create(e);
        searchFactura(new Factura());
    }


    public void removeLinea(int index) {
        if (index >= 0 && index <model.getCurrent().getLinea().size()) {
            model.getCurrent().getLinea().remove(index);
            model.setListL(model.getCurrent().getLinea());
        }
    }

    public void removeAllLineas() {
        model.getCurrent().getLinea().clear();
        model.setListL(model.getCurrent().getLinea());
    }


    public Producto getCurrentProducto() {
        return model.getCurrentP();
    }

    //Dudas en este método
    public void setExistencias(List<Linea>lineas) throws Exception {
        List<Producto> productos = model.getListaProductos(); // Obtener la lista de productos desde el servicio

        for (Linea linea : lineas) {
            for (Producto producto : productos) {
                if (Objects.equals(linea.getProducto().getCodigo(), producto.getCodigo())) { // Comparar por el ID o algún identificador único
                    // Actualizar las existencias localmente
                    producto.setExistencia(producto.getExistencia() - linea.getCantidad());
                    // Llamar a ProductoDAO para actualizar las existencias en la base de datos
                    Service.instance().updateExistencias(producto);

                    break; // Salir del bucle una vez actualizado el producto
                }
            }
        }
    }

    public void updateLineas(int i, int cant){
        model.updateLineaCant(i,cant);
        model.getCurrentP().setExistencia(cant);
    }
    public void updateLineasDescuento(int i , int desc){
        model.updateLineaDesc(i,desc);
    }
    public double getFinalTotal() {
        return model.getFinalTotal();
    }

    public List<Linea> getListaLineas() {
        return model.getCurrent().getLinea();
    }


    public View getView() {
        return view;
    }
    public Model getModel(){
        return model;
    }

    public List<Cliente> getClientes(){
        return model.getCliente();
    }
    public List<Cajero> getCajero(){
        return model.getCajero();
    }


    public Producto leerProducto(Producto prod) throws Exception {
        return Service.instance().read(prod);
    }
    public Cajero leerCajero(Cajero cajero) throws Exception {
        return  Service.instance().read(cajero);
    }

    public void createLinea(Linea linea) throws Exception {
        if (!existeProductoEnLinea(linea.getProducto().getCodigo())) {
            model.getCurrent().getLinea().add(linea);
            searchLinea(model.getFilterLinea());
        } else
            throw new Exception("Producto ya ha sido agregado");
    }

    public boolean existeProductoEnLinea(String codigoProducto) {
        return model.getCurrent().getLinea().stream()
                .anyMatch(linea -> linea.getProducto().getCodigo().equals(codigoProducto));
    }
}