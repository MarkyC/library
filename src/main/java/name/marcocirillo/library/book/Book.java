package name.marcocirillo.library.book;

import name.marcocirillo.library.base.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "book")
public class Book extends BaseEntity {
    @Id
    @Column
    private UUID id;

    @Column
    private String title;

    @Column(columnDefinition="bpchar(13)")
    private String isbn;

    @Column
    private UUID authorId;

    @Column
    private Boolean available;

    @Column
    private Integer stock;

    public Book() {
    }

    public Book(UUID id, String title, String isbn, UUID authorId, Boolean available, Integer stock) {
        this.id = id;
        this.title = title;
        this.isbn = isbn;
        this.authorId = authorId;
        this.available = available;
        this.stock = stock;
    }

    public UUID getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getIsbn() {
        return isbn;
    }

    public UUID getAuthorId() {
        return authorId;
    }

    public boolean getAvailable() {
        return Boolean.TRUE.equals(available);
    }

    public int getStock() {
        return stock == null
            ? 0
            : stock;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return Objects.equals(id, book.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", isbn='" + isbn + '\'' +
                ", authorId=" + authorId +
                ", available=" + available +
                ", stock=" + stock +
                '}';
    }
}
