package se.kwnna.library.application.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import se.kwnna.library.datastore.BookService;
import se.kwnna.library.datastore.dto.BookDto;
import se.kwnna.library.domain.book.Book;
import se.kwnna.library.domain.book_loan_register.Cake;
import se.kwnna.library.domain.book_loan_register.LoanConstructor;
import se.kwnna.library.domain.book_loan_register.LoanSetter;

@Service
@AllArgsConstructor
public class BookLoanService {
    private final BookService bookService;

    public Optional<Book> loan(Integer id) {
        Book book = bookService.findById(id).get();
        if (book.getQuantity() > 0) {
            Book updatedBook = book.toBuilder().withQuantity(book.getQuantity() - 1).build();
            bookService.save(updatedBook);
            return Optional.of(updatedBook);
        }
        return Optional.empty();
    }

    private void weTestedSomeStuff() {
        LoanConstructor lc = new LoanConstructor(1);

        LoanSetter ls = new LoanSetter();
        ls.setLoanId(1);

        Cake cake = Cake.builder()
                .withSugarInGrams(1)
                .build();
    }
}
