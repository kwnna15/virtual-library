package se.kwnna.library.datastore;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import se.kwnna.library.datastore.dto.BookDto;
import se.kwnna.library.domain.book.Book;

@Service
@AllArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

    public Book save(Book book) {
        return bookRepository.save(BookDto.fromDomain(book)).toDomain();
    }

    public Optional<Book> findById(Integer id) {
        return bookRepository.findById(id).map(BookDto::toDomain);
    }

    public void deleteById(Integer id) {
        bookRepository.deleteById(id);
    }

    public List<Book> findAllByTitleContaining(String title) {
        return toDomain(bookRepository.findAllByTitleContaining(title));
    }

    public List<Book> findAllByAuthorContaining(String author) {
        return toDomain(bookRepository.findAllByAuthorContaining(author));
    }

    public List<Book> findAllByIsbnContaining(String isbn) {
        return toDomain(bookRepository.findAllByIsbnContaining(isbn));
    }

    public List<Book> findAllByGenreContaining(String genre) {
        return toDomain(bookRepository.findAllByGenreContaining(genre));
    }

    private List<Book> toDomain(List<BookDto> books) {
        return books.stream()
                .map(BookDto::toDomain)
                .collect(Collectors.toList());
    }
}
