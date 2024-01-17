package se.kwnna.library.application.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import se.kwnna.library.datastore.BookLoanRegisterService;
import se.kwnna.library.datastore.BookService;
import se.kwnna.library.domain.book.Book;
import se.kwnna.library.domain.book_loan_register.BookLoanRegister;
import se.kwnna.library.domain.exception.UnknownBookException;

@Service
@AllArgsConstructor
public class BookLoanService {
    private final BookService bookService;
    private final BookLoanRegisterService bookLoanRegisterService;

    public Optional<BookLoanRegister> loan(Integer bookId, Integer userId) {
        Optional<Book> maybeBook = bookService.findById(bookId);
        if (maybeBook.isPresent()) {
            Book book = maybeBook.get();
            if (book.getQuantity() > 0) {
                Book updatedBook = book.toBuilder().withQuantity(book.getQuantity() - 1).build();
                bookService.save(updatedBook);
                BookLoanRegister bookLoanRegister = BookLoanRegister.builder()
                        .withBookId(bookId)
                        .withUserId(userId)
                        .withStartDate(LocalDate.now().format(DateTimeFormatter.ISO_DATE))
                        .withEndDate(LocalDate.now().plusMonths(1).format(DateTimeFormatter.ISO_DATE))
                        .build();
                return Optional.of(bookLoanRegisterService.save(bookLoanRegister));
            }
        }
        return Optional.empty();
    }

    public Optional<BookLoanRegister> findLoan(Integer loanId) {
        return bookLoanRegisterService.findById(loanId);
    }

    public Optional<BookLoanRegister> returnLoan(Integer loanId) {
        Optional<BookLoanRegister> bookLoanRegister = findLoan(loanId);
        if (bookLoanRegister.isPresent()) {
            int bookId = bookLoanRegister.get().getBookId();
            Optional<Book> book = bookService.findById(bookId);
            if (book.isPresent()) {
                Book updatedBook = book.get().toBuilder()
                        .withQuantity(book.get().getQuantity() + 1)
                        .build();
                bookService.save(updatedBook);
            } else {
                throw new UnknownBookException("Could not find book with id " + bookId);
            }
            bookLoanRegisterService.deleteById(loanId);
            return bookLoanRegister;
        } else {
            return Optional.empty();
        }
    }
}
