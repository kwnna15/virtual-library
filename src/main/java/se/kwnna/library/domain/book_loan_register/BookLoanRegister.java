package se.kwnna.library.domain.book_loan_register;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Builder(setterPrefix = "with", toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@EqualsAndHashCode
@ToString
public class BookLoanRegister {
    private final Integer loanId;
    private final Integer userId;
    private final Integer bookId;
    private final String startDate;
    private final String endDate;
}
