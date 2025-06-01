package zad1;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class createDatabase {
	public static void createBooksTableIfNotExists(String path) {
    	String jdbcURL = "jdbc:derby:" + System.getProperty("user.dir") + "/booksDatabase;create=true";
    	System.out.println("create database path: " + path);
    	
        try {
        	// ---------------- laczenie z baza ----------------
            Connection connection = DriverManager.getConnection(path);
            System.out.println("database created");

            Statement statement = connection.createStatement();

            // ---------------- usuwanie starej tabeli -----------
            try {
            	Book.clearBookList();
                statement.executeUpdate("DROP TABLE Books");
                System.out.println("Tabela Books usunięta");
            } catch (SQLException e) {
                if (!e.getSQLState().equals("42Y55")) { // 42Y55: Table does not exist
                    throw e; 
                }
            }
            
            // ---------------- tworzenie tabeli ----------------
            String createTableSQL = "CREATE TABLE Books ("
                    + "id INT NOT NULL GENERATED ALWAYS AS IDENTITY PRIMARY KEY, "
                    + "title VARCHAR(128), "
                    + "author VARCHAR(128))";

            statement.execute(createTableSQL);
            System.out.println("Table Books created");

            //  --------------- dodawanie danych ----------------
            ArrayList<String> inserts = new ArrayList<String>();
            inserts.add("INSERT INTO Books (title, author) VALUES ('Mitologia, cz. I Grecja', 'Jan Parandowski')");
            inserts.add("INSERT INTO Books (title, author) VALUES ('Iliada', 'Homer')");
            inserts.add("INSERT INTO Books (title, author) VALUES ('Antygona', 'Sofokles')");
            inserts.add("INSERT INTO Books (title, author) VALUES ('Lament świętokrzyski', 'nieznany')");
            inserts.add("INSERT INTO Books (title, author) VALUES ('Rozmowa Mistrza Polikarpa ze Śmiercią', 'nieznany')");
            inserts.add("INSERT INTO Books (title, author) VALUES ('Pieśń o Rolandzie', 'nieznany')");
            inserts.add("INSERT INTO Books (title, author) VALUES ('Makbet', 'William Szekspir')");
            inserts.add("INSERT INTO Books (title, author) VALUES ('Skąpiec', 'Molier')");
            inserts.add( "INSERT INTO Books (title, author) VALUES ('Wybrana satyra', 'Ignacy Krasicki')");
            inserts.add( "INSERT INTO Books (title, author) VALUES ('Romantyczność', 'Adam Mickiewicz')");
            inserts.add( "INSERT INTO Books (title, author) VALUES ('Dziady cz. III', 'Adam Mickiewicz')");
            inserts.add( "INSERT INTO Books (title, author) VALUES ('Lalka', 'Bolesław Prus')");
            inserts.add( "INSERT INTO Books (title, author) VALUES ('Potop', 'Henryk Sienkiewicz')");
            inserts.add( "INSERT INTO Books (title, author) VALUES ('Zbrodnia i kara', 'Fiodor Dostojewski')");
            inserts.add( "INSERT INTO Books (title, author) VALUES ('Wesele', 'Stanisław Wyspiański')");
            inserts.add( "INSERT INTO Books (title, author) VALUES ('Chłopi', 'Władysław Stanisław Reymont')");
            inserts.add( "INSERT INTO Books (title, author) VALUES ('Przedwiośnie', 'Stefan Żeromski')");
            inserts.add( "INSERT INTO Books (title, author) VALUES ('Ferdydurke', 'Witold Gombrowicz')");
            inserts.add( "INSERT INTO Books (title, author) VALUES ('Proszę państwa do gazu', 'Tadeusz Borowski')");
            inserts.add( "INSERT INTO Books (title, author) VALUES ('Inny świat', 'Gustaw Herling-Grudziński')");
            inserts.add( "INSERT INTO Books (title, author) VALUES ('Zdążyć przed Panem Bogiem', 'Hanna Krall')");
            inserts.add( "INSERT INTO Books (title, author) VALUES ('Dżuma', 'Albert Camus')");
            inserts.add( "INSERT INTO Books (title, author) VALUES ('Rok 1984', 'George Orwell')");
            inserts.add( "INSERT INTO Books (title, author) VALUES ('Tango', 'Sławomir Mrożek')");
            inserts.add( "INSERT INTO Books (title, author) VALUES ('Górą „Edek”', 'Marek Nowakowski')");
            inserts.add( "INSERT INTO Books (title, author) VALUES ('Miejsce', 'Andrzej Stasiuk')");
            inserts.add( "INSERT INTO Books (title, author) VALUES ('Profesor Andrews w Warszawie', 'Olga Tokarczuk')");
            inserts.add( "INSERT INTO Books (title, author) VALUES ('Podróże z Herodotem', 'Ryszard Kapuściński')");
            inserts.add( "INSERT INTO Books (title, author) VALUES ('Quo Vadis', 'Henryk Sienkiewicz')");

            ArrayList<Book> ksiazki = new ArrayList<Book>();
            ksiazki.add(new Book("Mitologia, cz. I Grecja", "Jan Parandowski"));
            ksiazki.add(new Book("Iliada", "Homer"));
            ksiazki.add(new Book("Antygona", "Sofokles"));
            ksiazki.add(new Book("Lament świętokrzyski", "nieznany"));
            ksiazki.add(new Book("Rozmowa Mistrza Polikarpa ze Śmiercią", "nieznany"));
            ksiazki.add(new Book("Pieśń o Rolandzie", "nieznany"));
            ksiazki.add(new Book("Makbet", "William Szekspir"));
            ksiazki.add(new Book("Skąpiec", "Molier"));
            ksiazki.add( new Book("Wybrana satyra", "Ignacy Krasicki"));
            ksiazki.add( new Book("Wybrane ballady, w tym Romantyczność); Dziady cz. III", "Adam Mickiewicz"));
            ksiazki.add( new Book("Lalka", "Bolesław Prus"));
            ksiazki.add( new Book("Potop", "Henryk Sienkiewicz"));
            ksiazki.add( new Book("Zbrodnia i kara", "Fiodor Dostojewski"));
            ksiazki.add( new Book("Wesele", "Stanisław Wyspiański"));
            ksiazki.add( new Book("Chłopi", "Władysław Stanisław Reymont"));
            ksiazki.add( new Book("Przedwiośnie", "Stefan Żeromski"));
            ksiazki.add( new Book("Ferdydurke", "Witold Gombrowicz"));
            ksiazki.add( new Book("Proszę państwa do gazu", "Tadeusz Borowski"));
            ksiazki.add( new Book("Inny świat", "Gustaw Herling-Grudziński"));
            ksiazki.add( new Book("Zdążyć przed Panem Bogiem", "Hanna Krall"));
            ksiazki.add( new Book("Dżuma", "Albert Camus"));
            ksiazki.add( new Book("Rok 1984", "George Orwell"));
            ksiazki.add( new Book("Tango", "Sławomir Mrożek"));
            ksiazki.add( new Book("Górą „Edek”", "Marek Nowakowski"));
            ksiazki.add( new Book("Miejsce", "Andrzej Stasiuk"));
            ksiazki.add( new Book("Profesor Andrews w Warszawie", "Olga Tokarczuk"));
            ksiazki.add( new Book("Podróże z Herodotem", "Ryszard Kapuściński"));
            ksiazki.add( new Book("Quo Vadis", "Henryk Sienkiewicz"));

            for(String insert : inserts) {
            	statement.executeUpdate(insert);
            }

            System.out.println("Books inserted");

            //connection.close();

        } catch (SQLException e) {
        	if (e.getSQLState().equals("X0Y32")) 
        	{
                System.out.println("Tabela BOOKS już istnieje.");
            } else 
            {
                e.printStackTrace();
            }
        }
    }
}