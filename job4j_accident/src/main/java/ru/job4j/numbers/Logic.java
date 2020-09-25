package ru.job4j.numbers;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * Logic.
 * Класс содержит логику преобразования прописных чисел в цифры.
 *
 * @author Oxana Menushina (oxsm@mail.ru).
 * @version $Id$
 * @since 0.1
 */
public class Logic implements ILogic {

    /**
     * Массив для цифр.
     */
    private long[] result;

    /**
     * Индекс массива цифр.
     */
    private int digitIndex;

    /**
     * Массив слов, введенных пользователем.
     */
    private String[] words;

    /**
     * Индекс массива слов.
     */
    private int wordIndex;

    /**
     * Шаблоны частей названий чисел.
     */
    private Map<String, Integer> numbers;

    /**
     * Условия, определяющие числовые разряды.
     */
    private Map<String, Predicate<String>> terms;

    /**
     * Операции преобразования слов в цифры.
     */
    private Map<Predicate<String>, Consumer<String>> operations;

    /**
     * Текущий разряд чисел.
     */
    private int currentDigitPlace;

    /**
     * Конструктор.
     *
     * @param numberTemplate - объект,
     * содержащий числовые шабллоны.
     */
    public Logic(NumberTemplate numberTemplate) {
        this.numbers = numberTemplate.getNumbers();
        this.terms = numberTemplate.getTerms();
        this.operations = this.fillOperations();
    }

    /**
     * Метод устанавливает параметры.
     *
     * @param string - строка, введенная пользователем.
     */
    private void setParameters(String string) {
        this.result = new long[10];
        this.words = string.toLowerCase().split(" ");
        this.wordIndex = this.words.length - 1;
        this.digitIndex = 9;
        this.currentDigitPlace = 0;
    }

    /**
     * Метод принимает строку прописных чисел
     * и возвращает преобразованный в цифры номер.
     *
     * @param string - строка из прописных чисел.
     * @return преобразованный в цифры номер.
     */
    public String convert(String string) throws IncorrectDataException {
        this.setParameters(string);
        while (this.wordIndex >= 0) {
            if (digitIndex < 0) {
                throw new IncorrectDataException();
            }
            this.operations.entrySet().stream().filter(e -> e.getKey().test(this.words[this.wordIndex])).findFirst()
                    .get().getValue().accept(this.words[this.wordIndex]);
        }
        return this.getResult();
    }

    /**
     * Метод заполняет операции преобразования слов в цифры.
     *
     * @return операции преобразования слов в цифры.
     */
    private Map<Predicate<String>, Consumer<String>> fillOperations() {
        Map<Predicate<String>, Consumer<String>> operations = new HashMap<>();
        operations.put(this.terms.get("tens"), this::tensOperation);
        operations.put(this.terms.get("hundreds"), this::hundredsOperation);
        operations.put(this.terms.get("thousands"), this::thousandOperation);
        operations.put(this.terms.get("millions"), this::mlnOperation);
        operations.put(this.terms.get("digits"), this::numberOfDigitsOperation);
        operations.put(this.terms.get("one digit"), this::oneDigitOperation);
        return operations;
    }

    /**
     * Метод преобразовывает одноразрядные числа в цифры.
     *
     * @param word - слово для преобразования в цифру.
     */
    private void oneDigitOperation(String word) {
        Map.Entry<String, Integer> current = this.getNumbersEntry(word);
        if (current != null) {
            this.result[this.digitIndex--] = current.getValue();
            boolean isOne = this.wordIndex - 1 < 0 || "одна".equals(this.words[this.wordIndex - 1]);
            this.wordIndex = isOne ? this.wordIndex - 2 : this.wordIndex - 1;
            this.currentDigitPlace = isOne || word.matches("[а-яё]+(ка|ца)") ? 0
                    : this.currentDigitPlace >= 3 ? 4 : 1;
        }
    }

    /**
     * Метод преобразовывает повторяющиеся цифры.
     *
     * @param word - слово для преобразования в цифру.
     */
    private void numberOfDigitsOperation(String word) {
        Map.Entry<String, Integer> current = this.getNumbersEntry(word);
        Map.Entry<String, Integer> previous = this.getNumbersEntry(this.words[this.wordIndex - 1]);
        if (current != null && previous != null) {
            for (int i = 0; i < (this.words[this.wordIndex - 1].equals("десять") ? 10 : previous.getValue())
                    && this.digitIndex >= 0; i++) {
                this.result[this.digitIndex--] = current.getValue();
            }
            this.wordIndex -= 2;
            this.currentDigitPlace = 0;
        }
    }

