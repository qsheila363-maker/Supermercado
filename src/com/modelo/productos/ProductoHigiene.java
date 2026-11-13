package com.modelo.productos;

import com.modelo.interfaces.ImpuestoAplicable;
import com.modelo.interfaces.Promocionable;

public class ProductoHigiene extends Producto implements ImpuestoAplicable, Promocionable {
    private String tipoDeUso;

    public ProductoHigiene(String codigo, String nombre, double precio, String tipoDeUso, int stock) {
        super(codigo, nombre, precio, "Higiene", stock);
        this.tipoDeUso = tipoDeUso;
    }

    @Override
    public double calcularImpuesto(double porcentaje) {
        return getPrecio() * porcentaje / 100;
    }

    @Override
    public double calcularPrecioFinal() {
        return getPrecio() + calcularImpuesto(10); // impuesto de higiene: 10%
    }

    @Override
    public String mostrarInformacion() {
        return "Higiene: " + getNombre() + " | Código: " + getCodigo()
                + " | Precio final: " + calcularPrecioFinal() + " | Uso: " + tipoDeUso;
    }

    public String getTipoDeUso() { return tipoDeUso; }
    public void setTipoDeUso(String tipoDeUso) { this.tipoDeUso = tipoDeUso; }
}
