package se.kwnna.library.application.validation;

import org.springframework.stereotype.Service;

import se.kwnna.library.domain.book.Book;
import se.kwnna.library.domain.exception.InvalidIsbnException;

@Service
public class BookValidator {

    public void validate(Book book) {
        validateIsbn(book.getIsbn());
    }

    public void validateIsbn(String isbn) {
        if (!isIsbnValid(isbn)) {
            throw new InvalidIsbnException("Invalid ISBN format");
        }
    }

    private boolean isIsbnValid(String isbn) {
        return isbn != null && isbn.matches("^[0-9]+") && isbnHasCorrectLength(isbn);
    }

    private boolean isbnHasCorrectLength(String isbn) {
        return isbn.length() == 10 || isbn.length() == 13;
    }
}
