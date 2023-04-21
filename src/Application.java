import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class Application {

    private static final Logger logger = Logger.getLogger(Application.class.getName());

    private final Device device;
    private int port = -1;

    protected Application(Device device) {
        this.device = device;
    }

    protected boolean connectToPort(int port) {
        if (device.addApplication(port, this)) {
            if (this.port != -1)
                device.removeApplication(this.port);
            this.port = port;
            return true;
        }
        return false;
    }

    /**
     * @return the device this application is dependent on
     */
    protected Device device() {
        return device;
    }

    /**
     * @return the port this application is on with regard to the device it is connected to;
     *   a port number of -1 signals an invalid port because this application hasn't connected to a device's port
     */
    protected int port() {
        return port;
    }

    /**
     * @param message the message to be sent from this application
     * @return whether the message was successfully sent and received
     */
    protected boolean sendMessage(Message message) {
        if (port == -1)
            logger.log(Level.WARNING, "this application isn't connected to a device's port so it cannot receive messages");
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
        if (!Message.binaryString(payload)) {
            logger.log(Level.WARNING, "payload is not in the correct format (binary string)");
            return false;
        }
        if (port == -1)
            logger.log(Level.WARNING, "this application isn't connected to a device's port so it cannot receive messages");
        return device.sendBroadcastMessage(payload);
    }

    protected abstract void receiveBroadcastMessage(String payload);
}
