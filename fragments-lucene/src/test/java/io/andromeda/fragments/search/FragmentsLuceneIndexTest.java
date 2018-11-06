package io.andromeda.fragments.search;

import io.andromeda.fragments.Configuration;
import io.andromeda.fragments.Fragments;
import io.andromeda.fragments.search.FragmentsLuceneIndex;
import org.apache.lucene.analysis.de.GermanAnalyzer;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.junit.Assert;
import org.junit.Test;
import ro.pippo.core.Application;

import java.nio.file.Paths;
import java.util.Map;

import static io.andromeda.fragments.search.FragmentsLuceneIndex.PAGES_ID;
import static io.andromeda.fragments.search.FragmentsLuceneIndex.RESULTS_ID;
import static org.hamcrest.CoreMatchers.equalTo;

public class FragmentsLuceneIndexTest extends Assert {

    @Test
    public void testFragmentsLuceneIndex() {
        GermanAnalyzer analyzer = new GermanAnalyzer();
        Directory directory = new RAMDirectory();

        SearchConfiguration configuration = new SearchConfiguration(analyzer, directory);
        assertThat(configuration.getAnalyzer(), equalTo(analyzer));
        assertThat(configuration.getDirectory(), equalTo(directory));
        int hitsPerPage = 10;
        int maxNoInPagination = 5;
        int maximumResults = 10000;
        boolean includingInvisible = false;
        configuration = new SearchConfiguration(analyzer, directory, hitsPerPage, 5, 10000, false);
        assertThat(configuration.getAnalyzer(), equalTo(analyzer));
        assertThat(configuration.getDirectory(), equalTo(directory));
        assertThat(configuration.getHitsPerPage(), equalTo(hitsPerPage));
        assertThat(configuration.getMaxNoInPagination(), equalTo(maxNoInPagination));
        assertThat(configuration.getMaximumResults(), equalTo(maximumResults));
        assertThat(configuration.getIncludingInvisible(), equalTo(includingInvisible));
        FragmentsLuceneIndex fragmentsLuceneIndex = new FragmentsLuceneIndex(configuration);
        String currentPath = System.getProperty("user.dir");
        Configuration fragmentsConfiguration = new Configuration("Search", "/",
                Paths.get(currentPath + "/src/test/resources/fragments/tests/feeds"), "", "");
        Fragments fragments = new Fragments(new Application(), fragmentsConfiguration);
        fragmentsLuceneIndex.addFragments(fragments);
        fragmentsLuceneIndex.createIndex();
        Map<String, Object> context = fragmentsLuceneIndex.search2(1, "fragment");
        assertThat(context.size(), equalTo(3));
        assertThat(context.get(PAGES_ID), equalTo(1));
        Map<String, Object> results = (Map<String, Object>)context.get(RESULTS_ID);
        assertThat(results.size(), equalTo(2));
    }

}