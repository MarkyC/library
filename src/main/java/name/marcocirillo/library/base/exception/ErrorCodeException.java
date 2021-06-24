package name.marcocirillo.library.base.exception;

public class ErrorCodeException extends RuntimeException {
    private ErrorCode errorCode;

    public ErrorCodeException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public ErrorCodeException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public ErrorCodeException(ErrorCode errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public ErrorCodeException(ErrorCode errorCode, Throwable cause) {
        super(cause);
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    /**
     * Global error codes.
     *
     * It's almost always a better idea to have a package specific
     * ErrorCodeException + ErrorCodes instead of sticking error codes in here.
     */
    public enum ErrorCodes implements ErrorCode {
        UNSUPPORTED_MEDIA_TYPE("Unsupported Media Type. You might need the header: Content-Type: application/json"),
        UNKNOWN("Unknown Error."),
        ;
        private String description;

        ErrorCodes(String description) {
            this.description = description;
        }

        @Override
        public String getCode() {
            return this.name();
        }

        @Override
        public String getDescription() {
            return this.description;
        }
    }
}
