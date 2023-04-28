import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class Application {

    private static final Logger logger = Logger.getLogger(Application.class.getName());

    private final Device device;

    protected Application(Device device) {
        this.device = device;
    }

    protected boolean connectToPort(int port) {
        return device.addApplication(port, this);
    }

    protected boolean connectedToAPort() {
        return device.isApplicationConnected(this);
    }

    /**
     * @return the device this application is dependent on
     */
    protected Device device() {
        return device;
    }

    /**
     * @param message the message to be sent from this application
     * @return whether the message was successfully sent and received
     */
    protected boolean sendMessage(Message message) {
        Objects.requireNonNull(message);
        if (!connectedToAPort())
            logger.log(Level.WARNING, "application %s is not connected to a port on device %s so messages cannot be received".formatted(this, device));
        return device.sendMessage(message);
    }

    /**
     * @param message receive the message and handle it accordingly
     */
    protected abstract void receiveMessage(Message message);

    /**
     * @param payload the contents of the broadcast message
     * @return whether this message was successfully broadcast
     */
    protected boolean sendBroadcastMessage(String payload) {
        Objects.requireNonNull(payload);
        if (!Message.binaryString(payload)) {
            logger.log(Level.WARNING, "payload is not in the correct format (binary string)");
            return false;
        }
        if (!connectedToAPort())
            logger.log(Level.WARNING, "application %s is not connected to a port on device %s so messages cannot be received".formatted(this, device));
        return device.sendBroadcastMessage(payload);
    }

    protected abstract void receiveBroadcastMessage(String payload);
}
