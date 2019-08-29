package com.javarush.games.game2048;

import com.javarush.engine.cell.*;

public class Game2048 extends Game {
    // зададим константу размера игрового поля
    private static final int SIDE = 4;
    // зададим матрицу для хранения состояния игрового поля размером с игровое поле
    private int[][] gameField = new int[SIDE][SIDE];
    // переменная флаг признака остановки игры
    private boolean isGameStopped = false;
    // поле отвечает за подсчет очков
    private int score;

    // метод инициализации игры
    @Override
    public void initialize() {
        // зададим размер игрового поля
        setScreenSize(SIDE, SIDE);
        // создаем игру
        createGame();
        // красим игровое поле в один начальный цвет
        drawScene();
    }

    // метод создающий игру
    private void createGame()
    {
        // создаем новую матрицу
        gameField = new int[SIDE][SIDE];
        // обнуляем набранные очки
        score = 0;
        setScore(score);
        // при создании игры на игровое поле в две случайные ячейки заносятся первые две случайные цифры
        createNewNumber();
        createNewNumber();
    }

    // метод отрисовывает игровое поле
    private void drawScene()
    {
        for (int y = 0; y < SIDE; y++)
        {
            for (int x = 0; x < SIDE; x++)
            {
                // проходим по всей матрице и отображаем все содержимое окрашивая ячейки в зависимости от значения в ячейке
                setCellColoredNumber(x, y, gameField[y][x]);
            }
        }
    }

    // метод создает случайное число 2 или 4 с вероятностью 90% и 10% соответственно
    private void createNewNumber()
    {
        // ищем и проверяем максимальное значение с выигрышным значением
        if (getMaxTileValue() == 2048)
        {
            // если нашлось то игра выигрывается
            win();
        }
        else {
            // в цикле генерируем случайные координаты ячейки
            while (true) {
                int x = getRandomNumber(SIDE);
                int y = getRandomNumber(SIDE);
                // проверяем что случайная ячейки пуста (имеет 0 значение)
                if (gameField[y][x] == 0) {
                    // в пустую ячейку с вероятностью 10% заносится цифра 4, в останьных случаях заносится цифра 2
                    int rnd = getRandomNumber(10);
                    if (rnd == 9) {
                        gameField[y][x] = 4;
                    } else {
                        gameField[y][x] = 2;
                    }
                    // выходим из цикла
                    break;
                }
            }
        }
    }

    // метод возвращает цвет в зависимости от переданного значения
    private Color getColorByValue(int value)
    {
        Color color;
        // в зависимости от значения устанавливает определенный цвет
        switch(value)
        {
            case (0):
                color = Color.WHITE;
                break;
            case (2):
                color = Color.YELLOW;
                break;
            case (4):
                color = Color.CYAN;
                break;
            case (8):
                color = Color.MAGENTA;
                break;
            case (16):
                color = Color.PURPLE;
                break;
            case (32):
                color = Color.ORANGE;
                break;
            case (64):
                color = Color.GREEN;
                break;
            case (128):
                color = Color.CORAL;
                break;
            case (256):
                color = Color.FIREBRICK;
                break;
            case (512):
                color = Color.DEEPPINK;
                break;
            case (1024):
                color = Color.SEAGREEN;
                break;
            case (2048):
                color = Color.GOLD;
                break;
            default:
                color = Color.NONE;
                break;
        }
        // возвращаем определенный цвет
        return color;
    }

    // метод устанавливает цвет клетки по переданным координатам
    private void setCellColoredNumber(int x, int y, int value)
    {
        String val;
        // если переданное значение не 0 то передаем его в метод строковым значением
        if (value != 0)
        {
            val = String.valueOf(value);
        }
        else
            // если 0 то передаем в метод пустую строку
        {
            val = "";
        }
        // устанавливаем по заданным координатам цвет ячейки в зависимости от значения и устанавливаем полученое значение
        setCellValueEx(x, y, getColorByValue(value), val);
    }

