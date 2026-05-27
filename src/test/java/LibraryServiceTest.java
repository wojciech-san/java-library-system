import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.LibraryService;

import static org.junit.jupiter.api.Assertions.*;

public class LibraryServiceTest {

    private LibraryData data;
    private LibraryService service;

    private Department department;
    private Shelf shelf;
    private Book book;
    private Reader reader;

    @BeforeEach
    void setUp(){
        data = new LibraryData();
        service = new LibraryService(data);

        department = new Department(
                1L,
                "IT",
                "Informatyka",
                "Książki o programowaniu"
        );

        shelf = new Shelf(
                1L,
                "A",
                "1"
        );

        book = new Book(
                1L,
                "Java. Podstawy",
                "Jan Kowalski",
                "WIT Press",
                2024,
                "978-83-0000-000-0",
                shelf,
                department
        );

        reader = new Reader(
                1L,
                "Anna",
                "Nowak",
                "Warszawa",
                "12321"
        );
    }
    @Test
    void shouldAddBook(){
        service.addBook(book);

        assertEquals(1,data.getBooks().size());
        assertEquals("Java. Podstawy",data.getBooks().get(0).getTitle());
    }

}
