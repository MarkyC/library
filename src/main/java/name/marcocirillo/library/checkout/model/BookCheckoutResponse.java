package name.marcocirillo.library.checkout.model;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

import java.time.OffsetDateTime;
import java.util.UUID;

@Value.Immutable
@JsonSerialize(as = ImmutableBookCheckoutResponse.class)
@JsonDeserialize(as = ImmutableBookCheckoutResponse.class)
public interface BookCheckoutResponse {
    UUID getId();
    UUID getBookId();
    OffsetDateTime getDue();
}
