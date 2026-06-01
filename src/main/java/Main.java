import model.Book;
import model.Department;
import model.LibraryData;
import model.Shelf;
import service.LibraryService;
import ui.MainFrame;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LibraryData data = new LibraryData();
            LibraryService service = new LibraryService(data);

            MainFrame frame = new MainFrame(service);
            frame.setVisible(true);
        });
    }
}