package com.vista;

import javax.swing.table.DefaultTableModel;

/** Modelo de tabla de solo lectura. */
public class ModeloTabla extends DefaultTableModel {

    public ModeloTabla(String... columnas) {
        super(columnas, 0);
    }

    @Override
    public boolean isCellEditable(int fila, int columna) {
        return false;
    }
}
