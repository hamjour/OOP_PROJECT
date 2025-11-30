package core;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class Member {

    // Fields
    private String memberID;
    private String name;
    private String email;
    private ArrayList<String> borrowedBooks; // List of ISBNs

    // Constructors
    public Member() {
        // Default constructor, initialize borrowedBooks as empty ArrayList
        borrowedBooks = new ArrayList<>();
    }

    public Member(String memberID, String name, String email) {
        // Initialize all fields, borrowedBooks as empty ArrayList
        this.memberID = memberID;
        this.name = name;
        this.email = email;
        borrowedBooks = new ArrayList<>();
    }

    // Getters
    public String getMemberID() {
        return memberID;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }


    public ArrayList<String> getBorrowedBooks() {
        if (borrowedBooks == null) {
            borrowedBooks = new ArrayList<>();
        }
        return borrowedBooks;
    }

    // Setters
    public void setMemberID(String memberID) {
        this.memberID = memberID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setBorrowedBooks(ArrayList<String> borrowedBooks) {
        this.borrowedBooks = new ArrayList<>(borrowedBooks);
    }

    // Business Logic Methods

    public boolean addBorrowedBook(String isbn) {
        for (int i = 0; i < borrowedBooks.size(); i++){
            if (borrowedBooks.get(i).equals(isbn)){
                return false;
            }
        }
        borrowedBooks.add(isbn);
        return true;
    }

    public boolean removeBorrowedBook(String isbn) {
        return borrowedBooks.remove(isbn);
    }

    public int getBorrowedBooksCount() {
        return borrowedBooks.size();
    }

    public boolean hasBorrowedBook(String isbn) {
        return borrowedBooks.contains(isbn);
    }

    public boolean canBorrowMore(int maxLimit) {
        return borrowedBooks.size() < maxLimit;
    }

    public boolean hasAnyBorrowedBooks() {
        if (borrowedBooks == null) {  // ⭐ THIS IS THE FIX
            return false;
        }
        return !borrowedBooks.isEmpty();
    }

    // Validation Methods

    public boolean isValidEmail() {
        return email.contains("@");
    }

    // Utility Methods

    @Override
    public String toString() {
        return "Member Name: " + name + ", \n"
                + "MemberID: " + memberID + ", \n"
                + "Member Email: " + email;
    }

    @Override
    public boolean equals(Object obj) {
        return memberID.equals(obj);
    }

    @Override
    public int hashCode() {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        byte[] hash = md.digest(memberID.getBytes(StandardCharsets.UTF_8));
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.hashCode();
    }
}
