package vista;

import dao.*;
import modelo.*;
import viewmodel.InventarioViewModel;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDateTime;
import java.util.List;

public class PanelTransaccion extends JPanel {

    private final InventarioViewModel viewModel;

    private JComboBox<String> comboTipo;
    private JComboBox<Almacen> comboAlmacen;
    private JComboBox<Stock> comboStock;
    private JComboBox<Producto> comboProducto;
    private JComboBox<Cliente> comboCliente;
    private JComboBox<Proveedor> comboProveedor;
    private JTextField campoCantidad;
    private JButton btnRegistrar;
    private JButton btnEliminar;

    private JTable tabla;
    private DefaultTableModel modeloTabla;

    private JPanel panelProducto;
    private PanelStock panelStock;

    public PanelTransaccion(InventarioViewModel viewModel, PanelStock panelStock) {
        this.viewModel = viewModel;
        this.panelStock = panelStock;
        setLayout(new BorderLayout());

        JLabel titulo = new JLabel("Registrar Transacción", JLabel.CENTER);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 20));
        add(titulo, BorderLayout.NORTH);

        inicializarComponentes();
        construirFormulario();
        construirTabla();

        cargarAlmacenes();
        cargarClientes();
        cargarProveedores();
        cargarProductos();
        cargarTransacciones();
        actualizarVistaTipo();
    }

    private void inicializarComponentes() {
        comboTipo = new JComboBox<>(new String[]{"venta", "compra"});
        comboAlmacen = new JComboBox<>();
        comboStock = new JComboBox<>();
        comboProducto = new JComboBox<>();
        comboCliente = new JComboBox<>();
        comboProveedor = new JComboBox<>();
        campoCantidad = new JTextField();
        btnRegistrar = new JButton("Registrar");
        btnEliminar = new JButton("Eliminar transacción");

        comboTipo.addActionListener(e -> {
            CardLayout cl = (CardLayout)(panelProducto.getLayout());
            String tipo = (String) comboTipo.getSelectedItem();
            cl.show(panelProducto, tipo);
            actualizarVistaTipo();
        });

        comboAlmacen.addActionListener(e -> cargarStocks());
        comboProveedor.addActionListener(e -> actualizarProductosProveedor());
        btnRegistrar.addActionListener(e -> registrar());
        btnEliminar.addActionListener(e -> eliminar());
    }

    private void construirFormulario() {
        JPanel formulario = new JPanel(new GridBagLayout());
        formulario.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.GRAY),
                "Formulario de transacción",
                TitledBorder.LEFT, TitledBorder.TOP,
                new Font("SansSerif", Font.BOLD, 14), Color.DARK_GRAY
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 1.0;

        int y = 0;

        gbc.gridx = 0; gbc.gridy = y;
        formulario.add(new JLabel("Tipo de transacción:"), gbc);
        gbc.gridx = 1;
        formulario.add(comboTipo, gbc);
        y++;

        gbc.gridx = 0; gbc.gridy = y;
        formulario.add(new JLabel("Almacén:"), gbc);
        gbc.gridx = 1;
        formulario.add(comboAlmacen, gbc);
        y++;
        
        gbc.gridx = 0; gbc.gridy = y;
        formulario.add(new JLabel("Proveedor:"), gbc);
        gbc.gridx = 1;
        formulario.add(comboProveedor, gbc);
        y++;

        gbc.gridx = 0; gbc.gridy = y;
        formulario.add(new JLabel("Cliente:"), gbc);
        gbc.gridx = 1;
        formulario.add(comboCliente, gbc);
        y++;

        gbc.gridx = 0; gbc.gridy = y;
        formulario.add(new JLabel("Producto:"), gbc);
        gbc.gridx = 1;
        panelProducto = new JPanel(new CardLayout());
        panelProducto.add(comboStock, "venta");
        panelProducto.add(comboProducto, "compra");
        formulario.add(panelProducto, gbc);
        y++;

        gbc.gridx = 0; gbc.gridy = y;
        formulario.add(new JLabel("Cantidad:"), gbc);
        gbc.gridx = 1;
        formulario.add(campoCantidad, gbc);
        y++;

        gbc.gridx = 0; gbc.gridy = y;
        gbc.gridwidth = 2;
        formulario.add(btnRegistrar, gbc);
        gbc.gridwidth = 1;

        add(formulario, BorderLayout.NORTH);
    }

    private void construirTabla() {
        modeloTabla = new DefaultTableModel(new String[]{
                "ID","Tipo", "Fecha", "Producto", "Cantidad", "Cliente", "Proveedor", "Almacén"
        }, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        tabla = new JTable(modeloTabla);
        add(new JScrollPane(tabla), BorderLayout.CENTER);
        add(btnEliminar, BorderLayout.SOUTH);
    }

    private void actualizarVistaTipo() {
        boolean esVenta = comboTipo.getSelectedItem().equals("venta");
        comboStock.setVisible(esVenta);
        comboCliente.setVisible(esVenta);
        comboProducto.setVisible(!esVenta);
        comboProveedor.setVisible(!esVenta);
    }

    private void cargarAlmacenes() {
        comboAlmacen.removeAllItems();
        for (Almacen a : new AlmacenDAO().listarPorUsuario(viewModel.getUsuarioActual().getId())) {
            comboAlmacen.addItem(a);
        }
    }

    private void cargarClientes() {
        comboCliente.removeAllItems();
        for (Cliente c : new ClienteDAO().listarPorUsuario(viewModel.getUsuarioActual().getId())) {
            comboCliente.addItem(c);
        }
    }

    private void cargarProveedores() {
        comboProveedor.removeAllItems();
        for (Proveedor p : new ProveedorDAO().listarPorUsuario(viewModel.getUsuarioActual().getId())) {
            comboProveedor.addItem(p);
        }
    }

    private void cargarProductos() {
        comboProducto.removeAllItems();
        for (Producto p : new ProductoDAO().listarPorUsuario(viewModel.getUsuarioActual().getId())) {
            comboProducto.addItem(p);
        }
    }

    private void actualizarProductosProveedor() {
    comboProducto.removeAllItems();
    Proveedor proveedorSeleccionado = (Proveedor) comboProveedor.getSelectedItem();

    if (proveedorSeleccionado == null) {
        return;
    }

    int id = proveedorSeleccionado.getId();

    List<Producto> productos = new ProductoDAO().listarPorProveedor(id);

    for (Producto p : productos) {
        comboProducto.addItem(p);
    }

    comboProducto.revalidate();
    comboProducto.repaint();
}

    private void cargarStocks() {
        comboStock.removeAllItems();
        Almacen alm = (Almacen) comboAlmacen.getSelectedItem();
        if (alm == null) return;
        for (Stock s : new StockDAO().listarPorAlmacen(alm.getId())) {
            comboStock.addItem(s);
        }
    }

    private void registrar() {
        String tipo = (String) comboTipo.getSelectedItem();
        Almacen alm = (Almacen) comboAlmacen.getSelectedItem();
        String cantidadStr = campoCantidad.getText().trim();

        if (alm == null || cantidadStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Completa todos los campos.");
            return;
        }

        try {
            int cantidad = Integer.parseInt(cantidadStr);
            if (cantidad <= 0) throw new NumberFormatException();

            String codigoProducto;
            Integer idCliente = null, idProveedor = null;

            if (tipo.equals("venta")) {
                Stock s = (Stock) comboStock.getSelectedItem();
                Cliente c = (Cliente) comboCliente.getSelectedItem();
                if (s == null || c == null) {
                    JOptionPane.showMessageDialog(this, "Completa todos los campos.");
                    return;
                }
                if (cantidad > s.getCantidad()) {
                    JOptionPane.showMessageDialog(this, "Stock insuficiente.");
                    return;
                }
                codigoProducto = s.getCodigoProducto();
                idCliente = c.getId();
            } else {
                Producto p = (Producto) comboProducto.getSelectedItem();
                Proveedor prov = (Proveedor) comboProveedor.getSelectedItem();
                if (p == null || prov == null) {
                    JOptionPane.showMessageDialog(this, "Completa todos los campos.");
                    return;
                }
                codigoProducto = p.getCodigo();
                idProveedor = prov.getId();
            }

            Transaccion t = new Transaccion(
                    0, tipo, LocalDateTime.now(),
                    codigoProducto, cantidad,
                    viewModel.getUsuarioActual().getId(),
                    idCliente, idProveedor,
                    alm.getId()
            );

            boolean ok = new TransaccionDAO().registrar(t, alm.getId());
            if (ok) {
                JOptionPane.showMessageDialog(this, "Transacción registrada.");
                campoCantidad.setText("");
                cargarStocks();
                cargarTransacciones();
                panelStock.refrescarVista();
            } else {
                JOptionPane.showMessageDialog(this, "Error al registrar transacción.");
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "La cantidad debe ser un número válido.");
        }
    }

    private void cargarTransacciones() {
        modeloTabla.setRowCount(0);
        TransaccionDAO dao = new TransaccionDAO();
        for (Transaccion t : dao.listarPorUsuario(viewModel.getUsuarioActual().getId())) {
            String productoNombre = new ProductoDAO().buscarPorCodigo(t.getCodigoProducto()) != null
                    ? new ProductoDAO().buscarPorCodigo(t.getCodigoProducto()).getNombre() : t.getCodigoProducto();

            String clienteNombre = (t.getIdCliente() != null)
                    ? new ClienteDAO().buscarPorId(t.getIdCliente()).getNombre()
                    : "-";

            String proveedorNombre = (t.getIdProveedor() != null)
                    ? new ProveedorDAO().buscarPorId(t.getIdProveedor()).getNombre()
                    : "-";

            String almacenNombre = new AlmacenDAO().buscarPorId(t.getIdAlmacen()).getNombre();

            modeloTabla.addRow(new Object[]{
                    t.getId(),
                    t.getTipo(),
                    t.getFecha().toString(),
                    productoNombre,
                    t.getCantidad(),
                    clienteNombre,
                    proveedorNombre,
                    almacenNombre
            });
        }
    }

    private void eliminar() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Selecciona una transacción para eliminar.");
            return;
        }

        int id = (int) modeloTabla.getValueAt(fila, 0); // colonne ID
        System.out.println("🗑 Eliminando transacción ID: " + id);

        if (new TransaccionDAO().eliminar(id)) {
            JOptionPane.showMessageDialog(this, "Transacción eliminada.");
            cargarTransacciones();
        } else {
            JOptionPane.showMessageDialog(this, "No se pudo eliminar.");
        }
    }
    public void refrescar() {
    System.out.println("↻ Refrescar PanelTransaccion"); // ← ligne de test
    cargarAlmacenes();
    cargarClientes();
    cargarProveedores();
    cargarTransacciones();

    comboAlmacen.setSelectedIndex(-1);
    comboCliente.setSelectedIndex(-1);
    comboProveedor.setSelectedIndex(-1);
    comboStock.removeAllItems();
    comboProducto.removeAllItems();
    campoCantidad.setText("");
}
}