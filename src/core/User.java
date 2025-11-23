package core;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class User {

    // Fields
    private String username;
    private String password;
    private String role;

    // Constructors
    public User() {
    }

    public User(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    // Getters
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }

    // Setters
    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRole(String role) {
        this.role = role;
    }

    // Business Logic Methods

    public boolean authenticate(String inputPassword) {
        return this.password.compareTo(inputPassword) == 0;
    }

    public boolean isAdmin() {
        return (role.equals("ADMIN"));
    }

    public boolean isMember() {
        return (role.equals("MEMBER"));
    }

    public boolean hasRole(String roleToCheck) {
        return role.compareToIgnoreCase(roleToCheck) == 0;
    }

    public void changePassword(String newPassword) {
        this.password = newPassword;
    }

    // Validation Methods

    public boolean isValidPassword(String password) {
        return password.length() >= 6;
    }

    public static boolean isValidRole(String role) {
        return (role.equals("ADMIN") || role.equals("MEMBER"));
    }

    // Utility Methods

    @Override
    public String toString() {
        return "Username: " + username + "\n"
                +"Role: " + role;
    }

    @Override
    public boolean equals(Object obj) {
        return username.equals(obj);
    }

    @Override
    public int hashCode() {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        byte[] hash = md.digest(username.getBytes(StandardCharsets.UTF_8));
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.hashCode();    }
}
