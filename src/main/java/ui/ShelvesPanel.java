package ui;

import service.LibraryService;

import javax.swing.*;
import java.awt.*;

public class ShelvesPanel extends JPanel {
    private final LibraryService libraryService;

    public ShelvesPanel(LibraryService libraryService) {
        this.libraryService = libraryService;
        setLayout(new BorderLayout());
        add(new JLabel("Shelves Panel"), BorderLayout.CENTER);
    }
}
