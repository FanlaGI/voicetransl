package util.iflytek.voicetransl.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.iflytek.msp.cpdb.lfasr.client.LfasrClientImp;
import com.iflytek.msp.cpdb.lfasr.exception.LfasrException;
import com.iflytek.msp.cpdb.lfasr.model.LfasrType;
import com.iflytek.msp.cpdb.lfasr.model.Message;
import com.iflytek.msp.cpdb.lfasr.model.ProgressStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import util.iflytek.voicetransl.util.ResponseResult;
import util.iflytek.voicetransl.util.Result;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

@Service
public class TransService {

    static Logger logger = LoggerFactory.getLogger(TransService.class);

    private LfasrClientImp lfasrClientImp;

    /**
     * 上传文件存本地
     * @param file
     * @param localFile
     * @return
     */
    public boolean saveLocal(MultipartFile file, String localFile) {
        boolean res = false;
        File saveDir = new File(localFile);
        if (!saveDir.getParentFile().exists()) {
            saveDir.getParentFile().mkdirs();
        }
        try {
            file.transferTo(saveDir);
            res = true;
        } catch (IOException e) {
            System.out.println(e);
            logger.error("上传文件存本地失败：" + e.toString());
        }
        return res;
    }

    /**
     * 获取客户端
     * @return
     */
    private LfasrClientImp getClient() {
        if (lfasrClientImp == null) {
            try {
                lfasrClientImp = LfasrClientImp.initLfasrClient();
            } catch (LfasrException e) {
                Message initMsg = JSON.parseObject(e.getMessage(), Message.class);
                logger.error("创建客户端异常,code：" + initMsg.getErr_no() + ",msg：" + initMsg.getFailed());
                logger.error(e.toString());
            }
        }
        return lfasrClientImp;
    }

    /**
     * 上传文件到讯飞 获取taskId
     * @param localFile
     * @return
     */
    public ResponseResult genTask(String localFile) {
        ResponseResult resp = new ResponseResult();
        resp.setSuccess(false);

        String taskId = null;
        LfasrClientImp lc = getClient();

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("speaker_number", "1");//对话人数
        params.put("pd", "edu");//edu领域

        try {
            Message uploadMsg = lc.lfasrUpload(localFile, LfasrType.LFASR_STANDARD_RECORDED_AUDIO, params);
            int ok = uploadMsg.getOk();
            if (ok == 0) {
                taskId = uploadMsg.getData();
                resp.setSuccess(true);
                resp.setMessage(taskId);
                logger.info("创建任务成功,taskId：" + taskId);
            } else {
                resp.setMessage(uploadMsg.getErr_no() + ":" + uploadMsg.getFailed());
                logger.info("创建任务失败,code：" + uploadMsg.getErr_no() + ",msg：" + uploadMsg.getFailed());
            }
        } catch (LfasrException e) {
            Message uploadMsg = JSON.parseObject(e.getMessage(), Message.class);
            logger.error("上传创建任务异常,code：" + uploadMsg.getErr_no() + ",msg：" + uploadMsg.getFailed());
            logger.error(e.toString());
            resp.setMessage("上传创建任务异常！");
        }
        return resp;
    }

    /**
     * 获取转写进度
     * @param taskId
     * @return
     */
    public ResponseResult getTransProgress(String taskId) {
        ResponseResult resp = new ResponseResult();
        LfasrClientImp lc = getClient();
        try {
            Message progressMsg = lc.lfasrGetProgress(taskId);
            if (progressMsg.getOk() != 0) {
                resp.setSuccess(false);
                resp.setMessage(progressMsg.getFailed());
                logger.info("转写失败,code：" + progressMsg.getErr_no() + ",msg：" + progressMsg.getFailed());
            } else {
                ProgressStatus progressStatus = JSON.parseObject(progressMsg.getData(), ProgressStatus.class);
                if (progressStatus.getStatus() == 9) {
                    resp.setSuccess(true);
                    resp.setMessage("转写完成，可以下载！");
                    logger.info("转写完成taskTId:" + taskId);
                } else {
                    resp.setSuccess(false);
                    resp.setMessage(progressStatus.getDesc());
                }
            }
        } catch (LfasrException e) {
            Message progressMsg = JSON.parseObject(e.getMessage(), Message.class);
            logger.error("获取进度异常,code：" + progressMsg.getErr_no() + ",msg：" + progressMsg.getFailed());
            resp.setSuccess(false);
            resp.setMessage("处理请求异常！");
        }
        return resp;
    }

    /**
     * 获取转写数据
     * @param taskId
     * @return
     */
    public String getTransResult(String taskId) {
        String resultJson = "";
        LfasrClientImp lc = getClient();
        try {
            Message resultMsg = lc.lfasrGetResult(taskId);
            if (resultMsg.getOk() != 0) {
                logger.info("获取结果失败，ecode：" + resultMsg.getErr_no() + ",msg：" + resultMsg.getFailed());
                return resultJson;
            } else {
                resultJson = resultMsg.getData();
            }
        } catch (LfasrException e) {
            // 获取结果异常处理，解析异常描述信息
            Message resultMsg = JSON.parseObject(e.getMessage(), Message.class);
            logger.error("获取结果异常，ecode：" + resultMsg.getErr_no() + ",msg：" + resultMsg.getFailed());
        }
        JSONArray jsonArray = JSONArray.parseArray(resultJson);    //com.alibaba.fastjson.JSONArray
        List<Result> list = jsonArray.toJavaList(Result.class);
        String content = "";
        for (Result res : list) {
            content += res.getOnebest();
        }
        logger.info(content);
        return content;
    }

    /**
     * 整合版
     * @param localFile
     * @return
     */
    public String doTrans(String localFile) {
        String taskId = null;
        LfasrClientImp lc = getClient();

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("speaker_number", "1");
        params.put("pd", "edu");

        try {
            Message uploadMsg = lc.lfasrUpload(localFile, LfasrType.LFASR_STANDARD_RECORDED_AUDIO, params);
            int ok = uploadMsg.getOk();
            if (ok == 0) {
                taskId = uploadMsg.getData();
                logger.info("创建任务成功,taskId：" + taskId);
            } else {
                logger.info("创建任务失败,code：" + uploadMsg.getErr_no() + ",msg：" + uploadMsg.getFailed());
                return taskId;
            }
        } catch (LfasrException e) {
            Message uploadMsg = JSON.parseObject(e.getMessage(), Message.class);
            logger.error("上传创建任务异常,code：" + uploadMsg.getErr_no() + ",msg：" + uploadMsg.getFailed());
            logger.error(e.toString());
            return taskId;
        }

        while (true) {
            try {
                Thread.sleep(10 * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                Message progressMsg = lc.lfasrGetProgress(taskId);
                if (progressMsg.getOk() != 0) {
                    logger.info("转写失败,code：" + progressMsg.getErr_no() + ",msg：" + progressMsg.getFailed());
                    return taskId;
                } else {
                    ProgressStatus progressStatus = JSON.parseObject(progressMsg.getData(), ProgressStatus.class);
                    if (progressStatus.getStatus() == 9) {
                        logger.info("转写完成taskTId:" + taskId);
                        break;
                    } else {
                        logger.info("转写中，10s后自动请求:" + taskId + ", status:" + progressStatus.getDesc());
                        continue;
                    }
                }
            } catch (LfasrException e) {
                Message progressMsg = JSON.parseObject(e.getMessage(), Message.class);
                logger.error("获取进度异常,code：" + progressMsg.getErr_no() + ",msg：" + progressMsg.getFailed());
                return taskId;
            }
        }
        return taskId;
    }
}
