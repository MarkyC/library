package name.marcocirillo.library.auth.exception;

import name.marcocirillo.library.base.exception.ErrorCode;
import name.marcocirillo.library.base.exception.ErrorCodeException;

public class AuthException extends ErrorCodeException {

    public AuthException(ErrorCode errorCode) {
        super(errorCode);
    }

    public AuthException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    public AuthException(ErrorCode errorCode, String message, Throwable cause) {
        super(errorCode, message, cause);
    }

    public AuthException(ErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }

    public enum ErrorCodes implements ErrorCode {
        INVALID_CREDENTIALS("Invalid username or password."),
        UNAUTHORIZED("The Authorization header was either missing or invalid."),
        UNKNOWN("Unknown Error."),
        ;
        private String description;

        ErrorCodes(String description) {
            this.description = description;
        }

        @Override
        public String getCode() {
            return "auth/"+this.name();
        }

        @Override
        public String getDescription() {
            return this.description;
        }
    }
}
