import java.util.Map;

public interface Device {

    /**
     * @param message message to be sent
     * @return whether this message was successfully sent
     */
    boolean sendMessage(Message message);

    /**
     * @param payload the actual content of the message to send
     * @return whether this message was successfully broadcast
     */
    boolean sendBroadcastMessage(String payload);

    /**
     * @param message the message to receive and handle
     * @return whether the message was successfully received
     */
    boolean receiveMessage(Message message);

    /**
     * @param payload the actual content of the message to receive and handle
     */
    void receiveBroadcastMessage(String payload);

    /**
     * @return the device's identifier
     */
    int identifier();

    /**
     * @param identifier the identifier to change this device's to
     * @return if this device's identifier was successfully set
     */
    boolean setIdentifier(int identifier);

    /**
     * @return portMapping
     */
    Map<Integer, Application> portMapping();

    /**
     * @return appMapping
     */
    Map<Application, Integer> appMapping();

    /**
     * @param application the application to check for if its connected
     * @return whether the app is connected
     */
    boolean isApplicationConnected(Application application);

    /**
     * @param port port to add app to
     * @param application app to add to this device on a specific port
     * @return whether this app was successfully added
     */
    boolean addApplication(int port, Application application);

    /**
     * @param port port to remove app from
     * @return whether an app was removed from this device's port
     */
    boolean removeApplication(int port);

    /**
     * @return whether this device wants to receive broadcast messages
     */
    boolean receiveBroadcast();

    /**
     * @param receiveBroadcast sets whether this device wants to receive broadcast messages
     */
    void setReceiveBroadcast(boolean receiveBroadcast);

    /**
     * @return whether this device is connected to a motherboard
     */
    boolean connectedToMotherboard();

    /**
     * @param motherboard the motherboard to connect this device to
     * @return whether this device was successfully connected to the motherboard
     */
    boolean setMotherboard(Motherboard motherboard);
}
