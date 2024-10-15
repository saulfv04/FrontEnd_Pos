package pos.presentation.facturar;

import pos.logic.Linea;
import pos.presentation.AbstractTableModel;

import java.util.List;

public class TableModel extends AbstractTableModel<Linea> implements javax.swing.table.TableModel {

    public TableModel(int[] cols, List<Linea> rows) {
        super(cols, rows);
    }

    public static final int ID=0;
    public static final int NOMBRE=1;
    public static final int CATEGORIA=2;
    public static final int CANTIDAD=3;
    public static final int PRECIO=4;
    public static final int DESCUENTO=5;
    public static final int NETO=6;
    public static final int IMPORTE=7;

    @Override
    protected Object getPropetyAt(Linea e, int col) {
        return switch (cols[col]) {
            case ID -> e.getProducto().getCodigo();
            case NOMBRE -> e.getProducto().getDescripcion();
            case CATEGORIA -> e.getProducto().getCategoria();
            case CANTIDAD -> e.getCantidad();
            case PRECIO -> e.getProducto().getPrecioUnitario();
            case DESCUENTO -> String.format("%.2f", e.calcularDescuento());
            case NETO -> String.format("%.2f", e.calcularNeto());
            case IMPORTE -> String.format("%.2f", e.calcularImporte());
            default -> "";
        };
    }

    @Override
    protected void initColNames(){
        colNames = new String[8];
        colNames[ID]= "Codigo";
        colNames[NOMBRE]= "Nombre";
        colNames[CATEGORIA]= "Categoria";
        colNames[CANTIDAD]= "Cantidad";
        colNames[PRECIO]= "Precio";
        colNames[DESCUENTO]= "Descuento";
        colNames[NETO]= "Neto";
        colNames[IMPORTE]= "Importe";
    }

    public double cantidadTotalArticulos(){
        double cantidadT=0;
        for (int i=0; i<getRowCount(); i++){
            for (int j=0; j<getColumnCount(); j++){
                Object value = getValueAt(i,CANTIDAD);
                if(value instanceof Integer){
                    cantidadT+=((Integer)value).doubleValue();
                }
            }
        }
    return cantidadT;
    }


    public double subtotalTotal(){
        double cantidad=0;
        for (int i=0; i<getRowCount(); i++){
            for (int j=0; j<getColumnCount(); j++){
                Object value = getValueAt(i,NETO);
                if(value instanceof Double){
                    cantidad+=((Double)value).doubleValue();
                }
            }
        }
        return cantidad;
    }

    public double descuentoTotal(){
        double cantidad=0;
        for (int i=0; i<getRowCount(); i++){
            for (int j=0; j<getColumnCount(); j++){
                Object value = getValueAt(i,DESCUENTO);
                if(value instanceof Double){
                    cantidad+=((Double)value).doubleValue();
                }
            }
        }
        return cantidad;
    }

    public double total(){
        double cantidad=0;
        for (int i=0; i<getRowCount(); i++){
            for (int j=0; j<getColumnCount(); j++){
                Object value = getValueAt(i,IMPORTE);
                if(value instanceof Double){
                    cantidad+=((Double)value).doubleValue();
                }
            }
        }
        return cantidad;
    }

    public String getNombreProducto(int rowIndex) {
        Object value = getValueAt(rowIndex,NOMBRE);
        String nombre=value.toString();
        return nombre;
    }

}
