package ru.job4j.numbers;

/**
 * Input.
 * Класс выводит на консоль текст и принимает строку ввода.
 *
 * @author Oxana Menushina (oxsm@mail.ru).
 * @version $Id$
 * @since 0.1
 */
public interface Input {

    /**
     * Метод выводит на консоль текст и принимает строку ввода.
     *
     * @param question - текст, выводимый на консоль.
     * @return строка ввода.
     */
    String ask(String question);
}