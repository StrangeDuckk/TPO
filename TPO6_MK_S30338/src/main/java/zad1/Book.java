package zad1;

import java.util.ArrayList;

public class Book {
    private int id;
    private String title;
    private String author;
    private static ArrayList<Book> bookList = new ArrayList<>();
    private static int autoinc = 0;

    public Book(String title, String author) {
        this.id = autoinc;
        autoinc ++;
        this.title = title;
        this.author = author;
        bookList.add(this);
    }
    public Book(int id, String title, String author) { //tylko dla book dto do tworzenia nowej ksiazki ktore istnieje w bazie
        this.id = id;
        this.title = title;
        this.author = author;
    }

    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public static ArrayList<Book> getBookList() {return bookList;}
    public static void clearBookList() {bookList.clear();}
}