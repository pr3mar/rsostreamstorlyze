package com.rsostream.storlyze.util;

public class ResourceNotAvailableException extends Exception {

    private static final long serialVersionUID = -2176454997137524534L;

    public ResourceNotAvailableException() {
        super();
    }

    /**
     * @param message the message for this exception
     */
    public ResourceNotAvailableException(String message) {
        super(message);
    }

    /**
     * @param cause the root cause
     */
    public ResourceNotAvailableException(Throwable cause) {
        super(cause);
    }

    /**
     * @param message the message for this exception
     * @param cause   the root cause
     */
    public ResourceNotAvailableException(String message, Throwable cause) {
        super(message, cause);
    }
}
