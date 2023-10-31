package se.kwnna.library.application.finder;

import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import se.kwnna.library.datastore.BookService;
import se.kwnna.library.domain.book.Book;

@Service
@AllArgsConstructor
public class BookFinder {

    private final BookService bookService;

    public List<Book> findByTitle(String title, String sortBy) {
        List<Book> books = bookService.findAllByTitleContaining(title);
        return sort(books, sortBy);
    }

    public List<Book> findByAuthor(String author, String sortBy) {
        List<Book> books = bookService.findAllByAuthorContaining(author);
        return sort(books, sortBy);
    }

    public List<Book> findByIsbn(String isbn, String sortBy) {
        List<Book> books = bookService.findAllByIsbnContaining(isbn);
        return sort(books, sortBy);
    }
    
    public List<Book> findByGenre(String genre, String sortBy) {
        List<Book> books = bookService.findAllByGenreContaining(genre);
        return sort(books, sortBy);
    }

    private List<Book> sort(List<Book> books, String sortBy) {
        if (sortBy.equals("title")) {
            books.sort(Comparator.comparing(Book::getTitle));
        } else if (sortBy.equals("author")) {
            books.sort(Comparator.comparing(Book::getAuthor));
        } else if (sortBy.equals("genre")) {
            books.sort(Comparator.comparing(Book::getGenre));
        }
        return books;
    }
}
