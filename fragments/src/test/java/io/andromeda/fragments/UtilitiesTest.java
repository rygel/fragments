package io.andromeda.fragments;

import org.hamcrest.CoreMatchers;
import org.junit.Test;

import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.equalTo;


/**
 * @author Alexander Brandt
 */
public class UtilitiesTest {

    @Test
    public void testRemoveTrailingSlashRoot() throws Exception {
        String expected = "/";
        String result = Utilities.removeTrailingSlash("/");
        assertThat(expected, equalTo(result));
    }

    @Test
    public void testRemoveTrailingSlashLevel1() throws Exception {
        String expected = "blog";
        String result = Utilities.removeTrailingSlash("blog/");
        assertThat(expected, equalTo(result));
    }

    @Test
    public void testRemoveTrailingSlashLevel3() throws Exception {
        String expected = "/blog/test/local";
        String result = Utilities.removeTrailingSlash("/blog/test/local/");
        assertThat(expected, equalTo(result));
    }

    @Test
    public void testRemoveTrailingSlashDoNothing() throws Exception {
        String expected = "/blog";
        String result = Utilities.removeTrailingSlash("/blog");
        assertThat(expected, equalTo(result));
    }

    @Test
    public void testCalculatePaginationPage1Of10() throws Exception {
        Map<String, Object> context = Utilities.calculatePagination(1, 5, 100);
        // The list has 9 items, 5 pages and "first", "last" and "previous", "next"
        int expectedItems = 9;
        assertThat(context.size(), CoreMatchers.equalTo(expectedItems));
        // Check the first item
        Map<String, Object> firstItem = (Map)context.get("first");
        String expectedFirstText = "first";
        assertThat(firstItem.get("text"), CoreMatchers.equalTo(expectedFirstText));
        String expectedFirstActive = "false";
        assertThat(firstItem.get("active"), CoreMatchers.equalTo(expectedFirstActive));
        String expectedFirstUrl = "1";
        assertThat(firstItem.get("url"), CoreMatchers.equalTo(expectedFirstUrl));
        // Check the previous item
        Map<String, Object> previousItem = (Map)context.get("previous");
        String expectedPreviousText = "previous";
        assertThat(previousItem.get("text"), CoreMatchers.equalTo(expectedPreviousText));
        String expectedPreviousActive = "false";
        assertThat(previousItem.get("active"), CoreMatchers.equalTo(expectedPreviousActive));
        String expectedPreviousUrl = "1";
        assertThat(previousItem.get("url"), CoreMatchers.equalTo(expectedPreviousUrl));

        // Check the 1 item
        Map<String, Object> oneItem = (Map)context.get("1");
        String expectedOneText = "1";
        assertThat(oneItem.get("text"), CoreMatchers.equalTo(expectedOneText));
        String expectedOneActive = "true";
        assertThat(oneItem.get("active"), CoreMatchers.equalTo(expectedOneActive));
        String expectedOneUrl = "1";
        assertThat(oneItem.get("url"), CoreMatchers.equalTo(expectedOneUrl));
        // Check the 2 item
        Map<String, Object> secondItem = (Map)context.get("2");
        String expectedSecondText = "2";
        assertThat(secondItem.get("text"), CoreMatchers.equalTo(expectedSecondText));
        String expectedSecondActive = "true";
        assertThat(secondItem.get("active"), CoreMatchers.equalTo(expectedSecondActive));
        String expectedSecondUrl = "2";
        assertThat(secondItem.get("url"), CoreMatchers.equalTo(expectedSecondUrl));
        // Check the 3 item
        Map<String, Object> thirdItem = (Map)context.get("3");
        String expectedThirdText = "3";
        assertThat(thirdItem.get("text"), CoreMatchers.equalTo(expectedThirdText));
        String expectedThirdActive = "true";
        assertThat(thirdItem.get("active"), CoreMatchers.equalTo(expectedThirdActive));
        String expectedThirdUrl = "3";
        assertThat(thirdItem.get("url"), CoreMatchers.equalTo(expectedThirdUrl));
        // Check the 4 item
        Map<String, Object> fourthItem = (Map)context.get("4");
        String expectedFourthText = "4";
        assertThat(fourthItem.get("text"), CoreMatchers.equalTo(expectedFourthText));
        String expectedFourthActive = "true";
        assertThat(fourthItem.get("active"), CoreMatchers.equalTo(expectedFourthActive));
        String expectedFourthUrl = "4";
        assertThat(fourthItem.get("url"), CoreMatchers.equalTo(expectedFourthUrl));
        // Check the 5 item
        Map<String, Object> fifthItem = (Map)context.get("5");
        String expectedFifthText = "5";
        assertThat(fifthItem.get("text"), CoreMatchers.equalTo(expectedFifthText));
        String expectedFifthActive = "true";
        assertThat(fifthItem.get("active"), CoreMatchers.equalTo(expectedFifthActive));
        String expectedFifthUrl = "5";
        assertThat(fifthItem.get("url"), CoreMatchers.equalTo(expectedFifthUrl));

        // Check the next item
        Map<String, Object> nextItem = (Map)context.get("next");
        String expectedNextText = "next";
        assertThat(nextItem.get("text"), CoreMatchers.equalTo(expectedNextText));
        String expectedNextActive = "true";
        assertThat(nextItem.get("active"), CoreMatchers.equalTo(expectedNextActive));
        String expectedNextUrl = "2";
        assertThat(nextItem.get("url"), CoreMatchers.equalTo(expectedNextUrl));
        // Check the last item
        Map<String, Object> lastItem = (Map)context.get("last");
        String expectedLastText = "last";
        assertThat(lastItem.get("text"), CoreMatchers.equalTo(expectedLastText));
        String expectedLastActive = "true";
        assertThat(lastItem.get("active"), CoreMatchers.equalTo(expectedLastActive));
        String expectedLastUrl = "10";
        assertThat(lastItem.get("url"), CoreMatchers.equalTo(expectedLastUrl));
    }

