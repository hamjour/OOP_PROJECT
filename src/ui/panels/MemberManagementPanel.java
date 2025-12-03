package ui.panels;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import core.LibrarySystem;
import core.Member;
import utils.Utils;

/**
 * Panel for managing library members
 */
public class MemberManagementPanel extends JPanel {

    private LibrarySystem librarySystem;
    private JTable membersTable;
    private DefaultTableModel tableModel;
    private JButton addButton;
    private JButton editButton;
    private JButton deleteButton;
    private JButton refreshButton;

    /*
     * Constructor - sets up the panel
     */
    public MemberManagementPanel(LibrarySystem system) {
        this.librarySystem = system;
        setupUI();
        loadMembers();
    }

    /*
     * Set up the user interface
     */
    private void setupUI() {
        setLayout(new BorderLayout(10, 10));
        setBackground(Utils.BG_PRIMARY);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Add title
        JPanel topPanel = createTopPanel();
        add(topPanel, BorderLayout.NORTH);

        // Add table
        JPanel centerPanel = createCenterPanel();
        add(centerPanel, BorderLayout.CENTER);

        // Add buttons
        JPanel bottomPanel = createBottomPanel();
        add(bottomPanel, BorderLayout.SOUTH);
    }

    /*
     * Create top panel with title
     */
    private JPanel createTopPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBackground(Utils.BG_PRIMARY);

        JLabel titleLabel = new JLabel("Manage Members");
        titleLabel.setFont(new Font("Monospace", Font.BOLD, 24));
        titleLabel.setForeground(Utils.TEXT_PRIMARY);

        panel.add(titleLabel);

        return panel;
    }

    /*
     * Create center panel with table
     */
    private JPanel createCenterPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Utils.BG_PRIMARY);

        // Create table
        String[] columns = {"Member ID", "Name", "Email", "Borrowed Books"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        membersTable = new JTable(tableModel);
        membersTable.setBackground(Utils.BG_SECONDARY);
        membersTable.setForeground(Utils.TEXT_PRIMARY);
        membersTable.setSelectionBackground(Utils.ACCENT);
        membersTable.setSelectionForeground(Utils.TEXT_PRIMARY);
        membersTable.setRowHeight(25);
        membersTable.setFillsViewportHeight(true);
        membersTable.setOpaque(true);

        // Style table header
        membersTable.getTableHeader().setBackground(Utils.BG_SECONDARY);
        membersTable.getTableHeader().setForeground(Utils.TEXT_PRIMARY);
        membersTable.getTableHeader().setOpaque(true);

        JScrollPane scrollPane = new JScrollPane(membersTable);
        scrollPane.getViewport().setBackground(Utils.BG_SECONDARY);
        scrollPane.setBorder(BorderFactory.createLineBorder(Utils.ACCENT, 1));
        scrollPane.setOpaque(true);

        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    /*
     * Create bottom panel with buttons
     */
    private JPanel createBottomPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBackground(Utils.BG_PRIMARY);

        addButton = new JButton("Add Member");
        addButton.addActionListener(e -> addMember());

        editButton = new JButton("Edit Member");
        editButton.addActionListener(e -> editMember());

        deleteButton = new JButton("Delete Member");
        deleteButton.addActionListener(e -> deleteMember());

        refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> loadMembers());

        panel.add(addButton);
        panel.add(editButton);
        panel.add(deleteButton);
        panel.add(refreshButton);

        return panel;
    }

    /*
     * Load all members into the table
     */
    private void loadMembers() {
        tableModel.setRowCount(0);

        List<Member> members = librarySystem.getAllMembers();

        for (Member member : members) {
            Object[] row = {
                    member.getMemberID(),
                    member.getName(),
                    member.getEmail(),
                    member.getBorrowedBooksCount()
            };

            tableModel.addRow(row);
        }
    }

    /*
     * Show dialog to add a new member
     */
    private void addMember() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Add Member", true);
        dialog.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Create input fields
        JTextField idField = new JTextField(20);
        JTextField nameField = new JTextField(20);
        JTextField emailField = new JTextField(20);

        // Add labels and fields
        gbc.gridx = 0;
        gbc.gridy = 0;
        dialog.add(new JLabel("Member ID:"), gbc);
        gbc.gridx = 1;
        dialog.add(idField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        dialog.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1;
        dialog.add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        dialog.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        dialog.add(emailField, gbc);

        // Create buttons
        JButton okButton = new JButton("OK");
        JButton cancelButton = new JButton("Cancel");

        gbc.gridx = 0;
        gbc.gridy = 4;
        dialog.add(okButton, gbc);
        gbc.gridx = 1;
        dialog.add(cancelButton, gbc);

        // OK button action
        okButton.addActionListener(e -> {
            String id = idField.getText().trim();
            String name = nameField.getText().trim();
            String email = emailField.getText().trim();

            if (id.isEmpty() || name.isEmpty() || email.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "All fields are required");
                return;
            }

            boolean success = librarySystem.addMember(id, name, email);

            if (success) {
                JOptionPane.showMessageDialog(dialog, "Member added successfully");
                loadMembers();
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, "Failed to add member");
            }
        });

        // Cancel button action
        cancelButton.addActionListener(e -> dialog.dispose());

        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    /*
     * Show dialog to edit selected member
     */
    private void editMember() {
        int selectedRow = membersTable.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a member first");
            return;
        }

        String memberID = (String) tableModel.getValueAt(selectedRow, 0);
        Member member = librarySystem.findMemberByID(memberID);

        if (member == null) {
            JOptionPane.showMessageDialog(this, "Member not found");
            return;
        }

        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Edit Member", true);
        dialog.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Create input fields with current values
        JTextField idField = new JTextField(member.getMemberID(), 20);
        idField.setEditable(false);
        JTextField nameField = new JTextField(member.getName(), 20);
        JTextField emailField = new JTextField(member.getEmail(), 20);

        // Add labels and fields
        gbc.gridx = 0;
        gbc.gridy = 0;
        dialog.add(new JLabel("Member ID:"), gbc);
        gbc.gridx = 1;
        dialog.add(idField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        dialog.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1;
        dialog.add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        dialog.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        dialog.add(emailField, gbc);

        // Create buttons
        JButton okButton = new JButton("OK");
        JButton cancelButton = new JButton("Cancel");

        gbc.gridx = 0;
        gbc.gridy = 4;
        dialog.add(okButton, gbc);
        gbc.gridx = 1;
        dialog.add(cancelButton, gbc);

        // OK button action
        okButton.addActionListener(e -> {
            String name = nameField.getText().trim();
            String email = emailField.getText().trim();

            if (name.isEmpty() || email.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "All fields are required");
                return;
            }

            boolean success = librarySystem.updateMember(memberID, name, email);

            if (success) {
                JOptionPane.showMessageDialog(dialog, "Member updated successfully");
                loadMembers();
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, "Failed to update member");
            }
        });

        // Cancel button action
        cancelButton.addActionListener(e -> dialog.dispose());

        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    /*
     * Delete selected member
     */
    private void deleteMember() {
        int selectedRow = membersTable.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a member first");
            return;
        }

        String memberID = (String) tableModel.getValueAt(selectedRow, 0);
        String memberName = (String) tableModel.getValueAt(selectedRow, 1);

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete " + memberName + "?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            boolean success = librarySystem.deleteMember(memberID);

            if (success) {
                JOptionPane.showMessageDialog(this, "Member deleted successfully");
                loadMembers();
            } else {
                JOptionPane.showMessageDialog(this, "Cannot delete member with borrowed books");
            }
        }
    }
}