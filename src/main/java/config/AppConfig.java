package config;

/**
 * Stores application configuration constants.
 */
public final class AppConfig {

    /**
     * Default number of worker threads used by background save operations.
     */
    public static final int SAVE_THREAD_POOL_SIZE = 1;

    /**
     * Default application language.
     */
    public static final String DEFAULT_LANGUAGE = "pl";

    /**
     * Default data file name.
     */
    public static final String DEFAULT_DATA_FILE = "library-data.bin";

    private AppConfig() {
    }
}