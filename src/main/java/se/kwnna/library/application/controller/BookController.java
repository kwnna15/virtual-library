package se.kwnna.library.application.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import se.kwnna.library.application.service.BookFinderService;
import se.kwnna.library.application.validation.BookValidator;
import se.kwnna.library.domain.book.Book;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping(path = "/api/book")
public class BookController {

    private final BookFinderService bookFinder;
    private final BookValidator bookValidator;

    @GetMapping("/find/title/{title}")
    public ResponseEntity<List<Book>> findByTitle(
            @PathVariable String title,
            @RequestParam(required = false, defaultValue = "title") String sortBy) {
        log.info("Received a findByTitle request of title={} author={} sortBy={}", title, sortBy);
        return ResponseEntity.ok(bookFinder.findByTitle(title, sortBy));
    }

    @GetMapping("/find/author/{author}")
    public ResponseEntity<List<Book>> findByAuthor(
            @PathVariable String author,
            @RequestParam(required = false, defaultValue = "author") String sortBy) {
        log.info("Received a findByAuthor request of title={} author={} sortBy={}", author, sortBy);
        return ResponseEntity.ok(bookFinder.findByAuthor(author, sortBy));
    }

    @GetMapping("/find/isbn/{isbn}")
    public ResponseEntity<List<Book>> findByIsbn(
            @PathVariable String isbn,
            @RequestParam(required = false, defaultValue = "title") String sortBy) {
        log.info("Received a findByIsbn request of title={} isbn={} sortBy={}", isbn, sortBy);
        bookValidator.validateIsbn(isbn);
        return ResponseEntity.ok(bookFinder.findByIsbn(isbn, sortBy));
    }

    @GetMapping("/find/genre/{genre}")
    public ResponseEntity<List<Book>> findByGenre(
            @PathVariable String genre,
            @RequestParam(required = false, defaultValue = "genre") String sortBy) {
        log.info("Received a findByGenre request of title={} genre={} sortBy={}", genre, sortBy);
        return ResponseEntity.ok(bookFinder.findByGenre(genre, sortBy));
    }
}
