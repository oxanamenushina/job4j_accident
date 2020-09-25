package ru.job4j.numbers;

/**
 * StartConverter.
 * Запускает программу.
 *
 * @author Oxana Menushina (oxsm@mail.ru).
 * @version $Id$
 * @since 0.1
 */
public class StartConverter {

    /**
     * Метод запускает консольную программу.
     *
     * @param args - аргументы.
     */
    public static void main(String[] args) {
        CheckInput checkInput = new CheckInput(new NumberTemplate());
        Input input = new ValidateInput(new ConsoleUI(checkInput));
        ILogic logic = new Logic(new NumberTemplate());
        NumberConverter numberConverter = new NumberConverter(input, System.out::println, logic);
        numberConverter.init();
    }
}