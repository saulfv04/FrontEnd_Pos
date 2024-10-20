package pos.presentation.Usuario;

import pos.Application;
import pos.logic.Service;
import pos.logic.Usuarios;
import pos.presentation.Usuario.Model;

import java.util.ArrayList;
import java.util.List;

public class Controller {
    pos.presentation.Usuario.Model model;
    pos.presentation.Usuario.View view;

    public Controller(View view,Model model) {
        model.init(new ArrayList<>());
        this.view= view;
        this.model = model;
        view.setController(this);
        view.setModel(model);
    }

    public void search(List<Usuarios> list) throws  Exception{
        model.setList(list);
    }


    public void delete() throws Exception {
    }

    public void clear() {
    }

}
