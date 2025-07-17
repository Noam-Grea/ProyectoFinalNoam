package modelo;

public class Producto {
    private String codigo; // Clé primaire
    private String nombre;
    private String descripcion;
    private int stockMinimo;
    private int idProveedor;        // Clé étrangère
    private String idUsuario;       // Propriétaire
    private String nota;            // Observation interne

    public Producto(String codigo, String nombre, String descripcion, int stockMinimo, int idProveedor, String idUsuario, String nota) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.stockMinimo = stockMinimo;
        this.idProveedor = idProveedor;
        this.idUsuario = idUsuario;
        this.nota = nota;
    }

    // Getters
    public String getCodigo() { return codigo; }
    public String getNombre() { return nombre; }
    public String getDescripcion() { return descripcion; }
    public int getStockMinimo() { return stockMinimo; }
    public int getIdProveedor() { return idProveedor; }
    public String getIdUsuario() { return idUsuario; }
    public String getNota() { return nota; }

    // Setters
    public void setCodigo(String codigo) { this.codigo = codigo; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public void setStockMinimo(int stockMinimo) { this.stockMinimo = stockMinimo; }
    public void setIdProveedor(int idProveedor) { this.idProveedor = idProveedor; }
    public void setIdUsuario(String idUsuario) { this.idUsuario = idUsuario; }
    public void setNota(String nota) { this.nota = nota; }

    @Override
    public String toString() {
        return codigo + " - " + nombre;
    }
   
}