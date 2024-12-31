package ru.shorter.service;

import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpSession;

import java.util.UUID;

import static ru.shorter.config.LinkConfig.USER_ATTR;

/**
 * Сервис, который заполняет идентификатор пользователя в сессии.
 *
 * @author Бородулин Никита Петрович.
 */
@Service
public class UserUuidService {

    /**
     * Проверяет и заполняет при необходимости uuid пользователя в сессии.
     */
    public UUID generateUuid(HttpSession session) {
        var uuidUser = (UUID) session.getAttribute(USER_ATTR);
        if(uuidUser == null){
            uuidUser = UUID.randomUUID();
            session.setAttribute(USER_ATTR, uuidUser);
        }
        return uuidUser;
    }

}
