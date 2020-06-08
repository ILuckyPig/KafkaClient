import com.lu.util.KafkaUtil;

import java.util.concurrent.ExecutionException;

public class KafkaUtilTest {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        boolean flag = KafkaUtil.testConnectivity("");
        System.out.println(flag);
    }
}
