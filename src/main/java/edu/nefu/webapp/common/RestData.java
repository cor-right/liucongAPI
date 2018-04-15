package edu.nefu.webapp.common;

/**
 * Rest Data Template
 */
public class RestData {

    private int code;
    private String message;
    private Object data;

    {
        this.code = 0;
        this.message = "";
        this.data = null;
    }

    // constructors
    public RestData() {}

    public RestData(Object data) {
        this.data = data;
    }

    public RestData(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public RestData(int code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    // getter and setters
    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
