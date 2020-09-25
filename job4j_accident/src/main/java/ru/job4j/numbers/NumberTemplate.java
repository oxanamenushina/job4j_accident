package ru.job4j.numbers;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

/**
 * NumberTemplate.
 * Класс создает числовые шаблоны.
 *
 * @author Oxana Menushina (oxsm@mail.ru).
 * @version $Id$
 * @since 0.1
 */
public class NumberTemplate {

    /**
     * Шаблоны частей названий цифр.
     */
    private Map<String, Integer> numbers;

    /**
     * Условия, определяющие числовые разряды.
     */
    private Map<String, Predicate<String>> terms;

    /**
     * Конструктор без параметров.
     */
    public NumberTemplate() {
        this.numbers = this.fillNumbers();
        this.terms = this.fillPredicates();
    }

    /**
     * Метод заполняет отображение с шаблонами частей названий цифр.
     *
     * @return отображение с шаблонами частей названий цифр.
     */
    private Map<String, Integer> fillNumbers() {
        Map<String, Integer> nums = new HashMap<>();
        nums.put("од", 1);
        nums.put("ед", 1);
        nums.put("сто", 1);
        nums.put("сотн", 1);
        nums.put("сотк", 1);
        nums.put("дв", 2);
        nums.put("пара", 2);
        nums.put("тр", 3);
        nums.put("чет", 4);
        nums.put("сор", 4);
        nums.put("пят", 5);
        nums.put("шест", 6);
        nums.put("сем", 7);
        nums.put("вос", 8);
        nums.put("дев", 9);
        nums.put("нол", 0);
        nums.put("нул", 0);
        return nums;
    }

    /**
     * Метод заполняет отображение с условиями,
     * определяющими числовые разряды.
     *
     * @return отображение с условиями, определяющими числовые разряды.
     */
    private Map<String, Predicate<String>> fillPredicates() {
        Map<String, Predicate<String>> predicates = new HashMap<>();
        predicates.put("tens", word -> word.contains("десят") || word.contains("дцат") || word.equals("сорок"));
        predicates.put("hundreds", word -> word.contains("сот") || word.contains("сти")
                || word.contains("сто") || word.contains("ста"));
        predicates.put("thousands", word -> word.contains("тыс"));
        predicates.put("millions", word -> word.contains("мил"));
        predicates.put("digits", word -> word.matches("[а-яё]+(цы|ц|ки|ок|ля|лей|ек)") && !word.equals("сорок"));
        predicates.put("one digit", word -> predicates.entrySet().stream()
                .noneMatch(e -> !e.getKey().equals("one digit") && e.getValue().test(word)));
        return predicates;
    }

    /**
     * Метод возвращает отображение с шаблонами частей названий цифр.
     *
     * @return отображение с шаблонами частей названий цифр.
     */
    public Map<String, Integer> getNumbers() {
        return numbers;
    }

    /**
     * Метод возвращает отображение с условиями,
     * определяющими числовые разряды.
     *
     * @return отображение с условиями, определяющими числовые разряды.
     */
    public Map<String, Predicate<String>> getTerms() {
        return terms;
    }
}