package io.andromeda.fragments;

import org.junit.Test;

import java.nio.file.Paths;

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

}
