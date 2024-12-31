package ru.shorter.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Исключение, выбрасывается когда не удалось найти ссылку в БД.
 *
 * @author Бородулин Никита Петрович.
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class NoSuchLinkException extends RuntimeException {
    public NoSuchLinkException() {
        super("Не удалось найти объект ссылки в БД");
    }
}
