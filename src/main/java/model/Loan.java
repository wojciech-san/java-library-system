package model;

import java.time.LocalDate;

/**
 * Klasa reprezentuje wypozyczenie ksiazki przez czytelnika
 */
public class Loan {

    // uniklany identyfikator wypozyczenia
    private Long id;

    // wypozyczona ksiazka
    private Book book;

    // osoba wypozyczjaca ksiazke

    private Reader reader;

    // data wypozyczenia
    private LocalDate loanDate;

    // data zwrotu
    private LocalDate dueDate;

    // faktyczna data zwrotu ksiazki (NULL oznacza ze ksiazka nie zostala jeszcze zwrocona)
    private  LocalDate returnDate;

    public Loan(Long id, Book book, Reader reader, LocalDate loanDate, LocalDate dueDate, LocalDate returnDate) {
        this.id = id;
        this.book = book;
        this.reader = reader;
        this.loanDate = loanDate;
        this.dueDate = dueDate;
        this.returnDate = returnDate;
    }

    public Long getId() {
        return id;
    }

    public Book getBook() {
        return book;
    }

    public Reader getReader() {
        return reader;
    }

    public LocalDate getLoanDate() {
        return loanDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void returnBook(LocalDate returnDate){
        this.returnDate=returnDate;
    }
    public boolean isReturned(){
        return returnDate != null;
    }
    public boolean isOverdue(){
        return returnDate == null && dueDate.isBefore(LocalDate.now());
    }
    @Override
    public String toString(){
        return book +"->"+ reader;
    }

}
