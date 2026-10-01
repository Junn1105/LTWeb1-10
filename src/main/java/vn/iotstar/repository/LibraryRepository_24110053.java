package vn.iotstar.repository;

import vn.iotstar.entity.*;
import java.util.List;
import java.util.Optional;

public interface LibraryRepository_24110053 {
    List<Book_24110053> findBooks(int page, int size);
    long countBooks();
    Optional<Book_24110053> findBook(int id);
    void saveBook(Book_24110053 book, Integer authorId);
    void deleteBook(int id);

    List<Author_24110053> findAuthors(int page, int size);
    List<Author_24110053> findAllAuthors();
    long countAuthors();
    Optional<Author_24110053> findAuthor(int id);
    void saveAuthor(Author_24110053 author);
    void deleteAuthor(int id);

    Optional<User_24110053> findUserByEmail(String email);
    void saveUser(User_24110053 user);
    void updateLastLogin(int id);

    List<Rating_24110053> findRatingsByBook(int bookId);
    void saveRating(int userId, int bookId, byte rating, String reviewText);
}
