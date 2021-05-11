package entity;

import enums.MessageType;
import lombok.Data;

@Data
public class Content {
    private String content;
    private MessageType messageType;

    public Content(MessageType messageType, String content) {
        this.content = String.format("消息通知[%s]: %s", messageType.getText(), content);
        this.messageType = messageType;
    }
}
