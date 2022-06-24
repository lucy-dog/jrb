package com.jsw.jrb.sms.service;

import java.util.Map;

public interface SmsService {
    //发送短信验证码
    void send(String mobile, String templateCode, Map<String,Object> param);
}
