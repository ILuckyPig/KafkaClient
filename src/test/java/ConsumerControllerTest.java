import com.lu.controller.ConsumerController;
import org.junit.Test;

public class ConsumerControllerTest {
    @Test
    public void testConsumerMessage() {
        ConsumerController controller = new ConsumerController();
        controller.setBootstrapServers("192.168.8.50:9092");
        controller.consumerMessage(10,"xhs_user", 0, 200);
    }
}
