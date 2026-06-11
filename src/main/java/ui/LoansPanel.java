package ui;

import model.Book;
import model.Loan;
import model.Reader;
import service.LibraryService;
import ui.tablemodel.LoanTableModel;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

public class LoansPanel extends JPanel {
    private final LibraryService libraryService;

    private final LoanTableModel loanTableModel;
    private final JTable loansTable;

    private final JComboBox<Book> bookComboBox;
    private final JComboBox<Reader> readerComboBox;
    private final JTextField dueDateField;

    private final JButton borrowButton;
    private final JButton returnButton;
    private final JButton refreshListsButton;

    public LoansPanel(LibraryService libraryService) {
        this.libraryService = libraryService;

        this.loanTableModel = new LoanTableModel(libraryService.getData().getLoans());
        this.loansTable = new JTable(loanTableModel);

        this.bookComboBox = new JComboBox<>();
        this.readerComboBox = new JComboBox<>();
        this.dueDateField = new JTextField();

        this.borrowButton = new JButton("Wypożycz");
        this.returnButton = new JButton("Zwróć");
        this.refreshListsButton = new JButton("Odśwież listy");

        setLayout(new BorderLayout());

        add(createTablePanel(), BorderLayout.CENTER);
        add(createFormPanel(), BorderLayout.SOUTH);

        refreshComboBoxes();
        addListeners();
    }
    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JScrollPane(loansTable), BorderLayout.CENTER);
        return panel;
    }
    private JPanel createFormPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout());

        JPanel fieldsPanel = new JPanel(new GridLayout(3, 2, 5, 5));

        fieldsPanel.add(new JLabel("Książka:"));
        fieldsPanel.add(bookComboBox);

        fieldsPanel.add(new JLabel("Czytelnik:"));
        fieldsPanel.add(readerComboBox);

        fieldsPanel.add(new JLabel("Termin zwrotu (YYYY-MM-DD):"));
        fieldsPanel.add(dueDateField);

        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonsPanel.add(refreshListsButton);
        buttonsPanel.add(borrowButton);
        buttonsPanel.add(returnButton);

        mainPanel.setBorder(BorderFactory.createTitledBorder("Dane wypożyczenia"));
        mainPanel.add(fieldsPanel, BorderLayout.CENTER);
        mainPanel.add(buttonsPanel, BorderLayout.SOUTH);

        return mainPanel;
    }
    private void addListeners() {
        refreshListsButton.addActionListener(event -> refreshComboBoxes());
        borrowButton.addActionListener(event -> borrowBook());
        returnButton.addActionListener(event -> returnSelectedBook());
    }

    private void refreshComboBoxes() {
        bookComboBox.removeAllItems();

        for (Book book : libraryService.getData().getBooks()) {
            if (libraryService.isBookAvailable(book)) {
                bookComboBox.addItem(book);
            }
        }

        readerComboBox.removeAllItems();

        for (Reader reader : libraryService.getData().getReaders()) {
            readerComboBox.addItem(reader);
        }
    }
    private void borrowBook() {
        try {
            Book selectedBook = (Book) bookComboBox.getSelectedItem();
            Reader selectedReader = (Reader) readerComboBox.getSelectedItem();

            if (selectedBook == null) {
                JOptionPane.showMessageDialog(this, "Brak dostępnych książek do wypożyczenia.");
                return;
            }

            if (selectedReader == null) {
                JOptionPane.showMessageDialog(this, "Najpierw dodaj wypożyczającego.");
                return;
            }

            LocalDate dueDate = LocalDate.parse(dueDateField.getText().trim());

            libraryService.borrowBook(selectedBook, selectedReader, dueDate);

            refreshTable();
            refreshComboBoxes();
            clearForm();

            JOptionPane.showMessageDialog(this, "Wypożyczono książkę.");
        } catch (Exception exception) {
            JOptionPane.showMessageDialog(this, "Błąd: " + exception.getMessage());
        }
    }
    private void returnSelectedBook() {
        int selectedRow = loansTable.getSelectedRow();

        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Wybierz wypożyczenie do zwrotu.");
            return;
        }

        Loan selectedLoan = loanTableModel.getLoanAt(selectedRow);

        try {
            libraryService.returnBook(selectedLoan);

            refreshTable();
            refreshComboBoxes();

            JOptionPane.showMessageDialog(this, "Zwrócono książkę.");
        } catch (Exception exception) {
            JOptionPane.showMessageDialog(this, "Błąd: " + exception.getMessage());
        }
    }
    private void refreshTable() {
        loanTableModel.refresh();
    }

    private void clearForm() {
        dueDateField.setText("");
    }

    public void refreshView() {
        refreshComboBoxes();
        refreshTable();
    }
}
