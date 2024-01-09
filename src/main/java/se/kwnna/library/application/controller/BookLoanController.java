package se.kwnna.library.application.controller;

import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import se.kwnna.library.application.service.BookLoanService;
import se.kwnna.library.domain.book_loan_register.BookLoanRegister;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping(path = "/api/loan")
public class BookLoanController {
    private final BookLoanService bookLoanService;

    @PostMapping("/{bookId}")
    public ResponseEntity<BookLoanRegister> loan(@PathVariable Integer bookId, @RequestBody Integer userId) {
        log.info("Received a Loan request of id={}, user id={}", bookId, userId);
        Optional<BookLoanRegister> bookLoanRegister = bookLoanService.loan(bookId, userId);
        if (bookLoanRegister.isPresent()) {
            // 200
            return ResponseEntity.ok(bookLoanRegister.get());
        } else {
            // 214
            return ResponseEntity.noContent().build();
        }
    }

    @GetMapping("/info/{loanId}")
    public ResponseEntity<BookLoanRegister> findLoan(@PathVariable Integer loanId) {
        log.info("Found the Loan request of id={}", loanId);
        Optional<BookLoanRegister> bookLoanRegister = bookLoanService.findLoan(loanId);
        if (bookLoanRegister.isPresent()) {
            // 200
            return ResponseEntity.ok(bookLoanRegister.get());
        } else {
            // 214
            return ResponseEntity.noContent().build();
        }
    }

    @PostMapping("/return/{loanId}")
    public ResponseEntity<BookLoanRegister> returnBook(@PathVariable Integer loanId) {
        log.info("Received a Return Book request for loan id={}", loanId);
        Optional<BookLoanRegister> bookLoanRegister = bookLoanService.returnLoan(loanId);
        if (bookLoanRegister.isPresent()) {
            // 200
            return ResponseEntity.ok(bookLoanRegister.get());
        } else {
            // 400
            return ResponseEntity.badRequest().build();
        }
    }

}
