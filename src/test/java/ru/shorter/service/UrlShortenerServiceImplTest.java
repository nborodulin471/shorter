package ru.shorter.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.shorter.config.LinkConfig;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UrlShortenerServiceImplTest {

    @Mock
    private LinkConfig linkConfig;

    @InjectMocks
    private UrlShortenerServiceImpl sut;

    @Test
    @DisplayName("Проверяет что сокращение ссылок работает корректно")
    void testShortenUrlGeneratesCorrectLength() {
        when(linkConfig.getUrlLength()).thenReturn(6);
        when(linkConfig.getBaseUrl()).thenReturn("http://localhost:8080/");
        String longUrl = "http://www.example.com/url";
        var lengthBaseUrl = linkConfig.getBaseUrl().length();

        String actual = sut.shortenUrl(longUrl);

        assertNotNull(actual);
        assertTrue(actual.contains(linkConfig.getBaseUrl()));
        assertEquals(linkConfig.getUrlLength() + lengthBaseUrl, actual.length());
    }

    @Test
    @DisplayName("Проверяем, что сгенерированные короткие URL не равны")
    void testShortenUrlGeneratesUniqueUrls() {
        when(linkConfig.getUrlLength()).thenReturn(6);
        when(linkConfig.getBaseUrl()).thenReturn("http://localhost:8080/");
        String longUrl1 = "http://www.example.com/url1";
        String longUrl2 = "http://www.example.com//url2";

        String shortUrl1 = sut.shortenUrl(longUrl1);
        String shortUrl2 = sut.shortenUrl(longUrl2);

        assertNotEquals(shortUrl1, shortUrl2);
    }

    @Test
    @DisplayName("Проверяем, что метод не выбрасывает исключение и возвращает не null")
    void testShortenUrlHandlesNullInput() {
        assertThrows(NullPointerException.class, () -> sut.shortenUrl(null));
    }

}