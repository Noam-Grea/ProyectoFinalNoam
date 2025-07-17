package modelo;

public class Usuario {
    private String id;
    private String nombre;
    private String contrasena;
    private String rol; // 'admin' o 'empleado'

    public Usuario(String id, String nombre, String contrasena, String rol) {
        this.id = id;
        this.nombre = nombre;
        this.contrasena = contrasena;
        this.rol = rol;
    }

    // Getters
    public String getId() { return id; }
    public String getNombre() { return nombre; }
    public String getContrasena() { return contrasena; }
    public String getRol() { return rol; }

    // Setters
    public void setId(String id) { this.id = id; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setContrasena(String contrasena) { this.contrasena = contrasena; }
    public void setRol(String rol) { this.rol = rol; }

    @Override
    public String toString() {
        return nombre + " (" + rol + ")";
    }
}
