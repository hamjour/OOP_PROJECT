package ui;

import javax.swing.*;
import java.awt.*;
import core.LibrarySystem;
import core.User;
import ui.*;
import utils.Utils;

/**
 * Main dashboard frame after login
 */
public class DashboardFrame extends JFrame {

    private LibrarySystem librarySystem;
    private User currentUser;
    private JPanel contentPanel;
    private CardLayout cardLayout;

    /*
     * Constructor - sets up the dashboard
     */
    public DashboardFrame(LibrarySystem system, User user) {
        this.librarySystem = system;
        this.currentUser = user;
        setupUI();
    }

    /*
     * Set up the user interface
     */
    private void setupUI() {
        setTitle("Library Management System - Dashboard");
        setSize(1200, 800);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Add top panel
        JPanel topPanel = createTopPanel();
        add(topPanel, BorderLayout.NORTH);

        // Add navigation panel
        JPanel navPanel = createNavigationPanel();
        add(navPanel, BorderLayout.WEST);

        // Add content panel
        createContentPanel();
        add(contentPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    /*
     * Create top panel with user info and logout button
     */
    private JPanel createTopPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Utils.BG_PRIMARY);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // User info label
        JLabel userLabel = new JLabel("Logged in as: " + currentUser.getUsername());
        userLabel.setFont(new Font("Monospace", Font.BOLD, 14));
        userLabel.setForeground(Utils.TEXT_PRIMARY);

        // Logout button
        JButton logoutButton = new JButton("Logout");
        logoutButton.setFont(new Font("Monospace", Font.BOLD, 14));
        logoutButton.addActionListener(e -> handleLogout());

        panel.add(userLabel, BorderLayout.WEST);
        panel.add(logoutButton, BorderLayout.EAST);

        return panel;
    }

    /*
     * Create navigation panel with menu buttons
     */
    private JPanel createNavigationPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Utils.BG_SECONDARY);
        panel.setPreferredSize(new Dimension(200, 0));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Create navigation buttons
        JButton searchButton = createNavButton("Search Books");
        searchButton.addActionListener(e -> showPanel("search"));

        JButton issueButton = createNavButton("Issue Book");
        issueButton.addActionListener(e -> showPanel("issue"));

        JButton returnButton = createNavButton("Return Book");
        returnButton.addActionListener(e -> showPanel("return"));

        JButton membersButton = createNavButton("Manage Members");
        membersButton.addActionListener(e -> showPanel("members"));

        JButton booksButton = createNavButton("Manage Books");
        booksButton.addActionListener(e -> showPanel("books"));

        // Add buttons with spacing
        panel.add(searchButton);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(issueButton);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(returnButton);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(membersButton);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(booksButton);

        return panel;
    }

    /*
     * Create a navigation button
     */
    private JButton createNavButton(String text) {
        JButton button = new JButton(text);
        button.setMaximumSize(new Dimension(180, 40));
        button.setPreferredSize(new Dimension(180, 40));
        button.setFont(new Font("Monospace", Font.PLAIN, 14));
        button.setForeground(Utils.TEXT_PRIMARY);
        button.setBackground(Utils.BG_SECONDARY);
        button.setFocusPainted(false);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);

        return button;
    }

    /*
     * Create content panel with all feature panels
     */
    private void createContentPanel() {
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(Utils.BG_PRIMARY);

        // Add all panels
        contentPanel.add(new SearchBookPanel(librarySystem), "search");
        contentPanel.add(new IssueBookPanel(librarySystem), "issue");
        contentPanel.add(new ReturnBookPanel(librarySystem), "return");
        contentPanel.add(new MemberManagementPanel(librarySystem), "members");
        contentPanel.add(new BooksManagementPanel(librarySystem), "books");

        // Show search panel by default
        cardLayout.show(contentPanel, "search");
    }

    /*
     * Show a specific panel
     */
    private void showPanel(String panelName) {
        cardLayout.show(contentPanel, panelName);
    }

    /*
     * Handle logout button click
     */
    private void handleLogout() {
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to logout?",
                "Confirm Logout",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            this.dispose();

            // Open login window
            SwingUtilities.invokeLater(() -> {
                LoginFrame loginFrame = new LoginFrame(librarySystem);
                loginFrame.setVisible(true);
            });
        }
    }
}