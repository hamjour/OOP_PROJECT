package ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import core.LibrarySystem;
import core.Book;
import ui.BookDetailsDialog;
import utils.Utils;


public class SearchBookPanel extends JPanel {
    private JTextField searchField;
    private JComboBox<String> searchCriteriaCombo;
    private JButton searchButton;
    private JTable resultsTable;
    private DefaultTableModel tableModel;
    private LibrarySystem librarySystem;

    /**
     * Constructor.
     * TODO: initialize librarySystem and call setupUI().
     */
    public SearchBookPanel(LibrarySystem system) {
        this.librarySystem = system;
        // TODO: call setupUI() after you implement it
        // setupUI();
        // TODO: once implemented, you may want to call loadAllBooks() to show initial data
        // loadAllBooks();
    }

    /**
     * TODO 1: Build the top-level UI:
     *  - set layout and background (use Utils constants if available)
     *  - create a title JLabel
     *  - add a search panel (call createSearchPanel())
     *  - add a table panel (call createTablePanel())
     *
     * HINT:
     *  - use BorderLayout for this panel
     *  - put title and search panel in NORTH and table panel in CENTER
     */
    private void setupUI() {
        // TODO: Implement layout, styling and add components
        // Example steps:
        // 1) setLayout(new BorderLayout(10, 10));
        // 2) setBackground(Utils.BG_PRIMARY);
        // 3) add(titlePanel, BorderLayout.NORTH);
        // 4) add(createTablePanel(), BorderLayout.CENTER);
    }

    /**
     * TODO 2: Create and return a JPanel that contains:
     *  - JLabel "Search by:"
     *  - JComboBox with options {"Title", "Author", "ISBN"}
     *  - JTextField for input
     *  - JButton "Search" which triggers performSearch()
     *
     * HINTS:
     *  - set fonts/colors using Utils constants if available
     *  - add an ActionListener to the searchButton:
     *      searchButton.addActionListener(e -> performSearch());
     *  - also add ActionListener to the searchField so Enter triggers search:
     *      searchField.addActionListener(e -> performSearch());
     */
    private JPanel createSearchPanel() {
        // TODO: build and return the search panel
        // Return an empty panel for now so the file compiles
        return new JPanel();
    }

    /**
     * TODO 3: Create and return the table panel:
     *  - Create DefaultTableModel with columns:
     *      {"ISBN", "Title", "Author", "Total Copies", "Available", "Status"}
     *  - Create a JTable using the model and style it
     *  - Add a MouseListener to detect double-clicks and call handleDoubleClick()
     *  - Put the table into a JScrollPane and return a JPanel containing it
     *
     * HINT:
     *  - Override isCellEditable in the model to return false
     *  - Example for double-click:
     *      resultsTable.addMouseListener(new MouseAdapter() {
     *          public void mouseClicked(MouseEvent e) {
     *              if (e.getClickCount() == 2) handleDoubleClick();
     *          }
     *      });
     */
    private JPanel createTablePanel() {
        // TODO: implement table creation and return the panel
        // Minimal placeholder so compilation is straightforward:
        JPanel panel = new JPanel(new BorderLayout());
        tableModel = new DefaultTableModel(new String[]{"ISBN", "Title", "Author", "Total Copies", "Available", "Status"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // TODO: keep this behavior when you implement the real model
            }
        };
        resultsTable = new JTable(tableModel);
        panel.add(new JScrollPane(resultsTable), BorderLayout.CENTER);
        return panel;
    }

    /**
     * TODO 4: Implement the search logic:
     *  - Read query from searchField (trim whitespace)
     *  - If empty: show a JOptionPane warning and return
     *  - Determine search criteria from searchCriteriaCombo
     *  - Use librarySystem to perform the search:
     *      - Title: librarySystem.searchByTitle(query) -> List<Book>
     *      - Author: librarySystem.searchByAuthor(query) -> List<Book>
     *      - ISBN: librarySystem.searchByISBN(query) -> Book (wrap into a List if not null)
     *  - Call displayResults(results)
     *
     * HINT:
     *  - Show an information dialog if no results found:
     *      JOptionPane.showMessageDialog(this, "No books found ...", "No Results", JOptionPane.INFORMATION_MESSAGE);
     */
    private void performSearch() {
        // TODO: implement search behavior
    }

    /**
     * TODO 5: Load and display all books from the librarySystem.
     *  - Get books via librarySystem.getAllBooks()
     *  - Pass them to displayResults()
     */
    private void loadAllBooks() {
        // TODO: implement loading and display of all books
    }

    /**
     * TODO 6: Given a list of Book objects, update the table model:
     *  - Clear existing rows (tableModel.setRowCount(0))
     *  - For each book add a row with fields:
     *      isbn, title, author, totalCopies, availableCopies, status (Available/Unavailable)
     *
     * HINT:
     *  - Example:
     *      tableModel.addRow(new Object[] {
     *          book.getIsbn(),
     *          book.getTitle(),
     *          book.getAuthor(),
     *          book.getTotalCopies(),
     *          book.getAvailableCopies(),
     *          book.isAvailable() ? "Available" : "Unavailable"
     *      });
     */
    public void displayResults(List<Book> books) {
        // TODO: implement table population
    }

    /**
     * TODO 7: Handle a double-click on a table row:
     *  - Get selected row index using resultsTable.getSelectedRow()
     *  - Get the ISBN from tableModel.getValueAt(row, 0)
     *  - Look up the Book with librarySystem.searchByISBN(isbn)
     *  - If found, open BookDetailsDialog with the book and librarySystem:
     *      Frame parentFrame = (Frame) SwingUtilities.getWindowAncestor(this);
     *      BookDetailsDialog dialog = new BookDetailsDialog(parentFrame, book, librarySystem);
     *      dialog.setVisible(true);
     *  - After dialog closes, refresh the list: loadAllBooks()
     *
     * HINT:
     *  - Check for selectedRow == -1 (nothing selected) before proceeding
     *  - Show an error dialog if book not found
     */
    private void handleDoubleClick() {
        // TODO: implement double-click behavior
    }
}