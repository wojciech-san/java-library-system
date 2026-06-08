package ui;

import model.Book;
import model.Loan;
import service.LibraryService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Panel for searching books and loans.
 */
public class SearchPanel extends JPanel {

    private final LibraryService libraryService;

    private final JTextField searchField;

    private final JButton searchBooksButton;
    private final JButton activeLoansButton;
    private final JButton overdueLoansButton;
    private final JButton clearButton;

    private final JTable resultsTable;
    private final DefaultTableModel resultsTableModel;

    public SearchPanel(LibraryService libraryService) {
        this.libraryService = libraryService;

        this.searchField = new JTextField();

        this.searchBooksButton = new JButton("Szukaj książek");
        this.activeLoansButton = new JButton("Aktywne wypożyczenia");
        this.overdueLoansButton = new JButton("Przeterminowane");
        this.clearButton = new JButton("Wyczyść");

        this.resultsTableModel = new DefaultTableModel();
        this.resultsTable = new JTable(resultsTableModel);

        setLayout(new BorderLayout());

        add(createSearchPanel(), BorderLayout.NORTH);
        add(createResultsPanel(), BorderLayout.CENTER);

        addListeners();
    }

    private JPanel createSearchPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout());

        JPanel inputPanel = new JPanel(new BorderLayout(5, 5));
        inputPanel.add(new JLabel("Fraza:"), BorderLayout.WEST);
        inputPanel.add(searchField, BorderLayout.CENTER);

        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonsPanel.add(searchBooksButton);
        buttonsPanel.add(activeLoansButton);
        buttonsPanel.add(overdueLoansButton);
        buttonsPanel.add(clearButton);

        mainPanel.setBorder(BorderFactory.createTitledBorder("Wyszukiwanie"));
        mainPanel.add(inputPanel, BorderLayout.CENTER);
        mainPanel.add(buttonsPanel, BorderLayout.SOUTH);

        return mainPanel;
    }

    private JPanel createResultsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Wyniki"));
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
            JOptionPane.showMessageDialog(this, "Wpisz frazę do wyszukania.");
            return;
        }

        List<Book> books = libraryService.searchBooks(phrase);

        showBooksResult(books);
    }

    private void showBooksResult(List<Book> books) {
        resultsTableModel.setColumnIdentifiers(new String[]{
                "ID",
                "Tytuł",
                "Autorzy",
                "Wydawnictwo",
                "Rok",
                "ISBN",
                "Dział",
                "Miejsce"
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
                "ID",
                "Książka",
                "Czytelnik",
                "Data wypożyczenia",
                "Termin zwrotu",
                "Data zwrotu",
                "Status"
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
                    loan.getReturnDate() == null ? "Aktywne" : "Zwrócone"
            });
        }
    }

    private void clearResults() {
        searchField.setText("");
        resultsTableModel.setRowCount(0);
        resultsTableModel.setColumnCount(0);
    }
}