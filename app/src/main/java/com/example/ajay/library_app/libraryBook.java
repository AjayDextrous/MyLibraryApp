/**
 * Created by ajay-5674 on 03/07/17.
 */
package com.example.ajay.library_app;

public class libraryBook {
    private int id;
    private String Name;
    private String Author;
    private String Publisher;
    private int nos;
    private int total;


    public libraryBook() {

    }

    public libraryBook(int id, String Name, String Author, String Publisher, int nos) {
        this.id = id;
        this.Name = Name;
        this.Author = Author;
        this.Publisher = Publisher;
        this.nos = nos;
        this.total = nos;
    }

    public libraryBook(int id, String Name, String Author, String Publisher, int nos, int total) {
        this.id = id;
        this.Name = Name;
        this.Author = Author;
        this.Publisher = Publisher;
        this.nos = nos;
        this.total = total;
    }

    public libraryBook(libraryBook book) {
        this.id = book.getId();
        this.Name = book.getName();
        this.Author = book.getAuthor();
        this.Publisher = book.getPublisher();
        this.nos = book.getNos();
        this.total = book.getTotal();
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public void setAuthor(String Author) {
        this.Author = Author;
    }

    public void setPublisher(String Publisher) {
        this.Publisher = Publisher;
    }

    public void setNos(int nos) {
        this.nos = nos;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getId() {
        return this.id;
    }

    public int getNos() {
        return this.nos;
    }

    public String getName() {
        return this.Name;
    }

    public String getAuthor() {
        return this.Author;
    }

    public String getPublisher() {
        return this.Publisher;
    }

    public int getTotal() {
        return this.total;
    }
}

