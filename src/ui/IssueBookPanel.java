package ui;

import javax.swing.*;
import java.awt.*;
import core.LibrarySystem;
import core.Member;
import core.Book;

public class IssueBookPanel extends JPanel {
    private JTextField memberIDField, isbnField;
    private JButton searchMemberButton, searchBookButton, issueButton;
    private JLabel memberNameLabel, bookTitleLabel, statusLabel;
    private LibrarySystem librarySystem;
    private Member selectedMember;
    private Book selectedBook;

    public IssueBookPanel(LibrarySystem system) {
        this.librarySystem = system;
        setupUI();
    }

    private void setupUI() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // TODO: Create top panel for "Enter Book and Member Details"

        // TODO: Member section:
        //       - Label "Member ID:"
        //       - Text field for member ID
        //       - "Search Member" button
        //       - Display area showing member name (initially empty)

        // TODO: Book section:
        //       - Label "Book ISBN:"
        //       - Text field for ISBN
        //       - "Search Book" button
        //       - Display area showing book title (initially empty)

        // TODO: Create center panel showing selected details
        //       - Show member info when found
        //       - Show book info when found
        //       - Show availability status

        // TODO: Create bottom panel
        //       - Large "Issue Book" button (disabled initially)
        //       - Status label for messages

        // TODO: Arrange everything nicely with proper spacing
    }

    private void searchMember() {
        // TODO: Get member ID from field
        // TODO: Validate not empty

        // TODO: Call librarySystem.findMemberByID(memberID)

        // TODO: If found:
        //       - Store member
        //       - Display member name
        //       - Check if member can borrow (< 3 books, no overdue)
        //       - Enable issue button if both member and book are selected

        // TODO: If not found:
        //       - Show "Member not found" error
        //       - Clear member display
    }

    private void searchBook() {
        // TODO: Get ISBN from field
        // TODO: Validate not empty

        // TODO: Call librarySystem.findBookByISBN(isbn)

        // TODO: If found:
        //       - Store book
        //       - Display book title and author
        //       - Check if book is available
        //       - Enable issue button if both member and book are selected

        // TODO: If not found or unavailable:
        //       - Show error message
        //       - Clear book display
    }

    private void issueBook() {
        // TODO: Validate both member and book are selected

        // TODO: Check book availability
        // TODO: Check member borrowing limit
        // TODO: Check if member has overdue books

        // TODO: If all checks pass:
        //       - Call librarySystem.issueBook(memberID, isbn)
        //       - This should return a Transaction object

        // TODO: If successful:
        //       - Show success message
        //       - Open IssueReceiptDialog (pass transaction)
        //       - Clear all fields and selections

        // TODO: If failed:
        //       - Show specific error message
    }
}
