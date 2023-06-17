package ru.itgirl.libraryproject.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.domain.Specification;
import ru.itgirl.libraryproject.dto.AuthorCreateDto;
import ru.itgirl.libraryproject.dto.AuthorDto;
import ru.itgirl.libraryproject.dto.AuthorUpdateDto;
import ru.itgirl.libraryproject.model.Author;
import ru.itgirl.libraryproject.model.Book;
import ru.itgirl.libraryproject.repository.AuthorRepository;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class AuthorServiceTest {

    @Mock
    AuthorRepository authorRepository;

    @InjectMocks
    AuthorServiceImpl authorService;

    @Test
    public void testGetAuthorById() {
        Long id = 1L;
        String name = "John";
        String surname = "Doe";
        Set<Book> books = new HashSet<>();

        Author author = new Author(id, name, surname, books);

        when(authorRepository.findById(id)).thenReturn(Optional.of(author));

        AuthorDto authorDto = authorService.getAuthorById(id);
        verify(authorRepository).findById(id);
        Assertions.assertEquals(authorDto.getId(), author.getId());
        Assertions.assertEquals(authorDto.getName(), author.getName());
        Assertions.assertEquals(authorDto.getSurname(), author.getSurname());

    }

    @Test
    public void testGetAuthorByIdFailed() {
        Long id = 1L;

        when(authorRepository.findById(id)).thenReturn(Optional.empty());

        Assertions.assertThrows(IllegalStateException.class, () -> authorService.getAuthorById(id));
        verify(authorRepository).findById(id);
    }

    @Test
    public void testGetBySurnameV1() {
        Long id = 1L;
        String name = "John";
        String surname = "Doe";
        Set<Book> books = new HashSet<>();

        Author author = new Author(id, name, surname, books);

        when(authorRepository.findAuthorBySurname(surname)).thenReturn(Optional.of(author));

        AuthorDto authorDto = authorService.getBySurnameV1(surname);
        verify(authorRepository).findAuthorBySurname(surname);
        Assertions.assertEquals(authorDto.getId(), author.getId());
        Assertions.assertEquals(authorDto.getName(), author.getName());
        Assertions.assertEquals(authorDto.getSurname(), author.getSurname());
    }

    @Test
    public void testGetBySurnameV1NotFound() {
        String surname = "Doe";

        when(authorRepository.findAuthorBySurname(surname)).thenReturn(Optional.empty());

        Assertions.assertThrows(IllegalStateException.class, () -> authorService.getBySurnameV1(surname));
        verify(authorRepository).findAuthorBySurname(surname);
    }

    @Test
    public void testGetBySurnameV2() {
        Long id = 1L;
        String name = "John";
        String surname = "Doe";
        Set<Book> books = new HashSet<>();

        Author author = new Author(id, name, surname, books);

        when(authorRepository.findAuthorBySurnameBySql(surname)).thenReturn(Optional.of(author));

        AuthorDto authorDto = authorService.getBySurnameV2(surname);
        verify(authorRepository).findAuthorBySurnameBySql(surname);
        Assertions.assertEquals(authorDto.getId(), author.getId());
        Assertions.assertEquals(authorDto.getName(), author.getName());
        Assertions.assertEquals(authorDto.getSurname(), author.getSurname());
    }

    @Test
    public void testGetBySurnameV2NotFound() {
        String surname = "Doe";

        when(authorRepository.findAuthorBySurnameBySql(surname)).thenReturn(Optional.empty());

        Assertions.assertThrows(IllegalStateException.class, () -> authorService.getBySurnameV2(surname));
        verify(authorRepository).findAuthorBySurnameBySql(surname);
    }

    @Test
    public void testGetBySurnameV3() {
        Long id = 1L;
        String name = "John";
        String surname = "Doe";
        Set<Book> books = new HashSet<>();
        Author author = new Author(id, name, surname, books);

        when(authorRepository.findOne(any(Specification.class)))
                .thenReturn(Optional.of(author));

        AuthorDto result = authorService.getBySurnameV3(surname);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(id, result.getId());
        Assertions.assertEquals(name, result.getName());
        Assertions.assertEquals(surname, result.getSurname());
        verify(authorRepository).findOne(any(Specification.class));
    }

    @Test
    public void testGetBySurnameV3NotFound() {
        String surname = "Doe";

        when(authorRepository.findOne(any(Specification.class)))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(IllegalStateException.class, () -> authorService.getBySurnameV3(surname));
        verify(authorRepository).findOne(any(Specification.class));
    }


    @Test
    public void testCreateAuthor() {

        AuthorCreateDto authorCreateDto = new AuthorCreateDto();
        authorCreateDto.setName("John");
        authorCreateDto.setSurname("Doe");

        Author author = new Author();
        author.setId(1L);
        author.setName("John");
        author.setSurname("Doe");

        when(authorRepository.save(any(Author.class))).thenReturn(author);

        AuthorDto authorDto = authorService.createAuthor(authorCreateDto);

        Assertions.assertEquals(1L, authorDto.getId());
        Assertions.assertEquals("John", authorDto.getName());
        Assertions.assertEquals("Doe", authorDto.getSurname());
        Assertions.assertNull(authorDto.getBooks());
    }

    @Test
    public void testUpdateAuthor() {
        Long authorId = 1L;
        String updatedName = "John";
        String updatedSurname = "Doe";
        Set<Book> books = new HashSet<>();

        AuthorUpdateDto authorUpdateDto = new AuthorUpdateDto(authorId, updatedName, updatedSurname);

        Author existingAuthor = new Author (authorId, "Александр", "Пушкин", books);
        when(authorRepository.findById(authorId))
                .thenReturn(Optional.of(existingAuthor));
        when(authorRepository.save(any(Author.class)))
                .thenReturn(existingAuthor);

        AuthorDto authorDto = authorService.updateAuthor(authorUpdateDto);

        Assertions.assertNotNull(authorDto);
        Assertions.assertEquals(authorId, authorDto.getId());
        Assertions.assertEquals(updatedName, authorDto.getName());
        Assertions.assertEquals(updatedSurname, authorDto.getSurname());
        Assertions.assertNotNull(authorDto.getBooks());
        Assertions.assertEquals(books.size(), authorDto.getBooks().size());
        Assertions.assertTrue(authorDto.getBooks().containsAll(books));
        verify(authorRepository).findById(authorId);
        verify(authorRepository).save(any(Author.class));
    }

    @Test
    public void testUpdateAuthorNotFound() {
        Long authorId = 1L;
        String updatedName = "John";
        String updatedSurname = "Doe";

        AuthorUpdateDto authorUpdateDto = new AuthorUpdateDto(authorId, updatedName, updatedSurname);

        when(authorRepository.findById(authorId)).thenReturn(Optional.empty());

        Assertions.assertThrows(IllegalStateException.class, () -> authorService.updateAuthor(authorUpdateDto));

        verify(authorRepository).findById(authorId);
        verify(authorRepository, never()).save(any(Author.class));
    }

    @Test
    public void testDeleteAuthor() {

        Long id = 2L;
        String name = "Николай";
        String surname = "Гоголь";
        Set<Book> books = new HashSet<>();

        Author author = new Author(id, name, surname, books);

        when(authorRepository.findById(id)).thenReturn(Optional.of(author));
        doNothing().when(authorRepository).deleteById(id);

        AuthorDto authorDto = authorService.deleteAuthor(id);

        verify(authorRepository).deleteById(id);

    }

    @Test
    public void testGetAllAuthor() {
        List<Author> authors = new ArrayList<>();
        authors.add(new Author(1L, "Bill", "Novak", new HashSet<>()));
        authors.add(new Author(2L, "Jake", "Rob", new HashSet<>()));

        when(authorRepository.findAll()).thenReturn(authors);

        List<AuthorDto> authorDto = authorService.getAllAuthors();

        Assertions.assertEquals(2, authorDto.size());
        Assertions.assertEquals("Bill", authorDto.get(0).getName());
        Assertions.assertEquals("Novak", authorDto.get(0).getSurname());
        Assertions.assertEquals("Jake", authorDto.get(1).getName());
        Assertions.assertEquals("Rob", authorDto.get(1).getSurname());

        verify(authorRepository).findAll();
    }
}


