package io.andromeda.fragments;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ro.pippo.core.Application;
import ro.pippo.core.Languages;
import ro.pippo.core.route.RouteContext;
import ro.pippo.core.route.RouteHandler;
import ro.pippo.core.util.ClasspathUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
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
    private final static Logger LOGGER = LoggerFactory.getLogger(Fragments.class);

    private List<Fragment> allFragments = new ArrayList<Fragment>();
    private List<Fragment> visibleFragments = new ArrayList<Fragment>();

    private Application application;
    private Configuration configuration;
    private String name;
    private String urlPath;
    private String dataDirectory;
    private String overviewTemplate;
    private String defaultTemplate;
    private Map<String, Object> defaultContext;

    public Fragments(Application application, String name, String urlPath, String dataDirectory, Configuration configuration) {
        this(application, name, urlPath, dataDirectory, "", "", null, configuration);
    }

    /**
     * Main class to hold all fragments and to create all routes automatically.
     *
     * @param application Reference to PippoApplication. Needed to create the routes for the files.
     * @param name Name of the Fragments instance. Used for error messages to aid debugging in case files cannot be loaded properly.
     * @param urlPath Path of the base URL. Used for the automatically created routes. Full path will be urlPath/slug.
     * @param dataDirectory Directory containing the Markdown files.
     * @param overviewTemplate Template to be used for the overview page, e.g urlPath
     * @param defaultTemplate Template to be used for the individual page, e.g. urlPath/slug. Can be overwritten inside the front matter.
     * @param defaultContext Default context to be used when rending the overview and the individual page. Can be extended via the front matter.
     */
    public Fragments(Application application, String name, String urlPath, String dataDirectory, String overviewTemplate, String defaultTemplate, Map<String, Object> defaultContext, Configuration configuration) {
        this.application = application;
        this.name = name;
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

        LOGGER.debug("Creating Fragments for [" + name + "].");
        URL location = ClasspathUtils.locateOnClasspath("io/andromeda/fragments/" + dataDirectory);

        if (location == null) {
            // The fragments path is not found in the class path. Now checking the file system.
            Path fragmentsPath = Paths.get(dataDirectory);
            //Check for path in the file system, not the Classpath.
            if (Files.exists(fragmentsPath)) {
                // The fragments directory was found in the file system
                try {
                    location =  fragmentsPath.toUri().toURL();
                } catch (MalformedURLException e) {
                    LOGGER.error("Problems working with the fragments path: " + e.toString());
                }
            } else {
                LOGGER.error("The directory for the fragments data (\"{}\") for fragments \"{}\" does not exist or does not contain any files! The fragments will not be loaded.",
                        dataDirectory, name);
            }
        } else {
            LOGGER.info(location.toString());
        }

        if (dataDirectory == null) {
            LOGGER.error("The data directory is empty! Fragments: " + this.name);
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
                    Languages l = application.getLanguages();
                    String a = l.getRegisteredLanguages().get(0);
                    Fragment fragment = new Fragment(path.normalize().toString(), urlPath, defaultTemplate, application.getLanguages().getRegisteredLanguages().get(0));
                    if (fragment.visible) {
                        visibleFragments.add(fragment);
                    }
                    allFragments.add(fragment);
                } catch (Exception e) {
                    LOGGER.error(e.toString());
                }
            }
        } catch (IOException ex) {
            LOGGER.error("[Fragments: " + name + "] Error reading data directory (" + directory + "): ", ex.getCause());
        }
        LOGGER.info("Fragments [{}]: Loaded {} visible fragments of {} total.", name, visibleFragments.size(), allFragments.size());
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
                    context.put("lang", lang);

                    routeContext.render(fragment.template, context);
                }
            });
        }
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
                routeContext.render(overviewTemplate, context);
            }
        });
    }

    /**
     * Handle
     */
    private void prepareFragments(){
        int counter = 0;
        for (Fragment fragment: allFragments) {
            fragment.full_url = configuration.protocol + configuration.domain + fragment.full_url;
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

    public List<Fragment> getVisibleFragmentOrdered(Comparator orderBy) {
        List<Fragment> result =  new ArrayList(visibleFragments);
        result.sort(orderBy);
        return result;
    }

    public Comparator<Fragment> byOrder = new Comparator<Fragment>() {
        public int compare(Fragment left, Fragment right) {
            return left.order - right.order;
        }
    };

    public Comparator<Fragment> byTitle = new Comparator<Fragment>() {
        public int compare(Fragment left, Fragment right) {
            return left.title.compareToIgnoreCase(right.title);
        }
    };
}
