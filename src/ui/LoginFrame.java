package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import core.LibrarySystem;
import core.User;
import utils.Utils;

public class LoginFrame extends JFrame {
    private LibrarySystem librarySystem;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerButton;

    private static final Color DARK_BG = Utils.PINE_TEAL;        // #344E41
    private static final Color COMPONENT_BG = Utils.HUNTER_GREEN; // #3A5A40
    private static final Color ACCENT = Utils.FERN;               // #588157
    private static final Color TEXT = Utils.DUST_GREY;            // #DAD7CD
    private static final Color TEXT_DIM = Utils.DRY_SAGE;         // #A3B18A

    public LoginFrame(LibrarySystem system) {
        this.librarySystem = system;
        setupUI();
    }

    private void setupUI() {
        setTitle("Library Management System - Login");
        setSize(500, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        getContentPane().setBackground(DARK_BG);

        // Main panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(DARK_BG);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(100, 80, 100, 80));

        // Title (optional - adds nice touch)
        JLabel titleLabel = new JLabel("Library System");
        titleLabel.setFont(new Font("Monospace", Font.BOLD, 24));
        titleLabel.setForeground(TEXT);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainPanel.add(titleLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 50)));

        // Add components with spacing
        mainPanel.add(createUsernamePanel());
        mainPanel.add(Box.createRigidArea(new Dimension(0, 25)));
        mainPanel.add(createPasswordPanel());
        mainPanel.add(Box.createRigidArea(new Dimension(0, 50)));
        mainPanel.add(createButtonPanel());

        add(mainPanel);
    }

    private JPanel createUsernamePanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(DARK_BG);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);

        this.usernameField = createStyledTextField("enter username");
        panel.add(usernameField);

        return panel;
    }

    private JPanel createPasswordPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(DARK_BG);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);

        this.passwordField = createStyledPasswordField("enter password");
        panel.add(passwordField);

        return panel;
    }

    private JTextField createStyledTextField(String placeholder) {
        JTextField field = new JTextField();
        field.setFont(new Font("Monospace", Font.PLAIN, 16));
        field.setForeground(TEXT);
        field.setBackground(DARK_BG);
        field.setCaretColor(TEXT);

        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ACCENT, 2, true),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));

        field.setMaximumSize(new Dimension(400, 50));

        // Placeholder
        field.setText(placeholder);
        field.setForeground(TEXT_DIM);

        field.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                if (field.getText().equals(placeholder)) {
                    field.setText("");
                    field.setForeground(TEXT);
                }
            }
            public void focusLost(FocusEvent e) {
                if (field.getText().isEmpty()) {
                    field.setText(placeholder);
                    field.setForeground(TEXT_DIM);
                }
            }
        });

        return field;
    }

    private JPasswordField createStyledPasswordField(String placeholder) {
        JPasswordField field = new JPasswordField();
        field.setFont(new Font("Monospace", Font.PLAIN, 16));
        field.setForeground(TEXT);
        field.setBackground(DARK_BG);
        field.setCaretColor(TEXT);
        field.setEchoChar((char) 0); // Show characters initially for placeholder

        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ACCENT, 2, true),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));

        field.setMaximumSize(new Dimension(400, 50));

        // Placeholder
        field.setText(placeholder);
        field.setForeground(TEXT_DIM);

        field.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                if (new String(field.getPassword()).equals(placeholder)) {
                    field.setText("");
                    field.setForeground(TEXT);
                    field.setEchoChar('•'); // Hide password when typing
                }
            }
            public void focusLost(FocusEvent e) {
                if (field.getPassword().length == 0) {
                    field.setEchoChar((char) 0); // Show placeholder
                    field.setText(placeholder);
                    field.setForeground(TEXT_DIM);
                }
            }
        });

        // Enter key triggers login
        field.addActionListener(e -> handleLogin());

        return field;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        panel.setBackground(DARK_BG);
        panel.setMaximumSize(new Dimension(400, 50));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);

        loginButton = createStyledButton("login");
        loginButton.addActionListener(e -> handleLogin());

        registerButton = createStyledButton("register new account");
        registerButton.addActionListener(e -> handleRegister());

        panel.add(loginButton);
        panel.add(registerButton);

        return panel;
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Monospace", Font.PLAIN, 14));
        button.setForeground(TEXT);
        button.setBackground(DARK_BG);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Hover effect
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setForeground(ACCENT);
            }
            public void mouseExited(MouseEvent e) {
                button.setForeground(TEXT);
            }
        });

        return button;
    }

    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();

        // Validate
        if (username.isEmpty() || username.equals("enter username")) {
            showError("Please enter a username");
            return;
        }

        if (password.isEmpty() || password.equals("enter password")) {
            showError("Please enter a password");
            return;
        }

        // Authenticate
        User user = librarySystem.login(username, password);

        if (user != null) {
            // Success
            this.dispose();

            SwingUtilities.invokeLater(() -> {
                // TODO: Create DashboardFrame when ready
                JOptionPane.showMessageDialog(null,
                        "Login successful!\nWelcome, " + user.getUsername() + "!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                // DashboardFrame dashboard = new DashboardFrame(librarySystem, user);
                // dashboard.setVisible(true);
            });
        } else {
            showError("Invalid username or password");
            passwordField.setText("");
            passwordField.setEchoChar((char) 0);
        }
    }

    private void handleRegister() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();

        // Validate
        if (username.isEmpty() || username.equals("enter username")) {
            showError("Please enter a username");
            return;
        }

        if (password.isEmpty() || password.equals("enter password")) {
            showError("Please enter a password");
            return;
        }

        if (password.length() < 6) {
            showError("Password must be at least 6 characters");
            return;
        }

        // Register (all users are admins in librarian-only system)
        boolean success = librarySystem.registerUser(username, password, "ADMIN");

        if (success) {
            showSuccess("Account created successfully!\nYou can now login.");

            // Clear fields
            usernameField.setText("enter username");
            usernameField.setForeground(TEXT_DIM);

            passwordField.setText("enter password");
            passwordField.setForeground(TEXT_DIM);
            passwordField.setEchoChar((char) 0);
        } else {
            showError("Username already exists");
        }
    }

    private void showError(String message) {
        // Custom styled dialog
        UIManager.put("OptionPane.background", COMPONENT_BG);
        UIManager.put("Panel.background", COMPONENT_BG);
        UIManager.put("OptionPane.messageForeground", TEXT);

        JOptionPane.showMessageDialog(
                this,
                message,
                "Error",
                JOptionPane.ERROR_MESSAGE
        );
    }

    private void showSuccess(String message) {
        // Custom styled dialog
        UIManager.put("OptionPane.background", COMPONENT_BG);
        UIManager.put("Panel.background", COMPONENT_BG);
        UIManager.put("OptionPane.messageForeground", TEXT);

        JOptionPane.showMessageDialog(
                this,
                message,
                "Success",
                JOptionPane.INFORMATION_MESSAGE
        );
    }
}