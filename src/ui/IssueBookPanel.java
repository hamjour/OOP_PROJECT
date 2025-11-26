package ui;

import javax.swing.*;
import java.awt.*;
import core.LibrarySystem;
import core.Member;
import core.Book;
import utils.Utils;

public class IssueBookPanel extends JPanel {
    private final JTextField memberIDField = new JTextField(12);
    private final JTextField isbnField = new JTextField(12);

    private final JButton searchMemberButton = new JButton("Search Member");
    private final JButton searchBookButton = new JButton("Search Book");
    private final JButton issueButton = new JButton("Issue Book");

    private final JLabel memberInfoLabel = new JLabel(""); // shows member name / info
    private final JLabel bookInfoLabel = new JLabel("");   // shows book title / info
    private final JLabel statusLabel = new JLabel(" ");    // shows short status messages

    private final LibrarySystem librarySystem;
    private Member selectedMember;
    private Book selectedBook;

    public IssueBookPanel(LibrarySystem system) {
        this.librarySystem = system;
        initColors();
        setupUI();
        wireActions();
        updateControls();
    }

    private void initColors() {
        setBackground(Utils.BG_PRIMARY);
        setForeground(Utils.TEXT_PRIMARY);

        // Buttons and fields — prefer using Utils constants
        searchMemberButton.setBackground(Utils.ACCENT);
        searchMemberButton.setForeground(Utils.TEXT_PRIMARY);

        searchBookButton.setBackground(Utils.ACCENT);
        searchBookButton.setForeground(Utils.TEXT_PRIMARY);

        issueButton.setBackground(Utils.BG_SECONDARY);
        issueButton.setForeground(Utils.TEXT_PRIMARY);

        memberIDField.setBackground(Utils.BG_SECONDARY);
        memberIDField.setForeground(Utils.TEXT_PRIMARY);

        isbnField.setBackground(Utils.BG_SECONDARY);
        isbnField.setForeground(Utils.TEXT_PRIMARY);

        memberInfoLabel.setForeground(Utils.TEXT_PRIMARY);
        bookInfoLabel.setForeground(Utils.TEXT_PRIMARY);
        statusLabel.setForeground(Utils.TEXT_SECONDARY);
    }

    private void setupUI() {
        setLayout(new BorderLayout(8, 8));
        setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        // Top: inputs and search buttons
        JPanel topPanel = new JPanel(new GridLayout(2, 1, 6, 6));
        topPanel.setOpaque(false);

        JPanel memberRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 6));
        memberRow.setOpaque(false);
        memberRow.add(new JLabel("Member ID:") {{
            setForeground(Utils.TEXT_PRIMARY);
        }});
        memberRow.add(memberIDField);
        memberRow.add(searchMemberButton);
        memberRow.add(memberInfoLabel);

        JPanel bookRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 6));
        bookRow.setOpaque(false);
        bookRow.add(new JLabel("Book ISBN:") {{
            setForeground(Utils.TEXT_PRIMARY);
        }});
        bookRow.add(isbnField);
        bookRow.add(searchBookButton);
        bookRow.add(bookInfoLabel);

        topPanel.add(memberRow);
        topPanel.add(bookRow);
        add(topPanel, BorderLayout.NORTH);

        // Center: status message
        JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        centerPanel.setOpaque(false);
        centerPanel.add(statusLabel);
        add(centerPanel, BorderLayout.CENTER);

        // Bottom: issue button
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setOpaque(false);
        issueButton.setPreferredSize(new Dimension(140, 36));
        bottomPanel.add(issueButton);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    // actions
    private void wireActions() {
        searchMemberButton.addActionListener(e -> searchMember());
        searchBookButton.addActionListener(e -> searchBook());
        issueButton.addActionListener(e -> issueBook());
    }

    // Search member by ID and display a simple result
    private void searchMember() {
        String memberID = memberIDField.getText().trim();
        if (memberID.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a member ID.", "Input required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        selectedMember = librarySystem.findMemberByID(memberID);

        if (selectedMember == null) {
            memberInfoLabel.setText("");
            statusLabel.setText("Member not found.");
            JOptionPane.showMessageDialog(this, "Member not found.", "Not found", JOptionPane.ERROR_MESSAGE);
        } else {
            memberInfoLabel.setText(selectedMember.toString());
            statusLabel.setText("Member found.");
        }
        updateControls();
    }

    // Search book by ISBN and display info
    private void searchBook() {
        String isbn = isbnField.getText().trim();
        if (isbn.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter an ISBN.", "Input required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        selectedBook = librarySystem.searchByISBN(isbn);

        if (selectedBook == null) {
            bookInfoLabel.setText("");
            statusLabel.setText("Book not found.");
            JOptionPane.showMessageDialog(this, "Book not found.", "Not found", JOptionPane.ERROR_MESSAGE);
        } else {
            bookInfoLabel.setText(selectedBook.toString());
            statusLabel.setText("Book found. Available: " + selectedBook.isAvailable());
        }
        updateControls();
    }

    // Enable/disable Issue button and show helpful status
    private void updateControls() {
        boolean ok = false;
        if (selectedMember == null) {
            statusLabel.setText("Select a member.");
        }
        if (selectedBook == null) {
            statusLabel.setText(selectedMember == null ? "Select a member and a book." : "Select a book.");
        }
        if (selectedMember != null && selectedBook != null) {
            boolean bookAvailable = selectedBook.isAvailable();
            boolean memberCanBorrow = selectedMember.canBorrowMore(3);

            if (!bookAvailable) {
                statusLabel.setText("Selected book is not available.");
            } else if (!memberCanBorrow) {
                statusLabel.setText("Member cannot borrow more books.");
            } else {
                statusLabel.setText("Ready to issue. Click Issue Book.");
                ok = true;
            }
        }
        issueButton.setEnabled(ok);
    }

    // Issue the book — simple flow for beginners
    private void issueBook() {
        if (selectedMember == null || selectedBook == null) {
            JOptionPane.showMessageDialog(this, "Select both member and book first.", "Missing data", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!selectedBook.isAvailable()) {
            JOptionPane.showMessageDialog(this, "Book is not available.", "Unavailable", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!selectedMember.canBorrowMore(3)) {
            JOptionPane.showMessageDialog(this, "Member reached borrow limit.", "Limit reached", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            librarySystem.issueBook(selectedMember.getMemberID(), selectedBook.getIsbn());

            JOptionPane.showMessageDialog(this, "Book issued successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);

            selectedMember = null;
            selectedBook = null;
            memberIDField.setText("");
            isbnField.setText("");
            memberInfoLabel.setText("");
            bookInfoLabel.setText("");
            statusLabel.setText("Book issued. You can search again.");
            updateControls();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Failed to issue book: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}