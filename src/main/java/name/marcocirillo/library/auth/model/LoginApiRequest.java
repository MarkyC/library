package name.marcocirillo.library.auth.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

@Value.Immutable
@JsonSerialize(as = ImmutableLoginApiRequest.class)
@JsonDeserialize(as = ImmutableLoginApiRequest.class)
public abstract class LoginApiRequest {
    public abstract String getAccountId();
}
