package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import core.LibrarySystem;
import core.User;
import utils.Utils;

/**
 * Login screen for the library management system
 */
public class LoginFrame extends JFrame {

    private LibrarySystem librarySystem;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerButton;

    /*
     * Constructor - sets up the login frame
     */
    public LoginFrame(LibrarySystem system) {
        this.librarySystem = system;
        setupUI();
    }

    /*
     * Set up the user interface
     */
    private void setupUI() {
        setTitle("Library Management System - Login");
        setSize(500, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        getContentPane().setBackground(Utils.BG_PRIMARY);

        // Create main panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Utils.BG_PRIMARY);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(100, 80, 100, 80));

        // Add title
        JLabel titleLabel = new JLabel("Library System");
        titleLabel.setFont(new Font("Monospace", Font.BOLD, 24));
        titleLabel.setForeground(Utils.TEXT_PRIMARY);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Add components with spacing
        mainPanel.add(titleLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 50)));
        mainPanel.add(createUsernamePanel());
        mainPanel.add(Box.createRigidArea(new Dimension(0, 25)));
        mainPanel.add(createPasswordPanel());
        mainPanel.add(Box.createRigidArea(new Dimension(0, 50)));
        mainPanel.add(createButtonPanel());

        add(mainPanel);
    }

    /*
     * Create username input panel
     */
    private JPanel createUsernamePanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Utils.BG_PRIMARY);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);

        usernameField = new JTextField();
        usernameField.setFont(new Font("Monospace", Font.PLAIN, 16));
        usernameField.setForeground(Utils.TEXT_PRIMARY);
        usernameField.setBackground(Utils.BG_PRIMARY);
        usernameField.setCaretColor(Utils.TEXT_PRIMARY);
        usernameField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Utils.ACCENT, 2, true),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        usernameField.setMaximumSize(new Dimension(400, 50));

        // Add placeholder
        usernameField.setText("enter username");
        usernameField.setForeground(Utils.TEXT_SECONDARY);

        usernameField.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                if (usernameField.getText().equals("enter username")) {
                    usernameField.setText("");
                    usernameField.setForeground(Utils.TEXT_PRIMARY);
                }
            }
            public void focusLost(FocusEvent e) {
                if (usernameField.getText().isEmpty()) {
                    usernameField.setText("enter username");
                    usernameField.setForeground(Utils.TEXT_SECONDARY);
                }
            }
        });

        panel.add(usernameField);

        return panel;
    }

    /*
     * Create password input panel
     */
    private JPanel createPasswordPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Utils.BG_PRIMARY);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);

        passwordField = new JPasswordField();
        passwordField.setFont(new Font("Monospace", Font.PLAIN, 16));
        passwordField.setForeground(Utils.TEXT_PRIMARY);
        passwordField.setBackground(Utils.BG_PRIMARY);
        passwordField.setCaretColor(Utils.TEXT_PRIMARY);
        passwordField.setEchoChar((char) 0);
        passwordField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Utils.ACCENT, 2, true),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        passwordField.setMaximumSize(new Dimension(400, 50));

        // Add placeholder
        passwordField.setText("enter password");
        passwordField.setForeground(Utils.TEXT_SECONDARY);

        passwordField.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                if (new String(passwordField.getPassword()).equals("enter password")) {
                    passwordField.setText("");
                    passwordField.setForeground(Utils.TEXT_PRIMARY);
                    passwordField.setEchoChar('•');
                }
            }
            public void focusLost(FocusEvent e) {
                if (passwordField.getPassword().length == 0) {
                    passwordField.setEchoChar((char) 0);
                    passwordField.setText("enter password");
                    passwordField.setForeground(Utils.TEXT_SECONDARY);
                }
            }
        });

        // Enter key triggers login
        passwordField.addActionListener(e -> handleLogin());

        panel.add(passwordField);

        return panel;
    }

    /*
     * Create button panel with login and register buttons
     */
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        panel.setBackground(Utils.BG_PRIMARY);
        panel.setMaximumSize(new Dimension(400, 50));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);

        loginButton = new JButton("login");
        loginButton.setFont(new Font("Monospace", Font.PLAIN, 14));
        loginButton.setForeground(Utils.TEXT_PRIMARY);
        loginButton.setBackground(Utils.BG_PRIMARY);
        loginButton.setBorderPainted(false);
        loginButton.setFocusPainted(false);
        loginButton.setContentAreaFilled(false);
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginButton.addActionListener(e -> handleLogin());

        registerButton = new JButton("register new account");
        registerButton.setFont(new Font("Monospace", Font.PLAIN, 14));
        registerButton.setForeground(Utils.TEXT_PRIMARY);
        registerButton.setBackground(Utils.BG_PRIMARY);
        registerButton.setBorderPainted(false);
        registerButton.setFocusPainted(false);
        registerButton.setContentAreaFilled(false);
        registerButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        registerButton.addActionListener(e -> handleRegister());

        // Add hover effects
        loginButton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                loginButton.setForeground(Utils.ACCENT);
            }
            public void mouseExited(MouseEvent e) {
                loginButton.setForeground(Utils.TEXT_PRIMARY);
            }
        });

        registerButton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                registerButton.setForeground(Utils.ACCENT);
            }
            public void mouseExited(MouseEvent e) {
                registerButton.setForeground(Utils.TEXT_PRIMARY);
            }
        });

        panel.add(loginButton);
        panel.add(registerButton);

        return panel;
    }

    /*
     * Handle login button click
     */
    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();

        // Check if fields are empty
        if (username.isEmpty() || username.equals("enter username")) {
            JOptionPane.showMessageDialog(this, "Please enter a username");
            return;
        }

        if (password.isEmpty() || password.equals("enter password")) {
            JOptionPane.showMessageDialog(this, "Please enter a password");
            return;
        }

        // Try to login
        User user = librarySystem.login(username, password);

        if (user != null) {
            // Login successful
            this.dispose();

            // Open dashboard
            SwingUtilities.invokeLater(() -> {
                DashboardFrame dashboard = new DashboardFrame(librarySystem, user);
                dashboard.setVisible(true);
            });
        } else {
            // Login failed
            JOptionPane.showMessageDialog(this, "Invalid username or password");

            // Clear password field
            passwordField.setText("");
            passwordField.setEchoChar((char) 0);
        }
    }

    /*
     * Handle register button click
     */
    private void handleRegister() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();

        // Check if fields are empty
        if (username.isEmpty() || username.equals("enter username")) {
            JOptionPane.showMessageDialog(this, "Please enter a username");
            return;
        }

        if (password.isEmpty() || password.equals("enter password")) {
            JOptionPane.showMessageDialog(this, "Please enter a password");
            return;
        }

        if (password.length() < 6) {
            JOptionPane.showMessageDialog(this, "Password must be at least 6 characters");
            return;
        }

        // Try to register
        boolean success = librarySystem.registerUser(username, password, "ADMIN");

        if (success) {
            JOptionPane.showMessageDialog(this, "Account created successfully!\nYou can now login.");

            // Clear fields
            usernameField.setText("enter username");
            usernameField.setForeground(Utils.TEXT_SECONDARY);

            passwordField.setText("enter password");
            passwordField.setForeground(Utils.TEXT_SECONDARY);
            passwordField.setEchoChar((char) 0);
        } else {
            JOptionPane.showMessageDialog(this, "Username already exists");
        }
    }
}