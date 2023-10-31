package se.kwnna.library.application.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import se.kwnna.library.application.LibraryApplication;
import se.kwnna.library.datastore.BookRepository;
import se.kwnna.library.domain.book.Book;

@SpringBootTest(classes = { LibraryApplication.class })
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = BookMvcTestCreator.class)
@AutoConfigureMockMvc
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private BookMvcTestCreator bookMvcTestCreator;
    @Autowired
    private BookRepository bookRepository;

    @BeforeEach
    void setUp() {
        // clean up
        bookRepository.deleteAll();
    }

    @Nested
    class FindByTitle {

        @Test
        @WithMockUser
        void none() throws Exception {
            MvcResult result = mockMvc.perform(get("/api/book/find/title/title"))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andReturn();

            List<Book> actual = bookMvcTestCreator.fromMvcToDomain(result);

            assertEquals(0, actual.size());
        }

        @Test
        @WithMockUser
        void singleResult() throws Exception {
            Book book = bookMvcTestCreator.create("title", "author");

            MvcResult result = mockMvc.perform(get("/api/book/find/title/title"))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andReturn();

            List<Book> actual = bookMvcTestCreator.fromMvcToDomain(result);

            assertEquals(List.of(book), actual);
        }

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
            mockMvc.perform(get("/api/book/find/title/title"))
                    .andDo(print())
                    .andExpect(status().isUnauthorized());
        }
    }

    @Nested
    class FindByAuthor {

        @Test
        @WithMockUser
        void none() throws Exception {
            MvcResult result = mockMvc.perform(get("/api/book/find/author/title"))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andReturn();

            List<Book> actual = bookMvcTestCreator.fromMvcToDomain(result);

            assertEquals(0, actual.size());
        }

        @Test
        @WithMockUser
        void singleResult() throws Exception {
            Book book = bookMvcTestCreator.create("title", "author");

            MvcResult result = mockMvc.perform(get("/api/book/find/author/author"))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andReturn();

            List<Book> actual = bookMvcTestCreator.fromMvcToDomain(result);

            assertEquals(List.of(book), actual);
        }

        @Test
        @WithMockUser
        void multipleResults() throws Exception {
            Book book1 = bookMvcTestCreator.create("title1", "author1");
            Book book2 = bookMvcTestCreator.create("title2", "author2");
            Book book3 = bookMvcTestCreator.create("title3", "author3");

            MvcResult result = mockMvc.perform(get("/api/book/find/author/author"))
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
            Book book = bookMvcTestCreator.create("title3", "findme");

            MvcResult result = mockMvc.perform(get("/api/book/find/author/findme"))
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

            MvcResult result = mockMvc.perform(get("/api/book/find/author/author")
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

            MvcResult result = mockMvc.perform(get("/api/book/find/author/author")
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
            mockMvc.perform(get("/api/book/find/author/author"))
                    .andDo(print())
                    .andExpect(status().isUnauthorized());
        }
    }

    @Nested
    class FindByisbn {

        @ParameterizedTest
        @ValueSource(strings = { "1234567890", "1234567890123" })
        @WithMockUser
        void validFormat(String isbn) throws Exception {
            MvcResult result = mockMvc.perform(get("/api/book/find/isbn/" + isbn))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andReturn();

            List<Book> actual = bookMvcTestCreator.fromMvcToDomain(result);

            assertEquals(0, actual.size());
        }

        @ParameterizedTest
        @ValueSource(strings = { "ABCDEFGHI", "ABCDEFGHIJKLM", "123456789A", "a087652347" })
        @WithMockUser
        void invalidFormat(String isbn) throws Exception {
            mockMvc.perform(get("/api/book/find/isbn/" + isbn))
                    .andDo(print())
                    .andExpect(status().isBadRequest());
        }

        @Test
        @WithMockUser
        void none() throws Exception {
            MvcResult result = mockMvc.perform(get("/api/book/find/isbn/1234567890"))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andReturn();

            List<Book> actual = bookMvcTestCreator.fromMvcToDomain(result);

            assertEquals(0, actual.size());
        }

        @Test
        @WithMockUser
        void singleResult() throws Exception {
            Book book = bookMvcTestCreator.create("title", "author", "genre", "1234567890");

            MvcResult result = mockMvc.perform(get("/api/book/find/isbn/1234567890"))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andReturn();

            List<Book> actual = bookMvcTestCreator.fromMvcToDomain(result);

            assertEquals(List.of(book), actual);
        }

        @Test
        @WithMockUser
        void multipleResults() throws Exception {
            Book book1 = bookMvcTestCreator.create("title1", "author1", "genre1", "1234567890");
            Book book2 = bookMvcTestCreator.create("title2", "author2", "genre2", "1234567890");
            Book book3 = bookMvcTestCreator.create("title3", "author3", "genre3", "1234567890");

            MvcResult result = mockMvc.perform(get("/api/book/find/isbn/1234567890"))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andReturn();

            List<Book> actual = bookMvcTestCreator.fromMvcToDomain(result);

            assertEquals(List.of(book1, book2, book3), actual);
        }

        @Test
        @WithMockUser
        void filter() throws Exception {
            bookMvcTestCreator.create("title1", "author1", "genre1", "0987654321");
            bookMvcTestCreator.create("title2", "author2", "genre2", "0987654321");
            Book book = bookMvcTestCreator.create("title3", "author3", "genre3", "1234567890");

            MvcResult result = mockMvc.perform(get("/api/book/find/isbn/1234567890"))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andReturn();

            List<Book> actual = bookMvcTestCreator.fromMvcToDomain(result);

            assertEquals(List.of(book), actual);
        }

        @Test
        @WithMockUser
        void sortByTitle() throws Exception {
            Book book1 = bookMvcTestCreator.create("title3", "author1", "genre1", "1234567890");
            Book book2 = bookMvcTestCreator.create("title1", "author2", "genre2", "1234567890");
            Book book3 = bookMvcTestCreator.create("title2", "author3", "genre3", "1234567890");

            MvcResult result = mockMvc.perform(get("/api/book/find/isbn/1234567890")
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
            Book book1 = bookMvcTestCreator.create("title1", "author3", "genre1", "1234567890");
            Book book2 = bookMvcTestCreator.create("title2", "author1", "genre2", "1234567890");
            Book book3 = bookMvcTestCreator.create("title3", "author2", "genre3", "1234567890");

            MvcResult result = mockMvc.perform(get("/api/book/find/isbn/1234567890")
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
            Book book1 = bookMvcTestCreator.create("title1", "author1", "genre3", "1234567890");
            Book book2 = bookMvcTestCreator.create("title2", "author2", "genre1", "1234567890");
            Book book3 = bookMvcTestCreator.create("title3", "author3", "genre2", "1234567890");

            MvcResult result = mockMvc.perform(get("/api/book/find/isbn/1234567890")
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
            mockMvc.perform(get("/api/book/find/isbn/1234567890"))
                    .andDo(print())
                    .andExpect(status().isUnauthorized());
        }
    }

    @Nested
    class FindByGenre {

        @Test
        @WithMockUser
        void none() throws Exception {
            MvcResult result = mockMvc.perform(get("/api/book/find/genre/genre"))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andReturn();

            List<Book> actual = bookMvcTestCreator.fromMvcToDomain(result);

            assertEquals(0, actual.size());
        }

        @Test
        @WithMockUser
        void singleResult() throws Exception {
            Book book = bookMvcTestCreator.create("title", "author", "genre");

            MvcResult result = mockMvc.perform(get("/api/book/find/genre/genre"))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andReturn();

            List<Book> actual = bookMvcTestCreator.fromMvcToDomain(result);

            assertEquals(List.of(book), actual);
        }

        @Test
        @WithMockUser
        void multipleResults() throws Exception {
            Book book1 = bookMvcTestCreator.create("title1", "author1", "genre1");
            Book book2 = bookMvcTestCreator.create("title2", "author2", "genre2");
            Book book3 = bookMvcTestCreator.create("title3", "author3", "genre3");

            MvcResult result = mockMvc.perform(get("/api/book/find/genre/genre"))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andReturn();

            List<Book> actual = bookMvcTestCreator.fromMvcToDomain(result);

            assertEquals(List.of(book1, book2, book3), actual);
        }

        @Test
        @WithMockUser
        void filter() throws Exception {
            bookMvcTestCreator.create("title1", "author1", "genre1");
            bookMvcTestCreator.create("title2", "author2", "genre2");
            Book book = bookMvcTestCreator.create("title3", "author3", "findme");

            MvcResult result = mockMvc.perform(get("/api/book/find/genre/findme"))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andReturn();

            List<Book> actual = bookMvcTestCreator.fromMvcToDomain(result);

            assertEquals(List.of(book), actual);
        }

        @Test
        @WithMockUser
        void sortByTitle() throws Exception {
            Book book1 = bookMvcTestCreator.create("title3", "author1", "genre1");
            Book book2 = bookMvcTestCreator.create("title1", "author2", "genre2");
            Book book3 = bookMvcTestCreator.create("title2", "author3", "genre3");

            MvcResult result = mockMvc.perform(get("/api/book/find/genre/genre")
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
            Book book1 = bookMvcTestCreator.create("title1", "author3", "genre1");
            Book book2 = bookMvcTestCreator.create("title2", "author1", "genre2");
            Book book3 = bookMvcTestCreator.create("title3", "author2", "genre3");

            MvcResult result = mockMvc.perform(get("/api/book/find/genre/genre")
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

            MvcResult result = mockMvc.perform(get("/api/book/find/genre/genre")
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
            mockMvc.perform(get("/api/book/find/genre/genre"))
                    .andDo(print())
                    .andExpect(status().isUnauthorized());
        }
    }
}
