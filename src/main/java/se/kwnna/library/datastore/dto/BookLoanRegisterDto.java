package se.kwnna.library.datastore.dto;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Getter;
import lombok.Setter;
import se.kwnna.library.domain.book_loan_register.BookLoanRegister;

@Getter
@Setter
@Entity
public class BookLoanRegisterDto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer loanId;
    private Integer userId;
    private Integer bookId;
    private String startDate;
    private String endDate;

    public static BookLoanRegisterDto fromDomain(BookLoanRegister bookLoanRegister) {
        BookLoanRegisterDto bookLoanRegisterDto = new BookLoanRegisterDto();
        bookLoanRegisterDto.setLoanId(bookLoanRegister.getLoanId());
        bookLoanRegisterDto.setUserId(bookLoanRegister.getUserId());
        bookLoanRegisterDto.setBookId(bookLoanRegister.getBookId());
        bookLoanRegisterDto.setStartDate(bookLoanRegister.getStartDate());
        bookLoanRegisterDto.setEndDate(bookLoanRegister.getEndDate());
        return bookLoanRegisterDto;
    }

    public BookLoanRegister toDomain() {
        return BookLoanRegister.builder()
                .withLoanId(loanId)
                .withUserId(userId)
                .withBookId(bookId)
                .withStartDate(startDate)
                .withEndDate(endDate)
                .build();
    }
}
