package se.kwnna.library.application.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import se.kwnna.library.application.LibraryApplication;
import se.kwnna.library.application.service.BookLoanService;
import se.kwnna.library.datastore.BookLoanRegisterRepository;
import se.kwnna.library.datastore.BookLoanRegisterService;
import se.kwnna.library.datastore.dto.BookLoanRegisterDto;
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
    private BookLoanRegisterService service;
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
        void singleResult() throws Exception {
            BookLoanRegister loan = BookLoanRegister.builder()
                    .withBookId(1)
                    .withUserId(1)
                    .withStartDate(LocalDate.now().format(DateTimeFormatter.ISO_DATE))
                    .withEndDate(LocalDate.now().plusMonths(1).format(DateTimeFormatter.ISO_DATE))
                    .build();

            BookLoanRegister savedLoan = service.save(loan);

            // BookLoanRegisterDto dto = new BookLoanRegisterDto();
            // dto.setId(1);
            // dto.setBookId(10);
            // dto.setUserId(100);
            // dto.setStartDate(LocalDate.now().format(DateTimeFormatter.ISO_DATE));
            // dto.setEndDate(LocalDate.now().plusMonths(1).format(DateTimeFormatter.ISO_DATE));

            // BookLoanRegister savedLoan = reporsitory.save(dto);


            // {
            //   id: "1",
            //   userId: "1"
            // }

            MvcResult result = mockMvc.perform(get("/api/loan/info/" + savedLoan.getId()))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andReturn();

            assertEquals(savedLoan, bookLoanRegisterMvcTestCreator.fromMvcToDomain(result));
        }

        ////////
        @Test
        @WithMockUser
        void multipleResults() throws Exception {
            Book book1 = bookMvcTestCreator.create("title1", "author1");
            Book book2 = bookMvcTestCreator.create("title2", "author2");
            Book book3 = bookMvcTestCreator.create("title3", "author3");

            MvcResult result = mockMvc.perform(get("/api/book/find/title/title"))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andReturn();

            List<Book> actual = bookMvcTestCreator.fromMvcToDomain(result);

            assertEquals(List.of(book1, book2, book3), actual);
        }

        @Test
        @WithMockUser
        void filter() throws Exception {
            bookMvcTestCreator.create("title1", "author1");
            bookMvcTestCreator.create("title2", "author2");
            Book book = bookMvcTestCreator.create("findme", "author3");

            MvcResult result = mockMvc.perform(get("/api/book/find/title/findme"))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andReturn();

            List<Book> actual = bookMvcTestCreator.fromMvcToDomain(result);

            assertEquals(List.of(book), actual);
        }

        @Test
        @WithMockUser
        void sortByTitle() throws Exception {
            Book book1 = bookMvcTestCreator.create("title3", "author1");
            Book book2 = bookMvcTestCreator.create("title1", "author2");
            Book book3 = bookMvcTestCreator.create("title2", "author3");

            MvcResult result = mockMvc.perform(get("/api/book/find/title/title")
                    .param("sortBy", "title"))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andReturn();

            List<Book> actual = bookMvcTestCreator.fromMvcToDomain(result);

            assertEquals(List.of(book2, book3, book1), actual);
        }

        @Test
        @WithMockUser
        void sortByAuthor() throws Exception {
            Book book1 = bookMvcTestCreator.create("title1", "author3");
            Book book2 = bookMvcTestCreator.create("title2", "author1");
            Book book3 = bookMvcTestCreator.create("title3", "author2");

            MvcResult result = mockMvc.perform(get("/api/book/find/title/title")
                    .param("sortBy", "author"))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andReturn();

            List<Book> actual = bookMvcTestCreator.fromMvcToDomain(result);

            assertEquals(List.of(book2, book3, book1), actual);
        }

        @Test
        @WithMockUser
        void sortByGenre() throws Exception {
            Book book1 = bookMvcTestCreator.create("title1", "author1", "genre3");
            Book book2 = bookMvcTestCreator.create("title2", "author2", "genre1");
            Book book3 = bookMvcTestCreator.create("title3", "author3", "genre2");

            MvcResult result = mockMvc.perform(get("/api/book/find/title/title")
                    .param("sortBy", "genre"))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andReturn();

            List<Book> actual = bookMvcTestCreator.fromMvcToDomain(result);

            assertEquals(List.of(book2, book3, book1), actual);
        }

        @Test
        @WithAnonymousUser
        void unauthenticatedUser() throws Exception {
            mockMvc.perform(get("/api/loan/info/loanID"))
                    .andDo(print())
                    .andExpect(status().isUnauthorized());
        }
    }

    /*
     * @Test
     * void testFindLoan() {
     * BookLoanRegisterDto loan = new BookLoanRegisterDto();
     * // given
     * loan.setId(1);
     * loan.setBookId(10);
     * loan.setUserId(100);
     * loan.setStartDate(LocalDate.now().format(DateTimeFormatter.ISO_DATE));
     * loan.setEndDate(LocalDate.now().plusMonths(1).format(DateTimeFormatter.
     * ISO_DATE));
     * 
     * when(controller.findLoan(1)).thenReturn(ResponseEntity.ok(loan.toDomain()));
     * 
     * // when
     * ResponseEntity<BookLoanRegister> result = controller.findLoan(1);
     * 
     * // then
     * assertEquals(ResponseEntity.ok(loan.toDomain()), result);
     * 
     * }
     */

    @Test
    void testLoan() {

    }

    @Test
    void testReturnBook() {

    }
}
