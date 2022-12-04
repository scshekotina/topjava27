package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Repository
@Transactional(readOnly = true)
public class JdbcUserRepository implements UserRepository {

    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final SimpleJdbcInsert insertUser;

    private final ResultSetExtractor<List<User>> userListExtractor = rs -> {
        Map<Integer, User> userMap = new LinkedHashMap<>();
        while (rs.next()) {
            int id = rs.getInt("id");
            User user = userMap.get(id);
            if (user == null) {
                user = new User();
                user.setId(id);
                user.setName(rs.getString("name"));
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password"));
                user.setEnabled(rs.getBoolean("enabled"));
                user.setCaloriesPerDay(rs.getInt("calories_per_day"));
                user.setRegistered(rs.getDate("registered"));
                String role = rs.getString("role");
                user.setRoles(role != null && !role.isEmpty() ? List.of(Role.valueOf(role)) : Collections.emptyList());
                userMap.put(id, user);
            } else {
                user.getRoles().add(Role.valueOf(rs.getString("role")));
            }
        }
        return userMap.values().stream().toList();
    };

    @Autowired
    public JdbcUserRepository(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.insertUser = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");

        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    @Transactional
    public User save(User user) {
        BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(user);
        if (user.isNew()) {
            Number newKey = insertUser.executeAndReturnKey(parameterSource);
            user.setId(newKey.intValue());
        } else if (namedParameterJdbcTemplate.update("UPDATE users SET name=:name, email=:email, password=:password, " +
                        "registered=:registered, enabled=:enabled, calories_per_day=:caloriesPerDay WHERE id=:id",
                parameterSource) == 0) {
            return null;
        } else {
            jdbcTemplate.update("DELETE FROM user_roles WHERE user_id=?", user.id());
        }
        List<Role> roles = user.getRoles().stream().toList();
        jdbcTemplate.batchUpdate("INSERT INTO user_roles (role, user_id) VALUES (?, ?)",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setString(1, roles.get(i).name());
                        ps.setInt(2, user.id());
                    }

                    @Override
                    public int getBatchSize() {
                        return roles.size();
                    }
                });
        return user;
    }

    @Override
    @Transactional
    public boolean delete(int id) {
        return jdbcTemplate.update("DELETE FROM users WHERE id=?", id) != 0;
    }

    @Override
    public User get(int id) {
        List<User> users = jdbcTemplate.query("SELECT u.id, u.name, u.email, u.password, u.enabled, u.calories_per_day," +
                "u.registered, ur.role AS role FROM users u LEFT JOIN user_roles ur on u.id = ur.user_id " +
                "WHERE u.id=?", userListExtractor, id);
        return DataAccessUtils.singleResult(users);
    }

    @Override
    public User getByEmail(String email) {
//        return jdbcTemplate.queryForObject("SELECT * FROM users WHERE email=?", ROW_MAPPER, email);
        List<User> users = jdbcTemplate.query("SELECT u.id, u.name, u.email, u.password, u.enabled, " +
                "u.calories_per_day, u.registered, ur.role AS role FROM users u LEFT JOIN user_roles ur " +
                "on u.id = ur.user_id " +
                "WHERE email=?", userListExtractor, email);
        return DataAccessUtils.singleResult(users);
    }

    @Override
    public List<User> getAll() {
        return jdbcTemplate.query("SELECT u.id, u.name, u.email, u.password, u.enabled, u.calories_per_day, " +
                "u.registered, ur.role AS role " +
                "FROM users u " +
                "LEFT JOIN user_roles ur on u.id = ur.user_id " +
                "ORDER BY name, email", userListExtractor);
    }
}
