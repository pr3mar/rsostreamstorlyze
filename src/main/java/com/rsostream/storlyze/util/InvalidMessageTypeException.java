package com.rsostream.storlyze.util;

public class InvalidMessageTypeException extends InvalidMessageException {

    private static final long serialVersionUID = -7010144121954291699L;

    public InvalidMessageTypeException() {
        super();
    }

    /**
     * @param message the message for this exception
     */
    public InvalidMessageTypeException(String message) {
        super(message);
    }

    /**
     * @param cause the root cause
     */
    public InvalidMessageTypeException(Throwable cause) {
        super(cause);
    }

    /**
     * @param message the message for this exception
     * @param cause   the root cause
     */
    public InvalidMessageTypeException(String message, Throwable cause) {
        super(message, cause);
    }
}
