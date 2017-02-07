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

import io.andromeda.fragments.db.DBConfiguration;
import io.andromeda.fragments.db.DBSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ro.pippo.core.Application;
import ro.pippo.core.route.RouteContext;
import ro.pippo.core.route.RouteHandler;
import ro.pippo.core.util.ClasspathUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author Alexander Brandt
 */
public class Fragments {
    /** The logger instance for this class. */
    private static final Logger LOGGER = LoggerFactory.getLogger(Fragments.class);

    /** All fragments, even invisible ones (visible: false). */
    private List<Fragment> allFragments = new ArrayList<>();
    /** Only fragments, invisible ones (visible: false) are excluded. */
    private List<Fragment> visibleFragments = new ArrayList<>();

    /** Reference to the Pippo applications. Needed for creating the routes. */
    private Application application;
    /** Reference to the @see io.andromeda.fragments.Configuration object. */
    private Configuration configuration;

    private DBSupport dbsupport;
    private String urlPath;
    private String dataDirectory;
    private String overviewTemplate;
    private String defaultTemplate;
    private Map<String, Object> defaultContext;

    public Fragments(Application application, String urlPath, String dataDirectory, Configuration configuration) {
        this(application, urlPath, dataDirectory, "", "", null, configuration);
    }

    /**
     * Main class to hold all fragments and to create all routes automatically.
     *
     * @param application Reference to PippoApplication. Needed to create the routes for the files.
     * @param urlPath Path of the base URL. Used for the automatically created routes. Full path will be urlPath/slug.
     * @param dataDirectory Directory containing the Markdown files.
     * @param overviewTemplate Template to be used for the overview page, e.g urlPath
     * @param defaultTemplate Template to be used for the individual page, e.g. urlPath/slug. Can be overwritten inside the front matter.
     * @param defaultContext Default context to be used when rending the overview and the individual page. Can be extended via the front matter.
     */
    public Fragments(Application application, String urlPath, String dataDirectory, String overviewTemplate, String defaultTemplate, Map<String, Object> defaultContext, Configuration configuration) {
        this.application = application;
        this.urlPath = urlPath;
        this.dataDirectory = dataDirectory;
        this.overviewTemplate = overviewTemplate;
        this.defaultTemplate = defaultTemplate;
        if (defaultContext == null) {
            this.defaultContext = new TreeMap<>();
        } else {
            this.defaultContext = defaultContext;
        }
        this.configuration = configuration;

        LOGGER.debug("Creating Fragments for [{}].", configuration.getName());
        URL location = ClasspathUtils.locateOnClasspath("io/andromeda/fragments/" + dataDirectory);

        if (location == null) {
            // The fragments path is not found in the class path. Now checking the file system.
            Path fragmentsPath = Paths.get(dataDirectory);
            //Check for path in the file system, not the Classpath.
            if (fragmentsPath.toFile().exists()) {
                // The fragments directory was found in the file system
                try {
                    location =  fragmentsPath.toUri().toURL();
                } catch (MalformedURLException e) {
                    LOGGER.error("Problems working with the fragments path: " + e.toString());
                }
            } else {
                LOGGER.error("The directory for the fragments data (\"{}\") for fragments \"{}\" does not exist or does not contain any files! The fragments will not be loaded.",
                        dataDirectory, configuration.getName());
            }
        } else {
            LOGGER.info(location.toString());
        }

        if (dataDirectory == null) {
            LOGGER.error("The data directory is empty! Fragments: {}", configuration.getName());
        }

        /*LOGGER.info("PROTOCOL: " + dataDirectory.getProtocol());
        if (dataDirectory.getProtocol().equals("jar")) {
        } else {
            try {
                mainDirectory = Utilities.checkIfDirectoryExists(dataDirectory.toURI().getPath());
            } catch (URISyntaxException e) {
                LOGGER.error("The data directory does not exist: " + dataDirectory.toString());
            }
        }*/

        readDirectory(dataDirectory.toString());
        prepareFragments();
        registerFragments();
    }

    public List<Fragment> getFragments(boolean includingInvisible) {
        if (includingInvisible) {
            return allFragments;
        } else {
            return visibleFragments;
        }
        //return fragments.stream().filter(p -> p.isVisible);
    }

