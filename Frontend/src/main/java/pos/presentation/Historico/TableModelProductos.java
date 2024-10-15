package pos.presentation.Historico;

import pos.logic.Linea;
import pos.presentation.AbstractTableModel;

import java.util.List;

public class TableModelProductos extends AbstractTableModel<Linea> implements javax.swing.table.TableModel {

    public TableModelProductos(int[] cols, List<Linea> rows) {
        super(cols, rows);
    }

    public static final int ID = 0;
    public static final int NOMBRE = 1;
    public static final int CATEGORIA = 2;
    public static final int CANTIDAD = 3;
    public static final int PRECIO = 4;
    public static final int DESCUENTO = 5;
    public static final int NETO = 6;
    public static final int IMPORTE = 7;

    @Override
    protected Object getPropetyAt(Linea e, int col) {
        return switch (cols[col]) {
            case ID -> e.getProducto().getCodigo();
            case NOMBRE -> e.getProducto().getDescripcion();
            case CATEGORIA -> e.getProducto().getCategoria().getNombre();
            case CANTIDAD -> e.getCantidad();
            case PRECIO ->  String.format("%.2f",e.getProducto().getPrecioUnitario());
            case DESCUENTO ->  String.format("%.2f",e.calcularDescuento());
            case NETO ->  String.format("%.2f",e.calcularNeto());
            case IMPORTE -> String.format("%.2f",e.calcularImporte());
            default -> "";
        };
    }

    @Override
    protected void initColNames() {
        colNames = new String[8];
        colNames[ID] = "Codigo";
        colNames[NOMBRE] = "Articulo";
        colNames[CATEGORIA] = "Categoria";
        colNames[CANTIDAD] = "Cantidad";
        colNames[PRECIO] = "Precio";
        colNames[DESCUENTO] = "Descuento";
        colNames[NETO] = "Neto";
        colNames[IMPORTE] = "Importe";
    }
}