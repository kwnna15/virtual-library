package se.kwnna.library.application.controller;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import se.kwnna.library.domain.book.Book;

public class BookMvcTestCreator {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    public Book create() throws Exception {
        return create("title", "author");
    }

    public Book create(String title, String author) throws Exception {
        return create(title, author, "genre");
    }

    public Book create(String title, String author, String genre) throws Exception {
        return create(title, author, genre, "1234567890");
    }

    public Book create(String title, String author, String genre, String isbn) throws Exception {
        Book book = Book.builder()
                .withIsbn(isbn)
                .withAuthor(author)
                .withTitle(title)
                .withGenre(genre)
                .build();

        MvcResult result = mockMvc.perform(post("/admin/book/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(book))
                .with(csrf())
                .with(user("admin").roles("ADMIN")))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        return objectMapper.readValue(result.getResponse().getContentAsString(),
                new TypeReference<Book>() {
                });
    }

    public List<Book> fromMvcToDomain(MvcResult result) throws Exception {
        return objectMapper.readValue(result.getResponse().getContentAsString(),
                new TypeReference<List<Book>>() {
                });
    }
}
