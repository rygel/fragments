package io.andromeda.fragments;

import io.andromeda.fragments.Fragment;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.hamcrest.Matchers.equalTo;

/**
 *
 * @author Alexander Brandt
 */
public class FragmentTest extends Assert {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    /*
    @Test
    public void testStaticPageFileNotFound() throws Exception {
        thrown.expect(Exception.class);
        thrown.expectMessage("file_not_found.md (The system cannot find the file specified)");
        Fragment staticPage = new Fragment("file_not_found.md", "/", "");
    }

    @Test
    public void testEmptyFragment() throws Exception {
        thrown.expect(Exception.class);
        thrown.expectMessage("File is empty: ");
        Fragment staticPage = new Fragment(System.getProperty("user.dir") + "/src/test/resources/lyricist/blog/empty_post.md", "/", "");
    }

    @Test
    public void testNoFrontMatter() throws Exception {
        thrown.expect(Exception.class);
        thrown.expectMessage("YAML/JSON Front Matter is missing in file: ");
        Fragment staticPage = new Fragment(System.getProperty("user.dir") + "/src/test/resources/lyricist/blog/no_front_matter.md", "/", "");
    }

    @Test
    public void testNoFrontMatterSlug() throws Exception {
        String expected = "no_slug";
        Fragment staticPage = new Fragment(System.getProperty("user.dir") + "/src/test/resources/lyricist/blog/static_files/no_slug.md", "/", "");
        String result = staticPage.slug;
        assertThat(expected, equalTo(result));
    }
    */

}
