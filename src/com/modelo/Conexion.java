package com.modelo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/** Administra las conexiones con la base de datos supermercado. */
public class Conexion {
    private static final String URL = "jdbc:mysql://localhost:3306/supermercado";
    private static final String USER = "root";
    private static final String PASSWORD = ""; // Cambiar si MySQL tiene contraseña.

    public Connection getConexion() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException ex) {
            throw new SQLException("No se encontró el driver JDBC de MySQL. Agregá mysql-connector-j al proyecto.", ex);
        }
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
