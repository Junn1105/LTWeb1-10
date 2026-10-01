package vn.iotstar.services;

import vn.iotstar.entity.*;
import vn.iotstar.repository.LibraryRepositoryImpl_24110053;
import vn.iotstar.repository.LibraryRepository_24110053;
import java.util.List;
import java.util.Optional;

public class LibraryServiceImpl_24110053 implements LibraryService_24110053 {
    private final LibraryRepository_24110053 repository = new LibraryRepositoryImpl_24110053();
    @Override public List<Book_24110053> books(int page, int size) { return repository.findBooks(Math.max(page, 1), size); }
    @Override public int bookPages(int size) { return Math.max(1, (int)Math.ceil(repository.countBooks() / (double)size)); }
    @Override public Optional<Book_24110053> book(int id) { return repository.findBook(id); }
    @Override public void saveBook(Book_24110053 book, Integer authorId) { repository.saveBook(book, authorId); }
    @Override public void deleteBook(int id) { repository.deleteBook(id); }
    @Override public List<Author_24110053> authors(int page, int size) { return repository.findAuthors(Math.max(page, 1), size); }
    @Override public List<Author_24110053> allAuthors() { return repository.findAllAuthors(); }
    @Override public int authorPages(int size) { return Math.max(1, (int)Math.ceil(repository.countAuthors() / (double)size)); }
    @Override public Optional<Author_24110053> author(int id) { return repository.findAuthor(id); }
    @Override public void saveAuthor(Author_24110053 author) { repository.saveAuthor(author); }
    @Override public void deleteAuthor(int id) { repository.deleteAuthor(id); }
    @Override public List<Rating_24110053> ratings(int bookId) { return repository.findRatingsByBook(bookId); }
    @Override public void rate(int userId, int bookId, int value, String text) {
        if (value < 1 || value > 5) throw new IllegalArgumentException("Điểm đánh giá phải từ 1 đến 5");
        repository.saveRating(userId, bookId, (byte)value, text);
    }
}
