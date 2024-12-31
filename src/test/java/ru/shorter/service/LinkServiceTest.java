package ru.shorter.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.shorter.config.LinkConfig;
import ru.shorter.dao.LinkRepository;
import ru.shorter.model.Link;
import ru.shorter.service.exception.LimitClicksException;
import ru.shorter.service.exception.NoSuchLinkException;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LinkServiceTest {

    @Mock
    private UrlShortenerService urlShortenerService;
    @Mock
    private LinkConfig linkConfig;
    @Mock
    private LinkRepository linkRepository;

    @InjectMocks
    private LinkService sut;

    private String longUrl;
    private String shortUrl;
    private Link link;

    @BeforeEach
    void setUp() {
        var userUuid = UUID.randomUUID();
        longUrl = "https://www.example.com/url";
        shortUrl = "shortUrl";
        link = new Link();
        link.setLongLink(longUrl);
        link.setUserUuid(userUuid);
        link.setShortLink(shortUrl);
        link.setClicksLeft(0);
        link.setExpirationDate(Instant.now().plusSeconds(600)); // устареет через 10 минут
    }

    @Test
    @DisplayName("Проверяем, что переход по ссылке прошел успешно")
    void testRedirectShortUrlSuccess() {
        var fullShortUrl = "http://localhost:8080/" + shortUrl;
        when(linkRepository.findByShortLink(fullShortUrl)).thenReturn(Optional.of(link));
        when(linkConfig.getBaseUrl()).thenReturn("http://localhost:8080/");
        when(linkConfig.getDefaultClicks()).thenReturn(10);

        var result = sut.redirectShortUrl(shortUrl);

        assertEquals(longUrl, result);
        assertEquals(1, link.getClicksLeft());
        verify(linkRepository).save(link);
    }

    @Test
    @DisplayName("Проверяем, что получаем ошибку, если пытаемся перейти по ссылке которой нет")
    void testRedirectShortUrlNoSuchLink() {
        when(linkRepository.findByShortLink(anyString())).thenReturn(Optional.empty());

        assertThrows(NoSuchLinkException.class, () -> sut.redirectShortUrl(shortUrl));
    }

    @Test
    @DisplayName("Проверяем, что работает срабатывание лимитов переходов по ссылкам")
    void testRedirectShortUrlLimitClicksExceeded() {
        link.setClicksLeft(10); // Устанавливаем количество переходов на максимум
        when(linkRepository.findByShortLink(anyString())).thenReturn(Optional.of(link));
        when(linkConfig.getBaseUrl()).thenReturn("http://localhost:8080/");
        when(linkConfig.getDefaultClicks()).thenReturn(10);

        assertThrows(LimitClicksException.class, () -> sut.redirectShortUrl(shortUrl));
    }
}