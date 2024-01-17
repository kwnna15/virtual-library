package se.kwnna.library.application.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import se.kwnna.library.domain.book_loan_register.BookLoanRegister;

public class BookLoanRegisterMvcTestCreator {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    public BookLoanRegister fromMvcToDomain(MvcResult result) throws Exception {
        return objectMapper.readValue(result.getResponse().getContentAsString(),
                new TypeReference<BookLoanRegister>() {
                });
    }

    public BookLoanRegister create(String string) {
        return null;
    }

}
