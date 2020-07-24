package com.lu.application.model;

public class PartitionOffsetAndLag {
    private String partition;
    private Long endOffset;
    private Long currentOffset;
    private Long lag;
    private String endCurrent;

    public PartitionOffsetAndLag(String partition, Long endOffset, Long currentOffset) {
        this.partition = partition;
        this.endOffset = endOffset;
        this.currentOffset = currentOffset;
    }

    public String getPartition() {
        return partition;
    }

    public void setPartition(String partition) {
        this.partition = partition;
    }

    public Long getEndOffset() {
        return endOffset;
    }

    public void setEndOffset(Long endOffset) {
        this.endOffset = endOffset;
    }

    public Long getCurrentOffset() {
        return currentOffset;
    }

    public void setCurrentOffset(Long currentOffset) {
        this.currentOffset = currentOffset;
    }

    public Long getLag() {
        return this.endOffset - this.currentOffset;
    }

    public void setLag(Long lag) {
        this.lag = lag;
    }

    public String getEndCurrent() {
        return this.endOffset + " - " + this.currentOffset;
    }

    public void setEndCurrent(String endCurrent) {
        this.endCurrent = endCurrent;
    }

    @Override
    public String toString() {
        return "PartitionOffsetAndLag{" +
                "partition='" + partition + '\'' +
                ", endOffset=" + endOffset +
                ", currentOffset=" + currentOffset +
                '}';
    }
}
