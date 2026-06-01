package ui.tablemodel;

import model.Book;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class BookTableModel extends AbstractTableModel {

    private final String[] columns = {
            "ID",
            "Tytuł",
            "Autorzy",
            "Wydawnictwo",
            "Rok",
            "ISBN",
            "Dział",
            "Miejsce"
    };
    private List<Book> books;

    public BookTableModel(List<Book> books) {
        this.books = books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
        fireTableDataChanged();
    }
    public Book getBookAt(int rowIndex) {
        return books.get(rowIndex);
    }
    @Override
    public int getRowCount() {
        return books.size();
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
        Book book = books.get(rowIndex);

        return switch (columnIndex) {
            case 0 -> book.getId();
            case 1 -> book.getTitle();
            case 2 -> book.getAuthors();
            case 3 -> book.getPublisher();
            case 4 -> book.getYear();
            case 5 -> book.getIsbn();
            case 6 -> book.getDepartment() != null ? book.getDepartment().getName() : "";
            case 7 -> book.getShelf() != null ? book.getShelf().toString() : "";
            default -> "";
        };

    }


}
