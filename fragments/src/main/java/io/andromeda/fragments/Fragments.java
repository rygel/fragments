package io.andromeda.fragments;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ro.pippo.core.Application;
import ro.pippo.core.route.RouteContext;
import ro.pippo.core.route.RouteHandler;
import ro.pippo.core.util.ClasspathUtils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
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
    private String name;
    private String urlPath;
    private String dataDirectory;
    private String overviewTemplate;
    private String defaultTemplate;
    private Map<String, Object> defaultContext;

    public Fragments(Application application, String name, String urlPath, String dataDirectory) {
        this(application, name, urlPath, dataDirectory, "", "", null);
    }

    public Fragments(Application application, String name, String urlPath, String dataDirectory, String overviewTemplate, String defaultTemplate, Map<String, Object> defaultContext) {
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

        LOGGER.debug("Creating Fragments.");
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
                    Fragment fragment = new Fragment(path.normalize().toString(), urlPath, defaultTemplate);
                    if (!fragment.isInvisible) {
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
        //preparePostings(blog, pattern);
        for (final Fragment fragment : visibleFragments) {
            //Route checking here?
            //Router router = application.getRouter();
            //List<Route> routes = router.getRoutes();
            application.GET(fragment.url, new RouteHandler() {
                @Override
                public void handle(RouteContext routeContext) {
                    final Map<String, Object> context = new TreeMap<>(defaultContext);
                    context.putAll(fragment.context);
                    context.put(Constants.CONTENT_ID, fragment.content);
                    context.put(Constants.PAGE_ID, fragment.frontMatter);
                    context.put("overview_url", urlPath);
                    context.put("fragments", visibleFragments);
                    context.put("all_fragments", allFragments);

                    routeContext.render(fragment.template, context);
                }
            });
        }
        String route = Utilities.removeTrailingSlash(urlPath);
        application.GET(route, new RouteHandler() {
            @Override
            public void handle(RouteContext routeContext) {
                final Map<String, Object> context = new TreeMap<>(defaultContext);
                context.put("overview_url", urlPath);
                context.put("fragments", visibleFragments);
                context.put("all_fragments", allFragments);
                routeContext.render(overviewTemplate, context);
            }
        });
    }
}
