package vn.iotstar.services;

import vn.iotstar.entity.*;
import java.util.List;
import java.util.Optional;

public interface LibraryService_24110053 {
    List<Book_24110053> books(int page, int size);
    int bookPages(int size);
    Optional<Book_24110053> book(int id);
    void saveBook(Book_24110053 book, Integer authorId);
    void deleteBook(int id);
    List<Author_24110053> authors(int page, int size);
    List<Author_24110053> allAuthors();
    int authorPages(int size);
    Optional<Author_24110053> author(int id);
    void saveAuthor(Author_24110053 author);
    void deleteAuthor(int id);
    List<Rating_24110053> ratings(int bookId);
    void rate(int userId, int bookId, int value, String reviewText);
}
