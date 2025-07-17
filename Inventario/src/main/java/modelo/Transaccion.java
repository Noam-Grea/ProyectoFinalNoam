package modelo;

import java.time.LocalDateTime;

public class Transaccion {
    private int id;
    private String tipo; // "venta" o "compra"
    private LocalDateTime fecha;
    private String codigoProducto;
    private int cantidad;
    private String idUsuario;
    private Integer idCliente;      
    private Integer idProveedor; 
    private Integer idAlmacen;
    

    public Transaccion(int id, String tipo, LocalDateTime fecha, String codigoProducto, int cantidad,
                       String idUsuario, Integer idCliente, Integer idProveedor, Integer idAlmacen) {
        this.id = id;
        this.tipo = tipo;
        this.fecha = fecha;
        this.codigoProducto = codigoProducto;
        this.cantidad = cantidad;
        this.idUsuario = idUsuario;
        this.idCliente = idCliente;
        this.idProveedor = idProveedor;
        this.idAlmacen = idAlmacen;
    }

    // Getters
    public int getId() { return id; }
    public String getTipo() { return tipo; }
    public LocalDateTime getFecha() { return fecha; }
    public String getCodigoProducto() { return codigoProducto; }
    public int getCantidad() { return cantidad; }
    public String getIdUsuario() { return idUsuario; }
    public Integer getIdCliente() { return idCliente; }
    public Integer getIdProveedor() { return idProveedor; }
    public Integer getIdAlmacen() { return idAlmacen; }

    // Setters
    public void setId(int id) { this.id = id; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }
    public void setCodigoProducto(String codigoProducto) { this.codigoProducto = codigoProducto; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }
    public void setIdUsuario(String idUsuario) { this.idUsuario = idUsuario; }
    public void setIdCliente(Integer idCliente) { this.idCliente = idCliente; }
    public void setIdProveedor(Integer idProveedor) { this.idProveedor = idProveedor; }

    @Override
    public String toString() {
        return tipo.toUpperCase() + " - " + codigoProducto + " x" + cantidad + " (" + fecha + ")";
    }
}