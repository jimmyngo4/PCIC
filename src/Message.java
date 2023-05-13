import java.util.Objects;

/**
 * Represents a Message that will be sent from Applications and Devices to other Devices and Applications.
 */
public class Message {

    /**
     * the unique Device ID of the recipient
     */
    private final int recipient;
    /**
     * the unique port number for that Device
     */
    private final int port;
    /**
     * the binary string payload
     */
    private final String payload;

    /**
     * Creates a new Message with no error checking.
     *
     * @param recipient which Device to send to
     * @param port which port on that Device to send to
     * @param payload content of the Message itself
     */
    protected Message(int recipient, int port, String payload) {
        this.recipient = recipient;
        this.port = port;
        this.payload = payload;
    }

    /**
     * Static factory method to create a new Message with error checking.
     *
     * @param recipient the unique Device identifier to send the Message to
     * @param port the unique port number of the Application on the Device to send the Message to
     * @param payload a non-empty binary string representing the Message's content
     * @return a new Message
     * @throws NullPointerException when the payload is null
     * @throws IllegalArgumentException when the payload is empty or only whitespace
     * @throws IllegalArgumentException when the payload isn't binary
     * Calls Message.binaryString(payload)
     */
    protected static Message of(int recipient, int port, String payload) {
        Objects.requireNonNull(payload);
        if (payload.trim().length() == 0)
            throw new IllegalArgumentException("payload cannot be empty or only whitespace");
        if (!binaryString(payload))
            throw new IllegalArgumentException("payload must be a binary string");
        return new Message(recipient, port, payload);
    }

    /**
     * Static method to check if the given string is binary.
     *
     * @param string the string to match against
     * @return whether the string is binary
     */
    protected static boolean binaryString(String string) {
        Objects.requireNonNull(string);
        return string.matches("^[0|1]+$");
    }

    /**
     * Returns the recipient of this Message.
     *
     * @return recipient of this Message
     */
    protected int recipient() {
        return recipient;
    }

    /**
     * Returns the port of this Message.
     *
     * @return port this Message should go to
     */
    protected int port() {
        return port;
    }

    /**
     * Returns the payload of this Message.
     *
     * @return payload of this Message
     */
    protected String payload() {
        return payload;
    }
}
