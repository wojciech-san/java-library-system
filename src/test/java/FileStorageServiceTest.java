package storage;

import model.Book;
import model.Department;
import model.LibraryData;
import model.Loan;
import model.Reader;
import model.Shelf;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class FileStorageServiceTest {

    @Test
    void shouldSaveAndLoadLibraryData() throws Exception {
        LibraryData data = new LibraryData();

        Department department = new Department(
                1L,
                "IT",
                "Informatyka",
                "Książki o programowaniu"
        );

        Shelf shelf = new Shelf(
                2L,
                "A",
                "1"
        );

        Reader reader = new Reader(
                3L,
                "Adam",
                "Nowak",
                "Warszawa",
                "12345"
        );

        Book book = new Book(
                4L,
                "Java. Podstawy",
                "Jan Kowalski",
                "WIT Press",
                2024,
                "978-83-0000-000-0",
                shelf,
                department
        );

        Loan loan = new Loan(
                5L,
                book,
                reader,
                LocalDate.of(2026, 6, 1),
                LocalDate.of(2026, 6, 15),
                null
        );

        data.getDepartments().add(department);
        data.getShelves().add(shelf);
        data.getReaders().add(reader);
        data.getBooks().add(book);
        data.getLoans().add(loan);

        FileStorageService storageService = new FileStorageService();

        File file = File.createTempFile("library-test", ".bin");

        storageService.save(data, file);

        LibraryData loadedData = storageService.load(file);

        assertEquals(1, loadedData.getDepartments().size());
        assertEquals(1, loadedData.getShelves().size());
        assertEquals(1, loadedData.getReaders().size());
        assertEquals(1, loadedData.getBooks().size());
        assertEquals(1, loadedData.getLoans().size());

        Department loadedDepartment = loadedData.getDepartments().get(0);
        Shelf loadedShelf = loadedData.getShelves().get(0);
        Reader loadedReader = loadedData.getReaders().get(0);
        Book loadedBook = loadedData.getBooks().get(0);
        Loan loadedLoan = loadedData.getLoans().get(0);

        assertEquals(1L, loadedDepartment.getId());
        assertEquals("IT", loadedDepartment.getCode());
        assertEquals("Informatyka", loadedDepartment.getName());

        assertEquals(2L, loadedShelf.getId());
        assertEquals("A", loadedShelf.getRack());
        assertEquals("1", loadedShelf.getShelf());

        assertEquals(3L, loadedReader.getId());
        assertEquals("Adam", loadedReader.getFirstName());
        assertEquals("Nowak", loadedReader.getLastName());
        assertEquals("12345", loadedReader.getCardNumber());

        assertEquals(4L, loadedBook.getId());
        assertEquals("Java. Podstawy", loadedBook.getTitle());
        assertEquals("Jan Kowalski", loadedBook.getAuthors());
        assertEquals("WIT Press", loadedBook.getPublisher());
        assertEquals(2024, loadedBook.getYear());
        assertEquals("978-83-0000-000-0", loadedBook.getIsbn());

        assertNotNull(loadedBook.getDepartment());
        assertNotNull(loadedBook.getShelf());
        assertEquals("IT", loadedBook.getDepartment().getCode());
        assertEquals("A", loadedBook.getShelf().getRack());

        assertEquals(5L, loadedLoan.getId());
        assertNotNull(loadedLoan.getBook());
        assertNotNull(loadedLoan.getReader());
        assertEquals("Java. Podstawy", loadedLoan.getBook().getTitle());
        assertEquals("Adam", loadedLoan.getReader().getFirstName());
        assertEquals(LocalDate.of(2026, 6, 1), loadedLoan.getLoanDate());
        assertEquals(LocalDate.of(2026, 6, 15), loadedLoan.getDueDate());
        assertNull(loadedLoan.getReturnDate());

        file.delete();
    }

    @Test
    void shouldSaveAndLoadReturnedLoan() throws Exception {
        LibraryData data = new LibraryData();

        Department department = new Department(
                1L,
                "LIT",
                "Literatura",
                "Książki literackie"
        );

        Shelf shelf = new Shelf(
                2L,
                "B",
                "3"
        );

        Reader reader = new Reader(
                3L,
                "Ewa",
                "Kowalska",
                "Kraków",
                "67890"
        );

        Book book = new Book(
                4L,
                "Lalka",
                "Bolesław Prus",
                "Wydawnictwo ABC",
                1890,
                "978-83-1111-111-1",
                shelf,
                department
        );

        Loan loan = new Loan(
                5L,
                book,
                reader,
                LocalDate.of(2026, 5, 1),
                LocalDate.of(2026, 5, 14),
                null
        );

        loan.returnBook(LocalDate.of(2026, 5, 10));

        data.getDepartments().add(department);
        data.getShelves().add(shelf);
        data.getReaders().add(reader);
        data.getBooks().add(book);
        data.getLoans().add(loan);

        FileStorageService storageService = new FileStorageService();

        File file = File.createTempFile("library-returned-test", ".bin");

        storageService.save(data, file);

        LibraryData loadedData = storageService.load(file);

        Loan loadedLoan = loadedData.getLoans().get(0);

        assertEquals(LocalDate.of(2026, 5, 1), loadedLoan.getLoanDate());
        assertEquals(LocalDate.of(2026, 5, 14), loadedLoan.getDueDate());
        assertEquals(LocalDate.of(2026, 5, 10), loadedLoan.getReturnDate());

        file.delete();
    }
}