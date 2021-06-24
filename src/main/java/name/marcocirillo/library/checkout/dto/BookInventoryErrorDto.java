package name.marcocirillo.library.checkout.dto;

import org.immutables.value.Value;

import java.util.UUID;

@Value.Immutable
public interface BookInventoryErrorDto {
    @Value.Parameter
    UUID getBookId();
    @Value.Parameter
    int getInventoryLevel();
    @Value.Parameter
    Reason getReason();

    enum Reason {
        NOT_AVAILABLE,
        OUT_OF_STOCK,
        NOT_ENOUGH_STOCK,
        ;
    }
}
