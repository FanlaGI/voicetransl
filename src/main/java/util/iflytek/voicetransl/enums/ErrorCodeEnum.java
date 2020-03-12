package util.iflytek.voicetransl.enums;

public enum ErrorCodeEnum {

    TRANS_SUCCESS (0,"成功"),

    TRANS_COMM_ERR (26000,"转写内部通用错误"),

    TRANS_CONF_ERR(26100,"转写配置文件错误"),

    TRANS_CONF_KEY_ERR(26101,"转写配置文件app_id/secret_key为空"),

    TRANS_CONF_HOST_ERR(26102,"转写配置文件lfasr_host错误"),

    TRANS_CONF_SIZE_ERR(26103,"转写配置文件file_piece_size错误"),

    TRANS_CONF_SIZE_ERR2(26104,"转写配置文件file_piece_size建议设置10M-30M之间"),

    TRANS_CONF_PATH_ERR(26105,"转写配置文件store_path错误，或目录不可读写"),

    TRANS_PARAM_UPLOAD_ERR(26201,"转写参数上传文件不能为空或文件不存在"),

    TRANS_PARAM_TYPE_ERR(26202,"转写参数类型不能为空"),

    TRANS_PARAM_SIGN_ERR(26203,"转写参数客户端生成签名错误"),

    TRANS_RESUME_PRES_ERR(26301,"转写断点续传持久化文件读写错误"),

    TRANS_RESUME_IO_ERR(26302,"转写断点续传文件夹读写错误"),

    TRANS_RESUME_PROCESS_ERR(26303,"转写恢复断点续传流程错误,请见日志"),

    TRANS_UPLOAD_URL_ERR(26401,"转写上传文件路径错误"),

    TRANS_UPLOAD_TYPE_ERR(26402,"转写上传文件类型不支持错误"),

    TRANS_UPLOAD_SIZE_ERR(26403,"转写本地文件上传超过限定大小500M"),

    TRANS_UPLOAD_IO_ERR(26404,"转写上传文件读取错误"),

    TRANS_HTTP_ERR(26500,"HTTP请求失败"),

    TRANS_GET_VER_ERR(26501,"转写获取版本号接口错误"),

    TRANS_PRE_ERR(26502,"转写预处理接口错误"),

    TRANS_UPLOAD_API_ERR(26503,"转写上传文件接口错误"),

    TRANS_MERGE_ERR(26504,"转写合并文件接口错误"),

    TRANS_GET_PROCESS(26505,"转写获取进度接口错误"),

    TRANS_GET_RESULT_ERR(26506,"转写获取结果接口错误"),

    TRANS_BIZ_ERR(26600,"转写业务通用错误"),

    TRANS_ILLEGAL_INFO(26601,"非法应用信息"),

    TRANS_TASKID_ERR(26602,"任务ID不存在"),

    TRANS_ACCESS_FREQ_ERR(26603,"接口访问频率受限（默认1秒内不得超过20次）"),

    TRANS_GET_RESULT_FREQ_ERR(26604,"获取结果次数超过限制，最多100次"),

    TRANS_TASK_PROCESSING(26605,"任务正在处理中，请稍后重试"),

    TRANS_EMPTY_AUDIO_ERR(26606,"空音频，请检查"),

    TRANS_REQ_PARAM_ERR(26610,"请求参数错误"),

    TRANS_PRE_SIZE_ERR(26621,"预处理文件大小受限（500M）"),

    TRANS_PRE_LENGTH_ERR(26622,"预处理音频时长受限（5小时）"),

    TRANS_PRE_TYPE_ERR(26623,"预处理音频格式受限"),

    TRANS_PRE_NOT_ENOUGH_TIME(26625,"预处理服务时长不足。您剩余的可用服务时长不足，请移步产品页http://www.xfyun.cn/services/lfasr 进行购买或者免费领取"),

    TRANS_FILE_SIZE_ERR(26631,"音频文件大小受限（500M）"),

    TRANS_FILE_LEN_ERR(26632,"音频时长受限（5小时）"),

    TRANS_NOT_ENOUGH_TIME(26633,"音频服务时长不足。您剩余的可用服务时长不足，请移步产品页http://www.xfyun.cn/services/lfasr 进行购买或者免费领"),

    TRANS_DOWNLOAD_ERR(26634,"文件下载失败"),

    TRANS_FILE_LEN_CHECK_ERR(26635,"文件长度校验失败"),

    TRANS_FILE_UPLOAD_ERR(26640,"文件上传失败"),

    TRANS_UPLOAD_SLICE_ERR(26641,"上传分片超过限制"),

    TRANS_SLICE_MEGER_ERR(26642,"分片合并失败"),

    TRANS_CALC_TIME_ERR(26643,"计算音频时长失败,请检查您的音频是否加密或者损坏"),

    TRANS_TYPE_CONV_ERR(26650,"音频格式转换失败,请检查您的音频是否加密或者损坏"),

    TRANS_BILLING_ERR(26660,"计费计量失败"),

    TRANS_RESULT_PARSING_ERR(26670,"转写结果集解析失败"),

    TRANS_ENGINE_ERR(26680,"引擎处理阶段错误");

    private int code;
    private String message;

    ErrorCodeEnum(int code,String message) {
        this.code = code;
        this.message = message;
    }

    public int code() {
        return code;
    }

    public String message(){
        return message;
    }

    public static ErrorCodeEnum select(int code) {
        for (ErrorCodeEnum errorCodeEnum : ErrorCodeEnum.values()) {
            if (errorCodeEnum.code() == code) {
                return errorCodeEnum;
            }
        }
        return null;
    }
}