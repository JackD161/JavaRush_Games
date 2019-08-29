package com.javarush.games.moonlander;

import  com.javarush.engine.cell.*;

import java.util.ArrayList;
import java.util.List;

public class Rocket extends GameObject {
    // зададим переменную отвечающую за скорость движения по вертикали (Y)
    private double speedY = 0;

    // зададим переменную отвечающую за скорость движения по горизонтали (X)
    private double speedX = 0;

    // зададим переменную отвечающую за ускорение
    private double boost = 0.05;

    // переменная отвечающая за энерцию движение корабля
    private double slowdown = boost / 10;

    //зададим переменные реактивного огня двигалелей
    private RocketFire leftFire;
    private RocketFire rightFire;
    private RocketFire downFire;

    // базовый конструктор задает стартовые координаты и задает форму посадочного модуля по матрице
    public Rocket(double x, double y) {
        super(x, y, ShapeMatrix.ROCKET);

        // создаем список кадров для анимации огня снизу
        List<int[][]> downFireAnimate = new ArrayList<>();

        // создаем список кадров для анимации огня слево
        List<int[][]> leftFireAnimate = new ArrayList<>();

        // создаем список кадров для анимации огня справо
        List<int[][]> rightFireAnimate = new ArrayList<>();

        // добавляем кадры огня снизу из матрицы форм
        downFireAnimate.add(ShapeMatrix.FIRE_DOWN_1);
        downFireAnimate.add(ShapeMatrix.FIRE_DOWN_2);
        downFireAnimate.add(ShapeMatrix.FIRE_DOWN_3);

        // добавляем кадры огня слево из матрицы форм
        leftFireAnimate.add(ShapeMatrix.FIRE_SIDE_1);
        leftFireAnimate.add(ShapeMatrix.FIRE_SIDE_2);

        // добавляем кадры огня справо из матрицы форм
        rightFireAnimate.add(ShapeMatrix.FIRE_SIDE_1);
        rightFireAnimate.add(ShapeMatrix.FIRE_SIDE_2);

        // инициализируем огонь снизу с полученными кадрами
        downFire = new RocketFire(downFireAnimate);

        // инициализируем огонь слево с полученными кадрами
        leftFire = new RocketFire(leftFireAnimate);

        // инициализируем огонь справо с полученными кадрами
        rightFire = new RocketFire(rightFireAnimate);
    }

    // метод отвечает за движение рокеты
    public void move(boolean isUpPressed, boolean isLeftPressed, boolean isRightPressed) {

        // если нажата кнопка вверх - движение по вертикали замедляется на значение ускорения
        if (isUpPressed) {
            speedY -= boost;
        } else {
            // иначе скорость по вертикали увеличивается на значение ускорения
            speedY += boost;
        }

        // изменяем координату Y на значение скорости
        y += speedY;

        // если нажата кнопка влево то уменьшаем скорость движения по горизонтали
        if (isLeftPressed) {
            speedX -= boost;
            x += speedX;
            // если нажата кнопка вправо то увеличиваем скорость движения по горизонтали
        } else if (isRightPressed) {
            speedX += boost;
            x += speedX;
            // если кнопки управления не нажаты и скорость движения по горизонтали в пределах значениея инерции в обоих направлениях
            // инерция может быть как положительной так и отрицательной величиной по отношению к скорости движения
            // останавливаем движение
        } else if (speedX > slowdown) {
            speedX -= slowdown;
        } else if (speedX < -slowdown) {
            speedX += slowdown;
        } else {
            speedX = 0;
        }
        // изменяем координату X на значение скорости
        x += speedX;
        checkBorders();
        switchFire(isUpPressed, isLeftPressed, isRightPressed);
    }

