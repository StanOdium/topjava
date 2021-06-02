package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> meals = Arrays.asList(
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
        );

        List<UserMealWithExcess> mealsTo = filteredByCycles(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        mealsTo.forEach(System.out::println);

//        System.out.println(filteredByStreams(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));
    }

    public static List<UserMealWithExcess> filteredByCycles(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        if (meals == null || meals.isEmpty())
            return null;

        Map<LocalDate, Integer> caloriesPerDayMap = new HashMap<>();
        List<UserMeal> filteredList = new ArrayList<>();
        List<UserMealWithExcess> filteredListWithExcess = new ArrayList<>();

        for (UserMeal nextMeal : meals) {
            LocalDate localDate = nextMeal.getDateTime().toLocalDate();

            if (caloriesPerDayMap.isEmpty() || !caloriesPerDayMap.containsKey(localDate)) {
                int caloriesSum = nextMeal.getCalories();
                caloriesPerDayMap.put(localDate, caloriesSum);
            } else {
                caloriesPerDayMap.compute(localDate, (date, cals) -> cals += nextMeal.getCalories());
            }

            if (TimeUtil.isBetweenHalfOpen(nextMeal.getDateTime().toLocalTime(), startTime, endTime)) {
                filteredList.add(nextMeal);
            }
        }

        for (UserMeal meal : filteredList) {
            filteredListWithExcess.add(new UserMealWithExcess(meal.getDateTime(), meal.getDescription(), meal.getCalories(),
                    caloriesPerDayMap.get(meal.getDateTime().toLocalDate()) > caloriesPerDay));
        }

        return filteredListWithExcess;
    }

    public static List<UserMealWithExcess> filteredByStreams(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        // TODO Implement by streams
        return null;
    }
}
