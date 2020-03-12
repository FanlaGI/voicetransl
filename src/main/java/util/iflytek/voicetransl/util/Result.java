package util.iflytek.voicetransl.util;

public class Result {
    private String bg;//开始时间
    private String ed;//结束时间
    private String onebest;//内容
    private String speaker;//说话人序号

    public String getBg() {
        return bg;
    }

    public void setBg(String bg) {
        this.bg = bg;
    }

    public String getEd() {
        return ed;
    }

    public void setEd(String ed) {
        this.ed = ed;
    }

    public String getOnebest() {
        return onebest;
    }

    public void setOnebest(String onebest) {
        this.onebest = onebest;
    }

    public String getSpeaker() {
        return speaker;
    }

    public void setSpeaker(String speaker) {
        this.speaker = speaker;
    }
}