    @Test
    public void testCalculatePaginationPage2Of10() throws Exception {
        Map<String, Object> context = Utilities.calculatePagination(2, 5, 100);
        // The list has 9 items, 5 pages and "first", "last" and "previous", "next"
        int expectedItems = 9;
        assertThat(context.size(), CoreMatchers.equalTo(expectedItems));
        // Check the first item
        Map<String, Object> firstItem = (Map)context.get("first");
        String expectedFirstText = "first";
        assertThat(firstItem.get("text"), CoreMatchers.equalTo(expectedFirstText));
        String expectedFirstActive = "true";
        assertThat(firstItem.get("active"), CoreMatchers.equalTo(expectedFirstActive));
        String expectedFirstUrl = "1";
        assertThat(firstItem.get("url"), CoreMatchers.equalTo(expectedFirstUrl));
        // Check the previous item
        Map<String, Object> previousItem = (Map)context.get("previous");
        String expectedPreviousText = "previous";
        assertThat(previousItem.get("text"), CoreMatchers.equalTo(expectedPreviousText));
        String expectedPreviousActive = "true";
        assertThat(previousItem.get("active"), CoreMatchers.equalTo(expectedPreviousActive));
        String expectedPreviousUrl = "1";
        assertThat(previousItem.get("url"), CoreMatchers.equalTo(expectedPreviousUrl));

        // Check the 1 item
        Map<String, Object> oneItem = (Map)context.get("1");
        String expectedOneText = "1";
        assertThat(oneItem.get("text"), CoreMatchers.equalTo(expectedOneText));
        String expectedOneActive = "true";
        assertThat(oneItem.get("active"), CoreMatchers.equalTo(expectedOneActive));
        String expectedOneUrl = "1";
        assertThat(oneItem.get("url"), CoreMatchers.equalTo(expectedOneUrl));
        // Check the 2 item
        Map<String, Object> secondItem = (Map)context.get("2");
        String expectedSecondText = "2";
        assertThat(secondItem.get("text"), CoreMatchers.equalTo(expectedSecondText));
        String expectedSecondActive = "true";
        assertThat(secondItem.get("active"), CoreMatchers.equalTo(expectedSecondActive));
        String expectedSecondUrl = "2";
        assertThat(secondItem.get("url"), CoreMatchers.equalTo(expectedSecondUrl));
        // Check the 3 item
        Map<String, Object> thirdItem = (Map)context.get("3");
        String expectedThirdText = "3";
        assertThat(thirdItem.get("text"), CoreMatchers.equalTo(expectedThirdText));
        String expectedThirdActive = "true";
        assertThat(thirdItem.get("active"), CoreMatchers.equalTo(expectedThirdActive));
        String expectedThirdUrl = "3";
        assertThat(thirdItem.get("url"), CoreMatchers.equalTo(expectedThirdUrl));
        // Check the 4 item
        Map<String, Object> fourthItem = (Map)context.get("4");
        String expectedFourthText = "4";
        assertThat(fourthItem.get("text"), CoreMatchers.equalTo(expectedFourthText));
        String expectedFourthActive = "true";
        assertThat(fourthItem.get("active"), CoreMatchers.equalTo(expectedFourthActive));
        String expectedFourthUrl = "4";
        assertThat(fourthItem.get("url"), CoreMatchers.equalTo(expectedFourthUrl));
        // Check the 5 item
        Map<String, Object> fifthItem = (Map)context.get("5");
        String expectedFifthText = "5";
        assertThat(fifthItem.get("text"), CoreMatchers.equalTo(expectedFifthText));
        String expectedFifthActive = "true";
        assertThat(fifthItem.get("active"), CoreMatchers.equalTo(expectedFifthActive));
        String expectedFifthUrl = "5";
        assertThat(fifthItem.get("url"), CoreMatchers.equalTo(expectedFifthUrl));

        // Check the next item
        Map<String, Object> nextItem = (Map)context.get("next");
        String expectedNextText = "next";
        assertThat(nextItem.get("text"), CoreMatchers.equalTo(expectedNextText));
        String expectedNextActive = "true";
        assertThat(nextItem.get("active"), CoreMatchers.equalTo(expectedNextActive));
        String expectedNextUrl = "3";
        assertThat(nextItem.get("url"), CoreMatchers.equalTo(expectedNextUrl));
        // Check the last item
        Map<String, Object> lastItem = (Map)context.get("last");
        String expectedLastText = "last";
        assertThat(lastItem.get("text"), CoreMatchers.equalTo(expectedLastText));
        String expectedLastActive = "true";
        assertThat(lastItem.get("active"), CoreMatchers.equalTo(expectedLastActive));
        String expectedLastUrl = "10";
        assertThat(lastItem.get("url"), CoreMatchers.equalTo(expectedLastUrl));
    }

    @Test
    public void testCalculatePaginationPage3Of10() throws Exception {
        Map<String, Object> context = Utilities.calculatePagination(3, 5, 100);
        // The list has 9 items, 5 pages and "first", "last" and "previous", "next"
        int expectedItems = 9;
        assertThat(context.size(), CoreMatchers.equalTo(expectedItems));
        // Check the first item
        Map<String, Object> firstItem = (Map)context.get("first");
        String expectedFirstText = "first";
        assertThat(firstItem.get("text"), CoreMatchers.equalTo(expectedFirstText));
        String expectedFirstActive = "true";
        assertThat(firstItem.get("active"), CoreMatchers.equalTo(expectedFirstActive));
        String expectedFirstUrl = "1";
        assertThat(firstItem.get("url"), CoreMatchers.equalTo(expectedFirstUrl));
        // Check the previous item
        Map<String, Object> previousItem = (Map)context.get("previous");
        String expectedPreviousText = "previous";
        assertThat(previousItem.get("text"), CoreMatchers.equalTo(expectedPreviousText));
        String expectedPreviousActive = "true";
        assertThat(previousItem.get("active"), CoreMatchers.equalTo(expectedPreviousActive));
        String expectedPreviousUrl = "2";
        assertThat(previousItem.get("url"), CoreMatchers.equalTo(expectedPreviousUrl));

        // Check the 1 item
        Map<String, Object> oneItem = (Map)context.get("1");
        String expectedOneText = "1";
        assertThat(oneItem.get("text"), CoreMatchers.equalTo(expectedOneText));
        String expectedOneActive = "true";
        assertThat(oneItem.get("active"), CoreMatchers.equalTo(expectedOneActive));
        String expectedOneUrl = "1";
        assertThat(oneItem.get("url"), CoreMatchers.equalTo(expectedOneUrl));
        // Check the 2 item
        Map<String, Object> secondItem = (Map)context.get("2");
        String expectedSecondText = "2";
        assertThat(secondItem.get("text"), CoreMatchers.equalTo(expectedSecondText));
        String expectedSecondActive = "true";
        assertThat(secondItem.get("active"), CoreMatchers.equalTo(expectedSecondActive));
        String expectedSecondUrl = "2";
        assertThat(secondItem.get("url"), CoreMatchers.equalTo(expectedSecondUrl));
        // Check the 3 item
        Map<String, Object> thirdItem = (Map)context.get("3");
        String expectedThirdText = "3";
        assertThat(thirdItem.get("text"), CoreMatchers.equalTo(expectedThirdText));
        String expectedThirdActive = "true";
        assertThat(thirdItem.get("active"), CoreMatchers.equalTo(expectedThirdActive));
        String expectedThirdUrl = "3";
        assertThat(thirdItem.get("url"), CoreMatchers.equalTo(expectedThirdUrl));
        // Check the 4 item
        Map<String, Object> fourthItem = (Map)context.get("4");
        String expectedFourthText = "4";
        assertThat(fourthItem.get("text"), CoreMatchers.equalTo(expectedFourthText));
        String expectedFourthActive = "true";
        assertThat(fourthItem.get("active"), CoreMatchers.equalTo(expectedFourthActive));
        String expectedFourthUrl = "4";
        assertThat(fourthItem.get("url"), CoreMatchers.equalTo(expectedFourthUrl));
        // Check the 5 item
        Map<String, Object> fifthItem = (Map)context.get("5");
        String expectedFifthText = "5";
        assertThat(fifthItem.get("text"), CoreMatchers.equalTo(expectedFifthText));
        String expectedFifthActive = "true";
        assertThat(fifthItem.get("active"), CoreMatchers.equalTo(expectedFifthActive));
        String expectedFifthUrl = "5";
        assertThat(fifthItem.get("url"), CoreMatchers.equalTo(expectedFifthUrl));

        // Check the next item
        Map<String, Object> nextItem = (Map)context.get("next");
        String expectedNextText = "next";
        assertThat(nextItem.get("text"), CoreMatchers.equalTo(expectedNextText));
        String expectedNextActive = "true";
        assertThat(nextItem.get("active"), CoreMatchers.equalTo(expectedNextActive));
        String expectedNextUrl = "4";
        assertThat(nextItem.get("url"), CoreMatchers.equalTo(expectedNextUrl));
        // Check the last item
        Map<String, Object> lastItem = (Map)context.get("last");
        String expectedLastText = "last";
        assertThat(lastItem.get("text"), CoreMatchers.equalTo(expectedLastText));
        String expectedLastActive = "true";
        assertThat(lastItem.get("active"), CoreMatchers.equalTo(expectedLastActive));
        String expectedLastUrl = "10";
        assertThat(lastItem.get("url"), CoreMatchers.equalTo(expectedLastUrl));
    }

