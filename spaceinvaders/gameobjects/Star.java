package com.javarush.games.spaceinvaders.gameobjects;

import com.javarush.engine.cell.*;

public class Star extends GameObject {
    // зададим обозначение символа звезд для отрисовки
    private static final String STAR_SIGN = "\u2605";
    public Star (double x, double y)
    {
        super(x, y);
    }

    // метод отрисовки звезд
    public void draw(Game game)
    {
       game.setCellValueEx((int)x, (int)y, Color.NONE, STAR_SIGN, Color.YELLOW, 100);
    }
}
