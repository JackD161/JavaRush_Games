package com.javarush.games.spaceinvaders.gameobjects;

import com.javarush.games.spaceinvaders.Direction;
import com.javarush.games.spaceinvaders.ShapeMatrix;

public class EnemyShip extends Ship {
    public int score = 15;

    public EnemyShip(double x, double y) {
        super(x, y);
        setStaticView(ShapeMatrix.ENEMY);
    }

    public void move(Direction direction, double speed)
    {
        // если движение идет вправо то увеличиваем х на переданную скорость
        if (direction.equals(Direction.RIGHT))
        {
            x += speed;
        }
        // если движение идет влево то уменьшаем x на переданную скорость
        else if (direction.equals(Direction.LEFT))
        {
            x -= speed;
        }
        // если движение идет вниз то увеличиваем y на 2, движение вниз идет с постоянной скоростью
        else if (direction.equals(Direction.DOWN))
        {
            y += 2;
        }
    }

    // переопределяем родительский метод ведения огня, который возвращает пулю с нужными, по отношению к рораблю, начальными координатами
    @Override
    public Bullet fire() {
        return new Bullet(x + 1, y + height, Direction.DOWN);
    }

    // переопределим родительский метод смерти корабля
    @Override
    public void kill() {
        // метод ничего не будет делать если корабль не живой
        if (isAlive)
        {
            // ставим флаг уничтожения корабля
            isAlive = false;

            // передаем слайды анимации
            setAnimatedView(false, ShapeMatrix.KILL_ENEMY_ANIMATION_FIRST, ShapeMatrix.KILL_ENEMY_ANIMATION_SECOND, ShapeMatrix.KILL_ENEMY_ANIMATION_THIRD);
        }
    }
}
