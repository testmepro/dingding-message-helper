package entity;

import com.alibaba.fastjson.JSON;
import lombok.Data;

;

@Data
public class DingdingMessage {
    private final String msgtype = "text";// text: 文本消息,photo: 图片消息,markdown: markdown消息
    private Content text;

    public String toJson() {
        return JSON.toJSONString(this);
    }
}


