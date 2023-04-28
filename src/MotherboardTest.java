import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class MotherboardTest {

    private final Logger logger = Logger.getLogger(Motherboard.class.getName());
    private final LoggerTestingHandler handler = new LoggerTestingHandler();
    @Before
    public void setup() {
        logger.addHandler(handler);
    }

    @Test
    public void addDevice() {
        Motherboard motherboard = new Motherboard();
        Mock.MockDevice mock = new Mock.MockDevice(0, true);
        Mock.MockDevice duplicate = new Mock.MockDevice(0, false);

        assertThrows(NullPointerException.class, () -> motherboard.addDevice(null));

        assertTrue(motherboard.addDevice(mock));
        assertFalse(motherboard.addDevice(mock));
        assertFalse(motherboard.addDevice(duplicate));
    }

    @Test
    public void removeDevice() {
        Motherboard motherboard = new Motherboard();
        motherboard.addDevice(new Mock.MockDevice(0, true));

        assertFalse(motherboard.removeDevice(1));
        assertTrue(motherboard.removeDevice(0));
        assertFalse(motherboard.removeDevice(0));
    }

    @Test
    public void devices() {
        Motherboard motherboard = new Motherboard();
        Mock.MockDevice mock1 = new Mock.MockDevice(1, true);
        Mock.MockDevice mock2 = new Mock.MockDevice(2, false);
        motherboard.addDevice(mock1);
        motherboard.addDevice(mock2);

        assertEquals(motherboard.devices(), Map.of(1, mock1, 2, mock2));
    }

    @Test
    public void hasDeviceWithID() {
        Motherboard motherboard = new Motherboard();
        motherboard.addDevice(new Mock.MockDevice(1, true));

        assertTrue(motherboard.hasDeviceWithID(1));
        assertFalse(motherboard.hasDeviceWithID(3));
    }

    @Test
    public void sendMessage() {
        Motherboard motherboard = new Motherboard();
        Message message = Message.of(1, 1, "100");
        handler.clearLogRecords();

        assertThrows(NullPointerException.class, () -> motherboard.sendMessage(null));

        assertFalse(motherboard.sendMessage(message));
        assertTrue(handler.getLastLog().orElse("").contains("no device with ID"));

        motherboard.addDevice(new Mock.MockDevice(1, false));
        motherboard.sendMessage(message);
    }

    @Test
    public void sendBroadcastMessage() {
        Motherboard motherboard = new Motherboard();
        motherboard.addDevice(new Mock.MockDevice(1, true));
        String binary = "100";
        String notBinary = "not binary";
        handler.clearLogRecords();

        assertThrows(NullPointerException.class, () -> motherboard.sendBroadcastMessage(null));

        assertTrue(motherboard.sendBroadcastMessage(binary));
        assertEquals(handler.getLastLog(), Optional.empty());
        assertFalse(motherboard.sendBroadcastMessage(notBinary));
        assertTrue(handler.getLastLog().orElse("").contains("payload is not in the correct format (binary string)"));
    }
}