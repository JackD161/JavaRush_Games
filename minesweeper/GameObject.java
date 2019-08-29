package com.javarush.games.minesweeper;

// Так как в нашей игре будут использоваться игровые объекты (ячейки), создадим для их описания отдельный класс
public class GameObject {

    // у каждого игрового объекта есть координаты
    public int x;
    public int y;

    // игровой объект может быть миной, добавляем флаг мины
    public boolean isMine;

    // поле содержик количество заминированных соседей
    public int countMineNeighbors;

    // поле отвечающее за признак открыта ли ячейка или нет
    public boolean isOpen;

    // поле отвечает за признак того что яцейка помечена флагом
    public boolean isFlag;

    // основной конструктор для создания игрового объекта
    public GameObject(int x, int y, boolean isMine)
    {
        this.x = x;
        this.y = y;
        this.isMine = isMine;
    }
}
