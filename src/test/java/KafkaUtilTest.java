import com.lu.util.KafkaUtil;
import org.apache.kafka.clients.admin.AdminClient;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.ExecutionException;

public class KafkaUtilTest {

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
}
