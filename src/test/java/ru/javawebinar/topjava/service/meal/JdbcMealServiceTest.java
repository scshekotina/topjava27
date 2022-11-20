package ru.javawebinar.topjava.service.meal;

import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.UserTestData;

import static org.junit.Assert.assertThrows;
import static ru.javawebinar.topjava.MealTestData.MEAL1_ID;

@ActiveProfiles(Profiles.JDBC)
public class JdbcMealServiceTest extends MealServiceTest {
    @Test
    public void getWithUserUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> service.getWithUser(MEAL1_ID, UserTestData.USER_ID));
    }
}
