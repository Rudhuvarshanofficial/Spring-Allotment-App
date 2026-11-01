
package com.tnea.counselling.ErrorHandling;

public class CounsellingException extends RuntimeException {

    public enum ErrorType {
        INVALID_DATA,
        CONFLICT
    }

    private final ErrorType errorType;

    public CounsellingException(String message) {
        this(message, ErrorType.INVALID_DATA);
    }

    public CounsellingException(String message, ErrorType errorType) {
        super(message);

        if (errorType == null) {
            throw new IllegalArgumentException(
                    "Counselling error type cannot be null."
            );
        }

        this.errorType = errorType;
    }

    public ErrorType getErrorType() {
        return errorType;
    }
}
