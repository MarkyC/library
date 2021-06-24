package name.marcocirillo.library.search.exception;

import name.marcocirillo.library.base.exception.ErrorCode;
import name.marcocirillo.library.base.exception.ErrorCodeException;

public class SearchException extends ErrorCodeException {
    public SearchException(ErrorCode errorCode) {
        this(errorCode, errorCode.getDescription());
    }

    public SearchException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    public SearchException(ErrorCode errorCode, String message, Throwable cause) {
        super(errorCode, message, cause);
    }

    public SearchException(ErrorCode errorCode, Throwable cause) {
        this(errorCode, errorCode.getDescription(), cause);
    }

    public enum ErrorCodes implements ErrorCode {
        UNKNOWN("Unknown Error."),
        ;
        private String description;

        ErrorCodes(String description) {
            this.description = description;
        }

        @Override
        public String getCode() {
            return "search/"+this.name();
        }

        @Override
        public String getDescription() {
            return this.description;
        }
    }
}
