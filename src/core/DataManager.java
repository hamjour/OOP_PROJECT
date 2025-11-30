package core;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonSerializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonParseException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class DataManager {

    // File paths
    private static final String DATA_DIR = "data/";
    private static final String BOOKS_FILE = DATA_DIR + "books.json";
    private static final String MEMBERS_FILE = DATA_DIR + "members.json";
    private static final String TRANSACTIONS_FILE = DATA_DIR + "transactions.json";
    private static final String USERS_FILE = DATA_DIR + "users.json";

    // Gson instance for JSON serialization
    private Gson gson;

    // Constructor
    public DataManager() {
        gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .create();
        createDataDirectory();
    }

    // ==================== DIRECTORY SETUP ====================

    /**
     * Create data directory if it doesn't exist
     */
    private void createDataDirectory() {
        File dataDir = new File(DATA_DIR);
        if (!dataDir.exists()){
            boolean result = dataDir.mkdirs();
            if (!result){
                System.out.println("Failed to create data directory");
            }
        }
    }

    // ==================== SAVE METHODS ====================

    /**
     * Save books list to JSON file
     */
    public boolean saveBooks(List<Book> books) {
        String jsonBooks = gson.toJson(books);
        return writeToFile(BOOKS_FILE, jsonBooks);
    }

    /**
     * Save members list to JSON file
     */
    public boolean saveMembers(List<Member> members) {
        String jsonMembers = gson.toJson(members);
        return writeToFile(MEMBERS_FILE, jsonMembers);
    }

    /**
     * Save transactions list to JSON file
     */
    public boolean saveTransactions(List<Transaction> transactions) {
        String jsonTransactions = gson.toJson(transactions);
        return writeToFile(TRANSACTIONS_FILE, jsonTransactions);
    }

    /**
     * Save users list to JSON file
     */
    public boolean saveUsers(ArrayList<User> users) {
        String jsonUsers = gson.toJson(users);
        return writeToFile(USERS_FILE, jsonUsers);
    }

    // ==================== LOAD METHODS ====================

    /**
     * Load books from JSON file
     * @return List of books, or empty list if file doesn't exist
     */
    public ArrayList<Book> loadBooks() throws FileNotFoundException {
        if (!fileExists(BOOKS_FILE)) {
            return new ArrayList<>();
        }

        String data = readFromFile(BOOKS_FILE);
        if (data == null || data.isEmpty()) {
            return new ArrayList<>();
        }

        ArrayList<Book> books = gson.fromJson(data, new TypeToken<ArrayList<Book>>(){}.getType());
        return books != null ? books : new ArrayList<>();
    }

    /**
     * Load members from JSON file
     */
    public ArrayList<Member> loadMembers() throws FileNotFoundException {
        if (!fileExists(MEMBERS_FILE)) {
            return new ArrayList<>();
        }

        String data = readFromFile(MEMBERS_FILE);
        if (data == null || data.isEmpty()) {
            return new ArrayList<>();
        }

        ArrayList<Member> members = gson.fromJson(data, new TypeToken<ArrayList<Member>>(){}.getType());
        return members != null ? members : new ArrayList<>();
    }

    /**
     * Load transactions from JSON file
     */
    public ArrayList<Transaction> loadTransactions() throws FileNotFoundException {
            if (!fileExists(TRANSACTIONS_FILE)) {
                return new ArrayList<>();
            }

            String data = readFromFile(TRANSACTIONS_FILE); // Use helper method
            if (data == null || data.isEmpty()) {
                return new ArrayList<>();
            }

            ArrayList<Transaction> transactions = gson.fromJson(data, new TypeToken<ArrayList<Transaction>>(){}.getType());
            return transactions != null ? transactions : new ArrayList<>();
    }

    /**
     * Load users from JSON file
     */
    public ArrayList<User> loadUsers() throws FileNotFoundException {
            if (!fileExists(USERS_FILE)) {
                return new ArrayList<>();
            }

            String data = readFromFile(USERS_FILE); // Use helper method
            if (data == null || data.isEmpty()) {
                return new ArrayList<>();
            }

            ArrayList<User> users = gson.fromJson(data, new TypeToken<ArrayList<User>>(){}.getType());
            return users != null ? users : new ArrayList<>();
    }

    // ==================== HELPER METHODS ====================

    /**
     * Write string content to a file
     */
    private boolean writeToFile(String filename, String content) {
         try (FileWriter writer = new FileWriter(filename)) {
             writer.write(content);
         } catch (IOException e) {
             return false;
         }
         return true;
    }

    /**
     * Read entire file content as string
     */
    private String readFromFile(String filename) {
         try {
             return Files.readString(Paths.get(filename));
         } catch (IOException e) {
             System.err.println("Error reading " + filename);
             return null;
         }
    }

    /**
     * Check if a file exists
     */
    private boolean fileExists(String filename) {
        File file = new File(filename);
        return file.exists();
    }

    /**
     * Check if data directory is empty (first time setup)
     */
    public boolean isFirstRun() {
        return !fileExists(USERS_FILE);
    }
    private static class LocalDateAdapter implements JsonSerializer<LocalDate>, JsonDeserializer<LocalDate> {
        private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;

        @Override
        public JsonElement serialize(LocalDate date, java.lang.reflect.Type typeOfSrc,
                                     com.google.gson.JsonSerializationContext context) {
            return new JsonPrimitive(date.format(formatter)); // "yyyy-MM-dd"
        }

        @Override
        public LocalDate deserialize(JsonElement json, java.lang.reflect.Type typeOfT,
                                     com.google.gson.JsonDeserializationContext context)
                throws JsonParseException {
            return LocalDate.parse(json.getAsString(), formatter);
        }
    }
}

