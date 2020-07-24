package com.lu.application.model;


public class Topic {
    private String topicName;
    private Integer replicationFactor;
    private Integer partitions;
    private Integer messageCount;
    private Integer consumerCount;

    public Topic() {
    }

    public Topic(String topicName, Integer replicationFactor, Integer partitions, Integer messageCount, Integer consumerCount) {
        this.topicName = topicName;
        this.replicationFactor = replicationFactor;
        this.partitions = partitions;
        this.messageCount = messageCount;
        this.consumerCount = consumerCount;
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public Integer getReplicationFactor() {
        return replicationFactor;
    }

    public void setReplicationFactor(Integer replicationFactor) {
        this.replicationFactor = replicationFactor;
    }

    public Integer getPartitions() {
        return partitions;
    }

    public void setPartitions(Integer partitions) {
        this.partitions = partitions;
    }

    public Integer getMessageCount() {
        return messageCount;
    }

    public void setMessageCount(Integer messageCount) {
        this.messageCount = messageCount;
    }

    public Integer getConsumerCount() {
        return consumerCount;
    }

    public void setConsumerCount(Integer consumerCount) {
        this.consumerCount = consumerCount;
    }

    @Override
    public String toString() {
        return "Topic{" +
                "topicName='" + topicName + '\'' +
                ", replicationFactor=" + replicationFactor +
                ", partitions=" + partitions +
                ", messageCount=" + messageCount +
                ", consumerCount=" + consumerCount +
                '}';
    }
}
