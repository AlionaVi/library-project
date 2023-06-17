package ru.itgirl.libraryproject.controller;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.itgirl.libraryproject.dto.*;
import ru.itgirl.libraryproject.model.Author;
import ru.itgirl.libraryproject.model.Book;
import ru.itgirl.libraryproject.model.Genre;
import ru.itgirl.libraryproject.repository.AuthorRepository;
import ru.itgirl.libraryproject.repository.BookRepository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class BookRestControllerTest {
    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testGetBookByNameV1() throws Exception {
        String bookName = "Анна Каренина";
        BookDto bookDto = new BookDto();
        bookDto.setId(1L);
        bookDto.setName(bookName);
        bookDto.setGenre("Роман");

        mockMvc.perform(MockMvcRequestBuilders.get("/book").param("name", bookName))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(bookDto.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.genre").value(bookDto.getGenre()));

    }

    @Test
    public void testGetBookByNameV2() throws Exception {
        String bookName = "Анна Каренина";
        BookDto bookDto = new BookDto();
        bookDto.setId(1L);
        bookDto.setName(bookName);
        bookDto.setGenre("Роман");

        mockMvc.perform(MockMvcRequestBuilders.get("/book/v2").param("name", bookName))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(bookDto.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.genre").value(bookDto.getGenre()));

    }

    @Test
    public void testGetBookByNameV3() throws Exception {
        String bookName = "Анна Каренина";
        BookDto bookDto = new BookDto();
        bookDto.setId(1L);
        bookDto.setName(bookName);
        bookDto.setGenre("Роман");

        mockMvc.perform(MockMvcRequestBuilders.get("/book/v3").param("name", bookName))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(bookDto.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.genre").value(bookDto.getGenre()));

    }


    @Test
    public void testCreateBook() throws Exception {

        BookCreateDto bookCreateDto = new BookCreateDto();
        bookCreateDto.setName("Алые паруса");
        bookCreateDto.setGenre_id(2L);

        mockMvc.perform(MockMvcRequestBuilders.post("/book/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"name\": \"Алые паруса\", \"genre_id\": 2 }"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Алые паруса"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.genre").value("Роман"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.authors").isEmpty());

    }

    @Test
    public void testUpdateBook() throws Exception {

        Long bookId = 1L;
        String updatedName = "Алые паруса";
        Long updatedGenre_id = 2L;

        BookUpdateDto bookUpdateDto = new BookUpdateDto();
        bookUpdateDto.setId(bookId);
        bookUpdateDto.setName(updatedName);
        bookUpdateDto.setGenre_id(updatedGenre_id);

        Book savedBook = new Book();
        savedBook.setId(bookId);
        savedBook.setName(updatedName);
        savedBook.setGenre(new Genre());
        savedBook.setAuthors(new HashSet<>());

        mockMvc.perform(MockMvcRequestBuilders.put("/book/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"id\": 1, \"name\": \"Алые паруса\", \"genre_id\": 2 }"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(bookId))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(updatedName))
                .andExpect(MockMvcResultMatchers.jsonPath("$.authors[0].books").isEmpty());
    }
    @Test
    public void testDeleteBook() throws Exception {
        Long id = 18L;

        mockMvc.perform(MockMvcRequestBuilders.delete("/book/delete/{id}", id))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetAllBook() throws Exception {

        List<BookDto> books = new ArrayList<>();

        List<AuthorDto> authorDto = new ArrayList<>();

        mockMvc.perform(MockMvcRequestBuilders.get("/books"))
                .andExpect(status().isOk());

    }

}

