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

        JPanel titlePanel = new JPanel();
        JPanel detailsPanel = new JPanel();
        JPanel buttonPanel = new JPanel();
        add(titlePanel, BorderLayout.NORTH);
        add(detailsPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private JPanel createTitlePanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panel.setBackground(Utils.BG_PRIMARY);

        JLabel titleLabel = new JLabel(book.getTItle());
        titleLabel.setFont(new Font("Monospace", Font.BOLD, 20));
        titleLabel.setForeground(Utils.TEXT_PRIMARY);

        return panel;
    }

    private JPanel createDetailsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Utils.BG_PRIMARY);
        panel.setBorder(Utils.addPadding(20, 30, 20, 30));

        JLabel bookDetailsLabel = new JLabel(book.toString());
        panel.add(bookDetailsLabel);

        return panel;
    }

    private JLabel createInfoLabel(String label, String value) {
        JLabel infoLabel = new JLabel(label + " " + value);
        infoLabel.setFont(new Font("Monospace", Font.PLAIN, 14));
        infoLabel.setForeground(Utils.TEXT_PRIMARY);

        infoLabel.setHorizontalAlignment(JLabel.LEFT);
        return infoLabel;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panel.setBackground(Utils.BG_PRIMARY);

        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> dispose());

        return panel;
    }
}