package ru.itgirl.libraryproject.service;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.itgirl.libraryproject.dto.AuthorCreateDto;
import ru.itgirl.libraryproject.dto.AuthorDto;
import ru.itgirl.libraryproject.dto.AuthorUpdateDto;
import ru.itgirl.libraryproject.dto.BookDto;
import ru.itgirl.libraryproject.model.Author;
import ru.itgirl.libraryproject.repository.AuthorRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;

    @Override
    public AuthorDto getAuthorById(Long id) {
        log.info("Try to find author by id {}", id);
        Optional<Author> author = authorRepository.findById(id);
        if (author.isPresent()) {
            AuthorDto authorDto = convertEntityToDto(author.get());
            log.info("Author: {}", authorDto.toString());
            return authorDto;
        } else {
            log.error("Author with id: {} not found", id);
            throw new IllegalStateException("Author not found");
        }
    }


    @Override
    public AuthorDto getBySurnameV1(String surname) {
        log.info("Try to find author by surname {}", surname);
        Optional<Author> author = authorRepository.findAuthorBySurname(surname);
        if (author.isPresent()) {
            AuthorDto authorDto = convertEntityToDto(author.get());
            log.info("Author: {}", authorDto.toString());
            return authorDto;
        } else {
            log.error("Author with surname: {} not found", surname);
            throw new IllegalStateException("Author not found");
        }

    }

    @Override
    public AuthorDto getBySurnameV2(String surname) {
        log.info("Try to find author by surname {}", surname);
        Optional<Author> author = authorRepository.findAuthorBySurnameBySql(surname);
        if (author.isPresent()) {
            AuthorDto authorDto = convertEntityToDto(author.get());
            log.info("Author: {}", authorDto.toString());
            return authorDto;
        } else {
            log.error("Author with surname: {} not found", surname);
            throw new IllegalStateException("Author not found");
        }
    }

    @Override
    public AuthorDto getBySurnameV3(String surname) {
        Specification<Author> authorSpecification = Specification.where(new Specification<Author>() {
            @Override
            public Predicate toPredicate(Root<Author> root,
                                         CriteriaQuery<?> query,
                                         CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.equal(root.get("surname"), surname);
            }
        });
        log.info("Try to find author by surname {}", surname);
        Optional<Author> author = authorRepository.findOne(authorSpecification);
        if (author.isPresent()) {
            AuthorDto authorDto = convertEntityToDto(author.get());
            log.info("Author: {}", authorDto.toString());
            return authorDto;
        } else {
            log.error("Author with surname: {} not found", surname);
            throw new IllegalStateException("Author not found");
        }
    }

    @Override
    public AuthorDto createAuthor(AuthorCreateDto authorCreateDto) {
        log.info("Creating author: {}", authorCreateDto);
        Author author = authorRepository.save(convertDtoToEntity(authorCreateDto));
        AuthorDto authorDto = convertEntityToDto(author);
        log.info("Author created: {}", authorDto);
        return authorDto;
    }

    @Override
    public AuthorDto updateAuthor(AuthorUpdateDto authorUpdateDto) {
        Optional<Author> authorOptional = authorRepository.findById(authorUpdateDto.getId());
        if (authorOptional.isPresent()) {
            Author author = authorOptional.get();
            author.setName(authorUpdateDto.getName());
            author.setSurname(authorUpdateDto.getSurname());

            Author savedAuthor = authorRepository.save(author);
            AuthorDto authorDto = convertEntityToDto(savedAuthor);
            log.info("Author updated: {}", authorDto);
            return authorDto;
        } else {
            log.error("Failed to update author. Author not found for id: {}", authorUpdateDto.getId());
            throw new IllegalStateException("Author not update");
        }
    }

    @Override
    public AuthorDto deleteAuthor(Long id) {
        log.info("Deleting author: {}", id);
        authorRepository.deleteById(id);
        log.info("Author deleted: {}", id);
        return null;
    }

    @Override
    public List<AuthorDto> getAllAuthors() {
        log.info("Getting a list of all authors");
        List<Author> authors = authorRepository.findAll();
        return authors.stream()
                .map(this::convertEntityToDto)
                .peek(authorDto -> log.info("Retrieved author: {}", authorDto))
                .collect(Collectors.toList());
    }

    private Author convertDtoToEntity(AuthorCreateDto authorCreateDto) {
        return Author.builder()
                .name(authorCreateDto.getName())
                .surname(authorCreateDto.getSurname())
                .build();
    }

    private AuthorDto convertEntityToDto(Author author) {
        List<BookDto> bookDtoList = null;
        if (author.getBooks() != null) {
            bookDtoList = author.getBooks().stream()
                    .map(book -> BookDto.builder()
                            .genre(book.getGenre().getName())
                            .name(book.getName())
                            .id(book.getId())
                            .build())
                    .toList();

        }
        AuthorDto authorDto = AuthorDto.builder()
                .id(author.getId())
                .name(author.getName())
                .surname(author.getSurname())
                .books(bookDtoList)
                .build();
        return authorDto;
    }
}
