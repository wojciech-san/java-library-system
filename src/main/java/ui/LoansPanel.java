package ui;

import i18n.LanguageManager;
import model.Book;
import model.Loan;
import model.Reader;
import service.LibraryService;
import ui.tablemodel.LoanTableModel;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.time.LocalDate;

/**
 * Panel odpowiedzialny za wyświetlanie, tworzenie oraz zwracanie wypożyczeń książek.
 */
public class LoansPanel extends JPanel {

    private final LibraryService libraryService;
    private final LanguageManager languageManager;

    private final LoanTableModel loanTableModel;
    private final JTable loansTable;

    private final JComboBox<Book> bookComboBox;
    private final JComboBox<Reader> readerComboBox;
    private final JTextField dueDateField;

    private final JButton borrowButton;
    private final JButton returnButton;
    private final JButton refreshListsButton;

    private final JLabel bookLabel;
    private final JLabel readerLabel;
    private final JLabel dueDateLabel;

    private TitledBorder formBorder;

    public LoansPanel(LibraryService libraryService, LanguageManager languageManager) {
        this.libraryService = libraryService;
        this.languageManager = languageManager;

        this.loanTableModel = new LoanTableModel(libraryService.getData().getLoans());
        this.loansTable = new JTable(loanTableModel);

        this.bookComboBox = new JComboBox<>();
        this.readerComboBox = new JComboBox<>();
        this.dueDateField = new JTextField();

        this.borrowButton = new JButton();
        this.returnButton = new JButton();
        this.refreshListsButton = new JButton();

        this.bookLabel = new JLabel();
        this.readerLabel = new JLabel();
        this.dueDateLabel = new JLabel();

        setLayout(new BorderLayout());

        add(createTablePanel(), BorderLayout.CENTER);
        add(createFormPanel(), BorderLayout.SOUTH);

        refreshComboBoxes();
        addListeners();
        reloadTexts();
    }

    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JScrollPane(loansTable), BorderLayout.CENTER);
        return panel;
    }

    private JPanel createFormPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout());

        JPanel fieldsPanel = new JPanel(new GridLayout(3, 2, 5, 5));

        fieldsPanel.add(bookLabel);
        fieldsPanel.add(bookComboBox);

        fieldsPanel.add(readerLabel);
        fieldsPanel.add(readerComboBox);

        fieldsPanel.add(dueDateLabel);
        fieldsPanel.add(dueDateField);

        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonsPanel.add(refreshListsButton);
        buttonsPanel.add(borrowButton);
        buttonsPanel.add(returnButton);

        formBorder = BorderFactory.createTitledBorder("");
        mainPanel.setBorder(formBorder);

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
            String dueDateText = dueDateField.getText().trim();

            if (selectedBook == null) {
                JOptionPane.showMessageDialog(
                        this,
                        languageManager.get("message.noAvailableBooks")
                );
                return;
            }

            if (selectedReader == null) {
                JOptionPane.showMessageDialog(
                        this,
                        languageManager.get("message.addReaderFirst")
                );
                return;
            }

            if (dueDateText.isEmpty()) {
                JOptionPane.showMessageDialog(
                        this,
                        languageManager.get("message.dueDateRequired")
                );
                return;
            }

            LocalDate dueDate = LocalDate.parse(dueDateText);

            libraryService.borrowBook(selectedBook, selectedReader, dueDate);

            refreshTable();
            refreshComboBoxes();
            clearForm();

            JOptionPane.showMessageDialog(this, languageManager.get("message.loanCreated"));
        } catch (Exception exception) {
            JOptionPane.showMessageDialog(
                    this,
                    languageManager.get("message.error") + " " + exception.getMessage()
            );
        }
    }

    private void returnSelectedBook() {
        int selectedRow = loansTable.getSelectedRow();

        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(
                    this,
                    languageManager.get("message.selectLoanToReturn")
            );
            return;
        }

        Loan selectedLoan = loanTableModel.getLoanAt(selectedRow);

        try {
            libraryService.returnBook(selectedLoan);

            refreshTable();
            refreshComboBoxes();

            JOptionPane.showMessageDialog(this, languageManager.get("message.loanReturned"));
        } catch (Exception exception) {
            JOptionPane.showMessageDialog(
                    this,
                    languageManager.get("message.error") + " " + exception.getMessage()
            );
        }
    }

    private void refreshTable() {
        loanTableModel.fireTableDataChanged();
    }

    private void clearForm() {
        dueDateField.setText("");
    }

    public void refreshView() {
        refreshComboBoxes();
        refreshTable();
    }

    public void reloadTexts() {
        borrowButton.setText(languageManager.get("button.borrow"));
        returnButton.setText(languageManager.get("button.return"));
        refreshListsButton.setText(languageManager.get("button.refresh"));

        bookLabel.setText(languageManager.get("label.book"));
        readerLabel.setText(languageManager.get("label.reader"));
        dueDateLabel.setText(languageManager.get("label.dueDate"));

        formBorder.setTitle(languageManager.get("border.loanData"));

        loanTableModel.setColumnNames(new String[]{
                languageManager.get("table.id"),
                languageManager.get("table.book"),
                languageManager.get("table.reader"),
                languageManager.get("table.loanDate"),
                languageManager.get("table.dueDate"),
                languageManager.get("table.returnDate"),
                languageManager.get("table.status")
        });

        revalidate();
        repaint();
    }
}