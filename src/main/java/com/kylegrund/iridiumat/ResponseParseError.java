package com.kylegrund.iridiumat;

/**
 * An exception which is thrown when there is an error parsing a command response.
 */
public class ResponseParseError extends Exception {
    /**
     * Initializes a new instance of the ResponseParseError class.
     * @param reason The reason for the exception.
     */
    public ResponseParseError(String reason) {
        super(reason);
    }

    /**
     * Initializes a new instance of the ResponseParseError class.
     * @param reason The reason for the exception.
     * @param cause The cause of the exception.
     */
    public ResponseParseError(String reason, Throwable cause) {
        super(reason, cause);
    }
}
