package ui;

import model.Shelf;
import service.LibraryService;
import ui.tablemodel.ShelfTableModel;

import javax.swing.*;
import java.awt.*;

public class ShelvesPanel extends JPanel {
    private final LibraryService libraryService;

    private final ShelfTableModel shelfTableModel;
    private final JTable shelvesTable;

    private final JTextField rackField;
    private final JTextField shelfField;

    private final JButton addButton;
    private final JButton deleteButton;

    public ShelvesPanel(LibraryService libraryService) {
        this.libraryService = libraryService;

        this.shelfTableModel = new ShelfTableModel(
                libraryService.getData().getShelves()
        );
        this.shelvesTable = new JTable(shelfTableModel);
        this.rackField = new JTextField();
        this.shelfField = new JTextField();

        this.addButton = new JButton("Dodaj");
        this.deleteButton = new JButton("Usuń");

        setLayout(new BorderLayout());
        add(createTablePanel(), BorderLayout.CENTER);
        add(createFormPanel(), BorderLayout.SOUTH);

        addListeners();
    }

    private JPanel createTablePanel(){
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JScrollPane(shelvesTable),BorderLayout.CENTER);
        return panel;
    }
    private JPanel createFormPanel(){
        JPanel mainPanel = new JPanel(new BorderLayout());
        JPanel fieldsPanel = new JPanel(new GridLayout(2,2,5,5));

        fieldsPanel.add(new JLabel("Regał:"));
        fieldsPanel.add(rackField);

        fieldsPanel.add(new JLabel("Półka"));
        fieldsPanel.add(shelfField);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);

        mainPanel.setBorder(BorderFactory.createTitledBorder("Dane regału i półki"));
        mainPanel.add(fieldsPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        return mainPanel;
    }

    public void addListeners(){
        addButton.addActionListener(event ->addShelf());
        deleteButton.addActionListener(event -> deleteSelectedShelf());
    }

    private void addShelf(){
        try{
            Long id  = System.currentTimeMillis();

            String rack = rackField.getText().trim();
            String shelfName = shelfField.getText().trim();

            if (rack.isEmpty() || shelfName.isEmpty()) {
                JOptionPane.showMessageDialog(this,"Regał i półka są wymagane.");
                return;
            }
            Shelf shelf = new Shelf(
                    id,
                    rack,
                    shelfName
            );
            libraryService.addShelf(shelf);

            refreshTable();
            clearForm();

            JOptionPane.showMessageDialog(this,"Dodano regał i półkę");

        } catch (Exception exception){
            JOptionPane.showMessageDialog(this,"Błąd: "+ exception.getMessage());
        }
    }
    private void deleteSelectedShelf(){
        int selectedRow = shelvesTable.getSelectedRow();
        if (selectedRow <0) {
            JOptionPane.showMessageDialog(this,"Wybierz regał/półkę do usunięcia");
            return;
        }
        Shelf selectedShelf = shelfTableModel.getShelfAt(selectedRow);
        try{
            libraryService.deleteShelf(selectedShelf);
            refreshTable();

            JOptionPane.showMessageDialog(this,"Usunięto regał/półkę");
        } catch (Exception exception){
            JOptionPane.showMessageDialog(this,"Błąd: "+exception.getMessage());
        }
    }

    private void refreshTable(){
        shelfTableModel.refresh();
    }
    private void clearForm(){
        rackField.setText("");
        shelfField.setText("");
    }

    public void refreshView() {
        refreshTable();
    }

}
