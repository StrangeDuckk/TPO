<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*, java.sql.*, zad1.Book, zad1.LibraryDTO" %>
<%
    String path = "jdbc:derby:" + System.getProperty("user.dir") + "/booksDatabase;create=true";
    LibraryDTO dto = new LibraryDTO();
    List<Book> books = dto.getAllBooks(path);
%>


<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>TPO6_MK_S30338</title>
</head>
<body>

<div class="search-box">
    <h1>Wyszukiwarka książek</h1>
    <form action="${pageContext.request.contextPath}/books" method="get">
        <label for="title">Tytuł książki:</label>
        <input type="text" id="title" name="title" placeholder="np. Lalka" required>
        <input type="submit" value="Szukaj">
    </form>
    <form action="${pageContext.request.contextPath}/authors" method="get">
        <label for="title">Nazwisko autora::</label>
        <input type="text" id="author" name="imie" placeholder="np. Henryk Sienkiewicz" required>
        <input type="submit" value="Szukaj">
    </form>
</div>

<h2>Lista wszystkich książek:</h2>
<ul>
<%
    if (books.isEmpty()) {
%>
    <li>Brak książek w bazie danych.</li>
<%
    } else {
        for (Book book : books) {
%>
    <li><b>Tytuł:</b> <%= book.getTitle() %> — <b>Autor:</b> <%= book.getAuthor() %></li>
<%
        }
    }
%>
</ul>



</body>
</html>