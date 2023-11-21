package ru.clevertec.servlettask.exception;

public class IncorrectRequestDataException extends RuntimeException{
    public IncorrectRequestDataException(String message) {
        super(message);
    }

    public IncorrectRequestDataException(String message, Throwable cause) {
        super(message, cause);
    }
}
