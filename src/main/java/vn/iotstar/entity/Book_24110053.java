package vn.iotstar.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "books")
public class Book_24110053 {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer bookid;
    private Integer isbn;
    @Column(length = 200)
    private String title;
    @Column(length = 100)
    private String publisher;
    @Column(precision = 6, scale = 2)
    private BigDecimal price;
    @Column(columnDefinition = "text")
    private String description;
    @Column(name = "publish_date")
    private LocalDate publishDate;
    @Column(name = "cover_image", length = 100)
    private String coverImage;
    private Integer quantity;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "book_author",
            joinColumns = @JoinColumn(name = "bookid"),
            inverseJoinColumns = @JoinColumn(name = "author_id"))
    private Set<Author_24110053> authors = new LinkedHashSet<>();

    public Integer getBookid() { return bookid; }
    public void setBookid(Integer bookid) { this.bookid = bookid; }
    public Integer getIsbn() { return isbn; }
    public void setIsbn(Integer isbn) { this.isbn = isbn; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getPublisher() { return publisher; }
    public void setPublisher(String publisher) { this.publisher = publisher; }
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public LocalDate getPublishDate() { return publishDate; }
    public void setPublishDate(LocalDate publishDate) { this.publishDate = publishDate; }
    public String getCoverImage() { return coverImage; }
    public void setCoverImage(String coverImage) { this.coverImage = coverImage; }
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    public Set<Author_24110053> getAuthors() { return authors; }
    public void setAuthors(Set<Author_24110053> authors) { this.authors = authors; }
}
