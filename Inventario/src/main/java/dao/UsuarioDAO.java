package dao;

import modelo.Usuario;
import util.Conexion;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {

    // Buscar un usuario por ID
    public Usuario buscarPorId(String id) {
        String sql = "SELECT * FROM usuario WHERE id = ?";
        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Usuario(
                    rs.getString("id"),
                    rs.getString("nom"),
                    rs.getString("mot_de_passe"),
                    rs.getString("role")
                );
            }

        } catch (SQLException e) {
            System.out.println("Error al buscar usuario: " + e.getMessage());
        }
        return null;
    }

    // Autenticación: verificar si ID + contraseña coinciden
    public boolean autenticar(String id, String contrasena) {
        Usuario usuario = buscarPorId(id);
        return usuario != null && usuario.getContrasena().equals(contrasena);
    }

    // Insertar nuevo usuario
    public boolean guardar(Usuario usuario) {
        String sql = "INSERT INTO usuario (id, nom, mot_de_passe, role) VALUES (?, ?, ?, ?)";
        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, usuario.getId());
            ps.setString(2, usuario.getNombre());
            ps.setString(3, usuario.getContrasena());
            ps.setString(4, usuario.getRol());

            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("Error al guardar usuario: " + e.getMessage());
            return false;
        }
    }

    // Listar todos los usuarios
    public List<Usuario> listar() {
        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT * FROM usuario";

        try (Connection con = Conexion.conectar();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Usuario u = new Usuario(
                    rs.getString("id"),
                    rs.getString("nom"),
                    rs.getString("mot_de_passe"),
                    rs.getString("role")
                );
                lista.add(u);
            }

        } catch (SQLException e) {
            System.out.println("Error al listar usuarios: " + e.getMessage());
        }

        return lista;
    }
}