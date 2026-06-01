package ui;

import service.LibraryService;

import javax.swing.*;
import java.awt.*;

public class SearchPanel extends JPanel {
    private final LibraryService libraryService;

    public SearchPanel(LibraryService libraryService) {
        this.libraryService = libraryService;
        setLayout(new BorderLayout());
        add(new JLabel("Search Panel"), BorderLayout.CENTER);
    }
}
