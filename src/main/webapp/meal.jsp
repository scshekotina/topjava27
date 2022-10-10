<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:useBean id="meal" scope="request" type="ru.javawebinar.topjava.model.Meal"/>
<html>
<head>
    <title>Add new meal</title>
</head>
<body>
<form method="POST" name="formAddMeal">
    <label><input type="text" readonly="readonly" name="id" hidden
           value="<c:out value="${meal.id}" />"/></label>
    <label>DateTime : &emsp;<input name="datetime" required type="datetime-local"
                            value="<c:out value="${meal.dateTime}"/>"/></label><br/><br/>
    <label>Description :&ensp;<input name="description" required type="text"
                              value="<c:out value="${meal.description}"/>"/></label><br/><br/>
    <label>Calories : &ensp;&emsp;<input name="calories" required type="number"
                                  value="<c:out value="${meal.calories}"/>"/></label><br/><br/>
    <input
            type="submit" value="Save"/>
    <input type="button" value="Cancel" onClick='location.href="meals"'/>
</form>
</body>
</html>