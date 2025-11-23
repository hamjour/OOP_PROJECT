package ui;

import javax.swing.*;
import java.awt.*;
import core.Book;
import core.LibrarySystem;
import utils.Utils;

public class BookDetailsDialog extends JDialog {
    private Book book;
    private LibrarySystem librarySystem;

    public BookDetailsDialog(Frame parent, Book book, LibrarySystem system) {
        super(parent, "Book Details", true);
        this.book = book;
        this.librarySystem = system;
        setupUI();
    }

    private void setupUI() {
        setSize(450, 400);
        setLocationRelativeTo(getParent());
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(Utils.BG_PRIMARY);

        // TODO: Add title panel (NORTH)
        JPanel titlePanel = createTitlePanel();
        // TODO: Add details panel (CENTER)
        JPanel detailsPanel = createDetailsPanel();
        // TODO: Add button panel (SOUTH)
        JPanel btnPanel = createButtonPanel();

    }

    private JPanel createTitlePanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panel.setBackground(Utils.BG_PRIMARY);

        // TODO: Create title label with book title
        String bookTitle = book.getTitle();
        // TODO: Font: Monospace, BOLD, 20
        // TODO: Color: TEXT_PRIMARY

        return panel;
    }

    private JPanel createDetailsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Utils.BG_PRIMARY);
        panel.setBorder(Utils.addPadding(20, 30, 20, 30));

        // TODO: Add book information:
        //       - ISBN: [value]
        //       - Title: [value]
        //       - Author: [value]
        //       - Total Copies: [value]
        //       - Available Copies: [value]
        //       - Times Borrowed: [value]

        // TODO: Add availability status (large, colored)
        //       - If available: "AVAILABLE" in green (ACCENT)
        //       - If not: "NOT AVAILABLE" in red

        return panel;
    }

    private JLabel createInfoLabel(String label, String value) {
        JLabel infoLabel = new JLabel(label + " " + value);
        // TODO: Font: Monospace, PLAIN, 14
        // TODO: Color: TEXT_PRIMARY
        // TODO: Alignment: LEFT
        return infoLabel;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panel.setBackground(Utils.BG_PRIMARY);

        // TODO: Create "Close" button
        // TODO: Add action listener: dispose()

        return panel;
    }
}
