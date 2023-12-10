package se.kwnna.library.datastore;

import java.util.Optional;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import se.kwnna.library.datastore.dto.BookLoanRegisterDto;
import se.kwnna.library.domain.book_loan_register.BookLoanRegister;

@Service
@AllArgsConstructor
public class BookLoanRegisterService {

    private final BookLoanRegisterRepository bookLoanRepository;

    public BookLoanRegister save(BookLoanRegister bookLoanRegister) {
        return bookLoanRepository.save(BookLoanRegisterDto.fromDomain(bookLoanRegister)).toDomain();
    }

    public Optional<BookLoanRegister> findById(Integer id) {
        return bookLoanRepository.findById(id).map(BookLoanRegisterDto::toDomain);
    }

    public void deleteById(Integer id) {
        bookLoanRepository.deleteById(id);
    }
}
