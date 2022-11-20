package ru.javawebinar.topjava.service.meal;

import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.UserTestData;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import static org.junit.Assert.assertThrows;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.USER_MATCHER;

@ActiveProfiles(Profiles.DATAJPA)
public class DataJpaMealServiceTest extends MealServiceTest {

    @Test
    public void getWithUser() {
        Meal expected = new Meal(meal1);
        User expectedUser = new User(UserTestData.user);
        expected.setUser(expectedUser);
        expectedUser.setMeals(meals);
        Meal actual = service.getWithUser(MEAL1_ID, UserTestData.USER_ID);
        MEAL_MATCHER.assertMatch(actual, expected);
        USER_MATCHER.assertMatch(actual.getUser(), expectedUser);
    }

    @Test
    public void getWithUserNotFound() {
        assertThrows(NotFoundException.class, () -> service.getWithUser(MEAL1_ID, UserTestData.ADMIN_ID));
    }
}
