package pos.presentation.Usuario;

import pos.Application;
import pos.logic.Factura;
import pos.logic.Usuarios;
import pos.presentation.AbstractModel;

import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

public class Model extends AbstractModel{

        List<Usuarios> listUsuarios;
        Usuarios current;
        List<Factura> listaFacturas;
        int mode;
        Usuarios especifico;

        @Override
        public void addPropertyChangeListener(PropertyChangeListener listener) {
            super.addPropertyChangeListener(listener);
            firePropertyChange(LISTUSUARIOS);
            firePropertyChange(CURRENT);
            firePropertyChange(LISTA_FACTURAS);
        }

        public Model() {

        }

        public void init(List<Usuarios> list){
            this.listUsuarios = list;
            this.mode= Application.MODE_CREATE;
            this.listaFacturas = new ArrayList<>();
            this.current = new Usuarios();
        }

        public List<Factura> getListFacturac() {
            return listaFacturas;
        }

        public void setListUsuarios(List<Usuarios> list){
            this.listUsuarios = list;
            firePropertyChange(LISTUSUARIOS);
        }

    public Usuarios getCurrent() {
        return current;
    }

    public void setCurrent(Usuarios current) {
        this.current = current;
        firePropertyChange(CURRENT);
    }

    public int getMode() {
            return mode;
        }

        public void setMode(int mode) {
            this.mode = mode;
        }

    public List<Factura> getListaFacturas() {
        return listaFacturas;
    }

    public Usuarios getUsuarioEspecifico() {
        return especifico;
    }

    public void setUsuarioEspecifico(Usuarios usuario) {
        this.especifico = usuario;
        firePropertyChange(LISTA_FACTURAS);
    }

    public void setListaFacturas(List<Factura> listaFacturas) {
            this.listaFacturas = listaFacturas;
            firePropertyChange(LISTA_FACTURAS);
    }




    public void addUsuario(Usuarios usuarios){
            this.listUsuarios.add(usuarios);
        }

    public static final String LISTUSUARIOS="listuUsuarios";
    public static final String CURRENT="Current";
    public static final String LISTA_FACTURAS="listaFacturas";


    public List<Usuarios> getListUsuarios() {
        return listUsuarios;
    }

    public void addFactura(Factura factura, Usuarios usuarios) {
        for (Usuarios user : listUsuarios) {
            if (user.getId().equals(usuarios.getId())) {
                user.getListaFacturas().add(factura);
                especifico = user;
                firePropertyChange(LISTA_FACTURAS);
                break;
            }
        }
    }
}