    @Test
    public void testCalculatePaginationPage4Of10() throws Exception {
        Map<String, Object> context = Utilities.calculatePagination(4, 5, 100);
        // The list has 9 items, 5 pages and "first", "last" and "previous", "next"
        int expectedItems = 9;
        assertThat(context.size(), CoreMatchers.equalTo(expectedItems));
        // Check the first item
        Map<String, Object> firstItem = (Map)context.get("first");
        String expectedFirstText = "first";
        assertThat(firstItem.get("text"), CoreMatchers.equalTo(expectedFirstText));
        String expectedFirstActive = "true";
        assertThat(firstItem.get("active"), CoreMatchers.equalTo(expectedFirstActive));
        String expectedFirstUrl = "1";
        assertThat(firstItem.get("url"), CoreMatchers.equalTo(expectedFirstUrl));
        // Check the previous item
        Map<String, Object> previousItem = (Map)context.get("previous");
        String expectedPreviousText = "previous";
        assertThat(previousItem.get("text"), CoreMatchers.equalTo(expectedPreviousText));
        String expectedPreviousActive = "true";
        assertThat(previousItem.get("active"), CoreMatchers.equalTo(expectedPreviousActive));
        String expectedPreviousUrl = "3";
        assertThat(previousItem.get("url"), CoreMatchers.equalTo(expectedPreviousUrl));

        // Check the 1 item
        Map<String, Object> oneItem = (Map)context.get("1");
        String expectedOneText = "2";
        assertThat(oneItem.get("text"), CoreMatchers.equalTo(expectedOneText));
        String expectedOneActive = "true";
        assertThat(oneItem.get("active"), CoreMatchers.equalTo(expectedOneActive));
        String expectedOneUrl = "2";
        assertThat(oneItem.get("url"), CoreMatchers.equalTo(expectedOneUrl));
        // Check the 2 item
        Map<String, Object> secondItem = (Map)context.get("2");
        String expectedSecondText = "3";
        assertThat(secondItem.get("text"), CoreMatchers.equalTo(expectedSecondText));
        String expectedSecondActive = "true";
        assertThat(secondItem.get("active"), CoreMatchers.equalTo(expectedSecondActive));
        String expectedSecondUrl = "3";
        assertThat(secondItem.get("url"), CoreMatchers.equalTo(expectedSecondUrl));
        // Check the 3 item
        Map<String, Object> thirdItem = (Map)context.get("3");
        String expectedThirdText = "4";
        assertThat(thirdItem.get("text"), CoreMatchers.equalTo(expectedThirdText));
        String expectedThirdActive = "true";
        assertThat(thirdItem.get("active"), CoreMatchers.equalTo(expectedThirdActive));
        String expectedThirdUrl = "4";
        assertThat(thirdItem.get("url"), CoreMatchers.equalTo(expectedThirdUrl));
        // Check the 4 item
        Map<String, Object> fourthItem = (Map)context.get("4");
        String expectedFourthText = "5";
        assertThat(fourthItem.get("text"), CoreMatchers.equalTo(expectedFourthText));
        String expectedFourthActive = "true";
        assertThat(fourthItem.get("active"), CoreMatchers.equalTo(expectedFourthActive));
        String expectedFourthUrl = "5";
        assertThat(fourthItem.get("url"), CoreMatchers.equalTo(expectedFourthUrl));
        // Check the 5 item
        Map<String, Object> fifthItem = (Map)context.get("5");
        String expectedFifthText = "6";
        assertThat(fifthItem.get("text"), CoreMatchers.equalTo(expectedFifthText));
        String expectedFifthActive = "true";
        assertThat(fifthItem.get("active"), CoreMatchers.equalTo(expectedFifthActive));
        String expectedFifthUrl = "6";
        assertThat(fifthItem.get("url"), CoreMatchers.equalTo(expectedFifthUrl));

        // Check the next item
        Map<String, Object> nextItem = (Map)context.get("next");
        String expectedNextText = "next";
        assertThat(nextItem.get("text"), CoreMatchers.equalTo(expectedNextText));
        String expectedNextActive = "true";
        assertThat(nextItem.get("active"), CoreMatchers.equalTo(expectedNextActive));
        String expectedNextUrl = "5";
        assertThat(nextItem.get("url"), CoreMatchers.equalTo(expectedNextUrl));
        // Check the last item
        Map<String, Object> lastItem = (Map)context.get("last");
        String expectedLastText = "last";
        assertThat(lastItem.get("text"), CoreMatchers.equalTo(expectedLastText));
        String expectedLastActive = "true";
        assertThat(lastItem.get("active"), CoreMatchers.equalTo(expectedLastActive));
        String expectedLastUrl = "10";
        assertThat(lastItem.get("url"), CoreMatchers.equalTo(expectedLastUrl));
    }

