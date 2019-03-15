package com.javarush.games.spaceinvaders.gameobjects;

import com.javarush.games.spaceinvaders.Direction;
import com.javarush.games.spaceinvaders.ShapeMatrix;

public class Boss extends EnemyShip {
    private int frameCount = 0;

    // задаем стоимость в очках босса
    public int score = 100;

    // конструктор создает босса и задает его анимацию
    public Boss(double x, double y) {
        super(x, y);
        setAnimatedView(true, ShapeMatrix.BOSS_ANIMATION_FIRST, ShapeMatrix.BOSS_ANIMATION_SECOND);
    }

    // переопределяем родительский метод смены кадра анимации
    @Override
    public void nextFrame() {
        frameCount++;

        // если босс неживой или счетчик анимации нацело делится на 10 вызываем метод смены кадра суперкласса
        if (!isAlive || frameCount % 10 == 0)
        {
            super.nextFrame();
        }
    }

    // переопределим метод ведения огня родительского класса
    @Override
    public Bullet fire() {
        // если босс неживой - возвращаем null
        if (!isAlive)
        {
            return null;
        }
        else
        {
            // босс будет стрелять из разных пушек в зависимости от кадра анимации
            // при первом кадре анимации босс стреляет правой пушкой
            if (matrix == ShapeMatrix.BOSS_ANIMATION_FIRST)
            {
                return new Bullet(x + 6, y + height, Direction.DOWN);
            }
            else
            {
                // при остальных кадрах левой
                return new Bullet(x, y + height, Direction.DOWN);
            }
        }
    }

    // переопределяем метод убийства родительского класса
    @Override
    public void kill() {
        // метод будет выполняться только если босс жив
        if (isAlive)
        {
            // устанавливаем флаг босса в неживой
            isAlive = false;

            // запускаем анимацию уничтожения по переданным слайдам
            setAnimatedView(false, ShapeMatrix.KILL_BOSS_ANIMATION_FIRST, ShapeMatrix.KILL_BOSS_ANIMATION_SECOND, ShapeMatrix.KILL_BOSS_ANIMATION_THIRD);
        }
    }
}
