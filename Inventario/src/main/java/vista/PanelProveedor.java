package vista;

import dao.ProveedorDAO;
import modelo.Proveedor;
import viewmodel.InventarioViewModel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PanelProveedor extends JPanel {

    private final InventarioViewModel viewModel;
    private final JTable tabla;
    private final DefaultTableModel modeloTabla;
    private final PanelDashboard panelDashboard;

    public PanelProveedor(InventarioViewModel viewModel, PanelDashboard panelDashboard) {
        this.viewModel = viewModel;
        this.panelDashboard = panelDashboard;
        setLayout(new BorderLayout());

        JLabel titulo = new JLabel("Gestión de Proveedores", JLabel.CENTER);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 20));
        add(titulo, BorderLayout.NORTH);

        modeloTabla = new DefaultTableModel(new String[]{"ID", "Nombre", "Teléfono", "Email", "Dirección"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // ← Tableau non modifiable
            }
        };

        tabla = new JTable(modeloTabla);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        JPanel botones = new JPanel();
        JButton btnNuevo = new JButton("Nuevo proveedor");
        JButton btnEditar = new JButton("Editar");
        JButton btnEliminar = new JButton("Eliminar");
        botones.add(btnNuevo);
        botones.add(btnEditar);
        botones.add(btnEliminar);
        add(botones, BorderLayout.SOUTH);

        btnNuevo.addActionListener(e -> crearProveedor());
        btnEditar.addActionListener(e -> editarProveedorSeleccionado());
        btnEliminar.addActionListener(e -> eliminarProveedorSeleccionado());

        cargarProveedores();
    }

    private void cargarProveedores() {
        modeloTabla.setRowCount(0);
        List<Proveedor> lista = new ProveedorDAO().listarPorUsuario(viewModel.getUsuarioActual().getId());
        for (Proveedor p : lista) {
            modeloTabla.addRow(new Object[]{
                p.getId(),
                p.getNombre(),
                p.getTelefono(),
                p.getCorreo(),
                p.getDireccion()
            });
        }
    }

    private void crearProveedor() {
        JTextField campoNombre = new JTextField();
        JTextField campoTelefono = new JTextField();
        JTextField campoCorreo = new JTextField();
        JTextField campoDireccion = new JTextField();

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Nombre:"));
        panel.add(campoNombre);
        panel.add(new JLabel("Teléfono:"));
        panel.add(campoTelefono);
        panel.add(new JLabel("Correo electrónico:"));
        panel.add(campoCorreo);
        panel.add(new JLabel("Dirección:"));
        panel.add(campoDireccion);

        int res = JOptionPane.showConfirmDialog(this, panel, "Nuevo proveedor", JOptionPane.OK_CANCEL_OPTION);
        if (res == JOptionPane.OK_OPTION) {
            String nombre = campoNombre.getText().trim();
            String telefono = campoTelefono.getText().trim();
            String correo = campoCorreo.getText().trim();
            String direccion = campoDireccion.getText().trim();

            if (nombre.isEmpty()) {
                JOptionPane.showMessageDialog(this, "El nombre es obligatorio.");
                return;
            }

            Proveedor p = new Proveedor(0, nombre, telefono, correo, direccion, viewModel.getUsuarioActual().getId());
            boolean ok = new ProveedorDAO().guardar(p);
            if (ok) {
                JOptionPane.showMessageDialog(this, "Proveedor creado correctamente.");
                cargarProveedores();
                panelDashboard.refrescar();
            } else {
                JOptionPane.showMessageDialog(this, "Error al crear proveedor.");
            }
        }
    }

    private void editarProveedorSeleccionado() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Selecciona un proveedor para editar.");
            return;
        }

        int id = (int) modeloTabla.getValueAt(fila, 0);
        Proveedor p = new ProveedorDAO().buscarPorId(id);
        if (p == null) {
            JOptionPane.showMessageDialog(this, "Proveedor no encontrado.");
            return;
        }

        JTextField campoNombre = new JTextField(p.getNombre());
        JTextField campoTelefono = new JTextField(p.getTelefono());
        JTextField campoCorreo = new JTextField(p.getCorreo());
        JTextField campoDireccion = new JTextField(p.getDireccion());

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Nombre:"));
        panel.add(campoNombre);
        panel.add(new JLabel("Teléfono:"));
        panel.add(campoTelefono);
        panel.add(new JLabel("Correo electrónico:"));
        panel.add(campoCorreo);
        panel.add(new JLabel("Dirección:"));
        panel.add(campoDireccion);

        int res = JOptionPane.showConfirmDialog(this, panel, "Editar proveedor", JOptionPane.OK_CANCEL_OPTION);
        if (res == JOptionPane.OK_OPTION) {
            p.setNombre(campoNombre.getText().trim());
            p.setTelefono(campoTelefono.getText().trim());
            p.setCorreo(campoCorreo.getText().trim());
            p.setDireccion(campoDireccion.getText().trim());

            boolean ok = new ProveedorDAO().actualizar(p);
            if (ok) {
                JOptionPane.showMessageDialog(this, "Proveedor actualizado.");
                cargarProveedores();
                panelDashboard.refrescar();
            } else {
                JOptionPane.showMessageDialog(this, "Error al actualizar proveedor.");
            }
        }
    }

    private void eliminarProveedorSeleccionado() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Selecciona un proveedor para eliminar.");
            return;
        }

        int id = (int) modeloTabla.getValueAt(fila, 0);
        int confirmar = JOptionPane.showConfirmDialog(this,
                "¿Seguro que deseas eliminar este proveedor?",
                "Confirmar",
                JOptionPane.YES_NO_OPTION);
        if (confirmar == JOptionPane.YES_OPTION) {
            boolean ok = new ProveedorDAO().eliminar(id);
            if (ok) {
                JOptionPane.showMessageDialog(this, "Proveedor eliminado.");
                cargarProveedores();
                panelDashboard.refrescar();
            } else {
                JOptionPane.showMessageDialog(this, "No se puede eliminar el proveedor porque tiene productos asociados.");
            }
        }
    }
}