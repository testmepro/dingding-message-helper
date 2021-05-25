import cn.hutool.core.date.DateTime;
import cn.hutool.core.exceptions.ExceptionUtil;
import com.alibaba.fastjson.JSON;
import com.github.rholder.retry.*;
import com.google.common.base.Preconditions;
import com.google.common.util.concurrent.RateLimiter;
import entity.Content;
import entity.DingdingMessage;
import entity.HttpResponse;
import enums.MessageType;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Slf4j
public class DingdingMessageHelper {
    private static final OkHttpClient client = new OkHttpClient();
    private static final RateLimiter RATE_LIMITER = RateLimiter.create(1D);
    private static final String defaultUrl = "https://oapi.dingtalk.com/robot/send?access_token=d7470bdd99785a2ebfb36c85c5d70c70c114b18e81b404f6711502b23a0453e4";
    /*public static final Retryer<Response> retryer = RetryerBuilder
            .<Response>newBuilder()
            .withWaitStrategy(WaitStrategies.fibonacciWait(3, 30, TimeUnit.SECONDS))
            .withStopStrategy(StopStrategies.stopAfterAttempt(3))
            .retryIfException()
            .withRetryListener(new RetryListener() {
                @Override
                public <V> void onRetry(Attempt<V> attempt) {
                    if (attempt.getAttemptNumber() > 1) {
                        log.warn("[{}]retry execute count: {},retry reason: {}", this.getClass().getName(), attempt.getAttemptNumber(), attempt.hasException() ? attempt.getExceptionCause().getMessage() : "");
                        if (attempt.hasException()) {
                            log.error("ERROR", attempt.getExceptionCause());
                        }
                    }
                }
            })
            .build();*/

    public static void SendDingdingMessage(Throwable t, String url) {
        DingdingMessage msg = new DingdingMessage();
        msg.setText(new Content(MessageType.EXCEPTION, ExceptionUtil.stacktraceToString(t)));
        SendDingdingMessage(msg, url);
    }

    public static void SendDingdingMessage(Throwable t) {
        SendDingdingMessage(t, defaultUrl);
    }

    public static void SendDingdingMessage(String info, String url) {
        DingdingMessage msg = new DingdingMessage();
        msg.setText(new Content(MessageType.INFO, info));
        SendDingdingMessage(msg, url);
    }

    public static void SendDingdingMessage(DingdingMessage msg) {

        SendDingdingMessage(msg, defaultUrl);
    }

    public static void SendDingdingMessage(DingdingMessage msg, String url) {
        if (!RATE_LIMITER.tryAcquire(20)) {//每分钟3次
            log.debug("frequency control limit! rate: {}", RATE_LIMITER.getRate());
            return;
        }
        RequestBody requestBody = RequestBody.create(msg.toJson(), MediaType.parse("application/json; charset=utf-8"));
        Request request = new Request.Builder()
                .url(url)//
                .header("Content-Type", "application/json")
                .post(requestBody)
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                log.error("ERROR request: {}", call.request().toString());
                log.error("ERROR", e);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) {
                try {
                    Preconditions.checkArgument(response.body() != null, "response(body)响应为空");
                    HttpResponse httpResponse = JSON.parseObject(Objects.requireNonNull(response.body()).bytes(), HttpResponse.class);
                    if (httpResponse.getErrcode() != 0) {
                        log.error("ERROR: {}", httpResponse.toString());
                    }
                } catch (Exception e) {
                    log.error("ERROR", e);
                }
            }
        });

    }

    public static void main(String[] args) throws InterruptedException {
        while (true) {
            if (RATE_LIMITER.tryAcquire(20)) {
                log.info("hit current datetime: {}", DateTime.now().toStringDefaultTimeZone());
            }
            Thread.sleep(1000L);
        }
    }
}
