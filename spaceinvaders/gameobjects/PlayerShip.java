package com.javarush.games.spaceinvaders.gameobjects;

import com.javarush.games.spaceinvaders.Direction;
import com.javarush.games.spaceinvaders.ShapeMatrix;
import com.javarush.games.spaceinvaders.SpaceInvadersGame;

import java.util.List;

public class PlayerShip extends Ship {
    // создадим и расположим корабль игрока внизу экрана по центру
    public PlayerShip()
    {
        super(SpaceInvadersGame.WIDTH / 2.0, SpaceInvadersGame.HEIGHT - ShapeMatrix.PLAYER.length - 1);

        // задаем форму корабля по шаблону
        setStaticView(ShapeMatrix.PLAYER);
    }

    //заводим направление движения корабля игрока, поскольку он может двигаться влево или вправо или вообще не двигаться
    // для стоянки на месте будем использовать направления вверх
    private Direction direction = Direction.UP;

    // метод устанавливает движение корабля игрока
    public void setDirection(Direction newDirection)
    {
        // если новое направление не вниз
        if (!newDirection.equals(Direction.DOWN))
        {
            // устанавливаем новое направление
            direction = newDirection;
        }
    }

    // метод возвращает направление движения игрока
    public Direction getDirection() {
        return direction;
    }

    // переопределяем метод смерти игрока
    @Override
    public void kill() {

        // если игрок не жив (мертв) то метод ничего делать не должен
        if (isAlive)
        {
            // переводим флаг в неживой
            isAlive = false;
            // передаем в метод анимации слайды взрыва корабля
            super.setAnimatedView(false, ShapeMatrix.KILL_PLAYER_ANIMATION_FIRST, ShapeMatrix.KILL_PLAYER_ANIMATION_SECOND, ShapeMatrix.KILL_PLAYER_ANIMATION_THIRD, ShapeMatrix.DEAD_PLAYER);
        }
    }

    // метод проверяет попадания пуль в цель
    public void verifyHit(List<Bullet> bullets)
    {
        // метод должен работать только если в переданном списке пуль есть пули
        if (bullets.size() > 0)
        {
            // работать всё должно только при условии что игрок жив
            if (this.isAlive)
            {
                for (Bullet bullet : bullets)
                {
                    // проверка происходит только с живыми пулями
                    if (bullet.isAlive) {
                        // проверяем коллизию пули и огрока встроенным методом в класс GameObject
                        if (this.isCollision(bullet)) {
                            // если произошло попадание пули в игрока
                            // убиваем игрока
                            this.kill();

                            // убиваем эту пулю
                            bullet.kill();
                        }
                    }
                }
            }
        }
    }

    // метод отвечающий за движение корабля
    public void move()
    {
        // метод ничего не должен делать если игрок мертв
        if (isAlive)
        {
            // если установлено движение влево то уменьшаем координату х на 1
            if (direction.equals(Direction.LEFT))
            {
                x--;
            }

            // если установлено движение вправо то увеличиваем координату х на 1
            if (direction.equals(Direction.RIGHT))
            {
                x++;
            }

            // если значение х стало больше чем сумма х и ширина корабля
            // устанавливаем х в значение ширина экрана минус ширина корабля
            if (SpaceInvadersGame.WIDTH < (x + width))
            {
                x = SpaceInvadersGame.WIDTH - width;
            }

            // если значение х стало меньше 0 то устанавливаем его в 0
            if (x < 0)
            {
                x = 0;
            }
        }
    }

    // метод отвечающий за выстрелы игрока
    @Override
    public Bullet fire() {
        if (!isAlive)
        {
            // если игрок мертв возвращаем null
            return null;
        }
        else
        {
            // возвращаем пулю с координатами от носа корабля в направлении вверх
            return new Bullet(x + 2, y - ShapeMatrix.BULLET.length, Direction.UP);
        }
    }

    // метод выигрыша игрока
    public void win()
    {
        // задаем форма корабля при победе игрока
        setStaticView(ShapeMatrix.WIN_PLAYER);
    }
}
