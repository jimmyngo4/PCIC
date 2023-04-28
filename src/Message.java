import java.util.Objects;

/**
 * Represents a Message that will be sent from Application <-> Device <-> Motherboard <-> Device <-> Application
 */
public class Message {

    /**
     * the unique device ID of the recipient
     */
    private final int recipient;
    /**
     * the unique port number for that device
     */
    private final int port;
    /**
     * the binary string payload
     */
    private final String payload;

    /**
     * @param recipient which device to send to
     * @param port which port on that device to send to
     * @param payload content of the message itself
     */
    protected Message(int recipient, int port, String payload) {
        this.recipient = recipient;
        this.port = port;
        this.payload = payload;
    }

    /**
     * @param recipient the unique device identifier to send the message to
     * @param port the unique port number of the application on the device to send the message to
     * @param payload a non-empty binary string representing the message's content
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
     * @param string the string to match against
     * @return whether the string is binary
     */
    protected static boolean binaryString(String string) {
        Objects.requireNonNull(string);
        return string.matches("^[0|1]+$");
    }

    /**
     * @return recipient of this message
     */
    protected int recipient() {
        return recipient;
    }

    /**
     * @return port this message should go to
     */
    protected int port() {
        return port;
    }

    /**
     * @return payload of this message
     */
    protected String payload() {
        return payload;
    }
}
