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
    private Map<String, Object> defaultContext;
    //Get the categories/tags sorted by name or the no of Fragments of this cat/tag
    private Map<String, List<Fragment>> allCategories = new TreeMap<>();
    private Map<String, List<Fragment>> visibleCategories = new TreeMap<>();
    private Map<String, List<Fragment>> allTags = new TreeMap<>();
    private Map<String, List<Fragment>> visibleTags = new TreeMap<>();

    public static final Comparator<Fragment> byOrder = Comparator.comparingInt(Fragment::getOrder);

    public static final Comparator<Fragment> byOrderThenTitle
            = Comparator.comparingInt(Fragment::getOrder).thenComparing(Fragment::getTitle);

    public static final Comparator<Fragment> byTitle = Comparator.comparing(Fragment::getTitle);

    public static final Comparator<Fragment> byDate = Comparator.comparing(Fragment::getDate);

    public Fragments(Application application, Configuration configuration) {
        this(application, null, configuration);
    }

    /**
     * Main class to hold all fragments and to create all routes automatically.
     *
     * @param application Reference to PippoApplication. Needed to create the routes for the files.
     * @param defaultContext Default context to be used when rending the overview and the individual page. Can be extended via the front matter.
     * @param configuration The configuration object holding all necessary information to create a Fragments instance.
     */
    public Fragments(Application application, Map<String, Object> defaultContext, Configuration configuration) {
        this.application = application;
        this.urlPath = configuration.getUrlPath();
        this.dataDirectory = configuration.getDataDirectory().normalize().toString();
        this.overviewTemplate = configuration.getOverviewTemplate();
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
                    LOGGER.error("Problems working with the fragments path: {}", e);
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
            return;
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

        readDirectory(dataDirectory);
        prepareFragments();
        registerFragments();
    }

    public List<Fragment> getFragments(boolean includingInvisible) {
        if (includingInvisible) {
            return allFragments;
        } else {
            return visibleFragments;
        }
    }

    private void readDirectory(String directory) {
        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(Paths.get(directory))) {
            for (Path path : directoryStream) {
                try {
                    if (path.toString().toLowerCase().endsWith(configuration.getExtension())) {
                        List<String> languages = application.getLanguages().getRegisteredLanguages();
                        String defaultLanguage = "en";
                        if (!languages.isEmpty()) {
                            defaultLanguage = languages.get(0);
                        }
                        Fragment fragment = new Fragment(path.normalize().toString(), defaultLanguage, configuration);
                        if (fragment.getVisible()) {
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
            application.GET(fragment.getUrl(), routeContext -> {
                final Map<String, Object> context = new TreeMap<>(defaultContext);
                String lang = routeContext.getParameter("lang").toString();
                fragment.update(lang);
                fragment.setContext(context, false);
                context.put(Constants.FRAGMENT_ID, fragment);
                context.put("overview_url", urlPath);
                context.put("fragments", getVisibleFragmentOrdered(byOrder));
                context.put("fragments_ordered_by_title", getVisibleFragmentOrdered(byTitle));
                context.put("all_fragments", allFragments);

                context.putAll(configuration.getDynamicContext(context));
                if (dbsupport != null) {
                    context.put("top_fragments", dbsupport.getTopFragments());
                    context.put("number_of_clicks", dbsupport.addClick(fragment));
                }
                context.put("lang", lang);

                routeContext.render(fragment.getTemplate(), context);
            });
        }
        if (configuration.registerOverviewRoute()) {
            String route = Utilities.removeTrailingSlash(urlPath);
            application.GET(route, routeContext -> {
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
            });
        }
    }

    /**
     * Handle the creation of the encoded URL as well as the order of the Fragments.
     */
    private void prepareFragments(){
        int counter = 0;
        /* Make sure that the fragments are ordered by Title before changing/overwriting the order! */
        if (!allFragments.isEmpty()) {
            allFragments.sort(byOrderThenTitle);
            for (Fragment fragment : allFragments) {
                fragment.setFullUrl(configuration.getProtocol() + configuration.getDomain() + fragment.getUrl());
                //Create the URLEncoded  url
                try {
                    fragment.setFullUrlEncoded(URLEncoder.encode(fragment.getFullUrl(), "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    LOGGER.error("Error: Cannot convert URL: {}! {}", fragment.getUrl(), e);
                }

                if (fragment.getOrder() == Integer.MAX_VALUE) {
                    fragment.setOrder(counter);
                }
                counter++;

                handleTaxonomies(fragment, fragment.getTags(), allTags, visibleTags);
                handleTaxonomies(fragment, fragment.getCategories(), allCategories, visibleCategories);
            }
        }
    }

    /**
     * Method to filter/sort the taxonomies (tags or categories) into the two result maps, allTags/allCategories and
     * visibleTags/visibleCategories. The result maps have the tag/category names as key and and a list of all Fragments
     * of this tag/category as the value.
     * @param fragment The current Fragment.
     * @param taxonomies The list of the taxonomies of this Fragment, either the tags or categories.
     * @param allTaxonomies Reference to the all Taxonomy, either allTags or allCategories.
     * @param visibleTaxonomies Reference to the visible Taxonomy, either visibleTags or visibleCategories.
     */
    private void handleTaxonomies(Fragment fragment, List<String> taxonomies, Map<String, List<Fragment>> allTaxonomies,
                                  Map<String, List<Fragment>> visibleTaxonomies) {
        if (taxonomies != null) {
            for (int i = 0; i < taxonomies.size(); i++) {
                List<Fragment> current;
                String taxName = taxonomies.get(i);
                if (allTaxonomies.containsKey(taxName)) {
                    current = allTaxonomies.get(taxName);
                } else {
                    current = new ArrayList<>();
                    allTaxonomies.put(taxName, current);
                }
                current.add(fragment);
                if (fragment.getVisible()) {
                    List<Fragment> currentVisible;
                    if (visibleTaxonomies.containsKey(taxName)) {
                        currentVisible = visibleTaxonomies.get(taxName);
                    } else {
                        currentVisible = new ArrayList<>();
                        visibleTaxonomies.put(taxName, currentVisible);
                    }
                    currentVisible.add(fragment);
                }
            }
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

    public DBSupport enableDatabase(DBConfiguration dbConfiguration) {
        dbsupport = new DBSupport(dbConfiguration, this);
        return dbsupport;
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

    /**
     * Gets a map of all tags (including invisible Fragments) of this Fragments instance.
     * @return A map of all tags (including invisible Fragments) of this Fragments instance.
     */
    public Map<String, List<Fragment>> getAllTags() {
        return allTags;
    }

    /**
     * Gets a map of only the tags of the visible Fragments of this Fragments instance.
     * @return A map of only the tags of the visible Fragments of this Fragments instance.
     */
    public Map<String, List<Fragment>> getVisibleTags() {
        return visibleTags;
    }

    /**
     * Gets a map of all categories (including invisible Fragments) of this Fragments instance.
     * @return A map of all categories (including invisible Fragments) of this Fragments instance.
     */
    public Map<String, List<Fragment>> getAllCategories() {
        return allCategories;
    }

    /**
     * Gets a map of only the categories of the visible Fragments of this Fragments instance.
     * @return A map of only the categories of the visible Fragments of this Fragments instance.
     */
    public Map<String, List<Fragment>> getVisibleCategories() {
        return visibleCategories;
    }

}
