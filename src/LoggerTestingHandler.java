import java.util.Optional;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

/**
 * Logging helper and handler for testing classes by Professor Vincenzo Liberatore and modified by Jimmy Ngo.
 */
class LoggerTestingHandler extends Handler {

    /**
     * Last log message
     */
    private String lastLog = null;

    /**
     * Creates a new LoggerTestingHandler.
     */
    protected LoggerTestingHandler() {
        super();
    }

    @Override
    public void publish(LogRecord record) {
        lastLog = record.getMessage();
    }

    @Override
    public void flush() {
        throw new AssertionError();
    }

    @Override
    public void close() throws SecurityException {
        throw new AssertionError();
    }

    /**
     * Returns the last log message and an empty Optional if there isn't any.
     *
     * @return the last log message
     */
    public Optional<String> getLastLog() {
        return Optional.ofNullable(lastLog);
    }

    /**
     * Clears the log records.
     */
    public void clearLogRecords() {
        lastLog = null;
    }
}