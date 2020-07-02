package com.lu.entity;

public enum ConsumerStartEnum {
    LATEST("latest"),
    EARLIEST("earliest"),
    OFFSET("an offset");

    private String key;

    ConsumerStartEnum(String key) {
        this.key = key;
    }

    public static ConsumerStartEnum from(String key) {
        for (ConsumerStartEnum cEnum : ConsumerStartEnum.values()) {
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
