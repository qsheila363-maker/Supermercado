package com.modelo.interfaces;

public interface Promocionable {

    default double aplicarDescuento(double precio, double porcentaje) {
        return precio - (precio * porcentaje / 100);
    }

    // tipoPromocion: 1 = 2x1, 2 = segunda unidad al 50%. Devuelve el total, no imprime.
    default double aplicarPromocion(int tipoPromocion, int cantidad, double precioU) {
        switch (tipoPromocion) {
            case 1:
                return precioU * ((cantidad + 1) / 2);
            case 2:
                int pares = cantidad / 2;
                int sobrantes = cantidad % 2;
                double precioPareja = precioU + (precioU * 0.5);
                return pares * precioPareja + sobrantes * precioU;
            default:
                return precioU * cantidad;
        }
    }
}
