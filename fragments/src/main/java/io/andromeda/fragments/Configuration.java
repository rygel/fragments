package io.andromeda.fragments;

import io.andromeda.fragments.types.RouteType;

import java.time.ZoneId;

/**
 * Configuration object for Fragments class.
 * @author Alexander Brandt
 */
public class Configuration {
    String extension = ".md";
    String domain = "";
    String protocol = "https://";
    RouteType routeType = RouteType.ARTICLES;
    ZoneId timeZone = ZoneId.of("UTC");

    /**
     * Default Constructor.
     */
    public Configuration() {

    }

    /**
     * Convenience Constructor for most used manual settings. Those two parameters are important for creating the fully
     * encoded URL.
     * @param protocol The protocol of the website. Either http:// or https://.
     * @param domain The domain name of the website.
     */
    public Configuration(String protocol, String domain) {
        this.protocol = protocol;
        this.domain = domain;
    }

    /**
     * Returns a default Configuration instance. Can be modified at will.
     * @return One default Configuration instance.
     */
    public static Configuration getDefault() {
        return new Configuration();
    }

    /**
     * Returns a default Configuration instance. With protocol and domain overrides.
     *
     * @param protocol The protocol of the website. Either http:// or https://.
     * @param domain The domain name of the website.
     * @return One default Configuration instance.
     */
    public static Configuration getDefault(String protocol, String domain) {
        return new Configuration(protocol, domain);
    }
}
