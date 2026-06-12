package ui;

import i18n.LanguageManager;
import model.Book;
import model.Department;
import model.Shelf;
import service.LibraryService;
import ui.tablemodel.BookTableModel;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

/**
 * Panel responsible for displaying, adding and deleting books.
 */
public class BooksPanel extends JPanel {

    private final LibraryService libraryService;
    private final LanguageManager languageManager;

    private final BookTableModel bookTableModel;
    private final JTable booksTable;

    private final JTextField titleField;
    private final JTextField authorsField;
    private final JTextField publisherField;
    private final JTextField yearField;
    private final JTextField isbnField;

    private final JComboBox<Department> departmentComboBox;
    private final JComboBox<Shelf> shelfComboBox;

    private final JButton addButton;
    private final JButton deleteButton;
    private final JButton refreshListsButton;

    private final JLabel titleLabel;
    private final JLabel authorsLabel;
    private final JLabel publisherLabel;
    private final JLabel yearLabel;
    private final JLabel isbnLabel;
    private final JLabel departmentLabel;
    private final JLabel storageLabel;

    private TitledBorder formBorder;

    public BooksPanel(LibraryService libraryService, LanguageManager languageManager) {
        this.libraryService = libraryService;
        this.languageManager = languageManager;

        this.bookTableModel = new BookTableModel(libraryService.getData().getBooks());
        this.booksTable = new JTable(bookTableModel);

        this.titleField = new JTextField();
        this.authorsField = new JTextField();
        this.publisherField = new JTextField();
        this.yearField = new JTextField();
        this.isbnField = new JTextField();

        this.departmentComboBox = new JComboBox<>();
        this.shelfComboBox = new JComboBox<>();

        this.addButton = new JButton();
        this.deleteButton = new JButton();
        this.refreshListsButton = new JButton();

        this.titleLabel = new JLabel();
        this.authorsLabel = new JLabel();
        this.publisherLabel = new JLabel();
        this.yearLabel = new JLabel();
        this.isbnLabel = new JLabel();
        this.departmentLabel = new JLabel();
        this.storageLabel = new JLabel();

        setLayout(new BorderLayout());

        add(createTablePanel(), BorderLayout.CENTER);
        add(createFormPanel(), BorderLayout.SOUTH);

        refreshComboBoxes();
        addListeners();
        reloadTexts();
    }

    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JScrollPane(booksTable), BorderLayout.CENTER);
        return panel;
    }

    private JPanel createFormPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout());

        JPanel fieldsPanel = new JPanel(new GridLayout(7, 2, 5, 5));

        fieldsPanel.add(titleLabel);
        fieldsPanel.add(titleField);

        fieldsPanel.add(authorsLabel);
        fieldsPanel.add(authorsField);

        fieldsPanel.add(publisherLabel);
        fieldsPanel.add(publisherField);

        fieldsPanel.add(yearLabel);
        fieldsPanel.add(yearField);

        fieldsPanel.add(isbnLabel);
        fieldsPanel.add(isbnField);

        fieldsPanel.add(departmentLabel);
        fieldsPanel.add(departmentComboBox);

        fieldsPanel.add(storageLabel);
        fieldsPanel.add(shelfComboBox);

        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonsPanel.add(refreshListsButton);
        buttonsPanel.add(addButton);
        buttonsPanel.add(deleteButton);

        formBorder = BorderFactory.createTitledBorder("");
        mainPanel.setBorder(formBorder);

        mainPanel.add(fieldsPanel, BorderLayout.CENTER);
        mainPanel.add(buttonsPanel, BorderLayout.SOUTH);

        return mainPanel;
    }

    private void addListeners() {
        addButton.addActionListener(event -> addBook());
        deleteButton.addActionListener(event -> deleteSelectedBook());
        refreshListsButton.addActionListener(event -> refreshComboBoxes());
    }

    private void addBook() {
        try {
            Long id = System.currentTimeMillis();

            String title = titleField.getText().trim();
            String authors = authorsField.getText().trim();
            String publisher = publisherField.getText().trim();
            String yearText = yearField.getText().trim();
            String isbn = isbnField.getText().trim();

            if (title.isEmpty()
                    || authors.isEmpty()
                    || publisher.isEmpty()
                    || yearText.isEmpty()
                    || isbn.isEmpty()) {
                JOptionPane.showMessageDialog(this, languageManager.get("message.fillAllFields"));
                return;
            }

            int year = Integer.parseInt(yearText);

            Department department = (Department) departmentComboBox.getSelectedItem();
            Shelf shelf = (Shelf) shelfComboBox.getSelectedItem();

            if (department == null || shelf == null) {
                JOptionPane.showMessageDialog(
                        this,
                        languageManager.get("message.addDepartmentAndShelfFirst")
                );
                return;
            }

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

            JOptionPane.showMessageDialog(this, languageManager.get("message.bookAdded"));
        } catch (NumberFormatException exception) {
            JOptionPane.showMessageDialog(this, languageManager.get("message.yearMustBeNumber"));
        } catch (Exception exception) {
            JOptionPane.showMessageDialog(
                    this,
                    languageManager.get("message.error") + " " + exception.getMessage()
            );
        }
    }

    private void deleteSelectedBook() {
        int selectedRow = booksTable.getSelectedRow();

        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, languageManager.get("message.selectBookToDelete"));
            return;
        }

        Book selectedBook = bookTableModel.getBookAt(selectedRow);

        try {
            libraryService.deleteBook(selectedBook);
            refreshTable();

            JOptionPane.showMessageDialog(this, languageManager.get("message.bookDeleted"));
        } catch (Exception exception) {
            JOptionPane.showMessageDialog(
                    this,
                    languageManager.get("message.error") + " " + exception.getMessage()
            );
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

    private void refreshComboBoxes() {
        departmentComboBox.removeAllItems();
        for (Department department : libraryService.getData().getDepartments()) {
            departmentComboBox.addItem(department);
        }

        shelfComboBox.removeAllItems();
        for (Shelf shelf : libraryService.getData().getShelves()) {
            shelfComboBox.addItem(shelf);
        }
    }

    public void refreshView() {
        refreshComboBoxes();
        refreshTable();
    }

    public void reloadTexts() {
        addButton.setText(languageManager.get("button.add"));
        deleteButton.setText(languageManager.get("button.delete"));
        refreshListsButton.setText(languageManager.get("button.refresh"));

        titleLabel.setText(languageManager.get("label.title"));
        authorsLabel.setText(languageManager.get("label.authors"));
        publisherLabel.setText(languageManager.get("label.publisher"));
        yearLabel.setText(languageManager.get("label.year"));
        isbnLabel.setText(languageManager.get("label.isbn"));
        departmentLabel.setText(languageManager.get("label.department"));
        storageLabel.setText(languageManager.get("label.storage"));

        formBorder.setTitle(languageManager.get("border.bookData"));

        revalidate();
        repaint();
    }
}