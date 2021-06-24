package name.marcocirillo.library.checkout.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

import java.util.UUID;

@Value.Immutable
@JsonSerialize(as = ImmutableBookCheckoutRequestDto.class)
@JsonDeserialize(as = ImmutableBookCheckoutRequestDto.class)
public interface BookCheckoutRequestDto {
    UUID getId();
    int getQuantity();
}
