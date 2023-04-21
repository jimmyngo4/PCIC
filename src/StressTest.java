import java.sql.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class StressTest {

    Motherboard motherboard = new Motherboard();
    List<Mock.MockDevice> devices = new ArrayList<>();
    List<Mock.MockApplication> apps = new ArrayList<>();
    List<Thread> threads = new ArrayList<>();

    @Before
    public void setUp() {
        IntStream.range(0, 100).forEach(i -> {
            Mock.MockDevice device = new Mock.MockDevice(i, true);
            Mock.MockApplication app = new Mock.MockApplication(device);
            app.connectToPort(i);
            device.setMotherboard(motherboard);
            devices.add(device);
            apps.add(app);
        });

        IntStream.range(0, 99).forEach(i -> {
            threads.add(new Thread(() -> {
                assertTrue(apps.get(i).sendMessage(new Message(i + 1, i + 1, "100")));
            }));
        });
        threads.add(new Thread(() -> {
            assertTrue(apps.get(99).sendMessage(new Message(0, 0, "100")));
        }));
    }

    @Test
    public void test() {
        for (Thread thread : threads) {
            thread.start();
        }
    }
}
