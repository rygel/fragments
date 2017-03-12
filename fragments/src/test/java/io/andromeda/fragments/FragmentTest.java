package io.andromeda.fragments;

import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.Appender;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.ArgumentMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Paths;
import java.util.List;

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


    @Test @SuppressWarnings("unchecked")
    public void testFragmentFileNotFound() throws Exception {
        ch.qos.logback.classic.Logger root = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(ch.qos.logback.classic.Logger.ROOT_LOGGER_NAME);
        final Appender mockAppender = mock(Appender.class);
        when(mockAppender.getName()).thenReturn("MOCK");
        root.addAppender(mockAppender);

        Fragment fragment = new Fragment("file_not_found.md", "en", new Configuration("Test", "/", Paths.get("")));

        verify(mockAppender).doAppend(argThat(new ArgumentMatcher() {
            @Override
            public boolean matches(final Object argument) {
                return ((LoggingEvent)argument).getFormattedMessage().contains("Cannot load file \"file_not_found.md\"");
            }
        }));
    }

    @Test
    public void testFragmentTags() throws Exception {
        ch.qos.logback.classic.Logger root = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(ch.qos.logback.classic.Logger.ROOT_LOGGER_NAME);
        final Appender mockAppender = mock(Appender.class);
        when(mockAppender.getName()).thenReturn("MOCK");
        root.addAppender(mockAppender);

        Fragment fragment = new Fragment(System.getProperty("user.dir") + "/src/test/resources/fragments/tests/categories_and_tags_json.md", "en", new Configuration("Test", "/", Paths.get("")));
        List tags = (List)fragment.frontMatter.get("tags");
        int size = tags.size();
        System.out.print(tags.get(0));
    }



    @Test @SuppressWarnings("unchecked")
    public void testEmptyFragment() throws Exception {
        ch.qos.logback.classic.Logger root = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(ch.qos.logback.classic.Logger.ROOT_LOGGER_NAME);
        final Appender mockAppender = mock(Appender.class);
        when(mockAppender.getName()).thenReturn("MOCK");
        root.addAppender(mockAppender);
        Fragment staticPage = new Fragment(System.getProperty("user.dir") + "/src/test/resources/fragments/blog/empty_post.md", "en", new Configuration("Test", "/", Paths.get("")));

        verify(mockAppender).doAppend(argThat(new ArgumentMatcher() {
            @Override
            public boolean matches(final Object argument) {
                return ((LoggingEvent)argument).getFormattedMessage().contains("File is empty: ");
            }
        }));
    }


    @Test @SuppressWarnings("unchecked")
    public void testNoFrontMatter() throws Exception {
        ch.qos.logback.classic.Logger root = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(ch.qos.logback.classic.Logger.ROOT_LOGGER_NAME);
        final Appender mockAppender = mock(Appender.class);
        when(mockAppender.getName()).thenReturn("MOCK");
        root.addAppender(mockAppender);
        Fragment staticPage = new Fragment(System.getProperty("user.dir") + "/src/test/resources/fragments/blog/no_front_matter.md", "en", new Configuration("Test", "/", Paths.get("")));

        verify(mockAppender).doAppend(argThat(new ArgumentMatcher() {
            @Override
            public boolean matches(final Object argument) {
                return ((LoggingEvent)argument).getFormattedMessage().contains("YAML/JSON Front Matter is missing in file: ");
            }
        }));
    }


    @Test
    public void testNoFrontMatterSlug() throws Exception {
        String expected = "no_slug";
        Fragment staticPage = new Fragment(System.getProperty("user.dir") + "/src/test/resources/fragments/blog/no_slug.md", "en", new Configuration("Test", "/", Paths.get("")));
        String result = staticPage.slug;
        assertThat(result, equalTo(expected));
    }

    @Test
    public void testPreviewTextOnly() throws Exception {
        String expected = "No HTML Tags";
        Fragment staticPage = new Fragment(System.getProperty("user.dir") + "/src/test/resources/fragments/blog/preview_text_only.md", "en", new Configuration("Test", "/", Paths.get("")));
        String result = staticPage.preview_text_only;
        assertThat(result, equalTo(expected));
    }


    /*@Test
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
    }*/

}
