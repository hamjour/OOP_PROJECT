package core;

import javax.xml.datatype.Duration;
import java.io.FileNotFoundException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.Period;
import java.util.*;
import java.util.stream.Collectors;

public class LibrarySystem {

    // Data storage
    private ArrayList<Book> books;
    private ArrayList<Member> members;
    private ArrayList<Transaction> transactions;
    private ArrayList<User> users;

    private DataManager dataManager;

    // Business rules constants
    private static final int MAX_BORROW_LIMIT = 3;
    private static final int LOAN_PERIOD_DAYS = 14;
    private static final double FINE_PER_DAY = 1.0;

    // Constructor
    public LibrarySystem() throws FileNotFoundException {
        books = new ArrayList<>();
        users = new ArrayList<>();
        members = new ArrayList<>();
        transactions = new ArrayList<>();
        this.dataManager = new DataManager();
        loadAllData();
    }

    // ==================== AUTHENTICATION ====================

    /**
     * Authenticate user with username and password
     * @return User object if valid, null if invalid
     */
    public User login(String username, String password) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getUsername().equals(username)){
                if (users.get(i).getPassword().equals(password)){
                    return users.get(i);
                }
            }
        }
        return null;
    }

    /**
     * Register a new user account
     * @return true if successful, false if username already exists
     */
    public boolean registerUser(String username, String password, String role) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getUsername().equals(username)){
                return false;
            }
        }

        User user = new User(username, password, role);
        users.add(user);

        return dataManager.saveUsers(users);
    }

    // ==================== BOOK OPERATIONS ====================

    /**
     * Get all books in the library
     */
    public ArrayList<Book> getAllBooks() {
        return books;
    }

    /**
     * Search books by title (case-insensitive, partial match)
     */
    public ArrayList<Book> searchByTitle(String title) {
        return books.stream()
                .filter(book -> book.getTitle().toLowerCase(Locale.ROOT)
                        .contains(title.toLowerCase(Locale.ROOT)))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Search books by author (case-insensitive, partial match)
     */
    public ArrayList<Book> searchByAuthor(String author) {
        return books.stream()
                .filter(book -> book.getAuthor().toLowerCase()
                        .contains(author.toLowerCase()))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Search book by exact ISBN
     */
    public Book searchByISBN(String isbn) {
        return (Book) books.stream().filter(book -> book.getIsbn().equals(isbn)).findFirst().orElse(null);
    }

    /**
     * Add a new book to the library
     */
    public boolean addBook(String isbn, String title, String author, int totalCopies) {
        for (int i = 0; i < books.size(); i++) {
            if (books.get(i).getIsbn().equals(isbn)) {
                return false;
            }
        }
        Book book = new Book(isbn, title, author, totalCopies);
        books.add(book);

        return dataManager.saveBooks(books);
    }

    /**
     * Update existing book information
     */
    public boolean updateBook(String isbn, String newTitle, String newAuthor, int newTotalCopies) {
        // TODO: Find book by ISBN
        for (int i = 0; i < books.size(); i++) {
            if (books.get(i).getIsbn().equals(isbn)) {
                if(newTotalCopies >= books.get(i).getTotalCopies() - books.get(i).getAvailableCopies()){
                    books.get(i).setAuthor(newAuthor);
                    books.get(i).setTitle(newTitle);
                    books.get(i).setTotalCopies(newTotalCopies);
                }
            }
        }

        return dataManager.saveBooks(books);
    }

    /**
     * Delete a book from the library
     */
    public boolean deleteBook(String isbn) {
        for (int i = 0; i < books.size(); i++) {
            if (books.get(i).getIsbn().equals(isbn)) {
                if(books.get(i).getAvailableCopies() < books.get(i).getTotalCopies()){
                    return false;
                }
                books.remove(books.get(i));
            }
        }
        return dataManager.saveBooks(books);
    }

    // ==================== MEMBER OPERATIONS ====================

    /**
     * Get all members
     */
    public ArrayList<Member> getAllMembers() {
        return members;
    }

    /**
     * Find member by ID
     */
    public Member findMemberByID(String memberID) {
        for (int i = 0; i < members.size(); i++) {
            if (members.get(i).getMemberID().equals(memberID)){
                return members.get(i);
            }
        }
        return null;
    }

    /**
     * Add a new member
     */
    public boolean addMember(String memberID, String name, String email) {
        for (int i = 0; i < members.size(); i++) {
            if (members.get(i).getMemberID().equals(memberID)){
                return false;
            }
        }
        Member member = new Member(memberID, name, email);
        members.add(member);
        return dataManager.saveMembers(members);
    }

    /**
     * Update member information
     */
    public boolean updateMember(String memberID, String newName, String newEmail) {
        for (int i = 0; i < members.size(); i++) {
            if (members.get(i).getMemberID().equals(memberID)){
                members.get(i).setName(newName);
                members.get(i).setEmail(newEmail);
            }
        }
        return dataManager.saveMembers(members);
    }

    /**
     * Delete a member
     */

    public boolean deleteMember(String memberID) {
        for (int i = 0; i < members.size(); i++) {
            if (members.get(i).getMemberID().equals(memberID)){
                if (!members.get(i).getBorrowedBooks().isEmpty()) { // ✅ Has books
                    return false;
                }
                Member toRemove = members.get(i);
                members.remove(i);
                boolean saved = dataManager.saveMembers(members);

                if (!saved) {
                    members.add(i, toRemove);
                    return false;
                }
                return true;
            }
        }
        return false;
    }

    // ==================== TRANSACTION OPERATIONS ====================

    /**
     * Issue a book to a member
     * @return Transaction object if successful, null if failed
     */
    public Transaction issueBook(String memberID, String isbn) {
        Member member = findMemberByID(memberID);
        Book book = searchByISBN(isbn);

        // Validation checks
        if (member == null) {
            System.err.println("Error: Member not found");
            return null;
        }

        if (book == null) {
            System.err.println("Error: Book not found");
            return null;
        }

        if (!book.isAvailable()) {
            System.err.println("Error: Book not available");
            return null;
        }

        if (!member.canBorrowMore(MAX_BORROW_LIMIT)) {
            System.err.println("Error: Member has reached borrowing limit");
            return null;
        }

        if (hasOverdueBooks(memberID)) {
            System.err.println("Error: Member has overdue books");
            return null;
        }

        // All checks passed - create transaction
        Transaction transaction = new Transaction(
                UUID.randomUUID().toString(),
                memberID,
                member.getName(),
                isbn,
                book.getTitle(),
                LocalDate.now(),
                calculateDueDate()
        );

        book.borrowCopy();

        member.addBorrowedBook(isbn);

        transactions.add(transaction);

        saveAllData();

        return transaction;
    }

    /**
     * Return a book
     * @return Transaction object with fine calculated, null if failed
     */
    public Transaction returnBook(String transactionID) {
        for (Transaction transaction : transactions) {
            if (transaction.getTransactionID().equals(transactionID)) {
                if (transaction.isActive()) {
                    Book book = searchByISBN(transaction.getIsbn());
                    Member member = findMemberByID(transaction.getMemberID());

                    double fine = calculateFine(transaction);

                    transaction.markAsReturned(LocalDate.now(), fine);

                    book.returnCopy();

                    member.removeBorrowedBook(transaction.getIsbn());

                    saveAllData();

                    return transaction;
                }
            }
        }

        return null;
    }

    /**
     * Get all active transactions for a member
     */
    public ArrayList<Transaction> getActiveTransactions(String memberID) {
        return transactions.stream()
                .filter(transaction -> transaction.getMemberID().equals(memberID) && transaction.isActive())
                .collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Get all transactions (active and completed)
     */
    public ArrayList<Transaction> getAllTransactions() {
        return transactions;
    }

    // ==================== VALIDATION & BUSINESS LOGIC ====================

    /**
     * Check if member can borrow more books
     */
    public boolean canMemberBorrow(String memberID) {
        Member member = findMemberByID(memberID);
        if (member.canBorrowMore(MAX_BORROW_LIMIT)){
            return true;
        }
        return false;
    }

    /**
     * Check if member has overdue books
     */
    public boolean hasOverdueBooks(String memberID) {
        ArrayList<Transaction> memberTransactions = transactions.stream()
                .filter(transaction -> transaction.getMemberID().equals(memberID) && transaction.isActive())
                .collect(Collectors.toCollection(ArrayList::new));

        for (Transaction transaction : memberTransactions) {
            if (transaction.isOverdue()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Calculate fine for a transaction
     */
    public double calculateFine(Transaction transaction) {
        return transaction.calculateCurrentFine(FINE_PER_DAY);
    }

    /**
     * Calculate days overdue for a transaction
     */
    public long calculateDaysOverdue(Transaction transaction) {
        return transaction.getDaysOverdue();
    }

    /**
     * Get due date for a new issue (today + loan period)
     */
    public LocalDate calculateDueDate() {
        return LocalDate.now().plusDays(LOAN_PERIOD_DAYS);
    }

    // ==================== STATISTICS (Optional) ====================

    /**
     * Get total number of books
     */
    public int getTotalBooksCount() {
        return books.size();
    }

    /**
     * Get number of available books (sum of all availableCopies)
     */
    public long getAvailableBooksCount() {
        long count = 0;
        for (Book book : books){
            count += book.getAvailableCopies();
        }
        return count;
    }

    /**
     * Get number of borrowed books
     */
    public int getBorrowedBooksCount() {
        long count = 0;
        for (Transaction transaction : transactions){
            if (transaction.isActive()) count++;
        }
        return 0;
    }

    /**
     * Get most borrowed books (top N)
     */
    public ArrayList<Book> getMostBorrowedBooks(int limit) {
        return books.stream()
                .sorted(Comparator.comparing(Book::getTimesBorrowed).reversed()) // reversed for descending order
                .limit(limit)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    // ==================== HELPER METHODS ====================

    /**
     * Save all data to files
     */
    private void saveAllData() {
        dataManager.saveMembers(members);
        dataManager.saveBooks(books);
        dataManager.saveTransactions(transactions);
        dataManager.saveUsers(users);
    }

    /**
     * Load all data from files
     */
    private void loadAllData() throws FileNotFoundException {
        assert dataManager != null;
        books = dataManager.loadBooks();
        members = dataManager.loadMembers();
        transactions = dataManager.loadTransactions();
        users = dataManager.loadUsers();
    }

    /**
     * Initialize default data (first time setup)
     */
    private void initializeDefaultData() {
        User defaultUser = new User("admin", "admin123", "ADMIN");
        Book defaultBook = new Book("isbn", "GOT", "Grr", 1);
        saveAllData();
    }
}