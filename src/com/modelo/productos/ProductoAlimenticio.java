package com.modelo.productos;

import com.modelo.interfaces.Promocionable;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class ProductoAlimenticio extends Producto implements Promocionable {
    private LocalDate fechaCaducidad;
    private boolean esPerecedero;

    public ProductoAlimenticio(String codigo, String nombre, double precio,
                               LocalDate fechaCaducidad, int stock) {
        super(codigo, nombre, precio, "Alimentación", stock);
        this.fechaCaducidad = fechaCaducidad;
        this.esPerecedero = ChronoUnit.DAYS.between(LocalDate.now(), fechaCaducidad) < 30;
    }

    @Override
    public double calcularPrecioFinal() {
        long diasParaCaducar = ChronoUnit.DAYS.between(LocalDate.now(), fechaCaducidad);
        if (diasParaCaducar < 0) {
            return 0.0;
        }
        if (diasParaCaducar < 5) {
            return getPrecio() * 0.70; // 30% de descuento
        }
        return getPrecio();
    }

    // Un alimento vencido no se puede vender
    @Override
    public boolean esVendible() {
        return super.esVendible() && !estaCaducado();
    }

    @Override
    public String mostrarInformacion() {
        return "Alimenticio: " + getNombre() + " | Código: " + getCodigo()
                + " | Precio final: " + calcularPrecioFinal()
                + " | Caduca: " + fechaCaducidad + " | Perecedero: " + esPerecedero;
    }

    public boolean estaCaducado() { return fechaCaducidad.isBefore(LocalDate.now()); }
    public LocalDate getFechaCaducidad() { return fechaCaducidad; }
    public boolean isEsPerecedero() { return esPerecedero; }
}
