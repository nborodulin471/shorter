package ru.shorter.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.shorter.dao.LinkRepository;
import ru.shorter.model.Link;

import java.time.Instant;
import java.util.Arrays;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExpiredLinkServiceTest {

    @InjectMocks
    private ExpiredLinkService sut;

    @Mock
    private LinkRepository linkRepository;

    @Test
    @DisplayName("Проверяем, что ссылки удалены, если они устарели")
    void testRemoveExpiredLinks() {
        var link1 = new Link();
        link1.setExpirationDate(Instant.now().minusSeconds(60)); // Устаревшая ссылка
        var link2 = new Link();
        link2.setExpirationDate(Instant.now().plusSeconds(60)); // Действующая ссылка
        var link3 = new Link();
        link3.setExpirationDate(Instant.now().minusSeconds(120)); // Устаревшая ссылка

        when(linkRepository.findAll()).thenReturn(Arrays.asList(link1, link2, link3));

        sut.deleteExpiredLinks();

        verify(linkRepository).deleteAll(Arrays.asList(link1, link3));
    }
}