package vista;

import dao.ClienteDAO;
import modelo.Cliente;
import viewmodel.InventarioViewModel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PanelCliente extends JPanel {

    private final InventarioViewModel viewModel;
    private final JTable tabla;
    private final DefaultTableModel modeloTabla;
    private final PanelDashboard panelDashboard;

    public PanelCliente(InventarioViewModel viewModel, PanelDashboard panelDashboard) {
        this.viewModel = viewModel;
        this.panelDashboard = panelDashboard;
        setLayout(new BorderLayout());

        JLabel titulo = new JLabel("Gestión de Clientes", JLabel.CENTER);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 20));
        add(titulo, BorderLayout.NORTH);

        modeloTabla = new DefaultTableModel(new String[]{"ID", "Nombre", "Teléfono", "Email", "Dirección"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // tableau non modifiable
            }
        };

        tabla = new JTable(modeloTabla);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        JPanel botones = new JPanel();
        JButton btnNuevo = new JButton("Nuevo cliente");
        JButton btnEditar = new JButton("Editar");
        JButton btnEliminar = new JButton("Eliminar");
        botones.add(btnNuevo);
        botones.add(btnEditar);
        botones.add(btnEliminar);
        add(botones, BorderLayout.SOUTH);

        btnNuevo.addActionListener(e -> crearCliente());
        btnEditar.addActionListener(e -> editarCliente());
        btnEliminar.addActionListener(e -> eliminarCliente());

        cargarClientes();
    }

    private void cargarClientes() {
        modeloTabla.setRowCount(0);
        List<Cliente> lista = new ClienteDAO().listarPorUsuario(viewModel.getUsuarioActual().getId());
        for (Cliente c : lista) {
            modeloTabla.addRow(new Object[]{
                c.getId(),
                c.getNombre(),
                c.getTelefono(),
                c.getCorreo(),
                c.getDireccion()
            });
        }
    }

    private void crearCliente() {
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

        int res = JOptionPane.showConfirmDialog(this, panel, "Nuevo cliente", JOptionPane.OK_CANCEL_OPTION);
        if (res == JOptionPane.OK_OPTION) {
            Cliente c = new Cliente(
                0,
                campoNombre.getText().trim(),
                campoTelefono.getText().trim(),
                campoCorreo.getText().trim(),
                campoDireccion.getText().trim(),
                viewModel.getUsuarioActual().getId()
            );
            boolean ok = new ClienteDAO().guardar(c);
            if (ok) {
                JOptionPane.showMessageDialog(this, "Cliente guardado correctamente.");
                cargarClientes();
                panelDashboard.refrescar();
            } else {
                JOptionPane.showMessageDialog(this, "Error al guardar cliente.");
            }
        }
    }

    private void editarCliente() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Selecciona un cliente para editar.");
            return;
        }

        int id = (int) modeloTabla.getValueAt(fila, 0);
        Cliente c = new ClienteDAO().buscarPorId(id);
        if (c == null) {
            JOptionPane.showMessageDialog(this, "Cliente no encontrado.");
            return;
        }

        JTextField campoNombre = new JTextField(c.getNombre());
        JTextField campoTelefono = new JTextField(c.getTelefono());
        JTextField campoCorreo = new JTextField(c.getCorreo());
        JTextField campoDireccion = new JTextField(c.getDireccion());

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Nombre:"));
        panel.add(campoNombre);
        panel.add(new JLabel("Teléfono:"));
        panel.add(campoTelefono);
        panel.add(new JLabel("Correo electrónico:"));
        panel.add(campoCorreo);
        panel.add(new JLabel("Dirección:"));
        panel.add(campoDireccion);

        int res = JOptionPane.showConfirmDialog(this, panel, "Editar cliente", JOptionPane.OK_CANCEL_OPTION);
        if (res == JOptionPane.OK_OPTION) {
            c.setNombre(campoNombre.getText().trim());
            c.setTelefono(campoTelefono.getText().trim());
            c.setCorreo(campoCorreo.getText().trim());
            c.setDireccion(campoDireccion.getText().trim());

            boolean ok = new ClienteDAO().actualizar(c);
            if (ok) {
                JOptionPane.showMessageDialog(this, "Cliente actualizado.");
                cargarClientes();
                panelDashboard.refrescar();
            } else {
                JOptionPane.showMessageDialog(this, "Error al actualizar cliente.");
            }
        }
    }

    private void eliminarCliente() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Selecciona un cliente para eliminar.");
            return;
        }

        int id = (int) modeloTabla.getValueAt(fila, 0);
        int confirmar = JOptionPane.showConfirmDialog(this,
                "¿Estás seguro de que deseas eliminar este cliente?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION);
        if (confirmar == JOptionPane.YES_OPTION) {
            boolean ok = new ClienteDAO().eliminar(id);
            if (ok) {
                JOptionPane.showMessageDialog(this, "Cliente eliminado.");
                cargarClientes();
                panelDashboard.refrescar();
            } else {
                JOptionPane.showMessageDialog(this, "Error al eliminar cliente.");
            }
        }
    }
}