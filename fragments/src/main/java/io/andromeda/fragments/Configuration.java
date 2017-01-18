package io.andromeda.fragments;

/**
 * @author Alexander Brandt
 */
public class Configuration {
    String extension = ".md";
    String domain    = "";
    String protocol  = "https://";

    public Configuration() {

    }

    public Configuration(String protocol, String domain) {
        this.protocol = protocol;
        this.domain = domain;
    }

    public static Configuration getDefault() {
        return new Configuration();
    }

    public static Configuration getDefault(String protocol, String domain) {
        return new Configuration(protocol, domain);
    }
}
