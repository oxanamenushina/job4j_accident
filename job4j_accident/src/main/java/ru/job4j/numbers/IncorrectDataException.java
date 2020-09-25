package ru.job4j.numbers;

/**
 * IncorrectDataException.
 * Исключение выбрасывается при некорректном вводе пользователя.
 *
 * @author Oxana Menushina (oxsm@mail.ru).
 * @version $Id$
 * @since 0.1
 */
public class IncorrectDataException extends RuntimeException {

    /**
     * Конструктор без параметров.
     */
    public IncorrectDataException() {
    }

    /**
     * Конструктор.
     *
     * @param info - информация о выбрасываемом исключении.
     */
    public IncorrectDataException(String info) {
        System.out.println(info);
    }
}