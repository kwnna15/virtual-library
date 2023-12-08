package se.kwnna.library.datastore.dto;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Getter;
import lombok.Setter;
import se.kwnna.library.domain.book.Book;

@Getter
@Setter
@Entity
public class BookDto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String isbn;
    private String author;
    private String title;
    private String genre;
    private Integer quantity;

    public static BookDto fromDomain(Book book) {
        BookDto bookDto = new BookDto();
        bookDto.setId(book.getId());
        bookDto.setIsbn(book.getIsbn());
        bookDto.setAuthor(book.getAuthor());
        bookDto.setTitle(book.getTitle());
        bookDto.setGenre(book.getGenre());
        bookDto.setQuantity(book.getQuantity());
        return bookDto;
    }

    public Book toDomain() {
        return Book.builder()
                .withId(id)
                .withIsbn(isbn)
                .withAuthor(author)
                .withTitle(title)
                .withGenre(genre)
                .withQuantity(quantity)
                .build();
    }
}
