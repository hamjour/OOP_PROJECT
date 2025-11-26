package core;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Book {

    // Fields
    private String isbn;
    private String title;
    private String author;
    private int totalCopies;
    private int availableCopies;
    private int timesBorrowed;

    // Constructors
    public Book() {
    }

    public Book(String isbn, String title, String author, int totalCopies) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.totalCopies = totalCopies;
        availableCopies = totalCopies;
        timesBorrowed = 0;
    }

    public Book(String isbn, String title, String author, int totalCopies,
                int availableCopies, int timesBorrowed) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.totalCopies = totalCopies;
        this.availableCopies = availableCopies;
        this.timesBorrowed = timesBorrowed;
    }

    // Getters
    public String getIsbn() {
        return isbn;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public int getTotalCopies() {
        return totalCopies;
    }

    public int getAvailableCopies() {
        return availableCopies;
    }

    public int getTimesBorrowed() {
        return timesBorrowed;
    }

    // Setters
    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setTotalCopies(int totalCopies) {
        this.totalCopies = totalCopies;
    }

    public void setAvailableCopies(int availableCopies) {
        this.availableCopies = availableCopies;
    }

    public void setTimesBorrowed(int timesBorrowed) {
        this.timesBorrowed = timesBorrowed;
    }

    // Business Logic Methods

    public boolean isAvailable() {
        return availableCopies > 0;
    }

    public int getBorrowedCopies() {
        return totalCopies - availableCopies;
    }

    public boolean borrowCopy() {
        if(availableCopies > 0){
            availableCopies--;
            timesBorrowed++;
            return true;
        }
        return false;
    }

    public boolean returnCopy() {
        if (availableCopies < totalCopies){
            availableCopies++;
            return true;
        }
        return false;
    }

    public double getAvailabilityPercentage() {
        return ((double) availableCopies / totalCopies) * 100;
    }

    // Utility Methods

    @Override
    public String toString() {
        return "Title: " + title + ", \n"
                + "Author: " + author + ", \n"
                + "Avaliable Copies: " + availableCopies;
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Book other = (Book) obj;
        return isbn.equals(other.isbn);
    }

    @Override
    public int hashCode() {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        byte[] hash = md.digest(isbn.getBytes(StandardCharsets.UTF_8));
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.hashCode();
    }
}