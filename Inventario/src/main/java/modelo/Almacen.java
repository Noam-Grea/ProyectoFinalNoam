package modelo;

public class Almacen {
    private int id;
    private String nombre;
    private String ubicacion;
    private String idUsuario; // Propriétaire

    public Almacen(int id, String nombre, String ubicacion, String idUsuario) {
        this.id = id;
        this.nombre = nombre;
        this.ubicacion = ubicacion;
        this.idUsuario = idUsuario;
    }

    // Getters
    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public String getUbicacion() { return ubicacion; }
    public String getIdUsuario() { return idUsuario; }

    // Setters
    public void setId(int id) { this.id = id; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setUbicacion(String ubicacion) { this.ubicacion = ubicacion; }
    public void setIdUsuario(String idUsuario) { this.idUsuario = idUsuario; }

    @Override
    public String toString() {
        return nombre + " ("+id+")";
    }
}