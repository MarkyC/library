package name.marcocirillo.library.web;

import name.marcocirillo.library.auth.exception.AuthException;
import name.marcocirillo.library.base.exception.ApiErrorResponse;
import name.marcocirillo.library.base.exception.ErrorCodeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static name.marcocirillo.library.base.exception.ErrorCodeException.ErrorCodes.UNKNOWN;
import static name.marcocirillo.library.base.exception.ErrorCodeException.ErrorCodes.UNSUPPORTED_MEDIA_TYPE;

/**
 * Global Error Handling. These handle 500s and 401s
 * Most 400s (except 401s) are expected to be returned from their respective Controllers.
 */
@ControllerAdvice
public class ErrorHandlingControllerAdvice {
    private static final Logger LOG = LoggerFactory.getLogger(ErrorHandlingControllerAdvice.class.getName());

    @ExceptionHandler({Exception.class})
    public ResponseEntity<ApiErrorResponse> handleException(Exception exception) {
        LOG.error("There was an unhandled exception.", exception);
        return ResponseEntity.internalServerError()
                .body(new ApiErrorResponse(UNKNOWN.getCode(), UNKNOWN.getDescription()));
    }

    @ExceptionHandler({HttpMediaTypeException.class})
    public ResponseEntity<ApiErrorResponse> handleHttpMediaTypeException(HttpMediaTypeException exception) {
        return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                .body(new ApiErrorResponse(UNSUPPORTED_MEDIA_TYPE.getCode(), UNSUPPORTED_MEDIA_TYPE.getDescription()));
    }

    @ExceptionHandler({ErrorCodeException.class})
    public ResponseEntity<ApiErrorResponse> handleErrorCodeExceptionException(ErrorCodeException exception) {
        return ResponseEntity.internalServerError()
                .body(new ApiErrorResponse(exception.getErrorCode()));
    }

    @ExceptionHandler({AuthException.class})
    public ResponseEntity<ApiErrorResponse> handleAuthException(AuthException exception) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ApiErrorResponse(exception.getErrorCode()));
    }
}