    // метод проверяет вылет ракеты за границы экрана
    private void checkBorders() {
        // если координата х за пределами поля то устанавливаем её в 0 и устанавливаем скорость движения по горизонтали в 0
        if (x < 0) {
            x = 0;
            speedX = 0;
            // если сумма координаты х и ширины корабля выходит за пределы игрового поля,
            // устанавливаем координату в максимально допустимое правое положение и устанавливаем скорость горизонтального движения в 0
        } else if (x + width > MoonLanderGame.WIDTH) {
            x = MoonLanderGame.WIDTH - width;
            speedX = 0;
        }
        // если значение координаты y выходит за верхние границы экрана - устанавливаем координату в 0 и устанавливаем ускорение горизонтальног движение в 0
        if (y <= 0) {
            y = 0;
            speedY = 0;
        }
    }

    // Метод будет проверять "мягкость" посадки. Если ракета слишком быстро приземляется на платформу метод вернет false.
    public boolean isStopped() {
        return speedY < 10 * boost;
    }

    // метод принимает в качестве параметра игровой объект и возвращает true, если ракета пересекается с этим объектом
    public boolean isCollision(GameObject object) {
        int transparent = Color.NONE.ordinal();

        for (int matrixX = 0; matrixX < width; matrixX++) {
            for (int matrixY = 0; matrixY < height; matrixY++) {
                int objectX = matrixX + (int) x - (int) object.x;
                int objectY = matrixY + (int) y - (int) object.y;

                if (objectX < 0 || objectX >= object.width || objectY < 0 || objectY >= object.height)
                    continue;

                if (matrix[matrixY][matrixX] != transparent && object.matrix[objectY][objectX] != transparent)
                    return true;
            }
        }
        return false;
    }

    // метод отвечает за приземление корабля
    public void land()
    {
        // для корректного отображения нам нужно поднять ракету на одну позицию вверх
        y--;
    }

    // метод отвечает за крушение корабля
    public void crash()
    {
        matrix = ShapeMatrix.ROCKET_CRASH;
    }

    // метод отвечает за переклчение реактивного огня
    private void switchFire(boolean isUpPressed, boolean isLeftPressed, boolean isRightPressed)
    {
        // реактивная тяга отображалась внизу ракеты, нужно установить для нее координаты x и y определенным образом:

        // для отображения в цунтре рокеты
        // в поле x огня мы устанавливаем значение x (позиция левой границы) ракеты + ширина ракеты, поделенная на 2. Таким образом x тяги окажется посредине ракеты;
        // в поле y огня мы устанавливаем значение y (позиция верхней границы) ракеты + высота ракеты. Таким образом y тяги окажется внизу ракеты;
        if (isUpPressed)
        {
            downFire.x = x + (width / 2);
            downFire.y = y + height;

            // отображаем огонь
            downFire.show();
        }
        else
        {
            downFire.hide();
        }

        // для отображения слева от рокеты
        // в поле х огня мы устанавливаем значение x + ширина рокеты (позиция левой границы) ракеты. Таким образом x тяги окажется слева от ракеты;
        // в поле y огня мы устанавливаем значение y (позиция верхней границы) ракеты + высота ракеты. Таким образом y тяги окажется внизу ракеты;
        if (isLeftPressed)
        {
            leftFire.x = x + width;
            leftFire.y = y + height;

            // отображаем огонь
            leftFire.show();
        }
        else
        {
            leftFire.hide();
        }

        // для отображениея справо от рокеты
        // в поле х огня мы устанавливаем значение x - ширина матрицы огня. Таким образом x тяги окажется справо от ракеты;
        // в поле y огня мы устанавливаем значение y (позиция верхней границы) ракеты + высота ракеты. Таким образом y тяги окажется внизу ракеты;
        if (isRightPressed)
        {
            rightFire.x = x - ShapeMatrix.FIRE_SIDE_1[0].length;
            rightFire.y = y + height;
            rightFire.show();
        }
        else
        {
            rightFire.hide();
        }
    }

    // переопределяем метод отрисовки базового класса
    @Override
    public void draw(Game game) {
        super.draw(game);
        downFire.draw(game);
        leftFire.draw(game);
        rightFire.draw(game);
    }
}
