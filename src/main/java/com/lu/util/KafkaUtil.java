package com.lu.util;

import com.lu.model.Topic;
import org.apache.kafka.clients.admin.*;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.Node;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.TopicPartitionInfo;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
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

    /**
     * 获得topic partitions
     *
     * @param adminClient
     * @param topics
     * @return
     */
    public static List<Topic> getTopicsPartitions(AdminClient adminClient, Set<String> topics) {
        try {
            Collection<TopicDescription> topicDescriptions = adminClient.describeTopics(topics).all().get().values();
            return topicDescriptions
                    .stream()
                    .map(topicDescription -> new Topic(topicDescription.name(), 0, topicDescription.partitions().size(), 0, 0))
                    .collect(Collectors.toList());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获得指定topic的config
     *
     * @param adminClient
     * @param topics
     * @return
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public static List<Topic> getTopics(AdminClient adminClient, Set<String> topics) {
        try {
            List<Topic> topicList = new ArrayList<>();
            Collection<TopicDescription> topicDescriptions = adminClient.describeTopics(topics).all().get().values();
            Set<TopicPartition> topicPartitions = topicDescriptions.stream()
                    .flatMap(topicDescription -> topicDescription.partitions().stream()
                            .map(p -> new TopicPartition(topicDescription.name(), p.partition()))
                    ).collect(Collectors.toSet());

            // Map<TopicPartition, PartitionReassignment> partitionReassignmentMap = adminClient
            //         .listPartitionReassignments(topicPartitions, new ListPartitionReassignmentsOptions())
            //         .reassignments()
            //         .get();

            for (TopicDescription topicDescription : topicDescriptions) {
                String name = topicDescription.name();
                int partitions = topicDescription.partitions().size();
                // TopicPartitionInfo firstPartition = topicDescription.partitions().iterator().next();
                // PartitionReassignment reassignment = partitionReassignmentMap.get(new TopicPartition(topicDescription.name(), firstPartition.partition()));
                // int replicationFactor = getReplicationFactor(firstPartition, reassignment);
                Topic topic = new Topic(name, 0, partitions,0,0);
                topicList.add(topic);
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static int getReplicationFactor(TopicPartitionInfo firstPartition, PartitionReassignment reassignment) {
        Set<Integer> allReplicaIds = firstPartition.replicas().stream().map(Node::id).collect(Collectors.toSet());
        Set<Integer> changingReplicaIds = new HashSet<>();
        changingReplicaIds.addAll(reassignment.removingReplicas());
        changingReplicaIds.addAll(reassignment.addingReplicas());

        if (allReplicaIds.retainAll(changingReplicaIds)) {
            reassignment.replicas().removeAll(reassignment.addingReplicas());
            return reassignment.replicas().size();
        } else {
            return firstPartition.replicas().size();
        }
    }
}
