package ru.shorter.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.shorter.dao.LinkRepository;

import java.time.Instant;

/**
 * Шедулер который удаляет устаревшие ссылки по заданному расписанию.
 *
 * @author Бородулин Никита Петрович.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ExpiredLinkService {

    private final LinkRepository linkRepository;

    /**
     * Очищает устаревшие ссылки раз в заданный промежуток времени.
     */
    @Scheduled(fixedRate = 600000) // 1 раз в десять минут
    public void isExpired() {
        var expiredLinks = linkRepository.findAll().stream()
                .filter(link -> isExpired(link.getExpirationDate()))
                .toList();

        log.debug("Ссылки будут удалены: {}", expiredLinks);

        linkRepository.deleteAll(expiredLinks);
    }

    /**
     * Проверяет, не истекла ли дата.
     */
    private boolean isExpired(Instant expirationDate) {
        return Instant.now().isAfter(expirationDate);
    }

}
