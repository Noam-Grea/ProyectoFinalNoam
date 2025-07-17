package dao;

import modelo.Almacen;
import util.Conexion;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AlmacenDAO {

    // Guardar un nuevo entrepôt
    public boolean guardar(Almacen e) {
        String sql = "INSERT INTO almacen (nom, localisation, utilisateur_id) VALUES (?, ?, ?)";
        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, e.getNombre());
            ps.setString(2, e.getUbicacion());
            ps.setString(3, e.getIdUsuario());

            ps.executeUpdate();
            return true;

        } catch (SQLException ex) {
            System.out.println("Error al guardar almacen: " + ex.getMessage());
            return false;
        }
    }

    // Buscar un entrepôt por ID
    public Almacen buscarPorId(int id) {
        String sql = "SELECT * FROM almacen WHERE id = ?";
        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Almacen(
                    rs.getInt("id"),
                    rs.getString("nom"),
                    rs.getString("localisation"),
                    rs.getString("utilisateur_id")
                );
            }

        } catch (SQLException e) {
            System.out.println("Error al buscar almacen: " + e.getMessage());
        }
        return null;
    }

    // Listar entrepôts por usuario
    public List<Almacen> listarPorUsuario(String idUsuario) {
        List<Almacen> lista = new ArrayList<>();
        String sql = "SELECT * FROM almacen WHERE utilisateur_id = ?";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, idUsuario);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Almacen e = new Almacen(
                    rs.getInt("id"),
                    rs.getString("nom"),
                    rs.getString("localisation"),
                    rs.getString("utilisateur_id")
                );
                lista.add(e);
            }

        } catch (SQLException e) {
            System.out.println("Error al listar entrepôts: " + e.getMessage());
        }

        return lista;
    }

    // Eliminar un entrepôt
    public boolean eliminar(int id) {
        String sql = "DELETE FROM almacen WHERE id = ?";
        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("Error al eliminar entrepôt: " + e.getMessage());
            return false;
        }
    }
    
}