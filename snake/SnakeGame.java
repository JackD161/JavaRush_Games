package com.javarush.games.snake;

import com.javarush.engine.cell.*;

public class SnakeGame extends Game{
    // задаем ширину и высоту игрового поля и длинны змейки для выигрыша
    public static final int WIDTH = 15;
    public static final int HEIGHT = 15;
    private static final int GOAL = 28;
    private Snake snake;
    private int turnDelay;
    private Apple apple;
    private boolean isGameStopped;
    private int score;

    @Override
    // инициализируем игровое поле и создаем игру
    public void initialize()
    {
        setScreenSize(WIDTH, HEIGHT);
        createGame();
    }

    // метод создает игру и инициализирует всче нужные для создания переменные
    private void createGame()
    {
        // создаем и устанавливаем змейку в центре игрового экрана
        snake = new Snake(WIDTH / 2, HEIGHT / 2);
        // создаем яблоко
        createNewApple();
        isGameStopped = false;
        // устанавливаем начальное количество очков
        setScore(score = 0);
        drawScene();
        // задаем начальную скорость
        turnDelay = 300;
        setTurnTimer(turnDelay);
    }

    // метод отрисовывает игровую сцену
    private void drawScene()
    {
        // отрисовываем игровое поле
        for (int w = 0; w < WIDTH; w++)
        {
            for (int h = 0; h < HEIGHT; h++)
            {
                // устанавливаем каждой ячейке игрового поля цвет и значение (пустое для пустой ячейки)
                setCellValueEx(w, h, Color.DARKGREEN, "");
            }
        }
        // отрисовываем змейку
        snake.draw(this);
        // отрисовываем яблоко
        apple.draw(this);
    }

    // метод пошагового движения в игре
    @Override
    public void onTurn(int step)
    {
        // вызываем метод движения змейки
        snake.move(apple);
        // если яблоко сьедено - увеличиваем игровые очки, ускоряем змейку и создаем новое яблоко
        if(!apple.isAlive)
        {
            setScore(score += 5);
            setTurnTimer(turnDelay -= 10);
            createNewApple();
        }
        // если змейка убилась - вызываем метод проигрыша, если змейка достигла нужной длинны - вызываем метод выигрыша
        if(!snake.isAlive)
        {
            gameOver();
        }
        if (GOAL < snake.getLength())
        {
            win();
        }
        drawScene();
    }

    // метод проверяет нажатия клавиш управления
    @Override
    public void onKeyPress(Key key) {
        if (key == Key.LEFT)
        {
            snake.setDirection(Direction.LEFT);
        }
        else if (key == Key.RIGHT)
        {
            snake.setDirection(Direction.RIGHT);
        }
        else if (key == Key.UP)
        {
            snake.setDirection(Direction.UP);
        }
        else if (key == Key.DOWN)
        {
            snake.setDirection(Direction.DOWN);
        }
        // если игра остановлена то клавишей пробел она перезапускается
        else if (key == Key.SPACE && isGameStopped)
        {
            createGame();
        }
    }

    // метод создает новое яблоко
    private void createNewApple()
    {
        while (true)
        {
            // если яблоко создается на теле змейки происходит пересоздание яблока, в случае удачи выходим из цикла
            if (!snake.checkCollision(apple = new Apple(getRandomNumber(WIDTH), getRandomNumber(HEIGHT))))
            {
                break;
            }
        }

    }

    // метод проигрыша
    private void gameOver()
    {
        stopTurnTimer();
        isGameStopped = true;
        showMessageDialog(Color.RED, "Game over!", Color.CORAL, 20);
    }

    // метод выигрыша
    private void win()
    {
        stopTurnTimer();
        isGameStopped = true;
        showMessageDialog(Color.SPRINGGREEN, "You win!!!", Color.DEEPPINK, 20);
    }
}
