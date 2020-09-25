package ru.job4j.numbers;

import java.util.Arrays;

/**
 * CheckInput.
 * Проверяет ввод пользователя.
 *
 * @author Oxana Menushina (oxsm@mail.ru).
 * @version $Id$
 * @since 0.1
 */
public class CheckInput {

    /**
     * Объект, предосттавляющий шаблоны для проверки чисел.
     */
    private NumberTemplate numberTemplate;

    /**
     * Конструктор.
     *
     * @param numberTemplate - объект,
     * предосттавляющий шаблоны для проверки чисел.
     */
    public CheckInput(NumberTemplate numberTemplate) {
        this.numberTemplate = numberTemplate;
    }

    /**
     * Метод проверяет ввод пользователя.
     *
     * @param string - строка ввода.
     * @return true - ввод корректный.
     */
    public boolean check(String string) {
        String[] words = string.toLowerCase().split(" ");
        return string.equalsIgnoreCase("exit") || Arrays.stream(words)
                .allMatch(w -> this.numberTemplate.getNumbers().keySet().stream().anyMatch(w::contains)
                        || this.numberTemplate.getTerms().entrySet().stream().anyMatch(e -> !e.getKey().equals("one digit") && e.getValue().test(w)));
    }
}