package com.javarush.games.spaceinvaders.gameobjects;

import com.javarush.engine.cell.Game;
import com.javarush.games.spaceinvaders.Direction;
import com.javarush.games.spaceinvaders.ShapeMatrix;
import com.javarush.games.spaceinvaders.SpaceInvadersGame;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class EnemyFleet {
    // заводим константы для определения размеров вражеского флота и скорости
    private static final int ROWS_COUNT = 3;
    private static final int COLUMNS_COUNT = 10;
    private static final int STEP = ShapeMatrix.ENEMY.length + 1;

    // переменная будет хранить вражеские корабли
    private List<EnemyShip> ships;

    // устанавливаем начальное направление движения вправо
    private Direction direction = Direction.RIGHT;

    // метод создает армаду вражеских кораблей
    private void createShips()
    {
        ships = new ArrayList<>();
        for (int y = 0; y < ROWS_COUNT; y++)
        {
            for (int x = 0; x < COLUMNS_COUNT; x++)
            {
                ships.add(new EnemyShip((double)x * STEP, (double)y * STEP + 12));
            }
        }
         // добавляем в список кораблей босса и распологаем его вверху по центру строя кораблей
        ships.add(new Boss(STEP * COLUMNS_COUNT / 2 - ShapeMatrix.BOSS_ANIMATION_FIRST.length / 2 - 1, 5));
    }

    // базовый метод создания вражеского флота
    public EnemyFleet()
    {
        createShips();
    }

    // метод отрисовки вражеского флота в котором каждый корабль из флота отрисовывается
    public void draw(Game game)
    {
        for (EnemyShip ship : ships)
        {
            ship.draw(game);
        }
    }

    // метод возвращает левую границу вражеского флота
    private double getLeftBorder()
    {
        // устанавливаем значение минимального х из перевого корабля флота
        double minX = ships.get(0).x;
        // перебираем все корабли флота и сравниваем их координаты х
        for (Ship ship : ships)
        {
            if (ship.x < minX)
            {
                minX = ship.x;
            }
        }
        // возвращаем минимальную найденную х
        return minX;
    }

    // метод возвращает правую границу вражеского флота
    private double getRightBorder()
    {
        // устанавливаем значение максимального х из перевого корабля флота + ширина игрового поля
        double maxX = ships.get(0).x;
        // перебираем все корабли флота и сравниваем их х
        for (Ship ship : ships)
        {
            if (maxX < ship.x + ship.width)
            {
                maxX = ship.x + ship.width;
            }
        }
        // возвращаем найденный максимум
        return maxX;
    }

    // метод возвращает нижнию границу флота
    public double getBottomBorder()
    {
        // за базу берем верхнию границу игровой области
        double bottomBorder = 0;
        Iterator<EnemyShip> iterator = ships.iterator();
        while(iterator.hasNext())
        {
            Ship ship = iterator.next();
            if (ship.y + ship.height > bottomBorder)
            {
                bottomBorder = ship.y + ship.height;
            }
        }
        return bottomBorder;
    }

    // метод возвращает количество вражеских кораблей во флоте
    public int getShipsCount()
    {
        return ships.size();
    }

    // метод возвращает скорость движения из расчета количества кораблей во флоте но не более 2
    private double getSpeed()
    {
        return 2.0 > (3.0 / ships.size()) ? 3.0 / ships.size() : 2.0;
    }

    // метод движения флота
    public void move()
    {
        // метод движения что то делает только если в списке кораблей есть корабли
        if (ships.size() > 0)
        {
            double speed = getSpeed();
            // если движение текущее влево и флот достиг левого края экрана
            if (direction.equals(Direction.LEFT) && getLeftBorder() < 0)
            {
                // меняем направление движения на противоположное всем кораблям
                direction = Direction.RIGHT;
                for (EnemyShip ship : ships)
                {
                    ship.move(Direction.DOWN, speed);
                }
            }
            // если текущее напраление движения вправо и флот достиг правого края экрана
            else if (direction.equals(Direction.RIGHT) && getRightBorder() > SpaceInvadersGame.WIDTH)
            {
                // меняем движение на противоположное всем кораблям
                direction = Direction.LEFT;
                for (EnemyShip ship : ships)
                {
                    ship.move(Direction.DOWN, speed);
                }
            }
            else
            {
                // иначе просто двигаем все корабли в заданном направлении
                for (EnemyShip ship : ships)
                {
                    ship.move(direction, speed);
                }
            }
        }
    }

    // метод ведения огня вражеским флотом
    public Bullet fire(Game game)
    {
        // если во вражеском флоте нет кораблей - возвращаем null
        if (ships.size() < 1)
        {
            return null;
        }
        else
        {
            // флот будет стрелять с вероятностью COMPLEXITY процентов

            // определяем вероятность выстрела
            int rndFire = game.getRandomNumber(100 / SpaceInvadersGame.COMPLEXITY);
            if (rndFire > 0)
            {
                return null;
            }
            // выбираем случайный корабль и стреляем
            return ships.get(game.getRandomNumber(ships.size())).fire();
        }
    }

    // метод проверяет попадания пуль игрока во вражеские корабли и считает из суммарные очки
    public int verifyHit(List<Bullet> bullets)
    {
        int hits = 0;

        // если пуль нет то возвращаем 0
        if (bullets.size() == 0)
        {
            return 0;
        }
        else {
            // проверяем коллизию каждой пули и каждого корабля
            for (Bullet bullet : bullets) {
                for (EnemyShip ship : ships) {
                    // если попадпние было и этот корабль жив и эта пуля жива
                    if (ship.isCollision(bullet) && ship.isAlive && bullet.isAlive) {
                        // убиваем корабль
                        ship.kill();
                        // добавляем очки убитого корабля в общий счет
                        hits += ship.score;

                        // убиваем пулю
                        bullet.kill();
                    }
                }
            }
        }
        return hits;
    }

    // метод удаляет скрытые корабли из списка кораблей
    public void deleteHiddenShips()
    {
        Iterator<EnemyShip> iterator = ships.iterator();
        while(iterator.hasNext())
        {
            Ship ship = iterator.next();
            if (!ship.isVisible())
            {
                iterator.remove();
            }
        }
    }
}
