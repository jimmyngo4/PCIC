import java.util.logging.Logger;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ApplicationTest {

    private final Logger logger = Logger.getLogger(Application.class.getName());
    private final LoggerTestingHandler handler = new LoggerTestingHandler();
    @Before
    public void setup() {
        logger.addHandler(handler);
    }

    @Test
    public void connectToPort() {
        Mock.MockDevice device = new Mock.MockDevice(1, false);
        Mock.MockApplication application1 = new Mock.MockApplication(device);
        Mock.MockApplication application2 = new Mock.MockApplication(device);

        assertTrue(application1.connectToPort(1));
        assertFalse(application2.connectToPort(1));
        assertTrue(application2.connectToPort(2));
        assertTrue(application2.connectToPort(3));
    }

    @Test
    public void device() {
        Mock.MockDevice device = new Mock.MockDevice(1, false);
        Mock.MockApplication application = new Mock.MockApplication(device);

        assertEquals(application.device(), device);
    }

    @Test
    public void port() {
        Mock.MockDevice device = new Mock.MockDevice(1, false);
        Mock.MockApplication application = new Mock.MockApplication(device);

        assertEquals(application.port(), -1);
        application.connectToPort(2);
        assertEquals(application.port(), 2);
    }

    @Test
    public void sendMessage() {
        Mock.MockDevice device = new Mock.MockDevice(1, false);
        Mock.MockApplication application = new Mock.MockApplication(device);
        Message message = new Message(1, 1, "100");
        handler.clearLogRecords();

        application.sendMessage(message);
        assertTrue(handler.getLastLog().orElse("").contains("this application isn't connected to a device's port so it cannot receive messages"));

        application.connectToPort(1);
        // will fail because the device isn't connected to a motherboard
        assertFalse(application.sendMessage(message));
    }
}