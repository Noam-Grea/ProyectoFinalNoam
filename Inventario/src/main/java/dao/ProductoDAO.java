package dao;

import modelo.Producto;
import util.Conexion;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductoDAO {

    // Insertar un nuevo producto
    public boolean guardar(Producto p) {
        String sql = "INSERT INTO producto (code, nom, description, seuil_min, fournisseur_id, usuario_id, note) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, p.getCodigo());
            ps.setString(2, p.getNombre());
            ps.setString(3, p.getDescripcion());
            ps.setInt(4, p.getStockMinimo());
            ps.setInt(5, p.getIdProveedor());
            ps.setString(6, p.getIdUsuario());
            ps.setString(7, p.getNota());

            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("Error al guardar producto: " + e.getMessage());
            return false;
        }
    }

    // Buscar producto por código
    public Producto buscarPorCodigo(String codigo) {
        String sql = "SELECT * FROM producto WHERE code = ?";
        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, codigo);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Producto(
                    rs.getString("code"),
                    rs.getString("nom"),
                    rs.getString("description"),
                    rs.getInt("seuil_min"),
                    rs.getInt("fournisseur_id"),
                    rs.getString("usuario_id"),
                    rs.getString("note")
                );
            }

        } catch (SQLException e) {
            System.out.println("Error al buscar producto: " + e.getMessage());
        }

        return null;
    }

    // Listar todos los productos (optionnellement filtrés par usuario_id)
    public List<Producto> listarPorUsuario(String idUsuario) {
        List<Producto> lista = new ArrayList<>();
        String sql = "SELECT * FROM producto WHERE usuario_id = ?";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, idUsuario);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Producto p = new Producto(
                    rs.getString("code"),
                    rs.getString("nom"),
                    rs.getString("description"),
                    rs.getInt("seuil_min"),
                    rs.getInt("fournisseur_id"),
                    rs.getString("usuario_id"),
                    rs.getString("note")
                );
                lista.add(p);
            }

        } catch (SQLException e) {
            System.out.println("Error al listar productos: " + e.getMessage());
        }

        return lista;
    }

    // Eliminar un producto
    public boolean eliminar(String codigo) {
        String sql = "DELETE FROM producto WHERE code = ?";
        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, codigo);
            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("Error al eliminar producto: " + e.getMessage());
            return false;
        }
    }
    
    public boolean actualizar(Producto p) {
        String sql = "UPDATE producto SET nom = ?, description = ?, seuil_min = ?, fournisseur_id = ?, note = ? WHERE code = ?";

        try (Connection con = Conexion.conectar();
            PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, p.getNombre());
            ps.setString(2, p.getDescripcion());
            ps.setInt(3, p.getStockMinimo());
            ps.setInt(4, p.getIdProveedor());
            ps.setString(5, p.getNota());
            ps.setString(6, p.getCodigo());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al actualizar producto: " + e.getMessage());
            e.printStackTrace(); // Ajoute ceci
            return false;
        }
    }
    public List<Producto> listarPorProveedor(int idProveedor) {
    List<Producto> lista = new ArrayList<>();
    String sql = "SELECT * FROM producto WHERE fournisseur_id = ?";

    try (Connection con = Conexion.conectar();
         PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setInt(1, idProveedor);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            Producto p = new Producto(
                rs.getString("code"),
                rs.getString("nom"),
                rs.getString("description"),
                rs.getInt("seuil_min"),
                rs.getInt("fournisseur_id"),
                rs.getString("utilisateur_id"),
                rs.getString("note")
            );
            lista.add(p);
        }

    } catch (SQLException e) {
        System.out.println("Error al listar productos por proveedor: " + e.getMessage());
    }

    return lista;
}
    public int contarProductosBajoStock(String idUsuario) {
    String sql = """
        SELECT COUNT(DISTINCT p.code)
        FROM producto p
        LEFT JOIN stock s ON s.produit_code = p.code AND s.utilisateur_id = p.usuario_id
        WHERE p.usuario_id = ?
        GROUP BY p.code, p.seuil_min
        HAVING COALESCE(SUM(s.quantite), 0) < p.seuil_min
    """;

    try (Connection con = Conexion.conectar();
         PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setString(1, idUsuario);
        ResultSet rs = ps.executeQuery();

        int count = 0;
        while (rs.next()) count++;

        return count;

    } catch (SQLException e) {
        System.out.println("Erreur dans contarProductosBajoStock: " + e.getMessage());
        return 0;
    }
}
}