package name.marcocirillo.library.seed.search.exception;

import name.marcocirillo.library.base.exception.ErrorCode;
import name.marcocirillo.library.base.exception.ErrorCodeException;

public class SearchSeedException extends ErrorCodeException {
    public SearchSeedException(ErrorCode errorCode) {
        this(errorCode, errorCode.getDescription());
    }

    public SearchSeedException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    public SearchSeedException(ErrorCode errorCode, String message, Throwable cause) {
        super(errorCode, message, cause);
    }

    public SearchSeedException(ErrorCode errorCode, Throwable cause) {
        this(errorCode, errorCode.getDescription(), cause);
    }

    public enum ErrorCodes implements ErrorCode {
        CREATE_SEARCH_INDEX_FAILED("Failed to create the search index."),
        SEARCH_SEED_TIMEOUT("Seeding the search index timed out."),
        SEARCH_SEED_INTERRUPTED("Seeding the search index was interrupted."),
        UNKNOWN("Unknown Error."),
        ;
        private String description;

        ErrorCodes(String description) {
            this.description = description;
        }

        @Override
        public String getCode() {
            return "searchSeed/"+this.name();
        }

        @Override
        public String getDescription() {
            return this.description;
        }
    }
}
