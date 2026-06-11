package ui;

import model.Book;
import model.LibraryData;
import service.LibraryService;

import javax.swing.*;
import java.awt.*;

import storage.FileStorageService;
import java.io.File;

public class MainFrame extends JFrame {
    private final LibraryService libraryService;
    private final JTabbedPane tabbedPane;

    private final FileStorageService fileStorageService;

    private BooksPanel booksPanel;
    private DepartmentsPanel departmentsPanel;
    private ShelvesPanel shelvesPanel;
    private ReadersPanel readersPanel;
    private LoansPanel loansPanel;
    private SearchPanel searchPanel;


    public MainFrame(LibraryService libraryService){
        this.libraryService=libraryService;
        this.tabbedPane=new JTabbedPane();
        this.fileStorageService = new FileStorageService();

        configureWindow();
        createTopPanel();
        createTabs();

    }

    private void configureWindow(){
        setTitle("Library System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000,700);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
    }

    private void createTopPanel() {
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JButton saveButton = new JButton("Zapisz");
        JButton loadButton = new JButton("Wczytaj");

        saveButton.addActionListener(event -> saveData());
        loadButton.addActionListener(event -> loadData());

        topPanel.add(saveButton);
        topPanel.add(loadButton);

        add(topPanel, BorderLayout.NORTH);
    }

    private void createTabs(){
        booksPanel = new BooksPanel(libraryService);
        departmentsPanel = new DepartmentsPanel(libraryService);
        shelvesPanel = new ShelvesPanel(libraryService);
        readersPanel = new ReadersPanel(libraryService);
        loansPanel = new LoansPanel(libraryService);
        searchPanel = new SearchPanel(libraryService);

        tabbedPane.addTab("Książki", booksPanel);
        tabbedPane.addTab("Działy", departmentsPanel);
        tabbedPane.addTab("Regały i półki", shelvesPanel);
        tabbedPane.addTab("Czytelnicy", readersPanel);
        tabbedPane.addTab("Wypożyczenia", loansPanel);
        tabbedPane.addTab("Wyszukiwanie", searchPanel);

        add(tabbedPane, BorderLayout.CENTER);
    }

    private void saveData() {
        JFileChooser fileChooser = new JFileChooser();

        int result = fileChooser.showSaveDialog(this);

        if (result != JFileChooser.APPROVE_OPTION) {
            return;
        }

        File file = fileChooser.getSelectedFile();

        new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                fileStorageService.save(libraryService.getData(), file);
                return null;
            }

            @Override
            protected void done() {
                try {
                    get();
                    JOptionPane.showMessageDialog(MainFrame.this, "Zapisano dane do pliku.");
                } catch (Exception exception) {
                    JOptionPane.showMessageDialog(MainFrame.this, "Błąd zapisu: " + exception.getMessage());
                }
            }
        }.execute();
    }
    private void replaceCurrentData(LibraryData loadedData) {
        LibraryData currentData = libraryService.getData();

        currentData.getBooks().clear();
        currentData.getBooks().addAll(loadedData.getBooks());

        currentData.getDepartments().clear();
        currentData.getDepartments().addAll(loadedData.getDepartments());

        currentData.getShelves().clear();
        currentData.getShelves().addAll(loadedData.getShelves());

        currentData.getReaders().clear();
        currentData.getReaders().addAll(loadedData.getReaders());

        currentData.getLoans().clear();
        currentData.getLoans().addAll(loadedData.getLoans());
    }
    private void loadData() {
        JFileChooser fileChooser = new JFileChooser();

        int result = fileChooser.showOpenDialog(this);

        if (result != JFileChooser.APPROVE_OPTION) {
            return;
        }

        File file = fileChooser.getSelectedFile();

        try {
            LibraryData loadedData = fileStorageService.load(file);

            replaceCurrentData(loadedData);
            refreshAllTabs();

            JOptionPane.showMessageDialog(this, "Wczytano dane z pliku.");
        } catch (Exception exception) {
            JOptionPane.showMessageDialog(this, "Błąd odczytu: " + exception.getMessage());
        }

    }

    private void refreshAllTabs() {
        booksPanel.refreshView();
        departmentsPanel.refreshView();
        shelvesPanel.refreshView();
        readersPanel.refreshView();
        loansPanel.refreshView();
        searchPanel.refreshView();
    }

}
