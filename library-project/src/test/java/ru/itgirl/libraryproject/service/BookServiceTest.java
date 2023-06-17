package ru.itgirl.libraryproject.service;

import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.domain.Specification;
import ru.itgirl.libraryproject.dto.*;
import ru.itgirl.libraryproject.model.Author;
import ru.itgirl.libraryproject.model.Book;
import ru.itgirl.libraryproject.model.Genre;
import ru.itgirl.libraryproject.repository.BookRepository;
import ru.itgirl.libraryproject.repository.GenreRepository;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class BookServiceTest {

    @Mock
    BookRepository bookRepository;
    @Mock
    GenreRepository genreRepository;

    @InjectMocks
    BookServiceImpl bookService;

    @Test
    public void testGetBookByNameV1() {
        Long id = 1L;
        String name = "Имя розы";
        Set<Book> books = new HashSet<>();
        Genre genre = new Genre(6L, "Детектив", books);

        Set<Author> authors = new HashSet<>();

        Book book = new Book(id, name, genre, authors);

        when(bookRepository.findBookByName(name)).thenReturn(Optional.of(book));

        BookDto bookDto = bookService.getBookByNameV1(name);

        verify(bookRepository).findBookByName(name);
        Assertions.assertEquals(bookDto.getId(), book.getId());
        Assertions.assertEquals(bookDto.getName(), book.getName());
    }

    @Test
    public void testGetBookByNameV1NotFound() {
        String name = "Имя розы";

        when(bookRepository.findBookByName(name)).thenReturn(Optional.empty());

        Assertions.assertThrows(IllegalStateException.class, () -> bookService.getBookByNameV1(name));
        verify(bookRepository).findBookByName(name);
    }

    @Test
    public void testGetBookByNameV2() {
        Long id = 4L;
        String name = "Мастер и Маргарита";
        Set<Book> books = new HashSet<>();
        Genre genre = new Genre(2L, "Роман", books);

        Set<Author> authors = new HashSet<>();

        Book book = new Book(id, name, genre, authors);

        when(bookRepository.findBookByNameBySql(name)).thenReturn(Optional.of(book));

        BookDto bookDto = bookService.getBookByNameV2(name);

        verify(bookRepository).findBookByNameBySql(name);
        Assertions.assertEquals(bookDto.getId(), book.getId());
        Assertions.assertEquals(bookDto.getName(), book.getName());
    }

    @Test
    public void testGetBookByNameV2NotFound() {
        String name = "Братья Карамазовы";

        when(bookRepository.findBookByNameBySql(name)).thenReturn(Optional.empty());

        Assertions.assertThrows(IllegalStateException.class, () -> bookService.getBookByNameV2(name));
        verify(bookRepository).findBookByNameBySql(name);
    }

    @Test
    public void testGetBookByNameV3() {
        Long id = 1L;
        String name = "Имя розы";
        Set<Book> books = new HashSet<>();
        Set<Author> authors = new HashSet<>();
        Genre genre = new Genre(6L, "Детектив", books);
        Book book = new Book(id, name, genre, authors);

        when(bookRepository.findOne(any(Specification.class)))
                .thenReturn(Optional.of(book));

        BookDto result = bookService.getBookByNameV3(name);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(id, result.getId());
        Assertions.assertEquals(name, result.getName());
        verify(bookRepository).findOne(any(Specification.class));
    }

    @Test
    public void testGetBookByNameV3NotFound() {
        String name = "Имя розы";

        when(bookRepository.findOne(any(Specification.class)))
                .thenReturn(Optional.empty());
        Assertions.assertThrows(IllegalStateException.class, () -> bookService.getBookByNameV3(name));
        verify(bookRepository).findOne(any(Specification.class));
    }


    @Test
    public void testCreateBook() {
        BookCreateDto bookCreateDto = new BookCreateDto();
        bookCreateDto.setName("Джейн Эйр");
        bookCreateDto.setGenre_id(2L);

        Genre genre = new Genre();
        genre.setId(2L);
        genre.setName("Роман");
        when(genreRepository.findById(2L)).thenReturn(Optional.of(genre));

        Book book = new Book();
        book.setId(3L);
        book.setName("Джейн Эйр");
        book.setGenre(genre);

        when(bookRepository.save(any(Book.class))).thenReturn(book);

        BookDto bookDto = bookService.createBook(bookCreateDto);

        Assertions.assertNotNull(bookDto);
        Assertions.assertEquals(3L, bookDto.getId());
        Assertions.assertEquals("Джейн Эйр", bookDto.getName());
        Assertions.assertNotNull(bookDto.getGenre());
        Assertions.assertEquals("Роман", bookDto.getGenre());
        Assertions.assertNull(bookDto.getAuthors());

        verify(genreRepository).findById(2L);
        verify(bookRepository).save(any(Book.class));

    }

    @Test
    public void testBookUpdate() {
        Long bookId = 1L;
        String updateName = "Алые паруса";
        Long updateGenre_id = 2L;
        Set<Author> authors = new HashSet<>();

        BookUpdateDto bookUpdateDto = new BookUpdateDto(bookId, updateName, updateGenre_id);


        Genre genre = new Genre();
        genre.setId(2L);
        genre.setName("Роман");
        when(genreRepository.findById(2L)).thenReturn(Optional.of(genre));

        Book existingBook = new Book(bookId, "Алые паруса", genre, authors);
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(existingBook));
        when(bookRepository.save(any(Book.class))).thenReturn(existingBook);

        BookDto bookDto = bookService.updateBook(bookUpdateDto);

        Assertions.assertNotNull(bookDto);
        Assertions.assertEquals(bookId, bookDto.getId());
        Assertions.assertEquals(updateName, bookDto.getName());
        Assertions.assertNotNull(bookDto.getGenre());
        Assertions.assertEquals(genre.getName(), bookDto.getGenre());
        Assertions.assertTrue(bookDto.getAuthors().isEmpty());


        verify(bookRepository).findById(bookId);
        verify(bookRepository).save(any(Book.class));
    }

    @Test
    public void testUpdateBookNotFound() {
        Long bookId = 1L;
        String updateName = "Алые паруса";
        Long updateGenre_id = 2L;

        BookUpdateDto bookUpdateDto = new BookUpdateDto(bookId, updateName, updateGenre_id);

        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        Assertions.assertThrows(IllegalStateException.class, () -> bookService.updateBook(bookUpdateDto));

        verify(bookRepository).findById(bookId);
        verify(bookRepository, never()).save(any(Book.class));
    }

    @Test
    public void testDeleteBook() {

        Long id = 17L;
        String name = "Алые паруса";
        Long genre_id = 2L;
        Set<Author> authors = new HashSet<>();
        Set<Book> books = new HashSet<>();
        Genre genre = new Genre(2L, "Роман", books);

        Book book = new Book(id, name, genre, authors);

        when(bookRepository.findById(id)).thenReturn(Optional.of(book));
        doNothing().when(bookRepository).deleteById(id);

        BookDto bookDto = bookService.deleteBook(id);

        verify(bookRepository).deleteById(id);

    }

    @Test
    public void testGetAllBook() {

        List<Book> books = new ArrayList<>();
        Set<Book> allBooks = new HashSet<>();
        Genre genre = new Genre(2L, "Роман", allBooks);
        books.add(new Book(5L, "Детство", genre, new HashSet<>()));
        books.add(new Book(6L, "Юность", genre, new HashSet<>()));
        allBooks.addAll(books);

        when(bookRepository.findAll()).thenReturn(books);

        List<BookDto> bookDto = bookService.getAllBooks();

        Assertions.assertEquals(2, bookDto.size());
        Assertions.assertEquals("Детство", bookDto.get(0).getName());
        Assertions.assertEquals("Юность", bookDto.get(1).getName());

        verify(bookRepository).findAll();
    }
}
