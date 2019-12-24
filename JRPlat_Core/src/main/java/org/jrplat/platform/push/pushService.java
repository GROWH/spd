package org.jrplat.platform.push;

import cn.jiguang.common.ClientConfig;
import cn.jiguang.common.ServiceHelper;
import cn.jiguang.common.connection.NettyHttpClient;
import cn.jiguang.common.resp.ResponseWrapper;
import cn.jpush.api.push.model.Message;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.Notification;
import io.netty.handler.codec.http.HttpMethod;
import org.jrplat.module.system.service.PropertyHolder;
import org.jrplat.platform.log.JRPlatLogger;
import org.jrplat.platform.log.JRPlatLoggerFactory;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by 赵腾飞 on 16-11-30.
 */

@Service
public class pushService {

    private static final JRPlatLogger LOG = JRPlatLoggerFactory.getJRPlatLogger(pushService.class);
    private static final String appKey = PropertyHolder.getProperty("push.appKey");
    private static final String masterSecret = PropertyHolder.getProperty("push.masterSecret");
    //    private JPushClient jPushClient;

    public boolean pushMessage(String msg, String alias) {
        PushPayload payload = PushPayload.newBuilder()
                .setPlatform(Platform.all())
                .setAudience(Audience.alias(alias))
                .setNotification(Notification.alert(msg))
                .build();
        push(payload);
        return true;
    }

    private void push(PushPayload payload) {
        try {

            ClientConfig config = ClientConfig.getInstance();

            // Setup the custom hostname
            config.setPushHostName("https://api.jpush.cn");

            String host = (String) config.get(ClientConfig.PUSH_HOST_NAME);
            NettyHttpClient client = new NettyHttpClient(
                    ServiceHelper.getBasicAuthorization(appKey, masterSecret), null, config);

            URI uri = new URI(host + config.get(ClientConfig.PUSH_PATH));

            client.sendRequest(HttpMethod.POST, payload.toString(), uri, new NettyHttpClient.BaseCallback() {
                @Override
                public void onSucceed(ResponseWrapper wrapper) {
                    LOG.info("Jpush result:" + wrapper.responseContent);
                }
            });
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
//    private void push(PushPayload payload) {
//        if (jPushClient == null) {
//            initJPush();
//        }
//        ClientConfig config = ClientConfig.getInstance();
//        //setup the custom hostname
//        config.setPushHostName("https://api.jpush.cn");
//
//        try {
//            PushResult result = jPushClient.sendPush(payload);
//            LOG.info("Got result - " + result);
//
//        } catch (APIConnectionException e) {
//            LOG.error("Connection error. Should retry later. ", e);
//
//        } catch (APIRequestException e) {
//            LOG.error("Error response from JPush server. Should review and fix it. ", e);
//            LOG.info("HTTP Status: " + e.getStatus());
//            LOG.info("Error Code: " + e.getErrorCode());
//            LOG.info("Error Message: " + e.getErrorMessage());
//            LOG.info("Msg ID: " + e.getMsgId());
//        }
//    }

//    private void initJPush() throws URISyntaxException {
//
//        ClientConfig config = ClientConfig.getInstance();
//
//        // Setup the custom hostname
//        config.setPushHostName("https://api.jpush.cn");
//
//        String host = (String) config.get(ClientConfig.PUSH_HOST_NAME);
//        client = new NettyHttpClient(
//                ServiceHelper.getBasicAuthorization(appKey, masterSecret), null, config);
//
//        uri = new URI(host + config.get(ClientConfig.PUSH_PATH));
////        jPushClient = new JPushClient(masterSecret, appKey, null, config);
//    }

    public void pushLogin(String alias) {
        PushPayload payload = PushPayload.newBuilder()
                .setPlatform(Platform.all())
                .setAudience(Audience.alias(alias))
                .setMessage(Message.content(alias + ":登陆成功"))
                .build();
        push(payload);
    }
}
