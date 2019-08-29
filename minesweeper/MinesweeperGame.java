package com.javarush.games.minesweeper;

import com.javarush.engine.cell.*;

import java.util.ArrayList;
import java.util.List;

// класс игры MinesweeperGame будет реализовывать логику игры
public class MinesweeperGame extends Game {
    // заведем приватную переменную задающую размер игрового поля
    private static final int SIDE = 9;
    // приватная матрица для хранения состоянгия игрового поля
    private GameObject[][] gameField = new GameObject[SIDE][SIDE];
    // заводим счетчик созданныч мин
    private int countMinesOnField;
    // переменная задающая символ мины
    private static final String MINE = "\uD83D\uDCA3";
    // переменная задающая симфол флага
    private static final String FLAG = "\uD83D\uDEA9";
    // переменная хранит количество неиспользованных флагов
    private int countFlags;
    // поле признак завершения игры
    private boolean isGameStopped;
    // переменная отвечет за подсчет ещё закрытых ячеек, изначально оно равно площади игрового поля
    private int countClosedTiles = SIDE * SIDE;
    // переменная отвечает за подсчет игровых очков
    private int score;

    // метод инициализации игры
    @Override
    public void initialize() {
        // инициализируем игровое поля заданным константой размером
        setScreenSize(SIDE, SIDE);

        // создаем игру
        createGame();
    }

    // метод создающий игру
    private void createGame()
    {
        // очищаем игровое поле
        for (int y = 0; y < gameField.length; y++)
        {
            for (int x = 0; x < gameField.length; x++)
            {
                setCellValue(x, y, "");
            }
        }
        // заполним матрицу
        for (int y = 0; y < SIDE; y++)
        {
            for (int x = 0; x < SIDE; x++)
            {
                /*
                Если вы заметили, координаты матрицы отличаются от классической прямоугольной системы координат (Декартовой системы координат).
                При обращении к матрице сначала вы указываете y, а потом x, в то время как в математике принято сначала указывать x (x, y).
                Возможно, вы задаетесь вопросом: «А почему бы не перевернуть матрицу в своем воображении и не обращаться к элементам привычным путем через (x, y)?
                От этого же содержимое матрицы не изменится».
                Да, ничего не изменится. Но в мире программирования к матрицам принято обращаться в форме «сначала y, потом x».
                Это нужно принять как должное.
                 */
                // заводим флаг который поднимается с вероятностью 10%
                boolean rnd = false;
                if (getRandomNumber(10) == 1) {
                    rnd = true;
                    countMinesOnField++;
                }
                // создаем игровой объект с учетом если влаг поднят то это мина
                gameField[y][x] = new GameObject(x, y, rnd);
                // окрашиваем каждую ячейку в оранжевый цвет
                setCellColor(x,y, Color.ORANGE);
            }
        }
        // подсчитываем мины по соседним объектам
        countMineNeighbors();
        // количество флагов для мин должно быть равно количеству мин на игровом поле
        countFlags = countMinesOnField;
    }

