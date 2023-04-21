public class Mock {

    public static class MockDevice extends Device {

        public MockDevice(int identifier, boolean receiveBroadcast) {
            super(identifier, receiveBroadcast);
        }

        @Override
        protected void receiveBroadcastMessage(String payload) {}
    }

    public static class MockApplication extends Application {

        protected MockApplication(Device device) {
            super(device);
        }

        @Override
        protected void receiveMessage(Message message) {}
    }
}