package service;

import model.*;

/**
 * Klasa odpowiadajaca za logike biznesowa systemu bibliotycznego
 */
public class LibraryService {
    //obiekt zwierajacy dane aplikacji
    private final LibraryData data;

    // serwis dla przetwarzania danych biblioteki
    // @param data kontener danych aplikacji
    public LibraryService(LibraryData data){
        this.data = data;
    }

    public LibraryData getData() {
        return data;
    }
    // metoda dodawanie ksiazki
    public void addBook(Book book){
        data.getBooks().add(book);
    }

    // metoda dodawanie departamentu
    public void addDepartment(Department department){
        data.getDepartments().add(department);
    }
    // metoda dodawanie polki
    public void addShelf(Shelf shelf){
        data.getShelves().add(shelf);
    }
    // metoda dodawanie czytelnika
    public void addReader(Reader reader){
        data.getReaders().add(reader);
    }
    public void deleteBook(Book book){
        if (!isBookAvailable(book)) {
            throw new IllegalStateException("Nie mozna usunac wypozyczonej ksiazki");
        }
        data.getBooks().remove(book);
    }
    public void deleteShelf(Shelf shelf){
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

}