    // метод сдвигает непустые ячейки массива влево
    private boolean compressRow(int[] row)
    {
        // заводим флаг совершонного действия
        boolean flagDo = false;
        // проходим циклом по массиву, сортируем "пузырьком"
        for (int i = 0; i < row.length - 1; i++)
        {
            for (int j = 0; j < row.length - 1; j++)
            {
                int a = row[j];
                int b = row[j + 1];
                // если текущий элемент 0 а следующий больше 0 то меняем их местами
                if (a == 0 && b > 0)
                {
                    // если сработало условие - значит действие произведено и мы поднимаем флаг фействия
                    flagDo = true;
                    row[j] = b;
                    row[j + 1] = a;
                }
            }
        }
        // возвращаем влаг фействия, если действия были то true инале false
        return flagDo;
    }

    // метод соединяет налетевшае друг на друга одинаковые ячейки
    private boolean mergeRow(int[] row)
    {
        // заводим совершенного флаг фействия
        boolean flagDo = false;
        for (int i = 0; i < row.length - 1; i++)
        {
            // проверяем соседние ячейки на одинаковость
            // действия выполняются только не с 0 ячейками
            if (row[i] != 0) {
                // если текущая ячейка равна следующей ячейке
                if (row[i] == row[i + 1]) {
                    // поднимаем флаг действия
                    flagDo = true;
                    // увеличиваем набранные очки на сумму соединенных ячеек
                    score += row[i] + row[i + 1];
                    // устанавливаем набранные очки на игровое поле
                    setScore(score);
                    // в текущую ячейку заносим сумму её и следующей ячейки
                    row[i] = row[i] + row[i + 1];
                    // в следующую ячейку записываем 0
                    row[i + 1] = 0;
                }
            }
        }
        // возвращаем флаг совершенного или не совершенного действия
        return flagDo;
    }

    // метод отвечает за сдвиг ячеек влево
    private void moveLeft()
    {
        // создаем флаг того что действие произошло
        boolean flagDo = false;
        // перебираем все строки игрового поля
        for (int[] row : gameField)
        {
            // сдвигаем строку
            // сжимаем строку
            // сдвигаем строку после сжимания
            if (compressRow(row) | mergeRow(row) | compressRow(row)) // обязвтельно используем не укороченное ИЛИ для выполнения всех действий
            {
                // если любое из действий произошло поднимаем флаг
                flagDo = true;
            }
        }
        // если любое из действий произошло то ход совершен и нужно вызвать генерацию нового числа
        if (flagDo)
        {
            createNewNumber();
        }
    }

    // метод отвечает за сдвиг ячеек вправо
    private void moveRight()
    {
        // если нужно сдвинуть плитки вниз, берем нашу матрицу gameField, дважды поворачиваем ее на 90 градусов по часовой стрелке
        // (право становится слева), сдвигаем влево и разворачиваем матрицу обратно
        // (еще 2 раза поворачиваем матрицу на 90 градусов по часовой стрелке)
        rotateClockwise();
        rotateClockwise();
        moveLeft();
        rotateClockwise();
        rotateClockwise();
    }

    // метод отвечает за сдвиг ячеек вверх
    private void moveUp()
    {
        // если нужно сдвинуть плитки вниз, берем нашу матрицу gameField, троижды поворачиваем ее на 90 градусов по часовой стрелке
        // (верх становится слева), сдвигаем влево и разворачиваем матрицу обратно
        // (еще 1 раза поворачиваем матрицу на 90 градусов по часовой стрелке)
        rotateClockwise();
        rotateClockwise();
        rotateClockwise();
        moveLeft();
        rotateClockwise();
    }

    // метод отвечает за сдвиг ячеек вниз
    private void moveDown()
    {
        // если нужно сдвинуть плитки вниз, берем нашу матрицу gameField, поворачиваем ее на 90 градусов по часовой стрелке
        // (низ становится слева), сдвигаем влево и разворачиваем матрицу обратно
        // (еще 3 раза поворачиваем матрицу на 90 градусов по часовой стрелке)
        rotateClockwise();
        moveLeft();
        rotateClockwise();
        rotateClockwise();
        rotateClockwise();
    }

