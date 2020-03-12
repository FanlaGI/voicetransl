package util.iflytek.voicetransl.controller;

import org.apache.poi.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import util.iflytek.voicetransl.service.TransService;
import util.iflytek.voicetransl.util.ResponseResult;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;

@RestController
public class TransController {

    @Autowired
    private TransService transService;

    @Value(value="${tempdir}")
    private String tempdir;

    /**
     * 上传文件到讯飞 获取taskId
     * @param file
     * @return
     */
    @CrossOrigin
    @PostMapping("/genTask")
    public ResponseResult genTask(MultipartFile file) {
        ResponseResult result = new ResponseResult();
        if (file != null) {
            String fileName = file.getOriginalFilename().substring(0,file.getOriginalFilename().lastIndexOf("."));
            String localPath = tempdir + file.getOriginalFilename();
            boolean res = transService.saveLocal(file,localPath);//文件存本地
            if (res) {
                result = transService.genTask(localPath);
                result.setBody(fileName);
            }
        } else {
            result.setSuccess(false);
            result.setMessage("文件为空！");
        }
        return  result;
    }

    /**
     * 获取转写进度
     * @param taskId
     * @return
     */
    @CrossOrigin
    @GetMapping("/getTransProgress")
    public ResponseResult getTransProgress(String taskId) {
        return transService.getTransProgress(taskId);
    }

    /**
     * 下载转写文本
     * @param taskId
     * @param fileName
     * @param request
     * @param response
     */
    @CrossOrigin
    @GetMapping("/doExport")
    public void doExport(String taskId,String fileName,HttpServletRequest request, HttpServletResponse response) {
        if (StringUtils.isEmpty(fileName)) fileName = taskId;
        String resp = transService.getTransResult(taskId);
        ServletOutputStream out = null;
        BufferedOutputStream buffOut = null;
        response.setContentType("text/plain");
        try {
            response.addHeader("Content-Disposition","attachment;filename=" + new String(fileName.getBytes("UTF-8"), "ISO-8859-1")+".txt");
            out = response.getOutputStream();
            buffOut = new BufferedOutputStream(out);
            buffOut.write(resp.getBytes("GB2312"));
            buffOut.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) out.close();
                if (buffOut != null) buffOut.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 整合版
     * @param file
     * @return
     */
    @CrossOrigin
    @PostMapping("/doTrans")
    public ResponseResult doTrans(MultipartFile file) {
        ResponseResult result = new ResponseResult();
        result.setSuccess(false);
        if (file != null) {
            String fileName = file.getOriginalFilename();
            String localPath = tempdir + file.getOriginalFilename();
            boolean res = transService.saveLocal(file,localPath);
            if (res) {
                String taskId = transService.doTrans(localPath);
                if  (taskId != null) {
                    result.setSuccess(true);
                    result.setMessage(taskId);
                    result.setBody(fileName);
                }
            }
        }
        return  result;
    }
}
