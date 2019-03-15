package com.javarush.games.spaceinvaders;

import com.javarush.engine.cell.*;
import com.javarush.games.spaceinvaders.gameobjects.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SpaceInvadersGame extends Game {

    // задаем размеры игрового поля
    public static final int WIDTH = 64;
    public static final int HEIGHT = 64;

    // задаем количество пуль игрока для
    private static final int PLAYER_BULLETS_MAX = 3;

    // переменная для подсчета очков игрока
    private int score;

    // переменная задающая сложность игры (вероятность выстрела вражеского корабля)
    public static final int COMPLEXITY = 5;

    // заводим список вражеских пуль
    private List<Bullet> enemyBullets;

    // заводим список пуль игрока
    private List<Bullet> playerBullets;

    // заводим список звезд на небе
    private List<Star> stars;

    // заводим переменную отвечающую за вражеский флот
    private EnemyFleet enemyFleet;

    // заводим переменную отвечающую за игрока
    private PlayerShip playerShip;

    // заводим флаг того что игра остановлена
    private boolean isGameStopped;

    // заводим счетчик анимации кадров
    private int animationsCount;

    // метод инициализации игры
    public void initialize()
    {
        setScreenSize(WIDTH, HEIGHT);
        createGame();
    }

    // метод создания игры
    private void createGame()
    {
        // создаем все объекты до отрисовки сцены

        // определяем начальные очки игрока
        score = 0;

        // создаем звезды
        createStars();

        // создаем вражеский флот
        enemyFleet = new EnemyFleet();

        // создаем корабль игрока
        playerShip = new PlayerShip();

        // создаем список выпушенных вражеских пуль
        enemyBullets = new ArrayList<>();

        // создаем список выпущенных игроком пуль
        playerBullets = new ArrayList<>();

        // устанавливаем базовую задержку хода в миллисекуднах
        setTurnTimer(40);

        // устанавливаем флаг что игра не остановлена
        isGameStopped = false;

        // устанавливаем счетчик анимации кадров в начало
        animationsCount = 0;

        // отрисовываем сцену
        drawScene();
    }

    // метод отрисовки игровой сцены
    private void drawScene()
    {
        // отрисовываем игровое поле
        drawField();

        // отрисовываем корабль игрока
        playerShip.draw(this);

        // отрисовываем вражеские пули
        for (Bullet bullet : enemyBullets)
        {
            bullet.draw(this);
        }

        // отрисовываем пули игрока
        for (Bullet bullet : playerBullets)
        {
            bullet.draw(this);
        }

        // отрисовываем вражеский флот
        enemyFleet.draw(this);
    }

    // метод отрисовки игрового пространства (поля)
    private void drawField()
    {
        // заполним игровое поле космосом (черным фоном и пустотой в яцейках)
        for (int y = 0; y < HEIGHT; y++)
        {
            for (int x = 0; x < WIDTH; x++)
            {
                setCellValueEx(x, y, Color.BLACK, "");
            }
        }

        // отрисовываем звезды
        for (Star star : stars)
        {
            star.draw(this);
        }
    }

    // метод создает звезды и заполняет ими список звезд
    private void createStars()
    {
        // инициализируем список и передаем в него 8 созданных звезд
        stars = new ArrayList<>();
        stars.add(new Star(43, 60));
        stars.add(new Star(53, 56));
        stars.add(new Star(8, 60));
        stars.add(new Star(27, 24));
        stars.add(new Star(25, 56));
        stars.add(new Star(24, 32));
        stars.add(new Star(15, 32));
        stars.add(new Star(12, 48));
    }

    // метод описывает что происходит каждый ход в игре
    @Override
    public void onTurn(int step) {
        // двигаем космические объекты
        moveSpaceObjects();

        // проверяем состояние всех игровых объектов
        check();

        // производим выстрелы вражеского флота
        Bullet bullet = enemyFleet.fire(this);

        // если выстрел произошел (метод вернул не null)
        if (bullet != null)
        {
            // добавляем выстреленную пулю в список вражеских пуль
            enemyBullets.add(bullet);
        }

        // устанавливаем очки игрока в поле очков
        setScore(score);

        // отрисовываем сцену
        drawScene();
    }

    private void moveSpaceObjects()
    {
        // двигаем вражеский флот
        enemyFleet.move();

        // двигаем корабль игрока
        playerShip.move();

        // двигаем вражеские пули
        for (Bullet bullet : enemyBullets)
        {
            bullet.move();
        }

        // двигаем пули игрока
        for (Bullet bullet : playerBullets)
        {
            bullet.move();
        }
    }

    // метод проверяет и удаляет "мертвые" пули, которые попали в цель или вылетели за границы игровой области
    private void removeDeadBullets()
    {
        // проверим все пули в списке вражеских пуль, "живые" ли они с помошью итератора
        Iterator<Bullet> iteratorEB = enemyBullets.iterator();
        while(iteratorEB.hasNext())
        {
            // получаем пулю от итератора
            Bullet enemyBullet = iteratorEB.next();
            // если пуля не "живая" или вылетела за край экрана
            if (!enemyBullet.isAlive || enemyBullet.y >= HEIGHT - 1)
            {
                // удаляем её
                iteratorEB.remove();
            }
        }

        // проверим все пули в списке пуль игрока и уберем все неживые и вышедшие за края экрана
        // сумма координаты y и высоты пули не должна быть меньше 0
        Iterator<Bullet> iteratorPB = playerBullets.iterator();
        while(iteratorPB.hasNext())
        {
            Bullet playerBullet = iteratorPB.next();
            if (!playerBullet.isAlive || playerBullet.y + playerBullet.height < 0)
            {
                iteratorPB.remove();
            }
        }
    }

    // метод проверяет состояние всех объектов игры
    private void check()
    {
        playerShip.verifyHit(enemyBullets);
        enemyFleet.deleteHiddenShips();
        removeDeadBullets();

        // подсчитываем очки игрока
        score += enemyFleet.verifyHit(playerBullets);

        // проверяем не достиг ли вражеский флот корабля игрока
        if (enemyFleet.getBottomBorder() >= playerShip.y)
        {
            // если достиг то убиваем игрока
            playerShip.kill();
        }

        // проверяем количество вражеских кораблей во флоте
        if (enemyFleet.getShipsCount() == 0)
        {
            // если вражеских кораблей не осталоь то вызываем метод победы игрока и остановку игры с задержкой на анимацию
            playerShip.win();
            stopGameWithDelay();
        }

        // проверяем жив ли игрок, если мертв то вызываем метод конца игры с задержкой
        if (!playerShip.isAlive)
        {
            stopGameWithDelay();
        }
    }

    // метод отвечает за остановку игры
    private void stopGame(boolean isWin)
    {
        // устанавливаем флаг остановки игры
        isGameStopped = true;

        // останавливаем таймер шагов
        stopTurnTimer();

        if (isWin)
        {
            // вывод сообщения если игрок выиграл
            showMessageDialog(Color.NONE, "Ты выиграл", Color.GREEN, 20);
        }
        else
        {
            // вывод сообщения если игрок проиграл
            showMessageDialog(Color.NONE, "Ты проиграл", Color.RED, 20);
        }
    }

    // метод устанавливает задержку перед выполнением метода конца игры
    private void stopGameWithDelay()
    {
        // увеличиваем счетчик анимации
        animationsCount++;

        if (animationsCount >= 10)
        {
            stopGame(playerShip.isAlive);
        }
    }

    // переопределяем метод разпознающий нажатия клавиш
    @Override
    public void onKeyPress(Key key) {
        // если нажат пробел когда игра остановлена происходит перезапуск игры
        if (key == Key.SPACE && isGameStopped)
        {
            createGame();
        }

        // если нажата клавиша влево нужно полю движения установить направление влево
        if (key == Key.LEFT)
        {
            playerShip.setDirection(Direction.LEFT);
        }

        // если нажата клавиша вправо нужно установить движение в направлении вправо
        if (key == Key.RIGHT)
        {
            playerShip.setDirection(Direction.RIGHT);
        }

        // если игра не остановлена то по нажатию на кнопку пробел должен происходить выстрел
        if (key.equals(Key.SPACE) && !isGameStopped)
        {
            Bullet oneShot = playerShip.fire();
            // если выстрел состоялся и в списке пуль игрока меньше чем задано по условиям сложности
            if (oneShot != null && playerBullets.size() < PLAYER_BULLETS_MAX)
            {
                // добавляем эту пулю в список пуль
                playerBullets.add(oneShot);
            }
        }
    }

    // переопределяем метод распознания отпускания клавиш
    @Override
    public void onKeyReleased(Key key) {
        // если текушее направление движения влево и отпущена кновка влево
        if (key == Key.LEFT && playerShip.getDirection().equals(Direction.LEFT))
        {
            // останавливаем корабль установкой направления вверх
            playerShip.setDirection(Direction.UP);
        }

        // если текущее направление движения вправо и отпущена кнопка вправо
        if (key == Key.RIGHT && playerShip.getDirection().equals(Direction.RIGHT))
        {
            // останавливаем корабль установкой направления вверх
            playerShip.setDirection(Direction.UP);
        }
    }

    // переопределим родительский класс работы с ячейками
    @Override
    public void setCellValueEx(int x, int y, Color cellColor, String value) {
        // метод должен работать только с координатами в пределах игрового поля
        if (x >= 0 && x <= WIDTH -1 && y > 0 && y <= HEIGHT - 1)
        {
            super.setCellValueEx(x, y, cellColor, value);
        }
    }
}
