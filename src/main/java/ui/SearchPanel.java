package ui;

import i18n.LanguageManager;
import model.Book;
import model.Loan;
import service.LibraryService;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Panel odpowiedzialny za wyszukiwanie książek oraz wyświetlanie raportów wypożyczeń.
 */
public class SearchPanel extends JPanel {

    private final LibraryService libraryService;
    private final LanguageManager languageManager;

    private final JTextField searchField;

    private final JButton searchBooksButton;
    private final JButton activeLoansButton;
    private final JButton overdueLoansButton;
    private final JButton clearButton;

    private final JTable resultsTable;
    private final DefaultTableModel resultsTableModel;

    private final JLabel phraseLabel;

    private TitledBorder searchBorder;
    private TitledBorder resultsBorder;

    public SearchPanel(LibraryService libraryService, LanguageManager languageManager) {
        this.libraryService = libraryService;
        this.languageManager = languageManager;

        this.searchField = new JTextField();

        this.searchBooksButton = new JButton();
        this.activeLoansButton = new JButton();
        this.overdueLoansButton = new JButton();
        this.clearButton = new JButton();

        this.resultsTableModel = new DefaultTableModel();
        this.resultsTable = new JTable(resultsTableModel);

        this.phraseLabel = new JLabel();

        setLayout(new BorderLayout());

        add(createSearchPanel(), BorderLayout.NORTH);
        add(createResultsPanel(), BorderLayout.CENTER);

        addListeners();
        reloadTexts();
    }

    private JPanel createSearchPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout());

        JPanel inputPanel = new JPanel(new BorderLayout(5, 5));
        inputPanel.add(phraseLabel, BorderLayout.WEST);
        inputPanel.add(searchField, BorderLayout.CENTER);

        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonsPanel.add(searchBooksButton);
        buttonsPanel.add(activeLoansButton);
        buttonsPanel.add(overdueLoansButton);
        buttonsPanel.add(clearButton);

        searchBorder = BorderFactory.createTitledBorder("");
        mainPanel.setBorder(searchBorder);

        mainPanel.add(inputPanel, BorderLayout.CENTER);
        mainPanel.add(buttonsPanel, BorderLayout.SOUTH);

        return mainPanel;
    }

    private JPanel createResultsPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        resultsBorder = BorderFactory.createTitledBorder("");
        panel.setBorder(resultsBorder);

        panel.add(new JScrollPane(resultsTable), BorderLayout.CENTER);

        return panel;
    }

    private void addListeners() {
        searchBooksButton.addActionListener(event -> searchBooks());
        activeLoansButton.addActionListener(event -> showActiveLoans());
        overdueLoansButton.addActionListener(event -> showOverdueLoans());
        clearButton.addActionListener(event -> clearResults());
    }

    private void searchBooks() {
        String phrase = searchField.getText().trim();

        if (phrase.isEmpty()) {
            JOptionPane.showMessageDialog(this, languageManager.get("message.enterSearchPhrase"));
            return;
        }

        List<Book> books = libraryService.searchBooks(phrase);

        showBooksResult(books);
    }

    private void showBooksResult(List<Book> books) {
        resultsTableModel.setColumnIdentifiers(new String[]{
                languageManager.get("table.id"),
                languageManager.get("table.title"),
                languageManager.get("table.authors"),
                languageManager.get("table.publisher"),
                languageManager.get("table.year"),
                languageManager.get("table.isbn"),
                languageManager.get("table.department"),
                languageManager.get("table.storage")
        });

        resultsTableModel.setRowCount(0);

        for (Book book : books) {
            resultsTableModel.addRow(new Object[]{
                    book.getId(),
                    book.getTitle(),
                    book.getAuthors(),
                    book.getPublisher(),
                    book.getYear(),
                    book.getIsbn(),
                    book.getDepartment() != null ? book.getDepartment().getName() : "",
                    book.getShelf() != null ? book.getShelf().toString() : ""
            });
        }
    }

    private void showActiveLoans() {
        List<Loan> activeLoans = libraryService.getData()
                .getLoans()
                .stream()
                .filter(loan -> loan.getReturnDate() == null)
                .toList();

        showLoansResult(activeLoans);
    }

    private void showOverdueLoans() {
        List<Loan> overdueLoans = libraryService.findOverdueLoans();

        showLoansResult(overdueLoans);
    }

    private void showLoansResult(List<Loan> loans) {
        resultsTableModel.setColumnIdentifiers(new String[]{
                languageManager.get("table.id"),
                languageManager.get("table.book"),
                languageManager.get("table.reader"),
                languageManager.get("table.loanDate"),
                languageManager.get("table.dueDate"),
                languageManager.get("table.returnDate"),
                languageManager.get("table.status")
        });

        resultsTableModel.setRowCount(0);

        for (Loan loan : loans) {
            resultsTableModel.addRow(new Object[]{
                    loan.getId(),
                    loan.getBook() != null ? loan.getBook().toString() : "",
                    loan.getReader() != null ? loan.getReader().toString() : "",
                    loan.getLoanDate(),
                    loan.getDueDate(),
                    loan.getReturnDate() != null ? loan.getReturnDate() : "",
                    loan.getReturnDate() == null
                            ? languageManager.get("status.active")
                            : languageManager.get("status.returned")
            });
        }
    }

    private void clearResults() {
        searchField.setText("");
        resultsTableModel.setRowCount(0);
        resultsTableModel.setColumnCount(0);
    }

    public void refreshView() {
        clearResults();
    }

    public void reloadTexts() {
        phraseLabel.setText(languageManager.get("label.phrase"));

        searchBooksButton.setText(languageManager.get("button.searchBooks"));
        activeLoansButton.setText(languageManager.get("button.activeLoans"));
        overdueLoansButton.setText(languageManager.get("button.overdueLoans"));
        clearButton.setText(languageManager.get("button.clear"));

        searchBorder.setTitle(languageManager.get("border.search"));
        resultsBorder.setTitle(languageManager.get("border.results"));

        revalidate();
        repaint();
    }
}