package com.javarush.games.moonlander;

import com.javarush.engine.cell.*;

public class MoonLanderGame extends Game {
    // задаем ширину игрового поля
    public static final int WIDTH = 64;

    // задаем высоту игрового поля
    public static final int HEIGHT = 64;

    // задаем переменую для рокеты игрока
    private Rocket rocket;

    // задаем переменную для ланшафта посадочной площадки
    private GameObject landscape;

    // поля флаги для управления ракетой
    private boolean isUpPressed;
    private boolean isLeftPressed;
    private boolean isRightPressed;

    // переменая отвечает за признак остановки инры
    private boolean isGameStopped;

    // создадим посадочную платформу
    private GameObject platform;

    // счетчик игровых очков
    private int score;

    // переопределяем родительский метод инициализации игры
    @Override
    public void initialize() {
        // инициализируем игровое поле заданными в константах размерами
        setScreenSize(WIDTH, HEIGHT);
        // скрываем сетку игрового поля
        showGrid(false);
        // вызываем метод создания игры
        createGame();
    }

    // метод создает игру
    private void createGame() {
        // создаем игровые объекты
        createGameObjects();

        //инициализируем поля флагов нажатий кнопок движения
        isUpPressed = false;
        isLeftPressed = false;
        isRightPressed = false;

        // инициализируем переменную признака остановки игры
        isGameStopped = false;

        // задаем скорость игры
        setTurnTimer(50);

        // задаем начальное количество игровых очков
        score = 1000;

        // вызываем метод отрисовки сцены
        drawScene();
    }

    // метод отрисовывает игровое поле
    private void drawScene() {
        // для каждой ячейки сцены нужно задать цвет
        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                setCellColor(x, y, Color.BLACK);
            }
        }

        // отрисовываем ланшафт
        landscape.draw(this);

        // отрисовываем рокету игрока
        rocket.draw(this);
    }

    // метод для создания игровых объектов
    private void createGameObjects() {
        // инициализируем переменную рокеты игрока и высталвяем его в верхней части экрана по центру
        rocket = new Rocket(WIDTH / 2, 0);

        // инициализируем посадочную платформу
        platform = new GameObject(23, MoonLanderGame.HEIGHT - 1, ShapeMatrix.PLATFORM);

        // инициализируем и задаем форму ланшафта
        landscape = new GameObject(0, 25, ShapeMatrix.LANDSCAPE);
    }

    // переопределяем родительский метод пошагового действия
    @Override
    public void onTurn(int step) {
        // двигаем рокету игрока
        rocket.move(isUpPressed, isLeftPressed, isRightPressed);

        // проверяем коллизию ракеты и ланшафта
        check();

        // уменьшаем количество очков каждый шаг игры
        if (score > 0)
        {
            score--;
        }

        // устанавливаем количество очков на экран
        setScore(score);

        // перерисовываем сцену
        drawScene();
    }

    // переопределяем родительский метод установки цвета для ячейки по заданным координатам
    @Override
    public void setCellColor(int x, int y, Color color) {
        // метод должен выполняться только если координаты в пределах игрового поля
        if (x > 0 && x < WIDTH && y > 0 && y < HEIGHT) {
            super.setCellColor(x, y, color);
        }
    }

    // переопределяем метод отвечающий за действия при нажатии кнопку управления
    @Override
    public void onKeyPress(Key key) {
        // если нажата кнопка вверх взводим флаг движения вверх в истину
        if (key == Key.UP) {
            isUpPressed = true;
        }

        // если нажата кнопка влево взводим флаг движения влево в истину, а движения вправо в ложь
        if (key == Key.LEFT) {
            isLeftPressed = true;
            isRightPressed = false;
        }

        // если нажата кнопка вправо взводим флаг движения влево в ложь, а движения вправо в истину
        if (key == Key.RIGHT) {
            isRightPressed = true;
            isLeftPressed = false;
        }

        // если нажата кнопка пробел и игра остановлена нужно перезапустить игру
        if (key == Key.SPACE && isGameStopped)
        {
            createGame();
        }
    }

    // переопределяем метод отвечающий за действия при отпускании кнопки управления
    @Override
    public void onKeyReleased(Key key) {
        // если кнопка вверх отпущена то возвращаем флагу движения вверх значение ложь
        if (key == Key.UP) {
            isUpPressed = false;
        }
        // если кнопка влево отпущена то возвращаем флагу движения влево значение ложь
        if (key == Key.LEFT) {
            isLeftPressed = false;
        }
        // если кнопка вправо отпущена то возвращаем флагу движения вправо ложь
        if (key == Key.RIGHT) {
            isRightPressed = false;
        }
    }

    // метод проверяет пересечение рокеты и ландшафта
    private void check() {
        // игра проиграна если рокета пересекается с ландшафтом и не пересекается с посадочной платформой и остановлена
        if (rocket.isCollision(landscape) && !(rocket.isCollision(platform) && rocket.isStopped()))
        {
            gameOver();
        }

        // если рокета пересекается с посадочной платформой и остановлена - тогда игра выиграна
        if (rocket.isCollision(platform) && rocket.isStopped())
        {
            win();
        }
    }

    // метод отвечает за выигрыш
    private void win() {
        // вызываем метод приземления
        rocket.land();

        // останавливаем игру
        isGameStopped = true;

        // выводим сообщение о победе
        showMessageDialog(Color.NONE, "Посадка успешная", Color.GREEN, 25);

        // останавливаем таймер шагов
        stopTurnTimer();
    }

    // метод отвечает за проигрыш
    private void gameOver()
    {
        // вызываем метод крушения
        rocket.crash();

        // останавливаем игру
        isGameStopped = true;

        // выводим сообщение о крушении
        showMessageDialog(Color.NONE, "Ты разбился", Color.RED, 25);

        // сбрасываем в 0 количество очков
        score = 0;
        
        // останавливаем таймер шагов
        stopTurnTimer();
    }
}
