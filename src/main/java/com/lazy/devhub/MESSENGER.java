package com.lazy.devhub;

public enum MESSENGER {
    TELEGRAM_SUCCESS(""),
    WHATSAPP("WhatsApp Messenger"),
    FACEBOOK("Facebook Messenger"),
    VIBER("Viber Messenger"),
    WECHAT("WeChat Messenger");

    private final String code_string;
    private final String value;
    private final String desc;

    // Constructor cho enum
    MESSENGER(String code_string,String value,String desc) {
        this.code_string = code_string;
    }

    public String getCode_string() {
        return code_string;
    }

    public String getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }
    
}
