package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.dao.IMealDao;
import ru.javawebinar.topjava.dao.MealDaoInMemory;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(MealServlet.class);
    private static String EDIT_FORM = "/meal.jsp";
    private static String LIST = "/meals.jsp";
    private IMealDao dao;

    public MealServlet() {
        super();
        dao = new MealDaoInMemory();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String forward = "";
        String action = request.getParameter("action");
        if (action == null) {
            log.debug("show meals list");
            forward = LIST;
            request.setAttribute("meals", getAllMealTos());
        } else if (action.equalsIgnoreCase("delete")) {
            log.debug("delete meal");
            int id = Integer.parseInt(request.getParameter("id"));
            dao.delete(id);
            forward = LIST;
            request.setAttribute("meals", getAllMealTos());
        } else if (action.equalsIgnoreCase("edit")) {
            log.debug("edit meal");
            forward = EDIT_FORM;
            int id = Integer.parseInt(request.getParameter("id"));
            request.setAttribute("meal", dao.get(id));
        } else if (action.equalsIgnoreCase("insert")) {
            log.debug("insert meal");
            forward = EDIT_FORM;
        }
        request.getRequestDispatcher(forward).forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        try {
            LocalDateTime dateTime = MealsUtil.format(request.getParameter("datetime"));
            String description = request.getParameter("description");
            int calories = Integer.parseInt(request.getParameter("calories"));

            String idString = request.getParameter("id");
            int mealId = (idString == null || idString.isEmpty()) ? 0 : Integer.parseInt(idString);

            Meal meal = new Meal(mealId, dateTime, description, calories );
            dao.update(meal);
        } catch (NumberFormatException | DateTimeParseException e) {
            e.printStackTrace();
        }

        request.setAttribute("meals", getAllMealTos());
        request.getRequestDispatcher(LIST).forward(request, response);
    }

    private List<MealTo> getAllMealTos() {
        return MealsUtil.filteredByStreams(dao.getAll(), null, null, MealsUtil.caloriesPerDay);
    }
}
