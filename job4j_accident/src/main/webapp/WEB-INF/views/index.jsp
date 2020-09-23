<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.min.css" integrity="sha384-9aIt2nRpC12Uk9gS9baDl411NQApFmC26EwAOH8WgZl5MYYxFfc+NcPb1dKGj7Sk" crossorigin="anonymous">
    <!-- Optional JavaScript -->
    <!-- jQuery first, then Popper.js, then Bootstrap JS -->
    <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js" integrity="sha384-DfXdz2htPH0lsSSs5nCTpuj/zy4C+OGpamoFVy38MVBnE+IbbVYUew+OrCXaRkfj" crossorigin="anonymous"></script>
    <script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.0/dist/umd/popper.min.js" integrity="sha384-Q6E9RHvbIyZFJoft+2mJbHaEWldlvI9IOYy5n3zV9zzTtmI3UksdQRVvoxMfooAo" crossorigin="anonymous"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/js/bootstrap.min.js" integrity="sha384-OgVRvuATP1z7JjHLkuOU7Xw704+h835Lr+6QL9UvYjZE3Ipu6Tp75j7Bh/kR0JKI" crossorigin="anonymous"></script>
    <title>Accident</title>
</head>
<body>
<div class="container">
    <h1>Accidents</h1>
    <table class="table">
        <thead class="thead-light">
        <tr>
            <th scope="col">№</th>
            <th scope="col">Title</th>
            <th scope="col">Address</th>
            <th scope="col">Description</th>
            <th scope="col">Participants</th>
            <th scope="col">Creation date</th>
            <th scope="col"></th>
            <th scope="col"></th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${accidents}" var="accident" varStatus="loop">
            <tr valign="top">
                <td>${loop.index + 1}</td>
                <td>${accident.name}</td>
                <td>${accident.address}</td>
                <td>${accident.desc}</td>
                <td>
                    <c:forEach items="${accident.participants}" var="participant">
                        <p><a href="${pageContext.servletContext.contextPath}/participant-page?pid=${participant.id}&aid=${accident.id}">
                                ${participant.name}
                        </a></p>
                    </c:forEach>
                </td>
                <td>${accident.created}</td>
                <td>
                    <form action='${pageContext.servletContext.contextPath}/edit-accident' method='get'>
                        <input type='hidden' name='id' value="${accident.id}"/>
                        <input type='submit' value='Edit'/>
                    </form>
                </td>
                <td>
                    <form action='${pageContext.servletContext.contextPath}/delete-accident' method='post'>
                        <input type='hidden' name='id' value="${accident.id}"/>
                        <input type='submit' value='Delete'/>
                    </form>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
    <form action='${pageContext.servletContext.contextPath}/add-accident'>
        <input type='submit' value='Add new accident'/>
    </form>
</div>
</body>
</html>