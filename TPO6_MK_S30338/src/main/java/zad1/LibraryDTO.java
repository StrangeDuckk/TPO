package zad1;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LibraryDTO {

    public List<Book> searchBooksByTitle(String title,String dbURL) {
    	System.out.println("bookDTO path: " + dbURL);
    	
    	List<Book> results = new ArrayList();

        try (Connection conn = DriverManager.getConnection(dbURL);
        	
             PreparedStatement stmt = conn.prepareStatement("SELECT id, title, author FROM books WHERE LOWER(title) = ?")) {

            stmt.setString(1, title.toLowerCase());
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
            	int id = rs.getInt("id");
                String bookTitle = rs.getString("title");
                String author = rs.getString("author");

                results.add(new Book(id, bookTitle, author));
            }
        } catch (SQLException e) {
            e.printStackTrace(); 
        }

        return results;
    }
    
    public List<Book> getAllBooks(String dbURL){
    	List<Book> result = new ArrayList<>();
    	
    	createDatabase.createBooksTableIfNotExists(dbURL);
    	
    	try {
    		Connection conn = DriverManager.getConnection(dbURL);
    		
    		PreparedStatement statement = conn.prepareStatement("Select id, title, author FROM books");
    		
    		ResultSet rs = statement.executeQuery();
    		
    		while(rs.next()) {
    			int id = rs.getInt("id");
    			String title = rs.getString("title");
    			String author = rs.getString("author");
    			result.add(new Book(id,title,author));
    		}
    	}
    	catch(SQLException e) {
    		e.printStackTrace();
    	}
    	
    	return result;
    }

	public List<Book> searchBooksByAuthor(String authorParam, String dbURL) {
		List<Book> results = new ArrayList<>();
		
		try {
			Connection conn = DriverManager.getConnection(dbURL);
			
			PreparedStatement stmt = conn.prepareStatement("Select id, title, author FROM books WHERE LOWER(author) = ?");
			
			stmt.setString(1, authorParam.toLowerCase());
			ResultSet rs = stmt.executeQuery();
			
			while(rs.next()) {
				int id = rs.getInt("id");
				String title = rs.getString("title");
				String name = rs.getString("author");
				
				results.add(new Book(id,title, name));
			}
		}catch(SQLException e) {
				e.printStackTrace();
		}
					
		return results;
	}
}
