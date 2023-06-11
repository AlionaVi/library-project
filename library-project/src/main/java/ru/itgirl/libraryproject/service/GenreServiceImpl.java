package ru.itgirl.libraryproject.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.itgirl.libraryproject.dto.AuthorDto;
import ru.itgirl.libraryproject.dto.BookDto;
import ru.itgirl.libraryproject.dto.GenreDto;
import ru.itgirl.libraryproject.model.Genre;
import ru.itgirl.libraryproject.repository.GenreRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j

public class GenreServiceImpl implements GenreService {

    private final GenreRepository genreRepository;

    @Override
    public GenreDto getGenreById(Long id) {
        log.info("Try to find genre by id {}", id);
        Optional<Genre> genre = genreRepository.findById(id);
        if (genre.isPresent()) {
            GenreDto genreDto = convertEntityToDto(genre.get());
            log.info("Genre: {}", genreDto.toString());
            return genreDto;
        } else {
            log.error("Genre with id: {} not found", id);
            throw new IllegalStateException("Genre not found");
        }
    }

    private GenreDto convertEntityToDto(Genre genre) {
        List<BookDto> bookDtoList = genre.getBooks().stream()
                .map(book -> {
                    List<AuthorDto> authorDtoList = book.getAuthors().stream()
                            .map(author -> AuthorDto.builder()
                                    .name(author.getName())
                                    .surname(author.getSurname())
                                    .id(author.getId())
                                    .build())
                            .toList();

                    return BookDto.builder()
                            .name(book.getName())
                            .id(book.getId())
                            .authors(authorDtoList)
                            .build();
                })
                .toList();

        GenreDto genreDto = GenreDto.builder()
                .id(genre.getId())
                .name(genre.getName())
                .books(bookDtoList)
                .build();

        return genreDto;
    }

}




