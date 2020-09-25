package ru.job4j.numbers;

/**
 * ILogic.
 * Содержит логику преобразования прописных чисел в цифры.
 *
 * @author Oxana Menushina (oxsm@mail.ru).
 * @version $Id$
 * @since 0.1
 */
public interface ILogic {

    /**
     * Метод принимает строку прописных чисел
     * и возвращает преобразованный в цифры номер.
     *
     * @param string - строка из прописных чисел.
     * @return преобразованный в цифры номер.
     */
    String convert(String string);
}
