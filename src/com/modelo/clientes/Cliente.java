package com.modelo.clientes;

import com.modelo.productos.Producto;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Cliente {
    // Atributos
    protected String nombre;
    protected String dni;
    // Variable estática
    protected static int totalClientes = 0;
    protected ArrayList<Producto> compras;

    public Cliente(String nombre, String dni) {
        this.nombre = nombre;
        this.dni = dni;
        this.compras = new ArrayList<>();
        totalClientes++;
    }

    @Override
    public String toString() {
        return "Nombre: " + nombre + " | DNI: " + dni;
    }

    // Un cliente normal no tiene descuento; ClienteVIP lo sobrescribe
    public double aplicarDescuento(double subtotal) {
        return subtotal;
    }

    public void comprar(Producto producto) {
        compras.add(producto);
    }

    public List<Producto> getCompras() {
        return compras;
    }

    // Devuelve el historial agrupado por producto (nombre x cantidad)
    public String mostrarCompras() {
        if (compras.isEmpty()) {
            return "Compras de " + nombre + ":\n(sin compras)";
        }
        Map<String, Integer> conteo = new LinkedHashMap<>();
        for (Producto p : compras) {
            conteo.merge(p.getNombre(), 1, Integer::sum);
        }
        StringBuilder sb = new StringBuilder("Compras de " + nombre + ":\n");
        for (Map.Entry<String, Integer> e : conteo.entrySet()) {
            sb.append("- ").append(e.getKey()).append(" x").append(e.getValue()).append("\n");
        }
        return sb.toString();
    }

    public String getNombre() { return nombre; }
    public String getDni() { return dni; }
}
