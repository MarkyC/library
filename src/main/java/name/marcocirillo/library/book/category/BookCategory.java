package name.marcocirillo.library.book.category;


import name.marcocirillo.library.base.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "book_category")
public class BookCategory extends BaseEntity {
    @Id
    @Column
    private UUID id;

    @Column
    private UUID bookId;

    @Column
    private UUID categoryId;

    public BookCategory() {
    }

    public BookCategory(UUID id, UUID bookId, UUID categoryId) {
        this.id = id;
        this.bookId = bookId;
        this.categoryId = categoryId;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getBookId() {
        return bookId;
    }

    public void setBookId(UUID bookId) {
        this.bookId = bookId;
    }

    public UUID getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(UUID categoryId) {
        this.categoryId = categoryId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookCategory that = (BookCategory) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "BookCategory{" +
                "id=" + id +
                ", bookId=" + bookId +
                ", categoryId=" + categoryId +
                '}';
    }
}
