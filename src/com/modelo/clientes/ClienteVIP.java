package com.modelo.clientes;

public class ClienteVIP extends Cliente {

    private double descuentoVIP; // porcentaje

    public ClienteVIP(String nombre, String dni, double descuentoVIP) {
        super(nombre, dni);
        this.descuentoVIP = descuentoVIP;
    }

    @Override
    public String toString() {
        return "Nombre: " + nombre + " | DNI: " + dni + " | Descuento: " + descuentoVIP + "%";
    }

    @Override
    public double aplicarDescuento(double subtotal) {
        return aplicarDescuentoVIP(subtotal);
    }

    public double aplicarDescuentoVIP(double subtotal) {
        return subtotal - (subtotal * descuentoVIP / 100);
    }

    public double getDescuentoVIP() { return descuentoVIP; }
}
