package zad1;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/authors")
public class AuthorController extends HttpServlet {
    private final LibraryDTO dto = new LibraryDTO();
    
    static {
    	
        try {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
            System.out.println("Sterownik Derby załadowany");
        } catch (ClassNotFoundException e) {
            System.out.println("Sterownik Derby NIE ZAŁADOWANY!");
            e.printStackTrace();
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    	String path = "jdbc:derby:" + System.getProperty("user.dir") + "/booksDatabase;create=true";   

        String authorParam = request.getParameter("imie");
        List<Book> books = dto.searchBooksByAuthor(authorParam, path);

        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        out.println("<html><head><meta charset='UTF-8'><title>Wyniki</title></head><body>");
        out.println("<h1>Wyniki wyszukiwania autora:</h1>");
        if (books.isEmpty()) {
            out.println("<p>Brak wyników dla autora: <b>" + authorParam + "</b></p>");
        } else {
            out.println("<ul>");
            for (Book book : books) {
                out.println("<li><b>Tytuł:</b> " + book.getTitle() + " — <b>Autor:</b> " + book.getAuthor() + "</li>");
            }
            out.println("</ul>");
        }
        out.println("<a href='index.jsp'>Powrót</a>");
        out.println("</body></html>");
    }
}
