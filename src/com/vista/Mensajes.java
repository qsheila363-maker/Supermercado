package com.vista;

import java.awt.Component;
import javax.swing.JOptionPane;

/** Diálogos reutilizables por todas las vistas. */
public final class Mensajes {

    private Mensajes() { }

    public static void info(Component padre, String mensaje) {
        JOptionPane.showMessageDialog(padre, mensaje, "Información", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void error(Component padre, String mensaje) {
        JOptionPane.showMessageDialog(padre, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
