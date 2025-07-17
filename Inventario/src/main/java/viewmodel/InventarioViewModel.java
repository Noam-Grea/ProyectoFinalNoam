package viewmodel;

import dao.*;
import modelo.*;
import java.util.Map;
import java.util.HashMap;

import java.util.List;

public class InventarioViewModel {

    private final UsuarioDAO usuarioDAO = new UsuarioDAO();
    private final ProductoDAO productoDAO = new ProductoDAO();
    private final AlmacenDAO almacenDAO = new AlmacenDAO();
    private final ClienteDAO clienteDAO = new ClienteDAO();
    private final ProveedorDAO proveedorDAO = new ProveedorDAO();
    private final StockDAO stockDAO = new StockDAO();
    private final TransaccionDAO transaccionDAO = new TransaccionDAO();

    private Usuario usuarioActual; 

    // LOGIN
    public boolean login(String id, String contrasena) {
        boolean ok = usuarioDAO.autenticar(id, contrasena);
        if (ok) {
            usuarioActual = usuarioDAO.buscarPorId(id);
        }
        return ok;
    }

    public void logout() {
        if (usuarioActual != null) {
            usuarioActual = null;
        }
    }

    public Usuario getUsuarioActual() {
        return usuarioActual;
    }

    // PRODUCTOS
    public List<Producto> getProductos() {
        return productoDAO.listarPorUsuario(usuarioActual.getId());
    }

    public boolean agregarProducto(Producto p) {
        return productoDAO.guardar(p);
    }

    // ENTREPOTS
    public List<Almacen> getAlmacenes() {
        return almacenDAO.listarPorUsuario(usuarioActual.getId());
    }

    public boolean agregarAlmacenes(Almacen e) {
        return almacenDAO.guardar(e);
    }

    //public List<Cliente> getClientes() {
    //    return clienteDAO.listarPorUsuario();
    //}

    public boolean agregarCliente(Cliente c) {
        return clienteDAO.guardar(c);
    }

    // PROVEEDORES
    public List<Proveedor> getProveedores() {
        return proveedorDAO.listar();
    }

    public boolean agregarProveedor(Proveedor p) {
        return proveedorDAO.guardar(p);
    }

    // TRANSACCIONES
    public boolean registrarCompra(Transaccion t, int idEntrepot) {
        return transaccionDAO.registrar(t, idEntrepot);
    }

    public boolean registrarVenta(Transaccion t, int idEntrepot) {
        return transaccionDAO.registrar(t, idEntrepot);
    }

    public List<Transaccion> getTransacciones() {
        return transaccionDAO.listarPorUsuario(usuarioActual.getId());
    }

    // STOCK
    public List<Stock> getStockDeProducto(String codigoProducto) {
        return stockDAO.listarPorProducto(codigoProducto);
    }

    
    public boolean agregarUsuario(Usuario u) {
        return usuarioDAO.guardar(u);
    }
    public boolean eliminarProducto(String codigo) {
        return productoDAO.eliminar(codigo);
    }
    public Producto buscarProductoPorCodigo(String codigo) {
        return productoDAO.buscarPorCodigo(codigo);
    }

    public boolean actualizarProducto(Producto p) {
        return productoDAO.actualizar(p);
    }
    public List<Stock> getStock() {
        return stockDAO.listarPorUsuario(usuarioActual.getId());
    }

    public boolean ajusterStock(String codeProduit, int idAlmacen, int qte) {
        return stockDAO.actualizarCantidad(codeProduit, idAlmacen, qte);
    }
    public boolean modificarCantidad(String codigo, int idAlmacen, int cantidad) {
        return stockDAO.modificarCantidad(codigo, idAlmacen, cantidad);
    }
    public boolean crearStock(Stock s) {
        return stockDAO.insertar(s);
    }
    public List<Stock> getStockFiltradoPorAlmacen(int idAlmacen) {
        return stockDAO.listarPorUsuarioYAlmacen(usuarioActual.getId(), idAlmacen);
    }
    public boolean crearAlmacen(Almacen a) {
        return new AlmacenDAO().guardar(a);
    }
    public int getTotalProductos() {
    return productoDAO.listarPorUsuario(usuarioActual.getId()).size();
}

public int getTotalClientes() {
    return clienteDAO.listarPorUsuario(usuarioActual.getId()).size();
}

public int getTotalProveedores() {
    return proveedorDAO.listarPorUsuario(usuarioActual.getId()).size();
}

public int getProductosBajoStock() {
    return productoDAO.contarProductosBajoStock(usuarioActual.getId());
}

public Map<String, Integer> getStockPorAlmacen() {
    List<Stock> lista = stockDAO.listarPorUsuario(usuarioActual.getId());
    Map<String, Integer> mapa = new HashMap<>();

    for (Stock s : lista) {
        Almacen a = almacenDAO.buscarPorId(s.getIdAlmacen());
        String nombre = (a != null) ? a.getNombre() : "Almacén #" + s.getIdAlmacen();
        mapa.put(nombre, mapa.getOrDefault(nombre, 0) + s.getCantidad());
    }

    return mapa;
}

}