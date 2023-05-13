import java.util.Map;

/**
 * Represents a Device connected to a Motherboard that has Application(s) listening on port(s).
 */
public interface Device {

    /**
     * Sends a Message from this Device to another Device and Application.
     * Returns whether this Message was successfully sent to another Device or Application.
     *
     * @param message Message to be sent
     * @return whether this Message was successfully sent
     */
    boolean sendMessage(Message message);

    /**
     * Broadcast a Message to any Device open to receiving them.
     * Return whether this Message was successfully broadcast.
     *
     * @param payload the actual content of the Message to send
     * @return whether this Message was successfully broadcast
     */
    boolean sendBroadcastMessage(String payload);

    /**
     * Receive the Message sent to this Device and handle it accordingly.
     *
     * @param message the Message to receive and handle
     * @return whether the Message was successfully received
     */
    boolean receiveMessage(Message message);

    /**
     * Receive a broadcast Message and handle it accordingly.
     *
     * @param payload the actual content of the Message to receive and handle
     */
    void receiveBroadcastMessage(String payload);

    /**
     * Return the unique identifier of this Device.
     *
     * @return the identifier of this Device
     */
    int identifier();

    /**
     * Set the identifier of this Device to the one given.
     * Return whether this Device's identifier was successfully set to the one given.
     *
     * @param identifier the identifier to change this Device's to
     * @return if the identifier of this Device was successfully set
     */
    boolean setIdentifier(int identifier);

    /**
     * Return the mapping of port to Application of this Device.
     *
     * @return the mapping of port to Application of this Device
     */
    Map<Integer, Application> portMapping();

    /**
     * Return the mapping of Application to port of this Device.
     *
     * @return the mapping of Application to port of this Device
     */
    Map<Application, Integer> appMapping();

    /**
     * Return whether the given Application is connected to this Device.
     *
     * @param application the Application to check for if its connected
     * @return whether the Application is connected
     */
    boolean isApplicationConnected(Application application);

    /**
     * Add the given Application to this Device by having it listen on the given port.
     * Return whether this Application was successfully added.
     *
     * @param port port to add the given Application to
     * @param application Application to add to this Device on a specific port
     * @return whether this Application was successfully added
     */
    boolean addApplication(int port, Application application);

    /**
     * Remove the Application listening on the given port number.
     * Return whether an Application was removed from the given port number.
     *
     * @param port port to remove an Application from
     * @return whether an Application was removed from this Device's port
     */
    boolean removeApplication(int port);

    /**
     * Return whether this Device wants to receive broadcast messages.
     *
     * @return whether this Device wants to receive broadcast messages
     */
    boolean receiveBroadcast();

    /**
     * Set whether this Device wants to receive broadcast messages.
     *
     * @param receiveBroadcast sets whether this Device wants to receive broadcast messages
     */
    void setReceiveBroadcast(boolean receiveBroadcast);

    /**
     * Return whether this Device is connected to a Motherboard.
     *
     * @return whether this Device is connected to a Motherboard
     */
    boolean connectedToMotherboard();

    /**
     * Set the Motherboard this Device will be connected to.
     * Return whether this Device was successfully connected to the Motherboard.
     *
     * @param motherboard the Motherboard to connect this Device to
     * @return whether this Device was successfully connected to the Motherboard
     */
    boolean setMotherboard(Motherboard motherboard);
}