    // метод должен найти всех соседей игрового объекта
    private List<GameObject> getNeighbors(GameObject gameObject)
    {
        // зададим флаги прилегания объекта к краю игрового поля
        boolean top = false;
        boolean bottom = false;
        boolean left = false;
        boolean right = false;

        // получаем координаты переданного объекта
        int x = gameObject.x;
        int y = gameObject.y;

        // проверяем позицию переданного объекта
        // Важно помнить, что координаты игрового поля начинаются с левого верхнего угла
        // обект занимает левую верхнию ячейку
        if (x == 0 && y == 0)
        {
            // значит соседей слева и вверху у него не будет
            left = true;
            top = true;
        }
        // объект занимает правую нижнию ячейку
        if (x == SIDE - 1 && y == SIDE - 1)
        {
            // значит соседей справа и снизу у него не будет
            right = true;
            bottom = true;
        }
        // объект занимает верхнию правую ячейку
        if (x == SIDE - 1 && y == 0)
        {
            // значит соседей справа и сверху у него не будет
            right = true;
            top = true;
        }
        // объект занимает нижнию левую ячейку
        if (x == 0 && y == SIDE - 1)
        {
            // значит сосдедей снизу и слева у него не будет
            left = true;
            bottom = true;
        }
        // объект занимает левую ячейку
        if (x == 0 && (y >0 && y < SIDE -1))
        {
            // значит соседей слева везде у него не будет
            left = true;
        }
        // объект занимает правую ячейку
        if (x == SIDE -1 && (y > 0 && y < SIDE - 1))
        {
            // значит соседей справа везде у него не будет
            right = true;
        }
        // объект занимает верхнию ячейку
        if ((x > 0 && x < SIDE - 1) && y == 0)
        {
            // значит соседей сверху везде у него не будет
            top = true;
        }
        // объект занимает нижнию ячейку
        if ((x > 0 && x < SIDE - 1) && y == SIDE - 1)
        {
            // значит соседей снизу везде у него не будет
            bottom = true;
        }
        // заводим список соседей
        ArrayList<GameObject> neighbors = new ArrayList<GameObject>();
        // добавляем соседа сверху слева, если он есть
        if (!top && !left) {
            neighbors.add(gameField[y - 1][x - 1]);
        }
        // добавляем соседа сверху, если он есть
        if (!top) {
            neighbors.add(gameField[y - 1][x]);
        }
        // добавляем соседа сверху справа, если он есть
        if (!top && !right) {
            neighbors.add(gameField[y - 1][x + 1]);
        }
        // добавляем соседа слева, если он есть
        if (!left) {
            neighbors.add(gameField[y][x - 1]);
        }
        // добавляем соседа справа, если он есть
        if (!right) {
            neighbors.add(gameField[y][x + 1]);
        }
        // добавляем соседа справа снизу, если он есть
        if (!bottom && !left) {
            neighbors.add(gameField[y + 1][x - 1]);
        }
        // добавляем соседа снизу, если он есть
        if (!bottom) {
            neighbors.add(gameField[y + 1][x]);
        }
        // добавляем соседа снизу справа, если он есть
        if (!bottom && !right) {
            neighbors.add(gameField[y + 1][x + 1]);
        }
        // возвращаем результат
        return neighbors;
    }

    // метод поиска мин по соседним объектам
    private void countMineNeighbors()
    {
        // перебираем все игровые объекты на игровом поле
        for (int y = 0; y < gameField.length; y++)
        {
            for (int x = 0; x < gameField.length; x++)
            {
                // если текащий объект не мина
                if (!gameField[y][x].isMine)
                {
                    // подсчитываем количество соседних объектоа мин
                    // заводим локальный счетчик мин для конкретной ячейки
                    int cntMines = 0;
                    // перебираем всех полученных соседей
                    for (GameObject search : getNeighbors(gameField[y][x]))
                    {
                        // если сосед мина то увеличиваем локальный счетчик
                         if (search.isMine)
                         {
                             cntMines++;
                         }
                    }
                    // заносим в игровой объект количество найденных заминированных соседей
                    gameField[y][x].countMineNeighbors = cntMines;
                }
            }
        }
    }

