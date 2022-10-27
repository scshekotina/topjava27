package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

public class MealTestData {

    public static final int NOT_FOUND = 10;

    public static final int USER_MEAL_ID_1 = START_SEQ + 3;
    public static final LocalDate MEAL_DATE_1 = LocalDate.parse("2020-10-05");
    public static final LocalDate MEAL_DATE_2 = LocalDate.parse("2020-10-06");
    public static final LocalDateTime MEAL_DATE_TIME = LocalDateTime.parse("2021-01-25T07:30:10");
    public static Meal USER_MEAL_1 = new Meal(USER_MEAL_ID_1, LocalDateTime.parse("2020-10-05T10:00:10"),
            "Завтрак юзера", 1000);
    public static Meal USER_MEAL_2 = new Meal(START_SEQ + 4, LocalDateTime.parse("2020-10-05T14:00:10"),
            "Обед юзера", 500);
    public static Meal USER_MEAL_3 = new Meal(START_SEQ + 5, LocalDateTime.parse("2020-10-05T19:00:10"),
            "Ужин юзера", 700);
    public static Meal USER_MEAL_4 = new Meal(START_SEQ + 6, LocalDateTime.parse("2020-10-06T08:05:10"),
            "Завтрак юзера", 800);
    public static Meal USER_MEAL_5 = new Meal(START_SEQ + 7, LocalDateTime.parse("2020-10-06T12:01:10"),
            "Перекус юзера", 700);
    
    public static Meal getNew() {
        return new Meal(null, MEAL_DATE_TIME, "new meal", 1000);
    }

    public static Meal getUpdated() {
        Meal updated = new Meal(USER_MEAL_1);
        updated.setDateTime(MEAL_DATE_TIME);
        updated.setDescription("updated");
        updated.setCalories(2000);
        return updated;
    }

    public static void assertMatch(Meal actual, Meal expected) {
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    public static void assertMatch(Iterable<Meal> actual, Meal... expected) {
        assertMatch(actual, Arrays.asList(expected));
    }
    public static void assertMatch(Iterable<Meal> actual, Iterable<Meal> expected) {
        assertThat(actual).usingRecursiveFieldByFieldElementComparator().isEqualTo(expected);
    }
}