package com.javarush.games.racer;

public class FinishLine extends GameObject {
    // поле отвечающее за видимость финишной линии
    private boolean isVisible = false;

    // базовый конструктор
    public FinishLine() {
        super(RacerGame.ROADSIDE_WIDTH, -1 * ShapeMatrix.FINISH_LINE.length, ShapeMatrix.FINISH_LINE);
    }

    // метод показывает финишную линию
    public void show()
    {
        isVisible = true;
    }

    // метод движения финишной линии
    public void move(int boost)
    {
        // метод ничего не должен делать если финишная линия не видна
        if (isVisible)
        {
            y += boost;
        }
    }

    // метод проверяет пересечение игроком финишной линии
    public boolean isCrossed(PlayerCar player)
    {
        if(player.y < this.y)
        {
            return true;
        }
        else
        {
            return false;
        }
    }
}
