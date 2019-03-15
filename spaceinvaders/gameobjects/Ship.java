package com.javarush.games.spaceinvaders.gameobjects;

import com.javarush.engine.cell.Game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Ship extends GameObject {
    // создаем флаг для состояния: "живой" или "неживой" и установим изначально в живой
    public boolean isAlive = true;

    // создадим флаг для безконечной анимации, по умолчанию он опущен
    private boolean loopAnimation = false;

    // список содержит кадры анимации крушения корабля
    private List<int[][]> frames;

    private int frameIndex;

    // базовый конструктор, создающий корабль и определяющий его начальные координаты
    public Ship(double x, double y) {
        super(x, y);
    }

    // конструктор для задания формы корабля
    public void setStaticView(int[][] viewFrame)
    {
        setMatrix(viewFrame);
        frames = new ArrayList<int[][]>();
        frames.add(viewFrame);
        frameIndex = 0;
    }

    // метод отвечающий за выстрелы корабля
    public Bullet fire()
    {
        return null;
    }

    // метод отвечающий за смерть корабля
    public void kill()
    {
        // переводим флаг в состояние "неживой" для корабля
        isAlive = false;
    }

    // метод отвечает за анимацию, принимает на вход произвольное количество слайдов анимации
    public void setAnimatedView(boolean isLoopAnimation, int[][]...viewFrames)
    {
        this.loopAnimation = isLoopAnimation;
        setMatrix(viewFrames[0]);
        frames = Arrays.asList(viewFrames);

        // устанавливаем номер первого кадра анимации
        frameIndex = 0;
    }

    // метод смены кадров анимации
    public void nextFrame()
    {
        frameIndex++;

        // если индекс больше или равен количеству кадров в списке кадров и флаг безконечной анимации поднят - обнуляем индекс
        if (frameIndex >= frames.size() && loopAnimation)
        {
            frameIndex = 0;
        }

        // смена кадров возможна только если индекс кадра меньше общего количества кадров с списке
        if (frameIndex < frames.size() && loopAnimation) {

            matrix = frames.get(frameIndex);
        }
    }

    @Override
    public void draw(Game game) {
        super.draw(game);
        nextFrame();
    }

    // создадим метод скрывающий корабль
    public boolean isVisible()
    {
        // если корабль не живой или количество слайдов в списке для анимации меньше индекса текущего слайда
        if (!isAlive && frames.size() <= frameIndex)
        {
            // возвращаем лож
            return false;
        }
        else
        {
            return true;
        }
    }
}
