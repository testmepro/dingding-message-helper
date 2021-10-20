package dingdinghelper.entity;


import cn.hutool.system.SystemUtil;
import cn.hutool.system.oshi.OshiUtil;
import dingdinghelper.enums.MessageType;
import lombok.Getter;

@Getter
public class Content {
    private String content;
    private MessageType messageType;
    private String hostAddress;
    private String hostName;
    private String osArch;
    private String osName;
    private String osVersion;
    private String userName;
    private String userHome;
    private String javaVersion;
    private String memoryInfo;


    private Content() {
    }

    public Content(MessageType messageType, String content) {
        this.content=content;
        this.hostAddress = SystemUtil.getHostInfo().getAddress();
        this.hostName = SystemUtil.getHostInfo().getName();
        this.osArch = SystemUtil.getOsInfo().getArch();
        this.osName = SystemUtil.getOsInfo().getName();
        this.osVersion = SystemUtil.getOsInfo().getVersion();
        this.userName = SystemUtil.getUserInfo().getName();
        this.userHome = SystemUtil.getUserInfo().getHomeDir();
        this.javaVersion = SystemUtil.getJavaInfo().getVersion();
        this.memoryInfo = OshiUtil.getMemory().toString();
        this.content = String.format("消息通知[%s]: \n%s", messageType.getText(), toString());
        this.messageType = messageType;

    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("");
        sb.append(String.format("    content: %s\n", content));
        sb.append(String.format("    hostAddress: %s\n", hostAddress));
        sb.append(String.format("    hostName: %s\n", hostName));
        sb.append(String.format("    osArch: %s\n", osArch));
        sb.append(String.format("    osName: %s\n", osName));
        sb.append(String.format("    osVersion: %s\n", osVersion));
        sb.append(String.format("    userName: %s\n", userName));
        sb.append(String.format("    userHome: %s\n", userHome));
        sb.append(String.format("    javaVersion: %s\n", javaVersion));
        sb.append(String.format("    memoryInfo: %s\n", memoryInfo));
        return sb.toString();
    }
}
