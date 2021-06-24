package name.marcocirillo.library.checkout.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import name.marcocirillo.library.base.BaseApiResponse;
import org.immutables.value.Value;

import java.util.Collection;


@Value.Immutable
@JsonSerialize(as = ImmutableCheckoutApiResponse.class)
@JsonDeserialize(as = ImmutableCheckoutApiResponse.class)
public abstract class CheckoutApiResponse extends BaseApiResponse {
    abstract Collection<BookCheckoutResponse> getCheckouts();
}
