package io.andromeda.fragments;

import io.andromeda.fragments.types.RouteType;
import org.junit.Test;
import ro.pippo.core.Application;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.Date;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;


/**
 * @author Alexander Brandt
 */
public class ConfigurationTest {

    @Test
    public void testConfigurationSetters() throws Exception {
        String name = "Test";
        String urlPath = "/";
        String currentPath = System.getProperty("user.dir");
        Path dataDirectory = Paths.get(currentPath + "/src/test/resources/fragments/tests/");
        String overviewTemplate = "overview.peb";
        String defaultTemplate = "default.peb";
        Configuration configuration = new Configuration(name, urlPath, dataDirectory, overviewTemplate, defaultTemplate);
        String extension = ".txt";
        String domain = "test.test";
        String protocol = "https://";
        RouteType routeType = RouteType.BLOG;
        Boolean registerOverviewRoute = false;
        configuration.setExtension(extension);
        configuration.setDomain(domain);
        configuration.setProtocol(protocol);
        configuration.setRouteType(routeType);
        configuration.setRegisterOverviewRoute(registerOverviewRoute);
        configuration.setDynamicContext(null);
        assertThat(configuration.getExtension(), equalTo(extension));
        assertThat(configuration.getDomain(), equalTo(domain));
        assertThat(configuration.getProtocol(), equalTo(protocol));
        assertThat(configuration.getRouteType(), equalTo(routeType));
        assertThat(configuration.registerOverviewRoute(), equalTo(registerOverviewRoute));
        assertThat(configuration.getName(), equalTo(name));
        assertThat(configuration.getUrlPath(), equalTo(urlPath));
        assertThat(configuration.getDataDirectory(), equalTo(dataDirectory));
        assertThat(configuration.getOverviewTemplate(), equalTo(overviewTemplate));
        assertThat(configuration.getDefaultTemplate(), equalTo(defaultTemplate));
        assertThat(configuration.getDynamicContext(null), equalTo(null));
    }

    /** Test all properties of a Fragment. */
    @Test
    public void testFragmentsDifferentExtension() throws Exception {
        String currentPath = System.getProperty("user.dir");
        Configuration configuration = new Configuration("order", "/", Paths.get(currentPath + "/src/test/resources/fragments/tests/general/"), "", "");
        configuration.setExtension(".txt");
        Fragments fragments = new Fragments(new Application(), configuration);
        List<Fragment> items = fragments.getFragments(true);
        Fragment fragment = items.get(0);
        assertThat(fragment.getTitle(), equalTo("This is a Different extension"));
    }

}
