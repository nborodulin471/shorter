package ru.shorter.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ru.shorter.model.Link;

import java.util.Optional;
import java.util.UUID;

/**
 * Репозиторий для хранения ссылок в БД.
 *
 * @author Бородулин Никита Петрович.
 */
@Repository
public interface LinkRepository extends JpaRepository<Link, Long> {

    Optional<Link> findByShortLink(String shortLink);
    Optional<Link> findByUserUuidAndLongLink(UUID userUuid,  String longLink);
}
