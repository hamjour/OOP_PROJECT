package ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import core.LibrarySystem;
import core.Book;
import ui.*;
import utils.Utils;

/**
 * Panel for searching books in the library
 */
public class SearchBookPanel extends JPanel {

    private LibrarySystem librarySystem;
    private JTextField searchField;
    private JComboBox<String> searchTypeCombo;
    private JButton searchButton;
    private JTable resultsTable;
    private DefaultTableModel tableModel;

    /*
     * Constructor - sets up the panel
     */
    public SearchBookPanel(LibrarySystem system) {
        this.librarySystem = system;
        setupUI();
        loadAllBooks();
    }

    /*
     * Set up the user interface
     */
    private void setupUI() {
        setLayout(new BorderLayout(10, 10));
        setBackground(Utils.BG_PRIMARY);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Add title at top
        JPanel topPanel = createTopPanel();
        add(topPanel, BorderLayout.NORTH);

        // Add table in center
        JPanel tablePanel = createTablePanel();
        add(tablePanel, BorderLayout.CENTER);
    }

    /*
     * Create the top panel with title and search controls
     */
    private JPanel createTopPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Utils.BG_SECONDARY);

        // Title
        JLabel titleLabel = new JLabel("Search Books");
        titleLabel.setFont(new Font("Monospace", Font.BOLD, 24));
        titleLabel.setForeground(Utils.TEXT_PRIMARY);

        // Search controls
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBackground(Utils.BG_PRIMARY);

        JLabel searchByLabel = new JLabel("Search by:");
        searchByLabel.setForeground(Utils.TEXT_PRIMARY);

        String[] searchTypes = {"Title", "Author", "ISBN"};
        searchTypeCombo = new JComboBox<>(searchTypes);

        searchField = new JTextField(20);
        searchField.setBackground(Utils.BG_SECONDARY);
        searchField.setForeground(Utils.TEXT_PRIMARY);

        searchButton = new JButton("Search");
        searchButton.addActionListener(e -> performSearch());

        searchPanel.add(searchByLabel);
        searchPanel.add(searchTypeCombo);
        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        panel.add(titleLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(searchPanel);

        return panel;
    }

    /*
     * Create the table panel to display search results
     */
    /*
     * Create the table panel to display search results
     */
    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Utils.BG_PRIMARY);

        // Create table columns
        String[] columns = {"ISBN", "Title", "Author", "Total Copies", "Available", "Status"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        // Create table
        resultsTable = new JTable(tableModel);
        resultsTable.setBackground(Utils.BG_SECONDARY);
        resultsTable.setForeground(Utils.TEXT_PRIMARY);
        resultsTable.setSelectionBackground(Utils.ACCENT);
        resultsTable.setSelectionForeground(Utils.TEXT_PRIMARY);
        resultsTable.setRowHeight(25);
        resultsTable.setFillsViewportHeight(true);

        // Style table header
        resultsTable.getTableHeader().setBackground(Utils.BG_SECONDARY);
        resultsTable.getTableHeader().setForeground(Utils.TEXT_PRIMARY);

        // Add double-click listener
        resultsTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    showBookDetails();
                }
            }
        });

        // Add table to scroll pane
        JScrollPane scrollPane = new JScrollPane(resultsTable);
        scrollPane.getViewport().setBackground(Utils.BG_SECONDARY); // FIX: Set viewport background
        scrollPane.setBorder(BorderFactory.createLineBorder(Utils.ACCENT, 1));

        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }
    /*
     * Perform search based on user input
     */
    private void performSearch() {
        String query = searchField.getText().trim();

        if (query.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a search term");
            return;
        }

        String searchType = (String) searchTypeCombo.getSelectedItem();
        List<Book> results = null;

        if (searchType.equals("Title")) {
            results = librarySystem.searchByTitle(query);
        } else if (searchType.equals("Author")) {
            results = librarySystem.searchByAuthor(query);
        } else if (searchType.equals("ISBN")) {
            Book book = librarySystem.searchByISBN(query);
            if (book != null) {
                results = List.of(book);
            }
        }

        if (results == null || results.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No books found");
            tableModel.setRowCount(0);
        } else {
            displayResults(results);
        }
    }

    /*
     * Load and display all books
     */
    private void loadAllBooks() {
        List<Book> books = librarySystem.getAllBooks();
        displayResults(books);
    }

    /*
     * Display book results in the table
     */
    private void displayResults(List<Book> books) {
        tableModel.setRowCount(0);

        for (Book book : books) {
            String status = book.isAvailable() ? "Available" : "Unavailable";

            Object[] row = {
                    book.getIsbn(),
                    book.getTitle(),
                    book.getAuthor(),
                    book.getTotalCopies(),
                    book.getAvailableCopies(),
                    status
            };

            tableModel.addRow(row);
        }
    }

    /*
     * Show details dialog when book is double-clicked
     */
    private void showBookDetails() {
        int selectedRow = resultsTable.getSelectedRow();

        if (selectedRow == -1) {
            return;
        }

        String isbn = (String) tableModel.getValueAt(selectedRow, 0);
        Book book = librarySystem.searchByISBN(isbn);

        if (book != null) {
            Frame parent = (Frame) SwingUtilities.getWindowAncestor(this);
            BookDetailsDialog dialog = new BookDetailsDialog(parent, book, librarySystem);
            dialog.setVisible(true);
        }
    }
}