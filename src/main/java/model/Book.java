package model;

/**
 * Klasa reprezentuje ksiazke przechowywana w bibliotece
 */
public class Book {
    // unikalny identyfikator ksiazki
    private Long id;
    // tytul ksiazki
    private  String title;
    //autor lub autorzy ksiazki
    private String authors;
    //wydawnictwp
    private String publisher;
    // rok wydania ksiazki
    private int year;
    // numer IBSN ksiazki
    private String isbn;
    // miejsce przechowywania ksiazki
    private Shelf shelf;
    // dzial literatury do ktorego przypisana jest ksiazka
    private Department department;

    public Book(Long id, String title, String authors, String publisher, int year,
                String isbn, Shelf shelf, Department department){
        this.id = id;
        this.title = title;
        this.authors = authors;
        this.publisher = publisher;
        this.year = year;
        this.isbn = isbn;
        this.shelf = shelf;
        this.department = department;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthors() {
        return authors;
    }

    public void setAuthors(String authors) {
        this.authors = authors;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getIsnn() {
        return isbn;
    }

    public void setIbsn(String isbn) {
        this.isbn = isbn;
    }

    public Shelf getShelf() {
        return shelf;
    }

    public void setShelf(Shelf shelf) {
        this.shelf = shelf;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }
    @Override
    public String toString(){
        return title +" - "+authors;
    }
}
