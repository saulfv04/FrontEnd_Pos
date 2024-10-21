package pos.presentation.Usuario;

import pos.Application;
import pos.logic.Usuarios;
import pos.presentation.AbstractModel;

import java.beans.PropertyChangeListener;
import java.util.List;

public class Model extends AbstractModel{

        List<String> list;
        Usuarios current;
        int mode;

        @Override
        public void addPropertyChangeListener(PropertyChangeListener listener) {
            super.addPropertyChangeListener(listener);
            firePropertyChange(LIST);
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
    }

    public int getMode() {
            return mode;
        }

        public void setMode(int mode) {
            this.mode = mode;
        }


        public void addUsuario(String usuarios){
            this.list.add(usuarios);
        }

        public static final String LIST="list";


}
