package ru.job4j.numbers;

/**
 * ValidateInput.
 * Класс производит валидацию ввода пользователя.
 *
 * @author Oxana Menushina (oxsm@mail.ru).
 * @version $Id$
 * @since 0.1
 */
public class ValidateInput implements Input {

    /**
     * Экземпляр класса, получающий ввод пользователя.
     */
    private Input input;

    /**
     * Конструктор.
     *
     * @param input - экземпляр класса, получающий ввод пользователя.
     */
    public ValidateInput(Input input) {
        this.input = input;
    }

    /**
     * Метод производит валидацию ввода пользователя.
     * @param question - текст, выводимый на консоль.
     * @return ввод пользователя.
     */
    @Override
    public String ask(String question) {
        boolean rst = true;
        String answer = "";
        while (rst) {
            try {
                answer = this.input.ask(question);
                rst = false;
            } catch (IncorrectDataException e) {
                System.out.print("Числа введены неверно." + System.lineSeparator());
            }
        }
        return answer;
    }
}