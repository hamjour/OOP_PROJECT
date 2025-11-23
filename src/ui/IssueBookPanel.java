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

        // TODO: Member section
        //       - Label "Member ID:"
        memberNameLabel = new JLabel("Member ID:");
        //       - Text field for member ID
        memberIDField = new JTextField();
        //       - "Search Member" button
        searchMemberButton = new JButton("Search Member");
        //       - Display area showing member name (initially empty)
        JPanel memberPanel = new JPanel();

        JLabel foundmember = new JLabel(selectedMember.toString());

        searchMemberButton.addActionListener(e -> searchMember());
        memberPanel.add(foundmember);
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
        String memberID =  memberIDField.getText();

        // TODO: Validate not empty
        if(memberID.isEmpty()){
            JOptionPane.showMessageDialog(null, "Please enter a valid member ID");
            return;
        }

        // TODO: Call librarySystem.findMemberByID(memberID)
        selectedMember = librarySystem.findMemberByID(memberID);
        // TODO: If found:
        //       - Store member
        //       - Display member name
        //       - Check if member can borrow (< 3 books, no overdue)
        if(selectedMember.canBorrowMore(3) && selectedMember != null && selectedBook != null){
            issueButton.setEnabled(true);
            return;
        }
        //       - Enable issue button if both member and book are selected

        // TODO: If not found:
        //       - Show "Member not found" error
        JOptionPane.showMessageDialog(IssueBookPanel.this,"Member not found");
        //       - Clear member display
    }

    private void searchBook() {
        // TODO: Get ISBN from field
        String isbn =  isbnField.getText();
        // TODO: Validate not empty
        if(isbn.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please enter a valid member ID");
            return;
        }
        // TODO: Call librarySystem.findBookByISBN(isbn)
        selectedBook = librarySystem.searchByISBN(isbn);
        // TODO: If found:
        //       - Store book
        if(selectedMember != null && selectedBook != null) {
            issueButton.setEnabled(true);
            return;
        }
        //       - Display book title and author
        JOptionPane.showMessageDialog(IssueBookPanel.this,selectedBook.toString());
        //       - Check if book is available
        if(selectedBook.isAvailable()){
            issueButton.setEnabled(true);
            return;

        }
        JOptionPane.showMessageDialog(IssueBookPanel.this,"Book not found");

        //       - Enable issue button if both member and book are selected

        // TODO: If not found or unavailable:
        //       - Show error message
        //       - Clear book display
    }

    private void issueBook() {
        // TODO: Validate both member and book are selected
        if(selectedMember != null && selectedBook != null) {
            if(selectedBook.isAvailable() && selectedMember.canBorrowMore(3)){
                librarySystem.issueBook(selectedMember.getMemberID(), selectedBook.getIsbn());
                return;
            }
        }
        JOptionPane.showMessageDialog(IssueBookPanel.this,"Error IssuingBook");
        // TODO: Check book availability NO
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