    // метод обрабатывает нажатия книпок управления
    // в зависимости от нажатой книпки вызывается отределенный метод сдвига ячеек в нужнгом направлении
    @Override
    public void onKeyPress(Key key) {
        // проверяем если игра остановлена и нажимается кнопка пробел то игра перезапускается, остальные кнопки в этом случае игнорируются
        if (key == Key.SPACE && isGameStopped) {
            // перед перезапуском игры сбрасываем влаг остановки
            isGameStopped = false;
            // создаем новую игру
            createGame();
            // отрисовываем игровое поле
            drawScene();
        } else {
            if (!isGameStopped) {
                if (!canUserMove()) {
                    gameOver();
                } else {
                    if (key == Key.LEFT) {
                        moveLeft();

                    } else if (key == Key.RIGHT) {
                        moveRight();
                    } else if (key == Key.UP) {
                        moveUp();
                    } else if (key == Key.DOWN) {
                        moveDown();
                    }
                    // после всего нужно перерисовать игровое поле
                    drawScene();
                }
            }
        }
    }

    // метод вращает матрицу по часовой стрелке на 90 градусов
    private void rotateClockwise()
    {
        // создаем временную матрицу размером с матрицу игрового поля
        int[][] tempArray = new int[SIDE][SIDE];
        // проходим циклом по матрице игрового поля
        for (int y = 0; y < gameField.length; y++)
        {
            for (int x = 0; x < gameField.length; x++)
            {
                tempArray[x][SIDE - 1 - y] = gameField[y][x];
            }
        }
        // заменяем матрицу игрового поля на временную матрицу
        gameField = tempArray;
    }

    // метод ищет максимальное значение в ячейках игрового поля и возвращает его
    private int getMaxTileValue()
    {
         int max = 0;
         for (int y = 0; y < gameField.length; y++)
         {
             for (int x = 0; x < gameField.length; x++)
             {
                 if (gameField[y][x] > max)
                 {
                     max = gameField[y][x];
                 }
             }
         }
         return max;
    }

    // метод отвечает за победу игрока
    private void win()
    {
        // останавливаем игру
        isGameStopped = true;
        // выводим поздравление
        showMessageDialog(Color.NONE, "Ты победил!", Color.GREEN, 50);
    }

    // метод отвечает за проигрыш
    private void gameOver()
    {
        // останавливаем игру
        isGameStopped = true;
        // выводим сообщение о проигрыше
        showMessageDialog(Color.NONE, "Ты проиграл!", Color.RED, 50);
    }

    private boolean canUserMove()
    {
        // заводим флаги для проверки свободных ячеек на поле и одинаковых соседей
        boolean checkPairs = false;
        boolean checkFreeCells = false;
        // перебираем игровое поле
        for (int y = 0; y < gameField.length; y++)
        {
            for (int x = 0; x < gameField.length - 1; x++)
            {
                // если есть хотябы одна свободная ячейка то поднимаем флаг свободных ячеек
                if (gameField[y][x] == 0 || gameField[y][x + 1] == 0)
                {
                    checkFreeCells = true;
                }
                // если есть хотябы одна пара ячеек по горизонтали или вертикали то поднимаем флаг имеющихся пар
                if (gameField[y][x] == gameField[y][x + 1])
                {
                    checkPairs = true;
                }
            }
        }
        for (int y = 0; y < gameField.length - 1; y++)
        {
            for (int x = 0; x < gameField.length; x++)
            {
                // если есть хотябы одна свободная ячейка то поднимаем флаг свободных ячеек
                if (gameField[y][x] == 0)
                {
                    checkFreeCells = true;
                }
                // если есть хотябы одна пара ячеек по горизонтали или вертикали то поднимаем флаг имеющихся пар
                if (gameField[y][x] == gameField[y + 1][x])
                {
                    checkPairs = true;
                }
            }
        }

        if (checkFreeCells)
        {
            return true;
        }
        else if (checkPairs)
        {
            return true;
        }
        else
        {
            return false;
        }
    }
}
