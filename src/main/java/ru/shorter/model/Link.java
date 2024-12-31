package ru.shorter.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

/**
 * Описывает структуру хранения ссылки в БД.
 *
 * @author Бородулин Никита Петрович.
 */
@Entity
@Getter
@Setter
public class Link {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String longLink;
    private String shortLink;
    private UUID userUuid;
    private int clicksLeft;
    private Instant expirationDate;
}
