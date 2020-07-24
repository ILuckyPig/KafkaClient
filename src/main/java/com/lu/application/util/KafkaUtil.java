package com.lu.application.util;

import com.lu.application.model.PartitionOffsetAndLag;
import com.lu.application.model.Topic;
import org.apache.kafka.clients.admin.*;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.KafkaFuture;
import org.apache.kafka.common.TopicPartition;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

public class KafkaUtil {
    /**
     * 判断kafka是否可以连接
     *
     * @param bootstrapServer
     * @return
     */
    public static boolean testConnectivity(String bootstrapServer) {
        try {
            Properties properties = new Properties();
            properties.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
            properties.put(AdminClientConfig.REQUEST_TIMEOUT_MS_CONFIG, "3000");
            properties.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "3000");
            AdminClient adminClient = KafkaAdminClient.create(properties);
            adminClient.listTopics().names().get(3, TimeUnit.SECONDS);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 获得kafka admin client
     *
     * @param bootstrapServer
     * @return
     */
    public static AdminClient getAdminClient(String bootstrapServer) {
        Properties properties = new Properties();
        properties.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
        return KafkaAdminClient.create(properties);
    }

    public static KafkaConsumer getConsumer(String groupId, String bootstrapServer, String keyDeserializer, String valueDeserializer) {
        Properties properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, keyDeserializer);
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, valueDeserializer);
        return new KafkaConsumer<>(properties);
    }

    /**
     * 获得topics的replication for和partitions
     *
     * @param adminClient
     * @param topics
     * @return
     */
    public static List<Topic> getTopicRfAndPartitions(AdminClient adminClient, Set<String> topics) {
        try {
            List<Topic> topicList = new ArrayList<>();
            Collection<TopicDescription> topicDescriptions = adminClient.describeTopics(topics).all().get().values();
            for (TopicDescription topicDescription : topicDescriptions) {
                String name = topicDescription.name();
                int partitions = topicDescription.partitions().size();
                int replicationFactor = topicDescription.partitions().iterator().next().replicas().size();
                Topic topic = new Topic(name, replicationFactor, partitions, 0, 0);
                topicList.add(topic);
            }
            return topicList;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获得所有consumer group
     *
     * @param adminClient
     * @return
     */
    public static List<String> getConsumerGroups(AdminClient adminClient) throws InterruptedException, ExecutionException, TimeoutException {
        return adminClient.listConsumerGroups()
                .valid()
                .get(10, TimeUnit.SECONDS)
                .stream()
                .map(ConsumerGroupListing::groupId)
                .collect(Collectors.toList());
    }

    /**
     * 获得consumer lag
     *
     * @param adminClient
     * @param consumer
     * @param groupId
     * @return
     */
    public static List<PartitionOffsetAndLag> getConsumerLag(AdminClient adminClient, KafkaConsumer consumer, String groupId) throws InterruptedException, ExecutionException, TimeoutException {
        Map<TopicPartition, OffsetAndMetadata> consumerGroupOffsets = adminClient.listConsumerGroupOffsets(groupId)
                .partitionsToOffsetAndMetadata()
                .get(10, TimeUnit.SECONDS);

        Map<TopicPartition, Long> topicEndOffsets = consumer.endOffsets(consumerGroupOffsets.keySet());
        return consumerGroupOffsets.entrySet()
                .stream()
                .map(entry -> {
                    Long endOffset = topicEndOffsets.get(entry.getKey());
                    long currentOffset = entry.getValue().offset();
                    return new PartitionOffsetAndLag(entry.getKey().toString(), endOffset, currentOffset);
                })
                .collect(Collectors.toList());
    }

    public static <K, V> List<ConsumerRecord<K, V>> consumerMessage(KafkaConsumer<K, V> consumer, String topic) {
        consumer.subscribe(Arrays.asList(topic));
        int size = 100;
        int i = 0;
        List<ConsumerRecord<K, V>> buffer = new ArrayList<>(size);
        while (i < size) {
            ConsumerRecords<K, V> consumerRecords = consumer.poll(Duration.ofMillis(100));
            for (ConsumerRecord<K, V> consumerRecord : consumerRecords) {
                buffer.add(consumerRecord);
                i++;
            }
        }

        return buffer;
    }

    /**
     * 判断consumer group是否有存活的consumer
     *
     * @param adminClient
     * @param groupId
     * @return 有-false; 没有-true
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public static boolean consumerAlive(AdminClient adminClient, String groupId) throws ExecutionException, InterruptedException {
        KafkaFuture<ConsumerGroupDescription> future = adminClient
                .describeConsumerGroups(Collections.singleton(groupId))
                .describedGroups()
                .get(groupId);
        ConsumerGroupDescription description = future.get();
        return !description.members().isEmpty();
    }
}
