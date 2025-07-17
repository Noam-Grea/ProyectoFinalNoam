package modelo;
import dao.*;

public class Stock {
    private String codigoProducto;
    private int idAlmacen;
    private int cantidad;
    private String idUsuario;

    public Stock(String codigoProducto, int idAlmacen, int cantidad, String idUsuario) {
        this.codigoProducto = codigoProducto;
        this.idAlmacen = idAlmacen;
        this.cantidad = cantidad;
        this.idUsuario = idUsuario;
    }

    // Getters
    public String getCodigoProducto() { return codigoProducto; }
    public int getIdAlmacen() { return idAlmacen; }
    public int getCantidad() { return cantidad; }
    public String getIdUsuario() {return idUsuario;}

    // Setters
    public void setCodigoProducto(String codigoProducto) { this.codigoProducto = codigoProducto; }
    public void setIdEntrepot(int idEntrepot) { this.idAlmacen = idEntrepot; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }
    public void setIdUsuario(String idUsuario) {this.idUsuario = idUsuario;}

    @Override
    public String toString() {
    ProductoDAO productoDAO = new ProductoDAO();
    Producto producto = productoDAO.buscarPorCodigo(codigoProducto);

    String nombre = (producto != null) ? producto.getNombre() : "Sin nombre";

    return nombre + " (" + codigoProducto + ") - Cantidad: " + cantidad;
}
}