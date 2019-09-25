package com.javarush.games.racer;

import com.javarush.engine.cell.*;
import com.javarush.games.racer.road.RoadManager;

public class RacerGame extends Game {
    // зададим константы длинны и ширины игрового поля
    public static final int WIDTH = 64;
    public static final int HEIGHT = 64;
    // в центре поля распологается разделительная полоса, вынесем её в константу
    public static final int CENTER_X = WIDTH / 2;
    // по бокам расположена обочина, её ширина 14 ячеек
    public static final int ROADSIDE_WIDTH = 14;
    // класс отрисовывает дополнительную дорожную разметку
    private RoadMarking roadMarking;
    // переменная для машинки игрока
    private PlayerCar player;
    // создаем менеджер дорожных объектов
    private RoadManager roadManager;
    // создадим флаг остановки игры
    private boolean isGameStopped;
    // поле содержащее финишную линию
    private FinishLine finishLine;
    // переменная содержит число машин для преодоления игроком для выигрыша
    private static final int RACE_GOAL_CARS_COUNT = 40;
    // поле отвечает за прогресс прохождения игры
    private ProgressBar progressBar;
    // поле для подсчета очков игрока
    private int score;

    // метод инициализации игры
    @Override
    public void initialize() {
        // устанавливаем размеры игрового поля
        setScreenSize(WIDTH, HEIGHT);
        // создаем игру
        createGame();
        // скрываем отображение сетки игрового поля
        showGrid(false);
    }

    // метод создает и запускает игру
    private void createGame()
    {
        // опускаем флаг остановленной игры перед началом игры
        isGameStopped = false;
        // инициализируем класс дополнительной дорожной разметки
        roadMarking = new RoadMarking();
        // создаем машинку игрока
        player = new PlayerCar();
        // создаем менеджер дорожных объектов
        roadManager = new RoadManager();
        // создаем финишную линию
        finishLine = new FinishLine();
        // создаем индикатор прогресса игры и передаем в него условие выигрыша для формирования его длинны
        progressBar = new ProgressBar(RACE_GOAL_CARS_COUNT);
        // отрисовываем игровое поле
        drawScene();
        // устанавливаем таймер хода игры
        setTurnTimer(40);
        // устанавливаем начальное количество очков
        score = 3500;
    }

    // метод отрисовывает игровые события на поле
    private void drawScene()
    {
        // рисуем игровое поле
        drawField();
        // отрисовываем дорожные объекты
        roadManager.draw(this);
        // отрисовываем динамические объектиы поверх отрисованного поля
        roadMarking.draw(this);
        // отрисовываем машинку игрока
        player.draw(this);
        // отрисовываем финишную линию, изначально она будет за шраницами экрана
        finishLine.draw(this);
        // отрисовываем индикатор прогресса игры
        progressBar.draw(this);
    }

    // метод отрисовывает игровое поле
    private void drawField()
    {
        // нановим разметку на игровое поле
        for (int y = 0; y < HEIGHT; y++)
        {
            for (int x = 0; x < WIDTH; x++)
            {
                // отрисовываем обочину по обоим краям игрового поля
                if (x < ROADSIDE_WIDTH || x >= WIDTH - ROADSIDE_WIDTH)
                {
                    setCellColor(x, y, Color.CHOCOLATE);
                }
                // отрисовываем дорожное полотно
                if (x >= ROADSIDE_WIDTH && x < WIDTH - ROADSIDE_WIDTH && x != CENTER_X)
                {
                    setCellColor(x, y, Color.GRAY);
                }
                // отрисовываем разделительную линию
                if (x == CENTER_X)
                {
                    setCellColor(CENTER_X, y, Color.WHITE);
                }
            }
        }
    }

    // переопределим метод окрашивания ячеек для исключения его работы с координатами выходящими за игровое поле
    @Override
    public void setCellColor(int x, int y, Color color) {
        if (x >= 0 && x < WIDTH && y >= 0 && y < HEIGHT ) {
            super.setCellColor(x, y, color);
        }
    }

