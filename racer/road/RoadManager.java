package com.javarush.games.racer.road;

import com.javarush.engine.cell.Game;
import com.javarush.games.racer.PlayerCar;
import com.javarush.games.racer.RacerGame;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class RoadManager {
    // определим коннстантами границы дороги
    public static final int LEFT_BORDER = RacerGame.ROADSIDE_WIDTH;
    public static final int RIGHT_BORDER = RacerGame.WIDTH - LEFT_BORDER;
    // константы левой и правой крайних координат оси х матриц объектов на дороге
    private static final int FIRST_LANE_POSITION = 16;
    private static final int FOURTH_LANE_POSITION = 44;
    // список всех текущих объектов препядствий
    private List<RoadObject> items = new ArrayList<>();
    // заведем константу межмашинного интервала
    private static final int PLAYER_CAR_DISTANCE = 12;
    // поле для подсчета количества машин с которыми разминулся игрок
    private int passedCarsCount = 0;

    public int getPassedCarsCount() {
        return passedCarsCount;
    }

    // метод создает разные объекты на дороге
    private RoadObject createRoadObject(RoadObjectType type, int x, int y) {
        // если создаваемый тип объекта шипы
        if (type == RoadObjectType.THORN) {
            // возвращаем шипы
            return new Thorn(x, y);
        }
        // если создаваемый тип объекта пбяный водитель
        else if (type == RoadObjectType.DRUNK_CAR) {
            // возвращаем пьяного водителя
            return new MovingCar(x, y);
        } else {
            // возвращаем обычную машину
            return new Car(type, x, y);
        }
    }

    // метод для создания и добавления препятствия в список препятствий
    private void addRoadObject(RoadObjectType type, Game game) {
        // создаем случайную переменную х в пределах допустимой области
        int x = game.getRandomNumber(FIRST_LANE_POSITION, FOURTH_LANE_POSITION);
        // создаем переменную y за пределами области видимости игрока
        // изначально объект располагается за пределами игрового поля, чтобы появиться плавно
        int y = -1 * RoadObject.getHeight(type);
        // создаем препятствие на дороге заданного типа с получившимися координатами
        RoadObject obj = createRoadObject(type, x, y);
        // если для объекта есть место на дороге то добавляем его в список препятствий на дороге
        if (isRoadSpaceFree(obj)) {
            items.add(obj);
        }
    }

    // метод для отрисовывания препятствий на дороге
    public void draw(Game game) {
        // у всех объектов препятствий нужно вызвать метод их отрисовки
        for (RoadObject objects : items) {
            objects.draw(game);
        }
    }

    // метод двигает объекты с переданным ускорением
    public void move(int boost) {
        // у всех объектов должен быть вызван метод движения с приданием заданного ускорения
        // скорость движения получается из суммы текущей скорости и преданного ускорения из переданного в метод
        for (RoadObject object : items) {
            object.move(object.speed + boost, items);
        }
        // после движения всех элементов удаляем элементы вышедшие за края игрового экрана
        deletePassedItems();
    }

    // метод проверяет есть ли шипы на дороге, шипов не должно быть больше 1
    private boolean isThornExists() {
        boolean isExist = false;
        // перебираем все объекты в списке дорожных объектов, если в нем есть с типом шипов то возвращаем true, иначе false
        for (RoadObject obj : items) {
            if (obj.type == RoadObjectType.THORN) {
                isExist = true;
            }
        }
        return isExist;
    }

    // метод создает шипы
    private void generateThorn(Game game) {
        // создаем случайное число и если оно меньше 10 и в списке дорожных объектов нет шипов то создадим их специальным методом
        if (game.getRandomNumber(100) < 10 && !isThornExists()) {
            addRoadObject(RoadObjectType.THORN, game);
        }
    }

    // метод создает новые дорожные объекты
    public void generateNewRoadObjects(Game game) {
        // создаем шипы
        generateThorn(game);
        // создаем обычные машины
        generateRegularCar(game);
        // создаем пьяного водителя
        generateMovingCar(game);
    }

    // метод удаляет объекты вышедшие за пределы экрана
    private void deletePassedItems() {
        // перебираем все элементы списка
        Iterator<RoadObject> iterator = items.iterator();
        while (iterator.hasNext()) {
            RoadObject object = iterator.next();
            // если у очередного элемента координата y вышла за пределы игрового экрана
            if (object.y >= RacerGame.HEIGHT) {
                // если объект вышел за пределя игрового поля и это не шипы то уыеличиваем счетчик машин с которыми разминулся игрок
                if(object.type != RoadObjectType.THORN)
                {
                    passedCarsCount++;
                }
                // удаляем элемент
                iterator.remove();
            }
        }
    }

    // метод отвечает за столкновение игрока с дорожными объектами
    public boolean checkCrush(PlayerCar player) {
        boolean stop = false;
        // проверяем все объекты в списке препядствий на столкновение с игроком
        for (RoadObject object : items) {
            // если есть пересечение хотябы с одним объектом то возвращаем истину
            if (object.isCollision(player)) {
                stop = true;
                break;
            }
        }
        return stop;
    }

    // метод создает разные машины
    private void generateRegularCar(Game game) {
        // получаем случайное число определяющее тип машины для создания и с вероятность 30% создаем машину
        if (game.getRandomNumber(100) < 30) {
            addRoadObject(RoadObjectType.values()[game.getRandomNumber(4)], game);
        }
    }

    // метод проверяет свободное место между дорожными объектами
    private boolean isRoadSpaceFree(RoadObject object) {
        // изначально фдаг наличия свободного места поднят
        boolean free = true;
        // перебираем все дорожные объекты
        for (RoadObject obj : items) {
            // если расстояния между объектамси не хватает для безопасного маневрирования машинки - опускапем флаг свободного места
            if (obj.isCollisionWithDistance(object, PLAYER_CAR_DISTANCE)) {
                // опускаем флаг
                free = false;
                // прерываем цикл за ненадобностью больше перебирать
                break;
            }
        }
        return free;
    }

    // метод проверяет есть ли пьяный водитель на дороге
    private boolean isMovingCarExists()
    {
        boolean drunkCar = false;
        // перебираем список дорожных объектов в поисках пьяного водителя
        for (RoadObject obj : items)
        {
            // если находим то поднимаем флаг и прерываем цикл
            if(obj.type == RoadObjectType.DRUNK_CAR)
            {
                drunkCar = true;
                break;
            }
        }
        return drunkCar;
    }

    // метод создает пьяного водителя
    private void generateMovingCar(Game game)
    {
        // с вероятностью 10% и при отсутствии в списке дорожных объектов пьяного водителя создается пьяный водитель
        if(game.getRandomNumber(100) < 10 && !isMovingCarExists())
        {
            addRoadObject(RoadObjectType.DRUNK_CAR, game);
        }
    }
}
