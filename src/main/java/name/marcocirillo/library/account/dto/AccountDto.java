package name.marcocirillo.library.account.dto;

import org.immutables.value.Value;

import java.util.UUID;

@Value.Immutable
public interface AccountDto {
    UUID getId();
    String getName();
    String getEmail();
}