    /**
     * Метод преобразовывает десятки в цифры.
     *
     * @param word - слово для преобразования в цифру.
     */
    private void tensOperation(String word) {
        Map.Entry<String, Integer> current = this.getNumbersEntry(word);
        if (current != null && this.digitIndex > 0) {
            this.tensOperationWithNotTen(word, current);
        } else if (this.digitIndex > 0) {
            this.result[this.digitIndex--] = 0;
            this.result[this.digitIndex--] = 1;
        }
        this.currentDigitPlace = this.currentDigitPlace >= 3 ? 5 : 2;
        this.wordIndex--;
    }

    /**
     * Метод преобразовывает десятки, кроме 10, в цифры.
     *
     * @param word - слово для преобразования в цифру.
     * @param current - запись части названия числа.
     */
    private void tensOperationWithNotTen(String word,  Map.Entry<String, Integer> current) {
        if (word.contains("над")) {
            this.result[this.digitIndex--] = current.getValue();
            this.result[this.digitIndex--] = 1;
        } else {
            if (this.currentDigitPlace % 3 != 1) {
                this.result[this.digitIndex--] = 0;
            }
            this.result[this.digitIndex--] = current.getValue();
        }
    }

    /**
     * Метод преобразовывает сотни в цифры.
     *
     * @param word - слово для преобразования в цифру.
     */
    private void hundredsOperation(String word) {
        Map.Entry<String, Integer> current = this.getNumbersEntry(word);
        if (current != null) {
            this.currentDigitPlace = this.currentDigitPlace >= 6 ? 0 : this.currentDigitPlace;
            for (int i = 0; i < 3 - this.currentDigitPlace % 3 - 1 && this.digitIndex >= 0; i++) {
                this.result[this.digitIndex--] = 0;
            }
            if (this.digitIndex >= 0) {
                this.result[this.digitIndex--] = current.getValue();
            }
        }
        this.currentDigitPlace = this.currentDigitPlace >= 3 ? 6 : word.matches("[а-яё]+(ня|ка)")
                || this.wordIndex - 1 > 0 && (this.terms.get("hundreds").test(this.words[this.wordIndex - 1])
                || this.terms.get("tens").test(this.words[this.wordIndex - 1])) ? 0 : 3;
        this.wordIndex--;
    }

    /**
     * Метод преобразовывает тысячи в цифры.
     *
     * @param word - слово для преобразования в цифру.
     */
    private void thousandOperation(String word) {
        for (int i = 0; i < 4 - (this.currentDigitPlace >= 4 ? 0 : this.currentDigitPlace) - 1 && this.digitIndex >= 0;
             i++) {
            this.result[this.digitIndex--] = 0;
        }
        this.currentDigitPlace = 3;
        boolean isOne = this.wordIndex - 1 < 0 || "одна".equals(this.words[this.wordIndex - 1]);
        if (word.endsWith("а") && (this.wordIndex == 0
                || (isOne && (this.wordIndex - 1 == 0 || this.checkCondition(this.words[this.wordIndex - 2])))
                || this.terms.values().stream().anyMatch(p -> p.test(this.words[this.wordIndex - 1])))
                && this.digitIndex >= 0) {
            this.result[this.digitIndex--] = 1;
            this.currentDigitPlace = 0;
        }
        this.wordIndex = isOne ? this.wordIndex - 2 : this.wordIndex - 1;
    }

    /**
     * Метод преобразовывает миллион в цифры.
     *
     * @param word - слово для преобразования в цифру.
     */
    private void mlnOperation(String word) {
        for (int i = 0; i < 7 - this.currentDigitPlace - 1 && this.digitIndex >= 0; i++) {
            this.result[this.digitIndex--] = 0;
        }
        if (this.digitIndex >= 0) {
            this.result[this.digitIndex--] = 1;
            this.currentDigitPlace = 0;
            this.wordIndex = this.wordIndex > 0 && this.words[this.wordIndex - 1].equals("один")
                    ? this.wordIndex - 2 : this.wordIndex - 1;
        }
    }

    /**
     * Метод проверяет условия, определяющие числовые разряды.
     *
     * @param previous - предыдущее слово.
     * @return true - слово удовлетворяет всем условиям.
     */
    private boolean checkCondition(String previous) {
        return this.terms.get("digits").test(previous) || this.terms.get("thousands").test(previous)
                || this.terms.get("one digit").test(previous)
                || previous.equals("десять") || previous.contains("над");
    }

    /**
     * Метод возвращает запись части названия числа,
     * удовлетворяющую условию.
     *
     * @param word - слово.
     * @return запись части названия числа.
     */
    private Map.Entry<String, Integer> getNumbersEntry(String word) {
        return this.numbers.entrySet().stream().filter(e -> word.contains(e.getKey())).findFirst().orElse(null);
    }

    /**
     * Метод возвращает результат преобразования
     * в форме строки цифр.
     *
     * @return строка цифр.
     */
    private String getResult() {
        StringBuilder sb = new StringBuilder();
        Arrays.stream(this.result).forEach(sb::append);
        return this.digitIndex < 0 ? sb.toString() : sb.toString().substring(10 - (9 - this.digitIndex));
    }
}