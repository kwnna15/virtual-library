package se.kwnna.library.application.controller;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import se.kwnna.library.application.LibraryApplication;
import se.kwnna.library.datastore.BookRepository;
import se.kwnna.library.domain.BookTestBuilder;
import se.kwnna.library.domain.book.Book;

@SpringBootTest(classes = { LibraryApplication.class })
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = BookMvcTestCreator.class)
@AutoConfigureMockMvc
class AdminBookControllerTest {

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
    class Save {

        @Test
        @WithMockUser(roles = "ADMIN")
        public void create() throws Exception {
            Book book = BookTestBuilder.aBook();

            mockMvc.perform(post("/admin/book/save")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(new ObjectMapper().writeValueAsString(book))
                    .with(csrf()))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").exists())
                    .andExpect(jsonPath("$.isbn").value(book.getIsbn()))
                    .andExpect(jsonPath("$.title").value(book.getTitle()))
                    .andExpect(jsonPath("$.author").value(book.getAuthor()));
        }

        @Test
        @WithMockUser(roles = "ADMIN")
        public void update() throws Exception {
            Book book = bookMvcTestCreator.create();
            Book bookToUpdate = book.toBuilder()
                    .withTitle("new title")
                    .build();

            mockMvc.perform(post("/admin/book/save")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(new ObjectMapper().writeValueAsString(bookToUpdate))
                    .with(csrf()))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(bookToUpdate.getId()))
                    .andExpect(jsonPath("$.isbn").value(bookToUpdate.getIsbn()))
                    .andExpect(jsonPath("$.title").value(bookToUpdate.getTitle()))
                    .andExpect(jsonPath("$.author").value(bookToUpdate.getAuthor()));
        }

        @Test
        @WithMockUser(roles = "ADMIN")
        public void invalidIsbn() throws Exception {
            Book book = BookTestBuilder.aBook("invalid");

            mockMvc.perform(post("/admin/book/save")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(new ObjectMapper().writeValueAsString(book))
                    .with(csrf()))
                    .andDo(print())
                    .andExpect(status().isBadRequest());
        }

        @Test
        @WithMockUser
        public void unauthorizedUser() throws Exception {
            Book book = BookTestBuilder.aBook();

            mockMvc.perform(post("/admin/book/save")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(new ObjectMapper().writeValueAsString(book))
                    .with(csrf()))
                    .andDo(print())
                    .andExpect(status().isForbidden());
        }

        @Test
        @WithAnonymousUser
        void unauthenticatedUser() throws Exception {
            Book book = BookTestBuilder.aBook();

            mockMvc.perform(post("/admin/book/save")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(new ObjectMapper().writeValueAsString(book))
                    .with(csrf()))
                    .andDo(print())
                    .andExpect(status().isUnauthorized());
        }
    }

    @Nested
    class Get {

        @Test
        @WithMockUser(roles = "ADMIN")
        public void success() throws Exception {
            Book book = bookMvcTestCreator.create();

            mockMvc.perform(get("/admin/book/get/{id}", book.getId()))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(book.getId()))
                    .andExpect(jsonPath("$.isbn").value(book.getIsbn()))
                    .andExpect(jsonPath("$.title").value(book.getTitle()))
                    .andExpect(jsonPath("$.author").value(book.getAuthor()));
        }

        @Test
        @WithMockUser(roles = "ADMIN")
        public void notFound() throws Exception {
            mockMvc.perform(get("/admin/book/get/{id}", -1))
                    .andDo(print())
                    .andExpect(status().isNotFound());
        }

        @Test
        @WithMockUser
        public void unauthorizedUser() throws Exception {
            mockMvc.perform(get("/admin/book/get/{id}", -1))
                    .andDo(print())
                    .andExpect(status().isForbidden());
        }

        @Test
        @WithAnonymousUser
        void unauthenticatedUser() throws Exception {
            mockMvc.perform(get("/admin/book/get/{id}", -1))
                    .andDo(print())
                    .andExpect(status().isUnauthorized());
        }
    }

    @Nested
    class Delete {

        @Test
        @WithMockUser(roles = "ADMIN")
        public void success() throws Exception {
            Book book = bookMvcTestCreator.create();

            mockMvc.perform(delete("/admin/book/delete/{id}", book.getId())
                    .with(csrf()))
                    .andDo(print())
                    .andExpect(status().isNoContent());
        }

        @Test
        @WithMockUser(roles = "ADMIN")
        public void notFound() throws Exception {
            mockMvc.perform(delete("/admin/book/delete/{id}", -1)
                    .with(csrf()))
                    .andDo(print())
                    .andExpect(status().isNotFound());
        }

        @Test
        @WithMockUser
        public void unauthorizedUser() throws Exception {
            mockMvc.perform(delete("/admin/book/delete/{id}", -1)
                    .with(csrf()))
                    .andDo(print())
                    .andExpect(status().isForbidden());
        }

        @Test
        @WithAnonymousUser
        void unauthenticatedUser() throws Exception {
            mockMvc.perform(delete("/admin/book/delete/{id}", -1)
                    .with(csrf()))
                    .andDo(print())
                    .andExpect(status().isUnauthorized());
        }
    }
}
