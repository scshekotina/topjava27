package ru.javawebinar.topjava.repository.inmemory;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.util.ValidationUtil;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private final Map<Integer, Meal> repository = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.meals.forEach(meal -> save(meal, MealsUtil.USER_ID));
    }

    @Override
    public Meal save(Meal meal, int userId) {
        meal.setUserId(userId);
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            repository.put(meal.getId(), meal);
            return meal;
        }
        return repository.computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
    }

    @Override
    public boolean delete(int id, int userId) {
        if (ValidationUtil.checkBelong(repository.get(id), userId)) {
            return repository.remove(id) != null;
        }
        return false;
    }
    @Override
    public Meal get(int id, int userId) {
        if (ValidationUtil.checkBelong(repository.get(id), userId)) {
            return repository.get(id);
        }
        return null;
    }

    @Override
    public Collection<Meal> getAll(int userId) {
        return getAllByPeriod(userId, null, null);
    }

    @Override
    public Collection<Meal> getAllByPeriod(int userId, LocalDateTime from, LocalDateTime to) {
        return repository.values().stream()
                .filter(meal -> (meal.getUserId() == userId) &&
                        (from == null || meal.getDateTime().toLocalDate().isAfter(from.toLocalDate().minusDays(1))) &&
                        (to == null || meal.getDateTime().toLocalDate().isAfter(to.toLocalDate().plusDays(1))))
                .sorted(Comparator.comparing(Meal::getDateTime))
                .collect(Collectors.toList());
    }
}

