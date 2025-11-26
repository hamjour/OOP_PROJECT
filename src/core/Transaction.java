package core;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.Period;

public class Transaction {

    // Fields
    private String transactionID;
    private String memberID;
    private String memberName;
    private String isbn;
    private String bookTitle;
    private LocalDate issueDate;
    private LocalDate dueDate;
    private LocalDate returnDate;
    private double fine;
    private boolean isActive;

    // Constructors
    public Transaction() {
    }

    public Transaction(String transactionID, String memberID, String memberName,
                       String isbn, String bookTitle, LocalDate issueDate,
                       LocalDate dueDate) {
        this.transactionID = transactionID;
        this.memberID = memberID;
        this.memberName = memberName;
        this.isbn = isbn;
        this.bookTitle = bookTitle;
        this.issueDate = issueDate;
        this.dueDate = dueDate;
        returnDate = null;
        fine = 0;
        isActive = true;
    }

    // Getters
    public String getTransactionID() {
        return transactionID;
    }

    public String getMemberID() {
        return memberID;
    }

    public String getMemberName() {
        return memberName;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public LocalDate getIssueDate() {
        return issueDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public double getFine() {
        return fine;
    }

    public boolean isActive() {
        return isActive;
    }

    // Setters
    public void setTransactionID(String transactionID) {
        this.transactionID = transactionID;
    }

    public void setMemberID(String memberID) {
        this.memberID = memberID;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public void setIssueDate(LocalDate issueDate) {
        this.issueDate = issueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    public void setFine(double fine) {
        this.fine = fine;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    // Business Logic Methods

    public boolean isOverdue() {
        return dueDate.isBefore(LocalDate.now());
    }

    public long getDaysOverdue() {
        if (isOverdue()){
            return LocalDate.now().toEpochDay() - dueDate.toEpochDay();
        }
        return 0;
    }
    public long getDaysUntilDue() {
        if (!isOverdue()){
            return dueDate.toEpochDay() - LocalDate.now().toEpochDay();
        }
        return 0;
    }

    public double calculateCurrentFine(double finePerDay) {
        if (isOverdue()) {
            return (getDaysOverdue()) * finePerDay;
        }
        return 0.0;
    }

    public void markAsReturned(LocalDate returnDate, double calculatedFine) {
        this.returnDate = returnDate;
        this.fine = calculatedFine;
        this.isActive = false;
    }

    public String getStatus() {
        if (!isActive) {
            return "RETURNED";
        }else if (isOverdue()){
            return "OVERDUE";
        }else {
            return "ACTIVE";
        }
    }

    // Utility Methods

    @Override
    public String toString() {
        return "Transaction ID: " + transactionID + ", \n"
                + "Member Name: " + memberName + ", \n"
                + "Member ID: " + memberID + ", \n"
                + "Book Title: " + bookTitle + ", \n"
                + "Book ISBN: " + isbn + ", \n"
                + "Issue Date: " + issueDate + ", \n"
                + "Due Date: " + dueDate + ", \n"
                + "Return Date: " + returnDate + ", \n"
                + "Active: " + isActive + ", \n";
    }

    @Override
    public boolean equals(Object obj) {
        return transactionID.equals(obj);
    }

    @Override
    public int hashCode() {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        byte[] hash = md.digest(transactionID.getBytes(StandardCharsets.UTF_8));
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.hashCode();
    }
}