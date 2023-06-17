package ru.itgirl.libraryproject.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.itgirl.libraryproject.dto.AuthorCreateDto;
import ru.itgirl.libraryproject.dto.AuthorDto;
import ru.itgirl.libraryproject.dto.AuthorUpdateDto;
import ru.itgirl.libraryproject.dto.BookDto;
import ru.itgirl.libraryproject.model.Author;
import ru.itgirl.libraryproject.repository.AuthorRepository;

import java.util.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class AuthorRestControllerTest {

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testGetAuthorById() throws Exception {
        Long authorId = 2L;
        AuthorDto authorDto = new AuthorDto();
        authorDto.setId(authorId);
        authorDto.setName("Николай");
        authorDto.setSurname("Гоголь");

        mockMvc.perform(MockMvcRequestBuilders.get("/author/{id}", authorId))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(authorDto.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(authorDto.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.surname").value(authorDto.getSurname()));

    }

    @Test
    public void testGetBySurnameV1() throws Exception {
        String authorSurname = "Гоголь";
        AuthorDto authorDto = new AuthorDto();
        authorDto.setId(2L);
        authorDto.setName("Николай");
        authorDto.setSurname(authorSurname);

        mockMvc.perform(MockMvcRequestBuilders.get("/author")
                        .param("surname", authorSurname))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(authorDto.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(authorDto.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.surname").value(authorDto.getSurname()));

    }

    @Test
    public void testGetBySurnameV2() throws Exception {
        String authorSurname = "Гоголь";
        AuthorDto authorDto = new AuthorDto();
        authorDto.setId(2L);
        authorDto.setName("Николай");
        authorDto.setSurname(authorSurname);

        mockMvc.perform(MockMvcRequestBuilders.get("/author/v2")
                        .param("surname", authorSurname))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(authorDto.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(authorDto.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.surname").value(authorDto.getSurname()));

    }

    @Test
    public void testGetBySurnameV3() throws Exception {
        String authorSurname = "Гоголь";
        AuthorDto authorDto = new AuthorDto();
        authorDto.setId(2L);
        authorDto.setName("Николай");
        authorDto.setSurname(authorSurname);

        mockMvc.perform(MockMvcRequestBuilders.get("/author/v3")
                        .param("surname", authorSurname))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(authorDto.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(authorDto.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.surname").value(authorDto.getSurname()));

    }

    @Test
    public void testCreateAuthor() throws Exception {

        AuthorCreateDto authorCreateDto = new AuthorCreateDto();
        authorCreateDto.setName("John");
        authorCreateDto.setSurname("Doe");

        mockMvc.perform(MockMvcRequestBuilders.post("/author/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"name\": \"John\", \"surname\": \"Doe\" }"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("John"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.surname").value("Doe"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.books").isEmpty());

    }

    @Test
    public void testUpdateAuthor() throws Exception {
        Long authorId = 3L;
        String updatedName = "John";
        String updatedSurname = "Doe";

        AuthorUpdateDto authorUpdateDto = new AuthorUpdateDto();
        authorUpdateDto.setId(authorId);
        authorUpdateDto.setName(updatedName);
        authorUpdateDto.setSurname(updatedSurname);

        Author savedAuthor = new Author();
        savedAuthor.setId(authorId);
        savedAuthor.setName(updatedName);
        savedAuthor.setSurname(updatedSurname);
        savedAuthor.setBooks(new HashSet<>());

        mockMvc.perform(MockMvcRequestBuilders.put("/author/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"id\": 3, \"name\": \"John\", \"surname\": \"Doe\" }"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(authorId))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(updatedName))
                .andExpect(MockMvcResultMatchers.jsonPath("$.surname").value(updatedSurname))
                .andExpect(MockMvcResultMatchers.jsonPath("$.books.length()").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.books[0].id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.books[0].name").value("Анна Каренина"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.books[0].genre").value("Роман"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.books[0].authors").isEmpty());
    }

    @Test
    public void testDeleteAuthor() throws Exception {
        Long id = 2L;

        mockMvc.perform(MockMvcRequestBuilders.delete("/author/delete/{id}", id))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetAllAuthor() throws Exception {

        List<BookDto> books = new ArrayList<>();

        List<AuthorDto> authorDto = new ArrayList<>();

        mockMvc.perform(MockMvcRequestBuilders.get("/authors"))
                .andExpect(status().isOk());

    }
}
