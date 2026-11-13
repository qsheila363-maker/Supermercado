package com.vista;

import com.modelo.clientes.Cliente;
import com.modelo.clientes.ClienteVIP;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

public class FormCliente extends JFrame {

    private final JTextField txtNombre = new JTextField();
    private final JTextField txtDni = new JTextField();
    private final JCheckBox chkVip = new JCheckBox("Cliente VIP");
    private final JTextField txtDescuento = new JTextField();
    private final JButton btnGuardar = new JButton("Guardar");
    private final JButton btnLimpiar = new JButton("Limpiar");
    private final JButton btnHistorial = new JButton("Ver historial de compras");
    private final ModeloTabla modelo = new ModeloTabla("Nombre", "DNI", "Tipo", "Descuento");
    private final JTable tabla = new JTable(modelo);

    public FormCliente() {
        setTitle("Clientes");
        setDefaultCloseOperation(HIDE_ON_CLOSE);

        txtDescuento.setEnabled(false);
        chkVip.addActionListener(e -> {
            txtDescuento.setEnabled(chkVip.isSelected());
            if (!chkVip.isSelected()) {
                txtDescuento.setText("");
            }
        });

        JPanel campos = new JPanel(new GridLayout(0, 2, 6, 6));
        campos.add(new JLabel("Nombre:"));
        campos.add(txtNombre);
        campos.add(new JLabel("DNI:"));
        campos.add(txtDni);
        campos.add(chkVip);
        campos.add(new JLabel(""));
        campos.add(new JLabel("Descuento VIP (%):"));
        campos.add(txtDescuento);

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        botones.add(btnLimpiar);
        botones.add(btnGuardar);

        JPanel formulario = new JPanel(new BorderLayout(0, 8));
        formulario.setBorder(BorderFactory.createEmptyBorder(12, 12, 8, 12));
        formulario.add(campos, BorderLayout.CENTER);
        formulario.add(botones, BorderLayout.SOUTH);

        JPanel pie = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pie.add(btnHistorial);

        JPanel raiz = new JPanel(new BorderLayout());
        raiz.add(formulario, BorderLayout.NORTH);
        raiz.add(new JScrollPane(tabla), BorderLayout.CENTER);
        raiz.add(pie, BorderLayout.SOUTH);
        setContentPane(raiz);

        setSize(620, 480);
        setLocationRelativeTo(null);
    }

    public void mostrarClientes(List<Cliente> clientes) {
        modelo.setRowCount(0);
        for (Cliente c : clientes) {
            boolean vip = c instanceof ClienteVIP;
            modelo.addRow(new Object[]{
                c.getNombre(), c.getDni(),
                vip ? "VIP" : "Normal",
                vip ? ((ClienteVIP) c).getDescuentoVIP() + "%" : "-"
            });
        }
    }

    public void limpiarCampos() {
        txtNombre.setText("");
        txtDni.setText("");
        txtDescuento.setText("");
        chkVip.setSelected(false);
        txtDescuento.setEnabled(false);
        txtNombre.requestFocus();
    }

    public void mostrarMensaje(String mensaje) { Mensajes.info(this, mensaje); }
    public void mostrarError(String mensaje) { Mensajes.error(this, mensaje); }

    /** Fila seleccionada de la tabla, o -1 si no hay ninguna. */
    public int getFilaSeleccionada() { return tabla.getSelectedRow(); }

    public JTextField getTxtNombre() { return txtNombre; }
    public JTextField getTxtDni() { return txtDni; }
    public boolean isVip() { return chkVip.isSelected(); }
    public String getDescuento() { return txtDescuento.getText().trim().replace(',', '.'); }
    public JButton getBtnGuardar() { return btnGuardar; }
    public JButton getBtnLimpiar() { return btnLimpiar; }
    public JButton getBtnHistorial() { return btnHistorial; }
}
