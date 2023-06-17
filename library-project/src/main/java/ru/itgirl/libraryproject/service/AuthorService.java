package ru.itgirl.libraryproject.service;

import ru.itgirl.libraryproject.dto.AuthorCreateDto;
import ru.itgirl.libraryproject.dto.AuthorDto;
import ru.itgirl.libraryproject.dto.AuthorUpdateDto;

import java.util.List;

public interface AuthorService {
    AuthorDto getAuthorById(Long id);

    AuthorDto getBySurnameV1(String surname);

    AuthorDto getBySurnameV2(String surname);

    AuthorDto getBySurnameV3(String surname);

    AuthorDto createAuthor (AuthorCreateDto authorCreateDto);

    AuthorDto updateAuthor (AuthorUpdateDto authorUpdateDto);

    AuthorDto deleteAuthor (Long id);

    List<AuthorDto> getAllAuthors();

}
