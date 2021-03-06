package io.andromeda.fragments;

import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.Appender;
import io.andromeda.fragments.types.RouteType;
import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.slf4j.LoggerFactory;
import ro.pippo.core.Application;

import java.nio.file.Paths;
import java.time.Instant;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static io.andromeda.fragments.Fragments.byOrder;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by Alexander on 11.01.2017.
 */
public class FragmentsTest {

    @Test
    public void testLanguageFragments() throws Exception {
        String currentPath = System.getProperty("user.dir");
        Configuration configuration = new Configuration("Test", "/",
                Paths.get(currentPath + "/src/test/resources/languages/item.md"), "",
                "", 0);
        Fragment fragment = new Fragment("test", "de", configuration);
        String result = Utilities.removeTrailingSlash("/");
        //assertThat(expected, equalTo(result));
    }

    @Test
    public void testFragmentsTagsAndCategories() throws Exception {
        int expectedSizeAllTags = 4;
        String currentPath = System.getProperty("user.dir");
        Configuration configuration = new Configuration("Test", "/",
                Paths.get(currentPath + "/src/test/resources/fragments/tests/"), "",
                "", 0);
        Fragments fragments = new Fragments(new Application(), configuration);
        // Size of all tags
        int sizeAllTags = fragments.getAllTags().size();
        assertThat(expectedSizeAllTags, equalTo(sizeAllTags));
        // Set of all keys of all tags
        Set<String> expectedTagsAll = new HashSet<>(Arrays.asList("a_tag_j", "a_tag_y", "b_tag", "z_tag"));
        assertThat(expectedTagsAll, equalTo(fragments.getAllTags().keySet()));
        // Set of all keys of the visible tags
        Set<String> expectedTagsVisible = new HashSet<>(Arrays.asList("a_tag_j", "b_tag", "z_tag"));
        assertThat(expectedTagsVisible, equalTo(fragments.getVisibleTags().keySet()));
        // Set of all keys of all categories
        Set<String> expectedCategoriesAll = new HashSet<>(Arrays.asList("a_category_j", "a_category_y", "b_category", "z_category"));
        assertThat(expectedCategoriesAll, equalTo(fragments.getAllCategories().keySet()));
        // Set of all keys of the visible categories
        Set<String> expectedCategoriesVisible = new HashSet<>(Arrays.asList("a_category_j", "b_category", "z_category"));
        assertThat(expectedCategoriesVisible, equalTo(fragments.getVisibleCategories().keySet()));
    }

    /** Test that the name is correctly passed from the Fragments class to the Fragment classes. */
    @Test
    public void testNameOfFragment() throws Exception {
        String fragmentsName = "TEST__test";
        String currentPath = System.getProperty("user.dir");
        Configuration configuration = new Configuration(fragmentsName, "/",
                Paths.get(currentPath + "/src/test/resources/fragments/tests/"), "",
                "", 0);
        Fragments fragments = new Fragments(new Application(), configuration);
        assertThat(fragments.getName(), equalTo(fragmentsName));
        List<Fragment> items = fragments.getFragments(true);
        for (int i = 0; i < items.size(); i++) {
            assertThat(items.get(i).getName(), equalTo(fragmentsName));
        }
    }

    /** Test that the order is correctly overwritten manually. */
    @Test
    public void testFragmentsOrder() throws Exception {
        String currentPath = System.getProperty("user.dir");
        Configuration configuration = new Configuration("order", "/",
                Paths.get(currentPath + "/src/test/resources/fragments/tests/order/"), "",
                "", 0);
        Fragments fragments = new Fragments(new Application(), configuration);
        List<Fragment> items = fragments.getVisibleFragmentOrdered(byOrder);
        assertThat(items.get(0).getTitle(), equalTo("Q as Q"));
        assertThat(items.get(1).getTitle(), equalTo("A as Andromeda"));
        assertThat(items.get(2).getTitle(), equalTo("P as Pippo"));
        assertThat(items.get(3).getTitle(), equalTo("Z as the End"));
    }

