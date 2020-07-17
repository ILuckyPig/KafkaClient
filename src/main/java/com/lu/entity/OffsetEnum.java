package com.lu.entity;

public enum OffsetEnum {
    EARLIEST("Earliest"),
    LATEST("Latest"),
    LATEST_MINUS_X("Latest - X"),
    CURRENT_OFFSET_PLUS_X("Current Offset + X");

    private String key;

    OffsetEnum(String key) {
        this.key = key;
    }

    public static OffsetEnum from(String key) {
        for (OffsetEnum offsetEnum : OffsetEnum.values()) {
            if (offsetEnum.getKey().equals(key)) {
                return offsetEnum;
            }
        }
        return null;
    }

    public String getKey() {
        return key;
    }

}
