package se.kwnna.library.application.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import se.kwnna.library.application.validation.BookValidator;
import se.kwnna.library.datastore.BookService;
import se.kwnna.library.domain.book.Book;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping(path = "/admin/book")
public class AdminBookController {

    private final BookService bookService;
    private final BookValidator bookValidator;

    @PostMapping("/save")
    public ResponseEntity<Book> saveBook(@RequestBody Book book) {
        log.info("Received a saveBook request of book={}", book);
        bookValidator.validate(book);
        return ResponseEntity.ok(bookService.save(book));
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Book> getBook(@PathVariable Integer id) {
        log.info("Received a getBook request of id={}", id);
        return bookService.findById(id)
                .map(book -> ResponseEntity.ok(book))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteBookById(@PathVariable Integer id) {
        log.info("Received a deleteBookById request of id={}", id);
        return bookService.findById(id)
                .map(book -> {
                    bookService.deleteById(id);
                    return ResponseEntity.noContent().build();
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}