package ui.tablemodel;

import model.Shelf;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class ShelfTableModel extends AbstractTableModel {
    private final String[] columns = {
            "ID",
            "Regał",
            "Półka"
    };
    private final List<Shelf> shelves;

    public ShelfTableModel(List<Shelf>shelves){
        this.shelves=shelves;
    }
    public Shelf getShelfAt(int rowIndex){
        return shelves.get(rowIndex);
    }
    @Override
    public int getRowCount(){
        return shelves.size();
    }
    @Override
    public int getColumnCount(){
        return columns.length;
    }
    @Override
    public String getColumnName(int column){
        return columns[column];
    }
    @Override
    public Object getValueAt(int rowIndex, int columnIndex){
        Shelf shelf = shelves.get(rowIndex);

        return switch (columnIndex){
            case 0 -> shelf.getId();
            case 1 -> shelf.getRack();
            case 2 -> shelf.getShelf();
            default -> "";
        };
    }
    public void refresh(){
        fireTableDataChanged();
    }
}