    // метод отвечает за открытие ячейки на игровом поле
    private void openTile(int x, int y)
    {
        // метод ничего не должен делать с уже открытыми элементами, помеченными флагами и если игра остановлена
        if (!gameField[y][x].isOpen && !gameField[y][x].isFlag && !isGameStopped)
        {
            // помечаем ячейку открытой и окрашиваем её в заданный цвет
            gameField[y][x].isOpen = true;
            // при открытии ячейки уменьшаем счетчик неоткрытых ячеек
            countClosedTiles--;
            setCellColor(x, y, Color.GREEN);
            // получаем объект по переданным координатам
            GameObject object = gameField[y][x];
            // если объект мина то
            if (object.isMine) {
                // выводим значек мины в яейку и красим ячейку в красный цвет
                setCellValueEx(x, y, Color.RED, MINE);
                // завершаем игру
                gameOver();
            }
            // если объект не мина и есть мины в соседних ячейках - выводим в клетку количество заминированных соседей
            else if (object.countMineNeighbors != 0) {
                setCellNumber(x, y, object.countMineNeighbors);
                // увеличиваем игровой счет на 5 очков
                score += 5;
                // выводим очки на экран
                setScore(score);
            }
            // иначе ничего не выводим и открываем соседние ячейки
            else {
                setCellValue(x, y, "");
                // получаем соседей и рекурсивно их открываем
                for (GameObject neighbors : getNeighbors(object)) {
                    // открываем не открытых ещё соседей
                    if (!neighbors.isOpen) {
                        openTile(neighbors.x, neighbors.y);
                    }
                }
                // если количество неоткрытых ячеек на поле равно количеству мин то игра выиграна
                if (countClosedTiles == countMinesOnField)
                {
                    win();
                }
            }
        }
    }

    // метод отвечет за маркировку ячейки флажком
    // Он должен:
    //- отмечать ячейку на игровом поле флагом или снимать флаг;
    //- следить за количеством флагов;
    //- заниматься отрисовкой и стиранием флагов на игровом поле;
    //- менять цвет ячейки поля, если в ней устанавливается флаг и возвращать цвет обратно, если флаг снимается.
    private void markTile(int x, int y)
    {
        // метод ничего не должен делать если игра остановлена
        if (!isGameStopped) {
            // метод ничего не должен делать если ячейка уже открыта или флаги все израсходовали и текущая ячейка не флаг
            if (!gameField[y][x].isOpen) {
                if (countFlags != 0 || gameField[y][x].isFlag) {
                    // если текущая ячейка не флаг
                    if (!gameField[y][x].isFlag) {
                        // поднимаем флаг
                        gameField[y][x].isFlag = true;
                        // уменьшаем количество флагов в запасе
                        countFlags--;
                        // отрисовываем флаг в ячейке
                        setCellValue(x, y, FLAG);
                        // окрашиваем ячейку нужным цветом
                        setCellColor(x, y, Color.YELLOW);
                    } else {
                        // опускаем флаг
                        gameField[y][x].isFlag = false;
                        // увеличиваем количество флагов в запасе
                        countFlags++;
                        // удаляем пометку ячейки
                        setCellValue(x, y, "");
                        // возвращаем ячейке исходный цвет
                        setCellColor(x, y, Color.ORANGE);
                    }
                }
            }
        }
    }

    // метод обрабатывает нажатия левой кнопки мыши
    @Override
    public void onMouseLeftClick(int x, int y) {
        // если игра остановлена тол нажатие левой книпки мыши её перезапустит
        if (isGameStopped)
        {
            restart();
        }
        // иначе происходит открытие ячейки
        else {
            openTile(x, y);
        }
    }

    // метод обрабатывает нажатия правой кнопки мыши
    @Override
    public void onMouseRightClick(int x, int y) {
        markTile(x, y);
    }

    // метод отвечает за проигрыш
    private void gameOver()
    {
        // ставил признак остановки игры
        isGameStopped = true;
        // выводим сообщение о проигрыше
        showMessageDialog(Color.NONE, "Ты взорвался", Color.DARKRED, 50);
    }

    // метод львечает за выигшрыш
    private void win()
    {
        // ставим признак остановки игры
        isGameStopped = true;
        // выводим сообщение о выигрыше
        showMessageDialog(Color.NONE, "Ты выиграл", Color.GOLDENROD, 50);
    }

    // метод для перезапуска игры
    private void restart()
    {
        // сбрасываем флаг остановки игры
        isGameStopped = false;
        // возвращаем в исходное состояние количество закрытых ячеек
        countClosedTiles = SIDE * SIDE;
        // обнуляем и отображаем набранные очки
        score = 0;
        setScore(score);
        // обнуляем количество мин на игровом поле
        countMinesOnField = 0;
        // вызываем метод запуска игры
        createGame();
    }
}
