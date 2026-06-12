package ui;

import i18n.LanguageManager;
import model.LibraryData;
import service.LibraryService;
import storage.FileStorageService;

import javax.swing.*;
import java.awt.*;
import java.io.File;

/**
 * Main application window.
 */
public class MainFrame extends JFrame {

    private final LibraryService libraryService;
    private final LanguageManager languageManager;
    private final FileStorageService fileStorageService;
    private final JTabbedPane tabbedPane;

    private BooksPanel booksPanel;
    private DepartmentsPanel departmentsPanel;
    private ShelvesPanel shelvesPanel;
    private ReadersPanel readersPanel;
    private LoansPanel loansPanel;
    private SearchPanel searchPanel;

    private JButton saveButton;
    private JButton loadButton;
    private JButton polishButton;
    private JButton englishButton;

    public MainFrame(LibraryService libraryService, LanguageManager languageManager) {
        this.libraryService = libraryService;
        this.languageManager = languageManager;
        this.fileStorageService = new FileStorageService();
        this.tabbedPane = new JTabbedPane();

        configureWindow();
        createTopPanel();
        createTabs();
        reloadTexts();
    }

    private void configureWindow() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
    }

    private void createTopPanel() {
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        saveButton = new JButton();
        loadButton = new JButton();
        polishButton = new JButton("PL");
        englishButton = new JButton("EN");

        saveButton.addActionListener(event -> saveData());
        loadButton.addActionListener(event -> loadData());

        polishButton.addActionListener(event -> {
            languageManager.setLanguage("pl");
            reloadTexts();
        });

        englishButton.addActionListener(event -> {
            languageManager.setLanguage("en");
            reloadTexts();
        });

        topPanel.add(saveButton);
        topPanel.add(loadButton);
        topPanel.add(polishButton);
        topPanel.add(englishButton);

        add(topPanel, BorderLayout.NORTH);
    }

    private void createTabs() {
        booksPanel = new BooksPanel(libraryService, languageManager);
        departmentsPanel = new DepartmentsPanel(libraryService, languageManager);
        shelvesPanel = new ShelvesPanel(libraryService, languageManager);
        readersPanel = new ReadersPanel(libraryService, languageManager);
        loansPanel = new LoansPanel(libraryService, languageManager);
        searchPanel = new SearchPanel(libraryService, languageManager);

        tabbedPane.addTab("", booksPanel);
        tabbedPane.addTab("", departmentsPanel);
        tabbedPane.addTab("", shelvesPanel);
        tabbedPane.addTab("", readersPanel);
        tabbedPane.addTab("", loansPanel);
        tabbedPane.addTab("", searchPanel);

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
                    JOptionPane.showMessageDialog(
                            MainFrame.this,
                            languageManager.get("message.dataSaved")
                    );
                } catch (Exception exception) {
                    JOptionPane.showMessageDialog(
                            MainFrame.this,
                            languageManager.get("message.saveError") + " " + exception.getMessage()
                    );
                }
            }
        }.execute();
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

            JOptionPane.showMessageDialog(this, languageManager.get("message.dataLoaded"));
        } catch (Exception exception) {
            JOptionPane.showMessageDialog(
                    this,
                    languageManager.get("message.loadError") + " " + exception.getMessage()
            );
        }
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

    private void refreshAllTabs() {
        booksPanel.refreshView();
        departmentsPanel.refreshView();
        shelvesPanel.refreshView();
        readersPanel.refreshView();
        loansPanel.refreshView();
        searchPanel.refreshView();
    }

    private void reloadTexts() {
        setTitle(languageManager.get("app.title"));

        saveButton.setText(languageManager.get("button.save"));
        loadButton.setText(languageManager.get("button.load"));

        tabbedPane.setTitleAt(0, languageManager.get("tab.books"));
        tabbedPane.setTitleAt(1, languageManager.get("tab.departments"));
        tabbedPane.setTitleAt(2, languageManager.get("tab.shelves"));
        tabbedPane.setTitleAt(3, languageManager.get("tab.readers"));
        tabbedPane.setTitleAt(4, languageManager.get("tab.loans"));
        tabbedPane.setTitleAt(5, languageManager.get("tab.search"));

        booksPanel.reloadTexts();
        departmentsPanel.reloadTexts();
        shelvesPanel.reloadTexts();
        readersPanel.reloadTexts();
        loansPanel.reloadTexts();
        searchPanel.reloadTexts();

        revalidate();
        repaint();
    }
}