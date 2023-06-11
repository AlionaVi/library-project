package ru.itgirl.libraryproject.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class AuthorCreateDto {

    @Size(min = 3, max = 10)
    @NotBlank(message = "You need to enter a name")
    private String name;

    @NotBlank(message = "You need to enter a surname")
    private String surname;

}
