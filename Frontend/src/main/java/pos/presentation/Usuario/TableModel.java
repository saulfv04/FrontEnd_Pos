package pos.presentation.Usuario;


import pos.logic.Usuarios;
import pos.presentation.AbstractTableModel;

import java.util.List;

public class TableModel extends AbstractTableModel<Usuarios> implements javax.swing.table.TableModel {


    public static final int ID = 0;
    public static final int FACTURA = 1; // Nueva columna para el checkbox

    private Boolean[] checkBoxData; // Para almacenar el estado del JCheckBox

    public TableModel(int[] cols, List<Usuarios> rows) {
        super(cols, rows);
        // Inicializamos el array de JCheckBox con valores falsos
        checkBoxData = new Boolean[rows.size()];
        for (int i = 0; i < rows.size(); i++) {
            checkBoxData[i] = false;
        }
    }

    @Override
    protected Object getPropetyAt(Usuarios e, int col) {
        switch (cols[col]) {
            case ID:
                return e.getId(); // El ID del usuario (string)
            case FACTURA:
                return checkBoxData[rows.indexOf(e)]; // Devuelve el valor del checkbox
            default:
                return "";
        }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int colIndex) {
        // Hacemos que solo la columna del checkbox sea editable
        return colIndex == FACTURA;
    }

    @Override
    public void setValueAt(Object value, int rowIndex, int colIndex) {
        if (colIndex == FACTURA) {
            checkBoxData[rowIndex] = (Boolean) value; // Asigna el valor del JCheckBox
            fireTableCellUpdated(rowIndex, colIndex); // Notifica a la tabla que la celda cambió
        }
    }

    @Override
    protected void initColNames() {
        colNames = new String[2]; // Ahora tenemos 2 columnas
        colNames[ID] = "Id";
        colNames[FACTURA] = "Facturas?";
    }

    @Override
    public Class<?> getColumnClass(int colIndex) {
        if (colIndex == FACTURA) {
            return Boolean.class; // Indicamos que la columna de facturas es un Boolean para el JCheckBox
        }
        return String.class;
    }


    public void marcarString(Usuarios valorEntrante) {
        // Recorremos la lista de filas (rows)
        for (int i = 0; i < rows.size(); i++) {
            if (rows.get(i).equals(valorEntrante)) {
                // Marcamos el checkbox correspondiente si hay coincidencia
                checkBoxData[i] = true;
                // Notificamos a la tabla que la celda cambió para que se actualice visualmente
                fireTableCellUpdated(i, FACTURA);
                break; // Rompemos el ciclo si encontramos la coincidencia
            }
        }
    }
}