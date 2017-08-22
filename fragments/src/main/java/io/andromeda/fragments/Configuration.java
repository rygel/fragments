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
import net.sourceforge.cobertura.CoverageIgnore;

import java.nio.file.Path;
import java.util.Map;

/**
 * Configuration object for Fragments class.
 * @author Alexander Brandt
 */
public class Configuration {
    private String extension = ".md";
    private String domain = "";
    private String protocol = "https://";
    private RouteType routeType = RouteType.ARTICLES;
    private boolean registerOverviewRoute = true;
    /** Name of the Fragments instance. Used for error messages to aid debugging in case files cannot be loaded
     * properly and for naming the database table. */
    private String name;
    private String urlPath;
    private Path dataDirectory;
    private String overviewTemplate;
    private String defaultTemplate;
    private DynamicContext dynamicContext;

    /**
     * Default Constructor.
     */
    @CoverageIgnore
    private Configuration() {

    }

    /**
     * Creates a new Configuration instance. The three required parameters are the minimal necessary user-provided
     * configuration items.
     * @param name The name of this Fragments instance. It is used for identifying the instance and for creating the
     *             filename of the database, when it is enabled. In a project each Fragments instance should have a
     *             unique name.
     * @param urlPath Path of the base URL. Used for the automatically created routes. Full path will be urlPath/slug.
     * @param dataDirectory Directory containing the Markdown files.
     */
    public Configuration(String name, String urlPath, Path dataDirectory){
        this(name, urlPath, dataDirectory, "", "");
    }

    /**
     * Creates a new Configuration instance. The three required parameters are the minimal necessary user-provided
     * configuration items.
     * @param name The name of this Fragments instance. It is used for identifying the instance and for creating the
     *             filename of the database, when it is enabled. In a project each Fragments instance should have a
     *             unique name.
     * @param urlPath Path of the base URL. Used for the automatically created routes. Full path will be urlPath/slug.
     * @param dataDirectory Directory containing the Markdown files.
     * @param overviewTemplate Template to be used for the overview page, e.g urlPath.
     * @param defaultTemplate Template to be used for the individual page, e.g. urlPath/slug. Can be overwritten inside
     *                        the front matter.
     */
    public Configuration(String name, String urlPath, Path dataDirectory, String overviewTemplate, String defaultTemplate){
        this.name = name;
        this.urlPath = urlPath;
        this.dataDirectory = dataDirectory;
        this.overviewTemplate = overviewTemplate;
        this.defaultTemplate = defaultTemplate;
    }

    /********** Getters ***********************************************************************************************/

    /**
     * Returns the extension of the fragments files of this instance.
     * @return The extension of the fragments files of this Fragments instance.
     */
    public final String getExtension() {
        return extension;
    }

    /**
     * Returns the domain name of this instance.
     * @return The domain name of this Fragments instance.
     */
    public final String getDomain() {
        return domain;
    }

    /**
     * Returns the protocol (http:// or https://) of this instance.
     * @return The protocol of this Fragments instance.
     */
    public final String getProtocol() {
        return protocol;
    }

    /**
     * Returns the RouteType of this instance.
     * @return The RouteType of this Fragments instance.
     */
    public final RouteType getRouteType() {
        return routeType;
    }

    public  final boolean registerOverviewRoute() {
        return registerOverviewRoute;
    }

    /**
     * Returns the name of this instance.
     * @return The name of this Fragments instance.
     */
    public final String getName() {
        return name;
    }

    /**
     * Returns the path of the base URL of this instance.
     * @return The name of this Fragments instance.
     */
    public final String getUrlPath() {
        return urlPath;
    }

    /**
     * Returns the directory containing the Markdown files of this instance.
     * @return The directory containing the Markdown files of this Fragments instance.
     */
    public final Path getDataDirectory() {
        return dataDirectory;
    }

    /**
     * Returns the template to be used for the overview page of this instance.
     * @return The template to be used for the overview page of this Fragments instance.
     */
    public final String getOverviewTemplate() {
        return overviewTemplate;
    }

    /**
     * Returns the Template to be used for the individual page of this instance.
     * @return The Template to be used for the individual page of this Fragments instance.
     */
    public final String getDefaultTemplate() {
        return defaultTemplate;
    }

    /**
     * Returns the dynamic context to be used for the fragments.
     * @return The dynamic context to be used for the fragments.
     */
    public final Map<String, Object> getDynamicContext(Map<String, Object> previousContext) {
        if (dynamicContext != null) {
            return dynamicContext.getContext(previousContext);
        } else {
            return previousContext;
        }

    }

    /********** Setters ***********************************************************************************************/

    /**
     * Sets the extension of the fragments files for this Fragments instance. The default one is ".md";
     * @param extension The new extension.
     */
    public final void setExtension(String extension) {
        this.extension = extension;
    }

    /**
     * Sets the domain name for this Fragments instance. It is needed to construct the encoded URL of a Fragment.
     * @param domain The new domain name.
     */
    public final void setDomain(String domain) {
        this.domain = domain;
    }

    /**
     * Sets the protocol for this Fragments instance. The default one is https://.
     * @param protocol The new protocol.
     */
    public final void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    /**
     * Sets the @see RouteType for this Fragments instance. The default one is @RouteType.ARTICLES.
     * @param routeType The new RouteType.
     */
    public final void setRouteType(RouteType routeType) {
        this.routeType = routeType;
    }

    /**
     * If set to true the route for the overview page, e.g. @see Fragments.urlPath, will be registered in Pippo.
     * Can only be set before creating the Fragments instance!
     * @param registerOverviewRoute Enables/disables the route for overview page.
     */
    public final void setRegisterOverviewRoute(boolean registerOverviewRoute) {
        this.registerOverviewRoute = registerOverviewRoute;
    }

    /**
     * Sets the dynamic context for this Fragments instance.
     * @param dynamicContext A class implementing the DynamicContext interface.
     */
    public final void setDynamicContext(DynamicContext dynamicContext) {
        this.dynamicContext = dynamicContext;
    }

}
