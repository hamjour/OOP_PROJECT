package ui;

import javax.swing.*;
import java.awt.*;
import core.LibrarySystem;
import core.Book;
import java.util.List;

public class SearchBookPanel extends JPanel {
    private JTextField searchField;
    private JComboBox<String> searchCriteriaCombo;
    private JButton searchButton;
    private JTable resultsTable;
    private LibrarySystem librarySystem;

    public SearchBookPanel(LibrarySystem system) {
        this.librarySystem = system;
        setupUI();
    }

    private void setupUI() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // TODO: Create top panel for "Enter Search Criteria"
        // TODO: Add label "Search by:"
        // TODO: Create combo box with options: "Title", "Author", "ISBN"
        // TODO: Add search text field (larger size)
        // TODO: Add search button
        // TODO: Arrange using FlowLayout or BoxLayout

        // TODO: Create center panel for "Display Results"
        // TODO: Create JTable with columns:
        //       - ISBN, Title, Author, Total Copies, Available Copies, Status
        // TODO: Add table to JScrollPane
        // TODO: Make table non-editable
        // TODO: Add double-click listener to show book details

        // TODO: Add panels to main panel
        // TODO: Style with borders and padding
    }

    private void performSearch() {
        // TODO: Get search query from text field
        // TODO: Get selected search criteria

        // TODO: Validate search query is not empty

        // TODO: Based on criteria, call:
        //       - librarySystem.searchByTitle(query)
        //       - librarySystem.searchByAuthor(query)
        //       - librarySystem.searchByISBN(query)

        // TODO: Display results in table
        // TODO: For each book, show:
        //       - ISBN, Title, Author, Total Copies, Available Copies
        //       - Status: "Available" (green) or "Unavailable" (red)

        // TODO: If no results, show "No books found" message
    }

    public void displayResults(List<Book> books) {
        // TODO: Clear existing table data
        // TODO: Add each book to table
        // TODO: Color code status column
    }
}
