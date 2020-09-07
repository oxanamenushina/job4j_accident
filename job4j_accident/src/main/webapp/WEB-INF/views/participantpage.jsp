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
    <blockquote class="blockquote text-right">
        <p><a href="${pageContext.servletContext.contextPath}/">Back</a></p>
    </blockquote>
    <h1>Participant</h1>
    <table class="table table-bordered">
        <tbody>
        <tr valign="top">
            <td>Name</td>
            <td>${participant.name}</td>
        </tr>
        <tr valign="top">
            <td>Status</td>
            <td>${participant.status.name()}</td>
        </tr>
        <tr valign="top">
            <td>Phone</td>
            <td>${participant.phone}</td>
        </tr>
        <tr valign="top">
            <td>Passport data</td>
            <td>${participant.passportData}</td>
        </tr>
        <tr valign="top">
            <td>Address</td>
            <td>${participant.address}</td>
        </tr>
        </tbody>
    </table>
</div>
</body>
</html>