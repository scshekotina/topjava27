package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.model.Meal;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Profile(Profiles.HSQL_DB)
@Repository
public class HsqldbJdbcMealRepository extends JdbcMealRepository {
    public HsqldbJdbcMealRepository(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        super(jdbcTemplate, namedParameterJdbcTemplate);
    }

    protected Object convertDate(LocalDateTime dateTime) {
        return Date.valueOf(dateTime.toLocalDate());
    }

    @Override
    protected void addDateTimeIntoMap(MapSqlParameterSource map, Meal meal) {
        map.addValue("date_time", Timestamp.valueOf(meal.getDateTime()));
    }
}
