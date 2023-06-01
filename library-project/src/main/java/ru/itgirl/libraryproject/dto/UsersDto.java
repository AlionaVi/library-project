package ru.itgirl.libraryproject.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Getter
public class UsersDto {
    private Long id;
    private String login;
    private String password;
    private String roles;

}
