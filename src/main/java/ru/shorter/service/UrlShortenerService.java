package ru.shorter.service;

/**
 * Сервис для формирования короткой ссылки.
 *
 * @author Бородулин Никита Петрович.
 */
public interface UrlShortenerService {

    /**
     * Формирует короткую ссылку на основании длинной.
     */
    String shortenUrl(String longUrl);
}
