package ui;

import javax.swing.*;
import java.awt.*;
import core.LibrarySystem;
import core.Member;
import core.Book;
import core.Transaction;
import utils.Utils;

/**
 * Panel for issuing books to members
 */
public class IssueBookPanel extends JPanel {

    private LibrarySystem librarySystem;
    private JTextField memberIDField;
    private JTextField isbnField;
    private JButton searchMemberButton;
    private JButton searchBookButton;
    private JButton issueButton;
    private JLabel memberInfoLabel;
    private JLabel bookInfoLabel;
    private Member selectedMember;
    private Book selectedBook;

    /*
     * Constructor - sets up the panel
     */
    public IssueBookPanel(LibrarySystem system) {
        this.librarySystem = system;
        setupUI();
    }

    /*
     * Set up the user interface
     */
    private void setupUI() {
        setLayout(new BorderLayout(10, 10));
        setBackground(Utils.BG_PRIMARY);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Add title
        JPanel topPanel = createTopPanel();
        add(topPanel, BorderLayout.NORTH);

        // Add input fields
        JPanel centerPanel = createCenterPanel();
        add(centerPanel, BorderLayout.CENTER);

        // Add issue button
        JPanel bottomPanel = createBottomPanel();
        add(bottomPanel, BorderLayout.SOUTH);
    }

    /*
     * Create top panel with title
     */
    private JPanel createTopPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBackground(Utils.BG_PRIMARY);

        JLabel titleLabel = new JLabel("Issue Book");
        titleLabel.setFont(new Font("Monospace", Font.BOLD, 24));
        titleLabel.setForeground(Utils.TEXT_PRIMARY);

        panel.add(titleLabel);

        return panel;
    }

    /*
     * Create center panel with input fields
     */
    private JPanel createCenterPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Utils.BG_PRIMARY);

        // Member section
        JPanel memberPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        memberPanel.setBackground(Utils.BG_PRIMARY);

        JLabel memberLabel = new JLabel("Member ID:");
        memberLabel.setForeground(Utils.TEXT_PRIMARY);

        memberIDField = new JTextField(15);
        memberIDField.setBackground(Utils.BG_SECONDARY);
        memberIDField.setForeground(Utils.TEXT_PRIMARY);

        searchMemberButton = new JButton("Search Member");
        searchMemberButton.addActionListener(e -> searchMember());

        memberInfoLabel = new JLabel("");
        memberInfoLabel.setForeground(Utils.TEXT_PRIMARY);

        memberPanel.add(memberLabel);
        memberPanel.add(memberIDField);
        memberPanel.add(searchMemberButton);
        memberPanel.add(memberInfoLabel);

        // Book section
        JPanel bookPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bookPanel.setBackground(Utils.BG_PRIMARY);

        JLabel bookLabel = new JLabel("Book ISBN:");
        bookLabel.setForeground(Utils.TEXT_PRIMARY);

        isbnField = new JTextField(15);
        isbnField.setBackground(Utils.BG_SECONDARY);
        isbnField.setForeground(Utils.TEXT_PRIMARY);

        searchBookButton = new JButton("Search Book");
        searchBookButton.addActionListener(e -> searchBook());

        bookInfoLabel = new JLabel("");
        bookInfoLabel.setForeground(Utils.TEXT_PRIMARY);

        bookPanel.add(bookLabel);
        bookPanel.add(isbnField);
        bookPanel.add(searchBookButton);
        bookPanel.add(bookInfoLabel);

        panel.add(memberPanel);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(bookPanel);

        return panel;
    }

    /*
     * Create bottom panel with issue button
     */
    private JPanel createBottomPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panel.setBackground(Utils.BG_PRIMARY);

        issueButton = new JButton("Issue Book");
        issueButton.setEnabled(false);
        issueButton.addActionListener(e -> issueBook());

        panel.add(issueButton);

        return panel;
    }

    /*
     * Search for member by ID
     */
    private void searchMember() {
        String memberID = memberIDField.getText().trim();

        if (memberID.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a member ID");
            return;
        }

        selectedMember = librarySystem.findMemberByID(memberID);

        if (selectedMember == null) {
            memberInfoLabel.setText("Not found");
            JOptionPane.showMessageDialog(this, "Member not found");
        } else {
            memberInfoLabel.setText("Found: " + selectedMember.getName());
        }

        updateIssueButton();
    }

    /*
     * Search for book by ISBN
     */
    private void searchBook() {
        String isbn = isbnField.getText().trim();

        if (isbn.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter an ISBN");
            return;
        }

        selectedBook = librarySystem.searchByISBN(isbn);

        if (selectedBook == null) {
            bookInfoLabel.setText("Not found");
            JOptionPane.showMessageDialog(this, "Book not found");
        } else {
            bookInfoLabel.setText("Found: " + selectedBook.getTitle());
        }

        updateIssueButton();
    }

    /*
     * Update issue button based on selections
     */
    private void updateIssueButton() {
        boolean canIssue = false;

        if (selectedMember != null && selectedBook != null) {
            if (selectedBook.isAvailable() && selectedMember.canBorrowMore(3)) {
                canIssue = true;
            }
        }

        issueButton.setEnabled(canIssue);
    }

    /*
     * Issue the book to the member
     */
    private void issueBook() {
        if (selectedMember == null || selectedBook == null) {
            JOptionPane.showMessageDialog(this, "Please select both member and book");
            return;
        }

        Transaction transaction = librarySystem.issueBook(
                selectedMember.getMemberID(),
                selectedBook.getIsbn()
        );

        if (transaction != null) {
            JOptionPane.showMessageDialog(this, "Book issued successfully!");

            // Show receipt
//            Frame parent = (Frame) SwingUtilities.getWindowAncestor(this);
//            IssueReceiptDialog dialog = new IssueReceiptDialog(parent, transaction);
//            dialog.setVisible(true);

            // Clear fields
            memberIDField.setText("");
            isbnField.setText("");
            memberInfoLabel.setText("");
            bookInfoLabel.setText("");
            selectedMember = null;
            selectedBook = null;
            updateIssueButton();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to issue book");
        }
    }
}