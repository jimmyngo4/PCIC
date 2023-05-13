/**
 * Mock class to contain mock implementations of the skeletal abstract classes AbstractDevice and AbstractApplication.
 */
public class Mock {

    /**
     * Creates a new Mock instance which is useless which is why this constructor is private.
     */
    private Mock() {
        super();
    }

    /**
     * Mock implementation of the skeletal abstract class AbstractDevice for testing.
     */
    public static class MockDevice extends AbstractDevice {

        /**
         * Creates a new MockDevice for testing AbstractDevice.
         *
         * @param identifier the unique identifier for this MockDevice
         * @param receiveBroadcast whether this MockDevice wants to receive broadcast messages
         */
        public MockDevice(int identifier, boolean receiveBroadcast) {
            super(identifier, receiveBroadcast);
        }

        @Override
        public void receiveBroadcastMessage(String payload) {}
    }

    /**
     * Mock implementation of the skeletal abstract class AbstractApplication for testing.
     */
    public static class MockApplication extends AbstractApplication {

        /**
         * Creates a new MockApplication for testing AbstractApplication.
         *
         * @param device the Device this MockApplication will be connected to
         */
        protected MockApplication(AbstractDevice device) {
            super(device);
        }

        @Override
        public void receiveMessage(Message message) {}

        @Override
        public void receiveBroadcastMessage(String payload) {}
    }
}