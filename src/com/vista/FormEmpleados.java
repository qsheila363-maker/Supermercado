package com.vista;

import com.modelo.empleados.Cajero;
import com.modelo.empleados.Empleado;
import com.modelo.empleados.Gerente;
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

public class FormEmpleados extends JFrame {

    private final JComboBox<String> cmbTipo = new JComboBox<>(new String[]{"Cajero", "Gerente"});
    private final JTextField txtNombre = new JTextField();
    private final JTextField txtId = new JTextField();
    private final JTextField txtSalario = new JTextField();
    private final JTextField txtExtra = new JTextField();
    private final JLabel lblExtra = new JLabel();
    private final JButton btnGuardar = new JButton("Guardar");
    private final JButton btnLimpiar = new JButton("Limpiar");
    private final ModeloTabla modelo =
            new ModeloTabla("Tipo", "Nombre", "ID", "Salario base", "Ventas / Bono", "Salario final");
    private final JTable tabla = new JTable(modelo);

    public FormEmpleados() {
        setTitle("Empleados");
        setDefaultCloseOperation(HIDE_ON_CLOSE);

        JPanel campos = new JPanel(new GridLayout(0, 2, 6, 6));
        campos.add(new JLabel("Tipo:"));
        campos.add(cmbTipo);
        campos.add(new JLabel("Nombre:"));
        campos.add(txtNombre);
        campos.add(new JLabel("ID del empleado:"));
        campos.add(txtId);
        campos.add(new JLabel("Salario base:"));
        campos.add(txtSalario);
        campos.add(lblExtra);
        campos.add(txtExtra);

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        botones.add(btnLimpiar);
        botones.add(btnGuardar);

        JPanel formulario = new JPanel(new BorderLayout(0, 8));
        formulario.setBorder(BorderFactory.createEmptyBorder(12, 12, 8, 12));
        formulario.add(campos, BorderLayout.CENTER);
        formulario.add(botones, BorderLayout.SOUTH);

        JPanel raiz = new JPanel(new BorderLayout());
        raiz.add(formulario, BorderLayout.NORTH);
        raiz.add(new JScrollPane(tabla), BorderLayout.CENTER);
        setContentPane(raiz);

        cmbTipo.addActionListener(e -> actualizarEtiquetaExtra());
        actualizarEtiquetaExtra();

        setSize(720, 480);
        setLocationRelativeTo(null);
    }

    private void actualizarEtiquetaExtra() {
        lblExtra.setText(cmbTipo.getSelectedIndex() == 0 ? "Ventas realizadas:" : "Bono gerencial:");
    }

    public void mostrarEmpleados(List<Empleado> empleados) {
        modelo.setRowCount(0);
        for (Empleado e : empleados) {
            String tipo = "";
            String extra = "";
            if (e instanceof Gerente) {
                tipo = "Gerente";
                extra = String.format("%.2f", ((Gerente) e).getBonoGerencial());
            } else if (e instanceof Cajero) {
                tipo = "Cajero";
                extra = String.format("%.2f", ((Cajero) e).getVentasRealizadas());
            }
            modelo.addRow(new Object[]{
                tipo, e.getNombre(), e.getIdEmpleado(),
                String.format("%.2f", e.getSalarioBase()), extra,
                String.format("%.2f", e.calcularSalario())
            });
        }
    }

    public void limpiarCampos() {
        txtNombre.setText("");
        txtId.setText("");
        txtSalario.setText("");
        txtExtra.setText("");
        cmbTipo.setSelectedIndex(0);
        txtNombre.requestFocus();
    }

    public void mostrarMensaje(String mensaje) { Mensajes.info(this, mensaje); }
    public void mostrarError(String mensaje) { Mensajes.error(this, mensaje); }

    /** 0 = cajero, 1 = gerente. */
    public int getTipo() { return cmbTipo.getSelectedIndex(); }
    public String getNombre() { return txtNombre.getText().trim(); }
    public String getId() { return txtId.getText().trim(); }
    public String getSalario() { return txtSalario.getText().trim().replace(',', '.'); }
    public String getExtra() { return txtExtra.getText().trim().replace(',', '.'); }
    public JButton getBtnGuardar() { return btnGuardar; }
    public JButton getBtnLimpiar() { return btnLimpiar; }
}
