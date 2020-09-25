package ru.job4j.numbers;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * LogicTest.
 *
 * @author Oxana Menushina (oxsm@mail.ru).
 * @version $Id$
 * @since 0.1
 */
public class LogicTest {

    /**
     * Экземпляр класса, отвечающего за логику преобразования
     * строкового представления чисел в цифры.
     */
    private ILogic logic = new Logic(new NumberTemplate());

    @Test
    public void when5550044ThenReturn5550044() {
        assertThat(this.logic.convert("три пятерки два нуля сорок четыре"), is("5550044"));
    }

    @Test
    public void when9999999999ThenReturn9999999999() {
        assertThat(this.logic.convert("девять девяток"), is("999999999"));
    }

    @Test
    public void when11231ThenReturn11231() {
        assertThat(this.logic.convert("один однущка двушка трешка единица"), is("11231"));
    }

    @Test
    public void when100100ThenReturn100100() {
        assertThat(this.logic.convert("сотка сотня"), is("100100"));
    }

    @Test
    public void when1010111210ThenReturn1010111210() {
        assertThat(this.logic.convert("десять десятка одиннадцать двенадцать десять"), is("1010111210"));
    }

    @Test
    public void when10002100ThenReturn10002100() {
        assertThat(this.logic.convert("тысяча две тысячи сто"), is("10002100"));
    }

    @Test
    public void when1234567ThenReturn1234567() {
        assertThat(this.logic.convert("один миллион двести тридцать четыре тысячи пятьсот шестьдесят семь"),
                is("1234567"));
    }

    @Test
    public void when20305000ThenReturn20305000() {
        assertThat(this.logic.convert("двести три тысячи пятьдесят два нуля"), is("20305000"));
    }

    @Test
    public void when10020ThenReturn20305000() {
        assertThat(this.logic.convert("сто двадцать пять единиц восемьдесят"), is("1201111180"));
    }

    @Test
    public void when1050080ThenReturn1050080() {
        assertThat(this.logic.convert("миллион пятьдесят тысяч восемьдесят"), is("1050080"));
    }

    @Test
    public void when1001010100ThenReturn1001010100() {
        assertThat(this.logic.convert("сто миллион десять тысяч сто"), is("1001010100"));
    }

    @Test
    public void when1900120444ThenReturn1900120444() {
        assertThat(this.logic.convert("один миллион девятьсот тысяч сто двадцать три четверки"),
                is("1900120444"));
    }

    @Test
    public void when1001050080ThenReturn1001050080() {
        assertThat(this.logic.convert("сто миллион пятьдесят тысяч восемьдесят"), is("1001050080"));
    }

    @Test
    public void when105008050ThenReturn105008050() {
        assertThat(this.logic.convert("миллион пятьдесят тысяч восемьдесят пятьдесят"), is("105008050"));
    }

    @Test
    public void when1050080100ThenReturn1050080100() {
        assertThat(this.logic.convert("миллион пятьдесят тысяч восемьдесят сто"), is("1050080100"));
    }

    @Test
    public void when153200083ThenReturn153200083() {
        assertThat(this.logic.convert("пятнадцать триста двадцать два ноля восьмерка тройка"), is("153200083"));
    }

    @Test
    public void when2000010242ThenReturn2000010242() {
        assertThat(this.logic.convert("двадцать тысяч тысяча двадцать четыре два"), is("2000010242"));
    }

    @Test
    public void when1001000222ThenReturn1001000222() {
        assertThat(this.logic.convert("сто тысяча три двойки"), is("1001000222"));
    }

    @Test
    public void when51000100ThenReturn51000100() {
        assertThat(this.logic.convert("пять тысяча сотня"), is("51000100"));
    }
}