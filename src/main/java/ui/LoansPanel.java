package ui;

import service.LibraryService;

import javax.swing.*;
import java.awt.*;

public class LoansPanel extends JPanel {
    private final LibraryService libraryService;

    public LoansPanel(LibraryService libraryService) {
        this.libraryService = libraryService;
        setLayout(new BorderLayout());
        add(new JLabel("Loans Panel"), BorderLayout.CENTER);
    }
}
