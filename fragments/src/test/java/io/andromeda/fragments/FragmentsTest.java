package io.andromeda.fragments;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

/**
 * Created by Alexander on 11.01.2017.
 */
public class FragmentsTest {

    @Test
    public void testLanguageFragments() throws Exception {
        Configuration configuration = Configuration.getDefault("Test");
        String currentPath = System.getProperty("user.dir");
        Fragment fragment = new Fragment(currentPath + "/src/test/resources/languages/item.md", "/", "", "de", configuration);

        String result = Utilities.removeTrailingSlash("/");
        //assertThat(expected, equalTo(result));
    }

}
