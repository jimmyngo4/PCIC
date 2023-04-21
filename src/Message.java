import java.util.Objects;

public class Message {

    private final int recipient;
    private final int port;
    private final String payload;

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
     */
    protected static Message of(int recipient, int port, String payload) {
        Objects.requireNonNull(payload);
        if (payload.trim().length() == 0)
            throw new IllegalArgumentException("payload cannot be empty or only whitespace");
        if (!binaryString(payload))
            throw new IllegalArgumentException("payload must be a binary string");
        return new Message(recipient, port, payload);
    }

    protected static boolean binaryString(String string) {
        Objects.requireNonNull(string);
        return string.matches("^[0|1]+$");
    }

    protected int recipient() {
        return recipient;
    }

    protected int port() {
        return port;
    }

    protected String payload() {
        return payload;
    }
}
