package ui;

import service.LibraryService;

import javax.swing.*;
import java.awt.*;

public class DepartmentsPanel extends JPanel {
    private final LibraryService libraryService;

    public DepartmentsPanel(LibraryService libraryService){
        this.libraryService = libraryService;
        setLayout(new BorderLayout());
        add(new JLabel("Departments Panel"));
    }
}
