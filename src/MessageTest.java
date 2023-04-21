import org.junit.Test;

import static org.junit.Assert.*;

public class MessageTest {

    @Test
    public void of() {
        assertThrows(IllegalArgumentException.class, () -> {
            Message.of(0, 0, "");
        });
        assertThrows(IllegalArgumentException.class, () -> {
            Message.of(1, 1, "non-binary string");
        });

        Message message1 = new Message(2, 2, "100");
        Message message2 = Message.of(2, 2, "100");
        assertEquals(message1.recipient(), message2.recipient());
        assertEquals(message1.port(), message2.port());
        assertEquals(message1.payload(), message2.payload());
    }

    @Test
    public void binaryString() {
        String binary = "100";
        String notBinary = "not binary";

        assertTrue(Message.binaryString(binary));
        assertFalse(Message.binaryString(notBinary));
    }

    @Test
    public void recipient() {
        Message message1 = Message.of(2, 2, "100");
        assertEquals(message1.recipient(), 2);
    }

    @Test
    public void port() {
        Message message1 = Message.of(2, 2, "100");
        assertEquals(message1.port(), 2);
    }

    @Test
    public void payload() {
        Message message1 = Message.of(2, 2, "100");
        assertEquals(message1.payload(), "100");
    }
}