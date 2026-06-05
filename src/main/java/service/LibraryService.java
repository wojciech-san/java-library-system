package service;

import model.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Klasa odpowiadajaca za logike biznesowa systemu bibliotycznego.
 */
public class LibraryService {
    //obiekt zwierajacy dane aplikacji
    private final LibraryData data;

    public LibraryService(LibraryData data) {
        if (data == null) {
            throw new IllegalArgumentException("Dane biblioteki nie moga byc puste");
        }
        this.data = data;
    }

    /**
     * Zwraca dane aplikacji bibliotecznej.
     */
    public LibraryData getData() {
        return data;
    }

    /**
     * Dodaje nowa ksiazke do biblioteki.
     */
    public void addBook(Book book) {
        validateNotNull(book, "Book");
        validateBookFields(book);
        data.getBooks().add(book);
    }

    /**
     * Dodaje nowy dzial literatury.
     */
    public void addDepartment(Department department) {
        validateNotNull(department, "Department");
        validateDepartmentFields(department);
        data.getDepartments().add(department);
    }

    /**
     * Dodaje nowa polke.
     */
    public void addShelf(Shelf shelf) {
        validateNotNull(shelf, "Shelf");
        data.getShelves().add(shelf);
    }

    /**
     * Dodaje nowego czytelnika.
     */
    public void addReader(Reader reader) {
        validateNotNull(reader, "Reader");
        validateReaderFields(reader);
        data.getReaders().add(reader);
    }

    /**
     * Aktualizuje dane istniejacej ksiazki.
     */
    public void updateBook(Book book) {
        validateNotNull(book, "Book");
        validateBookFields(book);

        Book existing = findBookById(book.getId());
        existing.setTitle(book.getTitle());
        existing.setAuthors(book.getAuthors());
        existing.setPublisher(book.getPublisher());
        existing.setYear(book.getYear());
        existing.setIsbn(book.getIsbn());
        existing.setShelf(book.getShelf());
        existing.setDepartment(book.getDepartment());
    }

    /**
     * Aktualizuje dane istniejacego dzialu literatury.
     */
    public void updateDepartment(Department department) {
        validateNotNull(department, "Department");
        validateDepartmentFields(department);

        Department existing = findDepartmentById(department.getId());
        existing.setCode(department.getCode());
        existing.setName(department.getName());
        existing.setDescription(department.getDescription());
    }

    /**
     * Aktualizuje dane istniejacej polki.
     */
    public void updateShelf(Shelf shelf) {
        validateNotNull(shelf, "Shelf");

        Shelf existing = findShelfById(shelf.getId());
        existing.setRack(shelf.getRack());
        existing.setShelf(shelf.getShelf());
    }

    /**
     * Aktualizuje dane istniejacego czytelnika.
     */
    public void updateReader(Reader reader) {
        validateNotNull(reader, "Reader");
        validateReaderFields(reader);

        Reader existing = findReaderById(reader.getId());
        existing.setFirstName(reader.getFirstName());
        existing.setLastName(reader.getLastName());
        existing.setAddress(reader.getAddress());
        existing.setCardNumber(reader.getCardNumber());
    }

    /**
     * Usuwa ksiazke, jesli nie jest aktualnie wypozyczona.
     */
    public void deleteBook(Book book) {
        validateNotNull(book, "Book");
        if (!isBookAvailable(book)) {
            throw new IllegalStateException("Nie mozna usunac wypozyczonej ksiazki");
        }
        data.getBooks().remove(book);
    }

    /**
     * Usuwa dzial literatury, jesli nie ma do niego przypisanych ksiazek.
     */
    public void deleteDepartment(Department department) {
        validateNotNull(department, "Department");

        boolean hasBooksAssigned = data.getBooks().stream()
                .anyMatch(book -> book.getDepartment() != null && book.getDepartment().equals(department));

        if (hasBooksAssigned) {
            throw new IllegalStateException("Nie mozna usunac dzialu z przypisanymi ksiazkami");
        }
        data.getDepartments().remove(department);
    }

    /**
     * Usuwa polke, jesli nie ma do niej przypisanych ksiazek.
     */
    public void deleteShelf(Shelf shelf) {
        validateNotNull(shelf, "Shelf");

        boolean hasBooksAssigned = data.getBooks().stream()
                .anyMatch(book -> book.getShelf() != null && book.getShelf().equals(shelf));

        if (hasBooksAssigned) {
            throw new IllegalStateException("Nie mozna usunac polki z przypisanymi ksiazkami");
        }
        data.getShelves().remove(shelf);
    }

    /**
     * Usuwa czytelnika, jesli nie ma aktywnych wypozyczen.
     */
    public void deleteReader(Reader reader) {
        validateNotNull(reader, "Reader");

        boolean hasActiveLoans = data.getLoans().stream()
                .anyMatch(loan -> loan.getReader().equals(reader) && loan.getReturnDate() == null);

        if (hasActiveLoans) {
            throw new IllegalStateException("Nie mozna usunac czytelnika z aktywnym wypozyczeniem");
        }
        data.getReaders().remove(reader);
    }

