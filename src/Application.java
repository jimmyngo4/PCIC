/**
 * Represents an application on a device
 */
public interface Application {

    /**
     * @param port the port to have this application listen on for the device this application is on
     * @return whether this application was successfully connected to the given port
     */
    boolean connectToPort(int port);

    /**
     * @return whether this application is listening on a port
     */
    boolean connectedToAPort();

    /**
     * @return the device this application is on
     */
    Device device();

    /**
     * @param message the message to send
     * @return whether this message was successfully sent
     */
    boolean sendMessage(Message message);

    /**
     * @param message the message to receive and handle
     */
    void receiveMessage(Message message);

    /**
     * @param payload the actual content of the message to send
     * @return whether this message was successfully broadcast
     */
    boolean sendBroadcastMessage(String payload);

    /**
     * @param payload the actual content of the message to receive and handle
     */
    void receiveBroadcastMessage(String payload);
}
