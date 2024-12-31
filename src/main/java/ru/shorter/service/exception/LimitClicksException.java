package ru.shorter.service.exception;

/**
 * Исключение, выбрасывается когда превышено кол-во использований.
 *
 * @author Бородулин Никита Петрович.
 */
public class LimitClicksException extends RuntimeException {
    public LimitClicksException(String message) {
        super(message);
    }
}
