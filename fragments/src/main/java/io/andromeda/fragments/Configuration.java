/*
 * Copyright (C) 2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.andromeda.fragments;

import io.andromeda.fragments.types.RouteType;

import java.time.ZoneId;

/**
 * Configuration object for Fragments class.
 * @author Alexander Brandt
 */
public class Configuration {
    public String extension = ".md";
    public String domain = "";
    public String protocol = "https://";
    public RouteType routeType = RouteType.ARTICLES;
    public boolean registerOverviewRoute = true;
    /** Name of the Fragments instance. Used for error messages to aid debugging in case files cannot be loaded properly and for naming the database table. */
    private String name;

    /**
     * Default Constructor.
     */
    private Configuration() {

    }

    public Configuration(String name){
        this.name = name;
    }

    /*
     * Convenience Constructor for most used manual settings. Those two parameters are important for creating the fully
     * encoded URL.
     * @param protocol The protocol of the website. Either http:// or https://.
     * @param domain The domain name of the website.
     */
    /*public Configuration(String protocol, String domain) {
        this.protocol = protocol;
        this.domain = domain;
    }*/

    /**
     * Returns a default Configuration instance. Can be modified at will.
     * @return One default Configuration instance.
     */
    public static Configuration getDefault(String name) {
        return new Configuration(name);
    }

    public void setRouteType(RouteType routeType) {
        this.routeType = routeType;
    }

    /*
     * Returns a default Configuration instance. With protocol and domain overrides.
     *
     * @param protocol The protocol of the website. Either http:// or https://.
     * @param domain The domain name of the website.
     * @return One default Configuration instance.
     */
    /*public static Configuration getDefault(String protocol, String domain) {
        return new Configuration(protocol, domain);
    }*/


    public String getName() {
        return name;
    }

}
