package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

import core.LibrarySystem;
import core.User;
import jdk.jshell.execution.Util;
import utils.Utils;

public class DashboardFrame extends JFrame {
    private LibrarySystem librarySystem;
    private User currentUser;

    private JPanel contentPanel;
    private CardLayout cardLayout;

    public DashboardFrame(LibrarySystem system, User user) {
        this.librarySystem = system;
        this.currentUser = user;
        setupUI();
    }

    private void setupUI() {
        setTitle("Dashboard");
        setSize(1200, 800);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);

        setLayout(new BorderLayout());

        JPanel topPanel = createTopPanel();
        add(topPanel, BorderLayout.NORTH);
        JPanel navPanel = createNavigationPanel();
        add(navPanel, BorderLayout.WEST);
        createContentPanel();
        add(contentPanel, BorderLayout.CENTER);
    }

    private JPanel createTopPanel() {
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Utils.BG_PRIMARY);
        topPanel.setBorder(Utils.addPadding(10, 10, 10, 10));

        JLabel userInfoLabel = new JLabel();
        userInfoLabel.setText(currentUser.getUsername());
        userInfoLabel.setForeground(Utils.TEXT_PRIMARY);
        userInfoLabel.setFont(new Font("Monospace", Font.BOLD, 14));
        topPanel.add(userInfoLabel, BorderLayout.WEST);

        JButton logoutBtn = new JButton();

        logoutBtn.setFont(new Font("MonoSpace", Font.BOLD, 16));
        logoutBtn.setText("LogOut");
        logoutBtn.setSize(25, 25);
        logoutBtn.addActionListener(e -> handleLogout());
        topPanel.add(logoutBtn, BorderLayout.EAST);

        return topPanel;
    }

    private JPanel createNavigationPanel() {
        JPanel navPanel = new JPanel();
        LayoutManager layout = new BoxLayout(navPanel, BoxLayout.Y_AXIS);
        navPanel.setLayout(layout);
        navPanel.setBackground(Utils.BG_SECONDARY);
        navPanel.setPreferredSize(new Dimension(200, 0));
        navPanel.setBorder(Utils.addPadding(10, 10, 10, 10));
        JButton searchBtn = createNavButton("Search Books", "search");
        JButton issueBtn = createNavButton("Issue Book", "issue");
        JButton returnBtn = createNavButton("Return Book", "return");
        JButton manageMembersBtn = createNavButton("Manage Members", "members");

        navPanel.add(searchBtn);
        navPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        navPanel.add(issueBtn);
        navPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        navPanel.add(returnBtn);
        navPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        navPanel.add(manageMembersBtn);

        return navPanel;
    }

    private JButton createNavButton(String text, String panelName) {
        JButton button = new JButton(text);
        button.setMaximumSize(new Dimension(180, 40));
        button.setPreferredSize(new Dimension(180, 40));
        button.setText(text);
        button.setFont(new Font("Monospace", Font.PLAIN, 16));
        button.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                cardLayout.show(contentPanel, panelName);
            }
        });
        return button;
    }

    private void createContentPanel() {
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(Utils.BG_SECONDARY);
        // TODO: Add panels:
         contentPanel.add(new SearchBookPanel(librarySystem), "search");
//         contentPanel.add(new IssueBookPanel(librarySystem), "issue");
//         contentPanel.add(new ReturnBookPanel(librarySystem), "return");
//         contentPanel.add(new MemberManagementPanel(librarySystem), "members");
        cardLayout.show(contentPanel, "search");
    }

    private void handleLogout() {
        int option = JOptionPane.showConfirmDialog(DashboardFrame.this, "sUrE YoU WaNt To logout??",
                "logout", JOptionPane.YES_NO_OPTION);

        if (option == JOptionPane.YES_OPTION){
            dispose();
            LoginFrame loginFrame = new LoginFrame(librarySystem);
            loginFrame.setVisible(true);
        }
    }
}
