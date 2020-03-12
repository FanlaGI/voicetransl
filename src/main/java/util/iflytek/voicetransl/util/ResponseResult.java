package util.iflytek.voicetransl.util;

import java.io.Serializable;

public class ResponseResult<T> implements Serializable {
    private static final long serialVersionUID = 1L;
    private boolean success;
    private String code;
    private String message;
    private T body;

    public ResponseResult() {
    }

    public static long getSerialVersionUID() {
        return 1L;
    }

    public boolean isSuccess() {
        return this.success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getBody() {
        return this.body;
    }

    public void setBody(T body) {
        this.body = body;
    }
}