    @Test
    public void testCalculatePaginationPage5Of10() throws Exception {
        Map<String, Object> context = Utilities.calculatePagination(5, 5, 100);
        // The list has 9 items, 5 pages and "first", "last" and "previous", "next"
        int expectedItems = 9;
        assertThat(context.size(), CoreMatchers.equalTo(expectedItems));
        // Check the first item
        Map<String, Object> firstItem = (Map)context.get("first");
        String expectedFirstText = "first";
        assertThat(firstItem.get("text"), CoreMatchers.equalTo(expectedFirstText));
        String expectedFirstActive = "true";
        assertThat(firstItem.get("active"), CoreMatchers.equalTo(expectedFirstActive));
        String expectedFirstUrl = "1";
        assertThat(firstItem.get("url"), CoreMatchers.equalTo(expectedFirstUrl));
        // Check the previous item
        Map<String, Object> previousItem = (Map)context.get("previous");
        String expectedPreviousText = "previous";
        assertThat(previousItem.get("text"), CoreMatchers.equalTo(expectedPreviousText));
        String expectedPreviousActive = "true";
        assertThat(previousItem.get("active"), CoreMatchers.equalTo(expectedPreviousActive));
        String expectedPreviousUrl = "4";
        assertThat(previousItem.get("url"), CoreMatchers.equalTo(expectedPreviousUrl));

        // Check the 1 item
        Map<String, Object> oneItem = (Map)context.get("1");
        String expectedOneText = "3";
        assertThat(oneItem.get("text"), CoreMatchers.equalTo(expectedOneText));
        String expectedOneActive = "true";
        assertThat(oneItem.get("active"), CoreMatchers.equalTo(expectedOneActive));
        String expectedOneUrl = "3";
        assertThat(oneItem.get("url"), CoreMatchers.equalTo(expectedOneUrl));
        // Check the 2 item
        Map<String, Object> secondItem = (Map)context.get("2");
        String expectedSecondText = "4";
        assertThat(secondItem.get("text"), CoreMatchers.equalTo(expectedSecondText));
        String expectedSecondActive = "true";
        assertThat(secondItem.get("active"), CoreMatchers.equalTo(expectedSecondActive));
        String expectedSecondUrl = "4";
        assertThat(secondItem.get("url"), CoreMatchers.equalTo(expectedSecondUrl));
        // Check the 3 item
        Map<String, Object> thirdItem = (Map)context.get("3");
        String expectedThirdText = "5";
        assertThat(thirdItem.get("text"), CoreMatchers.equalTo(expectedThirdText));
        String expectedThirdActive = "true";
        assertThat(thirdItem.get("active"), CoreMatchers.equalTo(expectedThirdActive));
        String expectedThirdUrl = "5";
        assertThat(thirdItem.get("url"), CoreMatchers.equalTo(expectedThirdUrl));
        // Check the 4 item
        Map<String, Object> fourthItem = (Map)context.get("4");
        String expectedFourthText = "6";
        assertThat(fourthItem.get("text"), CoreMatchers.equalTo(expectedFourthText));
        String expectedFourthActive = "true";
        assertThat(fourthItem.get("active"), CoreMatchers.equalTo(expectedFourthActive));
        String expectedFourthUrl = "6";
        assertThat(fourthItem.get("url"), CoreMatchers.equalTo(expectedFourthUrl));
        // Check the 5 item
        Map<String, Object> fifthItem = (Map)context.get("5");
        String expectedFifthText = "7";
        assertThat(fifthItem.get("text"), CoreMatchers.equalTo(expectedFifthText));
        String expectedFifthActive = "true";
        assertThat(fifthItem.get("active"), CoreMatchers.equalTo(expectedFifthActive));
        String expectedFifthUrl = "7";
        assertThat(fifthItem.get("url"), CoreMatchers.equalTo(expectedFifthUrl));

        // Check the next item
        Map<String, Object> nextItem = (Map)context.get("next");
        String expectedNextText = "next";
        assertThat(nextItem.get("text"), CoreMatchers.equalTo(expectedNextText));
        String expectedNextActive = "true";
        assertThat(nextItem.get("active"), CoreMatchers.equalTo(expectedNextActive));
        String expectedNextUrl = "6";
        assertThat(nextItem.get("url"), CoreMatchers.equalTo(expectedNextUrl));
        // Check the last item
        Map<String, Object> lastItem = (Map)context.get("last");
        String expectedLastText = "last";
        assertThat(lastItem.get("text"), CoreMatchers.equalTo(expectedLastText));
        String expectedLastActive = "true";
        assertThat(lastItem.get("active"), CoreMatchers.equalTo(expectedLastActive));
        String expectedLastUrl = "10";
        assertThat(lastItem.get("url"), CoreMatchers.equalTo(expectedLastUrl));
    }

    @Test
    public void testCalculatePaginationPage6Of10() throws Exception {
        Map<String, Object> context = Utilities.calculatePagination(6, 5, 100);
        // The list has 9 items, 5 pages and "first", "last" and "previous", "next"
        int expectedItems = 9;
        assertThat(context.size(), CoreMatchers.equalTo(expectedItems));
        // Check the first item
        Map<String, Object> firstItem = (Map)context.get("first");
        String expectedFirstText = "first";
        assertThat(firstItem.get("text"), CoreMatchers.equalTo(expectedFirstText));
        String expectedFirstActive = "true";
        assertThat(firstItem.get("active"), CoreMatchers.equalTo(expectedFirstActive));
        String expectedFirstUrl = "1";
        assertThat(firstItem.get("url"), CoreMatchers.equalTo(expectedFirstUrl));
        // Check the previous item
        Map<String, Object> previousItem = (Map)context.get("previous");
        String expectedPreviousText = "previous";
        assertThat(previousItem.get("text"), CoreMatchers.equalTo(expectedPreviousText));
        String expectedPreviousActive = "true";
        assertThat(previousItem.get("active"), CoreMatchers.equalTo(expectedPreviousActive));
        String expectedPreviousUrl = "5";
        assertThat(previousItem.get("url"), CoreMatchers.equalTo(expectedPreviousUrl));

        // Check the 1 item
        Map<String, Object> oneItem = (Map)context.get("1");
        String expectedOneText = "4";
        assertThat(oneItem.get("text"), CoreMatchers.equalTo(expectedOneText));
        String expectedOneActive = "true";
        assertThat(oneItem.get("active"), CoreMatchers.equalTo(expectedOneActive));
        String expectedOneUrl = "4";
        assertThat(oneItem.get("url"), CoreMatchers.equalTo(expectedOneUrl));
        // Check the 2 item
        Map<String, Object> secondItem = (Map)context.get("2");
        String expectedSecondText = "5";
        assertThat(secondItem.get("text"), CoreMatchers.equalTo(expectedSecondText));
        String expectedSecondActive = "true";
        assertThat(secondItem.get("active"), CoreMatchers.equalTo(expectedSecondActive));
        String expectedSecondUrl = "5";
        assertThat(secondItem.get("url"), CoreMatchers.equalTo(expectedSecondUrl));
        // Check the 3 item
        Map<String, Object> thirdItem = (Map)context.get("3");
        String expectedThirdText = "6";
        assertThat(thirdItem.get("text"), CoreMatchers.equalTo(expectedThirdText));
        String expectedThirdActive = "true";
        assertThat(thirdItem.get("active"), CoreMatchers.equalTo(expectedThirdActive));
        String expectedThirdUrl = "6";
        assertThat(thirdItem.get("url"), CoreMatchers.equalTo(expectedThirdUrl));
        // Check the 4 item
        Map<String, Object> fourthItem = (Map)context.get("4");
        String expectedFourthText = "7";
        assertThat(fourthItem.get("text"), CoreMatchers.equalTo(expectedFourthText));
        String expectedFourthActive = "true";
        assertThat(fourthItem.get("active"), CoreMatchers.equalTo(expectedFourthActive));
        String expectedFourthUrl = "7";
        assertThat(fourthItem.get("url"), CoreMatchers.equalTo(expectedFourthUrl));
        // Check the 5 item
        Map<String, Object> fifthItem = (Map)context.get("5");
        String expectedFifthText = "8";
        assertThat(fifthItem.get("text"), CoreMatchers.equalTo(expectedFifthText));
        String expectedFifthActive = "true";
        assertThat(fifthItem.get("active"), CoreMatchers.equalTo(expectedFifthActive));
        String expectedFifthUrl = "8";
        assertThat(fifthItem.get("url"), CoreMatchers.equalTo(expectedFifthUrl));

        // Check the next item
        Map<String, Object> nextItem = (Map)context.get("next");
        String expectedNextText = "next";
        assertThat(nextItem.get("text"), CoreMatchers.equalTo(expectedNextText));
        String expectedNextActive = "true";
        assertThat(nextItem.get("active"), CoreMatchers.equalTo(expectedNextActive));
        String expectedNextUrl = "7";
        assertThat(nextItem.get("url"), CoreMatchers.equalTo(expectedNextUrl));
        // Check the last item
        Map<String, Object> lastItem = (Map)context.get("last");
        String expectedLastText = "last";
        assertThat(lastItem.get("text"), CoreMatchers.equalTo(expectedLastText));
        String expectedLastActive = "true";
        assertThat(lastItem.get("active"), CoreMatchers.equalTo(expectedLastActive));
        String expectedLastUrl = "10";
        assertThat(lastItem.get("url"), CoreMatchers.equalTo(expectedLastUrl));
    }

