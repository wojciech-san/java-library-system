package ui;

import service.LibraryService;

import javax.swing.*;
import java.awt.*;

public class ReadersPanel extends JPanel {
    private final LibraryService libraryService;

    public ReadersPanel(LibraryService libraryService) {
        this.libraryService = libraryService;
        setLayout(new BorderLayout());
        add(new JLabel("Readers Panel"), BorderLayout.CENTER);
    }
}
