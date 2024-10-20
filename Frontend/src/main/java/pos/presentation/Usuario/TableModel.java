package pos.presentation.Usuario;


import pos.logic.Usuarios;
import pos.presentation.AbstractTableModel;

import java.util.List;

public class TableModel extends AbstractTableModel<Usuarios> implements javax.swing.table.TableModel {

    public TableModel(int[] cols, List<Usuarios> rows) {
        super(cols, rows);
    }

    public static final int ID=0;


    @Override
    protected Object getPropetyAt(Usuarios e, int col) {
        switch (cols[col]){
            case ID: return e.getId();
            default: return "";
        }
    }

    @Override
    protected void initColNames(){
        colNames = new String[1];
        colNames[ID]= "Id";
    }
}