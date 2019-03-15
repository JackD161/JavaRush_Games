package com.javarush.games.snake;

import com.javarush.engine.cell.*;

import java.util.ArrayList;
import java.util.List;


public class Snake {
    private int x;
    private int y;
    // список частей змейки
    private List<GameObject> snakeParts;
    // задаем какимми значками будет отображаться голова и тело змейки
    // https://www.charbase.com/block/miscellaneous-symbols-and-pictographs
    private final static String HEAD_SIGN = "\uD83D\uDC7E";
    private final static String BODY_SIGN = "\u26AB";
    // флаг что змейка жива (не убилась о края поля и не укусила себя)
    public boolean isAlive = true;
    // стартовое направление движения
    private Direction direction = Direction.LEFT;

    // метод отрисовывает змею на игровом поле
    public void draw(Game game) {
        // проходим цтклом по всему списку частей змейки
        for (int part = 0; part < snakeParts.size(); part++)
        {
            // первая часть змейки - голова
            if (part == 0)
            {
                // если змейка жива отображаем её коричневой
                if (isAlive) {
                    game.setCellValueEx(snakeParts.get(part).x, snakeParts.get(part).y, Color.NONE, HEAD_SIGN, Color.BROWN, 75);
                }
                // если змейка мертва - красной
                else
                {
                    game.setCellValueEx(snakeParts.get(part).x, snakeParts.get(part).y, Color.NONE, HEAD_SIGN, Color.RED, 75);
                }
            }
            // после головы в начале идет остальное тело змейки
            else
            {
                // если змейка жива отображаем её коричневой
                if (isAlive) {
                    game.setCellValueEx(snakeParts.get(part).x, snakeParts.get(part).y, Color.NONE, BODY_SIGN, Color.BROWN, 75);
                }
                // если змейка мертва - красной
                else
                {
                    game.setCellValueEx(snakeParts.get(part).x, snakeParts.get(part).y, Color.NONE, BODY_SIGN, Color.RED, 75);
                }
            }
        }
    }

    // инициализация змейки
    public Snake(int x, int y)
    {
        this.x = x;
        this.y = y;
        // создаем по переданным координатам голову и добавляем 2 этемента тела
        snakeParts = new ArrayList<>();
        snakeParts.add(new GameObject(x, y));
        snakeParts.add(new GameObject(x + 1, y));
        snakeParts.add(new GameObject(x + 2, y));
    }

    // устанавливаем направление змейки
    public void setDirection(Direction direction)
    {
        // если текущее направление ВВЕРХ и переданное направление НЕ ВНИЗ и координаты по вертикали головы и туловища не совпадают (чтобы голова не начала есть своё туловище)
        if (this.direction == Direction.UP && direction != Direction.DOWN && snakeParts.get(0).y != snakeParts.get(1).y)
        {
            // устанавливаем переданное направление движения
            this.direction = direction;
        }
        // если текущее направление ВНИЗ и переданное направление НЕ ВВЕРХ и координаты по вертикали головы и туловища не совпадают (чтобы голова не начала есть своё туловище)
        else if (this.direction == Direction.DOWN && direction != Direction.UP && snakeParts.get(0).y != snakeParts.get(1).y)
        {
            this.direction = direction;
        }
        // если текущее направление ВЛЕВО и переданное направление НЕ ВПРАВО и координаты по горизонтали головы и туловища не совпадают (чтобы голова не начала есть своё туловище)
        else if (this.direction == Direction.LEFT && direction != Direction.RIGHT && snakeParts.get(0).x != snakeParts.get(1).x)
        {
            this.direction = direction;
        }
        // если текущее направление ВПРАВО и переданное направление НЕ ВЛЕВО и координаты по горизонтали головы и туловища не совпадают (чтобы голова не начала есть своё туловище)
        else if (this.direction == Direction.RIGHT && direction != Direction.LEFT && snakeParts.get(0).x != snakeParts.get(1).x)
        {
            this.direction = direction;
        }
    }

    // метод движения змейки
    public void move(Apple apple)
    {
        // создаем объект новой головы
        GameObject obj = createNewHead();
        // если координаты новой головы выходят за пределы игрового поля
        if (obj.x >= SnakeGame.HEIGHT || obj.x < 0 || obj.y >= SnakeGame.WIDTH || obj.y < 0)
        {
            // устанавливаем флаг змейки в МЕРТВА
            isAlive = false;
        }
        else {
            // проверяем на наткнулась ли голова на туловище
            if (checkCollision(obj)) {
                // если змея укусила себя переводим флаг в МЕРТВА
                isAlive = false;
            } else {
                // сверяем координаты яблока на поле и новой головы змеи
                if (apple.x == obj.x && apple.y == obj.y) {
                    // змея съела яблоко, устанавливаем флаг яблока в состояние СЪЕДЕНО и добавляем в начало списка частей змейки новую голову
                    apple.isAlive = false;
                    snakeParts.add(0, obj);
                } else {
                    // если змея не на что не натолкнулась - добавляем новую голову в начало списка а хвост удаляем
                    snakeParts.add(0, obj);
                    removeTail();
                }
            }
        }
    }

    // создание новой головы змейки
    public GameObject createNewHead()
    {
         int x = snakeParts.get(0).x;
         int y = snakeParts.get(0).y;
         // при движении ВЛЕВО уменьшаем координату головы по горизонтали
       if (direction.equals(Direction.LEFT))
       {
           x--;
       }
       // при движении ВПРАВО увеличиваем координату головы по горизонтали
       else if (direction.equals(Direction.RIGHT))
       {
           x++;
       }
       // при движении ВВЕРХ уменьшаем координату головы по вертикали (адреса игрового поля начинаются с верхнего левого угла)
       else if (direction.equals(Direction.UP))
       {
           y--;
       }
       // при движении ВНИЗ увеличиваем координату головы по вертикали (адреса игрового поля начинаются с верхнего левого угла)
       else if (direction.equals(Direction.DOWN))
       {
           y++;
       }
       // возвращаем новую голову с измененными координатами
       return new GameObject(x, y);
    }

    // удаляем хвост змейки
    public void removeTail()
    {
        snakeParts.remove(snakeParts.size() - 1);
    }

    // метод проверяет коллию змейки с игровыми объектами
    public boolean checkCollision(GameObject object)
    {
        // циклом проверяем все части змейки и проверяем на совпадение их координат с координатами переданного игрового объекта
        for (GameObject parts : snakeParts)
        {
            if (object.x == parts.x && object.y == parts.y)
            {
                return true;
            }
        }
        return false;
    }
    // метод возвращает днанну змейки
    public int getLength()
    {
        return snakeParts.size();
    }
}
