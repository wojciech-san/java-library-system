package ui;

import model.Reader;
import service.LibraryService;
import ui.tablemodel.ReaderTableModel;

import javax.swing.*;
import java.awt.*;

public class ReadersPanel extends JPanel {
    private final LibraryService libraryService;

    private final ReaderTableModel readerTableModel;
    private final JTable readersTable;

    private final JTextField firstNameField;
    private final JTextField lastNameField;
    private final JTextField addressField;
    private final JTextField cardNumberField;

    private final JButton addButton;
    private final JButton deleteButton;

    public ReadersPanel(LibraryService libraryService) {
        this.libraryService = libraryService;

        this.readerTableModel = new ReaderTableModel(
                libraryService.getData().getReaders()
        );
        this.readersTable = new JTable(readerTableModel);

        this.firstNameField = new JTextField();
        this.lastNameField = new JTextField();
        this.addressField = new JTextField();
        this.cardNumberField = new JTextField();

        this.addButton = new JButton("Dodaj");
        this.deleteButton = new JButton("Usuń");

        setLayout(new BorderLayout());

        add(createTablePanel(), BorderLayout.CENTER);
        add(createFormPanel(), BorderLayout.SOUTH);

        addListeners();
    }

    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JScrollPane(readersTable), BorderLayout.CENTER);
        return panel;
    }

    private JPanel createFormPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout());

        JPanel fieldsPanel = new JPanel(new GridLayout(4, 2, 5, 5));

        fieldsPanel.add(new JLabel("Imię:"));
        fieldsPanel.add(firstNameField);

        fieldsPanel.add(new JLabel("Nazwisko:"));
        fieldsPanel.add(lastNameField);

        fieldsPanel.add(new JLabel("Adres:"));
        fieldsPanel.add(addressField);

        fieldsPanel.add(new JLabel("Nr legitymacji:"));
        fieldsPanel.add(cardNumberField);

        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonsPanel.add(addButton);
        buttonsPanel.add(deleteButton);

        mainPanel.setBorder(BorderFactory.createTitledBorder("Dane wypożyczającego"));
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

            if (firstName.isEmpty() || lastName.isEmpty() || address.isEmpty() || cardNumber.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Uzupełnij wszystkie pola.");
                return;
            }

            Reader reader = new Reader(
                    id,
                    firstName,
                    lastName,
                    address,
                    cardNumber
            );

            libraryService.addReader(reader);

            refreshTable();
            clearForm();

            JOptionPane.showMessageDialog(this, "Dodano wypożyczającego.");
        } catch (Exception exception) {
            JOptionPane.showMessageDialog(this, "Błąd: " + exception.getMessage());
        }
    }

    private void deleteSelectedReader() {
        int selectedRow = readersTable.getSelectedRow();

        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Wybierz wypożyczającego do usunięcia.");
            return;
        }

        Reader selectedReader = readerTableModel.getReaderAt(selectedRow);

        try {
            libraryService.deleteReader(selectedReader);

            refreshTable();

            JOptionPane.showMessageDialog(this, "Usunięto wypożyczającego.");
        } catch (Exception exception) {
            JOptionPane.showMessageDialog(this, "Błąd: " + exception.getMessage());
        }
    }

    private void refreshTable() {
        readerTableModel.refresh();
    }

    private void clearForm() {
        firstNameField.setText("");
        lastNameField.setText("");
        addressField.setText("");
        cardNumberField.setText("");
    }

}
