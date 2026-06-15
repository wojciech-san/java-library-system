package ui.tablemodel;

import model.Reader;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class ReaderTableModel extends AbstractTableModel {
    private String[] columns = {
            "ID",
            "Imię",
            "Nazwisko",
            "Adres",
            "Nr legitymacji"
    };
    private final List<Reader> readers;

    public ReaderTableModel(List<Reader> readers) {
        this.readers = readers;
    }

    public Reader getReaderAt(int rowIndex) {
        return readers.get(rowIndex);
    }

    @Override
    public int getRowCount() {
        return readers.size();
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
        Reader reader = readers.get(rowIndex);

        return switch (columnIndex) {
            case 0 -> reader.getId();
            case 1 -> reader.getFirstName();
            case 2 -> reader.getLastName();
            case 3 -> reader.getAddress();
            case 4 -> reader.getCardNumber();
            default -> "";
        };
    }

    public void refresh() {
        fireTableDataChanged();
    }
}
