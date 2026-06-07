package ui.tablemodel;

import model.Loan;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class LoanTableModel extends AbstractTableModel {
    private final String[] columns = {
            "ID",
            "Książka",
            "Czytelnik",
            "Data wypożyczenia",
            "Termin zwrotu",
            "Data zwrotu",
            "Status"
    };
    private final List<Loan> loans;

    public LoanTableModel(List<Loan> loans) {
        this.loans = loans;
    }

    public Loan getLoanAt(int rowIndex) {
        return loans.get(rowIndex);
    }

    @Override
    public int getRowCount() {
        return loans.size();
    }

    @Override
    public int getColumnCount() {
        return columns.length;
    }

    @Override
    public String getColumnName(int column) {
        return columns[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Loan loan = loans.get(rowIndex);

        return switch (columnIndex) {
            case 0 -> loan.getId();
            case 1 -> loan.getBook() != null ? loan.getBook().toString() : "";
            case 2 -> loan.getReader() != null ? loan.getReader().toString() : "";
            case 3 -> loan.getLoanDate();
            case 4 -> loan.getDueDate();
            case 5 -> loan.getReturnDate() != null ? loan.getReturnDate() : "";
            case 6 -> loan.getReturnDate() == null ? "Aktywne" : "Zwrócone";
            default -> "";
        };
    }

    public void refresh() {
        fireTableDataChanged();
    }
}
