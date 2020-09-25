package ru.job4j.numbers;

import java.util.Scanner;

/**
 * ConsoleUI.
 * Класс выводит на консоль текст и принимает строку ввода.
 *
 * @author Oxana Menushina (oxsm@mail.ru).
 * @version $Id$
 * @since 0.1
 */
public class ConsoleUI implements Input {

    /**
     * Экземпляр класса Scanner.
     */
    private Scanner scan = new Scanner(System.in);

    /**
     * Проверяет ввод пользователя.
     */
    private CheckInput checkInput;

    /**
     * Конструктор.
     *
     * @param checkInput - объект, проверяющий ввод пользователя.
     */
    public ConsoleUI(CheckInput checkInput) {
        this.checkInput = checkInput;
    }

    /**
     * Метод выводит на консоль текст и принимает строку ввода.
     *
     * @param question - текст, выводимый на консоль.
     * @return строка ввода.
     * @throws IncorrectDataException
     */
    @Override
    public String ask(String question) throws IncorrectDataException {
        System.out.print(question);
        String answer = this.scan.nextLine();
        if (!this.checkInput.check(answer)) {
            throw new IncorrectDataException();
        }
        return answer;
    }
}