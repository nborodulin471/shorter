package ru.shorter.model;

import jakarta.persistence.Column;
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

    /**
     * Идентификатор ссылки.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID uuid;

    /**
     * Сохраненное значение длинной ссылки.
     *
     * @implNote Ожидается, что 1000 символов на хранение должно хватить.
     */
    @Column(length = 1000)
    private String longLink;

    /**
     * Сохраненное значение короткой ссылки.
     */
    private String shortLink;

    /**
     * Идентификатор пользователя.
     */
    private UUID userUuid;

    /**
     * Количество срабатываний ссылки.
     */
    private int clicksLeft;

    /**
     * Время когда ссылка устареет.
     */
    private Instant expirationDate;
}
