package ru.javawebinar.topjava.web;

import static ru.javawebinar.topjava.util.MealsUtil.DEFAULT_CALORIES_PER_DAY;

public class SecurityUtil {

    private static int currentUser = 1;
    private static int caloriesPerDay = DEFAULT_CALORIES_PER_DAY;

    public static int authUserId() {
        return currentUser;
    }

    public static int authUserCaloriesPerDay() {
        return caloriesPerDay;
    }

    public static void setCurrentUser(int currentUser) {
        SecurityUtil.currentUser = currentUser;
    }
}