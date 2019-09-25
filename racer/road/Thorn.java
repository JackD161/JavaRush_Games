package com.javarush.games.racer.road;

public class Thorn extends RoadObject {
    public Thorn(int x, int y) {
        // создаем дорожный объект типа шипы
        super(RoadObjectType.THORN, x, y);
        // шипы стоят неподвижно на дороге
        speed = 0;
    }
}
