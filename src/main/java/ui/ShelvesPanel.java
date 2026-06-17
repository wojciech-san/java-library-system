package ui;

import i18n.LanguageManager;
import model.Shelf;
import service.LibraryService;
import ui.tablemodel.ShelfTableModel;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

/**
 * Panel odpowiedzialny za wyświetlanie, dodawanie i usuwanie półek oraz regałów.
 */
public class ShelvesPanel extends JPanel {

    private final LibraryService libraryService;
    private final LanguageManager languageManager;

    private final ShelfTableModel shelfTableModel;
    private final JTable shelvesTable;

    private final JTextField rackField;
    private final JTextField shelfField;

    private final JButton addButton;
    private final JButton deleteButton;

    private final JLabel rackLabel;
    private final JLabel shelfLabel;

    private TitledBorder formBorder;

    public ShelvesPanel(LibraryService libraryService, LanguageManager languageManager) {
        this.libraryService = libraryService;
        this.languageManager = languageManager;

        this.shelfTableModel = new ShelfTableModel(
                libraryService.getData().getShelves()
        );
        this.shelvesTable = new JTable(shelfTableModel);

        this.rackField = new JTextField();
        this.shelfField = new JTextField();

        this.addButton = new JButton();
        this.deleteButton = new JButton();

        this.rackLabel = new JLabel();
        this.shelfLabel = new JLabel();

        setLayout(new BorderLayout());

        add(createTablePanel(), BorderLayout.CENTER);
        add(createFormPanel(), BorderLayout.SOUTH);

        addListeners();
        reloadTexts();
    }

    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JScrollPane(shelvesTable), BorderLayout.CENTER);
        return panel;
    }

    private JPanel createFormPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout());

        JPanel fieldsPanel = new JPanel(new GridLayout(2, 2, 5, 5));

        fieldsPanel.add(rackLabel);
        fieldsPanel.add(rackField);

        fieldsPanel.add(shelfLabel);
        fieldsPanel.add(shelfField);

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
        addButton.addActionListener(event -> addShelf());
        deleteButton.addActionListener(event -> deleteSelectedShelf());
    }

    private void addShelf() {
        try {
            Long id = System.currentTimeMillis();

            String rack = rackField.getText().trim();
            String shelfName = shelfField.getText().trim();

            if (rack.isEmpty() || shelfName.isEmpty()) {
                JOptionPane.showMessageDialog(
                        this,
                        languageManager.get("message.rackAndShelfRequired")
                );
                return;
            }

            Shelf shelf = new Shelf(id, rack, shelfName);

            libraryService.addShelf(shelf);

            refreshTable();
            clearForm();

            JOptionPane.showMessageDialog(this, languageManager.get("message.shelfAdded"));
        } catch (Exception exception) {
            JOptionPane.showMessageDialog(
                    this,
                    languageManager.get("message.error") + " " + exception.getMessage()
            );
        }
    }

    private void deleteSelectedShelf() {
        int selectedRow = shelvesTable.getSelectedRow();

        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(
                    this,
                    languageManager.get("message.selectShelfToDelete")
            );
            return;
        }

        Shelf selectedShelf = shelfTableModel.getShelfAt(selectedRow);

        try {
            libraryService.deleteShelf(selectedShelf);

            refreshTable();

            JOptionPane.showMessageDialog(this, languageManager.get("message.shelfDeleted"));
        } catch (Exception exception) {
            JOptionPane.showMessageDialog(
                    this,
                    languageManager.get("message.error") + " " + exception.getMessage()
            );
        }
    }

    private void refreshTable() {
        shelfTableModel.fireTableDataChanged();
    }

    private void clearForm() {
        rackField.setText("");
        shelfField.setText("");
    }

    public void refreshView() {
        refreshTable();
    }

    public void reloadTexts() {
        addButton.setText(languageManager.get("button.add"));
        deleteButton.setText(languageManager.get("button.delete"));

        rackLabel.setText(languageManager.get("label.rack"));
        shelfLabel.setText(languageManager.get("label.shelf"));

        formBorder.setTitle(languageManager.get("border.shelfData"));

        shelfTableModel.setColumnNames(new String[]{
                languageManager.get("table.id"),
                languageManager.get("table.rack"),
                languageManager.get("table.shelf")
        });

        revalidate();
        repaint();
    }
}