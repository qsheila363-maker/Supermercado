package com.controlador;

import com.modelo.Supermercado;
import com.vista.FormCliente;
import com.vista.FormEmpleados;
import com.vista.FormPrincipal;
import com.vista.FormProducto;
import com.vista.FormVenta;

/** Controlador principal: arma las ventanas y deriva cada botón al controlador correspondiente. */
public class ControlPrincipal {

    private final FormPrincipal vista;
    private final ControlProducto productos;
    private final ControlCliente clientes;
    private final ControlEmpleado empleados;
    private final ControlVenta ventas;

    public ControlPrincipal(Supermercado supermercado) {
        this.vista = new FormPrincipal();
        this.productos = new ControlProducto(supermercado, new FormProducto());
        this.clientes = new ControlCliente(supermercado, new FormCliente());
        this.empleados = new ControlEmpleado(supermercado, new FormEmpleados());
        this.ventas = new ControlVenta(supermercado, new FormVenta());

        vista.getBtnProductos().addActionListener(e -> productos.abrir());
        vista.getBtnClientes().addActionListener(e -> clientes.abrir());
        vista.getBtnEmpleados().addActionListener(e -> empleados.abrir());
        vista.getBtnVenta().addActionListener(e -> ventas.abrir());
        vista.getBtnSalir().addActionListener(e -> System.exit(0));
    }

    public void iniciar() {
        vista.setVisible(true);
    }
}