    @Test
    public void testCalculatePaginationPage7Of10() throws Exception {
        Map<String, Object> context = Utilities.calculatePagination(7, 5, 100);
        // The list has 9 items, 5 pages and "first", "last" and "previous", "next"
        int expectedItems = 9;
        assertThat(context.size(), CoreMatchers.equalTo(expectedItems));
        // Check the first item
        Map<String, Object> firstItem = (Map)context.get("first");
        String expectedFirstText = "first";
        assertThat(firstItem.get("text"), CoreMatchers.equalTo(expectedFirstText));
        String expectedFirstActive = "true";
        assertThat(firstItem.get("active"), CoreMatchers.equalTo(expectedFirstActive));
        String expectedFirstUrl = "1";
        assertThat(firstItem.get("url"), CoreMatchers.equalTo(expectedFirstUrl));
        // Check the previous item
        Map<String, Object> previousItem = (Map)context.get("previous");
        String expectedPreviousText = "previous";
        assertThat(previousItem.get("text"), CoreMatchers.equalTo(expectedPreviousText));
        String expectedPreviousActive = "true";
        assertThat(previousItem.get("active"), CoreMatchers.equalTo(expectedPreviousActive));
        String expectedPreviousUrl = "6";
        assertThat(previousItem.get("url"), CoreMatchers.equalTo(expectedPreviousUrl));

        // Check the 1 item
        Map<String, Object> oneItem = (Map)context.get("1");
        String expectedOneText = "5";
        assertThat(oneItem.get("text"), CoreMatchers.equalTo(expectedOneText));
        String expectedOneActive = "true";
        assertThat(oneItem.get("active"), CoreMatchers.equalTo(expectedOneActive));
        String expectedOneUrl = "5";
        assertThat(oneItem.get("url"), CoreMatchers.equalTo(expectedOneUrl));
        // Check the 2 item
        Map<String, Object> secondItem = (Map)context.get("2");
        String expectedSecondText = "6";
        assertThat(secondItem.get("text"), CoreMatchers.equalTo(expectedSecondText));
        String expectedSecondActive = "true";
        assertThat(secondItem.get("active"), CoreMatchers.equalTo(expectedSecondActive));
        String expectedSecondUrl = "6";
        assertThat(secondItem.get("url"), CoreMatchers.equalTo(expectedSecondUrl));
        // Check the 3 item
        Map<String, Object> thirdItem = (Map)context.get("3");
        String expectedThirdText = "7";
        assertThat(thirdItem.get("text"), CoreMatchers.equalTo(expectedThirdText));
        String expectedThirdActive = "true";
        assertThat(thirdItem.get("active"), CoreMatchers.equalTo(expectedThirdActive));
        String expectedThirdUrl = "7";
        assertThat(thirdItem.get("url"), CoreMatchers.equalTo(expectedThirdUrl));
        // Check the 4 item
        Map<String, Object> fourthItem = (Map)context.get("4");
        String expectedFourthText = "8";
        assertThat(fourthItem.get("text"), CoreMatchers.equalTo(expectedFourthText));
        String expectedFourthActive = "true";
        assertThat(fourthItem.get("active"), CoreMatchers.equalTo(expectedFourthActive));
        String expectedFourthUrl = "8";
        assertThat(fourthItem.get("url"), CoreMatchers.equalTo(expectedFourthUrl));
        // Check the 5 item
        Map<String, Object> fifthItem = (Map)context.get("5");
        String expectedFifthText = "9";
        assertThat(fifthItem.get("text"), CoreMatchers.equalTo(expectedFifthText));
        String expectedFifthActive = "true";
        assertThat(fifthItem.get("active"), CoreMatchers.equalTo(expectedFifthActive));
        String expectedFifthUrl = "9";
        assertThat(fifthItem.get("url"), CoreMatchers.equalTo(expectedFifthUrl));

        // Check the next item
        Map<String, Object> nextItem = (Map)context.get("next");
        String expectedNextText = "next";
        assertThat(nextItem.get("text"), CoreMatchers.equalTo(expectedNextText));
        String expectedNextActive = "true";
        assertThat(nextItem.get("active"), CoreMatchers.equalTo(expectedNextActive));
        String expectedNextUrl = "8";
        assertThat(nextItem.get("url"), CoreMatchers.equalTo(expectedNextUrl));
        // Check the last item
        Map<String, Object> lastItem = (Map)context.get("last");
        String expectedLastText = "last";
        assertThat(lastItem.get("text"), CoreMatchers.equalTo(expectedLastText));
        String expectedLastActive = "true";
        assertThat(lastItem.get("active"), CoreMatchers.equalTo(expectedLastActive));
        String expectedLastUrl = "10";
        assertThat(lastItem.get("url"), CoreMatchers.equalTo(expectedLastUrl));
    }

