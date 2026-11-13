package com.modelo;

import com.modelo.clientes.Cliente;
import com.modelo.clientes.ClienteVIP;
import com.modelo.empleados.Cajero;
import com.modelo.empleados.Empleado;
import com.modelo.empleados.Gerente;
import com.modelo.excepciones.ClienteNoRegistradoException;
import com.modelo.excepciones.ProductoNoDisponibleException;
import com.modelo.interfaces.Promocionable;
import com.modelo.productos.Producto;
import com.modelo.productos.ProductoAlimenticio;
import com.modelo.productos.ProductoElectronico;
import com.modelo.productos.ProductoHigiene;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** Modelo del supermercado y acceso a persistencia SQL. */
public class Supermercado {
    private final ArrayList<Producto> productosDisponibles = new ArrayList<>();
    private final ArrayList<Cliente> clientes = new ArrayList<>();
    private final ArrayList<Empleado> empleados = new ArrayList<>();
    private final Conexion conexion = new Conexion();

    /** Carga en memoria los registros existentes en MySQL. */
    public void cargarDatosDesdeBD() throws SQLException {
        productosDisponibles.clear();
        clientes.clear();
        empleados.clear();
        try (Connection con = conexion.getConexion()) {
            try (Statement st = con.createStatement(); ResultSet rs = st.executeQuery("SELECT * FROM cliente")) {
                while (rs.next()) {
                    String tipo = rs.getString("tipo");
                    Cliente c = "VIP".equalsIgnoreCase(tipo)
                            ? new ClienteVIP(rs.getString("nombre"), rs.getString("dni"), rs.getDouble("descuento_vip"))
                            : new Cliente(rs.getString("nombre"), rs.getString("dni"));
                    clientes.add(c);
                }
            }
            try (Statement st = con.createStatement(); ResultSet rs = st.executeQuery("SELECT * FROM producto")) {
                while (rs.next()) {
                    String tipo = rs.getString("tipo");
                    String codigo = rs.getString("codigo");
                    String nombre = rs.getString("nombre");
                    double precio = rs.getDouble("precio");
                    int stock = rs.getInt("stock");
                    Producto p;
                    if ("ALIMENTICIO".equalsIgnoreCase(tipo)) {
                        Date fecha = rs.getDate("fecha_caducidad");
                        if (fecha == null) continue;
                        p = new ProductoAlimenticio(codigo, nombre, precio, fecha.toLocalDate(), stock);
                    } else if ("ELECTRONICO".equalsIgnoreCase(tipo)) {
                        p = new ProductoElectronico(codigo, nombre, precio, rs.getInt("garantia"), stock);
                    } else {
                        p = new ProductoHigiene(codigo, nombre, precio, rs.getString("tipo_uso"), stock);
                    }
                    productosDisponibles.add(p);
                }
            }
            try (Statement st = con.createStatement(); ResultSet rs = st.executeQuery("SELECT * FROM empleado")) {
                while (rs.next()) {
                    String tipo = rs.getString("tipo");
                    String nombre = rs.getString("nombre");
                    String id = rs.getString("id_empleado");
                    double salario = rs.getDouble("salario_base");
                    if ("GERENTE".equalsIgnoreCase(tipo)) {
                        empleados.add(new Gerente(nombre, id, salario, rs.getDouble("bono_gerencial")));
                    } else {
                        empleados.add(new Cajero(nombre, id, salario, rs.getDouble("ventas_realizadas")));
                    }
                }
            }
        }
    }

