package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;

import java.util.List;

public interface IMealDao {
    void delete(int id);

    void update(Meal meal);

    List<Meal> getAll();

    Meal get(int id);
}
