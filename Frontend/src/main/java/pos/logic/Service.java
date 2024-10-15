package pos.logic;

import pos.presentation.Estadisticas.Rango;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;


public class Service implements IService{
    private static Service theInstace;
    public static Service instance;
    Socket s;
    ObjectOutputStream os;
    ObjectInputStream is;
    public Service(){
        try{
            s= new Socket(Protocol.SERVER,Protocol.PORT);
            os = new ObjectOutputStream(s.getOutputStream());
            is = new ObjectInputStream(s.getInputStream());
        }catch (Exception e){
            System.exit(-1);
        }
    }

    //===================== PRODUCTO =====================

    @Override
    public void create(Producto e)throws Exception{
        os.writeInt(Protocol.PRODUCTO_CREATE);
        os.writeObject(e);
        os.flush();
        if(is.readInt()==Protocol.ERROR_NO_ERROR){}
        else throw new Exception("PRODUCTO DUPLICADO");
    }
    @Override
    public Producto read(Producto e) throws Exception {
        os.writeInt(Protocol.PRODUCTO_READ);
        os.writeObject(e);
        os.flush();

        int response = is.readInt();
        if (response == Protocol.ERROR_NO_ERROR) {
            return (Producto) is.readObject(); // Leer y devolver el objeto Producto
        } else {
            throw new Exception("ERROR AL LEER EL PRODUCTO");
        }
    }
    @Override
    public void update(Producto e) throws Exception {
        os.writeInt(Protocol.PRODUCTO_UPDATE);
        os.writeObject(e);
        os.flush();

        if (is.readInt() != Protocol.ERROR_NO_ERROR) {
            throw new Exception("ERROR AL ACTUALIZAR EL PRODUCTO");
        }
    }
    @Override
    public void delete(Producto e) throws Exception {
        os.writeInt(Protocol.PRODUCTO_DELETE);
        os.writeObject(e);
        os.flush();

        if (is.readInt() != Protocol.ERROR_NO_ERROR) {
            throw new Exception("ERROR AL ELIMINAR EL PRODUCTO");
        }
    }

