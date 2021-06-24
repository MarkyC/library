package name.marcocirillo.library.inventory;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.annotations.Immutable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;
import java.util.UUID;

@Entity
@Immutable
@Table(name = "book_inventory")
public class BookInventory {
    @Id
    @Column
    private UUID id;

    @Column
    private Integer inventoryLevel;

    public UUID getId() {
        return id;
    }

    public Integer getInventoryLevel() {
        return inventoryLevel;
    }

    public BookInventory() {
    }

    public BookInventory(UUID id, Integer inventoryLevel) {
        this.id = id;
        this.inventoryLevel = inventoryLevel;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookInventory that = (BookInventory) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "BookInventory{" +
                "id=" + id +
                ", inventoryLevel=" + inventoryLevel +
                '}';
    }
}
