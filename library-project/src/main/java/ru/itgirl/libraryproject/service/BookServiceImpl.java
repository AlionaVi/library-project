package ru.itgirl.libraryproject.service;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.itgirl.libraryproject.dto.*;
import ru.itgirl.libraryproject.model.Author;
import ru.itgirl.libraryproject.model.Book;
import ru.itgirl.libraryproject.model.Genre;
import ru.itgirl.libraryproject.repository.BookRepository;
import ru.itgirl.libraryproject.repository.GenreRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    private final GenreRepository genreRepository;

    @Override
    public BookDto getBookByNameV1(String name) {
        log.info("Try to find book by name {}", name);
        Optional<Book> book = bookRepository.findBookByName(name);
        if (book.isPresent()) {
            BookDto bookDto = convertEntityToDto(book.get());
            log.info("Book: {}", bookDto.toString());
            return bookDto;
        } else {
            log.error("Book with name: {} not found", name);
            throw new IllegalStateException("Book not found");
        }

    }

    @Override
    public BookDto getBookByNameV2(String name) {
        log.info("Try to find book by name {}", name);
        Optional<Book> book = bookRepository.findBookByNameBySql(name);
        if (book.isPresent()) {
            BookDto bookDto = convertEntityToDto(book.get());
            log.info("Book: {}", bookDto.toString());
            return bookDto;
        } else {
            log.error("Book with name: {} not found", name);
            throw new IllegalStateException("Book not found");
        }
    }

    @Override
    public BookDto getBookByNameV3(String name) {
        Specification<Book> bookSpecification = Specification.where(new Specification<Book>() {
            @Override
            public Predicate toPredicate(Root<Book> root,
                                         CriteriaQuery<?> query,
                                         CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.equal(root.get("name"), name);
            }
        });
        log.info("Try to find book by name {}", name);
        Optional<Book> book = bookRepository.findOne(bookSpecification);
        if (book.isPresent()) {
            BookDto bookDto = convertEntityToDto(book.get());
            log.info("Book: {}", bookDto.toString());
            return bookDto;
        } else {
            log.error("Book with name: {} not found", name);
            throw new IllegalStateException("Book not found");
        }
    }

    @Override
    public BookDto createBook(BookCreateDto bookCreateDto) {
        log.info("Creating book: {}", bookCreateDto);
        Book book = bookRepository.save(convertDtoToEntity(bookCreateDto));
        BookDto bookDto = convertEntityToDto(book);
        log.info("Book created: {}", bookDto);
        return bookDto;
    }


    @Override
    public BookDto updateBook(BookUpdateDto bookUpdateDto) {
        Optional<Book> bookOptional = bookRepository.findById(bookUpdateDto.getId());
        if (bookOptional.isPresent()) {
            Book book = bookOptional.get();
            book.setName(bookUpdateDto.getName());
            Genre genre = genreRepository.findById(bookUpdateDto.getGenre_id()).orElseThrow();
            book.setGenre(genre);
            Book savedBook = bookRepository.save(book);
            BookDto bookDto = convertEntityToDto(savedBook);
            log.info("Book updated: {}", bookDto);

            return bookDto;
        } else {
            log.error("Failed to update book. Book not found for id: {}", bookUpdateDto.getId());
            throw new IllegalStateException("Book not update");
        }
    }

    @Override
    public BookDto deleteBook(Long id) {
        log.info("Deleting book: {}", id);
        bookRepository.deleteById(id);
        log.info("Book deleted: {}", id);
        return null;
    }

    @Override
    public List<BookDto> getAllBooks() {
        log.info("Getting a list of all books");
        List<Book> books = bookRepository.findAll();
        return books.stream()
                .map(this::convertEntityToDto)
                .peek(authorDto -> log.info("Retrieved book: {}", authorDto))
                .collect(Collectors.toList());
    }

    private Book convertDtoToEntity(BookCreateDto bookCreateDto) {
        Genre genre = genreRepository.findById(bookCreateDto.getGenre_id())
                .orElseThrow(() -> new RuntimeException("Genre not found"));
        return Book.builder()
                .name(bookCreateDto.getName())
                .genre(genre)
                .build();
    }

    private BookDto convertEntityToDto(Book book) {
        List<AuthorDto> authorDtoList = null;
        if (book.getAuthors() != null) {
            authorDtoList = book.getAuthors()
                    .stream()
                    .map(author -> AuthorDto.builder()
                            .name(author.getName())
                            .surname(author.getSurname())
                            .id(author.getId())
                            .build())
                    .toList();
        }

        GenreDto genreDto = GenreDto.builder()
                .id(book.getGenre().getId())
                .name(book.getGenre().getName())
                .build();

        BookDto bookDto = BookDto.builder()
                .id(book.getId())
                .name(book.getName())
                .genre(genreDto.getName())
                .authors(authorDtoList)
                .build();
        return bookDto;
    }
}
