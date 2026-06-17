package ui;

import i18n.LanguageManager;
import model.Reader;
import service.LibraryService;
import ui.tablemodel.ReaderTableModel;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

/**
 * Panel odpowiedzialny za wyświetlanie, dodawanie i usuwanie czytelników.
 */
public class ReadersPanel extends JPanel {

    private final LibraryService libraryService;
    private final LanguageManager languageManager;

    private final ReaderTableModel readerTableModel;
    private final JTable readersTable;

    private final JTextField firstNameField;
    private final JTextField lastNameField;
    private final JTextField addressField;
    private final JTextField cardNumberField;

    private final JButton addButton;
    private final JButton deleteButton;

    private final JLabel firstNameLabel;
    private final JLabel lastNameLabel;
    private final JLabel addressLabel;
    private final JLabel cardNumberLabel;

    private TitledBorder formBorder;

    public ReadersPanel(LibraryService libraryService, LanguageManager languageManager) {
        this.libraryService = libraryService;
        this.languageManager = languageManager;

        this.readerTableModel = new ReaderTableModel(
                libraryService.getData().getReaders()
        );
        this.readersTable = new JTable(readerTableModel);

        this.firstNameField = new JTextField();
        this.lastNameField = new JTextField();
        this.addressField = new JTextField();
        this.cardNumberField = new JTextField();

        this.addButton = new JButton();
        this.deleteButton = new JButton();

        this.firstNameLabel = new JLabel();
        this.lastNameLabel = new JLabel();
        this.addressLabel = new JLabel();
        this.cardNumberLabel = new JLabel();

        setLayout(new BorderLayout());

        add(createTablePanel(), BorderLayout.CENTER);
        add(createFormPanel(), BorderLayout.SOUTH);

        addListeners();
        reloadTexts();
    }

    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JScrollPane(readersTable), BorderLayout.CENTER);
        return panel;
    }

    private JPanel createFormPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout());

        JPanel fieldsPanel = new JPanel(new GridLayout(4, 2, 5, 5));

        fieldsPanel.add(firstNameLabel);
        fieldsPanel.add(firstNameField);

        fieldsPanel.add(lastNameLabel);
        fieldsPanel.add(lastNameField);

        fieldsPanel.add(addressLabel);
        fieldsPanel.add(addressField);

        fieldsPanel.add(cardNumberLabel);
        fieldsPanel.add(cardNumberField);

        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonsPanel.add(addButton);
        buttonsPanel.add(deleteButton);

        formBorder = BorderFactory.createTitledBorder("");
        mainPanel.setBorder(formBorder);

        mainPanel.add(fieldsPanel, BorderLayout.CENTER);
        mainPanel.add(buttonsPanel, BorderLayout.SOUTH);

        return mainPanel;
    }

    private void addListeners() {
        addButton.addActionListener(event -> addReader());
        deleteButton.addActionListener(event -> deleteSelectedReader());
    }

    private void addReader() {
        try {
            Long id = System.currentTimeMillis();

            String firstName = firstNameField.getText().trim();
            String lastName = lastNameField.getText().trim();
            String address = addressField.getText().trim();
            String cardNumber = cardNumberField.getText().trim();

            if (firstName.isEmpty()
                    || lastName.isEmpty()
                    || address.isEmpty()
                    || cardNumber.isEmpty()) {
                JOptionPane.showMessageDialog(this, languageManager.get("message.fillAllFields"));
                return;
            }

            Reader reader = new Reader(id, firstName, lastName, address, cardNumber);

            libraryService.addReader(reader);

            refreshTable();
            clearForm();

            JOptionPane.showMessageDialog(this, languageManager.get("message.readerAdded"));
        } catch (Exception exception) {
            JOptionPane.showMessageDialog(
                    this,
                    languageManager.get("message.error") + " " + exception.getMessage()
            );
        }
    }

    private void deleteSelectedReader() {
        int selectedRow = readersTable.getSelectedRow();

        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(
                    this,
                    languageManager.get("message.selectReaderToDelete")
            );
            return;
        }

        Reader selectedReader = readerTableModel.getReaderAt(selectedRow);

        try {
            libraryService.deleteReader(selectedReader);

            refreshTable();

            JOptionPane.showMessageDialog(this, languageManager.get("message.readerDeleted"));
        } catch (Exception exception) {
            JOptionPane.showMessageDialog(
                    this,
                    languageManager.get("message.error") + " " + exception.getMessage()
            );
        }
    }

    private void refreshTable() {
        readerTableModel.fireTableDataChanged();
    }

    private void clearForm() {
        firstNameField.setText("");
        lastNameField.setText("");
        addressField.setText("");
        cardNumberField.setText("");
    }

    public void refreshView() {
        refreshTable();
    }

    public void reloadTexts() {
        addButton.setText(languageManager.get("button.add"));
        deleteButton.setText(languageManager.get("button.delete"));

        firstNameLabel.setText(languageManager.get("label.firstName"));
        lastNameLabel.setText(languageManager.get("label.lastName"));
        addressLabel.setText(languageManager.get("label.address"));
        cardNumberLabel.setText(languageManager.get("label.cardNumber"));

        formBorder.setTitle(languageManager.get("border.readerData"));

        readerTableModel.setColumnNames(new String[]{
                languageManager.get("table.id"),
                languageManager.get("table.firstName"),
                languageManager.get("table.lastName"),
                languageManager.get("table.address"),
                languageManager.get("table.cardNumber")
        });

        revalidate();
        repaint();
    }
}