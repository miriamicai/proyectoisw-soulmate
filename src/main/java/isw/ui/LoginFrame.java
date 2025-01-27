package isw.ui;

import isw.cliente.Cliente;
import isw.domain.AutentifCustomer;
import isw.domain.LoginHash;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

public class LoginFrame extends JFrame {

    public Cliente cliente = new Cliente();
    public HashMap<String, Object> session = new HashMap<>();

    private Container container = getContentPane();
    private JLabel logLabel = new JLabel("Log in to");
    private JLabel userLabel = new JLabel("Email or username");
    private JLabel passwordLabel = new JLabel("Password");
    private JTextField userTextField = new JTextField();
    private JPasswordField passwordField = new JPasswordField();
    private JButton loginButton = new JButton("Log In");
    private JButton forgotButton = new JButton("Forgot your password?");
    private JLabel noaccountLabel = new JLabel("Don't have an account?");
    private JButton signupButton = new JButton("Sign up for Soulmate");

    private JLabel logoLabel = new JLabel();

    public LoginFrame() {
        this.setTitle("Login Form");
        this.setVisible(true);
        this.setBounds(10, 10, 500, 650);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setResizable(false);

        setLayoutManager();
        setLocationAndSize();
        addComponentsToContainer();

        container.setBackground(new Color(32, 32, 32));

        //texto y botones
        userLabel.setForeground(Color.WHITE);
        passwordLabel.setForeground(Color.WHITE);
        noaccountLabel.setForeground(Color.WHITE);
        logLabel.setForeground(Color.WHITE);

        loginButton.setOpaque(true);
        loginButton.setContentAreaFilled(true);
        loginButton.setBorderPainted(false);
        loginButton.setBackground(Color.PINK);
        loginButton.setForeground(Color.WHITE);

        forgotButton.setForeground(new Color(32, 32, 32));
        signupButton.setForeground(new Color(32, 32, 32));

        // Cargar el logo desde una ruta interna
        try {
            ImageIcon logo = new ImageIcon("src/main/resources/soulmatelogosinfondo.png");
            Image scaledImage = logo.getImage().getScaledInstance(300, 300, Image.SCALE_SMOOTH);
            logo.setImage(scaledImage);
            logoLabel.setIcon(logo);
            logoLabel.setBounds(120, 65, 300, 240); // posición y tamaño del logo
            container.add(logoLabel);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error cargando la imagen del logo.");
            e.printStackTrace();
        }

        // Lógica para iniciar sesión
        loginButton.addActionListener(e -> {
            String usuario = userTextField.getText();
            String password = new String(passwordField.getPassword());
            Cliente cliente = new Cliente(); //instancio un cliente
            HashMap<String, Object> session = new HashMap<>(); //creo la sesión
            //añadir usuario y contraseña a la sesión
            session.put("usuario", usuario);
            session.put("contraseña", password);
            System.out.println("botón pulsado correcto");
            int idLogged = cliente.login(session);

            if (idLogged > 0) {
                LoginHash.setCredentials(session);
                JOptionPane.showMessageDialog(this, "¡Login exitoso!");
                new JVentanaLogged(idLogged, session, cliente); // Abrir la nueva ventana
            } else {
                JOptionPane.showMessageDialog(this, "Usuario o contraseña incorrectos.");
            }
        });
    }

    public void setLayoutManager() {
        container.setLayout(null);
    }

    public void setLocationAndSize() {
        Font defaultFont = userLabel.getFont();  // Get the current default font
        Font newFont = defaultFont.deriveFont(Font.BOLD, 30);  // Make it bold and increase size to 18
        logLabel.setFont(newFont);  // Apply the new font to the label

        // Setting location and size of each component
        logLabel.setBounds(180, 20, 200, 30);
        userLabel.setBounds(185, 310, 200, 30);
        userTextField.setBounds(150, 340, 180, 30);
        passwordLabel.setBounds(215, 370, 100, 30);
        passwordField.setBounds(150, 400, 180, 30);
        loginButton.setBounds(95, 450, 300, 30);
        forgotButton.setBounds(120, 500, 250, 30);
        noaccountLabel.setBounds(60, 550, 200, 30);
        signupButton.setBounds(230, 550, 200, 30);
    }

    public void addComponentsToContainer() {
        container.add(userLabel);
        container.add(logLabel);
        container.add(passwordLabel);
        container.add(userTextField);
        container.add(passwordField);
        container.add(forgotButton);
        container.add(loginButton);
        container.add(noaccountLabel);
        container.add(signupButton);
    }

    public static void main(String[] args) {
        LoginFrame frame = new LoginFrame();
    }
}
