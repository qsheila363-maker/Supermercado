package com.vista;

import com.modelo.clientes.Cliente;
import com.modelo.productos.Producto;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.SpinnerNumberModel;

public class FormVenta extends JFrame {

    private final JComboBox<Cliente> cmbCliente = new JComboBox<>();
    private final ModeloTabla modeloProductos =
            new ModeloTabla("Código", "Nombre", "Precio final", "Stock", "Promocionable");
    private final JTable tablaProductos = new JTable(modeloProductos);
    private final ModeloTabla modeloCarrito =
            new ModeloTabla("Producto", "Cantidad", "Precio unitario", "Subtotal");
    private final JTable tablaCarrito = new JTable(modeloCarrito);
    private final JSpinner spnCantidad = new JSpinner(new SpinnerNumberModel(1, 1, 9999, 1));
    private final JComboBox<String> cmbPromocion =
            new JComboBox<>(new String[]{"Sin promoción", "2x1", "Segunda unidad al 50%"});
    private final JButton btnAgregar = new JButton("Agregar al carrito");
    private final JButton btnFinalizar = new JButton("Finalizar compra");
    private final JLabel lblTotal = new JLabel("Total: $0.00");

    // Productos que se están mostrando (la fila de la tabla coincide con el índice)
    private List<Producto> productosMostrados = new ArrayList<>();

    public FormVenta() {
        setTitle("Realizar compra");
        setDefaultCloseOperation(HIDE_ON_CLOSE);

        JPanel norte = new JPanel(new BorderLayout(8, 0));
        norte.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));
        norte.add(new JLabel("Cliente:"), BorderLayout.WEST);
        norte.add(cmbCliente, BorderLayout.CENTER);

        JScrollPane scrollProductos = new JScrollPane(tablaProductos);
        scrollProductos.setBorder(BorderFactory.createTitledBorder("Productos disponibles"));
        JScrollPane scrollCarrito = new JScrollPane(tablaCarrito);
        scrollCarrito.setBorder(BorderFactory.createTitledBorder("Carrito"));

        JPanel centro = new JPanel(new GridLayout(2, 1, 0, 6));
        centro.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        centro.add(scrollProductos);
        centro.add(scrollCarrito);

        JPanel controles = new JPanel(new FlowLayout(FlowLayout.LEFT));
        controles.add(new JLabel("Cantidad:"));
        controles.add(spnCantidad);
        controles.add(new JLabel("Promoción:"));
        controles.add(cmbPromocion);
        controles.add(btnAgregar);

        JPanel totales = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        lblTotal.setFont(lblTotal.getFont().deriveFont(16f));
        totales.add(lblTotal);
        totales.add(btnFinalizar);

        JPanel sur = new JPanel(new BorderLayout());
        sur.add(controles, BorderLayout.WEST);
        sur.add(totales, BorderLayout.EAST);

        JPanel raiz = new JPanel(new BorderLayout(0, 6));
        raiz.add(norte, BorderLayout.NORTH);
        raiz.add(centro, BorderLayout.CENTER);
        raiz.add(sur, BorderLayout.SOUTH);
        setContentPane(raiz);

        setSize(860, 620);
        setLocationRelativeTo(null);
    }

    public void cargarClientes(List<Cliente> clientes) {
        Cliente seleccionado = (Cliente) cmbCliente.getSelectedItem();
        cmbCliente.removeAllItems();
        for (Cliente c : clientes) {
            cmbCliente.addItem(c);
        }
        if (seleccionado != null && clientes.contains(seleccionado)) {
            cmbCliente.setSelectedItem(seleccionado);
        }
    }

    public void cargarProductos(List<Producto> productos) {
        int fila = tablaProductos.getSelectedRow();
        productosMostrados = new ArrayList<>(productos);
        modeloProductos.setRowCount(0);
        for (Producto p : productosMostrados) {
            modeloProductos.addRow(new Object[]{
                p.getCodigo(), p.getNombre(),
                String.format("%.2f", p.calcularPrecioFinal()),
                p.getStock(),
                p instanceof com.modelo.interfaces.Promocionable ? "Sí" : "No"
            });
        }
        if (fila >= 0 && fila < modeloProductos.getRowCount()) {
            tablaProductos.setRowSelectionInterval(fila, fila);
        }
    }

    public void agregarLineaCarrito(String nombre, int cantidad, double precioUnitario, double subtotal) {
        modeloCarrito.addRow(new Object[]{
            nombre, cantidad,
            String.format("%.2f", precioUnitario),
            String.format("%.2f", subtotal)
        });
    }

    public void limpiarCarrito() {
        modeloCarrito.setRowCount(0);
        setTotal(0);
        spnCantidad.setValue(1);
        cmbPromocion.setSelectedIndex(0);
        cmbCliente.setEnabled(true);
    }

    public void setTotal(double total) {
        lblTotal.setText(String.format("Total: $%.2f", total));
    }

    /** Mientras haya productos en el carrito no se puede cambiar de cliente. */
    public void bloquearCliente(boolean bloquear) {
        cmbCliente.setEnabled(!bloquear);
    }

    public void mostrarMensaje(String mensaje) { Mensajes.info(this, mensaje); }
    public void mostrarError(String mensaje) { Mensajes.error(this, mensaje); }

    public Cliente getClienteSeleccionado() { return (Cliente) cmbCliente.getSelectedItem(); }

    public Producto getProductoSeleccionado() {
        int fila = tablaProductos.getSelectedRow();
        if (fila < 0 || fila >= productosMostrados.size()) {
            return null;
        }
        return productosMostrados.get(fila);
    }

    public int getCantidad() { return (Integer) spnCantidad.getValue(); }

    /** 0 = ninguna, 1 = 2x1, 2 = segunda unidad al 50%. */
    public int getPromocion() { return cmbPromocion.getSelectedIndex(); }

    public JButton getBtnAgregar() { return btnAgregar; }
    public JButton getBtnFinalizar() { return btnFinalizar; }
}
