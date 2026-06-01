package ui;

import model.Book;
import model.Department;
import model.Shelf;
import service.LibraryService;
import ui.tablemodel.BookTableModel;

import javax.swing.*;
import java.awt.*;

public class BooksPanel extends JPanel {
    private final LibraryService libraryService;

    private final BookTableModel bookTableModel;
    private final JTable booksTable;

    private final JTextField titleField;
    private final JTextField authorsField;
    private final JTextField publisherField;
    private final JTextField yearField;
    private final JTextField isbnField;

    private final JButton addButton;
    private final JButton deleteButton;

    public BooksPanel(LibraryService libraryService) {
        this.libraryService = libraryService;

        this.bookTableModel = new BookTableModel(libraryService.getData().getBooks());
        this.booksTable = new JTable(bookTableModel);

        this.titleField = new JTextField();
        this.authorsField = new JTextField();
        this.publisherField = new JTextField();
        this.yearField = new JTextField();
        this.isbnField = new JTextField();

        this.addButton = new JButton("Dodaj");
        this.deleteButton = new JButton("Usuń");

        setLayout(new BorderLayout());

        add(createTablePanel(), BorderLayout.CENTER);
        add(createFormPanel(), BorderLayout.SOUTH);

        addListeners();
    }
    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JScrollPane(booksTable), BorderLayout.CENTER);
        return panel;
    }

    private JPanel createFormPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout());

        JPanel fieldsPanel = new JPanel(new GridLayout(5, 2, 5, 5));

        fieldsPanel.add(new JLabel("Tytuł:"));
        fieldsPanel.add(titleField);

        fieldsPanel.add(new JLabel("Autorzy:"));
        fieldsPanel.add(authorsField);

        fieldsPanel.add(new JLabel("Wydawnictwo:"));
        fieldsPanel.add(publisherField);

        fieldsPanel.add(new JLabel("Rok:"));
        fieldsPanel.add(yearField);

        fieldsPanel.add(new JLabel("ISBN:"));
        fieldsPanel.add(isbnField);

        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonsPanel.add(addButton);
        buttonsPanel.add(deleteButton);

        mainPanel.setBorder(BorderFactory.createTitledBorder("Dane książki"));
        mainPanel.add(fieldsPanel, BorderLayout.CENTER);
        mainPanel.add(buttonsPanel, BorderLayout.SOUTH);

        return mainPanel;
    }
    private void addListeners() {
        addButton.addActionListener(event -> addBook());
        deleteButton.addActionListener(event -> deleteSelectedBook());
    }
    private void addBook() {
        try {
            Long id = System.currentTimeMillis();

            String title = titleField.getText().trim();
            String authors = authorsField.getText().trim();
            String publisher = publisherField.getText().trim();
            int year = Integer.parseInt(yearField.getText().trim());
            String isbn = isbnField.getText().trim();

            if (title.isEmpty() || authors.isEmpty() || publisher.isEmpty() || isbn.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Uzupełnij wszystkie pola.");
                return;
            }

            /*
             * Na razie department i shelf są ustawione jako null,
             * bo panele działów i półek mogą jeszcze nie być gotowe.
             * Później dodamy JComboBox<Department> i JComboBox<Shelf>.
             */
            Department department = null;
            Shelf shelf = null;

            Book book = new Book(
                    id,
                    title,
                    authors,
                    publisher,
                    year,
                    isbn,
                    shelf,
                    department
            );

            libraryService.addBook(book);

            refreshTable();
            clearForm();

            JOptionPane.showMessageDialog(this, "Dodano książkę.");
        } catch (NumberFormatException exception) {
            JOptionPane.showMessageDialog(this, "Rok musi być liczbą.");
        } catch (Exception exception) {
            JOptionPane.showMessageDialog(this, "Błąd: " + exception.getMessage());
        }
    }
    private void deleteSelectedBook() {
        int selectedRow = booksTable.getSelectedRow();

        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Wybierz książkę do usunięcia.");
            return;
        }

        Book selectedBook = bookTableModel.getBookAt(selectedRow);

        try {
            libraryService.deleteBook(selectedBook);
            refreshTable();

            JOptionPane.showMessageDialog(this, "Usunięto książkę.");
        } catch (Exception exception) {
            JOptionPane.showMessageDialog(this, "Błąd: " + exception.getMessage());
        }
    }
    private void refreshTable() {
        bookTableModel.fireTableDataChanged();
    }

    private void clearForm() {
        titleField.setText("");
        authorsField.setText("");
        publisherField.setText("");
        yearField.setText("");
        isbnField.setText("");
    }

}
