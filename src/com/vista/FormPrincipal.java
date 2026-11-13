package com.vista;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class FormPrincipal extends JFrame {

    private final JButton btnProductos = new JButton("Productos");
    private final JButton btnClientes = new JButton("Clientes");
    private final JButton btnEmpleados = new JButton("Empleados");
    private final JButton btnVenta = new JButton("Realizar compra");
    private final JButton btnSalir = new JButton("Salir");

    public FormPrincipal() {
        setTitle("Supermercado");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JLabel titulo = new JLabel("SUPERMERCADO", SwingConstants.CENTER);
        titulo.setFont(titulo.getFont().deriveFont(20f));

        JPanel botones = new JPanel(new GridLayout(0, 1, 8, 8));
        botones.add(btnProductos);
        botones.add(btnClientes);
        botones.add(btnEmpleados);
        botones.add(btnVenta);
        botones.add(btnSalir);

        JPanel raiz = new JPanel(new BorderLayout(10, 15));
        raiz.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        raiz.add(titulo, BorderLayout.NORTH);
        raiz.add(botones, BorderLayout.CENTER);
        setContentPane(raiz);

        setSize(320, 360);
        setLocationRelativeTo(null);
    }

    public JButton getBtnProductos() { return btnProductos; }
    public JButton getBtnClientes() { return btnClientes; }
    public JButton getBtnEmpleados() { return btnEmpleados; }
    public JButton getBtnVenta() { return btnVenta; }
    public JButton getBtnSalir() { return btnSalir; }
}
