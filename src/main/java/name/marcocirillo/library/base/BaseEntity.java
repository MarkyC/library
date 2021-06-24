package name.marcocirillo.library.base;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import java.time.OffsetDateTime;

@MappedSuperclass
public class BaseEntity {
    @Column
//    @Column(name = "created", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private OffsetDateTime created;
    @Column
//    @Column(name = "updated", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private OffsetDateTime updated;

    public OffsetDateTime getCreated() {
        return created;
    }

    public void setCreated(OffsetDateTime created) {
        this.created = created;
    }

    public OffsetDateTime getUpdated() {
        return updated;
    }

    public void setUpdated(OffsetDateTime updated) {
        this.updated = updated;
    }

    @PrePersist
    protected void onCreate() {
        updated = created = OffsetDateTime.now();
    }
}
