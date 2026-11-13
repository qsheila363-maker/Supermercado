package com.modelo.productos;

public abstract class Producto {

    // Atributos
    protected String codigo;
    protected String nombre;
    protected double precio;
    protected String categoria;
    protected int stock;
    // Variable estática
    protected static int totalProductos = 0;

    public Producto(String codigo, String nombre, double precio, String categoria, int stock) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.precio = precio;
        this.categoria = categoria;
        this.stock = stock;
        totalProductos++;
    }

    // Métodos abstractos
    public abstract double calcularPrecioFinal();
    public abstract String mostrarInformacion();

    // Métodos concretos
    public boolean esVendible() {
        return stock > 0;
    }

    public void descontarStock(int cantidad) {
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor que cero.");
        }
        if (cantidad > stock) {
            throw new IllegalArgumentException("No hay suficiente stock disponible.");
        }
        stock -= cantidad;
    }

    @Override
    public String toString() {
        return "Código: " + codigo + " | Nombre: " + nombre + " | Precio: " + precio
                + " | Categoría: " + categoria + " | Stock: " + stock;
    }

    public String getCodigo() { return codigo; }
    public String getNombre() { return nombre; }
    public double getPrecio() { return precio; }
    public String getCategoria() { return categoria; }
    public int getStock() { return stock; }

    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setPrecio(double precio) { this.precio = precio; }
    public void setCategoria(String categoria) { this.categoria = categoria; }
    public void setStock(int stock) { this.stock = stock; }

    public static int getTotalProductos() { return totalProductos; }
}
