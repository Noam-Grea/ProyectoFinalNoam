package dao;

import modelo.Proveedor;
import util.Conexion;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProveedorDAO {

    // Guardar un nuevo proveedor
    public boolean guardar(Proveedor p) {
        String sql = "INSERT INTO proveedor (nom, telephone, email, adresse, utilisateur_id) VALUES (?, ?, ?, ?, ?)";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, p.getNombre());
            ps.setString(2, p.getTelefono());
            ps.setString(3, p.getCorreo());
            ps.setString(4, p.getDireccion());
            ps.setString(5, p.getidUsuario());

            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("Error al guardar proveedor: " + e.getMessage());
            return false;
        }
    }

    // Buscar proveedor por ID
    public Proveedor buscarPorId(int id) {
        String sql = "SELECT * FROM proveedor WHERE id = ?";
        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Proveedor(
                    rs.getInt("id"),
                    rs.getString("nom"),
                    rs.getString("telephone"),
                    rs.getString("email"),
                    rs.getString("adresse"),
                    rs.getString("utilisateur_id")
                );
            }

        } catch (SQLException e) {
            System.out.println("Error al buscar proveedor: " + e.getMessage());
        }
        return null;
    }

    // Listar todos los proveedores
    public List<Proveedor> listar() {
        List<Proveedor> lista = new ArrayList<>();
        String sql = "SELECT * FROM proveedor";

        try (Connection con = Conexion.conectar();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Proveedor p = new Proveedor(
                    rs.getInt("id"),
                    rs.getString("nom"),
                    rs.getString("telephone"),
                    rs.getString("email"),
                    rs.getString("adresse"),
                    rs.getString("utilisateur_id")
                );
                lista.add(p);
            }

        } catch (SQLException e) {
            System.out.println("Error al listar proveedores: " + e.getMessage());
        }

        return lista;
    }

    // Eliminar proveedor por ID
    public boolean eliminar(int id) {
    String verificar = "SELECT COUNT(*) FROM producto WHERE fournisseur_id = ?";
    String eliminar = "DELETE FROM proveedor WHERE id = ?";

    try (Connection con = Conexion.conectar();
         PreparedStatement psVerificar = con.prepareStatement(verificar)) {

        psVerificar.setInt(1, id);
        ResultSet rs = psVerificar.executeQuery();
        if (rs.next() && rs.getInt(1) > 0) {
            // Le fournisseur a des produits associés, on bloque la suppression
            return false;
        }

        try (PreparedStatement psEliminar = con.prepareStatement(eliminar)) {
            psEliminar.setInt(1, id);
            int filas = psEliminar.executeUpdate();
            return filas > 0;
        }

    } catch (SQLException e) {
        System.out.println("Error al eliminar proveedor: " + e.getMessage());
        return false;
    }
}
    public List<Proveedor> listarPorUsuario(String idUsuario) {
    List<Proveedor> lista = new ArrayList<>();
    String sql = "SELECT * FROM proveedor WHERE utilisateur_id = ?";

    try (Connection con = Conexion.conectar();
         PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setString(1, idUsuario);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            Proveedor p = new Proveedor(
                rs.getInt("id"),
                rs.getString("nom"),
                rs.getString("telephone"),
                rs.getString("email"),
                rs.getString("adresse"),
                rs.getString("utilisateur_id") // on le passe à idUsuario dans la classe
            );
            lista.add(p);
        }

    } catch (SQLException e) {
        System.out.println("Error al listar proveedores: " + e.getMessage());
    }

    return lista;
}
    public boolean actualizar(Proveedor p) {
    String sql = "UPDATE proveedor SET nom = ?, telephone = ?, email = ?, adresse = ? WHERE id = ?";
    try (Connection con = Conexion.conectar();
         PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setString(1, p.getNombre());
        ps.setString(2, p.getTelefono());
        ps.setString(3, p.getCorreo());
        ps.setString(4, p.getDireccion());
        ps.setInt(5, p.getId());

        return ps.executeUpdate() > 0;

    } catch (SQLException e) {
        System.out.println("Error al actualizar proveedor: " + e.getMessage());
        return false;
    }
}
}