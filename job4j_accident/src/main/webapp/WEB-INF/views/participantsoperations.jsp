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
    <title>Participants-operations</title>
</head>
<body>
<div class="container">
    <blockquote class="blockquote text-right">
        <p><a href="${pageContext.servletContext.contextPath}/">Cancel</a></p>
    </blockquote>
    <h3>Operations with participants</h3>
    <br><br>
    <table class="table table-bordered">
        <tbody>
        <tr valign="top">
            <td>Name</td>
            <td>${accident.name}</td>
        </tr>
        <tr valign="top">
            <td>Address</td>
            <td>${accident.address}</td>
        </tr>
        <tr valign="top">
            <td>Description</td>
            <td>${accident.desc}</td>
        </tr>
        </tbody>
    </table>
    <div class="form-group">
        <h4>Participants:</h4>
    </div>
    <table class="table">
        <thead class="thead-light">
        <tr>
            <th>№</th>
            <th>Name</th>
            <th>Phone</th>
            <th>Passport data</th>
            <th>Address</th>
            <th>Status</th>
            <th></th>
            <th></th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${accident.participants}" var="participant" varStatus="loop">
            <tr>
                <td>${loop.index + 1}</td>
                <td>${participant.name}</td>
                <td>${participant.phone}</td>
                <td>${participant.passportData}</td>
                <td>${participant.address}</td>
                <td>${participant.status}</td>
                <td>
                    <form action='${pageContext.servletContext.contextPath}/edit-participant' method='get'>
                        <input type='hidden' name='id' value="${participant.id}"/>
                        <input type='submit' value='Edit'/>
                    </form>
                </td>
                <td>
                    <form action='${pageContext.servletContext.contextPath}/delete-participant' method='post'>
                        <input type='hidden' name='id' value="${participant.id}"/>
                        <input type='submit' value='Delete'/>
                    </form>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
    <form action='${pageContext.servletContext.contextPath}/add-participant' method='get'>
        <input type='submit' value='Add participant'/>
    </form>
    <form action='${pageContext.servletContext.contextPath}/complete-operation' method='post'>
        <input type='submit' value='Finish'/>
    </form>
</div>
</body>
</html>