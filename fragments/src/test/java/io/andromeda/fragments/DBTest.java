package io.andromeda.fragments;

import io.andromeda.fragments.db.DBConfiguration;
import org.junit.Test;
import ro.pippo.core.Application;

import java.nio.file.Paths;
import java.time.Instant;
import java.util.Date;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

/**
 * Created by Alexander on 12.02.2017.
 */
public class DBTest {

    /** Test all properties of a Fragment. */
    @Test
    public void testFragmentsDBSupport() throws Exception {
        Date expectedDate = Date.from(Instant.parse("2017-01-02T00:00:00.000Z"));
        String currentPath = System.getProperty("user.dir");
        Configuration configuration = new Configuration("order", "/", Paths.get(currentPath + "/src/test/resources/fragments/tests/general/"));

        Fragments fragments = new Fragments(new Application(), configuration);
        DBConfiguration dbConfig = new DBConfiguration(fragments);
        //fragments.d
        dbConfig.setResetDB(true);
        fragments.enableDatabase(dbConfig);

        List<Fragment> items = fragments.getFragments(true);
        Fragment fragment = items.get(0);
        assertThat(fragment.getTitle(), equalTo("This is a Test"));
        assertThat(fragment.getSlug(), equalTo("this_is_test"));
        assertThat(fragment.getDate(), equalTo(expectedDate));
        assertThat(fragment.getOrder(), equalTo(-100));
        assertThat(fragment.getVisible(), equalTo(false));
        assertThat(fragment.getPreview().trim(), equalTo("<p>Manual preview</p>"));
    }


}
