package ui;

import model.Book;
import service.LibraryService;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private final LibraryService libraryService;
    private final JTabbedPane tabbedPane;

    public MainFrame(LibraryService libraryService){
        this.libraryService=libraryService;
        this.tabbedPane=new JTabbedPane();

        configureWindow();
        createTabs();

    }

    private void configureWindow(){
        setTitle("Library System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000,700);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
    }

    private void createTabs(){
        tabbedPane.addTab("Books", new BooksPanel(libraryService));
        tabbedPane.addTab("Departments", new DepartmentsPanel(libraryService));
        tabbedPane.addTab("Shelves", new ShelvesPanel(libraryService));
        tabbedPane.addTab("Readers", new ReadersPanel(libraryService));
        tabbedPane.addTab("Loans", new LoansPanel(libraryService));
        tabbedPane.addTab("Search", new SearchPanel(libraryService));

        add(tabbedPane, BorderLayout.CENTER);
    }
}
