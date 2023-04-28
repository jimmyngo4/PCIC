import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Motherboard {

    private static final Logger logger = Logger.getLogger(Motherboard.class.getName());

    private final Map<Integer, Device> devices;

    protected Motherboard() {
        devices = new HashMap<>();
    }

    /**
     * @param device the device to be added
     * @return whether this motherboard already has a device with the identifier, and if not, "connects" with it
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
     */
    protected boolean removeDevice(int identifier) {
        if (!devices.containsKey(identifier))
            return false;
        devices.remove(identifier);
        return true;
    }

    /**
     * @return copy of the devices this motherboard is connected to
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
     */
    protected boolean sendMessage(Message message) {
        Objects.requireNonNull(message);
        if (!devices.containsKey(message.recipient())) {
            logger.log(Level.WARNING, "no device with ID %d is connected to motherboard %s to send the message to".formatted(message.recipient(), this));
            return false;
        }
        return devices.get(message.recipient()).receiveMessage(message);
    }

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
