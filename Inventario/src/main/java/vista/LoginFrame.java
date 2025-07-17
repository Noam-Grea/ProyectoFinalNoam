package vista;

import modelo.Usuario;
import viewmodel.InventarioViewModel;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {

    private final JTextField campoUsuario = new JTextField(15);
    private final JPasswordField campoContrasena = new JPasswordField(15);
    private final InventarioViewModel viewModel = new InventarioViewModel();

    public LoginFrame() {
        setTitle("InvenTrack Pro - Inicio de sesión");
        setSize(600, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        GridBagConstraints gbc = new GridBagConstraints();

        JLabel titulo = new JLabel("Inicio de sesión");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 18));
        titulo.setForeground(new Color(0, 76, 153));
        gbc.gridwidth = 2;
        gbc.insets = new Insets(10, 0, 20, 0);
        panel.add(titulo, gbc);

        gbc.gridwidth = 1;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("ID Usuario:"), gbc);
        gbc.gridx = 1;
        panel.add(campoUsuario, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Contraseña:"), gbc);
        gbc.gridx = 1;
        panel.add(campoContrasena, gbc);

        // Bouton Ingresar
        JButton btnLogin = new JButton("Ingresar");
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        gbc.insets = new Insets(15, 5, 5, 5);
        btnLogin.addActionListener(e -> login());
        panel.add(btnLogin, gbc);

        // Lien Créer compte
        JButton btnCrear = new JButton("Crear cuenta");
        btnCrear.setForeground(new Color(0, 102, 204));
        btnCrear.setFont(new Font("SansSerif", Font.PLAIN, 12));
        btnCrear.setBorderPainted(false);
        btnCrear.setContentAreaFilled(false);
        btnCrear.addActionListener(e -> mostrarFormularioRegistro());

        gbc.gridy = 4;
        panel.add(btnCrear, gbc);

        add(panel);
    }

    private void login() {
        String id = campoUsuario.getText().trim();
        String contrasena = new String(campoContrasena.getPassword()).trim();

        if (viewModel.login(id, contrasena)) {
            Usuario u = viewModel.getUsuarioActual();
            JOptionPane.showMessageDialog(this, "Bienvenido " + u.getNombre() + " (" + u.getRol() + ")");
            dispose();
            new VentanaPrincipal(viewModel).setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Usuario o contraseña incorrectos", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void mostrarFormularioRegistro() {
    JTextField idField = new JTextField();
    JTextField nombreField = new JTextField();
    JPasswordField passField = new JPasswordField();
    

    idField.setPreferredSize(new Dimension(200, 25));
    nombreField.setPreferredSize(new Dimension(200, 25));
    passField.setPreferredSize(new Dimension(200, 25));

    JPanel panel = new JPanel(new GridLayout(0, 1));
    panel.add(new JLabel("ID de usuario:"));
    panel.add(idField);
    panel.add(new JLabel("Nombre completo:"));
    panel.add(nombreField);
    panel.add(new JLabel("Contraseña:"));
    panel.add(passField);
    

    int result = JOptionPane.showConfirmDialog(this, panel, "Crear nuevo usuario", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

    if (result == JOptionPane.OK_OPTION) {
        String id = idField.getText().trim();
        String nombre = nombreField.getText().trim();
        String pass = new String(passField.getPassword()).trim();
        

        if (id.isEmpty() || nombre.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Usuario nuevo = new Usuario(id, nombre, pass, "empleado");
        boolean ok = viewModel.agregarUsuario(nuevo);
        if (ok) {
            JOptionPane.showMessageDialog(this, "Usuario registrado correctamente.");
        } else {
            JOptionPane.showMessageDialog(this, "Error al registrar usuario (ID ya existe).", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
}