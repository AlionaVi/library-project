package ru.itgirl.libraryproject.service;

import ru.itgirl.libraryproject.dto.AuthorDto;

public interface AuthorService {
    AuthorDto getAuthorById(Long id);

    AuthorDto getBySurnameV1(String surname);

    AuthorDto getBySurnameV2(String surname);

    AuthorDto getBySurnameV3(String surname);

}