    @Test
    public void testCalculatePaginationPage8Of10() throws Exception {
        Map<String, Object> context = Utilities.calculatePagination(8, 5, 100);
        // The list has 9 items, 5 pages and "first", "last" and "previous", "next"
        int expectedItems = 9;
        assertThat(context.size(), CoreMatchers.equalTo(expectedItems));
        // Check the first item
        Map<String, Object> firstItem = (Map)context.get("first");
        String expectedFirstText = "first";
        assertThat(firstItem.get("text"), CoreMatchers.equalTo(expectedFirstText));
        String expectedFirstActive = "true";
        assertThat(firstItem.get("active"), CoreMatchers.equalTo(expectedFirstActive));
        String expectedFirstUrl = "1";
        assertThat(firstItem.get("url"), CoreMatchers.equalTo(expectedFirstUrl));
        // Check the previous item
        Map<String, Object> previousItem = (Map)context.get("previous");
        String expectedPreviousText = "previous";
        assertThat(previousItem.get("text"), CoreMatchers.equalTo(expectedPreviousText));
        String expectedPreviousActive = "true";
        assertThat(previousItem.get("active"), CoreMatchers.equalTo(expectedPreviousActive));
        String expectedPreviousUrl = "7";
        assertThat(previousItem.get("url"), CoreMatchers.equalTo(expectedPreviousUrl));

        // Check the 1 item
        Map<String, Object> oneItem = (Map)context.get("1");
        String expectedOneText = "6";
        assertThat(oneItem.get("text"), CoreMatchers.equalTo(expectedOneText));
        String expectedOneActive = "true";
        assertThat(oneItem.get("active"), CoreMatchers.equalTo(expectedOneActive));
        String expectedOneUrl = "6";
        assertThat(oneItem.get("url"), CoreMatchers.equalTo(expectedOneUrl));
        // Check the 2 item
        Map<String, Object> secondItem = (Map)context.get("2");
        String expectedSecondText = "7";
        assertThat(secondItem.get("text"), CoreMatchers.equalTo(expectedSecondText));
        String expectedSecondActive = "true";
        assertThat(secondItem.get("active"), CoreMatchers.equalTo(expectedSecondActive));
        String expectedSecondUrl = "7";
        assertThat(secondItem.get("url"), CoreMatchers.equalTo(expectedSecondUrl));
        // Check the 3 item
        Map<String, Object> thirdItem = (Map)context.get("3");
        String expectedThirdText = "8";
        assertThat(thirdItem.get("text"), CoreMatchers.equalTo(expectedThirdText));
        String expectedThirdActive = "true";
        assertThat(thirdItem.get("active"), CoreMatchers.equalTo(expectedThirdActive));
        String expectedThirdUrl = "8";
        assertThat(thirdItem.get("url"), CoreMatchers.equalTo(expectedThirdUrl));
        // Check the 4 item
        Map<String, Object> fourthItem = (Map)context.get("4");
        String expectedFourthText = "9";
        assertThat(fourthItem.get("text"), CoreMatchers.equalTo(expectedFourthText));
        String expectedFourthActive = "true";
        assertThat(fourthItem.get("active"), CoreMatchers.equalTo(expectedFourthActive));
        String expectedFourthUrl = "9";
        assertThat(fourthItem.get("url"), CoreMatchers.equalTo(expectedFourthUrl));
        // Check the 5 item
        Map<String, Object> fifthItem = (Map)context.get("5");
        String expectedFifthText = "10";
        assertThat(fifthItem.get("text"), CoreMatchers.equalTo(expectedFifthText));
        String expectedFifthActive = "true";
        assertThat(fifthItem.get("active"), CoreMatchers.equalTo(expectedFifthActive));
        String expectedFifthUrl = "10";
        assertThat(fifthItem.get("url"), CoreMatchers.equalTo(expectedFifthUrl));

        // Check the next item
        Map<String, Object> nextItem = (Map)context.get("next");
        String expectedNextText = "next";
        assertThat(nextItem.get("text"), CoreMatchers.equalTo(expectedNextText));
        String expectedNextActive = "true";
        assertThat(nextItem.get("active"), CoreMatchers.equalTo(expectedNextActive));
        String expectedNextUrl = "9";
        assertThat(nextItem.get("url"), CoreMatchers.equalTo(expectedNextUrl));
        // Check the last item
        Map<String, Object> lastItem = (Map)context.get("last");
        String expectedLastText = "last";
        assertThat(lastItem.get("text"), CoreMatchers.equalTo(expectedLastText));
        String expectedLastActive = "true";
        assertThat(lastItem.get("active"), CoreMatchers.equalTo(expectedLastActive));
        String expectedLastUrl = "10";
        assertThat(lastItem.get("url"), CoreMatchers.equalTo(expectedLastUrl));
    }

    @Test
    public void testCalculatePaginationPage9Of10() throws Exception {
        Map<String, Object> context = Utilities.calculatePagination(9, 5, 100);
        // The list has 9 items, 5 pages and "first", "last" and "previous", "next"
        int expectedItems = 9;
        assertThat(context.size(), CoreMatchers.equalTo(expectedItems));
        // Check the first item
        Map<String, Object> firstItem = (Map)context.get("first");
        String expectedFirstText = "first";
        assertThat(firstItem.get("text"), CoreMatchers.equalTo(expectedFirstText));
        String expectedFirstActive = "true";
        assertThat(firstItem.get("active"), CoreMatchers.equalTo(expectedFirstActive));
        String expectedFirstUrl = "1";
        assertThat(firstItem.get("url"), CoreMatchers.equalTo(expectedFirstUrl));
        // Check the previous item
        Map<String, Object> previousItem = (Map)context.get("previous");
        String expectedPreviousText = "previous";
        assertThat(previousItem.get("text"), CoreMatchers.equalTo(expectedPreviousText));
        String expectedPreviousActive = "true";
        assertThat(previousItem.get("active"), CoreMatchers.equalTo(expectedPreviousActive));
        String expectedPreviousUrl = "8";
        assertThat(previousItem.get("url"), CoreMatchers.equalTo(expectedPreviousUrl));

        // Check the 1 item
        Map<String, Object> oneItem = (Map)context.get("1");
        String expectedOneText = "6";
        assertThat(oneItem.get("text"), CoreMatchers.equalTo(expectedOneText));
        String expectedOneActive = "true";
        assertThat(oneItem.get("active"), CoreMatchers.equalTo(expectedOneActive));
        String expectedOneUrl = "6";
        assertThat(oneItem.get("url"), CoreMatchers.equalTo(expectedOneUrl));
        // Check the 2 item
        Map<String, Object> secondItem = (Map)context.get("2");
        String expectedSecondText = "7";
        assertThat(secondItem.get("text"), CoreMatchers.equalTo(expectedSecondText));
        String expectedSecondActive = "true";
        assertThat(secondItem.get("active"), CoreMatchers.equalTo(expectedSecondActive));
        String expectedSecondUrl = "7";
        assertThat(secondItem.get("url"), CoreMatchers.equalTo(expectedSecondUrl));
        // Check the 3 item
        Map<String, Object> thirdItem = (Map)context.get("3");
        String expectedThirdText = "8";
        assertThat(thirdItem.get("text"), CoreMatchers.equalTo(expectedThirdText));
        String expectedThirdActive = "true";
        assertThat(thirdItem.get("active"), CoreMatchers.equalTo(expectedThirdActive));
        String expectedThirdUrl = "8";
        assertThat(thirdItem.get("url"), CoreMatchers.equalTo(expectedThirdUrl));
        // Check the 4 item
        Map<String, Object> fourthItem = (Map)context.get("4");
        String expectedFourthText = "9";
        assertThat(fourthItem.get("text"), CoreMatchers.equalTo(expectedFourthText));
        String expectedFourthActive = "true";
        assertThat(fourthItem.get("active"), CoreMatchers.equalTo(expectedFourthActive));
        String expectedFourthUrl = "9";
        assertThat(fourthItem.get("url"), CoreMatchers.equalTo(expectedFourthUrl));
        // Check the 5 item
        Map<String, Object> fifthItem = (Map)context.get("5");
        String expectedFifthText = "10";
        assertThat(fifthItem.get("text"), CoreMatchers.equalTo(expectedFifthText));
        String expectedFifthActive = "true";
        assertThat(fifthItem.get("active"), CoreMatchers.equalTo(expectedFifthActive));
        String expectedFifthUrl = "10";
        assertThat(fifthItem.get("url"), CoreMatchers.equalTo(expectedFifthUrl));

        // Check the next item
        Map<String, Object> nextItem = (Map)context.get("next");
        String expectedNextText = "next";
        assertThat(nextItem.get("text"), CoreMatchers.equalTo(expectedNextText));
        String expectedNextActive = "true";
        assertThat(nextItem.get("active"), CoreMatchers.equalTo(expectedNextActive));
        String expectedNextUrl = "10";
        assertThat(nextItem.get("url"), CoreMatchers.equalTo(expectedNextUrl));
        // Check the last item
        Map<String, Object> lastItem = (Map)context.get("last");
        String expectedLastText = "last";
        assertThat(lastItem.get("text"), CoreMatchers.equalTo(expectedLastText));
        String expectedLastActive = "true";
        assertThat(lastItem.get("active"), CoreMatchers.equalTo(expectedLastActive));
        String expectedLastUrl = "10";
        assertThat(lastItem.get("url"), CoreMatchers.equalTo(expectedLastUrl));
    }

