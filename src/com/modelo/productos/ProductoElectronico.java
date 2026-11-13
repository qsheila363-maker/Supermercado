package com.modelo.productos;

public class ProductoElectronico extends Producto {
    private int garantia; // años

    public ProductoElectronico(String codigo, String nombre, double precio, int garantia, int stock) {
        super(codigo, nombre, precio, "Electrónica", stock);
        this.garantia = garantia;
    }

    @Override
    public double calcularPrecioFinal() {
        if (garantia > 2) {
            return getPrecio() * 1.05; // 5% extra por garantía extendida
        }
        return getPrecio();
    }

    @Override
    public String mostrarInformacion() {
        return "Electrónico: " + getNombre() + " | Código: " + getCodigo()
                + " | Precio final: " + calcularPrecioFinal()
                + " | Garantía: " + garantia + " años";
    }

    public int getGarantia() { return garantia; }
    public void setGarantia(int garantia) { this.garantia = garantia; }
}
