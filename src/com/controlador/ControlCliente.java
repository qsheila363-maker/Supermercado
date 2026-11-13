package com.controlador;

import com.modelo.Supermercado;
import com.modelo.clientes.Cliente;
import com.modelo.clientes.ClienteVIP;
import com.vista.FormCliente;
import com.vista.Mensajes;
import java.sql.SQLException;

public class ControlCliente {

    private final Supermercado supermercado;
    private final FormCliente vista;

    public ControlCliente(Supermercado supermercado, FormCliente vista) {
        this.supermercado = supermercado;
        this.vista = vista;
        vista.getBtnGuardar().addActionListener(e -> guardarCliente());
        vista.getBtnLimpiar().addActionListener(e -> vista.limpiarCampos());
        vista.getBtnHistorial().addActionListener(e -> mostrarHistorial());
    }

    public void abrir() {
        vista.mostrarClientes(supermercado.getClientes());
        vista.setVisible(true);
        vista.toFront();
    }

    public void guardarCliente() {
        String nombre = vista.getTxtNombre().getText().trim();
        String dni = vista.getTxtDni().getText().trim();

        if (nombre.isEmpty() || dni.isEmpty()) {
            vista.mostrarError("Completá el nombre y el DNI.");
            return;
        }

        try {
            Cliente cliente;
            if (vista.isVip()) {
                double descuento = Double.parseDouble(vista.getDescuento());
                if (descuento < 0 || descuento > 100) {
                    vista.mostrarError("El descuento debe estar entre 0 y 100.");
                    return;
                }
                cliente = new ClienteVIP(nombre, dni, descuento);
            } else {
                cliente = new Cliente(nombre, dni);
            }

            supermercado.guardarClienteBD(cliente);
            supermercado.agregarCliente(cliente);
            vista.mostrarClientes(supermercado.getClientes());
            vista.limpiarCampos();
            vista.mostrarMensaje("Cliente guardado correctamente.");

        } catch (SQLException ex) {
            vista.mostrarError("No se pudo guardar el cliente en MySQL: " + ex.getMessage());
        } catch (NumberFormatException ex) {
            vista.mostrarError("El descuento debe ser un número válido.");
        } catch (IllegalArgumentException ex) {
            vista.mostrarError(ex.getMessage());
        }
    }

    public void mostrarHistorial() {
        int fila = vista.getFilaSeleccionada();
        if (fila < 0 || fila >= supermercado.getClientes().size()) {
            vista.mostrarError("Seleccioná un cliente de la tabla.");
            return;
        }
        Cliente cliente = supermercado.getClientes().get(fila);
        Mensajes.info(vista, cliente.mostrarCompras());
    }
}
