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
                Paths.get(currentPath + "/src/test/resources/fragments/tests/search"), "", "");
        Fragments fragments = new Fragments(new Application(), fragmentsConfiguration);
        fragmentsLuceneIndex.addFragments(fragments);
        fragmentsLuceneIndex.createIndex();
        Map<String, Object> context = fragmentsLuceneIndex.search2(1, "fragment");
        assertThat(context.size(), equalTo(3));
        assertThat(context.get(PAGES_ID), equalTo(1));
        Map<String, Object> results = (Map<String, Object>)context.get(RESULTS_ID);
        assertThat(results.size(), equalTo(2));
    }

    @Test
    public void testCalculatePaginationPage1Of10() throws Exception {
        SearchConfiguration configuration = new SearchConfiguration(null, null);
        FragmentsLuceneIndex fragmentsLuceneIndex = new FragmentsLuceneIndex(configuration);
        Map<String, Object> context = fragmentsLuceneIndex.calculatePagination(1, 100);
        // The list has 9 items, 5 pages and "first", "last" and "previous", "next"
        int expectedItems = 9;
        assertThat(context.size(), equalTo(expectedItems));
        // Check the first item
        Map<String, Object> firstItem = (Map)context.get("first");
        String expectedFirstText = "first";
        assertThat(firstItem.get("text"), equalTo(expectedFirstText));
        String expectedFirstActive = "false";
        assertThat(firstItem.get("active"), equalTo(expectedFirstActive));
        String expectedFirstUrl = "1";
        assertThat(firstItem.get("url"), equalTo(expectedFirstUrl));
        // Check the previous item
        Map<String, Object> previousItem = (Map)context.get("previous");
        String expectedPreviousText = "previous";
        assertThat(previousItem.get("text"), equalTo(expectedPreviousText));
        String expectedPreviousActive = "false";
        assertThat(previousItem.get("active"), equalTo(expectedPreviousActive));
        String expectedPreviousUrl = "1";
        assertThat(previousItem.get("url"), equalTo(expectedPreviousUrl));

        // Check the 1 item
        Map<String, Object> oneItem = (Map)context.get("1");
        String expectedOneText = "1";
        assertThat(oneItem.get("text"), equalTo(expectedOneText));
        String expectedOneActive = "true";
        assertThat(oneItem.get("active"), equalTo(expectedOneActive));
        String expectedOneUrl = "1";
        assertThat(oneItem.get("url"), equalTo(expectedOneUrl));
        // Check the 2 item
        Map<String, Object> secondItem = (Map)context.get("2");
        String expectedSecondText = "2";
        assertThat(secondItem.get("text"), equalTo(expectedSecondText));
        String expectedSecondActive = "true";
        assertThat(secondItem.get("active"), equalTo(expectedSecondActive));
        String expectedSecondUrl = "2";
        assertThat(secondItem.get("url"), equalTo(expectedSecondUrl));
        // Check the 3 item
        Map<String, Object> thirdItem = (Map)context.get("3");
        String expectedThirdText = "3";
        assertThat(thirdItem.get("text"), equalTo(expectedThirdText));
        String expectedThirdActive = "true";
        assertThat(thirdItem.get("active"), equalTo(expectedThirdActive));
        String expectedThirdUrl = "3";
        assertThat(thirdItem.get("url"), equalTo(expectedThirdUrl));
        // Check the 4 item
        Map<String, Object> fourthItem = (Map)context.get("4");
        String expectedFourthText = "4";
        assertThat(fourthItem.get("text"), equalTo(expectedFourthText));
        String expectedFourthActive = "true";
        assertThat(fourthItem.get("active"), equalTo(expectedFourthActive));
        String expectedFourthUrl = "4";
        assertThat(fourthItem.get("url"), equalTo(expectedFourthUrl));
        // Check the 5 item
        Map<String, Object> fifthItem = (Map)context.get("5");
        String expectedFifthText = "5";
        assertThat(fifthItem.get("text"), equalTo(expectedFifthText));
        String expectedFifthActive = "true";
        assertThat(fifthItem.get("active"), equalTo(expectedFifthActive));
        String expectedFifthUrl = "5";
        assertThat(fifthItem.get("url"), equalTo(expectedFifthUrl));

        // Check the next item
        Map<String, Object> nextItem = (Map)context.get("next");
        String expectedNextText = "next";
        assertThat(nextItem.get("text"), equalTo(expectedNextText));
        String expectedNextActive = "true";
        assertThat(nextItem.get("active"), equalTo(expectedNextActive));
        String expectedNextUrl = "2";
        assertThat(nextItem.get("url"), equalTo(expectedNextUrl));
        // Check the last item
        Map<String, Object> lastItem = (Map)context.get("last");
        String expectedLastText = "last";
        assertThat(lastItem.get("text"), equalTo(expectedLastText));
        String expectedLastActive = "true";
        assertThat(lastItem.get("active"), equalTo(expectedLastActive));
        String expectedLastUrl = "10";
        assertThat(lastItem.get("url"), equalTo(expectedLastUrl));
    }

    @Test
    public void testCalculatePaginationPage2Of10() throws Exception {
        SearchConfiguration configuration = new SearchConfiguration(null, null);
        FragmentsLuceneIndex fragmentsLuceneIndex = new FragmentsLuceneIndex(configuration);
        Map<String, Object> context = fragmentsLuceneIndex.calculatePagination(2, 100);
        // The list has 9 items, 5 pages and "first", "last" and "previous", "next"
        int expectedItems = 9;
        assertThat(context.size(), equalTo(expectedItems));
        // Check the first item
        Map<String, Object> firstItem = (Map)context.get("first");
        String expectedFirstText = "first";
        assertThat(firstItem.get("text"), equalTo(expectedFirstText));
        String expectedFirstActive = "true";
        assertThat(firstItem.get("active"), equalTo(expectedFirstActive));
        String expectedFirstUrl = "1";
        assertThat(firstItem.get("url"), equalTo(expectedFirstUrl));
        // Check the previous item
        Map<String, Object> previousItem = (Map)context.get("previous");
        String expectedPreviousText = "previous";
        assertThat(previousItem.get("text"), equalTo(expectedPreviousText));
        String expectedPreviousActive = "true";
        assertThat(previousItem.get("active"), equalTo(expectedPreviousActive));
        String expectedPreviousUrl = "1";
        assertThat(previousItem.get("url"), equalTo(expectedPreviousUrl));

        // Check the 1 item
        Map<String, Object> oneItem = (Map)context.get("1");
        String expectedOneText = "1";
        assertThat(oneItem.get("text"), equalTo(expectedOneText));
        String expectedOneActive = "true";
        assertThat(oneItem.get("active"), equalTo(expectedOneActive));
        String expectedOneUrl = "1";
        assertThat(oneItem.get("url"), equalTo(expectedOneUrl));
        // Check the 2 item
        Map<String, Object> secondItem = (Map)context.get("2");
        String expectedSecondText = "2";
        assertThat(secondItem.get("text"), equalTo(expectedSecondText));
        String expectedSecondActive = "true";
        assertThat(secondItem.get("active"), equalTo(expectedSecondActive));
        String expectedSecondUrl = "2";
        assertThat(secondItem.get("url"), equalTo(expectedSecondUrl));
        // Check the 3 item
        Map<String, Object> thirdItem = (Map)context.get("3");
        String expectedThirdText = "3";
        assertThat(thirdItem.get("text"), equalTo(expectedThirdText));
        String expectedThirdActive = "true";
        assertThat(thirdItem.get("active"), equalTo(expectedThirdActive));
        String expectedThirdUrl = "3";
        assertThat(thirdItem.get("url"), equalTo(expectedThirdUrl));
        // Check the 4 item
        Map<String, Object> fourthItem = (Map)context.get("4");
        String expectedFourthText = "4";
        assertThat(fourthItem.get("text"), equalTo(expectedFourthText));
        String expectedFourthActive = "true";
        assertThat(fourthItem.get("active"), equalTo(expectedFourthActive));
        String expectedFourthUrl = "4";
        assertThat(fourthItem.get("url"), equalTo(expectedFourthUrl));
        // Check the 5 item
        Map<String, Object> fifthItem = (Map)context.get("5");
        String expectedFifthText = "5";
        assertThat(fifthItem.get("text"), equalTo(expectedFifthText));
        String expectedFifthActive = "true";
        assertThat(fifthItem.get("active"), equalTo(expectedFifthActive));
        String expectedFifthUrl = "5";
        assertThat(fifthItem.get("url"), equalTo(expectedFifthUrl));

        // Check the next item
        Map<String, Object> nextItem = (Map)context.get("next");
        String expectedNextText = "next";
        assertThat(nextItem.get("text"), equalTo(expectedNextText));
        String expectedNextActive = "true";
        assertThat(nextItem.get("active"), equalTo(expectedNextActive));
        String expectedNextUrl = "3";
        assertThat(nextItem.get("url"), equalTo(expectedNextUrl));
        // Check the last item
        Map<String, Object> lastItem = (Map)context.get("last");
        String expectedLastText = "last";
        assertThat(lastItem.get("text"), equalTo(expectedLastText));
        String expectedLastActive = "true";
        assertThat(lastItem.get("active"), equalTo(expectedLastActive));
        String expectedLastUrl = "10";
        assertThat(lastItem.get("url"), equalTo(expectedLastUrl));
    }

    @Test
    public void testCalculatePaginationPage3Of10() throws Exception {
        SearchConfiguration configuration = new SearchConfiguration(null, null);
        FragmentsLuceneIndex fragmentsLuceneIndex = new FragmentsLuceneIndex(configuration);
        Map<String, Object> context = fragmentsLuceneIndex.calculatePagination(3, 100);
        // The list has 9 items, 5 pages and "first", "last" and "previous", "next"
        int expectedItems = 9;
        assertThat(context.size(), equalTo(expectedItems));
        // Check the first item
        Map<String, Object> firstItem = (Map)context.get("first");
        String expectedFirstText = "first";
        assertThat(firstItem.get("text"), equalTo(expectedFirstText));
        String expectedFirstActive = "true";
        assertThat(firstItem.get("active"), equalTo(expectedFirstActive));
        String expectedFirstUrl = "1";
        assertThat(firstItem.get("url"), equalTo(expectedFirstUrl));
        // Check the previous item
        Map<String, Object> previousItem = (Map)context.get("previous");
        String expectedPreviousText = "previous";
        assertThat(previousItem.get("text"), equalTo(expectedPreviousText));
        String expectedPreviousActive = "true";
        assertThat(previousItem.get("active"), equalTo(expectedPreviousActive));
        String expectedPreviousUrl = "2";
        assertThat(previousItem.get("url"), equalTo(expectedPreviousUrl));

        // Check the 1 item
        Map<String, Object> oneItem = (Map)context.get("1");
        String expectedOneText = "1";
        assertThat(oneItem.get("text"), equalTo(expectedOneText));
        String expectedOneActive = "true";
        assertThat(oneItem.get("active"), equalTo(expectedOneActive));
        String expectedOneUrl = "1";
        assertThat(oneItem.get("url"), equalTo(expectedOneUrl));
        // Check the 2 item
        Map<String, Object> secondItem = (Map)context.get("2");
        String expectedSecondText = "2";
        assertThat(secondItem.get("text"), equalTo(expectedSecondText));
        String expectedSecondActive = "true";
        assertThat(secondItem.get("active"), equalTo(expectedSecondActive));
        String expectedSecondUrl = "2";
        assertThat(secondItem.get("url"), equalTo(expectedSecondUrl));
        // Check the 3 item
        Map<String, Object> thirdItem = (Map)context.get("3");
        String expectedThirdText = "3";
        assertThat(thirdItem.get("text"), equalTo(expectedThirdText));
        String expectedThirdActive = "true";
        assertThat(thirdItem.get("active"), equalTo(expectedThirdActive));
        String expectedThirdUrl = "3";
        assertThat(thirdItem.get("url"), equalTo(expectedThirdUrl));
        // Check the 4 item
        Map<String, Object> fourthItem = (Map)context.get("4");
        String expectedFourthText = "4";
        assertThat(fourthItem.get("text"), equalTo(expectedFourthText));
        String expectedFourthActive = "true";
        assertThat(fourthItem.get("active"), equalTo(expectedFourthActive));
        String expectedFourthUrl = "4";
        assertThat(fourthItem.get("url"), equalTo(expectedFourthUrl));
        // Check the 5 item
        Map<String, Object> fifthItem = (Map)context.get("5");
        String expectedFifthText = "5";
        assertThat(fifthItem.get("text"), equalTo(expectedFifthText));
        String expectedFifthActive = "true";
        assertThat(fifthItem.get("active"), equalTo(expectedFifthActive));
        String expectedFifthUrl = "5";
        assertThat(fifthItem.get("url"), equalTo(expectedFifthUrl));

        // Check the next item
        Map<String, Object> nextItem = (Map)context.get("next");
        String expectedNextText = "next";
        assertThat(nextItem.get("text"), equalTo(expectedNextText));
        String expectedNextActive = "true";
        assertThat(nextItem.get("active"), equalTo(expectedNextActive));
        String expectedNextUrl = "4";
        assertThat(nextItem.get("url"), equalTo(expectedNextUrl));
        // Check the last item
        Map<String, Object> lastItem = (Map)context.get("last");
        String expectedLastText = "last";
        assertThat(lastItem.get("text"), equalTo(expectedLastText));
        String expectedLastActive = "true";
        assertThat(lastItem.get("active"), equalTo(expectedLastActive));
        String expectedLastUrl = "10";
        assertThat(lastItem.get("url"), equalTo(expectedLastUrl));
    }

    @Test
    public void testCalculatePaginationPage4Of10() throws Exception {
        SearchConfiguration configuration = new SearchConfiguration(null, null);
        FragmentsLuceneIndex fragmentsLuceneIndex = new FragmentsLuceneIndex(configuration);
        Map<String, Object> context = fragmentsLuceneIndex.calculatePagination(4, 100);
        // The list has 9 items, 5 pages and "first", "last" and "previous", "next"
        int expectedItems = 9;
        assertThat(context.size(), equalTo(expectedItems));
        // Check the first item
        Map<String, Object> firstItem = (Map)context.get("first");
        String expectedFirstText = "first";
        assertThat(firstItem.get("text"), equalTo(expectedFirstText));
        String expectedFirstActive = "true";
        assertThat(firstItem.get("active"), equalTo(expectedFirstActive));
        String expectedFirstUrl = "1";
        assertThat(firstItem.get("url"), equalTo(expectedFirstUrl));
        // Check the previous item
        Map<String, Object> previousItem = (Map)context.get("previous");
        String expectedPreviousText = "previous";
        assertThat(previousItem.get("text"), equalTo(expectedPreviousText));
        String expectedPreviousActive = "true";
        assertThat(previousItem.get("active"), equalTo(expectedPreviousActive));
        String expectedPreviousUrl = "3";
        assertThat(previousItem.get("url"), equalTo(expectedPreviousUrl));

        // Check the 1 item
        Map<String, Object> oneItem = (Map)context.get("1");
        String expectedOneText = "2";
        assertThat(oneItem.get("text"), equalTo(expectedOneText));
        String expectedOneActive = "true";
        assertThat(oneItem.get("active"), equalTo(expectedOneActive));
        String expectedOneUrl = "2";
        assertThat(oneItem.get("url"), equalTo(expectedOneUrl));
        // Check the 2 item
        Map<String, Object> secondItem = (Map)context.get("2");
        String expectedSecondText = "3";
        assertThat(secondItem.get("text"), equalTo(expectedSecondText));
        String expectedSecondActive = "true";
        assertThat(secondItem.get("active"), equalTo(expectedSecondActive));
        String expectedSecondUrl = "3";
        assertThat(secondItem.get("url"), equalTo(expectedSecondUrl));
        // Check the 3 item
        Map<String, Object> thirdItem = (Map)context.get("3");
        String expectedThirdText = "4";
        assertThat(thirdItem.get("text"), equalTo(expectedThirdText));
        String expectedThirdActive = "true";
        assertThat(thirdItem.get("active"), equalTo(expectedThirdActive));
        String expectedThirdUrl = "4";
        assertThat(thirdItem.get("url"), equalTo(expectedThirdUrl));
        // Check the 4 item
        Map<String, Object> fourthItem = (Map)context.get("4");
        String expectedFourthText = "5";
        assertThat(fourthItem.get("text"), equalTo(expectedFourthText));
        String expectedFourthActive = "true";
        assertThat(fourthItem.get("active"), equalTo(expectedFourthActive));
        String expectedFourthUrl = "5";
        assertThat(fourthItem.get("url"), equalTo(expectedFourthUrl));
        // Check the 5 item
        Map<String, Object> fifthItem = (Map)context.get("5");
        String expectedFifthText = "6";
        assertThat(fifthItem.get("text"), equalTo(expectedFifthText));
        String expectedFifthActive = "true";
        assertThat(fifthItem.get("active"), equalTo(expectedFifthActive));
        String expectedFifthUrl = "6";
        assertThat(fifthItem.get("url"), equalTo(expectedFifthUrl));

        // Check the next item
        Map<String, Object> nextItem = (Map)context.get("next");
        String expectedNextText = "next";
        assertThat(nextItem.get("text"), equalTo(expectedNextText));
        String expectedNextActive = "true";
        assertThat(nextItem.get("active"), equalTo(expectedNextActive));
        String expectedNextUrl = "5";
        assertThat(nextItem.get("url"), equalTo(expectedNextUrl));
        // Check the last item
        Map<String, Object> lastItem = (Map)context.get("last");
        String expectedLastText = "last";
        assertThat(lastItem.get("text"), equalTo(expectedLastText));
        String expectedLastActive = "true";
        assertThat(lastItem.get("active"), equalTo(expectedLastActive));
        String expectedLastUrl = "10";
        assertThat(lastItem.get("url"), equalTo(expectedLastUrl));
    }

    @Test
    public void testCalculatePaginationPage5Of10() throws Exception {
        SearchConfiguration configuration = new SearchConfiguration(null, null);
        FragmentsLuceneIndex fragmentsLuceneIndex = new FragmentsLuceneIndex(configuration);
        Map<String, Object> context = fragmentsLuceneIndex.calculatePagination(5, 100);
        // The list has 9 items, 5 pages and "first", "last" and "previous", "next"
        int expectedItems = 9;
        assertThat(context.size(), equalTo(expectedItems));
        // Check the first item
        Map<String, Object> firstItem = (Map)context.get("first");
        String expectedFirstText = "first";
        assertThat(firstItem.get("text"), equalTo(expectedFirstText));
        String expectedFirstActive = "true";
        assertThat(firstItem.get("active"), equalTo(expectedFirstActive));
        String expectedFirstUrl = "1";
        assertThat(firstItem.get("url"), equalTo(expectedFirstUrl));
        // Check the previous item
        Map<String, Object> previousItem = (Map)context.get("previous");
        String expectedPreviousText = "previous";
        assertThat(previousItem.get("text"), equalTo(expectedPreviousText));
        String expectedPreviousActive = "true";
        assertThat(previousItem.get("active"), equalTo(expectedPreviousActive));
        String expectedPreviousUrl = "4";
        assertThat(previousItem.get("url"), equalTo(expectedPreviousUrl));

        // Check the 1 item
        Map<String, Object> oneItem = (Map)context.get("1");
        String expectedOneText = "3";
        assertThat(oneItem.get("text"), equalTo(expectedOneText));
        String expectedOneActive = "true";
        assertThat(oneItem.get("active"), equalTo(expectedOneActive));
        String expectedOneUrl = "3";
        assertThat(oneItem.get("url"), equalTo(expectedOneUrl));
        // Check the 2 item
        Map<String, Object> secondItem = (Map)context.get("2");
        String expectedSecondText = "4";
        assertThat(secondItem.get("text"), equalTo(expectedSecondText));
        String expectedSecondActive = "true";
        assertThat(secondItem.get("active"), equalTo(expectedSecondActive));
        String expectedSecondUrl = "4";
        assertThat(secondItem.get("url"), equalTo(expectedSecondUrl));
        // Check the 3 item
        Map<String, Object> thirdItem = (Map)context.get("3");
        String expectedThirdText = "5";
        assertThat(thirdItem.get("text"), equalTo(expectedThirdText));
        String expectedThirdActive = "true";
        assertThat(thirdItem.get("active"), equalTo(expectedThirdActive));
        String expectedThirdUrl = "5";
        assertThat(thirdItem.get("url"), equalTo(expectedThirdUrl));
        // Check the 4 item
        Map<String, Object> fourthItem = (Map)context.get("4");
        String expectedFourthText = "6";
        assertThat(fourthItem.get("text"), equalTo(expectedFourthText));
        String expectedFourthActive = "true";
        assertThat(fourthItem.get("active"), equalTo(expectedFourthActive));
        String expectedFourthUrl = "6";
        assertThat(fourthItem.get("url"), equalTo(expectedFourthUrl));
        // Check the 5 item
        Map<String, Object> fifthItem = (Map)context.get("5");
        String expectedFifthText = "7";
        assertThat(fifthItem.get("text"), equalTo(expectedFifthText));
        String expectedFifthActive = "true";
        assertThat(fifthItem.get("active"), equalTo(expectedFifthActive));
        String expectedFifthUrl = "7";
        assertThat(fifthItem.get("url"), equalTo(expectedFifthUrl));

        // Check the next item
        Map<String, Object> nextItem = (Map)context.get("next");
        String expectedNextText = "next";
        assertThat(nextItem.get("text"), equalTo(expectedNextText));
        String expectedNextActive = "true";
        assertThat(nextItem.get("active"), equalTo(expectedNextActive));
        String expectedNextUrl = "6";
        assertThat(nextItem.get("url"), equalTo(expectedNextUrl));
        // Check the last item
        Map<String, Object> lastItem = (Map)context.get("last");
        String expectedLastText = "last";
        assertThat(lastItem.get("text"), equalTo(expectedLastText));
        String expectedLastActive = "true";
        assertThat(lastItem.get("active"), equalTo(expectedLastActive));
        String expectedLastUrl = "10";
        assertThat(lastItem.get("url"), equalTo(expectedLastUrl));
    }

    @Test
    public void testCalculatePaginationPage6Of10() throws Exception {
        SearchConfiguration configuration = new SearchConfiguration(null, null);
        FragmentsLuceneIndex fragmentsLuceneIndex = new FragmentsLuceneIndex(configuration);
        Map<String, Object> context = fragmentsLuceneIndex.calculatePagination(6, 100);
        // The list has 9 items, 5 pages and "first", "last" and "previous", "next"
        int expectedItems = 9;
        assertThat(context.size(), equalTo(expectedItems));
        // Check the first item
        Map<String, Object> firstItem = (Map)context.get("first");
        String expectedFirstText = "first";
        assertThat(firstItem.get("text"), equalTo(expectedFirstText));
        String expectedFirstActive = "true";
        assertThat(firstItem.get("active"), equalTo(expectedFirstActive));
        String expectedFirstUrl = "1";
        assertThat(firstItem.get("url"), equalTo(expectedFirstUrl));
        // Check the previous item
        Map<String, Object> previousItem = (Map)context.get("previous");
        String expectedPreviousText = "previous";
        assertThat(previousItem.get("text"), equalTo(expectedPreviousText));
        String expectedPreviousActive = "true";
        assertThat(previousItem.get("active"), equalTo(expectedPreviousActive));
        String expectedPreviousUrl = "5";
        assertThat(previousItem.get("url"), equalTo(expectedPreviousUrl));

        // Check the 1 item
        Map<String, Object> oneItem = (Map)context.get("1");
        String expectedOneText = "4";
        assertThat(oneItem.get("text"), equalTo(expectedOneText));
        String expectedOneActive = "true";
        assertThat(oneItem.get("active"), equalTo(expectedOneActive));
        String expectedOneUrl = "4";
        assertThat(oneItem.get("url"), equalTo(expectedOneUrl));
        // Check the 2 item
        Map<String, Object> secondItem = (Map)context.get("2");
        String expectedSecondText = "5";
        assertThat(secondItem.get("text"), equalTo(expectedSecondText));
        String expectedSecondActive = "true";
        assertThat(secondItem.get("active"), equalTo(expectedSecondActive));
        String expectedSecondUrl = "5";
        assertThat(secondItem.get("url"), equalTo(expectedSecondUrl));
        // Check the 3 item
        Map<String, Object> thirdItem = (Map)context.get("3");
        String expectedThirdText = "6";
        assertThat(thirdItem.get("text"), equalTo(expectedThirdText));
        String expectedThirdActive = "true";
        assertThat(thirdItem.get("active"), equalTo(expectedThirdActive));
        String expectedThirdUrl = "6";
        assertThat(thirdItem.get("url"), equalTo(expectedThirdUrl));
        // Check the 4 item
        Map<String, Object> fourthItem = (Map)context.get("4");
        String expectedFourthText = "7";
        assertThat(fourthItem.get("text"), equalTo(expectedFourthText));
        String expectedFourthActive = "true";
        assertThat(fourthItem.get("active"), equalTo(expectedFourthActive));
        String expectedFourthUrl = "7";
        assertThat(fourthItem.get("url"), equalTo(expectedFourthUrl));
        // Check the 5 item
        Map<String, Object> fifthItem = (Map)context.get("5");
        String expectedFifthText = "8";
        assertThat(fifthItem.get("text"), equalTo(expectedFifthText));
        String expectedFifthActive = "true";
        assertThat(fifthItem.get("active"), equalTo(expectedFifthActive));
        String expectedFifthUrl = "8";
        assertThat(fifthItem.get("url"), equalTo(expectedFifthUrl));

        // Check the next item
        Map<String, Object> nextItem = (Map)context.get("next");
        String expectedNextText = "next";
        assertThat(nextItem.get("text"), equalTo(expectedNextText));
        String expectedNextActive = "true";
        assertThat(nextItem.get("active"), equalTo(expectedNextActive));
        String expectedNextUrl = "7";
        assertThat(nextItem.get("url"), equalTo(expectedNextUrl));
        // Check the last item
        Map<String, Object> lastItem = (Map)context.get("last");
        String expectedLastText = "last";
        assertThat(lastItem.get("text"), equalTo(expectedLastText));
        String expectedLastActive = "true";
        assertThat(lastItem.get("active"), equalTo(expectedLastActive));
        String expectedLastUrl = "10";
        assertThat(lastItem.get("url"), equalTo(expectedLastUrl));
    }

    @Test
    public void testCalculatePaginationPage7Of10() throws Exception {
        SearchConfiguration configuration = new SearchConfiguration(null, null);
        FragmentsLuceneIndex fragmentsLuceneIndex = new FragmentsLuceneIndex(configuration);
        Map<String, Object> context = fragmentsLuceneIndex.calculatePagination(7, 100);
        // The list has 9 items, 5 pages and "first", "last" and "previous", "next"
        int expectedItems = 9;
        assertThat(context.size(), equalTo(expectedItems));
        // Check the first item
        Map<String, Object> firstItem = (Map)context.get("first");
        String expectedFirstText = "first";
        assertThat(firstItem.get("text"), equalTo(expectedFirstText));
        String expectedFirstActive = "true";
        assertThat(firstItem.get("active"), equalTo(expectedFirstActive));
        String expectedFirstUrl = "1";
        assertThat(firstItem.get("url"), equalTo(expectedFirstUrl));
        // Check the previous item
        Map<String, Object> previousItem = (Map)context.get("previous");
        String expectedPreviousText = "previous";
        assertThat(previousItem.get("text"), equalTo(expectedPreviousText));
        String expectedPreviousActive = "true";
        assertThat(previousItem.get("active"), equalTo(expectedPreviousActive));
        String expectedPreviousUrl = "6";
        assertThat(previousItem.get("url"), equalTo(expectedPreviousUrl));

        // Check the 1 item
        Map<String, Object> oneItem = (Map)context.get("1");
        String expectedOneText = "5";
        assertThat(oneItem.get("text"), equalTo(expectedOneText));
        String expectedOneActive = "true";
        assertThat(oneItem.get("active"), equalTo(expectedOneActive));
        String expectedOneUrl = "5";
        assertThat(oneItem.get("url"), equalTo(expectedOneUrl));
        // Check the 2 item
        Map<String, Object> secondItem = (Map)context.get("2");
        String expectedSecondText = "6";
        assertThat(secondItem.get("text"), equalTo(expectedSecondText));
        String expectedSecondActive = "true";
        assertThat(secondItem.get("active"), equalTo(expectedSecondActive));
        String expectedSecondUrl = "6";
        assertThat(secondItem.get("url"), equalTo(expectedSecondUrl));
        // Check the 3 item
        Map<String, Object> thirdItem = (Map)context.get("3");
        String expectedThirdText = "7";
        assertThat(thirdItem.get("text"), equalTo(expectedThirdText));
        String expectedThirdActive = "true";
        assertThat(thirdItem.get("active"), equalTo(expectedThirdActive));
        String expectedThirdUrl = "7";
        assertThat(thirdItem.get("url"), equalTo(expectedThirdUrl));
        // Check the 4 item
        Map<String, Object> fourthItem = (Map)context.get("4");
        String expectedFourthText = "8";
        assertThat(fourthItem.get("text"), equalTo(expectedFourthText));
        String expectedFourthActive = "true";
        assertThat(fourthItem.get("active"), equalTo(expectedFourthActive));
        String expectedFourthUrl = "8";
        assertThat(fourthItem.get("url"), equalTo(expectedFourthUrl));
        // Check the 5 item
        Map<String, Object> fifthItem = (Map)context.get("5");
        String expectedFifthText = "9";
        assertThat(fifthItem.get("text"), equalTo(expectedFifthText));
        String expectedFifthActive = "true";
        assertThat(fifthItem.get("active"), equalTo(expectedFifthActive));
        String expectedFifthUrl = "9";
        assertThat(fifthItem.get("url"), equalTo(expectedFifthUrl));

        // Check the next item
        Map<String, Object> nextItem = (Map)context.get("next");
        String expectedNextText = "next";
        assertThat(nextItem.get("text"), equalTo(expectedNextText));
        String expectedNextActive = "true";
        assertThat(nextItem.get("active"), equalTo(expectedNextActive));
        String expectedNextUrl = "8";
        assertThat(nextItem.get("url"), equalTo(expectedNextUrl));
        // Check the last item
        Map<String, Object> lastItem = (Map)context.get("last");
        String expectedLastText = "last";
        assertThat(lastItem.get("text"), equalTo(expectedLastText));
        String expectedLastActive = "true";
        assertThat(lastItem.get("active"), equalTo(expectedLastActive));
        String expectedLastUrl = "10";
        assertThat(lastItem.get("url"), equalTo(expectedLastUrl));
    }

    @Test
    public void testCalculatePaginationPage8Of10() throws Exception {
        SearchConfiguration configuration = new SearchConfiguration(null, null);
        FragmentsLuceneIndex fragmentsLuceneIndex = new FragmentsLuceneIndex(configuration);
        Map<String, Object> context = fragmentsLuceneIndex.calculatePagination(8, 100);
        // The list has 9 items, 5 pages and "first", "last" and "previous", "next"
        int expectedItems = 9;
        assertThat(context.size(), equalTo(expectedItems));
        // Check the first item
        Map<String, Object> firstItem = (Map)context.get("first");
        String expectedFirstText = "first";
        assertThat(firstItem.get("text"), equalTo(expectedFirstText));
        String expectedFirstActive = "true";
        assertThat(firstItem.get("active"), equalTo(expectedFirstActive));
        String expectedFirstUrl = "1";
        assertThat(firstItem.get("url"), equalTo(expectedFirstUrl));
        // Check the previous item
        Map<String, Object> previousItem = (Map)context.get("previous");
        String expectedPreviousText = "previous";
        assertThat(previousItem.get("text"), equalTo(expectedPreviousText));
        String expectedPreviousActive = "true";
        assertThat(previousItem.get("active"), equalTo(expectedPreviousActive));
        String expectedPreviousUrl = "7";
        assertThat(previousItem.get("url"), equalTo(expectedPreviousUrl));

        // Check the 1 item
        Map<String, Object> oneItem = (Map)context.get("1");
        String expectedOneText = "6";
        assertThat(oneItem.get("text"), equalTo(expectedOneText));
        String expectedOneActive = "true";
        assertThat(oneItem.get("active"), equalTo(expectedOneActive));
        String expectedOneUrl = "6";
        assertThat(oneItem.get("url"), equalTo(expectedOneUrl));
        // Check the 2 item
        Map<String, Object> secondItem = (Map)context.get("2");
        String expectedSecondText = "7";
        assertThat(secondItem.get("text"), equalTo(expectedSecondText));
        String expectedSecondActive = "true";
        assertThat(secondItem.get("active"), equalTo(expectedSecondActive));
        String expectedSecondUrl = "7";
        assertThat(secondItem.get("url"), equalTo(expectedSecondUrl));
        // Check the 3 item
        Map<String, Object> thirdItem = (Map)context.get("3");
        String expectedThirdText = "8";
        assertThat(thirdItem.get("text"), equalTo(expectedThirdText));
        String expectedThirdActive = "true";
        assertThat(thirdItem.get("active"), equalTo(expectedThirdActive));
        String expectedThirdUrl = "8";
        assertThat(thirdItem.get("url"), equalTo(expectedThirdUrl));
        // Check the 4 item
        Map<String, Object> fourthItem = (Map)context.get("4");
        String expectedFourthText = "9";
        assertThat(fourthItem.get("text"), equalTo(expectedFourthText));
        String expectedFourthActive = "true";
        assertThat(fourthItem.get("active"), equalTo(expectedFourthActive));
        String expectedFourthUrl = "9";
        assertThat(fourthItem.get("url"), equalTo(expectedFourthUrl));
        // Check the 5 item
        Map<String, Object> fifthItem = (Map)context.get("5");
        String expectedFifthText = "10";
        assertThat(fifthItem.get("text"), equalTo(expectedFifthText));
        String expectedFifthActive = "true";
        assertThat(fifthItem.get("active"), equalTo(expectedFifthActive));
        String expectedFifthUrl = "10";
        assertThat(fifthItem.get("url"), equalTo(expectedFifthUrl));

        // Check the next item
        Map<String, Object> nextItem = (Map)context.get("next");
        String expectedNextText = "next";
        assertThat(nextItem.get("text"), equalTo(expectedNextText));
        String expectedNextActive = "true";
        assertThat(nextItem.get("active"), equalTo(expectedNextActive));
        String expectedNextUrl = "9";
        assertThat(nextItem.get("url"), equalTo(expectedNextUrl));
        // Check the last item
        Map<String, Object> lastItem = (Map)context.get("last");
        String expectedLastText = "last";
        assertThat(lastItem.get("text"), equalTo(expectedLastText));
        String expectedLastActive = "true";
        assertThat(lastItem.get("active"), equalTo(expectedLastActive));
        String expectedLastUrl = "10";
        assertThat(lastItem.get("url"), equalTo(expectedLastUrl));
    }

    @Test
    public void testCalculatePaginationPage9Of10() throws Exception {
        SearchConfiguration configuration = new SearchConfiguration(null, null);
        FragmentsLuceneIndex fragmentsLuceneIndex = new FragmentsLuceneIndex(configuration);
        Map<String, Object> context = fragmentsLuceneIndex.calculatePagination(9, 100);
        // The list has 9 items, 5 pages and "first", "last" and "previous", "next"
        int expectedItems = 9;
        assertThat(context.size(), equalTo(expectedItems));
        // Check the first item
        Map<String, Object> firstItem = (Map)context.get("first");
        String expectedFirstText = "first";
        assertThat(firstItem.get("text"), equalTo(expectedFirstText));
        String expectedFirstActive = "true";
        assertThat(firstItem.get("active"), equalTo(expectedFirstActive));
        String expectedFirstUrl = "1";
        assertThat(firstItem.get("url"), equalTo(expectedFirstUrl));
        // Check the previous item
        Map<String, Object> previousItem = (Map)context.get("previous");
        String expectedPreviousText = "previous";
        assertThat(previousItem.get("text"), equalTo(expectedPreviousText));
        String expectedPreviousActive = "true";
        assertThat(previousItem.get("active"), equalTo(expectedPreviousActive));
        String expectedPreviousUrl = "8";
        assertThat(previousItem.get("url"), equalTo(expectedPreviousUrl));

        // Check the 1 item
        Map<String, Object> oneItem = (Map)context.get("1");
        String expectedOneText = "6";
        assertThat(oneItem.get("text"), equalTo(expectedOneText));
        String expectedOneActive = "true";
        assertThat(oneItem.get("active"), equalTo(expectedOneActive));
        String expectedOneUrl = "6";
        assertThat(oneItem.get("url"), equalTo(expectedOneUrl));
        // Check the 2 item
        Map<String, Object> secondItem = (Map)context.get("2");
        String expectedSecondText = "7";
        assertThat(secondItem.get("text"), equalTo(expectedSecondText));
        String expectedSecondActive = "true";
        assertThat(secondItem.get("active"), equalTo(expectedSecondActive));
        String expectedSecondUrl = "7";
        assertThat(secondItem.get("url"), equalTo(expectedSecondUrl));
        // Check the 3 item
        Map<String, Object> thirdItem = (Map)context.get("3");
        String expectedThirdText = "8";
        assertThat(thirdItem.get("text"), equalTo(expectedThirdText));
        String expectedThirdActive = "true";
        assertThat(thirdItem.get("active"), equalTo(expectedThirdActive));
        String expectedThirdUrl = "8";
        assertThat(thirdItem.get("url"), equalTo(expectedThirdUrl));
        // Check the 4 item
        Map<String, Object> fourthItem = (Map)context.get("4");
        String expectedFourthText = "9";
        assertThat(fourthItem.get("text"), equalTo(expectedFourthText));
        String expectedFourthActive = "true";
        assertThat(fourthItem.get("active"), equalTo(expectedFourthActive));
        String expectedFourthUrl = "9";
        assertThat(fourthItem.get("url"), equalTo(expectedFourthUrl));
        // Check the 5 item
        Map<String, Object> fifthItem = (Map)context.get("5");
        String expectedFifthText = "10";
        assertThat(fifthItem.get("text"), equalTo(expectedFifthText));
        String expectedFifthActive = "true";
        assertThat(fifthItem.get("active"), equalTo(expectedFifthActive));
        String expectedFifthUrl = "10";
        assertThat(fifthItem.get("url"), equalTo(expectedFifthUrl));

        // Check the next item
        Map<String, Object> nextItem = (Map)context.get("next");
        String expectedNextText = "next";
        assertThat(nextItem.get("text"), equalTo(expectedNextText));
        String expectedNextActive = "true";
        assertThat(nextItem.get("active"), equalTo(expectedNextActive));
        String expectedNextUrl = "10";
        assertThat(nextItem.get("url"), equalTo(expectedNextUrl));
        // Check the last item
        Map<String, Object> lastItem = (Map)context.get("last");
        String expectedLastText = "last";
        assertThat(lastItem.get("text"), equalTo(expectedLastText));
        String expectedLastActive = "true";
        assertThat(lastItem.get("active"), equalTo(expectedLastActive));
        String expectedLastUrl = "10";
        assertThat(lastItem.get("url"), equalTo(expectedLastUrl));
    }

    @Test
    public void testCalculatePaginationPage10Of10() throws Exception {
        SearchConfiguration configuration = new SearchConfiguration(null, null);
        FragmentsLuceneIndex fragmentsLuceneIndex = new FragmentsLuceneIndex(configuration);
        Map<String, Object> context = fragmentsLuceneIndex.calculatePagination(10, 100);
        // The list has 9 items, 5 pages and "first", "last" and "previous", "next"
        int expectedItems = 9;
        assertThat(context.size(), equalTo(expectedItems));
        // Check the first item
        Map<String, Object> firstItem = (Map)context.get("first");
        String expectedFirstText = "first";
        assertThat(firstItem.get("text"), equalTo(expectedFirstText));
        String expectedFirstActive = "true";
        assertThat(firstItem.get("active"), equalTo(expectedFirstActive));
        String expectedFirstUrl = "1";
        assertThat(firstItem.get("url"), equalTo(expectedFirstUrl));
        // Check the previous item
        Map<String, Object> previousItem = (Map)context.get("previous");
        String expectedPreviousText = "previous";
        assertThat(previousItem.get("text"), equalTo(expectedPreviousText));
        String expectedPreviousActive = "true";
        assertThat(previousItem.get("active"), equalTo(expectedPreviousActive));
        String expectedPreviousUrl = "9";
        assertThat(previousItem.get("url"), equalTo(expectedPreviousUrl));

        // Check the 1 item
        Map<String, Object> oneItem = (Map)context.get("1");
        String expectedOneText = "6";
        assertThat(oneItem.get("text"), equalTo(expectedOneText));
        String expectedOneActive = "true";
        assertThat(oneItem.get("active"), equalTo(expectedOneActive));
        String expectedOneUrl = "6";
        assertThat(oneItem.get("url"), equalTo(expectedOneUrl));
        // Check the 2 item
        Map<String, Object> secondItem = (Map)context.get("2");
        String expectedSecondText = "7";
        assertThat(secondItem.get("text"), equalTo(expectedSecondText));
        String expectedSecondActive = "true";
        assertThat(secondItem.get("active"), equalTo(expectedSecondActive));
        String expectedSecondUrl = "7";
        assertThat(secondItem.get("url"), equalTo(expectedSecondUrl));
        // Check the 3 item
        Map<String, Object> thirdItem = (Map)context.get("3");
        String expectedThirdText = "8";
        assertThat(thirdItem.get("text"), equalTo(expectedThirdText));
        String expectedThirdActive = "true";
        assertThat(thirdItem.get("active"), equalTo(expectedThirdActive));
        String expectedThirdUrl = "8";
        assertThat(thirdItem.get("url"), equalTo(expectedThirdUrl));
        // Check the 4 item
        Map<String, Object> fourthItem = (Map)context.get("4");
        String expectedFourthText = "9";
        assertThat(fourthItem.get("text"), equalTo(expectedFourthText));
        String expectedFourthActive = "true";
        assertThat(fourthItem.get("active"), equalTo(expectedFourthActive));
        String expectedFourthUrl = "9";
        assertThat(fourthItem.get("url"), equalTo(expectedFourthUrl));
        // Check the 5 item
        Map<String, Object> fifthItem = (Map)context.get("5");
        String expectedFifthText = "10";
        assertThat(fifthItem.get("text"), equalTo(expectedFifthText));
        String expectedFifthActive = "true";
        assertThat(fifthItem.get("active"), equalTo(expectedFifthActive));
        String expectedFifthUrl = "10";
        assertThat(fifthItem.get("url"), equalTo(expectedFifthUrl));

        // Check the next item
        Map<String, Object> nextItem = (Map)context.get("next");
        String expectedNextText = "next";
        assertThat(nextItem.get("text"), equalTo(expectedNextText));
        String expectedNextActive = "false";
        assertThat(nextItem.get("active"), equalTo(expectedNextActive));
        String expectedNextUrl = "10";
        assertThat(nextItem.get("url"), equalTo(expectedNextUrl));
        // Check the last item
        Map<String, Object> lastItem = (Map)context.get("last");
        String expectedLastText = "last";
        assertThat(lastItem.get("text"), equalTo(expectedLastText));
        String expectedLastActive = "false";
        assertThat(lastItem.get("active"), equalTo(expectedLastActive));
        String expectedLastUrl = "10";
        assertThat(lastItem.get("url"), equalTo(expectedLastUrl));
    }

    @Test
    public void testCalculatePaginationFirstOf1Item() throws Exception {
        SearchConfiguration configuration = new SearchConfiguration(null, null);
        FragmentsLuceneIndex fragmentsLuceneIndex = new FragmentsLuceneIndex(configuration);
        Map<String, Object> context = fragmentsLuceneIndex.calculatePagination(1, 1);
        // The list has 5 items, 1 page and "first", "last" and "previous", "next"
        int expectedItems = 5;
        assertThat(context.size(), equalTo(expectedItems));
        // Check the first item
        Map<String, Object> firstItem = (Map)context.get("first");
        String expectedFirstText = "first";
        assertThat(firstItem.get("text"), equalTo(expectedFirstText));
        String expectedFirstActive = "false";
        assertThat(firstItem.get("active"), equalTo(expectedFirstActive));
        String expectedFirstUrl = "1";
        assertThat(firstItem.get("url"), equalTo(expectedFirstUrl));
        // Check the previous item
        Map<String, Object> previousItem = (Map)context.get("previous");
        String expectedPreviousText = "previous";
        assertThat(previousItem.get("text"), equalTo(expectedPreviousText));
        String expectedPreviousActive = "false";
        assertThat(previousItem.get("active"), equalTo(expectedPreviousActive));
        String expectedPreviousUrl = "1";
        assertThat(previousItem.get("url"), equalTo(expectedPreviousUrl));

        // Check the 1 item
        Map<String, Object> oneItem = (Map)context.get("1");
        String expectedOneText = "1";
        assertThat(oneItem.get("text"), equalTo(expectedOneText));
        String expectedOneActive = "true";
        assertThat(oneItem.get("active"), equalTo(expectedOneActive));
        String expectedOneUrl = "1";
        assertThat(oneItem.get("url"), equalTo(expectedOneUrl));
        // Check the 2 item
        Map<String, Object> secondItem = (Map)context.get("2");
        String expectedSecondItem = null;
        assertThat(secondItem, equalTo(expectedSecondItem));

        // Check the next item
        Map<String, Object> nextItem = (Map)context.get("next");
        String expectedNextText = "next";
        assertThat(nextItem.get("text"), equalTo(expectedNextText));
        String expectedNextActive = "false";
        assertThat(nextItem.get("active"), equalTo(expectedNextActive));
        String expectedNextUrl = "1";
        assertThat(nextItem.get("url"), equalTo(expectedNextUrl));
        // Check the last item
        Map<String, Object> lastItem = (Map)context.get("last");
        String expectedLastText = "last";
        assertThat(lastItem.get("text"), equalTo(expectedLastText));
        String expectedLastActive = "false";
        assertThat(lastItem.get("active"), equalTo(expectedLastActive));
        String expectedLastUrl = "1";
        assertThat(lastItem.get("url"), equalTo(expectedLastUrl));
    }

    @Test
    public void testCalculatePaginationFirstOf5Items() throws Exception {
        SearchConfiguration configuration = new SearchConfiguration(null, null);
        FragmentsLuceneIndex fragmentsLuceneIndex = new FragmentsLuceneIndex(configuration);
        Map<String, Object> context = fragmentsLuceneIndex.calculatePagination(1, 50);
        // The list has 9 items, 5 pages and "first", "last" and "previous", "next"
        int expectedItems = 9;
        assertThat(context.size(), equalTo(expectedItems));
        // Check the first item
        Map<String, Object> firstItem = (Map)context.get("first");
        String expectedFirstText = "first";
        assertThat(firstItem.get("text"), equalTo(expectedFirstText));
        String expectedFirstActive = "false";
        assertThat(firstItem.get("active"), equalTo(expectedFirstActive));
        String expectedFirstUrl = "1";
        assertThat(firstItem.get("url"), equalTo(expectedFirstUrl));
        // Check the previous item
        Map<String, Object> previousItem = (Map)context.get("previous");
        String expectedPreviousText = "previous";
        assertThat(previousItem.get("text"), equalTo(expectedPreviousText));
        String expectedPreviousActive = "false";
        assertThat(previousItem.get("active"), equalTo(expectedPreviousActive));
        String expectedPreviousUrl = "1";
        assertThat(previousItem.get("url"), equalTo(expectedPreviousUrl));

        // Check the 1 item
        Map<String, Object> oneItem = (Map)context.get("1");
        String expectedOneText = "1";
        assertThat(oneItem.get("text"), equalTo(expectedOneText));
        String expectedOneActive = "true";
        assertThat(oneItem.get("active"), equalTo(expectedOneActive));
        String expectedOneUrl = "1";
        assertThat(oneItem.get("url"), equalTo(expectedOneUrl));
        // Check the 2 item
        Map<String, Object> secondItem = (Map)context.get("2");
        String expectedSecondText = "2";
        assertThat(secondItem.get("text"), equalTo(expectedSecondText));
        String expectedSecondActive = "true";
        assertThat(secondItem.get("active"), equalTo(expectedSecondActive));
        String expectedSecondUrl = "2";
        assertThat(secondItem.get("url"), equalTo(expectedSecondUrl));
        // Check the 3 item
        Map<String, Object> thirdItem = (Map)context.get("3");
        String expectedThirdText = "3";
        assertThat(thirdItem.get("text"), equalTo(expectedThirdText));
        String expectedThirdActive = "true";
        assertThat(thirdItem.get("active"), equalTo(expectedThirdActive));
        String expectedThirdUrl = "3";
        assertThat(thirdItem.get("url"), equalTo(expectedThirdUrl));
        // Check the 4 item
        Map<String, Object> fourthItem = (Map)context.get("4");
        String expectedFourthText = "4";
        assertThat(fourthItem.get("text"), equalTo(expectedFourthText));
        String expectedFourthActive = "true";
        assertThat(fourthItem.get("active"), equalTo(expectedFourthActive));
        String expectedFourthUrl = "4";
        assertThat(fourthItem.get("url"), equalTo(expectedFourthUrl));
        // Check the 5 item
        Map<String, Object> fifthItem = (Map)context.get("5");
        String expectedFifthText = "5";
        assertThat(fifthItem.get("text"), equalTo(expectedFifthText));
        String expectedFifthActive = "true";
        assertThat(fifthItem.get("active"), equalTo(expectedFifthActive));
        String expectedFifthUrl = "5";
        assertThat(fifthItem.get("url"), equalTo(expectedFifthUrl));

        // Check the next item
        Map<String, Object> nextItem = (Map)context.get("next");
        String expectedNextText = "next";
        assertThat(nextItem.get("text"), equalTo(expectedNextText));
        String expectedNextActive = "false";
        assertThat(nextItem.get("active"), equalTo(expectedNextActive));
        String expectedNextUrl = "5";
        assertThat(nextItem.get("url"), equalTo(expectedNextUrl));
        // Check the last item
        Map<String, Object> lastItem = (Map)context.get("last");
        String expectedLastText = "last";
        assertThat(lastItem.get("text"), equalTo(expectedLastText));
        String expectedLastActive = "false";
        assertThat(lastItem.get("active"), equalTo(expectedLastActive));
        String expectedLastUrl = "5";
        assertThat(lastItem.get("url"), equalTo(expectedLastUrl));
    }
}