    // метод двигает все объекты на экране
    private void moveAll()
    {
        // двигаем дорожныу разметку со скоростью игрока
        roadMarking.move(player.speed);
        // двигаем машину игрока
        player.move();
        // двигаем дорожные объекты со скоростью игрока
        roadManager.move(player.speed);
        // двигаем финишную линию со скоростью игрока
        finishLine.move(player.speed);
        // двигаем индикатор прогресса в зависимости от проехавших машин
        progressBar.move(roadManager.getPassedCarsCount());
    }

    // метод отвечает за произведения шага игры
    @Override
    public void onTurn(int step) {
        // с каждым шагом игры уменьшаем количество очков на 5
        score -= 5;
        // устанавливаем количество очком на панель очков
        setScore(score);
        // проверяем на столкновения игрока и препятствий
        if(roadManager.checkCrush(player))
        {
            // если столкновение произошло то вызываем метод конца игры
            gameOver();
            // отрисовываем сцену и больше ничего не делаем
            drawScene();
        }
        else {
            // проверяем количество преодоленных машин с выигрышным количеством
            if(RACE_GOAL_CARS_COUNT <= roadManager.getPassedCarsCount())
            {
                finishLine.show();
            }
            // проверяем не пересек ли игрок финишную линию
            if (finishLine.isCrossed(player))
            {
                // если пересек вызываем метод победы
                win();
                // отрисовываем сцену и больше ничего не делаем
                drawScene();
            }
            else {
                // передвигаем все объекты
                moveAll();
                // создаем новые дорожные объекты
                roadManager.generateNewRoadObjects(this);
                // отрисовываем сцену
                drawScene();
            }
        }
    }

    // метод распознает нажатие клавишь управления машинкой игрока
    @Override
    public void onKeyPress(Key key) {
        // если нажата клавиша влево то задаем направление движения игрока влево
        if (key == Key.LEFT)
        {
            player.setDirection(Direction.LEFT);
        }
        // если нажата клавиша вправо то задаем направление движения игрока вправо
        if (key == Key.RIGHT)
        {
            player.setDirection(Direction.RIGHT);
        }
        // реализация перезапуска игры если она остановлена кнопкой SPACE
        if (key == Key.SPACE && isGameStopped)
        {
            createGame();
        }
        // если нажата кнопка вперед то увеличиваем скорость игрока вдвое
        if (key == Key.UP)
        {
            player.speed = 2;
        }
    }

    // метод распознает когда игрок отпускает кнопки управления
    @Override
    public void onKeyReleased(Key key) {
        // если движение машинки игрока вправо и отпускается кнопка управление вправо то останавливаем движение машинки
        if (key == Key.RIGHT && player.getDirection() == Direction.RIGHT)
        {
            player.setDirection(Direction.NONE);
        }
        // если движение машинки влево и отпускается кнопка управления влево то останавливаем движение машинки
        if (key == Key.LEFT && player.getDirection() == Direction.LEFT)
        {
            player.setDirection(Direction.NONE);
        }
        // если кнопка газа отпущена то возвращаем скорость игрока к первоначальной
        if (key == Key.UP)
        {
            player.speed = 1;
        }
    }

    // метод отвечает за проигрыш
    private void gameOver()
    {
        // поднимаем флаг остановки игры
        isGameStopped = true;
        // выводим сообщение о проигрыше
        showMessageDialog(Color.NONE, "Ты врезался", Color.RED, 52);
        // останавливаем таймер игры
        stopTurnTimer();
        // останавливаем машинку игрока
        player.stop();
    }

    // метод отвечает за выигрышь
    private void win()
    {
        // поднимаем флаг остановки игры
        isGameStopped = true;
        // выводим сообщение о выигрыше
        showMessageDialog(Color.NONE, "Ты доехал!!!", Color.GREEN, 52);
        // останавливаем таймер игры
        stopTurnTimer();
    }
}
