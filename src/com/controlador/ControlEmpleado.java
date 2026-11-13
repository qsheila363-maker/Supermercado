package com.controlador;

import com.modelo.Supermercado;
import com.modelo.empleados.Cajero;
import com.modelo.empleados.Empleado;
import com.modelo.empleados.Gerente;
import com.vista.FormEmpleados;
import java.sql.SQLException;

public class ControlEmpleado {

    private final Supermercado supermercado;
    private final FormEmpleados vista;

    public ControlEmpleado(Supermercado supermercado, FormEmpleados vista) {
        this.supermercado = supermercado;
        this.vista = vista;
        vista.getBtnGuardar().addActionListener(e -> agregarEmpleado());
        vista.getBtnLimpiar().addActionListener(e -> vista.limpiarCampos());
    }

    public void abrir() {
        vista.mostrarEmpleados(supermercado.getEmpleados());
        vista.setVisible(true);
        vista.toFront();
    }

    public void agregarEmpleado() {
        String nombre = vista.getNombre();
        String id = vista.getId();

        if (nombre.isEmpty() || id.isEmpty() || vista.getSalario().isEmpty() || vista.getExtra().isEmpty()) {
            vista.mostrarError("Completá todos los campos.");
            return;
        }

        try {
            double salario = Double.parseDouble(vista.getSalario());
            double extra = Double.parseDouble(vista.getExtra());
            if (salario < 0 || extra < 0) {
                vista.mostrarError("Los montos no pueden ser negativos.");
                return;
            }

            Empleado empleado;
            if (vista.getTipo() == 0) {
                empleado = new Cajero(nombre, id, salario, extra);
            } else {
                empleado = new Gerente(nombre, id, salario, extra);
            }

            supermercado.guardarEmpleadoBD(empleado);
            supermercado.agregarEmpleado(empleado);
            vista.mostrarEmpleados(supermercado.getEmpleados());
            vista.limpiarCampos();
            vista.mostrarMensaje("Empleado agregado correctamente.");

        } catch (SQLException ex) {
            vista.mostrarError("No se pudo guardar el empleado en MySQL: " + ex.getMessage());
        } catch (NumberFormatException ex) {
            vista.mostrarError("Salario, ventas y bono deben ser números válidos.");
        } catch (IllegalArgumentException ex) {
            vista.mostrarError(ex.getMessage());
        }
    }
}
