package pos.presentation.producto;

import pos.logic.Producto;
import pos.presentation.AbstractTableModel;

import java.util.List;

public class TableModel  extends AbstractTableModel<Producto> implements javax.swing.table.TableModel {

    public TableModel(int[] cols, List<Producto> rows) {
        super(cols, rows);
    }

    private List<?> rows;
    private String[] columns;



    public static final int ID=0;
    public static final int DESCRIPCION=1;
    public static final int PRECIO=2;
    public static final int UNIDADADEMEDIDA=3;
    public static final int CATEGORIA=4;
    public static final int EXISTENCIAS=5;

    @Override
    protected Object getPropetyAt(Producto e, int col) {
        switch (cols[col]){
            case ID: return e.getCodigo();
            case DESCRIPCION: return e.getDescripcion();
            case PRECIO: return e.getPrecioUnitario(); // double
            case UNIDADADEMEDIDA: return e.getUnidadDeMedidad(); // double
            case EXISTENCIAS: return e.getExistencia(); // int
            case CATEGORIA: return e.getCategoria().getNombre();
            default: return "";
        }
    }

    @Override
    protected void initColNames(){
        colNames = new String[6];
        colNames[ID]= "Id";
        colNames[DESCRIPCION]= "DESCRIPCION";
        colNames[PRECIO]= "PRECIO";
        colNames[UNIDADADEMEDIDA]= "UNIDAD DE MEDIDA";
        colNames[CATEGORIA]= "CATEGORIA";
        colNames[EXISTENCIAS]= "EXISTENCIAS";
    }

}
