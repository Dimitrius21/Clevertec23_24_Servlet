package ru.clevertec.servlettask.domain;

public class ExceptionDto {
    private String message;
    private String code;

    public ExceptionDto(String message, String code) {
        this.message = message;
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public String getCode() {
        return code;
    }
}
