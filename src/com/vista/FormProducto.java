package com.vista;

import com.modelo.productos.Producto;
import com.modelo.productos.ProductoAlimenticio;
import com.modelo.productos.ProductoElectronico;
import com.modelo.productos.ProductoHigiene;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

public class FormProducto extends JFrame {
    private final JComboBox<String> cmbTipo = new JComboBox<>(new String[]{"Alimenticio", "Electrónico", "Higiene"});
    private final JTextField txtCodigo = new JTextField();
    private final JTextField txtNombre = new JTextField();
    private final JTextField txtPrecio = new JTextField();
    private final JTextField txtStock = new JTextField();
    private final JTextField txtExtra = new JTextField();
    private final JLabel lblExtra = new JLabel();
    private final JButton btnGuardar = new JButton("Guardar / sumar stock");
    private final JButton btnActualizar = new JButton("Actualizar datos");
    private final JButton btnEliminar = new JButton("Eliminar");
    private final JButton btnLimpiar = new JButton("Limpiar");
    private final ModeloTabla modelo = new ModeloTabla("Código", "Nombre", "Categoría", "Precio", "Precio final", "Stock");
    private final JTable tabla = new JTable(modelo);

    public FormProducto() {
        setTitle("Productos"); setDefaultCloseOperation(HIDE_ON_CLOSE);
        JPanel campos = new JPanel(new GridLayout(0, 2, 6, 6));
        campos.add(new JLabel("Tipo:")); campos.add(cmbTipo);
        campos.add(new JLabel("Código:")); campos.add(txtCodigo);
        campos.add(new JLabel("Nombre:")); campos.add(txtNombre);
        campos.add(new JLabel("Precio:")); campos.add(txtPrecio);
        campos.add(new JLabel("Stock / cantidad a sumar:")); campos.add(txtStock);
        campos.add(lblExtra); campos.add(txtExtra);
        JPanel botones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        botones.add(btnLimpiar); botones.add(btnEliminar); botones.add(btnActualizar); botones.add(btnGuardar);
        JPanel formulario = new JPanel(new BorderLayout(0, 8));
        formulario.setBorder(BorderFactory.createEmptyBorder(12, 12, 8, 12));
        formulario.add(campos, BorderLayout.CENTER); formulario.add(botones, BorderLayout.SOUTH);
        JPanel raiz = new JPanel(new BorderLayout()); raiz.add(formulario, BorderLayout.NORTH); raiz.add(new JScrollPane(tabla), BorderLayout.CENTER);
        setContentPane(raiz);
        cmbTipo.addActionListener(e -> actualizarEtiquetaExtra()); actualizarEtiquetaExtra();
        setSize(900, 560); setLocationRelativeTo(null);
    }

    private void actualizarEtiquetaExtra() {
        switch (cmbTipo.getSelectedIndex()) {
            case 0: lblExtra.setText("Fecha caducidad (AAAA-MM-DD):"); break;
            case 1: lblExtra.setText("Garantía (años):"); break;
            default: lblExtra.setText("Tipo de uso:"); break;
        }
    }

    private void cargarSeleccionado() {
        int fila = tabla.getSelectedRow(); if (fila < 0) return;
        txtCodigo.setText(String.valueOf(modelo.getValueAt(fila, 0)));
        txtNombre.setText(String.valueOf(modelo.getValueAt(fila, 1)));
        txtPrecio.setText(String.valueOf(modelo.getValueAt(fila, 3)));
        // Se completan los campos específicos desde el producto seleccionado mediante el controlador.
    }

    public void mostrarProductos(List<Producto> productos) {
        modelo.setRowCount(0);
        for (Producto p : productos) modelo.addRow(new Object[]{p.getCodigo(), p.getNombre(), p.getCategoria(),
            String.format(java.util.Locale.US, "%.2f", p.getPrecio()),
            String.format(java.util.Locale.US, "%.2f", p.calcularPrecioFinal()), p.getStock()});
    }
    public void cargarProductoEnFormulario(Producto p) {
        txtCodigo.setText(p.getCodigo()); txtNombre.setText(p.getNombre());
        txtPrecio.setText(String.format(java.util.Locale.US, "%.2f", p.getPrecio())); txtStock.setText(String.valueOf(p.getStock()));
        if (p instanceof ProductoAlimenticio) { cmbTipo.setSelectedIndex(0); txtExtra.setText(((ProductoAlimenticio)p).getFechaCaducidad().toString()); }
        else if (p instanceof ProductoElectronico) { cmbTipo.setSelectedIndex(1); txtExtra.setText(String.valueOf(((ProductoElectronico)p).getGarantia())); }
        else { cmbTipo.setSelectedIndex(2); txtExtra.setText(((ProductoHigiene)p).getTipoDeUso()); }
    }
    public String getCodigoSeleccionado() { int f=tabla.getSelectedRow(); return f < 0 ? "" : String.valueOf(modelo.getValueAt(f,0)); }
    public void limpiarCampos() { txtCodigo.setText(""); txtNombre.setText(""); txtPrecio.setText(""); txtStock.setText(""); txtExtra.setText(""); cmbTipo.setSelectedIndex(0); tabla.clearSelection(); txtCodigo.requestFocus(); }
    public void mostrarMensaje(String m) { Mensajes.info(this,m); }
    public void mostrarError(String m) { Mensajes.error(this,m); }
    public int getTipo() { return cmbTipo.getSelectedIndex(); }
    public String getCodigo() { return txtCodigo.getText().trim(); }
    public String getNombre() { return txtNombre.getText().trim(); }
    public String getPrecio() { return txtPrecio.getText().trim().replace(',','.'); }
    public String getStock() { return txtStock.getText().trim(); }
    public String getExtra() { return txtExtra.getText().trim(); }
    public JTable getTabla() { return tabla; }
    public JButton getBtnGuardar() { return btnGuardar; }
    public JButton getBtnActualizar() { return btnActualizar; }
    public JButton getBtnEliminar() { return btnEliminar; }
    public JButton getBtnLimpiar() { return btnLimpiar; }
}
