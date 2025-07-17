package vista;

import dao.AlmacenDAO;
import dao.ProductoDAO;
import modelo.Almacen;
import modelo.Producto;
import modelo.Stock;
import viewmodel.InventarioViewModel;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PanelStock extends JPanel {

    private final InventarioViewModel viewModel;
    private final DefaultTableModel modeloTabla;
    private final JTable tabla;
    private final TableRowSorter<DefaultTableModel> sorter;
    private JPanel panelFiltros;
    private Map<String, Integer> mapaStockCritico;
    private PanelDashboard panelDashboard;

    public PanelStock(InventarioViewModel viewModel, PanelDashboard panelDashboard) {
        this.viewModel = viewModel;
        this.panelDashboard = panelDashboard;
        setLayout(new BorderLayout());

        // Titre
        JLabel titulo = new JLabel("Gestión de Stock", JLabel.CENTER);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 20));
        add(titulo, BorderLayout.NORTH);

        // Tableau
        String[] columnas = {"Producto", "Almacén", "Cantidad"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabla = new JTable(modeloTabla);
        sorter = new TableRowSorter<>(modeloTabla);
        tabla.setRowSorter(sorter);

        // Coloration rouge si stock critique
        tabla.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus,
                                                           int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                String codigo = table.getValueAt(row, 0).toString();
                int cantidad = Integer.parseInt(table.getValueAt(row, 2).toString());
                Integer critico = mapaStockCritico.getOrDefault(codigo, -1);

                if (!isSelected) {
                    if (critico >= 0 && cantidad <= critico) {
                        c.setBackground(Color.PINK);
                    } else {
                        c.setBackground(Color.WHITE);
                    }
                }
                return c;
            }
        });

        add(new JScrollPane(tabla), BorderLayout.CENTER);

        // Créer panelFiltros qui contiendra boutons + recherche
        panelFiltros = new JPanel(new BorderLayout());

        // Recherche
        JPanel panelBusqueda = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBusqueda.add(new JLabel("Buscar producto:"));
        JTextField campoBusqueda = new JTextField(15);
        panelBusqueda.add(campoBusqueda);
        panelFiltros.add(panelBusqueda, BorderLayout.EAST);

        // Ajout du panelFiltros au haut de l’écran
        add(panelFiltros, BorderLayout.NORTH);

        // Recherche dynamique
        campoBusqueda.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { filtrar(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { filtrar(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { filtrar(); }

            public void filtrar() {
                String texto = campoBusqueda.getText().trim();
                if (texto.isEmpty()) {
                    sorter.setRowFilter(null);
                } else {
                    sorter.setRowFilter(RowFilter.regexFilter("(?i)" + texto, 0));
                }
            }
        });

        // Panneau des boutons du bas
        JPanel panelBotones = new JPanel();
        JButton btnNuevo = new JButton("Nuevo stock");
        JButton btnEditar = new JButton("Editar cantidad");
        JButton btnCrearAlmacen = new JButton("Crear almacén");

        btnNuevo.addActionListener(e -> crearNuevoStock());
        btnEditar.addActionListener(e -> editarCantidadSeleccionada());
        btnCrearAlmacen.addActionListener(e -> crearNuevoAlmacen());

        panelBotones.add(btnNuevo);
        panelBotones.add(btnEditar);
        panelBotones.add(btnCrearAlmacen);
        add(panelBotones, BorderLayout.SOUTH);

        recargarBotonesFiltro();
        cargarStock();
    }

    private void recargarBotonesFiltro() {
        // Crée un nouveau sous-panneau de boutons (à gauche)
        JPanel botones = new JPanel();
        botones.setLayout(new FlowLayout(FlowLayout.LEFT));

        JButton btnTodos = new JButton("Todos");
        btnTodos.addActionListener(e -> cargarStock());
        botones.add(btnTodos);

        AlmacenDAO almacenDAO = new AlmacenDAO();
        List<Almacen> almacenes = almacenDAO.listarPorUsuario(viewModel.getUsuarioActual().getId());
        for (Almacen a : almacenes) {
            JButton btn = new JButton(a.getNombre());
            int idAlmacen = a.getId();
            btn.addActionListener(e -> cargarStockPorAlmacen(idAlmacen));
            botones.add(btn);
        }

        panelFiltros.add(botones, BorderLayout.WEST);
        revalidate();
        repaint();
    }

    private void cargarStock() {
        modeloTabla.setRowCount(0);
        mapaStockCritico = cargarMapaStockCritico();

        List<Stock> lista = viewModel.getStock();
        for (Stock s : lista) {
            modeloTabla.addRow(new Object[]{
                s.getCodigoProducto(),
                s.getIdAlmacen(),
                s.getCantidad()
            });
        }
    }

    private void cargarStockPorAlmacen(int idAlmacen) {
        modeloTabla.setRowCount(0);
        mapaStockCritico = cargarMapaStockCritico();

        List<Stock> lista = viewModel.getStockFiltradoPorAlmacen(idAlmacen);
        for (Stock s : lista) {
            modeloTabla.addRow(new Object[]{
                s.getCodigoProducto(),
                s.getIdAlmacen(),
                s.getCantidad()
            });
        }
    }

    private Map<String, Integer> cargarMapaStockCritico() {
        Map<String, Integer> mapa = new HashMap<>();
        ProductoDAO productoDAO = new ProductoDAO();
        List<Producto> productos = productoDAO.listarPorUsuario(viewModel.getUsuarioActual().getId());

        for (Producto p : productos) {
            mapa.put(p.getCodigo(), p.getStockMinimo());
        }

        return mapa;
    }

    private void crearNuevoStock() {
        String idUsuario = viewModel.getUsuarioActual().getId();
        ProductoDAO productoDAO = new ProductoDAO();
        AlmacenDAO almacenDAO = new AlmacenDAO();

        List<Producto> productos = productoDAO.listarPorUsuario(idUsuario);
        List<Almacen> almacenes = almacenDAO.listarPorUsuario(idUsuario);

        JComboBox<String> comboProductos = new JComboBox<>();
        for (Producto p : productos) {
            comboProductos.addItem(p.getCodigo() + " - " + p.getNombre());
        }

        JComboBox<String> comboAlmacenes = new JComboBox<>();
        for (Almacen a : almacenes) {
            comboAlmacenes.addItem(a.getId() + " - " + a.getNombre());
        }

        JTextField campoCantidad = new JTextField();

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Selecciona producto:"));
        panel.add(comboProductos);
        panel.add(new JLabel("Selecciona almacén:"));
        panel.add(comboAlmacenes);
        panel.add(new JLabel("Cantidad inicial:"));
        panel.add(campoCantidad);

        int res = JOptionPane.showConfirmDialog(this, panel, "Nuevo stock", JOptionPane.OK_CANCEL_OPTION);
        if (res == JOptionPane.OK_OPTION) {
            try {
                String productoSeleccionado = (String) comboProductos.getSelectedItem();
                String codigo = productoSeleccionado.split(" - ")[0];

                String almacenSeleccionado = (String) comboAlmacenes.getSelectedItem();
                int idAlmacen = Integer.parseInt(almacenSeleccionado.split(" - ")[0]);

                int cantidad = Integer.parseInt(campoCantidad.getText().trim());

                Stock nuevo = new Stock(codigo, idAlmacen, cantidad, idUsuario);
                boolean ok = viewModel.crearStock(nuevo);
                if (ok) {
                    JOptionPane.showMessageDialog(this, "Stock creado correctamente.");
                    cargarStock();
                    panelDashboard.refrescar();
                } else {
                    JOptionPane.showMessageDialog(this, "Error al crear el stock.");
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Datos inválidos.");
            }
        }
    }

    private void crearNuevoAlmacen() {
        JTextField campoNombre = new JTextField();
        JTextField campoUbicacion = new JTextField();

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Nombre del almacén:"));
        panel.add(campoNombre);
        panel.add(new JLabel("Ubicación:"));
        panel.add(campoUbicacion);

        int res = JOptionPane.showConfirmDialog(this, panel, "Nuevo almacén", JOptionPane.OK_CANCEL_OPTION);
        if (res == JOptionPane.OK_OPTION) {
            String nombre = campoNombre.getText().trim();
            String ubicacion = campoUbicacion.getText().trim();
            String idUsuario = viewModel.getUsuarioActual().getId();

            if (nombre.isEmpty()) {
                JOptionPane.showMessageDialog(this, "El nombre no puede estar vacío.");
                return;
            }

            Almacen nuevo = new Almacen(0, nombre, ubicacion, idUsuario);
            boolean ok = viewModel.crearAlmacen(nuevo);

            if (ok) {
                JOptionPane.showMessageDialog(this, "Almacén creado correctamente.");
            } else {
                JOptionPane.showMessageDialog(this, "Error al crear el almacén.");
            }

            recargarBotonesFiltro();
            panelDashboard.refrescar();
        }
    }

    private void editarCantidadSeleccionada() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Selecciona una fila.");
            return;
        }

        String codigo = (String) modeloTabla.getValueAt(fila, 0);
        int idAlmacen = (int) modeloTabla.getValueAt(fila, 1);
        String cantidadStr = JOptionPane.showInputDialog(this, "Nueva cantidad:");

        if (cantidadStr != null && !cantidadStr.isBlank()) {
            try {
                int cantidad = Integer.parseInt(cantidadStr.trim());
                boolean ok = viewModel.modificarCantidad(codigo, idAlmacen, cantidad);
                if (ok) {
                    JOptionPane.showMessageDialog(this, "Cantidad actualizada.");
                    cargarStock();
                    panelDashboard.refrescar();
                } else {
                    JOptionPane.showMessageDialog(this, "Error al actualizar.");
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Cantidad inválida.");
            }
        }
    }
    public void refrescarVista() {
        cargarStock(); 
        recargarBotonesFiltro();
        panelDashboard.refrescar();
    }
}