package vista;

import dao.ProductoDAO;
import dao.ProveedorDAO;
import modelo.Producto;
import modelo.Proveedor;
import viewmodel.InventarioViewModel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PanelProductos extends JPanel {

    private final InventarioViewModel viewModel;
    private final PanelStock panelStock;
    private final JTable tabla;
    private final DefaultTableModel modeloTabla;

    public PanelProductos(InventarioViewModel viewModel, PanelStock panelStock) {
        this.viewModel = viewModel;
        this.panelStock = panelStock;
        setLayout(new BorderLayout());

        JLabel titulo = new JLabel("Gestión de Productos", JLabel.CENTER);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 20));
        add(titulo, BorderLayout.NORTH);

        modeloTabla = new DefaultTableModel(new String[]{"Código", "Nombre", "Descripción", "Stock mínimo", "Proveedor", "Nota"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // ← rendre le tableau non modifiable
            }
        };

        tabla = new JTable(modeloTabla);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        JPanel botones = new JPanel();
        JButton btnNuevo = new JButton("Nuevo producto");
        JButton btnModificar = new JButton("Modificar producto");
        JButton btnEliminar = new JButton("Eliminar");
        botones.add(btnNuevo);
        botones.add(btnModificar);
        botones.add(btnEliminar);
        add(botones, BorderLayout.SOUTH);

        btnNuevo.addActionListener(e -> crearProducto());
        btnModificar.addActionListener(e -> modificarProducto());
        btnEliminar.addActionListener(e -> eliminarProductoSeleccionado());

        cargarProductos();
    }

    private void cargarProductos() {
        modeloTabla.setRowCount(0);
        List<Producto> lista = new ProductoDAO().listarPorUsuario(viewModel.getUsuarioActual().getId());
        for (Producto p : lista) {
            modeloTabla.addRow(new Object[]{
                p.getCodigo(),
                p.getNombre(),
                p.getDescripcion(),
                p.getStockMinimo(),
                p.getIdProveedor(),
                p.getNota()
            });
        }
    }

    private void crearProducto() {
        JTextField campoCodigo = new JTextField();
        JTextField campoNombre = new JTextField();
        JTextField campoDescripcion = new JTextField();
        JTextField campoStockMinimo = new JTextField();
        JTextArea campoNota = new JTextArea(3, 20);
        JScrollPane scrollNota = new JScrollPane(campoNota);

        JComboBox<Proveedor> comboProveedor = new JComboBox<>();
        List<Proveedor> proveedores = new ProveedorDAO().listarPorUsuario(viewModel.getUsuarioActual().getId());
        for (Proveedor p : proveedores) {
            comboProveedor.addItem(p);
        }

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Código:"));
        panel.add(campoCodigo);
        panel.add(new JLabel("Nombre:"));
        panel.add(campoNombre);
        panel.add(new JLabel("Descripción:"));
        panel.add(campoDescripcion);
        panel.add(new JLabel("Stock mínimo:"));
        panel.add(campoStockMinimo);
        panel.add(new JLabel("Proveedor:"));
        panel.add(comboProveedor);
        panel.add(new JLabel("Nota:"));
        panel.add(scrollNota);

        int res = JOptionPane.showConfirmDialog(this, panel, "Nuevo producto", JOptionPane.OK_CANCEL_OPTION);
        if (res == JOptionPane.OK_OPTION) {
            String codigo = campoCodigo.getText().trim();
            String nombre = campoNombre.getText().trim();
            String descripcion = campoDescripcion.getText().trim();
            String stockMinimoStr = campoStockMinimo.getText().trim();
            String nota = campoNota.getText().trim();

            if (codigo.isEmpty() || nombre.isEmpty() || stockMinimoStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Todos los campos obligatorios deben estar completos.");
                return;
            }

            try {
                int stockMinimo = Integer.parseInt(stockMinimoStr);
                Proveedor proveedorSeleccionado = (Proveedor) comboProveedor.getSelectedItem();
                int idProveedor = proveedorSeleccionado != null ? proveedorSeleccionado.getId() : -1;

                Producto nuevo = new Producto(codigo, nombre, descripcion, stockMinimo, idProveedor, viewModel.getUsuarioActual().getId(), nota);
                boolean ok = new ProductoDAO().guardar(nuevo);
                if (ok) {
                    JOptionPane.showMessageDialog(this, "Producto guardado correctamente.");
                    cargarProductos();
                    panelStock.refrescarVista();
                } else {
                    JOptionPane.showMessageDialog(this, "Error al guardar producto.");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Stock mínimo debe ser un número entero.");
            }
        }
    }

    private void modificarProducto() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Selecciona un producto para modificar.");
            return;
        }

        String codigo = (String) modeloTabla.getValueAt(fila, 0);
        Producto producto = new ProductoDAO().buscarPorCodigo(codigo);
        if (producto == null) {
            JOptionPane.showMessageDialog(this, "Producto no encontrado.");
            return;
        }

        JTextField campoNombre = new JTextField(producto.getNombre());
        JTextField campoDescripcion = new JTextField(producto.getDescripcion());
        JTextField campoStockMinimo = new JTextField(String.valueOf(producto.getStockMinimo()));
        JTextArea campoNota = new JTextArea(producto.getNota(), 3, 20);
        JScrollPane scrollNota = new JScrollPane(campoNota);

        JComboBox<Proveedor> comboProveedor = new JComboBox<>();
        List<Proveedor> proveedores = new ProveedorDAO().listarPorUsuario(viewModel.getUsuarioActual().getId());
        for (Proveedor p : proveedores) {
            comboProveedor.addItem(p);
            if (p.getId() == producto.getIdProveedor()) {
                comboProveedor.setSelectedItem(p);
            }
        }

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Nombre:"));
        panel.add(campoNombre);
        panel.add(new JLabel("Descripción:"));
        panel.add(campoDescripcion);
        panel.add(new JLabel("Stock mínimo:"));
        panel.add(campoStockMinimo);
        panel.add(new JLabel("Proveedor:"));
        panel.add(comboProveedor);
        panel.add(new JLabel("Nota:"));
        panel.add(scrollNota);

        int res = JOptionPane.showConfirmDialog(this, panel, "Modificar producto", JOptionPane.OK_CANCEL_OPTION);
        if (res == JOptionPane.OK_OPTION) {
            try {
                producto.setNombre(campoNombre.getText().trim());
                producto.setDescripcion(campoDescripcion.getText().trim());
                producto.setStockMinimo(Integer.parseInt(campoStockMinimo.getText().trim()));
                producto.setNota(campoNota.getText().trim());

                Proveedor proveedorSeleccionado = (Proveedor) comboProveedor.getSelectedItem();
                if (proveedorSeleccionado != null) {
                    producto.setIdProveedor(proveedorSeleccionado.getId());
                }

                boolean ok = new ProductoDAO().actualizar(producto);
                if (ok) {
                    JOptionPane.showMessageDialog(this, "Producto actualizado correctamente.");
                    cargarProductos();
                    panelStock.refrescarVista();
                } else {
                    JOptionPane.showMessageDialog(this, "Error al actualizar producto.");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Stock mínimo debe ser un número entero.");
            }
        }
    }

    private void eliminarProductoSeleccionado() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Selecciona un producto.");
            return;
        }

        String codigo = (String) modeloTabla.getValueAt(fila, 0);
        int confirmar = JOptionPane.showConfirmDialog(this,
                "¿Seguro que deseas eliminar este producto?",
                "Confirmar",
                JOptionPane.YES_NO_OPTION);
        if (confirmar == JOptionPane.YES_OPTION) {
            boolean ok = new ProductoDAO().eliminar(codigo);
            if (ok) {
                JOptionPane.showMessageDialog(this, "Producto eliminado.");
                cargarProductos();
                panelStock.refrescarVista();
            } else {
                JOptionPane.showMessageDialog(this, "Error al eliminar producto.");
            }
        }
    }
}