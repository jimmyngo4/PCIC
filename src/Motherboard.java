import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Motherboard {

    /**
     * Logger to log descriptive warning messages instead of throwing exceptions
     */
    private static final Logger logger = Logger.getLogger(Motherboard.class.getName());

    /**
     * mapping of device IDs to a device
     */
    private final Map<Integer, Device> devices;

    /**
     * creates a new Motherboard with no connected devices
     */
    protected Motherboard() {
        devices = new HashMap<>();
    }

    /**
     * @param device the device to be added
     * @return whether this motherboard already has a device with the identifier, and if not, "connects" with it
     * @throws NullPointerException if the device is null
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
     * @param identifier the unique identifier of the device to be removed
     * @return whether the device was successfully removed from this motherboard
     * Removes from devices if successful
     */
    protected boolean removeDevice(int identifier) {
        if (!devices.containsKey(identifier))
            return false;
        devices.remove(identifier);
        return true;
    }

    /**
     * @return unmodifiable copy of the devices this motherboard is connected to
     */
    protected Map<Integer, Device> devices() {
        return Map.copyOf(devices);
    }

    /**
     * @param identifier unique identifier of a device
     * @return whether this motherboard already has a device with the given identifier
     */
    protected boolean hasDeviceWithID(int identifier) {
        return devices.containsKey(identifier);
    }

    /**
     * @param message the message to be sent
     * @return whether this motherboard is connected to a device with identifier matching the message's recipient
     * @throws NullPointerException if the device is null
     * Logs a warning and returns false when no device with the message's ID is connected to this motherboard
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
     * @param payload the payload to broadcast
     * @return whether any device received the broadcast message
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
