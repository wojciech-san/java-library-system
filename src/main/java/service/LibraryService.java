package service;

import model.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Klasa odpowiadajaca za logike biznesowa systemu bibliotycznego
 */
public class LibraryService {
    //obiekt zwierajacy dane aplikacji
    private final LibraryData data;

    // serwis dla przetwarzania danych biblioteki
    // @param data kontener danych aplikacji
    public LibraryService(LibraryData data) {
        this.data = data;
    }

    public LibraryData getData() {
        return data;
    }

    // metoda dodawanie ksiazki
    public void addBook(Book book) {
        data.getBooks().add(book);
    }

    // metoda dodawanie departamentu
    public void addDepartment(Department department) {
        data.getDepartments().add(department);
    }

    // metoda dodawanie polki
    public void addShelf(Shelf shelf) {
        data.getShelves().add(shelf);
    }

    // metoda dodawanie czytelnika
    public void addReader(Reader reader) {
        data.getReaders().add(reader);
    }

    public void deleteBook(Book book) {
        if (!isBookAvailable(book)) {
            throw new IllegalStateException("Nie mozna usunac wypozyczonej ksiazki");
        }
        data.getBooks().remove(book);
    }

    public void deleteShelf(Shelf shelf) {
        boolean hasBooksAssigned = data.getBooks().stream().anyMatch(book -> book.getShelf().equals(shelf));

        if (hasBooksAssigned) {
            throw new IllegalStateException("Nie mozna usunac polki z przypisanymi ksiazkami");
        }
        data.getShelves().remove(shelf);
    }

    public boolean isBookAvailable(Book book) {
        return data.getLoans()
                .stream()
                .noneMatch(loan ->
                        loan.getBook().equals(book)
                                && loan.getReturnDate() == null
                );
    }

    public Loan borrowBook(Book book, Reader reader, LocalDate dueDate) {
        if (!isBookAvailable(book)) {
            throw new IllegalStateException("Ksiazka jest juz wypozyczona.");
        }

        Loan loan = new Loan(
                System.currentTimeMillis(),
                book,
                reader,
                LocalDate.now(),
                dueDate, null
        );

        data.getLoans().add(loan);

        return loan;
    }

    public void returnBook(Loan loan) {
        if (loan.getReturnDate() != null) {
            throw new IllegalStateException("Ksiazka zostala zwrocona.");
        }
        loan.returnBook(LocalDate.now());
    }

    public List<Loan> findOverdueLoans() {
        return data.getLoans()
                .stream()
                .filter(Loan::isOverdue)
                .collect(Collectors.toList());
    }

    public List<Book> searchBooks(String phrase) {
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

}