    private void readDirectory(String directory) {
        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(Paths.get(directory))) {
            for (Path path : directoryStream) {
                try {
                    if (path.toString().toLowerCase().endsWith(configuration.extension)) {
                        Fragment fragment = new Fragment(path.normalize().toString(), urlPath, defaultTemplate, application.getLanguages().getRegisteredLanguages().get(0), configuration);
                        if (fragment.visible) {
                            visibleFragments.add(fragment);
                        }
                        allFragments.add(fragment);
                    }
                } catch (Exception e) {
                    LOGGER.error("Error: ", e);
                }
            }
        } catch (IOException ex) {
            LOGGER.error("[Fragments: \"{}\"] Error reading data directory (\"{}\"): {}", configuration.getName(), directory, ex);
        }
        LOGGER.info("Fragments [{}]: Loaded {} visible fragments of {} total.", configuration.getName(), visibleFragments.size(), allFragments.size());
    }

    public void registerFragments() {
        for (final Fragment fragment : visibleFragments) {
            application.GET(fragment.url, new RouteHandler() {
                @Override
                public void handle(RouteContext routeContext) {
                    //fragment.full_url = urlPath + fragment.frontMatter.get(Constants.SLUG_ID);
                    final Map<String, Object> context = new TreeMap<>(defaultContext);
                    String lang = routeContext.getParameter("lang").toString();
                    fragment.update(lang);
                    context.putAll(fragment.context);
                    context.put(Constants.FRAGMENT_ID, fragment);
                    context.put("overview_url", urlPath);
                    context.put("fragments", getVisibleFragmentOrdered(byOrder));
                    context.put("fragments_ordered_by_title", getVisibleFragmentOrdered(byTitle));
                    context.put("all_fragments", allFragments);
                    if (dbsupport != null) {
                        context.put("top_fragments", dbsupport.getTopFragments());
                        dbsupport.addClick(fragment);
                    }
                    context.put("lang", lang);

                    routeContext.render(fragment.template, context);
                }
            });
        }
        if (configuration.registerOverviewRoute) {
            String route = Utilities.removeTrailingSlash(urlPath);
            application.GET(route, new RouteHandler() {
                @Override
                public void handle(RouteContext routeContext) {
                    for (Fragment fragment: allFragments) {
                        fragment.update(routeContext.getParameter("lang").toString());
                    }
                    final Map<String, Object> context = new TreeMap<>(defaultContext);
                    context.put("overview_url", urlPath);
                    context.put("fragments", getVisibleFragmentOrdered(byOrder));
                    context.put("fragments_ordered_by_title", getVisibleFragmentOrdered(byTitle));
                    context.put("all_fragments", allFragments);
                    if (dbsupport != null) {
                        context.put("top_fragments", dbsupport.getTopFragments());
                    }
                    routeContext.render(overviewTemplate, context);
                }
            });
        }
    }

    /**
     * Handle the creation of the encoded URL as well as the order of the Fragments.
     */
    private void prepareFragments(){
        int counter = 0;
        for (Fragment fragment: allFragments) {
            fragment.full_url = configuration.protocol + configuration.domain + fragment.url;
            //Create the URLEncoded  url
            try {
                fragment.full_url_encoded = URLEncoder.encode(fragment.full_url, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                LOGGER.error("Error: Cannot convert URL: " + fragment.url + "! " + e);
            }

            if (fragment.order == Integer.MIN_VALUE) {
                fragment.order = counter;
            }
            counter++;
        }
    }

    /**
     * Update the default context. This is useful to inject new information after the Fragments are created.
     * Already existing keys will be overridden!
     * @param newContext Map containing the new context.
     */
    public void updateDefaultContext(Map<String, Object> newContext) {
        defaultContext.putAll(newContext);
    }

    public void enableDatabase(DBConfiguration dbConfiguration) {
        dbsupport = new DBSupport(dbConfiguration, this);

    }

    public String getDataDirectory() {
        return dataDirectory;
    }

    public List<Fragment> getVisibleFragmentOrdered(Comparator orderBy) {
        List<Fragment> result =  new ArrayList(visibleFragments);
        result.sort(orderBy);
        return result;
    }

    public String getName() {
        return configuration.getName();
    }

    public Comparator<Fragment> byOrder = new Comparator<Fragment>() {
        @Override
        public int compare(Fragment left, Fragment right) {
            return left.order - right.order;
        }
    };

    public Comparator<Fragment> byTitle = new Comparator<Fragment>() {
        @Override
        public int compare(Fragment left, Fragment right) {
            return left.title.compareToIgnoreCase(right.title);
        }
    };
}
