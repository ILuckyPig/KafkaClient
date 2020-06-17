package com.lu.util;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.KafkaAdminClient;
import org.apache.kafka.clients.consumer.ConsumerConfig;

import java.util.Properties;
import java.util.concurrent.TimeUnit;

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
}
