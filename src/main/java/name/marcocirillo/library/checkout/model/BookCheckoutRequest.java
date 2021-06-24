package name.marcocirillo.library.checkout.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

import java.util.UUID;

@Value.Immutable
@JsonSerialize(as = ImmutableBookCheckoutRequest.class)
@JsonDeserialize(as = ImmutableBookCheckoutRequest.class)
public interface BookCheckoutRequest {
    UUID getId();
    int getQuantity();
}
