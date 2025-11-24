package ui;
import core.*;

import javax.swing.*;
public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                LibrarySystem system = new LibrarySystem();

                // Create test user
                system.registerUser("test", "test123", "ADMIN");
                system.addBook("123", "Song of ice and fire", "GRR", 1);

                LoginFrame frame = new LoginFrame(system);
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

}
