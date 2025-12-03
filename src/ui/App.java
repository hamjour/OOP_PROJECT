package ui;
import core.*;

import javax.swing.*;
public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                LibrarySystem system = new LibrarySystem();

                LoginFrame frame = new LoginFrame(system);
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

}
