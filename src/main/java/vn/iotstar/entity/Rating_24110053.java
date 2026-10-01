package vn.iotstar.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "rating")
public class Rating_24110053 {
    @EmbeddedId
    private RatingId_24110053 id;
    @MapsId("userid")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userid")
    private User_24110053 user;
    @MapsId("bookid")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bookid")
    private Book_24110053 book;
    private Byte rating;
    @Column(name = "review_text", columnDefinition = "text")
    private String reviewText;

    public RatingId_24110053 getId() { return id; }
    public void setId(RatingId_24110053 id) { this.id = id; }
    public User_24110053 getUser() { return user; }
    public void setUser(User_24110053 user) { this.user = user; }
    public Book_24110053 getBook() { return book; }
    public void setBook(Book_24110053 book) { this.book = book; }
    public Byte getRating() { return rating; }
    public void setRating(Byte rating) { this.rating = rating; }
    public String getReviewText() { return reviewText; }
    public void setReviewText(String reviewText) { this.reviewText = reviewText; }
}
