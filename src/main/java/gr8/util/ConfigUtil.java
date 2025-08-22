package gr8.util;

import static java.util.ResourceBundle.getBundle;

public class ConfigUtil {

    public static String getBreweriesUrl() {
        return getBundle("config").getString("breweries.url");
    }
}
