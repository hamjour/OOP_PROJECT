package ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import core.LibrarySystem;
import core.Member;
import utils.Utils;

public class MemberManagementPanel extends JPanel {
    private LibrarySystem librarySystem;

    private JTable membersTable;
    private DefaultTableModel tableModel;
    private JButton addButton;
    private JButton editButton;
    private JButton deleteButton;
    private JButton refreshButton;

    public MemberManagementPanel(LibrarySystem system) {
        this.librarySystem = system;
        setupUI();
        loadMembers();
    }

    private void setupUI() {
        setLayout(new BorderLayout(10, 10));
        setBackground(Utils.BG_PRIMARY);
        setBorder(Utils.addPadding(20, 20, 20, 20));

        // TODO: Add title panel (NORTH)
        // TODO: Add table panel (CENTER)
        // TODO: Add button panel (SOUTH)
    }

    private JPanel createTitlePanel() {
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        // TODO: Background color

        // TODO: Create title label "Manage Members"
        // TODO: Font: Monospace, BOLD, 24
        // TODO: Color: TEXT_PRIMARY

        return titlePanel;
    }

    private JPanel createTablePanel() {
        JPanel tablePanel = new JPanel(new BorderLayout());
        // TODO: Background color

        // TODO: Create table model with columns:
        //       "Member ID", "Name", "Email", "Borrowed Books"

        // TODO: Create JTable with table model
        // TODO: Make non-editable
        // TODO: Set selection mode (single row)
        // TODO: Style table (colors, font)

        // TODO: Add table to JScrollPane
        // TODO: Add scroll pane to panel

        return tablePanel;
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        // TODO: Background color

        // TODO: Create buttons: Add, Edit, Delete, Refresh
        // TODO: Add action listeners
        // TODO: Style buttons

        return buttonPanel;
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        // TODO: Font, colors, size
        // TODO: Hover effect
        return button;
    }

    private void loadMembers() {
        // TODO: Clear table
        // TODO: Get all members from librarySystem.getAllMembers()
        // TODO: Loop through members and add to table:
        //       - memberID
        //       - name
        //       - email
        //       - borrowedBooks.size() + " books"
    }

    private void handleAdd() {
        // TODO: Create dialog with input fields:
        //       - Member ID
        //       - Name
        //       - Email

        // TODO: Validate inputs
        // TODO: Call librarySystem.addMember()
        // TODO: If success: refresh table, show success message
        // TODO: If fail: show error message
    }

    private void handleEdit() {
        // TODO: Get selected row from table
        // TODO: If no selection: show error "Select a member first"

        // TODO: Get member ID from selected row
        // TODO: Find member using librarySystem.findMemberByID()

        // TODO: Create dialog with pre-filled fields
        // TODO: Update member info
        // TODO: Refresh table
    }

    private void handleDelete() {
        // TODO: Get selected row
        // TODO: If no selection: show error

        // TODO: Get member ID
        // TODO: Show confirmation dialog

        // TODO: If confirmed:
        //       - Call librarySystem.deleteMember()
        //       - If success: refresh table
        //       - If fail: show error (might have borrowed books)
    }

    private void handleRefresh() {
        // TODO: Just call loadMembers()
    }
}
