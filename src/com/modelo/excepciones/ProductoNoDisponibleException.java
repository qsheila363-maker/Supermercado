package com.modelo.excepciones;

public class ProductoNoDisponibleException extends Exception {
    public ProductoNoDisponibleException(String mensaje) {
        super(mensaje);
    }
}
