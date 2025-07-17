package dao;

import modelo.Transaccion;
import modelo.Stock;
import util.Conexion;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TransaccionDAO {
    private final StockDAO stockDAO = new StockDAO();

    // Registrar una nueva transacción y actualizar el stock automáticamente
    public boolean registrar(Transaccion t, int idAlmacen) {
        String sql = "INSERT INTO transaccion (type, date, produit_code, quantite, utilisateur_id, client_id, fournisseur_id, entrepot_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, t.getTipo());
            ps.setTimestamp(2, Timestamp.valueOf(t.getFecha()));
            ps.setString(3, t.getCodigoProducto());
            ps.setInt(4, t.getCantidad());
            ps.setString(5, t.getIdUsuario());
            ps.setInt(8, idAlmacen);

            if (t.getIdCliente() != null) {
                ps.setInt(6, t.getIdCliente());
            } else {
                ps.setNull(6, Types.INTEGER);
            }

            if (t.getIdProveedor() != null) {
                ps.setInt(7, t.getIdProveedor());
            } else {
                ps.setNull(7, Types.INTEGER);
            }

            ps.executeUpdate();

            // 🔄 Actualización del stock
            Stock stockExistente = stockDAO.buscar(t.getCodigoProducto(), idAlmacen);
            int nuevaCantidad;

            if (stockExistente != null) {
                if (t.getTipo().equalsIgnoreCase("compra")) {
                    nuevaCantidad =  t.getCantidad();
                } else if (t.getTipo().equalsIgnoreCase("venta")) {
                    nuevaCantidad = - t.getCantidad();
                } else {
                    return false; // Tipo inválido
                }
                return stockDAO.actualizarCantidad(t.getCodigoProducto(), idAlmacen, nuevaCantidad);
            } else {
                // Si no existía, se crea el stock solo si es una compra
                if (t.getTipo().equalsIgnoreCase("compra")) {
                    return stockDAO.insertar(new Stock(t.getCodigoProducto(), idAlmacen, t.getCantidad(), t.getIdUsuario()));
                } else {
                    System.out.println("Stock inexistente para venta");
                    return false;
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al registrar transacción: " + e.getMessage());
            return false;
        }
    }

    // Listar todas las transacciones (opcional: filtrar por usuario, cliente o producto)
    public List<Transaccion> listar() {
        List<Transaccion> lista = new ArrayList<>();
        String sql = "SELECT * FROM transaccion ORDER BY date DESC";

        try (Connection con = Conexion.conectar();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Transaccion t = new Transaccion(
                    rs.getInt("id"),
                    rs.getString("type"),
                    rs.getTimestamp("date").toLocalDateTime(),
                    rs.getString("produit_code"),
                    rs.getInt("quantite"),
                    rs.getString("utilisateur_id"),
                    rs.getObject("client_id") != null ? rs.getInt("client_id") : null,
                    rs.getObject("fournisseur_id") != null ? rs.getInt("fournisseur_id") : null,
                    rs.getInt("entrepot_id")
                );
                lista.add(t);
            }

        } catch (SQLException e) {
            System.out.println("Error al listar transacciones: " + e.getMessage());
        }

        return lista;
    }
    public int obtenerIdPorFechaYProducto(String fechaStr, String codProducto) {
    String sql = "SELECT id FROM transaccion WHERE date = ? AND produit_code = ?";

    try (Connection con = Conexion.conectar();
         PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setString(1, fechaStr);
        ps.setString(2, codProducto);

        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return rs.getInt("id");
        }

    } catch (SQLException e) {
        System.out.println("Error al buscar ID de transacción: " + e.getMessage());
    }

    return -1;
}
    public boolean eliminar(int id) {
    String sql = "DELETE FROM transaccion WHERE id = ?";
    System.out.println("Eliminando transacción ID: " + id);

    try (Connection con = Conexion.conectar();
         PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setInt(1, id);
        return ps.executeUpdate() > 0;

    } catch (SQLException e) {
        System.out.println("Error al eliminar transacción: " + e.getMessage());
        return false;
    }
}
    public List<Transaccion> listarPorUsuario(String idUsuario) {
    List<Transaccion> lista = new ArrayList<>();
    String sql = "SELECT * FROM transaccion WHERE utilisateur_id = ? ORDER BY date DESC";

    try (Connection con = Conexion.conectar();
         PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setString(1, idUsuario);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            Transaccion t = new Transaccion(
                rs.getInt("id"),
                rs.getString("type"),
                rs.getTimestamp("date").toLocalDateTime(),
                rs.getString("produit_code"),
                rs.getInt("quantite"),
                rs.getString("utilisateur_id"),
                rs.getObject("client_id") != null ? rs.getInt("client_id") : null,
                rs.getObject("fournisseur_id") != null ? rs.getInt("fournisseur_id") : null,
                rs.getInt("entrepot_id")
            );
            lista.add(t);
        }

    } catch (SQLException e) {
        System.out.println("Error al listar transacciones por usuario: " + e.getMessage());
    }

    return lista;
}
}