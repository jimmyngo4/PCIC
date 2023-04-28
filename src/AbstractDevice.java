import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Skeletal implementation of Device
 */
public abstract class AbstractDevice implements Device {

    /**
     * Logger to log descriptive warning messages instead of throwing exceptions
     */
    protected static final Logger logger = Logger.getLogger(AbstractDevice.class.getName());

    /**
     * a unique ID for each device to guarantee that the motherboard this device is connected to can always identify it
     */
    protected int identifier;

    /**
     * mapping of ports to applications; each port can only have one application listening on it
     */
    protected final Map<Integer, Application> portMapping = new HashMap<>();

    /**
     * mapping of applications to ports; each application can only listen on one port
     */
    protected final Map<Application, Integer> appMapping = new HashMap<>();

    /**
     * whether this device wants to receive broadcast messages or not
     */
    protected boolean receiveBroadcast;

    /**
     * the motherboard this device is connected to
     */
    protected Motherboard motherboard = null;

    /**
     * @param identifier a unique ID for each device to guarantee that the motherboard this device is connected to can always identify it
     * @param receiveBroadcast whether this device wants to receive broadcast messages or not
     */
    protected AbstractDevice(int identifier, boolean receiveBroadcast) {
        this.identifier = identifier;
        this.receiveBroadcast = receiveBroadcast;
    }

    /**
     * @param message the message to be sent
     * @return whether this device is connected to a motherboard and can therefore send the message up
     * @throws NullPointerException if the message is null
     * Logs a warning message if the device is not connected to a motherboard
     * Calls motherboard.sendMessage(message) to send the message up
     */
    public boolean sendMessage(Message message) {
        Objects.requireNonNull(message);
        if (!connectedToMotherboard()) {
            logger.log(Level.WARNING, "couldn't send message from device with ID %d because it is not connected to a motherboard".formatted(identifier));
            return false;
        }
        return motherboard.sendMessage(message);
    }

    /**
     * @param payload the contents of the broadcast message
     * @return whether the payload string is binary and was successfully broadcast to any devices
     * @throws NullPointerException if the payload is null
     * Logs a warning message and returns false when the payload string is not binary
     * Logs a warning message and returns false if the device is not connected to a motherboard
     * Calls motherboard.sendBroadcastMessage(payload)
     */
    public boolean sendBroadcastMessage(String payload) {
        Objects.requireNonNull(payload);
        if (!Message.binaryString(payload)) {
            logger.log(Level.WARNING, "payload is not in the correct format (binary string)");
            return false;
        }
        if (!connectedToMotherboard()) {
            logger.log(Level.WARNING, "couldn't send message from device with ID %d because it is not connected to a motherboard".formatted(identifier));
            return false;
        }
        return motherboard.sendBroadcastMessage(payload);
    }

    /**
     * @param message the message to be received
     * @return whether an application is on the specified port for this device and the message can be delivered there
     * @throws NullPointerException if the message is null
     * Logs a warning message and returns false if no application is on the specified port for this device
     * Calls device.receiveMessage(message) to pass the message along
     */
    public boolean receiveMessage(Message message) {
        Objects.requireNonNull(message);
        if (!portMapping.containsKey(message.port())) {
            logger.log(Level.WARNING, "no application is listening on port %d for device with ID %d to deliver the message to".formatted(message.port(), message.recipient()));
            return false;
        }
        portMapping.get(message.port()).receiveMessage(message);
        return true;
    }

    /**
     * @param payload the binary string to receive and process
     */
    public abstract void receiveBroadcastMessage(String payload);

    /**
     * @return the unique ID for this device
     */
    public int identifier() {
        return identifier;
    }

    /**
     * @param identifier the new identifier for this device
     * @return whether this device can change its identifier to the given one;
     *   if this device isn't connected to a motherboard, it can freely change its identifier
     *   if the motherboard it's connected to does not have a device with the given identifier, change it
     *   otherwise, return false and exit early
     * Calls motherboard.hasDeviceWithID(identifier)
     * Calls motherboard.removeDevice(this.identifier)
     * Calls motherboard.addDevice(this)
     */
    public boolean setIdentifier(int identifier) {
        if (connectedToMotherboard()) {
            if (motherboard.hasDeviceWithID(identifier))
                return false;
            motherboard.removeDevice(this.identifier);
            this.identifier = identifier;
            motherboard.addDevice(this);
        }
        this.identifier = identifier;
        return true;
    }

    /**
     * @return an unmodifiable copy of portMapping
     */
    public Map<Integer, Application> portMapping() {
        return Map.copyOf(portMapping);
    }

    /**
     * @return an unmodifiable copy of appMapping
     */
    public Map<Application, Integer> appMapping() { return Map.copyOf(appMapping); }

    /**
     * @param application the application to check for
     * @return whether this application is in appMapping
     */
    public boolean isApplicationConnected(Application application) {
        return appMapping.containsKey(application);
    }

    /**
     * @param port the port number to add this application on for this device
     * @param application the application itself
     * @return whether this application was successfully added
     * @throws NullPointerException if application is null
     * Logs warning message and returns false when the port is already taken by another app
     * Logs warning message and returns false if this application is already listening on a port
     * Adds to portMapping and appMapping otherwise
     */
    public boolean addApplication(int port, Application application) {
        Objects.requireNonNull(application);
        if (portMapping.containsKey(port)) {
            logger.log(Level.WARNING, "application %s couldn't be connected to port %d on device with ID %d because the port is already taken by application %s".formatted(application, port, identifier, portMapping.get(port)));
            return false;
        }
        if (appMapping.containsKey(application)) {
            logger.log(Level.WARNING, "this application %s is already connected to port %d so it was not connected to given port %d".formatted(application, appMapping.get(application), port));
            return false;
        }
        portMapping.put(port, application);
        appMapping.put(application, port);
        return true;
    }

    /**
     * @param port the port number the application to be removed is at
     * @return whether an application exists at that port and was removed successfully
     * If successful, removes the application from appMapping and portMapping
     */
    public boolean removeApplication(int port) {
        if (!portMapping.containsKey(port))
            return false;
        appMapping.remove(portMapping.get(port));
        portMapping.remove(port);
        return true;
    }

    /**
     * @return whether this device wants to receive broadcast messages
     */
    public boolean receiveBroadcast() {
        return receiveBroadcast;
    }

    /**
     * @param receiveBroadcast sets if this device wants to receive broadcast messages
     */
    public void setReceiveBroadcast(boolean receiveBroadcast) {
        this.receiveBroadcast = receiveBroadcast;
    }

    /**
     * @return returns whether this device is connected to a motherboard by checking if motherboard is non-null
     */
    public boolean connectedToMotherboard() {
        return motherboard != null;
    }

    /**
     * @param motherboard the motherboard to connect this device to
     * @return whether this device was successfully connected to the given motherboard
     * @throws NullPointerException when motherboard is null
     * Calls motherboard.hasDeviceWithID(this.identifier)
     * Calls motherboard.addDevice(this)
     */
    public boolean setMotherboard(Motherboard motherboard) {
        Objects.requireNonNull(motherboard);
        if (motherboard.hasDeviceWithID(this.identifier))
            return false;
        this.motherboard = motherboard;
        motherboard.addDevice(this);
        return true;
    }
}
