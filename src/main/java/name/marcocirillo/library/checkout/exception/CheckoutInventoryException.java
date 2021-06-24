package name.marcocirillo.library.checkout.exception;

import name.marcocirillo.library.checkout.dto.BookInventoryErrorDto;

import java.util.List;

import static name.marcocirillo.library.checkout.exception.CheckoutException.ErrorCodes.NOT_ENOUGH_INVENTORY;

public class CheckoutInventoryException extends CheckoutException {
    private final List<BookInventoryErrorDto> errors;
    public CheckoutInventoryException(List<BookInventoryErrorDto> errors) {
        super(NOT_ENOUGH_INVENTORY);
        this.errors = errors;
    }

    public List<BookInventoryErrorDto> getErrors() {
        return errors;
    }

    @Override
    public String toString() {
        return "CheckoutInventoryException{" +
                "errors=" + errors +
                '}';
    }
}
