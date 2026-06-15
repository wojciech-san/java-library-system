package ui;

import i18n.LanguageManager;
import model.Department;
import service.LibraryService;
import ui.tablemodel.DepartmentTableModel;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

/**
 * Panel responsible for displaying, adding and deleting literature departments.
 */
public class DepartmentsPanel extends JPanel {

    private final LibraryService libraryService;
    private final LanguageManager languageManager;

    private final DepartmentTableModel departmentTableModel;
    private final JTable departmentsTable;

    private final JTextField codeField;
    private final JTextField nameField;
    private final JTextArea descriptionArea;

    private final JButton addButton;
    private final JButton deleteButton;

    private final JLabel codeLabel;
    private final JLabel nameLabel;
    private final JLabel descriptionLabel;

    private TitledBorder formBorder;

    public DepartmentsPanel(LibraryService libraryService, LanguageManager languageManager) {
        this.libraryService = libraryService;
        this.languageManager = languageManager;

        this.departmentTableModel = new DepartmentTableModel(
                libraryService.getData().getDepartments()
        );
        this.departmentsTable = new JTable(departmentTableModel);

        this.codeField = new JTextField();
        this.nameField = new JTextField();
        this.descriptionArea = new JTextArea(3, 20);

        this.addButton = new JButton();
        this.deleteButton = new JButton();

        this.codeLabel = new JLabel();
        this.nameLabel = new JLabel();
        this.descriptionLabel = new JLabel();

        setLayout(new BorderLayout());

        add(createTablePanel(), BorderLayout.CENTER);
        add(createFormPanel(), BorderLayout.SOUTH);

        addListeners();
        reloadTexts();
    }

    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JScrollPane(departmentsTable), BorderLayout.CENTER);
        return panel;
    }

    private JPanel createFormPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout());

        JPanel fieldsPanel = new JPanel(new GridLayout(3, 2, 5, 5));

        fieldsPanel.add(codeLabel);
        fieldsPanel.add(codeField);

        fieldsPanel.add(nameLabel);
        fieldsPanel.add(nameField);

        fieldsPanel.add(descriptionLabel);
        fieldsPanel.add(new JScrollPane(descriptionArea));

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
        addButton.addActionListener(event -> addDepartment());
        deleteButton.addActionListener(event -> deleteSelectedDepartment());
    }

    private void addDepartment() {
        try {
            Long id = System.currentTimeMillis();

            String code = codeField.getText().trim();
            String name = nameField.getText().trim();
            String description = descriptionArea.getText().trim();

            if (code.isEmpty() || name.isEmpty()) {
                JOptionPane.showMessageDialog(
                        this,
                        languageManager.get("message.departmentCodeAndNameRequired")
                );
                return;
            }

            Department department = new Department(id, code, name, description);

            libraryService.addDepartment(department);

            refreshTable();
            clearForm();

            JOptionPane.showMessageDialog(this, languageManager.get("message.departmentAdded"));
        } catch (Exception exception) {
            JOptionPane.showMessageDialog(
                    this,
                    languageManager.get("message.error") + " " + exception.getMessage()
            );
        }
    }

    private void deleteSelectedDepartment() {
        int selectedRow = departmentsTable.getSelectedRow();

        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(
                    this,
                    languageManager.get("message.selectDepartmentToDelete")
            );
            return;
        }

        Department selectedDepartment = departmentTableModel.getDepartmentAt(selectedRow);

        try {
            libraryService.deleteDepartment(selectedDepartment);

            refreshTable();

            JOptionPane.showMessageDialog(this, languageManager.get("message.departmentDeleted"));
        } catch (Exception exception) {
            JOptionPane.showMessageDialog(
                    this,
                    languageManager.get("message.error") + " " + exception.getMessage()
            );
        }
    }

    private void refreshTable() {
        departmentTableModel.fireTableDataChanged();
    }

    private void clearForm() {
        codeField.setText("");
        nameField.setText("");
        descriptionArea.setText("");
    }

    public void refreshView() {
        refreshTable();
    }

    public void reloadTexts() {
        addButton.setText(languageManager.get("button.add"));
        deleteButton.setText(languageManager.get("button.delete"));

        codeLabel.setText(languageManager.get("label.departmentCode"));
        nameLabel.setText(languageManager.get("label.name"));
        descriptionLabel.setText(languageManager.get("label.description"));

        formBorder.setTitle(languageManager.get("border.departmentData"));

        departmentTableModel.setColumnNames(new String[]{
                languageManager.get("table.id"),
                languageManager.get("table.departmentCode"),
                languageManager.get("table.name"),
                languageManager.get("table.description")
        });

        revalidate();
        repaint();
    }
}