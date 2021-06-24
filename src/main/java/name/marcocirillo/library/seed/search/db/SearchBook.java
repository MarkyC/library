package name.marcocirillo.library.seed.search.db;

import com.vladmihalcea.hibernate.type.array.ListArrayType;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@TypeDefs({
        @TypeDef(
                name = "list-array",
                typeClass = ListArrayType.class
        )
})
@Entity
@Immutable
@Table(name = "search_book")
public class SearchBook {
    @Column
    private UUID authorId;

    @Column
    private String authorName;

    @Id
    @Column
    private UUID id;

    @Column
    private String title;

    @Column(columnDefinition="bpchar(13)")
    private String isbn;

    @Type(type = "list-array")
    @Column(
            name = "category_ids",
            columnDefinition = "uuid[]"
    )
    private List<UUID> categoryIds;

    @Column
    private Boolean available;

    @Column
    private Integer inventoryLevel;

    public SearchBook() {
    }

    public SearchBook(UUID authorId, String authorName, UUID id, String title, String isbn, Boolean available, Integer inventoryLevel) {
        this(authorId, authorName, id, title, isbn, Collections.emptyList(), available, inventoryLevel);
    }

    public SearchBook(UUID authorId, String authorName, UUID id, String title, String isbn, List<UUID> categoryIds, Boolean available, Integer inventoryLevel) {
        this.authorId = authorId;
        this.authorName = authorName;
        this.id = id;
        this.title = title;
        this.isbn = isbn;
        this.categoryIds = categoryIds;
        this.available = available;
        this.inventoryLevel = inventoryLevel;
    }

    public UUID getAuthorId() {
        return authorId;
    }

    public String getAuthorName() {
        return authorName;
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

    public List<UUID> getCategoryIds() {
        return categoryIds;
    }

    public Boolean getAvailable() {
        return available;
    }

    public Integer getInventoryLevel() {
        return inventoryLevel;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof SearchBook && ((SearchBook) o).getIsbn().equals(isbn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(isbn);
    }

    @Override
    public String toString() {
        return "SearchBook{" +
                "authorId=" + authorId +
                ", authorName='" + authorName + '\'' +
                ", id=" + id +
                ", title='" + title + '\'' +
                ", isbn='" + isbn + '\'' +
                ", categoryIds=" + categoryIds +
                ", available=" + available +
                ", inventoryLevel=" + inventoryLevel +
                '}';
    }
}
