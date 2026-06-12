package i18n;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Manages application language and translated messages.
 */

public class LanguageManager {
    private Locale locale;
    private ResourceBundle messages;

    public LanguageManager(){
        setLanguage("PL");
    }
    public void setLanguage(String languageCode) {
        this.locale = new Locale(languageCode);
        this.messages = ResourceBundle.getBundle("messages", locale);
    }

    public String get(String key) {
        return messages.getString(key);
    }

    public Locale getLocale() {
        return locale;
    }
}
