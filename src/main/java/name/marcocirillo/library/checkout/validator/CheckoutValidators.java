package name.marcocirillo.library.checkout.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CheckoutValidators {
    private final InventoryValidator inventoryValidator;
    private final MaxCheckoutsValidator maxCheckoutsValidator;

    @Autowired
    public CheckoutValidators(
            InventoryValidator inventoryValidator,
            MaxCheckoutsValidator maxCheckoutsValidator
    ) {
        this.inventoryValidator = inventoryValidator;
        this.maxCheckoutsValidator = maxCheckoutsValidator;
    }

    public InventoryValidator inventoryValidator() {
        return inventoryValidator;
    }

    public MaxCheckoutsValidator maxCheckoutsValidator() {
        return maxCheckoutsValidator;
    }
}
