package io.andromeda.fragments;

import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.Appender;
import io.andromeda.fragments.types.FrontMatterType;
import io.andromeda.fragments.types.RouteType;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.ArgumentMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;

import static org.hamcrest.CoreMatchers.not;
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

        Fragment fragment = new Fragment("file_not_found.md", "en",
                new Configuration("Test", "/", Paths.get(""), "", "",
                        0));

        verify(mockAppender).doAppend(argThat(new ArgumentMatcher() {
            @Override
            public boolean matches(final Object argument) {
                return ((LoggingEvent)argument).getFormattedMessage().contains("Cannot load file \"file_not_found.md\"");
            }
        }));
    }

    @Test
    public void testFragmentGeneral() {
        Path directory = Paths.get(System.getProperty("user.dir"), "/src/test/resources/fragments/tests/blog/");
        String expectedFilename = Paths.get(directory.toString(), "/blog_post_date_time.md").toString();
        Configuration configuration = new Configuration("Test", "/blog", Paths.get(""),
                "", "", 0);
        String expectedDefaultLanguage = "en";
        Fragment staticPage = new Fragment(expectedFilename, expectedDefaultLanguage, configuration);
        Path result = Paths.get(staticPage.getDirectory());
        assertThat(result, equalTo(directory));
        String expectedContent = "<p>Text<!--more-->The rest.</p>\n";
        String content = staticPage.getContent();
        assertThat(content, equalTo(expectedContent));
        Date expectedDate = new Date(1484216100000L);
        Date date = staticPage.getDate();
        assertThat(date, equalTo(expectedDate));
        ZonedDateTime expectedZonedDateTime = ZonedDateTime.of(2017, 01, 12, 10, 15, 00, 00, ZoneId.of("UTC"));
        ZonedDateTime zonedDateTime = staticPage.getDateTime();
        assertThat(zonedDateTime, equalTo(expectedZonedDateTime));
        String defaultLanguage = staticPage.getDefaultLanguage();
        assertThat(defaultLanguage, equalTo(expectedDefaultLanguage));
        String filename2 = staticPage.getFilename();
        assertThat(filename2, equalTo(expectedFilename));
        FrontMatterType expectedFrontMatterType = FrontMatterType.YAML;
        FrontMatterType frontMatterType = staticPage.getFrontMatterType();
        assertThat(frontMatterType, equalTo(expectedFrontMatterType));
        String expectedTemplate = "static";
        String template = staticPage.getTemplate();
        assertThat(template, equalTo(expectedTemplate));
    }

    @Test
    public void testFragmentTags() throws Exception {
        ch.qos.logback.classic.Logger root = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(ch.qos.logback.classic.Logger.ROOT_LOGGER_NAME);
        final Appender mockAppender = mock(Appender.class);
        when(mockAppender.getName()).thenReturn("MOCK");
        root.addAppender(mockAppender);

        Fragment fragment = new Fragment(System.getProperty("user.dir") + "/src/test/resources/fragments/tests/categories_and_tags_json.md", "en",
                new Configuration("Test", "/", Paths.get(""), "", "", 0));
        List tags = (List)fragment.getFrontMatter().get("tags");
        int size = tags.size();
        System.out.print(tags.get(0));
    }

    @Test @SuppressWarnings("unchecked")
    public void testEmptyFragment() throws Exception {
        ch.qos.logback.classic.Logger root = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(ch.qos.logback.classic.Logger.ROOT_LOGGER_NAME);
        final Appender mockAppender = mock(Appender.class);
        when(mockAppender.getName()).thenReturn("MOCK");
        root.addAppender(mockAppender);
        Fragment staticPage = new Fragment(System.getProperty("user.dir") + "/src/test/resources/fragments/blog/empty_post.md", "en",
                new Configuration("Test", "/", Paths.get(""), "", "", 0));

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
        Fragment staticPage = new Fragment(System.getProperty("user.dir") + "/src/test/resources/fragments/blog/no_front_matter.md", "en",
                new Configuration("Test", "/", Paths.get(""), "", "", 0));

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
        Fragment staticPage = new Fragment(System.getProperty("user.dir") + "/src/test/resources/fragments/blog/no_slug.md", "en",
                new Configuration("Test", "/", Paths.get(""), "", "", 0));
        String result = staticPage.getSlug();
        assertThat(result, equalTo(expected));
    }

    @Test
    public void testPreviewTextOnly() {
        String expected = "No HTML Tags";
        Fragment staticPage = new Fragment(System.getProperty("user.dir") + "/src/test/resources/fragments/blog/preview_text_only.md", "en",
                new Configuration("Test", "/", Paths.get(""), "", "", 0));
        String result = staticPage.getPreviewTextOnly();
        assertThat(result, equalTo(expected));
    }

    @Test
    public void testDateOnly() {
        String expected = "/blog/2017/01/12/blog_post_date_only";
        Configuration configuration = new Configuration("Test", "/blog", Paths.get(""), "", "", 0);
        configuration.setRouteType(RouteType.BLOG);
        Fragment staticPage = new Fragment(System.getProperty("user.dir") + "/src/test/resources/fragments/tests/blog/blog_post_date_only.md", "en", configuration);
        String result = staticPage.getUrl();
        assertThat(result, equalTo(expected));
    }

    @Test
    public void testDateTime() {
        String expected = "/blog/2017/01/12/blog_post_date_time";
        Configuration configuration = new Configuration("Test", "/blog", Paths.get(""), "", "", 0);
        configuration.setRouteType(RouteType.BLOG);
        Fragment staticPage = new Fragment(System.getProperty("user.dir") + "/src/test/resources/fragments/tests/blog/blog_post_date_time.md", "en", configuration);
        String result = staticPage.getUrl();
        assertThat(result, equalTo(expected));
    }

    @Test
    public void testLanguage() {
        String expected = "Quisque lectus magna, cursus non augue quis, blandit sollicitudin augue. Curabitur eget leo risus. Vivamus viverra nisi nec leo laoreet, vitae commodo nunc faucibus. Pellentesque non mauris ex. Proin blandit elementum sapien, ac viverra magna fermentum vel. Aliquam consectetur orci dui, at euismod libero ullamcorper et. Donec interdum vestibulum ligula, eget ultrices tortor convallis ut. Fusce at malesuada eros, nec luctus mauris. Pellentesque maximus ornare nibh, ac scelerisque sapien lacinia ut. Curabitur enim nunc, dictum at pretium sed, volutpat nec odio. Nunc eu erat augue. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Proin elementum sit amet nisi eu rhoncus. Cras dolor dolor, posuere vel erat at, pellentesque feugiat urna.";
        Fragment staticPage = new Fragment(System.getProperty("user.dir") + "/src/test/resources/fragments/tests/blog/blog_post_2.md", "en", new Configuration("Test", "/", Paths.get(""), "", "", 0));
        String result = staticPage.getPreviewTextOnly();
        assertThat(result, equalTo(expected));
    }

    @Test
    public void testMarkdownTables() {
        String expected = "<table>\n" +
                "<thead>\n" +
                "<tr><th align=\"left\">Header 1</th><th align=\"left\">Header 2</th></tr>\n" +
                "</thead>\n" +
                "<tbody>\n" +
                "<tr><td align=\"left\">Row 1</td><td align=\"left\">Row 2</td></tr>\n" +
                "</tbody>\n" +
                "</table>\n";
        Configuration configuration = new Configuration("Test", "/test", Paths.get(""), "", "", 0);
        Fragment staticPage = new Fragment(System.getProperty("user.dir") + "/src/test/resources/fragments/tests/tables/tables.md", "en", configuration);
        String result = staticPage.getContent();
        assertThat(result, equalTo(expected));
    }

    @Test
    public void testCompareFragmentsEqual() {
        Fragment expected = new Fragment(System.getProperty("user.dir") + "/src/test/resources/fragments/tests/blog/blog_post_2.md", "en",
                new Configuration("Test", "/", Paths.get(""), "", "", 0));
        Fragment result = expected;
        assertThat(result, equalTo(expected));
    }

    @Test
    public void testCompareFragmentsNotEqual() {
        Fragment expected = new Fragment(System.getProperty("user.dir") + "/src/test/resources/fragments/tests/blog/blog_post_2.md", "en", new Configuration("Test", "/", Paths.get(""), "", "", 0));
        Fragment result = new Fragment(System.getProperty("user.dir") + "/src/test/resources/fragments/tests/tables/tables.md", "en", new Configuration("Test", "/", Paths.get(""), "", "", 0));
        assertThat(result, not(expected));
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
