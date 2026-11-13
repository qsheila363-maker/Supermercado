/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.modelo.empleados;

/**
 *
 * @author mendo
 */
public class Cajero extends Empleado {
    private double ventasRealizadas;
    private static final double COMISION = 0.02; // 2% por ventas

    public Cajero(String nombre, String idEmpleado, double salarioBase, double ventasRealizadas) {
        super(nombre, idEmpleado, salarioBase);
        this.ventasRealizadas = ventasRealizadas;
    }

    @Override
    public double calcularSalario() {
        return getSalarioBase() + ventasRealizadas * COMISION;
    }

    public double getVentasRealizadas() { return ventasRealizadas; }
    public void setVentasRealizadas(double ventasRealizadas) { this.ventasRealizadas = ventasRealizadas; }
}

