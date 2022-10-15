package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.util.ValidationUtil;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static ru.javawebinar.topjava.web.SecurityUtil.authUserId;

@Controller
public class MealRestController {
    private final MealService service;

    public MealRestController(MealService service) {
        this.service = service;
    }

    protected final Logger log = LoggerFactory.getLogger(getClass());

    public List<MealTo> getAll() {
        log.info("get all meals");
        return MealsUtil.getTos(service.getAll(authUserId()), SecurityUtil.authUserCaloriesPerDay());
    }

    public List<MealTo> getAll(LocalDate startDate, LocalTime startTime, LocalDate endDate, LocalTime endTime) {
        log.info("get all meals from {} {} to {} {}", startDate, startTime, endDate, endTime);
        return MealsUtil.getFilteredTos(service.getAllByPeriod(authUserId(), startDate, endDate),
                SecurityUtil.authUserCaloriesPerDay(), startTime, endTime);
    }

    public Meal get (int id) {
        log.info("get meal with id {}", id);
        return service.get(id, authUserId());
    }

    public void delete (int id) {
        log.info("delete meal with id {}", id);
        service.delete(id, authUserId());
    }

    public void create(Meal meal) {
        log.info("create meal {}", meal);
        ValidationUtil.checkNew(meal);
        service.save(meal, authUserId());
    }

    public void update(Meal meal, int id) {
        log.info("update meal {}", meal);
        ValidationUtil.assureIdConsistent(meal, id);
        service.save(meal, authUserId());
    }
}