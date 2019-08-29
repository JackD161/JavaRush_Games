package com.javarush.games.moonlander;

import com.javarush.engine.cell.*;

import java.util.List;

public class RocketFire extends GameObject {
    // кадры горения огня
    private List<int[][]> frames;

    // индекс текущего кадра
    private int frameIndex;

    // флаг видимости для отрисовки
    private boolean isVisible;

    public RocketFire(List<int[][]> frameList) {
        super(0, 0, frameList.get(0));
        frames = frameList;
        frameIndex = 0;
        isVisible = false;
    }

    // метод смены кадров анимации
    private void nextFrame()
    {
        // увеличиваем счетчик кадров на 1
        frameIndex++;

        // если счетчик кадров превысит количество кадров в списке кадров, то устанавливаем индекс кадров в начало на 0
        if (frameIndex >= frames.size())
        {
            frameIndex = 0;
        }

        // устанавливаем в матрицу отрисовки текущий кадр анимации
        matrix = frames.get(frameIndex);
    }

    // переопределяем родительский метод отрисовки
    @Override
    public void draw(Game game) {
        // метод не должен ничего отрисовывать если огонь не видро
        if (isVisible)
        {
            // вызываем метод смены кадра
            nextFrame();

            // вызываем родительский метод отрисовки
            super.draw(game);
        }
    }

    // метод отвечает за показ реактивного огня
    public void show()
    {
        isVisible = true;
    }

    // метод отвечает за скрытие реактивного огня
    public void hide()
    {
        isVisible = false;
    }
}