    @Test
    public void testCalculatePaginationPage10Of10() throws Exception {
        Map<String, Object> context = Utilities.calculatePagination(10, 5, 100);
        // The list has 9 items, 5 pages and "first", "last" and "previous", "next"
        int expectedItems = 9;
        assertThat(context.size(), CoreMatchers.equalTo(expectedItems));
        // Check the first item
        Map<String, Object> firstItem = (Map)context.get("first");
        String expectedFirstText = "first";
        assertThat(firstItem.get("text"), CoreMatchers.equalTo(expectedFirstText));
        String expectedFirstActive = "true";
        assertThat(firstItem.get("active"), CoreMatchers.equalTo(expectedFirstActive));
        String expectedFirstUrl = "1";
        assertThat(firstItem.get("url"), CoreMatchers.equalTo(expectedFirstUrl));
        // Check the previous item
        Map<String, Object> previousItem = (Map)context.get("previous");
        String expectedPreviousText = "previous";
        assertThat(previousItem.get("text"), CoreMatchers.equalTo(expectedPreviousText));
        String expectedPreviousActive = "true";
        assertThat(previousItem.get("active"), CoreMatchers.equalTo(expectedPreviousActive));
        String expectedPreviousUrl = "9";
        assertThat(previousItem.get("url"), CoreMatchers.equalTo(expectedPreviousUrl));

        // Check the 1 item
        Map<String, Object> oneItem = (Map)context.get("1");
        String expectedOneText = "6";
        assertThat(oneItem.get("text"), CoreMatchers.equalTo(expectedOneText));
        String expectedOneActive = "true";
        assertThat(oneItem.get("active"), CoreMatchers.equalTo(expectedOneActive));
        String expectedOneUrl = "6";
        assertThat(oneItem.get("url"), CoreMatchers.equalTo(expectedOneUrl));
        // Check the 2 item
        Map<String, Object> secondItem = (Map)context.get("2");
        String expectedSecondText = "7";
        assertThat(secondItem.get("text"), CoreMatchers.equalTo(expectedSecondText));
        String expectedSecondActive = "true";
        assertThat(secondItem.get("active"), CoreMatchers.equalTo(expectedSecondActive));
        String expectedSecondUrl = "7";
        assertThat(secondItem.get("url"), CoreMatchers.equalTo(expectedSecondUrl));
        // Check the 3 item
        Map<String, Object> thirdItem = (Map)context.get("3");
        String expectedThirdText = "8";
        assertThat(thirdItem.get("text"), CoreMatchers.equalTo(expectedThirdText));
        String expectedThirdActive = "true";
        assertThat(thirdItem.get("active"), CoreMatchers.equalTo(expectedThirdActive));
        String expectedThirdUrl = "8";
        assertThat(thirdItem.get("url"), CoreMatchers.equalTo(expectedThirdUrl));
        // Check the 4 item
        Map<String, Object> fourthItem = (Map)context.get("4");
        String expectedFourthText = "9";
        assertThat(fourthItem.get("text"), CoreMatchers.equalTo(expectedFourthText));
        String expectedFourthActive = "true";
        assertThat(fourthItem.get("active"), CoreMatchers.equalTo(expectedFourthActive));
        String expectedFourthUrl = "9";
        assertThat(fourthItem.get("url"), CoreMatchers.equalTo(expectedFourthUrl));
        // Check the 5 item
        Map<String, Object> fifthItem = (Map)context.get("5");
        String expectedFifthText = "10";
        assertThat(fifthItem.get("text"), CoreMatchers.equalTo(expectedFifthText));
        String expectedFifthActive = "true";
        assertThat(fifthItem.get("active"), CoreMatchers.equalTo(expectedFifthActive));
        String expectedFifthUrl = "10";
        assertThat(fifthItem.get("url"), CoreMatchers.equalTo(expectedFifthUrl));

        // Check the next item
        Map<String, Object> nextItem = (Map)context.get("next");
        String expectedNextText = "next";
        assertThat(nextItem.get("text"), CoreMatchers.equalTo(expectedNextText));
        String expectedNextActive = "false";
        assertThat(nextItem.get("active"), CoreMatchers.equalTo(expectedNextActive));
        String expectedNextUrl = "10";
        assertThat(nextItem.get("url"), CoreMatchers.equalTo(expectedNextUrl));
        // Check the last item
        Map<String, Object> lastItem = (Map)context.get("last");
        String expectedLastText = "last";
        assertThat(lastItem.get("text"), CoreMatchers.equalTo(expectedLastText));
        String expectedLastActive = "false";
        assertThat(lastItem.get("active"), CoreMatchers.equalTo(expectedLastActive));
        String expectedLastUrl = "10";
        assertThat(lastItem.get("url"), CoreMatchers.equalTo(expectedLastUrl));
    }

