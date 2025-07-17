package vista;

import modelo.Usuario;
import viewmodel.InventarioViewModel;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class VentanaPrincipal extends JFrame {

    private final InventarioViewModel viewModel;
    private final CardLayout layoutCentral = new CardLayout();
    private final JPanel panelCentral = new JPanel(layoutCentral);
    private final Map<String, JPanel> paneles = new HashMap<>();

    public VentanaPrincipal(InventarioViewModel viewModel) {
        this.viewModel = viewModel;
        PanelDashboard panelDashboard = new PanelDashboard(viewModel);
        PanelStock panelStock = new PanelStock(viewModel, panelDashboard);
        Usuario usuario = viewModel.getUsuarioActual();

        setTitle("InvenTrack Pro - " + usuario.getNombre());
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Layout principal
        setLayout(new BorderLayout());

        // Menu latéral
        JPanel menuLateral = crearMenuLateral();

        // Barre d’outils
        JToolBar toolbar = crearToolBar();

        // Panneaux principaux
        paneles.put("Dashboard", panelDashboard);
        paneles.put("Productos", new PanelProductos(viewModel, panelStock));
        paneles.put("Stock", panelStock);
        paneles.put("Clientes", new PanelCliente(viewModel, panelDashboard));
        paneles.put("Proveedores", new PanelProveedor(viewModel, panelDashboard));
        paneles.put("Transacciones", new PanelTransaccion(viewModel, panelStock));

        for (Map.Entry<String, JPanel> entry : paneles.entrySet()) {
            panelCentral.add(entry.getValue(), entry.getKey());
        }

        add(menuLateral, BorderLayout.WEST);
        add(toolbar, BorderLayout.NORTH);
        add(panelCentral, BorderLayout.CENTER);
    }
    
    private JPanel crearPanelPlaceholder(String texto) {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel label = new JLabel(texto, JLabel.CENTER);
        label.setFont(new Font("SansSerif", Font.PLAIN, 18));
        panel.add(label, BorderLayout.CENTER);
        return panel;
    }

    private JPanel crearMenuLateral() {
        JPanel menu = new JPanel();
        menu.setLayout(new BoxLayout(menu, BoxLayout.Y_AXIS));
        menu.setBackground(new Color(230, 230, 250));
        menu.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        String[] secciones = {
            "Dashboard", "Productos", "Stock", "Clientes", "Proveedores", "Transacciones"
        };

        for (String s : secciones) {
            JButton btn = new JButton(s);
            btn.setAlignmentX(Component.CENTER_ALIGNMENT);
            btn.setMaximumSize(new Dimension(180, 30));
            btn.addActionListener(e -> {
            layoutCentral.show(panelCentral, s);
            JPanel panelActual = paneles.get(s);

            if (s.equals("Transacciones") && panelActual instanceof PanelTransaccion transaccion) {
                transaccion.refrescar();
            }
            });
            menu.add(btn);
            menu.add(Box.createVerticalStrut(10));
        }

        JButton btnSalir = new JButton("Cerrar sesión");
        btnSalir.setForeground(Color.RED);
        btnSalir.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnSalir.addActionListener(e -> logout());

        menu.add(Box.createVerticalGlue());
        menu.add(btnSalir);

        return menu;
    }

    private JToolBar crearToolBar() {
        JToolBar toolbar = new JToolBar();
        toolbar.setFloatable(false);
        toolbar.setBackground(new Color(204, 229, 255));

        Usuario u = viewModel.getUsuarioActual();
        JLabel lblUsuario = new JLabel("  Usuario: " + u.getNombre() + "  | Rol: " + u.getRol());
        lblUsuario.setFont(new Font("SansSerif", Font.PLAIN, 14));
        toolbar.add(lblUsuario);

        return toolbar;
    }

    private void logout() {
        int opcion = JOptionPane.showConfirmDialog(this, "¿Deseas cerrar sesión?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (opcion == JOptionPane.YES_OPTION) {
            viewModel.logout();
            dispose();
            new LoginFrame().setVisible(true);
        }
    }
}