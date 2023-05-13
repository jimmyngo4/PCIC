/**
 * Represents an Application on a Device
 */
public interface Application {

    /**
     * Connects this Application to the specified port for the Device this Application is on.
     * Returns whether this Application was successfully connected.
     *
     * @param port the port to have this Application listen on for the Device this Application is on
     * @return whether this Application was successfully connected to the given port
     */
    boolean connectToPort(int port);

    /**
     * Return whether this Application is listening to a port on the Device this Application is on.
     *
     * @return whether this Application is listening on a port
     */
    boolean connectedToAPort();

    /**
     * Returns the Device this Application is on.
     *
     * @return the Device this Application is on
     */
    Device device();

    /**
     * Sends a Message from this Application to another Device and Application.
     * Returns whether this Message was successfully sent to another Device or Application.
     *
     * @param message the Message to send
     * @return whether this Message was successfully sent
     */
    boolean sendMessage(Message message);

    /**
     * Receive the Message sent to this Application and handle it accordingly.
     *
     * @param message the Message to receive and handle
     */
    void receiveMessage(Message message);

    /**
     * Broadcast a Message to any Device open to receiving them.
     * Return whether this Message was successfully broadcast.
     *
     * @param payload the actual content of the Message to send
     * @return whether this Message was successfully broadcast
     */
    boolean sendBroadcastMessage(String payload);

    /**
     * Receive a broadcast Message and handle it accordingly.
     *
     * @param payload the actual content of the Message to receive and handle
     */
    void receiveBroadcastMessage(String payload);
}
