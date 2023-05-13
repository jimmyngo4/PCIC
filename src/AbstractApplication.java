import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Skeletal implementation of the Application interface to minimize the effort required to implement it.
 */
public abstract class AbstractApplication implements Application {

    /**
     * Logger to log descriptive warning messages instead of throwing exceptions
     */
    private static final Logger logger = Logger.getLogger(AbstractApplication.class.getName());

    /**
     * Device this Application is connected to
     */
    private final Device device;

    /**
     * Creates a new AbstractApplication with the given Device it will be connected to.
     *
     * @param device the Device this Application will be connected to
     */
    protected AbstractApplication(Device device) {
        this.device = device;
    }

    /**
     * Connects this AbstractApplication to the specified port for the Device this AbstractApplication is on.
     * Returns whether this AbstractApplication was successfully connected.
     *
     * @param port the port to have this Application listen on for the Device this Application is on
     * @return the result of device.addApplication(port, this)
     */
    public boolean connectToPort(int port) {
        return device.addApplication(port, this);
    }

    /**
     * Return whether this AbstractApplication is listening to a port on the Device this AbstractApplication is on.
     *
     * @return the result of device.isApplicationConnected(this)
     */
    public boolean connectedToAPort() {
        return device.isApplicationConnected(this);
    }

    /**
     * Returns the Device this AbstractApplication is on.
     *
     * @return the Device this Application is dependent on
     */
    public Device device() {
        return device;
    }

    /**
     * Sends a Message from this AbstractApplication to another Device and Application.
     * Returns whether this Message was successfully sent to another Device or Application.
     *
     * @param message the Message to be sent from this Application
     * @return whether the Message was successfully sent and received
     * @throws NullPointerException when the message is null
     * Logs a warning message when this Application is not connected to a port, so it can't receive a Message
     * Calls device.sendMessage(message)
     */
    public boolean sendMessage(Message message) {
        Objects.requireNonNull(message);
        if (!connectedToAPort())
            logger.log(Level.WARNING, "application %s is not connected to a port on device %s so messages cannot be received".formatted(this, device));
        return device.sendMessage(message);
    }

    /**
     * Receive the Message sent to this AbstractApplication and handle it accordingly.
     *
     * @param message receive the Message and handle it accordingly
     */
    public abstract void receiveMessage(Message message);

    /**
     * Broadcast a Message to any Device open to receiving them.
     * Return whether this Message was successfully broadcast.
     *
     * @param payload the contents of the Message that was broadcast
     * @return whether this Message was successfully broadcast
     * @throws NullPointerException when the payload is null
     * Logs a warning message and returns false when the payload is not a binary string
     * Logs a warning message when this Application is not connected to a port, so it can't receive a Message
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
     * Receive a broadcast Message and handle it accordingly.
     *
     * @param payload the actual content of the Message to receive and handle
     */
    public abstract void receiveBroadcastMessage(String payload);
}
