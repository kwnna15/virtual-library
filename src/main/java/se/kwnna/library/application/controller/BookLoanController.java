package se.kwnna.library.application.controller;

import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import se.kwnna.library.application.service.BookLoanService;
import se.kwnna.library.domain.book.Book;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping(path = "/api/loan")
public class BookLoanController {
    private final BookLoanService bookLoanService;

    @GetMapping("/{id}")
    public ResponseEntity<Book> loan(@PathVariable Integer id) {
        log.info("Received a Loan request of id={}", id);
        Optional<Book> book = bookLoanService.loan(id);
        if (book.isPresent()) {
            // 200
            return ResponseEntity.ok(book.get());
        } else {
            // 214
            return ResponseEntity.noContent().build();
        }
    }

}
