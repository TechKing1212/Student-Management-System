package project;

import com.formdev.flatlaf.FlatDarkLaf;

public class StartApp {
    public static void main(String[] args) {
        try {
            FlatDarkLaf.setup(); // Activates the modern dark UI theme
        } catch (Exception e) {
            e.printStackTrace();
        }

        new OpeningScreen(); // Launch the opening splash screen
    }
}
