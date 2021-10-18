package dingdinghelper.enums;

public enum MessageType {
    EXCEPTION("异常"),
    INFO("消息")
    ;
    private String text;

    MessageType(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
