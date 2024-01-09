package se.kwnna.library.domain;

import se.kwnna.library.domain.book.Book;

public class BookTestBuilder {

    public static Book aBook() {
        return aBook("1234567890");
    }

    public static Book aBook(String isbn) {
        return Book.builder()
                .withIsbn(isbn)
                .withTitle("title")
                .withAuthor("author")
                .withGenre("genre")
                .withQuantity(1)
                .build();
    }
}
