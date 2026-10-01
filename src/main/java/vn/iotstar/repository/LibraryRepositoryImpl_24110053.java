package vn.iotstar.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import vn.iotstar.entity.*;
import vn.iotstar.util.JpaUtil_24110053;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Consumer;

public class LibraryRepositoryImpl_24110053 implements LibraryRepository_24110053 {
    @Override
    public List<Book_24110053> findBooks(int page, int size) {
        try (EntityManager em = JpaUtil_24110053.entityManager()) {
            List<Integer> ids = em.createQuery("select b.bookid from Book_24110053 b order by b.bookid", Integer.class)
                    .setFirstResult((page - 1) * size).setMaxResults(size).getResultList();
            if (ids.isEmpty()) return List.of();
            return em.createQuery("select b from Book_24110053 b left join fetch b.authors where b.bookid in :ids order by b.bookid", Book_24110053.class)
                    .setParameter("ids", ids).getResultList();
        }
    }
    @Override public long countBooks() {
        try (EntityManager em = JpaUtil_24110053.entityManager()) {
            return em.createQuery("select count(b) from Book_24110053 b", Long.class).getSingleResult();
        }
    }
    @Override public Optional<Book_24110053> findBook(int id) {
        try (EntityManager em = JpaUtil_24110053.entityManager()) {
            return em.createQuery("select b from Book_24110053 b left join fetch b.authors where b.bookid=:id", Book_24110053.class)
                    .setParameter("id", id).getResultStream().findFirst();
        }
    }
    @Override public void saveBook(Book_24110053 input, Integer authorId) {
        transaction(em -> {
            Book_24110053 book = input.getBookid() == null ? input : em.find(Book_24110053.class, input.getBookid());
            if (book != input) copyBook(input, book);
            book.getAuthors().clear();
            if (authorId != null && authorId > 0) book.getAuthors().add(em.getReference(Author_24110053.class, authorId));
            if (book.getBookid() == null) em.persist(book);
        });
    }
    @Override public void deleteBook(int id) {
        transaction(em -> {
            em.createQuery("delete from Rating_24110053 r where r.book.bookid=:id").setParameter("id", id).executeUpdate();
            em.createNativeQuery("delete from book_author where bookid=?1").setParameter(1, id).executeUpdate();
            Book_24110053 book = em.find(Book_24110053.class, id);
            if (book != null) em.remove(book);
        });
    }
    @Override public List<Author_24110053> findAuthors(int page, int size) {
        try (EntityManager em = JpaUtil_24110053.entityManager()) {
            return em.createQuery("select a from Author_24110053 a order by a.authorId", Author_24110053.class)
                    .setFirstResult((page - 1) * size).setMaxResults(size).getResultList();
        }
    }
    @Override public List<Author_24110053> findAllAuthors() {
        try (EntityManager em = JpaUtil_24110053.entityManager()) {
            return em.createQuery("select a from Author_24110053 a order by a.authorName", Author_24110053.class).getResultList();
        }
    }
    @Override public long countAuthors() {
        try (EntityManager em = JpaUtil_24110053.entityManager()) {
            return em.createQuery("select count(a) from Author_24110053 a", Long.class).getSingleResult();
        }
    }
    @Override public Optional<Author_24110053> findAuthor(int id) {
        try (EntityManager em = JpaUtil_24110053.entityManager()) { return Optional.ofNullable(em.find(Author_24110053.class, id)); }
    }
    @Override public void saveAuthor(Author_24110053 input) {
        transaction(em -> {
            if (input.getAuthorId() == null) em.persist(input);
            else {
                Author_24110053 author = em.find(Author_24110053.class, input.getAuthorId());
                if (author != null) {
                    author.setAuthorName(input.getAuthorName());
                    author.setDateOfBirth(input.getDateOfBirth());
                }
            }
        });
    }
    @Override public void deleteAuthor(int id) {
        transaction(em -> {
            em.createNativeQuery("delete from book_author where author_id=?1").setParameter(1, id).executeUpdate();
            Author_24110053 author = em.find(Author_24110053.class, id);
            if (author != null) em.remove(author);
        });
    }
    @Override public Optional<User_24110053> findUserByEmail(String email) {
        try (EntityManager em = JpaUtil_24110053.entityManager()) {
            return em.createQuery("select u from User_24110053 u where lower(u.email)=lower(:email)", User_24110053.class)
                    .setParameter("email", email).getResultStream().findFirst();
        }
    }
    @Override public void saveUser(User_24110053 user) { transaction(em -> em.persist(user)); }
    @Override public void updateLastLogin(int id) {
        transaction(em -> {
            User_24110053 user = em.find(User_24110053.class, id);
            if (user != null) user.setLastLogin(LocalDateTime.now());
        });
    }
    @Override public List<Rating_24110053> findRatingsByBook(int bookId) {
        try (EntityManager em = JpaUtil_24110053.entityManager()) {
            return em.createQuery("select r from Rating_24110053 r join fetch r.user where r.book.bookid=:id order by r.user.id", Rating_24110053.class)
                    .setParameter("id", bookId).getResultList();
        }
    }
    @Override public void saveRating(int userId, int bookId, byte value, String reviewText) {
        transaction(em -> {
            RatingId_24110053 id = new RatingId_24110053(userId, bookId);
            Rating_24110053 rating = em.find(Rating_24110053.class, id);
            if (rating == null) {
                rating = new Rating_24110053();
                rating.setId(id);
                rating.setUser(em.getReference(User_24110053.class, userId));
                rating.setBook(em.getReference(Book_24110053.class, bookId));
                em.persist(rating);
            }
            rating.setRating(value);
            rating.setReviewText(reviewText == null || reviewText.isBlank() ? null : reviewText.trim());
        });
    }

    private static void copyBook(Book_24110053 from, Book_24110053 to) {
        to.setIsbn(from.getIsbn()); to.setTitle(from.getTitle()); to.setPublisher(from.getPublisher());
        to.setPrice(from.getPrice()); to.setDescription(from.getDescription()); to.setPublishDate(from.getPublishDate());
        to.setCoverImage(from.getCoverImage()); to.setQuantity(from.getQuantity());
    }
    private static void transaction(Consumer<EntityManager> work) {
        EntityManager em = JpaUtil_24110053.entityManager();
        EntityTransaction tx = em.getTransaction();
        try { tx.begin(); work.accept(em); tx.commit(); }
        catch (RuntimeException ex) { if (tx.isActive()) tx.rollback(); throw ex; }
        finally { em.close(); }
    }
}
