import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Skeletal implementation of the Device interface to minimize the effort required to implement it.
 */
public abstract class AbstractDevice implements Device {

    /**
     * Logger to log descriptive warning messages instead of throwing exceptions
     */
    protected static final Logger logger = Logger.getLogger(AbstractDevice.class.getName());

    /**
     * a unique ID for each AbstractDevice to guarantee that the Motherboard this AbstractDevice is connected to can always identify it
     */
    protected int identifier;

    /**
     * mapping of port to Application; each port can only have one Application listening on it
     */
    protected final Map<Integer, Application> portMapping = new HashMap<>();

    /**
     * mapping of Application to port; each Application can only listen on one port
     */
    protected final Map<Application, Integer> appMapping = new HashMap<>();

    /**
     * whether this AbstractDevice wants to receive broadcast messages or not
     */
    protected boolean receiveBroadcast;

    /**
     * the Motherboard this AbstractDevice is connected to
     */
    protected Motherboard motherboard = null;

    /**
     * Creates a new AbstractDevice with the given identifier and set whether it wants to receive broadcast messages.
     *
     * @param identifier a unique ID for each Device to guarantee that the Motherboard this Device is connected to can always identify it
     * @param receiveBroadcast whether this Device wants to receive broadcast messages or not
     */
    protected AbstractDevice(int identifier, boolean receiveBroadcast) {
        this.identifier = identifier;
        this.receiveBroadcast = receiveBroadcast;
    }

    /**
     * @param message the Message to be sent
     * @return whether this AbstractDevice is connected to a Motherboard and can therefore send the Message up
     * @throws NullPointerException if the Message is null
     * Logs a warning message if this AbstractDevice is not connected to a Motherboard
     * Calls motherboard.sendMessage(message) to send the Message up
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
     * @return whether the payload string is binary and was successfully broadcast to any other Device
     * @throws NullPointerException if the payload is null
     * Logs a warning message and returns false when the payload string is not binary
     * Logs a warning message and returns false if this AbstractDevice is not connected to a Motherboard
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
     * @param message the Message to be received
     * @return whether an Application is on the specified port for this AbstractDevice and the Message can be delivered there
     * @throws NullPointerException if the Message is null
     * Logs a warning message and returns false if no Application is on the specified port for this AbstractDevice
     * Calls device.receiveMessage(message) to pass the Message along
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
     * @return the unique ID for this AbstractDevice
     */
    public int identifier() {
        return identifier;
    }

    /**
     * @param identifier the new identifier for this AbstractDevice
     * @return whether this AbstractDevice can change its identifier to the given one;
     *   if this AbstractDevice isn't connected to a Motherboard, it can freely change its identifier
     *   if the Motherboard it's connected to does not have a Device with the given identifier, change it
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
     * @param application the Application to check for
     * @return whether this Application is in appMapping
     */
    public boolean isApplicationConnected(Application application) {
        return appMapping.containsKey(application);
    }

    /**
     * @param port the port number to add this Application on for this AbstractDevice
     * @param application the Application itself
     * @return whether this Application was successfully added
     * @throws NullPointerException if Application is null
     * Logs warning message and returns false when the port is already taken by another Application
     * Logs warning message and returns false if this Application is already listening on a port
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
     * @param port the port number the Application to be removed is at
     * @return whether an Application exists at that port and was removed successfully
     * If successful, removes the Application from appMapping and portMapping
     */
    public boolean removeApplication(int port) {
        if (!portMapping.containsKey(port))
            return false;
        appMapping.remove(portMapping.get(port));
        portMapping.remove(port);
        return true;
    }

    /**
     * @return whether this AbstractDevice wants to receive broadcast messages
     */
    public boolean receiveBroadcast() {
        return receiveBroadcast;
    }

    /**
     * @param receiveBroadcast sets if this AbstractDevice wants to receive broadcast messages
     */
    public void setReceiveBroadcast(boolean receiveBroadcast) {
        this.receiveBroadcast = receiveBroadcast;
    }

    /**
     * @return returns whether this AbstractDevice is connected to a Motherboard by checking if Motherboard is non-null
     */
    public boolean connectedToMotherboard() {
        return motherboard != null;
    }

    /**
     * @param motherboard the Motherboard to connect this Device to
     * @return whether this AbstractDevice was successfully connected to the given Motherboard
     * @throws NullPointerException when the Motherboard is null
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
