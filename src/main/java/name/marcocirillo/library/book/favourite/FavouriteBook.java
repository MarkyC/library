package name.marcocirillo.library.book.favourite;

import org.hibernate.annotations.Immutable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;
import java.util.UUID;

@Entity
@Immutable
@Table(name = "favourite_book")
public class FavouriteBook {
    @Id
    @Column
    private UUID id;

    @Column
    private UUID accountId;

    @Column
    private UUID bookId;

    @Column
    private Integer timesCheckedOut;

    public FavouriteBook() {
    }

    public FavouriteBook(UUID id, UUID accountId, UUID bookId, Integer timesCheckedOut) {
        this.id = id;
        this.accountId = accountId;
        this.bookId = bookId;
        this.timesCheckedOut = timesCheckedOut;
    }

    public UUID getId() {
        return id;
    }

    public UUID getAccountId() {
        return accountId;
    }

    public UUID getBookId() {
        return bookId;
    }

    public Integer getTimesCheckedOut() {
        return timesCheckedOut;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FavouriteBook that = (FavouriteBook) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "FavouriteBook{" +
                "id=" + id +
                ", accountId=" + accountId +
                ", bookId=" + bookId +
                ", timesCheckedOut=" + timesCheckedOut +
                '}';
    }
}
