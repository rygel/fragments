package io.andromeda.fragments;

import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.Appender;
import io.andromeda.fragments.Fragment;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.ArgumentMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 *
 * @author Alexander Brandt
 */
public class FragmentTest extends Assert {
    private static Logger logger = LoggerFactory.getLogger(FragmentTest.class);

    @Rule
    public ExpectedException thrown = ExpectedException.none();


    @Test
    public void testFragmentFileNotFound() throws Exception {
        ch.qos.logback.classic.Logger root = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(ch.qos.logback.classic.Logger.ROOT_LOGGER_NAME);
        final Appender mockAppender = mock(Appender.class);
        when(mockAppender.getName()).thenReturn("MOCK");
        root.addAppender(mockAppender);

        Fragment fragment = new Fragment("file_not_found.md", "/", "", "en", Configuration.getDefault());

        verify(mockAppender).doAppend(argThat(new ArgumentMatcher() {
            @Override
            public boolean matches(final Object argument) {
                return ((LoggingEvent)argument).getFormattedMessage().contains("Error reading file (file_not_found.md): java.lang.Exception: file_not_found.md (The system cannot find the file specified)");
            }
        }));
    }

    /*
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

    @Test
    public void testFragmentDateIsShort() throws Exception {
        ch.qos.logback.classic.Logger root = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(ch.qos.logback.classic.Logger.ROOT_LOGGER_NAME);
        final Appender mockAppender = mock(Appender.class);
        when(mockAppender.getName()).thenReturn("MOCK");
        root.addAppender(mockAppender);

        Fragment fragment = new Fragment(System.getProperty("user.dir") + "/src/test/resources/fragments/blog/date_only.md", "/", "", "en", Configuration.getDefault());

        verify(mockAppender).doAppend(argThat(new ArgumentMatcher() {
            @Override
            public boolean matches(final Object argument) {
                return ((LoggingEvent)argument).getFormattedMessage().contains("Error reading file (file_not_found.md): java.lang.Exception: file_not_found.md (The system cannot find the file specified)");
            }
        }));
    }

}