    @Test
    public void testCalculatePaginationFirstOf1Item() throws Exception {
        Map<String, Object> context = Utilities.calculatePagination(1, 5, 1);
        // The list has 5 items, 1 page and "first", "last" and "previous", "next"
        int expectedItems = 5;
        assertThat(context.size(), CoreMatchers.equalTo(expectedItems));
        // Check the first item
        Map<String, Object> firstItem = (Map)context.get("first");
        String expectedFirstText = "first";
        assertThat(firstItem.get("text"), CoreMatchers.equalTo(expectedFirstText));
        String expectedFirstActive = "false";
        assertThat(firstItem.get("active"), CoreMatchers.equalTo(expectedFirstActive));
        String expectedFirstUrl = "1";
        assertThat(firstItem.get("url"), CoreMatchers.equalTo(expectedFirstUrl));
        // Check the previous item
        Map<String, Object> previousItem = (Map)context.get("previous");
        String expectedPreviousText = "previous";
        assertThat(previousItem.get("text"), CoreMatchers.equalTo(expectedPreviousText));
        String expectedPreviousActive = "false";
        assertThat(previousItem.get("active"), CoreMatchers.equalTo(expectedPreviousActive));
        String expectedPreviousUrl = "1";
        assertThat(previousItem.get("url"), CoreMatchers.equalTo(expectedPreviousUrl));

        // Check the 1 item
        Map<String, Object> oneItem = (Map)context.get("1");
        String expectedOneText = "1";
        assertThat(oneItem.get("text"), CoreMatchers.equalTo(expectedOneText));
        String expectedOneActive = "true";
        assertThat(oneItem.get("active"), CoreMatchers.equalTo(expectedOneActive));
        String expectedOneUrl = "1";
        assertThat(oneItem.get("url"), CoreMatchers.equalTo(expectedOneUrl));
        // Check the 2 item
        Map<String, Object> secondItem = (Map)context.get("2");
        String expectedSecondItem = null;
        assertThat(secondItem, CoreMatchers.equalTo(expectedSecondItem));

        // Check the next item
        Map<String, Object> nextItem = (Map)context.get("next");
        String expectedNextText = "next";
        assertThat(nextItem.get("text"), CoreMatchers.equalTo(expectedNextText));
        String expectedNextActive = "false";
        assertThat(nextItem.get("active"), CoreMatchers.equalTo(expectedNextActive));
        String expectedNextUrl = "1";
        assertThat(nextItem.get("url"), CoreMatchers.equalTo(expectedNextUrl));
        // Check the last item
        Map<String, Object> lastItem = (Map)context.get("last");
        String expectedLastText = "last";
        assertThat(lastItem.get("text"), CoreMatchers.equalTo(expectedLastText));
        String expectedLastActive = "false";
        assertThat(lastItem.get("active"), CoreMatchers.equalTo(expectedLastActive));
        String expectedLastUrl = "1";
        assertThat(lastItem.get("url"), CoreMatchers.equalTo(expectedLastUrl));
    }

    @Test
    public void testCalculatePaginationFirstOf5Items() throws Exception {
        Map<String, Object> context = Utilities.calculatePagination(1, 5, 50);
        // The list has 9 items, 5 pages and "first", "last" and "previous", "next"
        int expectedItems = 9;
        assertThat(context.size(), CoreMatchers.equalTo(expectedItems));
        // Check the first item
        Map<String, Object> firstItem = (Map)context.get("first");
        String expectedFirstText = "first";
        assertThat(firstItem.get("text"), CoreMatchers.equalTo(expectedFirstText));
        String expectedFirstActive = "false";
        assertThat(firstItem.get("active"), CoreMatchers.equalTo(expectedFirstActive));
        String expectedFirstUrl = "1";
        assertThat(firstItem.get("url"), CoreMatchers.equalTo(expectedFirstUrl));
        // Check the previous item
        Map<String, Object> previousItem = (Map)context.get("previous");
        String expectedPreviousText = "previous";
        assertThat(previousItem.get("text"), CoreMatchers.equalTo(expectedPreviousText));
        String expectedPreviousActive = "false";
        assertThat(previousItem.get("active"), CoreMatchers.equalTo(expectedPreviousActive));
        String expectedPreviousUrl = "1";
        assertThat(previousItem.get("url"), CoreMatchers.equalTo(expectedPreviousUrl));

        // Check the 1 item
        Map<String, Object> oneItem = (Map)context.get("1");
        String expectedOneText = "1";
        assertThat(oneItem.get("text"), CoreMatchers.equalTo(expectedOneText));
        String expectedOneActive = "true";
        assertThat(oneItem.get("active"), CoreMatchers.equalTo(expectedOneActive));
        String expectedOneUrl = "1";
        assertThat(oneItem.get("url"), CoreMatchers.equalTo(expectedOneUrl));
        // Check the 2 item
        Map<String, Object> secondItem = (Map)context.get("2");
        String expectedSecondText = "2";
        assertThat(secondItem.get("text"), CoreMatchers.equalTo(expectedSecondText));
        String expectedSecondActive = "true";
        assertThat(secondItem.get("active"), CoreMatchers.equalTo(expectedSecondActive));
        String expectedSecondUrl = "2";
        assertThat(secondItem.get("url"), CoreMatchers.equalTo(expectedSecondUrl));
        // Check the 3 item
        Map<String, Object> thirdItem = (Map)context.get("3");
        String expectedThirdText = "3";
        assertThat(thirdItem.get("text"), CoreMatchers.equalTo(expectedThirdText));
        String expectedThirdActive = "true";
        assertThat(thirdItem.get("active"), CoreMatchers.equalTo(expectedThirdActive));
        String expectedThirdUrl = "3";
        assertThat(thirdItem.get("url"), CoreMatchers.equalTo(expectedThirdUrl));
        // Check the 4 item
        Map<String, Object> fourthItem = (Map)context.get("4");
        String expectedFourthText = "4";
        assertThat(fourthItem.get("text"), CoreMatchers.equalTo(expectedFourthText));
        String expectedFourthActive = "true";
        assertThat(fourthItem.get("active"), CoreMatchers.equalTo(expectedFourthActive));
        String expectedFourthUrl = "4";
        assertThat(fourthItem.get("url"), CoreMatchers.equalTo(expectedFourthUrl));
        // Check the 5 item
        Map<String, Object> fifthItem = (Map)context.get("5");
        String expectedFifthText = "5";
        assertThat(fifthItem.get("text"), CoreMatchers.equalTo(expectedFifthText));
        String expectedFifthActive = "true";
        assertThat(fifthItem.get("active"), CoreMatchers.equalTo(expectedFifthActive));
        String expectedFifthUrl = "5";
        assertThat(fifthItem.get("url"), CoreMatchers.equalTo(expectedFifthUrl));

        // Check the next item
        Map<String, Object> nextItem = (Map)context.get("next");
        String expectedNextText = "next";
        assertThat(nextItem.get("text"), CoreMatchers.equalTo(expectedNextText));
        String expectedNextActive = "false";
        assertThat(nextItem.get("active"), CoreMatchers.equalTo(expectedNextActive));
        String expectedNextUrl = "5";
        assertThat(nextItem.get("url"), CoreMatchers.equalTo(expectedNextUrl));
        // Check the last item
        Map<String, Object> lastItem = (Map)context.get("last");
        String expectedLastText = "last";
        assertThat(lastItem.get("text"), CoreMatchers.equalTo(expectedLastText));
        String expectedLastActive = "false";
        assertThat(lastItem.get("active"), CoreMatchers.equalTo(expectedLastActive));
        String expectedLastUrl = "5";
        assertThat(lastItem.get("url"), CoreMatchers.equalTo(expectedLastUrl));
    }
}
