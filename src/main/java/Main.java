import model.Book;
import model.Department;
import model.LibraryData;
import model.Shelf;

public class Main {
    public static void main(String[] args) {
        LibraryData data = new LibraryData();

        Department department = new Department(
                1L,
                "IT",
                "Informatyka",
                "Ksiazki zwiazane z programowaniem i komputerami"
        );
        Shelf shelf = new Shelf(
                1L,
                "A",
                "1"
        );
        Book book = new Book(
                1L,
                "Effective Java",
                "Joshua Bloch",
                "Addison-Wesley",
                2018,
                "978-0134685991",
                shelf,
                department
        );

        data.getDepartments().add(department);
        data.getShelves().add(shelf);
        data.getBooks().add(book);

        System.out.println("liczba ksiazek: "+data.getBooks().size());
        System.out.println("Pierwsza ksiazka: "+data.getBooks().get(0));
    }
}