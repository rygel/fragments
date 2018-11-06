package io.andromeda.fragments.feeds;

import io.andromeda.fragments.Configuration;
import io.andromeda.fragments.Fragments;
import org.junit.Assert;
import org.junit.Test;
import ro.pippo.core.Application;

import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.equalTo;

public class FragmentsFeedTest extends Assert {

    @Test
    public void testFragmentsFeed() throws Exception {
        final DateFormat DATE_PARSER = new SimpleDateFormat("yyyy-MM-dd");

        String currentPath = System.getProperty("user.dir");
        Configuration fragmentsConfiguration = new Configuration("Feeds", "/",
                Paths.get(currentPath + "/src/test/resources/fragments/tests/feeds"), "", "");
        Fragments fragments = new Fragments(new Application(), fragmentsConfiguration);

        FeedConfiguration configuration = new FeedConfiguration(FeedType.RSS_2_0, Paths.get("feeds.xml"), "Test Title",
                "Test Description", "TEST Link", DATE_PARSER.parse("2004-06-08"),
                false, false);
        FragmentsFeed fragmentsFeed = new FragmentsFeed(configuration);
        fragmentsFeed.createFeed(fragments);
        //assertThat(expected, equalTo(result));
    }

}