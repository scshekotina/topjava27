<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<html>
<head>
    <title><spring:message code="meal.meal"/></title>
    <link rel="stylesheet" href=<spring:url value="/resources/css/style.css"/>>
</head>
<body>
<section>
    <h3><a href=<spring:url value="/"/>><spring:message code="meal.home"/></a></h3>
    <hr>
    <jsp:useBean id="meal" type="ru.javawebinar.topjava.model.Meal" scope="request"/>
    <h2><spring:message code="${meal.isNew() ? 'meal.createMeal' : 'meal.editMeal'}"/></h2>

    <form method="post" action=<spring:url value="/meals/save"/>>
        <input type="hidden" name="id" value="${meal.id}">
        <dl>
            <dt><spring:message code="meal.datetime"/></dt>
            <dd><input type="datetime-local" value="${meal.dateTime}" name="dateTime" required></dd>
        </dl>
        <dl>
            <dt><spring:message code="meal.description"/></dt>
            <dd><input type="text" value="${meal.description}" size=40 name="description" required></dd>
        </dl>
        <dl>
            <dt><spring:message code="meal.calories"/></dt>
            <dd><input type="number" value="${meal.calories}" name="calories" required></dd>
        </dl>
        <button type="submit"><spring:message code="meal.save"/></button>
        <button onclick="window.history.back()" type="button"><spring:message code="meal.cancel"/></button>
    </form>
</section>
</body>
</html>
