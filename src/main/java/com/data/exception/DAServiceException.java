package com.data.exception;

import lombok.Data;


/**
 * The type Service exception.
 */
@Data
public class DAServiceException extends RuntimeException {
    private String message;

    private String code;

    /**
     * Constructs a new runtime exception with {@code null} as its
     * detail message.  The cause is not initialized, and may subsequently be
     * initialized by a call to {@link #initCause}.
     * @param message the message
     * @param code    the code
     */
    public DAServiceException(String message, String code) {
        this.message = message;
        this.code = code;
    }

    /**
     * Instantiates a new Service exception.
     * @param message the message
     */
    public DAServiceException(String message) {
        this.message = message;
        this.code = code;
    }

    /**
     * Constructs a new runtime exception with the specified cause and a
     * detail message of <tt>(cause==null ? null : cause.toString())</tt>
     * (which typically contains the class and detail message of
     * <tt>cause</tt>).  This constructor is useful for runtime exceptions
     * that are little more than wrappers for other throwables.
     * @param cause   the cause (which is saved for later retrieval by the              {@link #getCause()} method).  (A <tt>null</tt> value is              permitted, and indicates that the cause is nonexistent or              unknown.)
     * @param message the message
     * @param code    the code
     * @since 1.4
     */
    public DAServiceException(Throwable cause, String message, String code) {
        super(cause);
        this.message = message;
        this.code = code;
    }
}
