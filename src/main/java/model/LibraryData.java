package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Klasa przechowuje dane aplikacji bibliotecznej
 */
public class LibraryData {
    // lista ksiazek
    private List<Book> books = new ArrayList<>();
    // lista dzialow literackich
    private  List<Department> departments = new ArrayList<>();
    // lista regalow i pulek
    private  List<Shelf> shelves = new ArrayList<>();
    // lista wypozyczjacych
    private  List<Reader>readers = new ArrayList<>();
    // lista wypozyczen
    private List<Loan> loans = new ArrayList<>();

    public List<Book> getBooks() {
        return books;
    }

    public List<Department> getDepartments() {
        return departments;
    }

    public List<Shelf> getShelves() {
        return shelves;
    }

    public List<Reader> getReaders() {
        return readers;
    }

    public List<Loan> getLoans() {
        return loans;
    }
}
