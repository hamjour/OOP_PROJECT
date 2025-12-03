package ui.panels;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

import core.LibrarySystem;
import core.Member;
import core.Transaction;
import utils.Utils;

/**
 * Panel for returning books
 */
public class ReturnBookPanel extends JPanel {

    private LibrarySystem librarySystem;
    private JTextField memberIDField;
    private JButton searchButton;
    private JTable borrowedBooksTable;
    private DefaultTableModel tableModel;
    private JButton returnButton;
    private Member selectedMember;
    private List<Transaction> activeTransactions;

    /*
     * Constructor - sets up the panel
     */
    public ReturnBookPanel(LibrarySystem system) {
        this.librarySystem = system;
        setupUI();
    }

    /*
     * Set up the user interface
     */
    private void setupUI() {
        setLayout(new BorderLayout(10, 10));
        setBackground(Utils.BG_PRIMARY);
        setOpaque(true);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Add top panel
        JPanel topPanel = createTopPanel();
        add(topPanel, BorderLayout.NORTH);

        // Add table
        JPanel centerPanel = createCenterPanel();
        add(centerPanel, BorderLayout.CENTER);

        // Add button panel
        JPanel bottomPanel = createBottomPanel();
        add(bottomPanel, BorderLayout.SOUTH);
    }

    /*
     * Create top panel with search
     */
    private JPanel createTopPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Utils.BG_PRIMARY);

        // Title
        JLabel titleLabel = new JLabel("Return Book");
        titleLabel.setFont(new Font("Monospace", Font.BOLD, 24));
        titleLabel.setForeground(Utils.TEXT_PRIMARY);
        titleLabel.setAlignmentX(LEFT_ALIGNMENT);

        // Search panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBackground(Utils.BG_PRIMARY);
        searchPanel.setAlignmentX(LEFT_ALIGNMENT);

        JLabel memberLabel = new JLabel("Member ID:");
        memberLabel.setForeground(Utils.TEXT_PRIMARY);

        memberIDField = new JTextField(15);
        memberIDField.setBackground(Utils.BG_SECONDARY);
        memberIDField.setForeground(Utils.TEXT_PRIMARY);

        searchButton = new JButton("Search");
        searchButton.addActionListener(e -> searchMember());

        searchPanel.add(memberLabel);
        searchPanel.add(memberIDField);
        searchPanel.add(searchButton);

        panel.add(titleLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(searchPanel);

        return panel;
    }

    /*
     * Create center panel with table
     */
    private JPanel createCenterPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Utils.BG_PRIMARY);

        // Create table
        String[] columns = {"ISBN", "Book Title", "Issue Date", "Due Date", "Days Overdue", "Fine"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        borrowedBooksTable = new JTable(tableModel);
        borrowedBooksTable.setBackground(Utils.BG_SECONDARY);
        borrowedBooksTable.setForeground(Utils.TEXT_PRIMARY);
        borrowedBooksTable.setSelectionBackground(Utils.ACCENT);
        borrowedBooksTable.setSelectionForeground(Utils.TEXT_PRIMARY);
        borrowedBooksTable.setRowHeight(25);
        borrowedBooksTable.setFillsViewportHeight(true);
        borrowedBooksTable.setOpaque(true);

        // Style table header
        borrowedBooksTable.getTableHeader().setBackground(Utils.BG_SECONDARY);
        borrowedBooksTable.getTableHeader().setForeground(Utils.TEXT_PRIMARY);
        borrowedBooksTable.getTableHeader().setOpaque(true);

        JScrollPane scrollPane = new JScrollPane(borrowedBooksTable);
        scrollPane.getViewport().setBackground(Utils.BG_SECONDARY);
        scrollPane.getViewport().setOpaque(true);
        scrollPane.setBorder(BorderFactory.createLineBorder(Utils.ACCENT, 1));

        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    /*
     * Create bottom panel with return button
     */
    private JPanel createBottomPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panel.setBackground(Utils.BG_PRIMARY);

        returnButton = new JButton("Return Selected Book");
        returnButton.setEnabled(false);
        returnButton.addActionListener(e -> returnBook());

        panel.add(returnButton);

        return panel;
    }

    /*
     * Search for member and load their borrowed books
     */
    private void searchMember() {
        String memberID = memberIDField.getText().trim();

        if (memberID.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a member ID");
            return;
        }

        selectedMember = librarySystem.findMemberByID(memberID);

        if (selectedMember == null) {
            JOptionPane.showMessageDialog(this, "Member not found");
            tableModel.setRowCount(0);
            returnButton.setEnabled(false);
            return;
        }

        loadBorrowedBooks();
    }

    /*
     * Load borrowed books for the member
     */
    private void loadBorrowedBooks() {
        // Clear table first
        tableModel.setRowCount(0);

        // Get active transactions
        activeTransactions = librarySystem.getActiveTransactions(selectedMember.getMemberID());


        // Check if member has borrowed books
        if (activeTransactions == null || activeTransactions.isEmpty()) {
            JOptionPane.showMessageDialog(this, "This member has no borrowed books");
            returnButton.setEnabled(false);
            return;
        }

        // Add each transaction to table
        for (Transaction transaction : activeTransactions) {
            long daysOverdue = transaction.getDaysOverdue();
            double fine = librarySystem.calculateFine(transaction);

            Object[] row = {
                    transaction.getIsbn(),
                    transaction.getBookTitle(),
                    transaction.getIssueDate().toString(),
                    transaction.getDueDate().toString(),
                    daysOverdue,
                    String.format("$%.2f", fine)
            };

            tableModel.addRow(row);
        }

        // Enable return button
        returnButton.setEnabled(true);
    }

    /*
     * Return the selected book
     */
    private void returnBook() {
        int selectedRow = borrowedBooksTable.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a book to return");
            return;
        }

        // Get the transaction from the list
        Transaction transaction = activeTransactions.get(selectedRow);

        // Return the book
        Transaction returnedTransaction = librarySystem.returnBook(transaction.getTransactionID());

        if (returnedTransaction != null) {
            // Calculate fine
            double fine = returnedTransaction.getFine();

            // Show success message
            String message = "Book returned successfully!";
            if (fine > 0) {
                message += "\nFine: $" + String.format("%.2f", fine);
            }
            JOptionPane.showMessageDialog(this, message);

//            // Show receipt
//            Frame parent = (Frame) SwingUtilities.getWindowAncestor(this);
//            ReturnReceiptDialog dialog = new ReturnReceiptDialog(parent, returnedTransaction);
//            dialog.setVisible(true);

            // Refresh the table
            loadBorrowedBooks();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to return book");
        }
    }
}