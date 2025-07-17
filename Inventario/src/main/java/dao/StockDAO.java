package dao;

import modelo.Stock;
import util.Conexion;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StockDAO {

    // Rechercher une ligne de stock par produit + entrepôt
    public Stock buscar(String codigoProducto, int idEntrepot) {
        String sql = "SELECT * FROM stock WHERE produit_code = ? AND entrepot_id = ?";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, codigoProducto);
            ps.setInt(2, idEntrepot);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Stock(
                    rs.getString("produit_code"),
                    rs.getInt("entrepot_id"),
                    rs.getInt("quantite"),
                    rs.getString("utilisateur_id")
                );
            }

        } catch (SQLException e) {
            System.out.println("Error al buscar stock: " + e.getMessage());
        }
        return null;
    }

    // Modifier la quantité existante (remplacement)
   public boolean modificarCantidad(String codigo, int idAlmacen, int cantidad) {
    if (cantidad == 0) {
        // Supprimer la ligne si quantité = 0
        String sqlDelete = "DELETE FROM stock WHERE produit_code = ? AND entrepot_id = ?";
        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sqlDelete)) {

            ps.setString(1, codigo);
            ps.setInt(2, idAlmacen);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Erreur lors de la suppression du stock : " + e.getMessage());
            return false;
        }

    } else {
        // Mettre à jour la quantité sinon
        String sqlUpdate = "UPDATE stock SET quantite = ? WHERE produit_code = ? AND entrepot_id = ?";
        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sqlUpdate)) {

            ps.setInt(1, cantidad);
            ps.setString(2, codigo);
            ps.setInt(3, idAlmacen);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Erreur lors de la modification de la quantité : " + e.getMessage());
            return false;
        }
    }
}

    // Ajouter une nouvelle ligne de stock
    public boolean insertar(Stock s) {
        String sql = "INSERT INTO stock (produit_code, entrepot_id, quantite, utilisateur_id) VALUES (?, ?, ?, ?)";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, s.getCodigoProducto());
            ps.setInt(2, s.getIdAlmacen());
            ps.setInt(3, s.getCantidad());
            ps.setString(4, s.getIdUsuario());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al insertar stock: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Lister le stock d’un produit dans tous les entrepôts
    public List<Stock> listarPorProducto(String codigoProducto) {
        List<Stock> lista = new ArrayList<>();
        String sql = "SELECT * FROM stock WHERE produit_code = ?";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, codigoProducto);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Stock s = new Stock(
                    rs.getString("produit_code"),
                    rs.getInt("entrepot_id"),
                    rs.getInt("quantite"),
                    rs.getString("utilisateur_id")
                );
                lista.add(s);
            }

        } catch (SQLException e) {
            System.out.println("Error al listar stock por producto: " + e.getMessage());
        }

        return lista;
    }

    // Lister tout le stock pour un utilisateur donné
    public List<Stock> listarPorUsuario(String idUsuario) {
        List<Stock> lista = new ArrayList<>();
        String sql = "SELECT * FROM stock WHERE utilisateur_id = ?";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, idUsuario);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Stock s = new Stock(
                    rs.getString("produit_code"),
                    rs.getInt("entrepot_id"),
                    rs.getInt("quantite"),
                    rs.getString("utilisateur_id")
                );
                lista.add(s);
            }

        } catch (SQLException e) {
            System.out.println("Erreur lors de la lecture du stock : " + e.getMessage());
        }

        return lista;
    }

    // Mettre à jour la quantité de stock (ex: ajout ou retrait relatif)
    public boolean actualizarCantidad(String codigoProducto, int idEntrepot, int nuevaCantidad) {
        String sql = "UPDATE stock SET quantite = quantite + ? WHERE produit_code = ? AND entrepot_id = ?";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, nuevaCantidad);
            ps.setString(2, codigoProducto);
            ps.setInt(3, idEntrepot);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al actualizar cantidad: " + e.getMessage());
            return false;
        }
    }
    public List<Stock> listarPorUsuarioYAlmacen(String idUsuario, int idAlmacen) {
    List<Stock> lista = new ArrayList<>();
    String sql = "SELECT * FROM stock WHERE utilisateur_id = ? AND entrepot_id = ?";

    try (Connection con = Conexion.conectar();
         PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setString(1, idUsuario);
        ps.setInt(2, idAlmacen);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            Stock s = new Stock(
                rs.getString("produit_code"),
                rs.getInt("entrepot_id"),
                rs.getInt("quantite"),
                rs.getString("utilisateur_id")
            );
            lista.add(s);
        }

    } catch (SQLException e) {
        System.out.println("Erreur lors du filtrage par entrepôt : " + e.getMessage());
    }

    return lista;
}
    public List<Stock> listarPorAlmacen(int idAlmacen) {
    List<Stock> lista = new ArrayList<>();
    String sql = "SELECT * FROM stock WHERE entrepot_id = ?";

    try (Connection con = Conexion.conectar();
         PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setInt(1, idAlmacen);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            Stock s = new Stock(
                rs.getString("produit_code"),
                rs.getInt("entrepot_id"),
                rs.getInt("quantite"),
                rs.getString("utilisateur_id")
            );
            lista.add(s);
        }

    } catch (SQLException e) {
        System.out.println("Error al listar stock por almacén: " + e.getMessage());
    }

    return lista;
}
}