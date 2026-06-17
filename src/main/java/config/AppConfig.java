package config;

/**
 * Przechowuje stałe konfiguracyjne aplikacji.
 */
public final class AppConfig {

    /**
     * Domyślna liczba wątków roboczych wykorzystywanych
     * przez operacje zapisu działające w tle.
     */
    public static final int SAVE_THREAD_POOL_SIZE = 1;

    /**
     * Domyślny język aplikacji.
     */
    public static final String DEFAULT_LANGUAGE = "pl";

    /**
     * Domyślna nazwa pliku z danymi.
     */
    public static final String DEFAULT_DATA_FILE = "library-data.bin";

    private AppConfig() {
    }
}