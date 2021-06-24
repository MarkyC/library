package name.marcocirillo.library.checkout.dto;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

import java.time.OffsetDateTime;
import java.util.UUID;

@Value.Immutable
@JsonSerialize(as = ImmutableBookCheckoutResponseDto.class)
@JsonDeserialize(as = ImmutableBookCheckoutResponseDto.class)
public interface BookCheckoutResponseDto {
    UUID getId();
    UUID getBookId();
    OffsetDateTime getDue();
}
