package ru.job4j.numbers;

import java.util.function.Consumer;

/**
 * NumberConverter.
 * Класс, в котором запускается главный цикл.
 *
 * @author Oxana Menushina (oxsm@mail.ru).
 * @version $Id$
 * @since 0.1
 */
public class NumberConverter {

    /**
     * Экземпляр класса, получающий ввод пользователя.
     */
    private final Input input;

    /**
     * Объект для вывода результата.
     */
    private final Consumer<String> output;

    /**
     * Экземпляр класса, отвечающего за логику преобразования
     * строкового представления чисел в цифры.
     */
    private final ILogic logic;

    /**
     * Конструктор.
     *
     * @param input - экземпляр класса, получающий ввод пользователя.
     * @param output - объект для вывода результата.
     * @param logic - экземпляр класса, отвечающего за логику преобразования
     * строкового представления чисел в цифры.
     */
    public NumberConverter(Input input, Consumer<String> output, ILogic logic) {
        this.input = input;
        this.output = output;
        this.logic = logic;
    }

    /**
     * Метод запускает главный цикл.
     */
    public void init() {
        String text = new StringBuilder().append("Введите номер прописью.").append(System.lineSeparator())
                .append("Максимальное количеством цифр в номере - 10.").append(System.lineSeparator())
                .append("Прописные числа, составляющие номер,").append(System.lineSeparator())
                .append("должны быть в диапазоне от 0 до 1000000 (включительно).")
                .append(System.lineSeparator()).append("Введите \"exit\" для выхода.")
                .append(System.lineSeparator()).toString();
        boolean isContinue;
        do {
            String answer = this.input.ask(text);
            isContinue = !answer.equalsIgnoreCase("exit");
            if (isContinue) {
                String result;
                try {
                    result = this.logic.convert(answer);
                    this.output.accept(String.format("Result: %s%s", result, System.lineSeparator()));
                } catch (IncorrectDataException e) {
                    System.out.println("Количество цифр превышает максимально допустимое." + System.lineSeparator());
                }

            }
        } while (isContinue);
    }
}