package com.controlador;

import com.modelo.Supermercado;
import com.modelo.productos.Producto;
import com.modelo.productos.ProductoAlimenticio;
import com.modelo.productos.ProductoElectronico;
import com.modelo.productos.ProductoHigiene;
import com.vista.FormProducto;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import javax.swing.JOptionPane;

public class ControlProducto {
    private final Supermercado supermercado;
    private final FormProducto vista;

    public ControlProducto(Supermercado supermercado, FormProducto vista) {
        this.supermercado = supermercado; this.vista = vista;
        vista.getBtnGuardar().addActionListener(e -> guardarProducto());
        vista.getBtnActualizar().addActionListener(e -> actualizarProducto());
        vista.getBtnEliminar().addActionListener(e -> eliminarProducto());
        vista.getBtnLimpiar().addActionListener(e -> vista.limpiarCampos());
        vista.getTabla().getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                String codigo = vista.getCodigoSeleccionado();
                if (!codigo.isEmpty()) try { vista.cargarProductoEnFormulario(supermercado.buscarProducto(codigo)); }
                catch (Exception ex) { vista.mostrarError(ex.getMessage()); }
            }
        });
    }

    public void abrir() { vista.mostrarProductos(supermercado.getProductos()); vista.setVisible(true); vista.toFront(); }

    /** CREATE si el código no existe; si existe, solamente suma stock. */
    public void guardarProducto() {
        try {
            String codigo = vista.getCodigo();
            if (codigo.isEmpty()) { vista.mostrarError("Ingresá el código del producto."); return; }
            if (supermercado.existeProducto(codigo)) {
                if (vista.getStock().isEmpty()) { vista.mostrarError("Ingresá cuántas unidades querés sumar."); return; }
                int cantidad = Integer.parseInt(vista.getStock());
                if (cantidad <= 0) { vista.mostrarError("Para sumar stock, ingresá una cantidad mayor que cero."); return; }
                supermercado.sumarStockBD(codigo, cantidad);
                vista.mostrarMensaje("El producto ya existía. Se sumaron " + cantidad + " unidades al stock.");
            } else {
                Producto producto = leerProducto();
                supermercado.guardarProductoBD(producto);
                supermercado.agregarProducto(producto);
                vista.mostrarMensaje("Producto nuevo registrado correctamente.");
            }
            refrescar();
        } catch (SQLException ex) { vista.mostrarError("Error en MySQL: " + ex.getMessage()); }
        catch (DateTimeParseException ex) { vista.mostrarError("Fecha inválida. Use AAAA-MM-DD."); }
        catch (NumberFormatException ex) { vista.mostrarError("Precio, stock y garantía deben ser números válidos."); }
        catch (IllegalArgumentException ex) { vista.mostrarError(ex.getMessage()); }
    }

    /** UPDATE: modifica los datos del producto seleccionado en la tabla. */
    public void actualizarProducto() {
        String codigo = vista.getCodigoSeleccionado();
        if (codigo.isEmpty()) { vista.mostrarError("Seleccioná un producto de la tabla para actualizarlo."); return; }
        try {
            Producto producto = leerProducto();
            if (!codigo.equals(producto.getCodigo())) { vista.mostrarError("No se puede cambiar el código del producto seleccionado."); return; }
            supermercado.actualizarProductoBD(producto);
            refrescar(); vista.mostrarMensaje("Producto actualizado correctamente.");
        } catch (SQLException ex) { vista.mostrarError("No se pudo actualizar en MySQL: " + ex.getMessage()); }
        catch (DateTimeParseException ex) { vista.mostrarError("Fecha inválida. Use AAAA-MM-DD."); }
        catch (NumberFormatException ex) { vista.mostrarError("Precio, stock y garantía deben ser números válidos."); }
        catch (IllegalArgumentException ex) { vista.mostrarError(ex.getMessage()); }
    }

    /** DELETE: MySQL rechazará el borrado si el producto figura en detalle_compra. */
    public void eliminarProducto() {
        String codigo = vista.getCodigoSeleccionado();
        if (codigo.isEmpty()) { vista.mostrarError("Seleccioná un producto de la tabla para eliminarlo."); return; }
        int opcion = JOptionPane.showConfirmDialog(vista, "¿Eliminar el producto " + codigo + "?", "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
        if (opcion != JOptionPane.YES_OPTION) return;
        try {
            supermercado.eliminarProductoBD(codigo); refrescar(); vista.mostrarMensaje("Producto eliminado correctamente.");
        } catch (SQLException ex) {
            vista.mostrarError("No se pudo eliminar. Si el producto tiene ventas asociadas, debe conservarse el historial. Detalle: " + ex.getMessage());
        }
    }

    private Producto leerProducto() {
        String codigo=vista.getCodigo(), nombre=vista.getNombre(), extra=vista.getExtra();
        if (codigo.isEmpty() || nombre.isEmpty() || vista.getPrecio().isEmpty() || vista.getStock().isEmpty() || extra.isEmpty())
            throw new IllegalArgumentException("Completá todos los campos.");
        double precio=Double.parseDouble(vista.getPrecio()); int stock=Integer.parseInt(vista.getStock());
        if (precio <= 0) throw new IllegalArgumentException("El precio debe ser mayor que cero.");
        if (stock < 0) throw new IllegalArgumentException("El stock no puede ser negativo.");
        switch (vista.getTipo()) {
            case 0: return new ProductoAlimenticio(codigo,nombre,precio,LocalDate.parse(extra),stock);
            case 1:
                int garantia=Integer.parseInt(extra);
                if (garantia < 0) throw new IllegalArgumentException("La garantía no puede ser negativa.");
                return new ProductoElectronico(codigo,nombre,precio,garantia,stock);
            default: return new ProductoHigiene(codigo,nombre,precio,extra,stock);
        }
    }
    private void refrescar() { vista.mostrarProductos(supermercado.getProductos()); vista.limpiarCampos(); }
}
