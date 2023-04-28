import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class AbstractApplication implements Application {

    /**
     * Logger to log descriptive warning messages instead of throwing exceptions
     */
    private static final Logger logger = Logger.getLogger(AbstractApplication.class.getName());

    /**
     * device this application is connected to
     */
    private final Device device;

    /**
     * @param device the device this application will be connected to
     */
    protected AbstractApplication(Device device) {
        this.device = device;
    }

    /**
     * @param port the port to have this application listen on for the device this application is on
     * @return the result of device.addApplication(port, this)
     */
    public boolean connectToPort(int port) {
        return device.addApplication(port, this);
    }

    /**
     * @return the result of device.isApplicationConnected(this)
     */
    public boolean connectedToAPort() {
        return device.isApplicationConnected(this);
    }

    /**
     * @return the device this application is dependent on
     */
    public Device device() {
        return device;
    }

    /**
     * @param message the message to be sent from this application
     * @return whether the message was successfully sent and received
     * @throws NullPointerException when the message is null
     * Logs a warning message when this application is not connected to a port, so it can't receive messages
     * Calls device.sendMessage(message)
     */
    public boolean sendMessage(Message message) {
        Objects.requireNonNull(message);
        if (!connectedToAPort())
            logger.log(Level.WARNING, "application %s is not connected to a port on device %s so messages cannot be received".formatted(this, device));
        return device.sendMessage(message);
    }

    /**
     * @param message receive the message and handle it accordingly
     */
    public abstract void receiveMessage(Message message);

    /**
     * @param payload the contents of the broadcast message
     * @return whether this message was successfully broadcast
     * @throws NullPointerException when the payload is null
     * Logs a warning message and returns false when the payload is not a binary string
     * Logs a warning message when this application is not connected to a port, so it can't receive messages
     * Calls device.sendBroadcastMessage(payload)
     */
    public boolean sendBroadcastMessage(String payload) {
        Objects.requireNonNull(payload);
        if (!Message.binaryString(payload)) {
            logger.log(Level.WARNING, "payload is not in the correct format (binary string)");
            return false;
        }
        if (!connectedToAPort())
            logger.log(Level.WARNING, "application %s is not connected to a port on device %s so messages cannot be received".formatted(this, device));
        return device.sendBroadcastMessage(payload);
    }

    /**
     * @param payload the actual content of the message to receive and handle
     */
    public abstract void receiveBroadcastMessage(String payload);
}
