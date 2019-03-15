package com.javarush.games.spaceinvaders.gameobjects;

import com.javarush.games.spaceinvaders.Direction;
import com.javarush.games.spaceinvaders.ShapeMatrix;

public class Bullet extends GameObject {
    // пули летают только вверх и вниз, поэтому при движении меняется только координита y (dy будем вычитать из y)
    // если переменная примет отрицательное значение - пуля полетит вверх, если положительное - сниз
    private int dy;

    // создаем флаг для состояния: "живой" или "неживой" и установим изначально в живой
    public boolean isAlive = true;

    public Bullet(double x, double y, Direction direction) {
        super(x, y);

        // создаем пулю по матрице форм объектов
        setMatrix(ShapeMatrix.BULLET);

        // инициализируем переменную направления движения
        // если направление передано вверх то пуля полетит вверх
        if (direction.equals(Direction.UP))
        {
            dy = -1;
        }
        // если передано любое другое направление то пуля полетит вниз
        else
        {
            dy = 1;
        }
    }

    // метод отвечает за движение пули
    public void move()
    {
        // меняем координату по y
        // в зависимости от того положительна или отрицательна переменная направления движения в ту сторону пуля полетит
        y += dy;
    }

    // метод отвечает за смерть пули
    public void kill()
    {
        // переводим флаг в состояние "неживой" для пули
        isAlive = false;
    }
}
