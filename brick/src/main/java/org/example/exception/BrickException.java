package org.example.exception;

/**
 * @author chenqian
 * @date 2024/12/26 17:02
 **/
public class BrickException  extends RuntimeException{
    public BrickException(String message, Throwable cause) {
        super(message, cause);
    }

    public BrickException(String message) {
        super(message);
    }
}
