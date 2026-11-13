package supermercado;

import com.controlador.ControlPrincipal;
import com.modelo.Supermercado;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Supermercado supermercado = new Supermercado();
            try {
                supermercado.cargarDatosDesdeBD();
                new ControlPrincipal(supermercado).iniciar();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null,
                    "No se pudo conectar o cargar los datos de MySQL.\n"
                    + "Verificá que MariaDB/MySQL esté iniciado, que exista la base supermercado,\n"
                    + "y que el conector JDBC esté agregado al proyecto.\n\nDetalle: " + ex.getMessage(),
                    "Error de conexión", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
