package modelo;

public class Cliente {
    private int id;
    private String nombre;
    private String telefono;
    private String correo;
    private String direccion;
    private String idUsuario;

    public Cliente(int id, String nombre, String telefono, String correo, String direccion, String idUsuario) {
        this.id = id;
        this.nombre = nombre;
        this.telefono = telefono;
        this.correo = correo;
        this.direccion = direccion;
        this.idUsuario = idUsuario;
    }

    // Getters
    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public String getTelefono() { return telefono; }
    public String getCorreo() { return correo; }
    public String getDireccion() { return direccion; }
    public String getIdUsuario() { return idUsuario; }

    // Setters
    public void setId(int id) { this.id = id; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public void setCorreo(String correo) { this.correo = correo; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    @Override
    public String toString() {
        return nombre + " ("+id+")";
    }
}