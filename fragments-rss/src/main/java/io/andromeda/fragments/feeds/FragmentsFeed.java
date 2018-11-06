/*
 * Copyright (C) 2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.andromeda.fragments.feeds;

import com.rometools.rome.feed.synd.SyndContent;
import com.rometools.rome.feed.synd.SyndContentImpl;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndEntryImpl;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.feed.synd.SyndFeedImpl;
import com.rometools.rome.io.SyndFeedOutput;
import io.andromeda.fragments.Fragment;
import io.andromeda.fragments.Fragments;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

/**
 * Create an RSS feed from Fragments data.
 * @author Alexander Brandt
 */
public class FragmentsFeed {
    private FeedConfiguration configuration;

    /** The logger instance for this class. */
    private static final Logger LOGGER = LoggerFactory.getLogger(FragmentsFeed.class);

    public FragmentsFeed(FeedConfiguration configuration) {
        this.configuration = configuration;
        LOGGER.info("FragmentsFeed configuration:");
        LOGGER.info("Feed type: {}", configuration.getFeedType());
        LOGGER.info("Feed file : {}", configuration.getPath());
        LOGGER.info("Title: {}", configuration.getTitle());
        LOGGER.info("Description: {}", configuration.getDescription());
        LOGGER.info("Link: {}", configuration.getLink());
        LOGGER.info("Published Date: {}", configuration.getPublishedDate());
        LOGGER.info("Including invisible Fragments: {}", configuration.getIncludingInvisible());
    }

    public boolean createFeed(Fragments fragments) {
        try {
            SyndFeed feed = new SyndFeedImpl();
            feed.setFeedType(configuration.getFeedType().toString());

            feed.setTitle(configuration.getTitle());
            feed.setLink(configuration.getLink());
            feed.setDescription(configuration.getDescription());
            feed.setPublishedDate(configuration.getPublishedDate());

            List<SyndEntry> entries = new ArrayList();
            SyndEntry entry;
            SyndContent description;

            LOGGER.info("Creating feed for fragments: {}", fragments.getName());
            for (Fragment fragment: fragments.getFragments(configuration.getIncludingInvisible())) {
                entry = new SyndEntryImpl();
                entry.setTitle(fragment.getTitle());
                entry.setLink(fragment.getFullUrl());
                entry.setPublishedDate(fragment.getDate());
                description = new SyndContentImpl();
                description.setType("text/html");
                if (configuration.getPreviewTextOnly()) {
                    description.setValue(fragment.getPreview());
                } else {
                    description.setValue(fragment.getContent());
                }
                entry.setDescription(description);
                entries.add(entry);
            }
            feed.setEntries(entries);

            try (BufferedWriter writer = Files.newBufferedWriter(configuration.getPath(), StandardCharsets.UTF_8)) {
                SyndFeedOutput output = new SyndFeedOutput();
                output.output(feed, writer);
            }
        } catch (Exception e) {
            LOGGER.error("Exception: {}", e);
            return false;
        }
        return true;
    }

}
