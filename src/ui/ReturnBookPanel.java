package ui;

import javax.swing.*;
import java.awt.*;
import core.LibrarySystem;
import core.Member;
import core.Transaction;
import java.util.List;

public class ReturnBookPanel extends JPanel {
    private JTextField memberIDField;
    private JButton searchButton;
    private JTable borrowedBooksTable;
    private JButton returnButton;
    private LibrarySystem librarySystem;
    private Member selectedMember;
    private Transaction selectedTransaction;

    public ReturnBookPanel(LibrarySystem system) {
        this.librarySystem = system;
        setupUI();
    }

    private void setupUI() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // TODO: Create top panel for "Enter Book Details"
        //       - Label "Member ID:"
        //       - Text field for member ID
        //       - "Search" button to find member

        // TODO: Create center panel showing borrowed books
        //       - Label "Books Borrowed by This Member:"
        //       - JTable with columns:
        //         * ISBN
        //         * Book Title
        //         * Issue Date
        //         * Due Date
        //         * Days Overdue (show 0 if not overdue)
        //         * Fine Amount (show $0.00 if not overdue)
        //       - Make table scrollable
        //       - Add selection listener

        // TODO: Create bottom panel
        //       - "Return Selected Book" button (disabled initially)
        //       - Status label for messages

        // TODO: Style table:
        //       - Color code overdue rows in RED/PINK
        //       - Normal rows in default color
    }

    private void searchMember() {
        // TODO: Get member ID from field
        // TODO: Validate not empty

        // TODO: Call librarySystem.findMemberByID(memberID)

        // TODO: If found:
        //       - Store member
        //       - Get member's borrowed books (active transactions)
        //       - Display in table with calculated fines
        //       - Enable return button if books exist

        // TODO: If not found:
        //       - Show "Member not found" error
        //       - Clear table
    }

    private void loadBorrowedBooks() {
        // TODO: Get list of active transactions for member
        // TODO: For each transaction:
        //       - Get book details
        //       - Calculate days overdue (if any)
        //       - Calculate fine (e.g., $1 per day)
        //       - Add row to table

        // TODO: Color code overdue rows
    }

    private void returnBook() {
        // TODO: Get selected transaction from table
        // TODO: Validate a book is selected

        // TODO: Calculate final fine if overdue

        // TODO: Call librarySystem.returnBook(transaction)
        //       This should:
        //       - Update book status (increment available copies)
        //       - Mark transaction as complete
        //       - Record return date and fine

        // TODO: If successful:
        //       - Show success message
        //       - If fine > 0, display fine amount
        //       - Open ReturnReceiptDialog
        //       - Refresh borrowed books table

        // TODO: If failed:
        //       - Show error message
    }
}
