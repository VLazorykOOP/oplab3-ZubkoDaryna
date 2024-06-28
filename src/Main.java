import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Prototype pattern
abstract class Book implements Cloneable {
    private String id;
    protected String title;

    abstract void read();

    public String getTitle() {
        return title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public Object clone() {
        Object clone = null;
        try {
            clone = super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return clone;
    }
}

class FictionBook extends Book {
    public FictionBook() {
        title = "Fiction Book";
    }

    @Override
    void read() {
        System.out.println("Reading a Fiction Book");
    }
}

class ScienceBook extends Book {
    public ScienceBook() {
        title = "Science Book";
    }

    @Override
    void read() {
        System.out.println("Reading a Science Book");
    }
}

class BookCache {
    private static final Map<String, Book> bookMap = new HashMap<>();

    public static Book getBook(String bookId) {
        Book cachedBook = bookMap.get(bookId);
        return (Book) cachedBook.clone();
    }

    public static void loadCache() {
        FictionBook fiction = new FictionBook();
        fiction.setId("1");
        bookMap.put(fiction.getId(), fiction);

        ScienceBook science = new ScienceBook();
        science.setId("2");
        bookMap.put(science.getId(), science);
    }
}

// Adapter pattern
interface BookPrinter {
    void printBook(Book book);
}

class DetailedBookPrinter {
    public void printDetailedBook(Book book) {
        System.out.println("Book ID: " + book.getId());
        System.out.println("Book Title: " + book.getTitle());
    }
}

class PrinterAdapter implements BookPrinter {
    DetailedBookPrinter detailedBookPrinter;

    public PrinterAdapter() {
        detailedBookPrinter = new DetailedBookPrinter();
    }

    @Override
    public void printBook(Book book) {
        detailedBookPrinter.printDetailedBook(book);
    }
}

// Observer pattern
interface Observer {
    void update();
}

class Library {
    private final List<Book> books = new ArrayList<>();
    private final List<Observer> observers = new ArrayList<>();

    public void addBook(Book book) {
        books.add(book);
        notifyAllObservers();
    }

    public void removeBook(Book book) {
        books.remove(book);
        notifyAllObservers();
    }

    public List<Book> getBooks() {
        return books;
    }

    public void attach(Observer observer) {
        observers.add(observer);
    }

    public void notifyAllObservers() {
        for (Observer observer : observers) {
            observer.update();
        }
    }
}

class LibraryObserver implements Observer {
    private final Library library;

    public LibraryObserver(Library library) {
        this.library = library;
        this.library.attach(this);
    }

    @Override
    public void update() {
        System.out.println("Library has been updated. Current books:");
        for (Book book : library.getBooks()) {
            System.out.println(" - " + book.getTitle());
        }
    }
}

// Main class
public class Main {
    public static void main(String[] args) {
        // Prototype pattern
        BookCache.loadCache();

        Book clonedFiction = BookCache.getBook("1");
        System.out.println("Book: " + clonedFiction.getTitle());
        clonedFiction.read();

        Book clonedScience = BookCache.getBook("2");
        System.out.println("Book: " + clonedScience.getTitle());
        clonedScience.read();

        // Observer pattern
        Library library = new Library();
        new LibraryObserver(library);

        library.addBook(clonedFiction);
        library.addBook(clonedScience);

        // Adapter pattern
        PrinterAdapter printerAdapter = new PrinterAdapter();
        System.out.println("Detailed book information:");
        for (Book book : library.getBooks()) {
            printerAdapter.printBook(book);
        }
    }
}