    public void guardarClienteBD(Cliente cliente) throws SQLException {
        String sql = "INSERT INTO cliente (dni,nombre,tipo,descuento_vip) VALUES (?,?,?,?)";
        try (Connection con = conexion.getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, cliente.getDni());
            ps.setString(2, cliente.getNombre());
            if (cliente instanceof ClienteVIP) {
                ps.setString(3, "VIP");
                ps.setDouble(4, ((ClienteVIP) cliente).getDescuentoVIP());
            } else {
                ps.setString(3, "NORMAL");
                ps.setNull(4, Types.DECIMAL);
            }
            ps.executeUpdate();
        }
    }

    public void guardarProductoBD(Producto p) throws SQLException {
        String sql = "INSERT INTO producto (codigo,nombre,precio,categoria,stock,tipo,fecha_caducidad,es_perecedero,garantia,tipo_uso) VALUES (?,?,?,?,?,?,?,?,?,?)";
        try (Connection con = conexion.getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, p.getCodigo()); ps.setString(2, p.getNombre()); ps.setDouble(3, p.getPrecio());
            ps.setString(4, p.getCategoria()); ps.setInt(5, p.getStock());
            if (p instanceof ProductoAlimenticio) {
                ProductoAlimenticio a = (ProductoAlimenticio) p;
                ps.setString(6, "ALIMENTICIO"); ps.setDate(7, Date.valueOf(a.getFechaCaducidad()));
                ps.setBoolean(8, a.isEsPerecedero()); ps.setNull(9, Types.INTEGER); ps.setNull(10, Types.VARCHAR);
            } else if (p instanceof ProductoElectronico) {
                ps.setString(6, "ELECTRONICO"); ps.setNull(7, Types.DATE); ps.setNull(8, Types.BOOLEAN);
                ps.setInt(9, ((ProductoElectronico) p).getGarantia()); ps.setNull(10, Types.VARCHAR);
            } else {
                ps.setString(6, "HIGIENE"); ps.setNull(7, Types.DATE); ps.setNull(8, Types.BOOLEAN);
                ps.setNull(9, Types.INTEGER); ps.setString(10, ((ProductoHigiene) p).getTipoDeUso());
            }
            ps.executeUpdate();
        }
    }

    public void guardarEmpleadoBD(Empleado e) throws SQLException {
        String sql = "INSERT INTO empleado (id_empleado,nombre,salario_base,tipo,ventas_realizadas,bono_gerencial) VALUES (?,?,?,?,?,?)";
        try (Connection con = conexion.getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, e.getIdEmpleado()); ps.setString(2, e.getNombre()); ps.setDouble(3, e.getSalarioBase());
            if (e instanceof Cajero) {
                ps.setString(4, "CAJERO"); ps.setDouble(5, ((Cajero) e).getVentasRealizadas()); ps.setNull(6, Types.DECIMAL);
            } else {
                ps.setString(4, "GERENTE"); ps.setNull(5, Types.DECIMAL); ps.setDouble(6, ((Gerente) e).getBonoGerencial());
            }
            ps.executeUpdate();
        }
    }

    /** Una línea que se persiste como parte de una compra. */
    public static class LineaVenta {
        public final Producto producto; public final int cantidad; public final int promocion;
        public final double precioUnitario; public final double subtotal;
        public LineaVenta(Producto producto, int cantidad, int promocion, double precioUnitario, double subtotal) {
            this.producto = producto; this.cantidad = cantidad; this.promocion = promocion;
            this.precioUnitario = precioUnitario; this.subtotal = subtotal;
        }
    }

    /** Guarda compra, detalles y stock en una sola transacción. */
    public void guardarCompraBD(Cliente cliente, List<LineaVenta> lineas, double total) throws SQLException {
        String compraSql = "INSERT INTO compra (dni_cliente,descuento_vip,total) VALUES (?,?,?)";
        String detalleSql = "INSERT INTO detalle_compra (id_compra,codigo_producto,cantidad,precio_unitario,promocion,subtotal) VALUES (?,?,?,?,?,?)";
        String stockSql = "UPDATE producto SET stock = stock - ? WHERE codigo = ? AND stock >= ?";
        try (Connection con = conexion.getConexion()) {
            con.setAutoCommit(false);
            try {
                int idCompra;
                try (PreparedStatement ps = con.prepareStatement(compraSql, Statement.RETURN_GENERATED_KEYS)) {
                    ps.setString(1, cliente.getDni());
                    if (cliente instanceof ClienteVIP) ps.setDouble(2, ((ClienteVIP) cliente).getDescuentoVIP());
                    else ps.setNull(2, Types.DECIMAL);
                    ps.setDouble(3, total); ps.executeUpdate();
                    try (ResultSet keys = ps.getGeneratedKeys()) {
                        if (!keys.next()) throw new SQLException("No se pudo obtener el número de compra.");
                        idCompra = keys.getInt(1);
                    }
                }
                try (PreparedStatement detalle = con.prepareStatement(detalleSql);
                     PreparedStatement stock = con.prepareStatement(stockSql)) {
                    for (LineaVenta l : lineas) {
                        detalle.setInt(1, idCompra); detalle.setString(2, l.producto.getCodigo());
                        detalle.setInt(3, l.cantidad); detalle.setDouble(4, l.precioUnitario);
                        detalle.setInt(5, l.promocion); detalle.setDouble(6, l.subtotal); detalle.executeUpdate();
                        stock.setInt(1, l.cantidad); stock.setString(2, l.producto.getCodigo()); stock.setInt(3, l.cantidad);
                        if (stock.executeUpdate() != 1) throw new SQLException("Stock insuficiente para el producto " + l.producto.getCodigo());
                    }
                }
                con.commit();
            } catch (SQLException ex) {
                con.rollback(); throw ex;
            } finally { con.setAutoCommit(true); }
        }
    }

    /** Comprueba si un código ya está registrado en la lista cargada desde BD. */
    public boolean existeProducto(String codigo) {
        for (Producto p : productosDisponibles) if (p.getCodigo().equalsIgnoreCase(codigo)) return true;
        return false;
    }

    /** Suma unidades al stock en MySQL y actualiza el objeto en memoria. */
    public void sumarStockBD(String codigo, int cantidad) throws SQLException {
        if (cantidad <= 0) throw new IllegalArgumentException("La cantidad a sumar debe ser mayor que cero.");
        String sql = "UPDATE producto SET stock = stock + ? WHERE codigo = ?";
        try (Connection con = conexion.getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, cantidad); ps.setString(2, codigo);
            if (ps.executeUpdate() != 1) throw new SQLException("No se encontró el producto " + codigo + " en MySQL.");
        }
        for (Producto p : productosDisponibles) {
            if (p.getCodigo().equalsIgnoreCase(codigo)) { p.setStock(p.getStock() + cantidad); break; }
        }
    }

    /** Actualiza los datos editables del producto conservando su código. */
    public void actualizarProductoBD(Producto p) throws SQLException {
        String sql = "UPDATE producto SET nombre=?, precio=?, categoria=?, stock=?, tipo=?, fecha_caducidad=?, es_perecedero=?, garantia=?, tipo_uso=? WHERE codigo=?";
        try (Connection con = conexion.getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, p.getNombre()); ps.setDouble(2, p.getPrecio()); ps.setString(3, p.getCategoria());
            ps.setInt(4, p.getStock());
            if (p instanceof ProductoAlimenticio) {
                ProductoAlimenticio a = (ProductoAlimenticio) p;
                ps.setString(5, "ALIMENTICIO"); ps.setDate(6, Date.valueOf(a.getFechaCaducidad()));
                ps.setBoolean(7, a.isEsPerecedero()); ps.setNull(8, Types.INTEGER); ps.setNull(9, Types.VARCHAR);
            } else if (p instanceof ProductoElectronico) {
                ps.setString(5, "ELECTRONICO"); ps.setNull(6, Types.DATE); ps.setNull(7, Types.BOOLEAN);
                ps.setInt(8, ((ProductoElectronico) p).getGarantia()); ps.setNull(9, Types.VARCHAR);
            } else {
                ps.setString(5, "HIGIENE"); ps.setNull(6, Types.DATE); ps.setNull(7, Types.BOOLEAN);
                ps.setNull(8, Types.INTEGER); ps.setString(9, ((ProductoHigiene) p).getTipoDeUso());
            }
            ps.setString(10, p.getCodigo());
            if (ps.executeUpdate() != 1) throw new SQLException("No se encontró el producto " + p.getCodigo() + " para actualizar.");
        }
        for (int i = 0; i < productosDisponibles.size(); i++) {
            if (productosDisponibles.get(i).getCodigo().equalsIgnoreCase(p.getCodigo())) {
                productosDisponibles.set(i, p); return;
            }
        }
    }

    /** Elimina de MySQL y de memoria. La FK impide borrar productos con ventas asociadas. */
    public void eliminarProductoBD(String codigo) throws SQLException {
        String sql = "DELETE FROM producto WHERE codigo=?";
        try (Connection con = conexion.getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, codigo);
            if (ps.executeUpdate() != 1) throw new SQLException("No se encontró el producto " + codigo + ".");
        }
        productosDisponibles.removeIf(p -> p.getCodigo().equalsIgnoreCase(codigo));
    }

    public void agregarProducto(Producto producto) {
        for (Producto p : productosDisponibles) if (p.getCodigo().equals(producto.getCodigo())) throw new IllegalArgumentException("Ya existe un producto con el código " + producto.getCodigo() + ".");
        productosDisponibles.add(producto);
    }
    public void agregarCliente(Cliente cliente) {
        for (Cliente c : clientes) if (c.getDni().equals(cliente.getDni())) throw new IllegalArgumentException("Ya existe un cliente con el DNI " + cliente.getDni() + ".");
        clientes.add(cliente);
    }
    public void agregarEmpleado(Empleado empleado) {
        for (Empleado e : empleados) if (e.getIdEmpleado().equals(empleado.getIdEmpleado())) throw new IllegalArgumentException("Ya existe un empleado con el ID " + empleado.getIdEmpleado() + ".");
        empleados.add(empleado);
    }
    public Producto buscarProducto(String codigo) throws ProductoNoDisponibleException {
        for (Producto p : productosDisponibles) if (p.getCodigo().equals(codigo)) return p;
        throw new ProductoNoDisponibleException("El producto con código " + codigo + " no está disponible.");
    }
    public Cliente buscarCliente(String dni) throws ClienteNoRegistradoException {
        for (Cliente c : clientes) if (c.getDni().equals(dni)) return c;
        throw new ClienteNoRegistradoException("El cliente con DNI " + dni + " no está registrado.");
    }
    public double registrarCompra(Cliente cliente, Producto producto, int cantidad, int promocion)
            throws ClienteNoRegistradoException, ProductoNoDisponibleException {
        if (!clientes.contains(cliente)) throw new ClienteNoRegistradoException("El cliente " + cliente.getNombre() + " no está registrado.");
        if (!productosDisponibles.contains(producto)) throw new ProductoNoDisponibleException("El producto " + producto.getNombre() + " no está disponible.");
        if (!producto.esVendible()) throw new ProductoNoDisponibleException("El producto " + producto.getNombre() + " no se puede vender (sin stock o vencido).");
        if (cantidad <= 0 || cantidad > producto.getStock()) throw new IllegalArgumentException("La cantidad debe ser positiva y no superar el stock disponible.");
        double precioUnitario = producto.calcularPrecioFinal();
        double subtotal = precioUnitario * cantidad;
        if ((promocion == 1 || promocion == 2) && producto instanceof Promocionable) subtotal = ((Promocionable) producto).aplicarPromocion(promocion, cantidad, precioUnitario);
        subtotal = cliente.aplicarDescuento(subtotal);
        producto.descontarStock(cantidad);
        for (int i = 0; i < cantidad; i++) cliente.comprar(producto);
        return subtotal;
    }
    public List<Producto> getProductos() { return Collections.unmodifiableList(productosDisponibles); }
    public List<Cliente> getClientes() { return Collections.unmodifiableList(clientes); }
    public List<Empleado> getEmpleados() { return Collections.unmodifiableList(empleados); }
}
