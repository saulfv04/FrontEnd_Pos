package pos.presentation.Usuario;

import pos.Application;
import pos.logic.*;
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
            socketListener = new SocketListener(this, Service.instance().getSid(), Application.usuario);
            socketListener.start();
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

    public void search(List<Usuarios> list) throws Exception {
        model.setListUsuarios(list);
    }

    public void delete() throws Exception {
    }

    public void clear() {
    }


    public void setList(List<Usuarios> activeUsers) {
        model.setListUsuarios(activeUsers);
    }


    public void send(Factura factura,String id) {
        try {
            Service.instance().enviarFactura(factura,id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Usuarios> getListUsuarios() {
        return model.getListUsuarios();
    }

    public List<Factura> getListaFacturas(){
        return model.getListaFacturas();
    }

    public void facturRecibida(Factura factura,Usuarios usuarios){
        model.listaFacturas.add(factura);
        model.setUsuarioEspecifico(usuarios);
        model.setListaFacturas(model.listaFacturas);

    }


}