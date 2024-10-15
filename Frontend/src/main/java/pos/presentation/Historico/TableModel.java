package pos.presentation.Historico;

import pos.logic.Factura;
import pos.presentation.AbstractTableModel;

import java.util.List;

public class TableModel  extends AbstractTableModel<Factura> implements javax.swing.table.TableModel {

    public TableModel(int[] cols, List<Factura> rows) {
        super(cols, rows);
    }

    public static final int ID=0;
    public static final int IDCLIENTE=1;
    public static final int IDCAJERO=2;
    public static final int FECHA=3;
    public static final int TOTAL=4;
    @Override
    protected Object getPropetyAt(Factura e, int col) {
        return switch (cols[col]) {
            case ID -> e.getId();
            case IDCLIENTE -> e.getCliente().getNombre();
            case IDCAJERO -> e.getCajero().getNombre();
            case FECHA -> e.getFecha().toString();
            case TOTAL -> String.format("%.2f", e.getTotal());
            default -> "";
        };
    }

    @Override
    protected void initColNames(){
        colNames = new String[5];
        colNames[ID]= "Codigo";
        colNames[IDCLIENTE]= "Cliente";
        colNames[IDCAJERO]= "Cajero";
        colNames[FECHA]= "Fecha";
        colNames[TOTAL]= "Total";

    }
}