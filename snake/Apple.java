package com.javarush.games.snake;

import com.javarush.engine.cell.*;
// класс яблока, описывает базовые параметры яблока
public class Apple extends GameObject {
    // задаем значение отображаемого яблока в юникоде
    // https://www.charbase.com/1f34e-unicode-red-apple
    private final static String APPLE_SIGN = "\ud83c\udf4e";
    // флаг того что яблоко живое (не сьедено)
    public boolean isAlive = true;

    public void draw(Game game)
    {
        // отрисовка яблока, задаются координаты по x и y, цвет фона клетки размещения, цвет отображения значка и размер
        game.setCellValueEx(x, y, Color.NONE, APPLE_SIGN, Color.RED, 75);
    }

    public Apple(int x, int y) {
        super(x, y);
    }
}
