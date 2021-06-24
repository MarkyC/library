package name.marcocirillo.library.checkout.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import name.marcocirillo.library.base.BaseApiResponse;
import org.immutables.value.Value;

import java.util.List;
import java.util.UUID;

@Value.Immutable
@JsonSerialize(as = ImmutableBookInventoryErrorApiResponse.class)
@JsonDeserialize(as = ImmutableBookInventoryErrorApiResponse.class)
public abstract class BookInventoryErrorApiResponse extends BaseApiResponse {
    public abstract List<BookInventoryError> getErrors();

    @Value.Immutable
    @JsonSerialize(as = ImmutableBookInventoryError.class)
    @JsonDeserialize(as = ImmutableBookInventoryError.class)
    public interface BookInventoryError {
        UUID getBookId();
        int getInventoryLevel();
        Reason getReason();
    }

    public enum Reason {
        NOT_AVAILABLE,
        OUT_OF_STOCK,
        NOT_ENOUGH_STOCK,
        ;
    }
}
