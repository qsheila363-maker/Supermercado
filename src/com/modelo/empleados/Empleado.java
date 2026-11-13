package com.modelo.empleados;

public abstract class Empleado {
    private String nombre;
    private String idEmpleado;
    private double salarioBase;

    public Empleado(String nombre, String idEmpleado, double salarioBase) {
        this.nombre = nombre;
        this.idEmpleado = idEmpleado;
        this.salarioBase = salarioBase;
    }

    public abstract double calcularSalario();

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getIdEmpleado() { return idEmpleado; }
    public double getSalarioBase() { return salarioBase; }
    public void setSalarioBase(double salarioBase) { this.salarioBase = salarioBase; }
}
