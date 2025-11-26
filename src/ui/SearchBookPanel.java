package ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import core.LibrarySystem;
import core.Book;
import ui.BookDetailsDialog;
import utils.Utils;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class SearchBookPanel extends JPanel {
    private JTextField searchField;
    private JComboBox<String> searchCriteriaCombo;
    private JButton searchButton;
    private JTable resultsTable;
    private DefaultTableModel tableModel;
    private LibrarySystem librarySystem;

    public SearchBookPanel(LibrarySystem system) {
        this.librarySystem = system;
        setupUI();
        loadAllBooks(); // Show all books initially
    }

    private void setupUI() {
        setLayout(new BorderLayout(10, 10));
        setBackground(Utils.BG_PRIMARY);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setOpaque(true);

        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        titlePanel.setBackground(Utils.BG_PRIMARY);
        titlePanel.setOpaque(true);
        JLabel titleLabel = new JLabel("Search Books");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        titleLabel.setForeground(Utils.TEXT_PRIMARY);
        titlePanel.add(titleLabel);

        JPanel topContainer = new JPanel();
        topContainer.setLayout(new BoxLayout(topContainer, BoxLayout.Y_AXIS));
        topContainer.setBackground(Utils.BG_PRIMARY);
        topContainer.setOpaque(true);
        topContainer.add(titlePanel);
        topContainer.add(Box.createRigidArea(new Dimension(0, 10)));
        topContainer.add(createSearchPanel());

        add(topContainer, BorderLayout.NORTH);
        add(createTablePanel(), BorderLayout.CENTER);
    }

    private JPanel createSearchPanel() {
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        searchPanel.setBackground(Utils.BG_PRIMARY);
        searchPanel.setOpaque(true);

        JLabel searchByLabel = new JLabel("Search by:");
        searchByLabel.setForeground(Utils.TEXT_SECONDARY);
        searchByLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));

        String[] options = {"Title", "Author", "ISBN"};
        searchCriteriaCombo = new JComboBox<>(options);
        searchCriteriaCombo.setFont(new Font("SansSerif", Font.PLAIN, 14));
        searchCriteriaCombo.setForeground(Utils.TEXT_PRIMARY);
        searchCriteriaCombo.setBackground(Utils.BG_SECONDARY);
        searchCriteriaCombo.setOpaque(true);
        searchCriteriaCombo.setBorder(BorderFactory.createLineBorder(Utils.ACCENT, 1));

        searchField = new JTextField(24);
        searchField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        searchField.setForeground(Utils.TEXT_PRIMARY);
        searchField.setBackground(Utils.BG_SECONDARY);
        searchField.setCaretColor(Utils.TEXT_PRIMARY);
        searchField.setOpaque(true);
        searchField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Utils.ACCENT, 1),
                BorderFactory.createEmptyBorder(4, 6, 4, 6)
        ));

        searchButton = new JButton("Search");
        searchButton.setFont(new Font("SansSerif", Font.PLAIN, 14));
        searchButton.setForeground(Utils.TEXT_PRIMARY);
        searchButton.setBackground(Utils.ACCENT);
        searchButton.setFocusPainted(false);
        searchButton.setOpaque(true);
        searchButton.setBorder(BorderFactory.createLineBorder(Utils.PINE_TEAL.darker(), 1));
        searchButton.addActionListener(e -> performSearch());

        searchField.addActionListener(e -> performSearch());

        searchPanel.add(searchByLabel);
        searchPanel.add(searchCriteriaCombo);
        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        return searchPanel;
    }

    private JPanel createTablePanel() {
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(Utils.BG_PRIMARY);
        tablePanel.setOpaque(true);

        String[] columns = {"ISBN", "Title", "Author", "Total Copies", "Available", "Status"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        // Create table
        resultsTable = new JTable(tableModel);
        resultsTable.setFont(new Font("SansSerif", Font.PLAIN, 12));
        resultsTable.setForeground(Utils.TEXT_PRIMARY);
        resultsTable.setBackground(Utils.BG_SECONDARY);
        resultsTable.setSelectionBackground(Utils.ACCENT);
        resultsTable.setSelectionForeground(Utils.TEXT_PRIMARY);
        resultsTable.setRowHeight(25);
        resultsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        resultsTable.setFillsViewportHeight(true); // fill with background color

        // Style table header
        resultsTable.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 12));
        resultsTable.getTableHeader().setForeground(Utils.TEXT_PRIMARY);
        resultsTable.getTableHeader().setBackground(Utils.BG_SECONDARY);
        resultsTable.getTableHeader().setOpaque(true);

        // ⭐ KEY: Add double-click listener
        resultsTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) { // Double-click detected
                    handleDoubleClick();
                }
            }
        });

        // Scroll pane
        JScrollPane scrollPane = new JScrollPane(resultsTable);
        scrollPane.getViewport().setBackground(Utils.BG_PRIMARY);
        scrollPane.setBorder(BorderFactory.createLineBorder(Utils.ACCENT, 1));
        scrollPane.setOpaque(true);

        tablePanel.add(scrollPane, BorderLayout.CENTER);

        return tablePanel;
    }

    private void performSearch() {
        String query = searchField.getText().trim();

        if (query.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please enter a search term",
                    "Empty Search",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        String criteria = (String) searchCriteriaCombo.getSelectedItem();
        List<Book> results = null;

        // Search based on criteria
        switch (criteria) {
            case "Title":
                results = librarySystem.searchByTitle(query);
                break;
            case "Author":
                results = librarySystem.searchByAuthor(query);
                break;
            case "ISBN":
                Book book = librarySystem.searchByISBN(query);
                if (book != null) {
                    results = List.of(book);
                }
                break;
        }

        // Display results
        if (results == null || results.isEmpty()) {
            tableModel.setRowCount(0); // Clear table
            JOptionPane.showMessageDialog(this,
                    "No books found matching: " + query,
                    "No Results",
                    JOptionPane.INFORMATION_MESSAGE);
        } else {
            displayResults(results);
        }
    }

    private void loadAllBooks() {
        ArrayList<Book> allBooks = librarySystem.getAllBooks();
        displayResults(allBooks);
    }

    public void displayResults(List<Book> books) {
        // Clear existing data
        tableModel.setRowCount(0);

        for (Book book : books) {
            String status = book.isAvailable() ? "Available" : "Unavailable";

            tableModel.addRow(new Object[]{
                    book.getIsbn(),
                    book.getTitle(),
                    book.getAuthor(),
                    book.getTotalCopies(),
                    book.getAvailableCopies(),
                    status
            });
        }
    }

    private void handleDoubleClick() {
        int selectedRow = resultsTable.getSelectedRow();

        if (selectedRow == -1) {
            return; // No row selected
        }

        String isbn = (String) tableModel.getValueAt(selectedRow, 0);

        Book book = librarySystem.searchByISBN(isbn);

        if (book != null) {
            Frame parentFrame = (Frame) SwingUtilities.getWindowAncestor(this);

            BookDetailsDialog dialog = new BookDetailsDialog(parentFrame, book, librarySystem);
            dialog.setVisible(true);

            loadAllBooks();
        } else {
            JOptionPane.showMessageDialog(this,
                    "Book not found",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}