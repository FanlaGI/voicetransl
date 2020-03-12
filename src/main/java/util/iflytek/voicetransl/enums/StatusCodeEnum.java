package util.iflytek.voicetransl.enums;

public enum StatusCodeEnum {
    STATUS_TASK_CREATE_SUCCESS(0,"任务创建成功"),

    STATUS_UPLOAD_SUCCESS(1,"音频上传完成"),

    STATUS_MEGER_SUCCESS(2,"音频合并完成"),

    STATUS_TRANSCRIPTION_ING(3,"音频转写中"),

    STATUS_TRANSCRIPTION_RESULT_ING(4,"转写结果处理中"),

    STATUS_TRANSCRIPTION_SUCCESS(5,"转写完成"),

    STATUS_TRANSCRIPTION_UPLOAD_SUCCESS(9,"转写结果上传完成");

    private int code;
    private String message;

    StatusCodeEnum(int code,String message) {
        this.code = code;
        this.message = message;
    }

    public int code() {
        return code;
    }

    public String message(){
        return message;
    }

    public static StatusCodeEnum select(int code) {
        for (StatusCodeEnum statusCodeEnum : StatusCodeEnum.values()) {
            if (statusCodeEnum.code() == code) {
                return statusCodeEnum;
            }
        }
        return null;
    }
}
