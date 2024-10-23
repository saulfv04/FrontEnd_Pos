package pos.presentation.Usuario;

import pos.Application;
import pos.logic.Service;
import pos.logic.SocketListener;
import pos.logic.ThreadListener;
import pos.logic.Usuarios;
import pos.presentation.Usuario.Model;

import java.util.ArrayList;
import java.util.List;

public class Controller implements ThreadListener {
    pos.presentation.Usuario.Model model;
    pos.presentation.Usuario.View view;
    SocketListener socketListener;

    public Controller(View view, Model model) {
        model.init(new ArrayList<>());
        this.view = view;
        this.model = model;
        view.setController(this);
        view.setModel(model);
        try {
            socketListener = new SocketListener(this, Service.instance().getSid(), Application.usuario.getId());
            socketListener.start();
            model.setList(Service.instance().usuariosActivos());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deliver_message(String message) {
        try {
            if ("NEW_CONNECTION".equals(message)) {
                socketListener.deliver("NEW_CONNECTION");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void search(List<String> list) throws Exception {
        model.setList(list);
    }

    public void delete() throws Exception {
    }

    public void clear() {
    }

}