package ru.shorter.controller;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import ru.shorter.service.exception.LimitClicksException;
import ru.shorter.service.exception.NoSuchLinkException;

/**
 * Осуществляет обработку исключений возникших при работе со ссылками.
 *
 * @author Бородулин Никита Петрович.
 */
@ControllerAdvice
public class UrlExceptionHandler {

    /**
     * Обрабатывает ситуацию, когда не найдена ссылка.
     */
    @ExceptionHandler(NoSuchLinkException.class)
    public String handleNoSuchLinkException() {
        return "noLink";
    }


    /**
     * Обрабатывает ситуацию, когда количество переходов по ссылке превышено.
     */
    @ExceptionHandler(LimitClicksException.class)
    public String handleLimitClicksException() {
        return "linkExceeded";
    }
}
