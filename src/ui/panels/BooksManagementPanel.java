package ui.panels;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import core.LibrarySystem;
import core.Book;
import utils.Utils;

/**
 * Panel for managing books
 */
public class BooksManagementPanel extends JPanel {

    private LibrarySystem librarySystem;
    private JTable booksTable;
    private DefaultTableModel tableModel;
    private JButton addButton;
    private JButton editButton;
    private JButton deleteButton;
    private JButton refreshButton;

    /*
     * Constructor - sets up the panel
     */
    public BooksManagementPanel(LibrarySystem system) {
        this.librarySystem = system;
        setupUI();
        loadBooks();
    }

    /*
     * Set up the user interface
     */
    private void setupUI() {
        setLayout(new BorderLayout(10, 10));
        setBackground(Utils.BG_PRIMARY);
        setOpaque(true);
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

        JLabel titleLabel = new JLabel("Manage Books");
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
        String[] columns = {"ISBN", "Title", "Author", "Total Copies", "Available", "Times Borrowed"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        booksTable = new JTable(tableModel);
        booksTable.setBackground(Utils.BG_SECONDARY);
        booksTable.setForeground(Utils.TEXT_PRIMARY);
        booksTable.setSelectionBackground(Utils.ACCENT);
        booksTable.setSelectionForeground(Utils.TEXT_PRIMARY);
        booksTable.setRowHeight(25);
        booksTable.setFillsViewportHeight(true);
        booksTable.setOpaque(true);

        // Style table header
        booksTable.getTableHeader().setBackground(Utils.BG_SECONDARY);
        booksTable.getTableHeader().setForeground(Utils.TEXT_PRIMARY);
        booksTable.getTableHeader().setOpaque(true);

        JScrollPane scrollPane = new JScrollPane(booksTable);
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

        addButton = new JButton("Add Book");
        addButton.addActionListener(e -> addBook());

        editButton = new JButton("Edit Book");
        editButton.addActionListener(e -> editBook());

        deleteButton = new JButton("Delete Book");
        deleteButton.addActionListener(e -> deleteBook());

        refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> loadBooks());

        panel.add(addButton);
        panel.add(editButton);
        panel.add(deleteButton);
        panel.add(refreshButton);

        return panel;
    }

    /*
     * Load all books into the table
     */
    private void loadBooks() {
        tableModel.setRowCount(0);

        List<Book> books = librarySystem.getAllBooks();

        for (Book book : books) {
            Object[] row = {
                    book.getIsbn(),
                    book.getTitle(),
                    book.getAuthor(),
                    book.getTotalCopies(),
                    book.getAvailableCopies(),
                    book.getTimesBorrowed()
            };

            tableModel.addRow(row);
        }
    }

    /*
     * Show dialog to add a new book
     */
    private void addBook() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Add Book", true);
        dialog.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Create input fields
        JTextField isbnField = new JTextField(20);
        JTextField titleField = new JTextField(20);
        JTextField authorField = new JTextField(20);
        JTextField copiesField = new JTextField(20);

