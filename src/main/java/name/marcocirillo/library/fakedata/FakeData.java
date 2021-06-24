package name.marcocirillo.library.fakedata;

import org.immutables.value.Value;

import java.time.OffsetDateTime;

@Value.Immutable
public interface FakeData {
    String getTitle();
    String getAuthor();
    String getIsbn();
    String getGenre();
    int getStock();
    String getAccountName();
    String getAccountEmail();
    boolean isAvailable();
    boolean isReturned();
    OffsetDateTime getDue();
}

