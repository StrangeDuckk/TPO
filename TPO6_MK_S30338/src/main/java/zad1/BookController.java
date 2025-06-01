package zad1;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/books")
public class BookController extends HttpServlet {
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
    	// ------------- tworzenie tabeli -------------
    	createDatabase.createBooksTableIfNotExists(path);
    	
    	if(request.getParameter("title") == null) {
    		List<Book> allBooks = dto.getAllBooks(path);
    		
    		request.setAttribute("books", allBooks);
    		request.getRequestDispatcher("/index.jsp").forward(request, response);
    		return;
    	}
    	
    	System.out.println("Book controller:" + path);
    	
        String title = request.getParameter("title");
        List<Book> books = dto.searchBooksByTitle(title,path);

        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        out.println("<html><head><meta charset='UTF-8'><title>Wyniki</title></head><body>");
        out.println("<h1>Wyniki wyszukiwania:</h1>");
        if (books.isEmpty()) {
            out.println("<p>Brak wyników dla tytułu: <b>" + title + "</b></p>");
        } else {
            out.println("<ul>");
            for(Book book : books) {
				out.println("<li><t1> Tytuł: " + book.getTitle() + "</t1></li>");
                out.println("<li> Id: " + book.getId() + "</li>");
                out.println("<li> Autor: " + book.getAuthor() + "</li>");
            }
            out.println("</ul>");
        }
        out.println("<a href='index.jsp'>Powrót</a>");
        out.println("</body></html>");
    }
}