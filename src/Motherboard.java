import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Represents a Motherboard which will have Devices connected to it and Applications on those Devices.
 */
public class Motherboard {

    /**
     * Logger to log descriptive warning messages instead of throwing exceptions
     */
    private static final Logger logger = Logger.getLogger(Motherboard.class.getName());

    /**
     * mapping of device IDs to a Device
     */
    private final Map<Integer, Device> devices;

    /**
     * creates a new Motherboard with no connected devices
     */
    protected Motherboard() {
        devices = new HashMap<>();
    }

    /**
     * Adds the given Device to this Motherboard.
     * Returns if the given Device was successfully added.
     *
     * @param device the Device to be added
     * @return whether this Motherboard already has a Device with the identifier, and if not, "connects" with it
     * @throws NullPointerException if the given Device is null
     * Adds to devices if successful
     */
    protected boolean addDevice(Device device) {
        Objects.requireNonNull(device);
        if (devices.containsKey(device.identifier()))
            return false;
        devices.put(device.identifier(), device);
        return true;
    }

    /**
     * Removes the Device with the given identifier from this Motherboard.
     * Returns if a Device was removed from this Motherboard or not.
     *
     * @param identifier the unique identifier of the Device to be removed
     * @return whether the Device was successfully removed from this Motherboard
     * Removes from devices if successful
     */
    protected boolean removeDevice(int identifier) {
        if (!devices.containsKey(identifier))
            return false;
        devices.remove(identifier);
        return true;
    }

    /**
     * Returns an unmodifiable copy of the mapping of identifier to Device for this Motherboard.
     *
     * @return unmodifiable copy of the devices this Motherboard is connected to
     */
    protected Map<Integer, Device> devices() {
        return Map.copyOf(devices);
    }

    /**
     * Returns whether this Motherboard has a Device with the given identifier.
     *
     * @param identifier unique identifier of a Device
     * @return whether this Motherboard already has a Device with the given identifier
     */
    protected boolean hasDeviceWithID(int identifier) {
        return devices.containsKey(identifier);
    }

    /**
     * Sends the given Message to the Message's recipient if they are connected to this motherboard.
     * Returns whether the Message was successfully sent.
     *
     * @param message the Message to be sent
     * @return whether this Motherboard is connected to a Device with identifier matching the Message's recipient
     * @throws NullPointerException if the Device is null
     * Logs a warning and returns false when no Device with the Message's ID is connected to this Motherboard
     * Calls device.receiveMessage(message)
     */
    protected boolean sendMessage(Message message) {
        Objects.requireNonNull(message);
        if (!devices.containsKey(message.recipient())) {
            logger.log(Level.WARNING, "no device with ID %d is connected to motherboard %s to send the message to".formatted(message.recipient(), this));
            return false;
        }
        return devices.get(message.recipient()).receiveMessage(message);
    }

    /**
     * Sends the given broadcast message to all Devices connected to this Motherboard that wants to receive broadcast messages.
     * Returns whether any Device received the broadcast message.
     *
     * @param payload the payload to broadcast
     * @return whether any Device received the broadcast message
     * @throws NullPointerException if the payload is null
     * Logs a warning message and returns false if the payload is not binary
     * Calls device.receiveBroadcast()
     * Calls device.receiveBroadcastMessage(payload)
     */
    protected boolean sendBroadcastMessage(String payload) {
        Objects.requireNonNull(payload);
        if (Message.binaryString(payload)) {
            devices.values().stream().filter(Device::receiveBroadcast).forEach(device -> {
                device.receiveBroadcastMessage(payload);
            });
            return true;
        }
        else {
            logger.log(Level.WARNING, "payload is not in the correct format (binary string)");
            return false;
        }
    }
}
