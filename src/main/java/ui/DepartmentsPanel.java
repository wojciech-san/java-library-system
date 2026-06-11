package ui;

import model.Department;
import service.LibraryService;
import ui.tablemodel.DepartmentTableModel;

import javax.swing.*;
import java.awt.*;

public class DepartmentsPanel extends JPanel {
    private final LibraryService libraryService;

    private final DepartmentTableModel departmentTableModel;
    private final JTable departmentsTable;

    private final JTextField codeField;
    private final JTextField nameField;
    private final JTextArea descriptionArea;

    private final JButton addButton;
    private final JButton deleteButton;

    public DepartmentsPanel(LibraryService libraryService) {
        this.libraryService = libraryService;

        this.departmentTableModel = new DepartmentTableModel(
                libraryService.getData().getDepartments()
        );
        this.departmentsTable = new JTable(departmentTableModel);

        this.codeField = new JTextField();
        this.nameField = new JTextField();
        this.descriptionArea = new JTextArea(3, 20);

        this.addButton = new JButton("Dodaj");
        this.deleteButton = new JButton("Usuń");

        setLayout(new BorderLayout());

        add(createTablePanel(), BorderLayout.CENTER);
        add(createFormPanel(), BorderLayout.SOUTH);

        addListeners();
    }
    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JScrollPane(departmentsTable), BorderLayout.CENTER);
        return panel;
    }

    private JPanel createFormPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout());

        JPanel fieldsPanel = new JPanel(new GridLayout(3, 2, 5, 5));

        fieldsPanel.add(new JLabel("Kod działu:"));
        fieldsPanel.add(codeField);

        fieldsPanel.add(new JLabel("Nazwa:"));
        fieldsPanel.add(nameField);

        fieldsPanel.add(new JLabel("Opis:"));
        fieldsPanel.add(new JScrollPane(descriptionArea));

        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonsPanel.add(addButton);
        buttonsPanel.add(deleteButton);

        mainPanel.setBorder(BorderFactory.createTitledBorder("Dane działu"));
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
                JOptionPane.showMessageDialog(this, "Kod i nazwa działu są wymagane.");
                return;
            }

            Department department = new Department(
                    id,
                    code,
                    name,
                    description
            );

            libraryService.addDepartment(department);

            refreshTable();
            clearForm();

            JOptionPane.showMessageDialog(this, "Dodano dział literatury.");
        } catch (Exception exception) {
            JOptionPane.showMessageDialog(this, "Błąd: " + exception.getMessage());
        }
    }

    private void deleteSelectedDepartment() {
        int selectedRow = departmentsTable.getSelectedRow();

        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Wybierz dział do usunięcia.");
            return;
        }

        Department selectedDepartment = departmentTableModel.getDepartmentAt(selectedRow);

        try {
            libraryService.deleteDepartment(selectedDepartment);
            refreshTable();

            JOptionPane.showMessageDialog(this, "Usunięto dział literatury.");
        } catch (Exception exception) {
            JOptionPane.showMessageDialog(this, "Błąd: " + exception.getMessage());
        }
    }

    private void refreshTable() {
        departmentTableModel.refresh();
    }

    private void clearForm() {
        codeField.setText("");
        nameField.setText("");
        descriptionArea.setText("");
    }

    public void refreshView() {
        refreshTable();
    }

}
