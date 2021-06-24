package name.marcocirillo.library.checkout.exception;

import name.marcocirillo.library.base.exception.ErrorCode;
import name.marcocirillo.library.base.exception.ErrorCodeException;

public class CheckoutException extends ErrorCodeException {
    public CheckoutException(ErrorCode errorCode) {
        super(errorCode);
    }

    public CheckoutException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    public CheckoutException(ErrorCode errorCode, String message, Throwable cause) {
        super(errorCode, message, cause);
    }

    public CheckoutException(ErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }

    public enum ErrorCodes implements ErrorCode {
        TOO_MANY_CHECKOUTS("There is a limit of 10 checkouts per account."),
        NOT_ENOUGH_INVENTORY("There is not enough inventory to fulfill this checkout request."),
        UNKNOWN("Unknown Error."),
        ;
        private String description;

        ErrorCodes(String description) {
            this.description = description;
        }

        @Override
        public String getCode() {
            return "checkout/"+this.name();
        }

        @Override
        public String getDescription() {
            return this.description;
        }
    }

}
