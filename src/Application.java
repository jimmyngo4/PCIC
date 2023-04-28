public interface Application {

    boolean connectToPort(int port);

    boolean connectedToAPort();

    Device device();

    boolean sendMessage(Message message);

    void receiveMessage(Message message);

    boolean sendBroadcastMessage(String payload);

    void receiveBroadcastMessage(String payload);
}
