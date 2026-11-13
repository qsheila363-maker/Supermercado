package com.controlador;

import com.modelo.Supermercado;
import com.modelo.clientes.Cliente;
import com.modelo.excepciones.ClienteNoRegistradoException;
import com.modelo.excepciones.ProductoNoDisponibleException;
import com.modelo.interfaces.Promocionable;
import com.modelo.productos.Producto;
import com.vista.FormVenta;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ControlVenta {

    private final Supermercado supermercado;
    private final FormVenta vista;
    private double total = 0;
    private final List<Supermercado.LineaVenta> lineasVenta = new ArrayList<>();

    public ControlVenta(Supermercado supermercado, FormVenta vista) {
        this.supermercado = supermercado;
        this.vista = vista;
        vista.getBtnAgregar().addActionListener(e -> agregarAlCarrito());
        vista.getBtnFinalizar().addActionListener(e -> finalizarCompra());
    }

    public void abrir() {
        vista.cargarClientes(supermercado.getClientes());
        vista.cargarProductos(supermercado.getProductos());
        vista.setVisible(true);
        vista.toFront();
    }

    public void agregarAlCarrito() {
        Cliente cliente = vista.getClienteSeleccionado();
        if (cliente == null) {
            vista.mostrarError("Registrá un cliente antes de comprar.");
            return;
        }
        Producto producto = vista.getProductoSeleccionado();
        if (producto == null) {
            vista.mostrarError("Seleccioná un producto de la tabla.");
            return;
        }

        int cantidad = vista.getCantidad();
        int promocion = vista.getPromocion();
        if (promocion != 0 && !(producto instanceof Promocionable)) {
            vista.mostrarError("Este producto no admite promociones.");
            return;
        }

        try {
            double precioUnitario = producto.calcularPrecioFinal();
            double subtotal = supermercado.registrarCompra(cliente, producto, cantidad, promocion);
            total += subtotal;
            lineasVenta.add(new Supermercado.LineaVenta(producto, cantidad, promocion, precioUnitario, subtotal));

            vista.agregarLineaCarrito(producto.getNombre(), cantidad, precioUnitario, subtotal);
            vista.setTotal(total);
            vista.bloquearCliente(true);
            vista.cargarProductos(supermercado.getProductos()); // refresca el stock

        } catch (ClienteNoRegistradoException | ProductoNoDisponibleException | IllegalArgumentException ex) {
            vista.mostrarError(ex.getMessage());
        }
    }

    public void finalizarCompra() {
        Cliente cliente = vista.getClienteSeleccionado();
        if (total <= 0 || cliente == null) {
            vista.mostrarError("No se realizó ninguna compra.");
            return;
        }
        try {
            supermercado.guardarCompraBD(cliente, lineasVenta, total);
            vista.mostrarMensaje("Cliente: " + cliente.getNombre()
                    + "\nTotal a pagar: $" + String.format("%.2f", total)
                    + "\n\n¡Compra guardada en la base de datos!");
            total = 0;
            lineasVenta.clear();
            vista.limpiarCarrito();
        } catch (SQLException ex) {
            vista.mostrarError("No se pudo guardar la compra en MySQL. No se registró la venta.\n" + ex.getMessage()
                    + "\nCerrá o reiniciá el programa para volver a cargar el stock real.");
        }
    }
}
