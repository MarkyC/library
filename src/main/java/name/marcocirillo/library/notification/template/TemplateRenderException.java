package name.marcocirillo.library.notification.template;

import name.marcocirillo.library.base.exception.ErrorCode;
import name.marcocirillo.library.base.exception.ErrorCodeException;

public class TemplateRenderException extends ErrorCodeException {

    public TemplateRenderException(ErrorCode errorCode) {
        super(errorCode);
    }

    public TemplateRenderException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    public TemplateRenderException(ErrorCode errorCode, String message, Throwable cause) {
        super(errorCode, message, cause);
    }

    public TemplateRenderException(ErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
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
            return "templateRender/"+this.name();
        }

        @Override
        public String getDescription() {
            return this.description;
        }
    }
}
