import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class DeviceTest {

    private final Logger logger = Logger.getLogger(Device.class.getName());
    private final LoggerTestingHandler handler = new LoggerTestingHandler();
    @Before
    public void setup() {
        logger.addHandler(handler);
    }

    @Test
    public void sendMessage() {
        Mock.MockDevice sender = new Mock.MockDevice(1, false);
        handler.clearLogRecords();
        Message message = new Message(2, 2, "100");

        assertThrows(NullPointerException.class, () -> sender.sendMessage(null));

        // device has not been connected to a motherboard so sending a message fails
        assertFalse(sender.sendMessage(message));
        assertTrue(handler.getLastLog().orElse("").contains("couldn't send message because device is not connected to a motherboard"));

        Motherboard motherboard = new Motherboard();
        sender.setMotherboard(motherboard);

        // motherboard does not have a device that matches the message's recipient so sending a message fails
        assertFalse(sender.sendMessage(message));

        Mock.MockDevice receiver = new Mock.MockDevice(2, false);
        receiver.setMotherboard(motherboard);

        // the receiving device does not have an application on the specified port so sending a message fails
        assertFalse(sender.sendMessage(message));

        Mock.MockApplication application = new Mock.MockApplication(receiver);
        application.connectToPort(2);

        assertTrue(sender.sendMessage(message));
    }

    @Test
    public void receiveMessage() {
        Message message = new Message(2, 2, "100");
        Mock.MockDevice device = new Mock.MockDevice(2, false);
        Mock.MockApplication application = new Mock.MockApplication(device);
        handler.clearLogRecords();

        assertThrows(NullPointerException.class, () -> device.receiveMessage(null));

        assertFalse(device.receiveMessage(message));
        assertTrue(handler.getLastLog().orElse("").contains("no application exists for the message's port to deliver the message to"));

        application.connectToPort(2);
        assertTrue(device.receiveMessage(message));
    }

    @Test
    public void sendBroadcastMessage() {
        handler.clearLogRecords();
        String binary = "100";
        String notBinary = "not binary";
        Mock.MockDevice device = new Mock.MockDevice(1, false);

        assertThrows(NullPointerException.class, () -> device.sendBroadcastMessage(null));

        assertFalse(device.sendBroadcastMessage(notBinary));
        assertTrue(handler.getLastLog().orElse("").contains("payload is not in the correct format (binary string)"));

        assertFalse(device.sendBroadcastMessage(binary));
        assertTrue(handler.getLastLog().orElse("").contains("couldn't send message because device is not connected to a motherboard"));

        Motherboard motherboard = new Motherboard();
        device.setMotherboard(motherboard);
        assertTrue(device.sendBroadcastMessage(binary));
    }

    @Test
    public void identifier() {
        Mock.MockDevice device = new Mock.MockDevice(1, false);
        assertEquals(device.identifier(), 1);
    }

    @Test
    public void setIdentifier() {
        Mock.MockDevice device = new Mock.MockDevice(1, false);
        assertTrue(device.setIdentifier(3));
        assertEquals(device.identifier(), 3);

        Motherboard motherboard = new Motherboard();
        device.setMotherboard(motherboard);
        assertFalse(device.setIdentifier(3));

        assertTrue(device.setIdentifier(5));
        assertTrue(motherboard.devices().containsKey(5));
        assertFalse(motherboard.devices().containsKey(3));
    }

    @Test
    public void portMapping() {
        Mock.MockDevice device = new Mock.MockDevice(1, false);
        Mock.MockApplication application = new Mock.MockApplication(device);

        assertEquals(device.portMapping(), new HashMap<>());
        assertThrows(UnsupportedOperationException.class, () -> {
            device.portMapping().put(2, application);
        });

        device.addApplication(2, application);
        assertEquals(device.portMapping(), Map.of(2, application));
    }

    @Test
    public void addApplication() {
        Mock.MockDevice device = new Mock.MockDevice(1, false);
        Mock.MockApplication app1 = new Mock.MockApplication(device);
        Mock.MockApplication app2 = new Mock.MockApplication(device);

        assertThrows(NullPointerException.class, () -> device.addApplication(1, null));

        assertTrue(device.addApplication(1, app1));
        assertFalse(device.addApplication(1, app2));
        assertTrue(device.addApplication(2, app2));
    }

    @Test
    public void removeApplication() {
        Mock.MockDevice device = new Mock.MockDevice(1, false);
        Mock.MockApplication app1 = new Mock.MockApplication(device);
        Mock.MockApplication app2 = new Mock.MockApplication(device);
        device.addApplication(1, app1);
        device.addApplication(2, app2);

        assertTrue(device.removeApplication(1));
        assertFalse(device.removeApplication(1));
        assertTrue(device.removeApplication(2));
    }

    @Test
    public void receiveBroadcast() {
        Mock.MockDevice device = new Mock.MockDevice(1, true);
        assertTrue(device.receiveBroadcast());
    }

    @Test
    public void setReceiveBroadcast() {
        Mock.MockDevice device = new Mock.MockDevice(1, true);
        assertTrue(device.receiveBroadcast());
        device.setReceiveBroadcast(false);
        assertFalse(device.receiveBroadcast());
        device.setReceiveBroadcast(true);
        assertTrue(device.receiveBroadcast());
    }

    @Test
    public void connectedToMotherboard() {
        Mock.MockDevice device = new Mock.MockDevice(1, true);
        assertFalse(device.connectedToMotherboard());

        Motherboard motherboard = new Motherboard();
        device.setMotherboard(motherboard);
        assertTrue(device.connectedToMotherboard());
    }

    @Test
    public void setMotherboard() {
        Motherboard motherboard = new Motherboard();
        Mock.MockDevice device1 = new Mock.MockDevice(1, false);
        Mock.MockDevice duplicate = new Mock.MockDevice(1, true);

        assertThrows(NullPointerException.class, () -> device1.setMotherboard(null));

        assertTrue(device1.setMotherboard(motherboard));
        assertFalse(duplicate.setMotherboard(motherboard));
    }
}