package vn.iotstar.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "author")
public class Author_24110053 {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "author_id")
    private Integer authorId;
    @Column(name = "author_name", length = 100)
    private String authorName;
    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;
    @ManyToMany(mappedBy = "authors")
    private Set<Book_24110053> books = new LinkedHashSet<>();

    public Integer getAuthorId() { return authorId; }
    public void setAuthorId(Integer authorId) { this.authorId = authorId; }
    public String getAuthorName() { return authorName; }
    public void setAuthorName(String authorName) { this.authorName = authorName; }
    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(LocalDate dateOfBirth) { this.dateOfBirth = dateOfBirth; }
    public Set<Book_24110053> getBooks() { return books; }
    public void setBooks(Set<Book_24110053> books) { this.books = books; }
}
