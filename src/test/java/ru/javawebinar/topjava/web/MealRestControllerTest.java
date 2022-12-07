package ru.javawebinar.topjava.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javawebinar.topjava.MealTestData.*;

import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.util.exception.NotFoundException;
import ru.javawebinar.topjava.web.json.JsonUtil;
import ru.javawebinar.topjava.web.meal.MealRestController;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.javawebinar.topjava.UserTestData.USER_ID;


public class MealRestControllerTest extends AbstractControllerTest {

    private static final String REST_URL = MealRestController.REST_URL + '/';

    @Autowired
    private MealService mealService;

    @Test
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + MEAL1_ID))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MEAL_MATCHER.contentJson(meal1));
    }

    @Test
    void getAll() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MEAL_TO_MATCHER.contentJson(MealsUtil.getTos(meals, MealsUtil.DEFAULT_CALORIES_PER_DAY)));
    }

    @Test
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + MEAL1_ID))
                .andDo(print())
                .andExpect(status().isNoContent());
        assertThrows(NotFoundException.class, () -> mealService.get(MEAL1_ID, USER_ID));
    }

    @Test
    void createWithLocation() throws Exception{
        Meal newMeal = MealTestData.getNew();
        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newMeal)))
                .andExpect(status().isCreated());
        Meal created = MEAL_MATCHER.readFromJson(action);
        int newId = created.id();
        newMeal.setId(newId);
        MEAL_MATCHER.assertMatch(created, newMeal);
        MEAL_MATCHER.assertMatch(mealService.get(newId, USER_ID), newMeal);
    }

    @Test
    void update() throws Exception{
        Meal updated = MealTestData.getUpdated();
        perform(MockMvcRequestBuilders.put(REST_URL + MEAL1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated)))
                .andDo(print())
                .andExpect(status().isNoContent());
        MEAL_MATCHER.assertMatch(mealService.get(updated.id(), USER_ID), updated);
    }

    @Test
    void getBetweenDateTime() throws Exception {

        List<MealTo> tos = MealsUtil.getTos(meals, MealsUtil.DEFAULT_CALORIES_PER_DAY);

        perform(MockMvcRequestBuilders
                .get(REST_URL + "filter/")
                .param("startDate", "2020-01-30")
                .param("startTime", "09:00")
                .param("endDate", "2022-01-31")
                .param("endTime", "11:00"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MEAL_TO_MATCHER.contentJson(tos.get(2), tos.get(6)));
    }

    @Test
    void getBetweenDateTimeWithNull() throws Exception {

        List<MealTo> tos = MealsUtil.getTos(meals, MealsUtil.DEFAULT_CALORIES_PER_DAY);

        perform(MockMvcRequestBuilders
                .get(REST_URL + "filter/")
                .param("startDate", "2020-01-31"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MEAL_TO_MATCHER.contentJson(tos.get(0), tos.get(1), tos.get(2), tos.get(3)));
    }

}