        // Add labels and fields
        gbc.gridx = 0; gbc.gridy = 0;
        dialog.add(new JLabel("ISBN:"), gbc);
        gbc.gridx = 1;
        dialog.add(isbnField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        dialog.add(new JLabel("Title:"), gbc);
        gbc.gridx = 1;
        dialog.add(titleField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        dialog.add(new JLabel("Author:"), gbc);
        gbc.gridx = 1;
        dialog.add(authorField, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        dialog.add(new JLabel("Total Copies:"), gbc);
        gbc.gridx = 1;
        dialog.add(copiesField, gbc);

        // Create buttons
        JButton okButton = new JButton("OK");
        JButton cancelButton = new JButton("Cancel");

        gbc.gridx = 0; gbc.gridy = 4;
        dialog.add(okButton, gbc);
        gbc.gridx = 1;
        dialog.add(cancelButton, gbc);

        // OK button action
        okButton.addActionListener(e -> {
            String isbn = isbnField.getText().trim();
            String title = titleField.getText().trim();
            String author = authorField.getText().trim();
            String copiesStr = copiesField.getText().trim();

            if (isbn.isEmpty() || title.isEmpty() || author.isEmpty() || copiesStr.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "All fields are required");
                return;
            }

            try {
                int copies = Integer.parseInt(copiesStr);
                boolean success = librarySystem.addBook(isbn, title, author, copies);

                if (success) {
                    JOptionPane.showMessageDialog(dialog, "Book added successfully");
                    loadBooks();
                    dialog.dispose();
                } else {
                    JOptionPane.showMessageDialog(dialog, "Failed to add book");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Copies must be a number");
            }
        });

        // Cancel button action
        cancelButton.addActionListener(e -> dialog.dispose());

        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    /*
     * Show dialog to edit selected book
     */
    private void editBook() {
        int selectedRow = booksTable.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a book first");
            return;
        }

        String isbn = (String) tableModel.getValueAt(selectedRow, 0);
        Book book = librarySystem.searchByISBN(isbn);

        if (book == null) {
            JOptionPane.showMessageDialog(this, "Book not found");
            return;
        }

        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Edit Book", true);
        dialog.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Create input fields with current values
        JTextField isbnField = new JTextField(book.getIsbn(), 20);
        isbnField.setEditable(false);
        JTextField titleField = new JTextField(book.getTitle(), 20);
        JTextField authorField = new JTextField(book.getAuthor(), 20);
        JTextField copiesField = new JTextField(String.valueOf(book.getTotalCopies()), 20);

        // Add labels and fields
        gbc.gridx = 0; gbc.gridy = 0;
        dialog.add(new JLabel("ISBN:"), gbc);
        gbc.gridx = 1;
        dialog.add(isbnField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        dialog.add(new JLabel("Title:"), gbc);
        gbc.gridx = 1;
        dialog.add(titleField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        dialog.add(new JLabel("Author:"), gbc);
        gbc.gridx = 1;
        dialog.add(authorField, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        dialog.add(new JLabel("Total Copies:"), gbc);
        gbc.gridx = 1;
        dialog.add(copiesField, gbc);

        // Create buttons
        JButton okButton = new JButton("OK");
        JButton cancelButton = new JButton("Cancel");

        gbc.gridx = 0; gbc.gridy = 4;
        dialog.add(okButton, gbc);
        gbc.gridx = 1;
        dialog.add(cancelButton, gbc);

        // OK button action
        okButton.addActionListener(e -> {
            String title = titleField.getText().trim();
            String author = authorField.getText().trim();
            String copiesStr = copiesField.getText().trim();

            if (title.isEmpty() || author.isEmpty() || copiesStr.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "All fields are required");
                return;
            }

            try {
                int copies = Integer.parseInt(copiesStr);
                boolean success = librarySystem.updateBook(isbn, title, author, copies);

                if (success) {
                    JOptionPane.showMessageDialog(dialog, "Book updated successfully");
                    loadBooks();
                    dialog.dispose();
                } else {
                    JOptionPane.showMessageDialog(dialog, "Failed to update book");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Copies must be a number");
            }
        });

        // Cancel button action
        cancelButton.addActionListener(e -> dialog.dispose());

        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    /*
     * Delete selected book
     */
    private void deleteBook() {
        int selectedRow = booksTable.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a book first");
            return;
        }

        String isbn = (String) tableModel.getValueAt(selectedRow, 0);
        String title = (String) tableModel.getValueAt(selectedRow, 1);

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete " + title + "?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            boolean success = librarySystem.deleteBook(isbn);

            if (success) {
                JOptionPane.showMessageDialog(this, "Book deleted successfully");
                loadBooks();
            } else {
                JOptionPane.showMessageDialog(this, "Cannot delete book with borrowed copies");
            }
        }
    }
}