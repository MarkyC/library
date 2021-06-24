package name.marcocirillo.library.checkout;

import name.marcocirillo.library.base.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "checkout")
public class Checkout extends BaseEntity {
    @Id
    @Column
    private UUID id;

    @Column
    private UUID bookId;

    @Column
    private UUID accountId;

    @Column
    private Boolean returned;

    @Column
    private OffsetDateTime due;

    /** Default constructor needed for Hibernate */
    public Checkout() {
    }

    public Checkout(UUID bookId, UUID accountId, OffsetDateTime due) {
        this(UUID.randomUUID(), bookId, accountId, false, due);
    }

    public Checkout(UUID id, UUID bookId, UUID accountId, Boolean returned, OffsetDateTime due) {
        this.id = id;
        this.bookId = bookId;
        this.accountId = accountId;
        this.returned = returned;
        this.due = due;
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

    public UUID getAccountId() {
        return accountId;
    }

    public void setAccountId(UUID accountId) {
        this.accountId = accountId;
    }

    public Boolean getReturned() {
        return Boolean.TRUE.equals(returned);
    }

    public void setReturned(Boolean returned) {
        this.returned = Boolean.TRUE.equals(returned);
    }

    public OffsetDateTime getDue() {
        return due;
    }

    public void setDue(OffsetDateTime due) {
        this.due = due;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Checkout checkout = (Checkout) o;
        return Objects.equals(id, checkout.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Checkout{" +
                "id=" + id +
                ", bookId=" + bookId +
                ", accountId=" + accountId +
                ", returned=" + returned +
                ", due=" + due +
                '}';
    }
}
