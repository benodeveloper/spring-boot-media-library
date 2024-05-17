package com.benodeveloper.medialibrary.exceptions;

import java.io.IOException;

public class UnreadableFileException extends FileOperationException {
    /**
     * Constructs an {@code FileOperationException} with {@code null}
     * as its error detail message.
     */
    public UnreadableFileException() {
    }

    /**
     * Constructs an {@code FileOperationException} with the specified detail message.
     *
     * @param message The detail message (which is saved for later retrieval
     *                by the {@link #getMessage()} method)
     */
    public UnreadableFileException(String message) {
        super(message);
    }

    /**
     * Constructs an {@code FileOperationException} with the specified detail message
     * and cause.
     *
     * <p> Note that the detail message associated with {@code cause} is
     * <i>not</i> automatically incorporated into this exception's detail
     * message.
     *
     * @param message The detail message (which is saved for later retrieval
     *                by the {@link #getMessage()} method)
     * @param cause   The cause (which is saved for later retrieval by the
     *                {@link #getCause()} method).  (A null value is permitted,
     *                and indicates that the cause is nonexistent or unknown.)
     * @since 1.6
     */
    public UnreadableFileException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs an {@code FileOperationException} with the specified cause and a
     * detail message of {@code (cause==null ? null : cause.toString())}
     * (which typically contains the class and detail message of {@code cause}).
     * This constructor is useful for IO exceptions that are little more
     * than wrappers for other throwables.
     *
     * @param cause The cause (which is saved for later retrieval by the
     *              {@link #getCause()} method).  (A null value is permitted,
     *              and indicates that the cause is nonexistent or unknown.)
     * @since 1.6
     */
    public UnreadableFileException(Throwable cause) {
        super(cause);
    }
}
