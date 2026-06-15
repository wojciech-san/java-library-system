package ui.tablemodel;

import model.Department;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class DepartmentTableModel extends AbstractTableModel {
    private String[] columns = {
            "ID",
            "Kod",
            "Nazwa",
            "Opis"
    };
    private final List<Department> departments;

    public DepartmentTableModel(List<Department> departments) {
        this.departments = departments;
    }

    public Department getDepartmentAt(int rowIndex) {
        return departments.get(rowIndex);
    }

    @Override
    public int getRowCount() {
        return departments.size();
    }

    @Override
    public int getColumnCount() {
        return columns.length;
    }

    @Override
    public String getColumnName(int column) {
        return columns[column];
    }

    public void setColumnNames(String[] columns) {
        this.columns = columns;
        fireTableStructureChanged();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Department department = departments.get(rowIndex);

        return switch (columnIndex) {
            case 0 -> department.getId();
            case 1 -> department.getCode();
            case 2 -> department.getName();
            case 3 -> department.getDescription();
            default -> "";
        };
    }

    public void refresh() {
        fireTableDataChanged();
    }


}
