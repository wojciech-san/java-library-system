import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.LibraryService;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class LibraryServiceTest {

    private LibraryData data;
    private LibraryService service;

    private Department department;
    private Shelf shelf;
    private Book book;
    private Reader reader;

    @BeforeEach
    void setUp() {
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
    void shouldAddBook() {
        service.addBook(book);

        assertEquals(1, data.getBooks().size());
        assertEquals("Java. Podstawy", data.getBooks().get(0).getTitle());
    }

    @Test
    void shouldAddDepartment() {
        service.addDepartment(department);

        assertEquals(1, data.getDepartments().size());
        assertEquals("IT", data.getDepartments().get(0).getCode());
    }

    @Test
    void shouldAddShelf() {
        service.addShelf(shelf);

        assertEquals(1, data.getShelves().size());
        assertEquals("A", data.getShelves().get(0).getRack());
    }

    @Test
    void shouldAddReader() {
        service.addReader(reader);

        assertEquals(1, data.getReaders().size());
        assertEquals("Anna", data.getReaders().get(0).getFirstName());
    }

    @Test
    void shouldBorrowAvailableBook() {
        service.addBook(book);
        service.addReader(reader);

        Loan loan = service.borrowBook(book, reader, LocalDate.now().plusDays(14));

        assertNotNull(loan);
        assertEquals(1, data.getLoans().size());
        assertFalse(service.isBookAvailable(book));
    }

    @Test
    void shouldNotBorrowAlreadyBorrowedBook() {
        service.addBook(book);
        service.addReader(reader);

        service.borrowBook(book, reader, LocalDate.now().plusDays(14));

        assertThrows(
                IllegalStateException.class,
                () -> service.borrowBook(book, reader, LocalDate.now().plusDays(14))
        );
    }

    @Test
    void shouldReturnBorrowedBook() {
        service.addBook(book);
        service.addReader(reader);

        Loan loan = service.borrowBook(book, reader, LocalDate.now().plusDays(14));

        service.returnBook(loan);

        assertTrue(service.isBookAvailable(book));
        assertNotNull(loan.getReturnDate());
    }

    @Test
    void shouldNotReturnAlreadyReturnedBook() {
        service.addBook(book);
        service.addReader(reader);

        Loan loan = service.borrowBook(book, reader, LocalDate.now().plusDays(14));
        service.returnBook(loan);

        assertThrows(
                IllegalStateException.class,
                () -> service.returnBook(loan)
        );

    }

    @Test
    void shouldSearchBooksByTitle() {
        service.addBook(book);
        List<Book> results = service.searchBooks("java");

        assertEquals(1, results.size());
        assertEquals(book, results.get(0));
    }

    @Test
    void shouldFindOverdueLoans() {
        service.addBook(book);
        service.addReader(reader);

        service.borrowBook(book, reader, LocalDate.now().minusDays(1));

        List<Loan> overdueLoans = service.findOverdueLoans();

        assertEquals(1, overdueLoans.size());
    }

    @Test
    void shouldDeleteShelfWhenNoBooksAssigned() {
        service.addShelf(shelf);

        service.deleteShelf(shelf);

        assertEquals(0, data.getShelves().size());
    }

    @Test
    void shouldNotDeleteShelfWhenBooksAssigned() {
        service.addShelf(shelf);
        service.addBook(book);

        assertThrows(
                IllegalStateException.class,
                () -> service.deleteShelf(shelf)
        );
    }

    @Test
    void shouldDeleteAvailableBook() {
        service.addBook(book);

        service.deleteBook(book);

        assertEquals(0, data.getBooks().size());
    }

    @Test
    void shouldNotDeleteBorrowedBook() {
        service.addBook(book);
        service.addReader(reader);

        service.borrowBook(book, reader, LocalDate.now().plusDays(14));

        assertThrows(
                IllegalStateException.class,
                () -> service.deleteBook(book)
        );
    }

    @Test
    void shouldUpdateBook() {
        service.addBook(book);

        Book updated = new Book(
                1L,
                "Java. Zaawansowane",
                "Jan Kowalski, Adam Nowak",
                "WIT Press",
                2025,
                "978-83-0000-000-1",
                shelf,
                department
        );

        service.updateBook(updated);

        Book stored = data.getBooks().get(0);
        assertEquals("Java. Zaawansowane", stored.getTitle());
        assertEquals(2025, stored.getYear());
        assertEquals("978-83-0000-000-1", stored.getIsbn());
    }

    @Test
    void shouldNotUpdateNonExistingBook() {
        assertThrows(
                IllegalArgumentException.class,
                () -> service.updateBook(book)
        );
    }

    @Test
    void shouldUpdateDepartment() {
        service.addDepartment(department);

        Department updated = new Department(1L, "CS", "Computer Science", "Programming books");
        service.updateDepartment(updated);

        Department stored = data.getDepartments().get(0);
        assertEquals("CS", stored.getCode());
        assertEquals("Computer Science", stored.getName());
    }

    @Test
    void shouldUpdateShelf() {
        service.addShelf(shelf);

        Shelf updated = new Shelf(1L, "B", "2");
        service.updateShelf(updated);

        Shelf stored = data.getShelves().get(0);
        assertEquals("B", stored.getRack());
        assertEquals("2", stored.getShelf());
    }

    @Test
    void shouldUpdateReader() {
        service.addReader(reader);

        Reader updated = new Reader(1L, "Maria", "Kowalska", "Krakow", "99999");
        service.updateReader(updated);

        Reader stored = data.getReaders().get(0);
        assertEquals("Maria", stored.getFirstName());
        assertEquals("Krakow", stored.getAddress());
    }

    @Test
    void shouldDeleteDepartmentWhenNoBooksAssigned() {
        service.addDepartment(department);

        service.deleteDepartment(department);

        assertEquals(0, data.getDepartments().size());
    }

    @Test
    void shouldNotDeleteDepartmentWhenBooksAssigned() {
        service.addDepartment(department);
        service.addBook(book);

        assertThrows(
                IllegalStateException.class,
                () -> service.deleteDepartment(department)
        );
    }

    @Test
    void shouldDeleteReaderWhenNoActiveLoans() {
        service.addReader(reader);

        service.deleteReader(reader);

        assertEquals(0, data.getReaders().size());
    }

    @Test
    void shouldNotDeleteReaderWithActiveLoan() {
        service.addBook(book);
        service.addReader(reader);

        service.borrowBook(book, reader, LocalDate.now().plusDays(14));

        assertThrows(
                IllegalStateException.class,
                () -> service.deleteReader(reader)
        );
    }

    @Test
    void shouldDeleteReaderAfterBookReturned() {
        service.addBook(book);
        service.addReader(reader);

        Loan loan = service.borrowBook(book, reader, LocalDate.now().plusDays(14));
        service.returnBook(loan);

        service.deleteReader(reader);

        assertEquals(0, data.getReaders().size());
    }

    @Test
    void shouldFindActiveLoans() {
        service.addBook(book);
        service.addReader(reader);

        service.borrowBook(book, reader, LocalDate.now().plusDays(14));

        List<Loan> activeLoans = service.findActiveLoans();

        assertEquals(1, activeLoans.size());
        assertNull(activeLoans.get(0).getReturnDate());
    }

    @Test
    void shouldNotIncludeReturnedLoansInActiveLoans() {
        service.addBook(book);
        service.addReader(reader);

        Loan loan = service.borrowBook(book, reader, LocalDate.now().plusDays(14));
        service.returnBook(loan);

        assertEquals(0, service.findActiveLoans().size());
    }

    @Test
    void shouldFindLoansByReader() {
        service.addBook(book);
        service.addReader(reader);

        service.borrowBook(book, reader, LocalDate.now().plusDays(14));

        List<Loan> readerLoans = service.findLoansByReader(reader);

        assertEquals(1, readerLoans.size());
        assertEquals(reader, readerLoans.get(0).getReader());
    }

    @Test
    void shouldReturnEmptyListForBlankSearchPhrase() {
        service.addBook(book);

        assertTrue(service.searchBooks("").isEmpty());
        assertTrue(service.searchBooks("   ").isEmpty());
    }

}