    @Override
    public List<Producto> search(Producto e){
        try {
            os.writeInt(Protocol.PRODUCTO_SEARCH);
            os.writeObject(e); // Enviar el objeto para buscar
            os.flush();

            int response = is.readInt();
            if (response == Protocol.ERROR_NO_ERROR) {
                return (List<Producto>) is.readObject(); // Leer y devolver la lista de productos
            } else {
                throw new Exception("ERROR AL BUSCAR PRODUCTO");
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void updateExistencias(Producto e){
        try {
            os.writeInt(Protocol.PRODUCTO_UPDATE_EXISTENCIAS); // Protocolo para actualización de existencias
            os.writeObject(e); // Enviar el objeto producto con las nuevas existencias
            os.flush();

            int response = is.readInt(); // Leer respuesta del servidor
            if (response != Protocol.ERROR_NO_ERROR) {
                throw new Exception("ERROR AL ACTUALIZAR EXISTENCIAS DEL PRODUCTO");
            }

        } catch (Exception ex) {
            throw new RuntimeException("Error al actualizar las existencias del producto", ex);
        }
    }

    //===================== CATEGORIA =====================

    @Override
    public List<Categoria> search(Categoria e) {
        try {
            os.writeInt(Protocol.CATEGORIA_SEARCH);
            os.writeObject(e);
            os.flush();

            int response = is.readInt();
            if (response == Protocol.ERROR_NO_ERROR) {
                // Leer la lista de categorías
                return (List<Categoria>) is.readObject();
            } else {
                throw new Exception("ERROR AL BUSCAR CATEGORIA");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ArrayList<>(); // Retorna una lista vacía en caso de error
        }
    }

    @Override
    public List<Categoria> getCategorias(){
        try {
            os.writeInt(Protocol.CATEGORIA_GETCATEGORIAS);
            os.writeObject(e);
            os.flush();

            int response = is.readInt();
            if (response == Protocol.ERROR_NO_ERROR) {
                // Leer la lista de categorías
                return (List<Categoria>) is.readObject();
            } else {
                throw new Exception("ERROR AL BUSCAR CATEGORIA");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ArrayList<>(); // Retorna una lista vacía en caso de error
        }
    }

    //===================== CLIENTE =====================

    @Override
    public void create(Cliente e) throws Exception {
        os.writeInt(Protocol.CLIENTE_CREATE);
        os.writeObject(e);
        os.flush();
        if (is.readInt() == Protocol.ERROR_NO_ERROR) {
            // Éxito
        } else {
            throw new Exception("CLIENTE DUPLICADO");
        }
    }

    @Override
    public Cliente read(Cliente e) throws Exception {
        os.writeInt(Protocol.CLIENTE_READ);
        os.writeObject(e);
        os.flush();

        int response = is.readInt();
        if (response == Protocol.ERROR_NO_ERROR) {
            return (Cliente) is.readObject(); // Leer y devolver el objeto Cliente
        } else {
            throw new Exception("ERROR AL LEER EL CLIENTE");
        }
    }

    @Override
    public void update(Cliente e) throws Exception {
        os.writeInt(Protocol.CLIENTE_UPDATE);
        os.writeObject(e);
        os.flush();

        if (is.readInt() != Protocol.ERROR_NO_ERROR) {
            throw new Exception("ERROR AL ACTUALIZAR EL CLIENTE");
        }
    }

    @Override
    public void delete(Cliente e) throws Exception {
        os.writeInt(Protocol.CLIENTE_DELETE);
        os.writeObject(e);
        os.flush();

        if (is.readInt() != Protocol.ERROR_NO_ERROR) {
            throw new Exception("ERROR AL ELIMINAR EL CLIENTE");
        }
    }

    @Override
    public List<Cliente> search(Cliente e){
        try{
        os.writeInt(Protocol.CLIENTE_SEARCH);
        os.writeObject(e); // Enviar el objeto para buscar
        os.flush();

        int response = is.readInt();
        if (response == Protocol.ERROR_NO_ERROR) {
            return (List<Cliente>) is.readObject(); // Leer y devolver la lista de clientes
        } else {
            throw new Exception("ERROR AL BUSCAR CLIENTE");
        }
    } catch (Exception ex) {
        ex.printStackTrace();
        return new ArrayList<>(); // Retorna una lista vacía en caso de error
    }
    }

    //===================== CAJERO =====================

    @Override
    public void create(Cajero e) throws Exception {
        os.writeInt(Protocol.CAJERO_CREATE);
        os.writeObject(e);
        os.flush();
        if (is.readInt() == Protocol.ERROR_NO_ERROR) {
            // Éxito
        } else {
            throw new Exception("CAJERO DUPLICADO");
        }
    }

    @Override
    public Cajero read(Cajero e) throws Exception {
        os.writeInt(Protocol.CAJERO_READ);
        os.writeObject(e);
        os.flush();

        int response = is.readInt();
        if (response == Protocol.ERROR_NO_ERROR) {
            return (Cajero) is.readObject(); // Leer y devolver el objeto Cajero
        } else {
            throw new Exception("ERROR AL LEER EL CAJERO");
        }
    }

    @Override
    public void update(Cajero e) throws Exception {
        os.writeInt(Protocol.CAJERO_UPDATE);
        os.writeObject(e);
        os.flush();

        if (is.readInt() != Protocol.ERROR_NO_ERROR) {
            throw new Exception("ERROR AL ACTUALIZAR EL CAJERO");
        }
    }

    @Override
    public void delete(Cajero e) throws Exception {
        os.writeInt(Protocol.CAJERO_DELETE);
        os.writeObject(e);
        os.flush();

        if (is.readInt() != Protocol.ERROR_NO_ERROR) {
            throw new Exception("ERROR AL ELIMINAR EL CAJERO");
        }
    }


    @Override
    public List<Cajero> search(Cajero e) {
        try {
        os.writeInt(Protocol.CAJERO_SEARCH);
        os.writeObject(e); // Enviar el objeto para buscar
        os.flush();

        int response = is.readInt();
        if (response == Protocol.ERROR_NO_ERROR) {
            return (List<Cajero>) is.readObject(); // Leer y devolver la lista de cajeros
        } else {
            throw new Exception("ERROR AL BUSCAR CAJERO");
        }
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ArrayList<>(); // Retorna una lista vacía en caso de error
        }
    }

    //===================== FACTURA =====================

    @Override
    public void create(Factura e) throws Exception {
        os.writeInt(Protocol.FACTURA_CREATE);
        os.writeObject(e);
        os.flush();
        if (is.readInt() == Protocol.ERROR_NO_ERROR) {
            // Éxito
        } else {
            throw new Exception("FACTURA DUPLICADA");
        }
    }

    @Override
    public Factura read(Factura e) throws Exception {
        os.writeInt(Protocol.FACTURA_READ);
        os.writeObject(e);
        os.flush();

        int response = is.readInt();
        if (response == Protocol.ERROR_NO_ERROR) {
            return (Factura) is.readObject(); // Leer y devolver el objeto Factura
        } else {
            throw new Exception("ERROR AL LEER LA FACTURA");
        }
    }

    @Override
    public void update(Factura e) throws Exception {
        os.writeInt(Protocol.FACTURA_UPDATE);
        os.writeObject(e);
        os.flush();

        if (is.readInt() != Protocol.ERROR_NO_ERROR) {
            throw new Exception("ERROR AL ACTUALIZAR LA FACTURA");
        }
    }

    @Override
    public void delete(Factura e) throws Exception {
        os.writeInt(Protocol.FACTURA_DELETE);
        os.writeObject(e);
        os.flush();

        if (is.readInt() != Protocol.ERROR_NO_ERROR) {
            throw new Exception("ERROR AL ELIMINAR LA FACTURA");
        }
    }

    @Override
    public List<Factura> search(Factura e) {
        try{
        os.writeInt(Protocol.FACTURA_SEARCH);
        os.writeObject(e); // Enviar el objeto para buscar
        os.flush();

        int response = is.readInt();
        if (response == Protocol.ERROR_NO_ERROR) {
            return (List<Factura>) is.readObject(); // Leer y devolver la lista de facturas
        } else {
            throw new Exception("ERROR AL BUSCAR FACTURA");
        }
    } catch (Exception ex) {
        ex.printStackTrace();
        return new ArrayList<>(); // Retorna una lista vacía en caso de error
    }
    }
    public List<Linea> searchByFacturId(String facturaId) {
        try {
            os.writeInt(Protocol.FACTURA_SEARCHFACTURAID); // Protocolo para buscar por facturaId
            os.writeObject(facturaId); // Enviar el id de la factura a buscar
            os.flush();

            int response = is.readInt(); // Leer respuesta del servidor
            if (response == Protocol.ERROR_NO_ERROR) {
                return (List<Linea>) is.readObject(); // Leer y devolver la lista de Linea asociadas a la factura
            } else {
                throw new Exception("ERROR AL BUSCAR FACTURA");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ArrayList<>(); // Retornar una lista vacía en caso de error
        }
    }

    //===================== LINEA =====================

    @Override
    public void create(Linea e) throws Exception {

        os.writeInt(Protocol.LINEA_CREATE);
        os.writeObject(e);
        os.flush();
        if (is.readInt() == Protocol.ERROR_NO_ERROR) {
            // Éxito
        } else {
            throw new Exception("LÍNEA DUPLICADA");
        }
    }

    @Override
    public Linea read(Linea e) throws Exception {
        os.writeInt(Protocol.LINEA_READ);
        os.writeObject(e);
        os.flush();

        int response = is.readInt();
        if (response == Protocol.ERROR_NO_ERROR) {
            return (Linea) is.readObject(); // Leer y devolver el objeto Línea
        } else {
            throw new Exception("ERROR AL LEER LA LÍNEA");
        }
    }

    @Override
    public void update(Linea e) throws Exception {
        os.writeInt(Protocol.LINEA_UPDATE);
        os.writeObject(e);
        os.flush();

        if (is.readInt() != Protocol.ERROR_NO_ERROR) {
            throw new Exception("ERROR AL ACTUALIZAR LA LÍNEA");
        }
    }

    @Override
    public void delete(Linea e) throws Exception {
        os.writeInt(Protocol.LINEA_DELETE);
        os.writeObject(e);
        os.flush();

        if (is.readInt() != Protocol.ERROR_NO_ERROR) {
            throw new Exception("ERROR AL ELIMINAR LA LÍNEA");
        }
    }
//
    @Override
    public List<Linea> search(Linea e) {
        try{
        os.writeInt(Protocol.LINEA_SEARCH);
        os.writeObject(e); // Enviar el objeto para buscar
        os.flush();

        int response = is.readInt();
        if (response == Protocol.ERROR_NO_ERROR) {
            return (List<Linea>) is.readObject(); // Leer y devolver la lista de líneas
        } else {
            throw new Exception("ERROR AL BUSCAR LÍNEA");
        }
    } catch (Exception ex) {
        ex.printStackTrace();
        return new ArrayList<>(); // Retorna una lista vacía en caso de error
    }
    }

    public float[][] getEstadisticas(List<Categoria> categorias, List<String> cols, Rango rango){
        return new float[0][];
    }

}
