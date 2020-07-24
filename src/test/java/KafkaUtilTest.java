import com.lu.application.model.PartitionOffsetAndLag;
import com.lu.application.model.Topic;
import com.lu.application.util.KafkaUtil;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.TopicDescription;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public class KafkaUtilTest {
    AdminClient adminClient;

    public KafkaUtilTest() {
        adminClient = KafkaUtil.getAdminClient("192.168.8.50:9092");
    }

    @Test
    public void testTestConnectivity() {
        boolean flag = KafkaUtil.testConnectivity("");
        System.out.println(flag);
        Assert.assertFalse(flag);
    }

    @Test
    public void testGetAdminClient() throws ExecutionException, InterruptedException {
        AdminClient adminClient = KafkaUtil.getAdminClient("192.168.8.50:9092");
        adminClient.listTopics().names().get().forEach(System.out::println);
        Assert.assertNotNull(adminClient);
    }

    @Test
    public void testGetTopicsConfig() throws ExecutionException, InterruptedException {
        AdminClient adminClient = KafkaUtil.getAdminClient("192.168.8.50:9092");
        Collection<TopicDescription> topicDescriptions = adminClient.describeTopics(Collections.singleton("xhs_user")).all().get().values();
        for (TopicDescription topicDescription : topicDescriptions) {
            System.out.println(topicDescription.name() + "," + topicDescription.partitions().size());
        }
    }

    @Test
    public void testGetTopics() {
        AdminClient adminClient = KafkaUtil.getAdminClient("192.168.8.50:9092");
        List<Topic> xhs_user = KafkaUtil.getTopicRfAndPartitions(adminClient, Set.of("xhs_user"));
        for (Topic topic : xhs_user) {
            System.out.println(topic);
        }
    }

    @Test
    public void testGetConsumers() throws InterruptedException, ExecutionException, TimeoutException {
        AdminClient adminClient = KafkaUtil.getAdminClient("192.168.8.50:9092");
        List<String> consumers = KafkaUtil.getConsumerGroups(adminClient);
        consumers.forEach(System.out::println);
        Assert.assertNotNull(consumers);
    }

    @Test
    public void testGetConsumerDescription() throws InterruptedException, ExecutionException, TimeoutException {
        KafkaConsumer consumer = KafkaUtil.getConsumer("mbase-spider-parser-dev", "192.168.8.50:9092",
                StringDeserializer.class.getName(), StringDeserializer.class.getName());
        List<PartitionOffsetAndLag> consumerDescription = KafkaUtil.getConsumerLag(adminClient, consumer, "mbase-spider-parser-dev");
        consumerDescription.forEach(System.out::println);
    }

    @Test
    public void testConsumerMessage() {
        Properties properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.8.50:9092");
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, "kafka-client");
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
        properties.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 10);
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(properties);

        KafkaUtil.consumerMessage(consumer, "xhs_user");
    }

    @Test
    public void test() throws ExecutionException, InterruptedException {
        AdminClient adminClient = KafkaUtil.getAdminClient("192.168.8.50:9092");
        boolean alive = KafkaUtil.consumerAlive(adminClient, "mbase-spider-parser-dev");
        Assert.assertFalse(alive);
    }
}
