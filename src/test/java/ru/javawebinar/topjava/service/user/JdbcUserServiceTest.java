package ru.javawebinar.topjava.service.user;


import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.UserTestData;

import static org.junit.Assert.assertThrows;

@ActiveProfiles(Profiles.JDBC)
public class JdbcUserServiceTest extends UserServiceTest {
    @Test
    public void getWithUserUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> service.getWithMeals(UserTestData.USER_ID));
    }
}
