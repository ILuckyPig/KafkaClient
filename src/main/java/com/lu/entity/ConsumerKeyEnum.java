package com.lu.entity;

public enum ConsumerKeyEnum {
    STRING("string"),
    JSON("JSON");

    private String key;

    ConsumerKeyEnum(String key) {
        this.key = key;
    }

    public static ConsumerKeyEnum from(String key) {
        for (ConsumerKeyEnum cEnum : ConsumerKeyEnum.values()) {
            if (cEnum.getKey().equals(key)) {
                return cEnum;
            }
        }
        return null;
    }

    public String getKey() {
        return key;
    }
}
