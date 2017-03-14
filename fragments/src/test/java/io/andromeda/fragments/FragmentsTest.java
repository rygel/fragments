package io.andromeda.fragments;

import org.junit.Test;
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

/**
 * Created by Alexander on 11.01.2017.
 */
public class FragmentsTest {

    @Test
    public void testLanguageFragments() throws Exception {
        String currentPath = System.getProperty("user.dir");
        Configuration configuration = new Configuration("Test", "/", Paths.get(currentPath + "/src/test/resources/languages/item.md"));
        Fragment fragment = new Fragment("test", "de", configuration);
        String result = Utilities.removeTrailingSlash("/");
        //assertThat(expected, equalTo(result));
    }

    @Test
    public void testFragmentsTagsAndCategories() throws Exception {
        int expectedSizeAllTags = 4;
        String currentPath = System.getProperty("user.dir");
        Configuration configuration = new Configuration("Test", "/", Paths.get(currentPath + "/src/test/resources/fragments/tests/"));
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
        Configuration configuration = new Configuration(fragmentsName, "/", Paths.get(currentPath + "/src/test/resources/fragments/tests/"));
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
        Configuration configuration = new Configuration("order", "/", Paths.get(currentPath + "/src/test/resources/fragments/tests/order/"));
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
        Configuration configuration = new Configuration("order", "/", Paths.get(currentPath + "/src/test/resources/fragments/tests/general/"));
        Fragments fragments = new Fragments(new Application(), configuration);
        List<Fragment> items = fragments.getFragments(true);
        Fragment fragment = items.get(0);
        assertThat(fragment.getTitle(), equalTo("This is a Test"));
        assertThat(fragment.slug, equalTo("this_is_test"));
        assertThat(fragment.getDate(), equalTo(expectedDate));
        assertThat(fragment.getOrder(), equalTo(-100));
        assertThat(fragment.visible, equalTo(false));
    }

}
