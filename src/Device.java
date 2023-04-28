import java.util.Map;

public interface Device {

    boolean sendMessage(Message message);

    boolean sendBroadcastMessage(String payload);

    boolean receiveMessage(Message message);

    void receiveBroadcastMessage(String payload);

    int identifier();

    boolean setIdentifier(int identifier);

    Map<Integer, Application> portMapping();

    Map<Application, Integer> appMapping();

    boolean isApplicationConnected(Application application);

    boolean addApplication(int port, Application application);

    boolean removeApplication(int port);

    boolean receiveBroadcast();

    void setReceiveBroadcast(boolean receiveBroadcast);

    boolean connectedToMotherboard();

    boolean setMotherboard(Motherboard motherboard);
}
