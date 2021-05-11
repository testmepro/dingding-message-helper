package entity;

import lombok.Data;

@Data
public class HttpResponse {
    private int errcode;
    private String errmsg;
}
