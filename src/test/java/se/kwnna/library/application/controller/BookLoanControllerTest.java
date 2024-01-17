package se.kwnna.library.application.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;

import se.kwnna.library.application.LibraryApplication;
import se.kwnna.library.datastore.BookLoanRegisterRepository;
import se.kwnna.library.datastore.BookLoanRegisterService;
import se.kwnna.library.datastore.BookService;
import se.kwnna.library.domain.book.Book;
import se.kwnna.library.domain.book_loan_register.BookLoanRegister;

@SpringBootTest(classes = { LibraryApplication.class })
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = BookLoanRegisterMvcTestCreator.class)
@AutoConfigureMockMvc
public class BookLoanControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private BookLoanRegisterService bookLoanService;
    @Autowired
    private BookService bookService;
    @Autowired
    private BookLoanRegisterRepository reporsitory;
    @Autowired
    private BookLoanRegisterMvcTestCreator bookLoanRegisterMvcTestCreator;

    @BeforeEach
    private void setUp() {
        // clean up
        reporsitory.deleteAll();
    }

    @Nested
    class FindLoan {

        @Test
        @WithMockUser
        void loanDoesNotExist() throws Exception {
            mockMvc.perform(get("/api/loan/info/1"))
                    .andDo(print())
                    .andExpect(status().isNoContent())
                    .andReturn();
        }

        @Test
        @WithMockUser
        void loanExists() throws Exception {
            BookLoanRegister loan = BookLoanRegister.builder()
                    .withBookId(1)
                    .withUserId(1)
                    .withStartDate(LocalDate.now().format(DateTimeFormatter.ISO_DATE))
                    .withEndDate(LocalDate.now().plusMonths(1).format(DateTimeFormatter.ISO_DATE))
                    .build();

            BookLoanRegister savedLoan = bookLoanService.save(loan);

            MvcResult result = mockMvc.perform(get("/api/loan/info/" + savedLoan.getId()))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andReturn();

            assertEquals(savedLoan, bookLoanRegisterMvcTestCreator.fromMvcToDomain(result));
        }

        @Test
        @WithMockUser
        void loanTest() throws Exception {
            BookLoanRegister loan = BookLoanRegister.builder()
                    .withBookId(1)
                    .withUserId(1)
                    .withStartDate(LocalDate.now().format(DateTimeFormatter.ISO_DATE))
                    .withEndDate(LocalDate.now().plusMonths(1).format(DateTimeFormatter.ISO_DATE))
                    .build();

            BookLoanRegister savedLoan = bookLoanService.save(loan);

            MvcResult result = mockMvc.perform(get("/api/loan/info/" + savedLoan.getId()))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andReturn();

            assertEquals(savedLoan, bookLoanRegisterMvcTestCreator.fromMvcToDomain(result));
        }
    }

    @Nested
    class Loan {

        @Test
        @WithMockUser
        void success() throws Exception {
            Integer userId = 1;
            Integer bookdId = 2;
            Book book = Book.builder()
                    .withAuthor("Mary Test")
                    .withGenre("Genre 1")
                    .withId(bookdId)
                    .withIsbn("1234567890")
                    .withQuantity(3)
                    .withTitle("New book")
                    .build();

            bookService.save(book);

            MvcResult result = mockMvc.perform(post("/api/loan/" + bookdId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(new ObjectMapper().writeValueAsString(userId)))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andReturn();

            BookLoanRegister actualLoan = bookLoanRegisterMvcTestCreator.fromMvcToDomain(result);
            BookLoanRegister expectedLoan = BookLoanRegister.builder()
                    .withId(actualLoan.getId())
                    .withBookId(bookdId)
                    .withUserId(userId)
                    .withStartDate(actualLoan.getStartDate())
                    .withEndDate(actualLoan.getEndDate())
                    .build();

            assertEquals(expectedLoan, actualLoan);
        }

        @Test
        @WithMockUser
        void noQuantity() throws Exception {
            Integer userId = 1;
            Integer bookdId = 2;
            Book book = Book.builder()
                    .withAuthor("Mary Test")
                    .withGenre("Genre 1")
                    .withId(bookdId)
                    .withIsbn("1234567890")
                    .withQuantity(0)
                    .withTitle("New book")
                    .build();

            bookService.save(book);

            mockMvc.perform(post("/api/loan/" + bookdId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(new ObjectMapper().writeValueAsString(userId)))
                    .andDo(print())
                    .andExpect(status().isNoContent());
        }

        @Test
        @WithMockUser
        void bookDoesNotExist() throws Exception {
            Integer userId = 1;
            mockMvc.perform(post("/api/loan/1334")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(new ObjectMapper().writeValueAsString(userId)))
                    .andDo(print())
                    .andExpect(status().isNoContent());
        }
    }

    @Nested
    class Return {

        @Test
        @WithMockUser
        void success() throws Exception {
            Integer userId = 1;
            Integer bookdId = 2;
            Book book = Book.builder()
                    .withAuthor("Mary Test")
                    .withGenre("Genre 1")
                    .withId(bookdId)
                    .withIsbn("1234567890")
                    .withQuantity(3)
                    .withTitle("New book")
                    .build();

            bookService.save(book);

            MvcResult loanResult = mockMvc.perform(post("/api/loan/" + bookdId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(new ObjectMapper().writeValueAsString(userId)))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andReturn();

            BookLoanRegister actualLoan = bookLoanRegisterMvcTestCreator.fromMvcToDomain(loanResult);

            MvcResult returnResult = mockMvc.perform(post("/api/loan/return/" + actualLoan.getId()))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andReturn();

            BookLoanRegister expectedLoan = BookLoanRegister.builder()
                    .withId(actualLoan.getId())
                    .withBookId(bookdId)
                    .withUserId(userId)
                    .withStartDate(actualLoan.getStartDate())
                    .withEndDate(actualLoan.getEndDate())
                    .build();

            assertEquals(expectedLoan, bookLoanRegisterMvcTestCreator.fromMvcToDomain(returnResult));
        }
    }
}