    /**
     * Sprawdza, czy ksiazka jest dostepna do wypozyczenia.
     */
    public boolean isBookAvailable(Book book) {
        validateNotNull(book, "Book");
        return data.getLoans()
                .stream()
                .noneMatch(loan ->
                        loan.getBook().equals(book)
                                && loan.getReturnDate() == null
                );
    }

    /**
     * Wypozycza ksiazke czytelnikowi.
     */
    public Loan borrowBook(Book book, Reader reader, LocalDate dueDate) {
        validateNotNull(book, "Book");
        validateNotNull(reader, "Reader");
        validateNotNull(dueDate, "Due Date");

        if (!isBookAvailable(book)) {
            throw new IllegalStateException("Ksiazka jest juz wypozyczona.");
        }

        Loan loan = new Loan(
                System.currentTimeMillis(),
                book,
                reader,
                LocalDate.now(),
                dueDate,
                null
        );

        data.getLoans().add(loan);

        return loan;
    }

    /**
     * Rejestruje zwrot wypozyczonej ksiazki.
     */
    public void returnBook(Loan loan) {
        validateNotNull(loan, "Loan");
        if (loan.getReturnDate() != null) {
            throw new IllegalStateException("Ksiazka zostala zwrocona.");
        }
        loan.returnBook(LocalDate.now());
    }

    /**
     * Zwraca liste aktywnych (niewroconych) wypozyczen.
     */
    public List<Loan> findActiveLoans() {
        return data.getLoans()
                .stream()
                .filter(loan -> loan.getReturnDate() == null)
                .collect(Collectors.toList());
    }

    /**
     * Zwraca liste przeterminowanych wypozyczen.
     */
    public List<Loan> findOverdueLoans() {
        return data.getLoans()
                .stream()
                .filter(Loan::isOverdue)
                .collect(Collectors.toList());
    }

    /**
     * Zwraca wypozyczenia danego czytelnika.
     */
    public List<Loan> findLoansByReader(Reader reader) {
        validateNotNull(reader, "Reader");
        return data.getLoans()
                .stream()
                .filter(loan -> loan.getReader().equals(reader))
                .collect(Collectors.toList());
    }

    /**
     * Wyszukuje ksiazki po tytule, autorze lub ISBN.
     */
    public List<Book> searchBooks(String phrase) {
        if (phrase == null || phrase.isBlank()) {
            return List.of();
        }

        String lowerPhrase = phrase.toLowerCase();

        return data.getBooks()
                .stream()
                .filter(book ->
                        book.getTitle().toLowerCase().contains(lowerPhrase)
                                || book.getAuthors().toLowerCase().contains(lowerPhrase)
                                || book.getIsbn().toLowerCase().contains(lowerPhrase)
                )
                .collect(Collectors.toList());
    }

    private Book findBookById(Long id) {
        return data.getBooks().stream()
                .filter(book -> book.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Nie znaleziono ksiazki"));
    }

    private Department findDepartmentById(Long id) {
        return data.getDepartments().stream()
                .filter(department -> department.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Nie znaleziono dzialu"));
    }

    private Shelf findShelfById(Long id) {
        return data.getShelves().stream()
                .filter(shelf -> shelf.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Nie znaleziono polki"));
    }

    private Reader findReaderById(Long id) {
        return data.getReaders().stream()
                .filter(reader -> reader.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Nie znaleziono czytelnika"));
    }

    private void validateNotNull(Object value, String name) {
        if (value == null) {
            throw new IllegalArgumentException(name + " nie moze byc puste");
        }
    }

    private void validateBookFields(Book book) {
        if (book.getTitle() == null || book.getTitle().isBlank()) {
            throw new IllegalArgumentException("Tytul ksiazki jest wymagany");
        }
        if (book.getAuthors() == null || book.getAuthors().isBlank()) {
            throw new IllegalArgumentException("Autorzy ksiazki sa wymagani");
        }
        if (book.getIsbn() == null || book.getIsbn().isBlank()) {
            throw new IllegalArgumentException("ISBN ksiazki jest wymagany");
        }
    }

    private void validateDepartmentFields(Department department) {
        if (department.getCode() == null || department.getCode().isBlank()) {
            throw new IllegalArgumentException("Kod dzialu jest wymagany");
        }
        if (department.getName() == null || department.getName().isBlank()) {
            throw new IllegalArgumentException("Nazwa dzialu jest wymagana");
        }
    }

    private void validateReaderFields(Reader reader) {
        if (reader.getFirstName() == null || reader.getFirstName().isBlank()) {
            throw new IllegalArgumentException("Imie czytelnika jest wymagane");
        }
        if (reader.getLastName() == null || reader.getLastName().isBlank()) {
            throw new IllegalArgumentException("Nazwisko czytelnika jest wymagane");
        }
        if (reader.getCardNumber() == null || reader.getCardNumber().isBlank()) {
            throw new IllegalArgumentException("Numer legitymacji jest wymagany");
        }
    }
}
