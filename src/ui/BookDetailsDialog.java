package ui;

import javax.swing.*;
import java.awt.*;
import core.Book;
import core.LibrarySystem;
import utils.Utils;

/**
 * Dialog to show detailed book information
 */
public class BookDetailsDialog extends JDialog {

    private Book book;
    private LibrarySystem librarySystem;

    /*
     * Constructor - sets up the dialog
     */
    public BookDetailsDialog(Frame parent, Book book, LibrarySystem system) {
        super(parent, "Book Details", true);
        this.book = book;
        this.librarySystem = system;
        setupUI();
    }

    /*
     * Set up the user interface
     */
    private void setupUI() {
        setSize(450, 400);
        setLocationRelativeTo(getParent());
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(Utils.BG_PRIMARY);

        // Add title
        JPanel titlePanel = createTitlePanel();
        add(titlePanel, BorderLayout.NORTH);

        // Add book details
        JPanel detailsPanel = createDetailsPanel();
        add(detailsPanel, BorderLayout.CENTER);

        // Add buttons
        JPanel buttonPanel = createButtonPanel();
        add(buttonPanel, BorderLayout.SOUTH);
    }

    /*
     * Create title panel
     */
    private JPanel createTitlePanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panel.setBackground(Utils.BG_SECONDARY);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel titleLabel = new JLabel(book.getTitle());
        titleLabel.setFont(new Font("Monospace", Font.BOLD, 20));
        titleLabel.setForeground(Utils.TEXT_PRIMARY);

        panel.add(titleLabel);

        return panel;
    }

    /*
     * Create details panel with book information
     */
    private JPanel createDetailsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Utils.BG_PRIMARY);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        // Add book details
        panel.add(createDetailLabel("ISBN: " + book.getIsbn()));
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(createDetailLabel("Title: " + book.getTitle()));
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(createDetailLabel("Author: " + book.getAuthor()));
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(createDetailLabel("Total Copies: " + book.getTotalCopies()));
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(createDetailLabel("Available Copies: " + book.getAvailableCopies()));
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(createDetailLabel("Times Borrowed: " + book.getTimesBorrowed()));
        panel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Add status
        String status = book.isAvailable() ? "AVAILABLE" : "NOT AVAILABLE";
        Color statusColor = book.isAvailable() ? Utils.ACCENT : Color.RED;

        JLabel statusLabel = new JLabel(status);
        statusLabel.setFont(new Font("Monospace", Font.BOLD, 18));
        statusLabel.setForeground(statusColor);
        statusLabel.setAlignmentX(LEFT_ALIGNMENT);

        panel.add(statusLabel);

        return panel;
    }

    /*
     * Create a detail label
     */
    private JLabel createDetailLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Monospace", Font.PLAIN, 14));
        label.setForeground(Utils.TEXT_PRIMARY);
        label.setAlignmentX(LEFT_ALIGNMENT);

        return label;
    }

    /*
     * Create button panel
     */
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panel.setBackground(Utils.BG_PRIMARY);

        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> dispose());

        panel.add(closeButton);

        return panel;
    }
}