package ru.clevertec.servlettask.domain;

public class Response {
    private Object body;
    private int code;
    private String type;

    public Response(Object body, int code, String type) {
        this.body = body;
        this.code = code;
        this.type = type;
    }

    public Object getBody() {
        return body;
    }

    public int getCode() {
        return code;
    }

    public String getType() {
        return type;
    }
}
