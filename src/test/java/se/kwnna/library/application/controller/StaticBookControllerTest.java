package se.kwnna.library.application.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import se.kwnna.library.application.LibraryApplication;
import se.kwnna.library.domain.book.Book;

@Disabled
@SpringBootTest(classes = { LibraryApplication.class })
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = BookMvcTestCreator.class)
@AutoConfigureMockMvc
class StaticBookControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private BookMvcTestCreator bookMvcTestCreator;

    @Nested
    class StaticDataset {

        @Test
        @WithMockUser
        void findByTitle() throws Exception {
            MvcResult result = mockMvc.perform(get("/api/book/find/title/Moln"))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andReturn();

            List<Book> actual = bookMvcTestCreator.fromMvcToDomain(result);

            assertEquals(1, actual.size());
        }

        @Test
        @WithMockUser
        void findByAuthor() throws Exception {
            MvcResult result = mockMvc.perform(get("/api/book/find/author/Stieg Larsson"))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andReturn();

            List<Book> actual = bookMvcTestCreator.fromMvcToDomain(result);

            assertEquals(2, actual.size());
        }
    }
}