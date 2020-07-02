package com.lu.entity;

public enum ConsumerUntilEnum {
    FOREVER("forever"),
    NUMBER("number of messages");

    private String key;

    ConsumerUntilEnum(String key) {
        this.key = key;
    }

    public static ConsumerUntilEnum from(String key) {
        for (ConsumerUntilEnum cEnum : ConsumerUntilEnum.values()) {
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
