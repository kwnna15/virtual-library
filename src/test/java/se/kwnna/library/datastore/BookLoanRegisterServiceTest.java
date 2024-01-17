package se.kwnna.library.datastore;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import se.kwnna.library.datastore.dto.BookLoanRegisterDto;
import se.kwnna.library.domain.book_loan_register.BookLoanRegister;

class BookLoanRegisterServiceTest {

    private BookLoanRegisterRepository repository;
    private BookLoanRegisterService service;

    @BeforeEach
    private void setUp() {
        repository = mock(BookLoanRegisterRepository.class);
        service = new BookLoanRegisterService(repository);
    }

    @Test
    void testDeleteById() {
        service.deleteById(1);

        verify(repository, times(1)).deleteById(1);
    }

   

    @Test
    void findById_idExists() {
        BookLoanRegisterDto loan = new BookLoanRegisterDto();
        // given
        loan.setId(1);
        loan.setBookId(10);
        loan.setUserId(100);
        loan.setStartDate(LocalDate.now().format(DateTimeFormatter.ISO_DATE));
        loan.setEndDate(LocalDate.now().plusMonths(1).format(DateTimeFormatter.ISO_DATE));

        when(repository.findById(1)).thenReturn(Optional.of(loan));

        // when
        Optional<BookLoanRegister> result = service.findById(1);

        // then
        assertEquals(Optional.of(loan.toDomain()), result);
        verify(repository, times(1)).findById(1);
    }

    @Test
    void findById_idDoesNotExist() {
        when(repository.findById(1)).thenReturn(Optional.empty());
        
        Optional<BookLoanRegister> result = service.findById(1);

        assertEquals(Optional.empty(), result);
    }

    @Test
    void testSave() {
        BookLoanRegisterDto loan = new BookLoanRegisterDto();
        loan.setId(1);
        loan.setBookId(10);
        loan.setUserId(100);
        loan.setStartDate(LocalDate.now().format(DateTimeFormatter.ISO_DATE));
        loan.setEndDate(LocalDate.now().plusMonths(1).format(DateTimeFormatter.ISO_DATE));

        when(repository.save(loan)).thenReturn(loan);

        BookLoanRegister result = service.save(loan.toDomain());

        assertEquals(loan.toDomain(), result);

    }
}
