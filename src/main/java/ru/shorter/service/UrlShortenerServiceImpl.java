package ru.shorter.service;

import org.springframework.stereotype.Service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.shorter.config.LinkConfig;

import java.util.Random;

/**
 * Сокращает переданную длинную ссылку в короткую.
 *
 * @author Бородулин Никита Петрович.
 * @implNote Генерирует ссылку на основании иденит
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UrlShortenerServiceImpl implements UrlShortenerService {

    private static final String CHAR_SET = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    private final LinkConfig linkConfig;
    private final Random random = new Random();

    /**
     * Набор символов для генерации короткой ссылки.
     */
    public String shortenUrl(@NonNull String longUrl) {
        log.debug("Начало генерации короткого урла, для длинного {}", longUrl);
        StringBuilder shortUrl = new StringBuilder(linkConfig.getUrlLength());
        for (int i = 0; i < linkConfig.getUrlLength(); i++) {
            shortUrl.append(CHAR_SET.charAt(random.nextInt(CHAR_SET.length())));
        }

        var shortUrlWithBase = String.format("%s%s", linkConfig.getBaseUrl(), shortUrl);

        log.debug("Был сгенерирован короткий урл: {}, для длинного урла {}", shortUrlWithBase, longUrl);

        return shortUrlWithBase;
    }
}
