package storage;

import model.Book;
import model.Department;
import model.LibraryData;
import model.Loan;
import model.Reader;
import model.Shelf;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

/**
 * Serwis odpowiedzialny za zapisywanie i odczytywanie danych biblioteki
 * z pliku binarnego.
 */
public class FileStorageService {

    /**
     * Zapisuje dane biblioteki do pliku przy użyciu DataOutputStream.
     *
     * @param data dane biblioteki
     * @param file plik docelowy
     * @throws IOException gdy zapis się nie powiedzie
     */
    public void save(LibraryData data, File file) throws IOException {
        try (DataOutputStream out = new DataOutputStream(new FileOutputStream(file))) {
            saveDepartments(out, data);
            saveShelves(out, data);
            saveReaders(out, data);
            saveBooks(out, data);
            saveLoans(out, data);
        }
    }

    /**
     * Odczytuje dane biblioteki z pliku przy użyciu DataInputStream.
     *
     * @param file plik źródłowy
     * @return wczytane dane biblioteki
     * @throws IOException gdy odczyt się nie powiedzie
     */
    public LibraryData load(File file) throws IOException {
        LibraryData data = new LibraryData();

        Map<Long, Department> departmentsById = new HashMap<>();
        Map<Long, Shelf> shelvesById = new HashMap<>();
        Map<Long, Reader> readersById = new HashMap<>();
        Map<Long, Book> booksById = new HashMap<>();

        try (DataInputStream in = new DataInputStream(new FileInputStream(file))) {
            loadDepartments(in, data, departmentsById);
            loadShelves(in, data, shelvesById);
            loadReaders(in, data, readersById);
            loadBooks(in, data, booksById, shelvesById, departmentsById);
            loadLoans(in, data, booksById, readersById);
        }

        return data;
    }
    /**
     * Zapisuje działy biblioteki.
     */
    private void saveDepartments(DataOutputStream out, LibraryData data) throws IOException {
        out.writeInt(data.getDepartments().size());

        for (Department department : data.getDepartments()) {
            out.writeLong(department.getId());
            out.writeUTF(department.getCode());
            out.writeUTF(department.getName());
            out.writeUTF(department.getDescription());
        }
    }
    /**
     * Wczytuje działy biblioteki.
     */
    private void loadDepartments(
            DataInputStream in,
            LibraryData data,
            Map<Long, Department> departmentsById
    ) throws IOException {
        int count = in.readInt();

        for (int i = 0; i < count; i++) {
            Long id = in.readLong();
            String code = in.readUTF();
            String name = in.readUTF();
            String description = in.readUTF();

            Department department = new Department(id, code, name, description);

            data.getDepartments().add(department);
            departmentsById.put(id, department);
        }
    }
    /**
     * Zapisuje regały.
     */
    private void saveShelves(DataOutputStream out, LibraryData data) throws IOException {
        out.writeInt(data.getShelves().size());

        for (Shelf shelf : data.getShelves()) {
            out.writeLong(shelf.getId());
            out.writeUTF(shelf.getRack());
            out.writeUTF(shelf.getShelf());
        }
    }
    /**
     * Wczytuje regały.
     */
    private void loadShelves(
            DataInputStream in,
            LibraryData data,
            Map<Long, Shelf> shelvesById
    ) throws IOException {
        int count = in.readInt();

        for (int i = 0; i < count; i++) {
            Long id = in.readLong();
            String rack = in.readUTF();
            String shelfName = in.readUTF();

            Shelf shelf = new Shelf(id, rack, shelfName);

            data.getShelves().add(shelf);
            shelvesById.put(id, shelf);
        }
    }
    /**
     * Zapisuje czytelników.
     */
    private void saveReaders(DataOutputStream out, LibraryData data) throws IOException {
        out.writeInt(data.getReaders().size());

        for (Reader reader : data.getReaders()) {
            out.writeLong(reader.getId());
            out.writeUTF(reader.getFirstName());
            out.writeUTF(reader.getLastName());
            out.writeUTF(reader.getAddress());
            out.writeUTF(reader.getCardNumber());
        }
    }
    /**
     * Wczytuje czytelników.
     */
    private void loadReaders(
            DataInputStream in,
            LibraryData data,
            Map<Long, Reader> readersById
    ) throws IOException {
        int count = in.readInt();

        for (int i = 0; i < count; i++) {
            Long id = in.readLong();
            String firstName = in.readUTF();
            String lastName = in.readUTF();
            String address = in.readUTF();
            String cardNumber = in.readUTF();

            Reader reader = new Reader(id, firstName, lastName, address, cardNumber);

            data.getReaders().add(reader);
            readersById.put(id, reader);
        }
    }
    /**
     * Zapisuje książki.
     */
    private void saveBooks(DataOutputStream out, LibraryData data) throws IOException {
        out.writeInt(data.getBooks().size());

        for (Book book : data.getBooks()) {
            out.writeLong(book.getId());
            out.writeUTF(book.getTitle());
            out.writeUTF(book.getAuthors());
            out.writeUTF(book.getPublisher());
            out.writeInt(book.getYear());
            out.writeUTF(book.getIsbn());

            out.writeLong(book.getShelf().getId());
            out.writeLong(book.getDepartment().getId());
        }
    }
    /**
     * Wczytuje książki.
     */
    private void loadBooks(
            DataInputStream in,
            LibraryData data,
            Map<Long, Book> booksById,
            Map<Long, Shelf> shelvesById,
            Map<Long, Department> departmentsById
    ) throws IOException {
        int count = in.readInt();

        for (int i = 0; i < count; i++) {
            Long id = in.readLong();
            String title = in.readUTF();
            String authors = in.readUTF();
            String publisher = in.readUTF();
            int year = in.readInt();
            String isbn = in.readUTF();

            Long shelfId = in.readLong();
            Long departmentId = in.readLong();

            Shelf shelf = shelvesById.get(shelfId);
            Department department = departmentsById.get(departmentId);

            Book book = new Book(
                    id,
                    title,
                    authors,
                    publisher,
                    year,
                    isbn,
                    shelf,
                    department
            );

            data.getBooks().add(book);
            booksById.put(id, book);
        }
    }
    /**
     * Zapisuje wypożyczenia.
     */
    private void saveLoans(DataOutputStream out, LibraryData data) throws IOException {
        out.writeInt(data.getLoans().size());

        for (Loan loan : data.getLoans()) {
            out.writeLong(loan.getId());

            out.writeLong(loan.getBook().getId());
            out.writeLong(loan.getReader().getId());

            writeLocalDate(out, loan.getLoanDate());
            writeLocalDate(out, loan.getDueDate());
            writeLocalDate(out, loan.getReturnDate());
        }
    }
    /**
     * Wczytuje wypożyczenia.
     */
    private void loadLoans(
            DataInputStream in,
            LibraryData data,
            Map<Long, Book> booksById,
            Map<Long, Reader> readersById
    ) throws IOException {
        int count = in.readInt();

        for (int i = 0; i < count; i++) {
            Long id = in.readLong();

            Long bookId = in.readLong();
            Long readerId = in.readLong();

            LocalDate loanDate = readLocalDate(in);
            LocalDate dueDate = readLocalDate(in);
            LocalDate returnDate = readLocalDate(in);

            Book book = booksById.get(bookId);
            Reader reader = readersById.get(readerId);

            Loan loan = new Loan(id, book, reader, loanDate, dueDate,null);

            if (returnDate != null) {
                loan.returnBook(returnDate);
            }

            data.getLoans().add(loan);
        }
    }
    /**
     * Zapisuje datę typu LocalDate do strumienia.
     */
    private void writeLocalDate(DataOutputStream out, LocalDate date) throws IOException {
        if (date == null) {
            out.writeBoolean(false);
        } else {
            out.writeBoolean(true);
            out.writeUTF(date.toString());
        }
    }
    /**
     * Odczytuje datę typu LocalDate ze strumienia.
     *
     * @return odczytana data lub null, jeśli data nie istnieje
     */
    private LocalDate readLocalDate(DataInputStream in) throws IOException {
        boolean exists = in.readBoolean();

        if (!exists) {
            return null;
        }

        return LocalDate.parse(in.readUTF());
    }
}