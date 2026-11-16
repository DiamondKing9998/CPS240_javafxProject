package com.dynamicformbuilder;

import javafx.scene.text.Font;

import java.io.IOException;
import java.io.InputStream;

public class FontLoader {

    private static final String FONT_DIRECTORY = "/com/dynamicformbuilder/fonts/";
    private static final String[] FONT_FILES = { "BerlinSansFB.ttf", "BerlinSansFBBold.ttf", "BerlinSansFBDemiBold.ttf" };

    // Registers the bundled fonts with JavaFX so FXML/CSS can refer to them by family name
    // (e.g. "Berlin Sans FB"). Must be called before any scene that uses them is loaded.
    public static void loadFonts() {
        for (String fontFile : FONT_FILES) {
            try (InputStream stream = FontLoader.class.getResourceAsStream(FONT_DIRECTORY + fontFile)) {
                if (stream == null) {
                    System.out.println("Font resource not found: " + fontFile);
                    continue;
                }
                Font font = Font.loadFont(stream, 12);
                if (font == null) {
                    System.out.println("Failed to load font: " + fontFile);
                } else {
                    System.out.println("Loaded font: " + font.getName() + " (family: " + font.getFamily() + ")");
                }
            } catch (IOException e) {
                System.out.println("Error reading font " + fontFile + ": " + e.getMessage());
            }
        }
    }
}
