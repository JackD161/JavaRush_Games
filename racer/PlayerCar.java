package com.javarush.games.racer;

import com.javarush.games.racer.road.RoadManager;

public class PlayerCar extends GameObject {
    // переменная задает высоту игровой машинки из высоты матрицы машинки игрока
    private static int playerCarHeight = ShapeMatrix.PLAYER.length;
    // переменная отвечает за скорость
    public int speed = 1;
    // переменная отвечает за направление движения пользовательской машины
    private Direction direction;

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public Direction getDirection() {
        return direction;
    }

    // Вначале машина игрока будет находиться в третьей по счету полосе движения и на одну позицию выше нижнего края игрового поля
    public PlayerCar() {
        super(RacerGame.WIDTH / 2 + 2, RacerGame.HEIGHT - playerCarHeight - 1, ShapeMatrix.PLAYER);
    }

    // метод отвечает за движение игрока
    public void move()
    {
        // если навравление игрока влево то уменьшаем координату х на 1
        if (direction == Direction.LEFT)
        {
            x--;
        }
        // если х станем меньше левого края то устанавливаем х на край дороги
        if (x < RoadManager.LEFT_BORDER)
        {
            x = RoadManager.LEFT_BORDER;
        }
        // если направление игрока вправо то увеличиваем координату х на 1
        if (direction == Direction.RIGHT)
        {
            x++;
        }
        // если х станет больше правого края то устанавливаем х в правый край дороги с учетом ширины машинки
        if (x > RoadManager.RIGHT_BORDER - width)
        {
            x = RoadManager.RIGHT_BORDER - width;
        }
    }

    // метод останавливает игрока
    public void stop()
    {
        // устанавливаем в матрицу игрока форму разбившегося авто
        matrix = ShapeMatrix.PLAYER_DEAD;
    }
}
