public class Mock {

    public static class MockDevice extends AbstractDevice {

        public MockDevice(int identifier, boolean receiveBroadcast) {
            super(identifier, receiveBroadcast);
        }

        @Override
        public void receiveBroadcastMessage(String payload) {}
    }

    public static class MockApplication extends AbstractApplication {

        protected MockApplication(AbstractDevice device) {
            super(device);
        }

        @Override
        public void receiveMessage(Message message) {}

        @Override
        public void receiveBroadcastMessage(String payload) {}
    }
}