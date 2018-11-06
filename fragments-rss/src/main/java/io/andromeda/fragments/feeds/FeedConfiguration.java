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

import java.nio.file.Path;
import java.util.Date;

/**
 * Configuration for RSS feed of Fragments data.
 * @author Alexander Brandt
 */
public class FeedConfiguration {
    /** Feed type. */
    private FeedType feedType;
    /** Path of the created file. */
    private Path path;
    /** Feed title */
    private String title;
    /** Feed URL */
    private String link;
    /** Feed description */
    private String description;
    /** Published date */
    private Date publishedDate;
    /** Include invisible Fragments in the feeds index. Normally you want to set this to false. Default is false. */
    private boolean includingInvisible = false;
    /** Include only the preview text of the fragments. If false the complete content will be put into the feed.
     *  Default is false. */
    private boolean previewTextOnly = false;

    public FeedConfiguration(FeedType feedType, Path path, String title, String description, String link,
                             Date publishedDate, boolean includingInvisible, boolean previewTextOnly) {
        this.feedType = feedType;
        this.path = path;
        this.title = title;
        this.description = description;
        this.link = link;
        this.publishedDate = publishedDate;
        this.includingInvisible = includingInvisible;
        this.previewTextOnly = previewTextOnly;
    }

    public FeedType getFeedType() {
        return feedType;
    }

    public Path getPath() {
        return path;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getLink() {
        return link;
    }

    public Date getPublishedDate() {
        return publishedDate;
    }

    public boolean getIncludingInvisible() {
        return includingInvisible;
    }

    public boolean getPreviewTextOnly() {
        return previewTextOnly;
    }
}
