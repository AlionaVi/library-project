package ru.itgirl.libraryproject.model;

import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Entity
@Data

public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column (nullable = false)
    private String login;

    @Column (nullable = false)
    private String password;

    @Column (nullable = false)
    private String roles;

  }

