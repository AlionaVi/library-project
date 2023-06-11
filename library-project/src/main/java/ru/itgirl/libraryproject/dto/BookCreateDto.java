package ru.itgirl.libraryproject.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class BookCreateDto {
    @NotBlank(message = "Необходимо указать название книги")
    @Size(min = 1, max = 80)
    private String name;
    private Long genre_id;
}
