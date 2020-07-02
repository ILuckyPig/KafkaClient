package com.lu.entity;

public enum ConsumerValueEnum {
    STRING("string"),
    JSON("JSON");

    private String key;

    ConsumerValueEnum(String key) {
        this.key = key;
    }

    public static ConsumerValueEnum from(String key) {
        for (ConsumerValueEnum cEnum : ConsumerValueEnum.values()) {
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
