import entity.Content;
import entity.DingdingMessage;
import enums.MessageType;

class DingdingMessageHelperTest {

    @org.junit.jupiter.api.Test
    void testSendDingdingMessageInfo() {
        DingdingMessage msg = new DingdingMessage();
        msg.setText(new Content(MessageType.INFO, "测试消息"));
        DingdingMessageHelper.SendDingdingMessage(msg);
    }

    @org.junit.jupiter.api.Test
    void testSendDingdingMessageException() {
        Exception runExc = new RuntimeException("异常消息");
        DingdingMessageHelper.SendDingdingMessage(runExc);
    }
}