package pos.presentation.Usuario;

import pos.Application;
import pos.logic.Factura;
import pos.logic.Usuarios;
import pos.presentation.AbstractModel;

import java.beans.PropertyChangeListener;
import java.util.List;

public class Model extends AbstractModel{

        List<String> list;
        Usuarios current;
        List<Factura> listaFacturas;
        int mode;

        @Override
        public void addPropertyChangeListener(PropertyChangeListener listener) {
            super.addPropertyChangeListener(listener);
            firePropertyChange(LIST);
            firePropertyChange(CURRENT);
        }

        public Model() {

        }

        public void init(List<String> list){
            this.list = list;
            this.mode= Application.MODE_CREATE;
        }

        public List<String> getList() {
            return list;
        }

        public void setList(List<String> list){
            this.list = list;
            firePropertyChange(LIST);
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

    public void setListaFacturas(List<Factura> listaFacturas) {
        this.listaFacturas = listaFacturas;
    }

    public void addUsuario(String usuarios){
            this.list.add(usuarios);
        }

    public static final String LIST="list";
    public static final String CURRENT="Current";


}