    /** Test all properties of a Fragment. */
    @Test
    public void testFragmentsAll() throws Exception {
        Date expectedDate = Date.from(Instant.parse("2017-01-02T00:00:00.000Z"));
        String currentPath = System.getProperty("user.dir");
        Configuration configuration = new Configuration("order", "/",
                Paths.get(currentPath + "/src/test/resources/fragments/tests/general/"), "",
                "", 0);
        Fragments fragments = new Fragments(new Application(), configuration);
        List<Fragment> items = fragments.getFragments(true);
        Fragment fragment = items.get(0);
        assertThat(fragment.getTitle(), equalTo("This is a Test"));
        assertThat(fragment.getSlug(), equalTo("this_is_test"));
        assertThat(fragment.getDate(), equalTo(expectedDate));
        assertThat(fragment.getOrder(), equalTo(-100));
        assertThat(fragment.getVisible(), equalTo(false));
        assertThat(fragment.getPreview().trim(), equalTo("<p>Manual preview</p>"));
    }

    /** Test all variants of the more tag. */
    @Test
    public void testFragmentsMoreTags() throws Exception {
        String currentPath = System.getProperty("user.dir");
        Configuration configuration = new Configuration("order", "/",
                Paths.get(currentPath + "/src/test/resources/fragments/tests/more_tags/"), "",
                "", 0);
        Fragments fragments = new Fragments(new Application(), configuration);
        List<Fragment> items = fragments.getFragments(true);
        Fragment fragment_01 = items.get(0);
        assertThat(fragment_01.getPreview().trim(), equalTo("<p>This is a preview 01!</p>"));
        assertThat(fragment_01.getPreviewTextOnly(), equalTo("This is a preview 01!"));
        Fragment fragment_02 = items.get(1);
        assertThat(fragment_02.getPreview().trim(), equalTo("<p>This is a preview 02!</p>"));
        assertThat(fragment_02.getPreviewTextOnly(), equalTo("This is a preview 02!"));
        Fragment fragment_03 = items.get(2);
        assertThat(fragment_03.getPreview().trim(), equalTo("<p>This is a preview 03!</p>"));
        assertThat(fragment_03.getPreviewTextOnly(), equalTo("This is a preview 03!"));
        Fragment fragment_04 = items.get(3);
        assertThat(fragment_04.getPreview().trim(), equalTo("<p>This is a preview 04!</p>"));
        assertThat(fragment_04.getPreviewTextOnly(), equalTo("This is a preview 04!"));
    }

    /** Test RouteType.BLOG. */
    @Test
    public void testFragmentsRouteTypeBlog() throws Exception {
        ch.qos.logback.classic.Logger root = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(ch.qos.logback.classic.Logger.ROOT_LOGGER_NAME);
        final Appender mockAppender = mock(Appender.class);
        when(mockAppender.getName()).thenReturn("MOCK");
        root.addAppender(mockAppender);
        String currentPath = System.getProperty("user.dir");
        Configuration configuration = new Configuration("order", "/",
                Paths.get(currentPath + "/src/test/resources/fragments/tests/blog/"), "",
                "", 0);
        configuration.setRouteType(RouteType.BLOG);
        Fragments fragments = new Fragments(new Application(), configuration);
        List<Fragment> items = fragments.getFragments(true);

        verify(mockAppender).doAppend(argThat(new ArgumentMatcher() {
            @Override
            public boolean matches(final Object argument) {
                return ((LoggingEvent)argument).getFormattedMessage().contains("Date is not available for a fragment of type Blog: ");
            }
        }));
    }

    @Test
    public void testFragmentsCompareTo() {
        String currentPath = System.getProperty("user.dir");
        Configuration configuration = new Configuration("order", "/",
                Paths.get(currentPath + "/src/test/resources/fragments/tests/more_tags/"), "",
                "", 0);
        Fragments fragments = new Fragments(new Application(), configuration);
        List<Fragment> items = fragments.getFragments(true);
    }

}
