import com.lu.model.PartitionOffsetAndLag;
import com.lu.model.Topic;
import com.lu.util.KafkaUtil;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.TopicDescription;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.Assert;
import org.junit.Test;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
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
}
