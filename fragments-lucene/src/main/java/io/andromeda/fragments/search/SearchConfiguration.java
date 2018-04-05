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
package io.andromeda.fragments.search;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.store.Directory;

/**
 * Search configuration for custom Lucene DB of Fragments data.
 * @author Alexander Brandt
 */
public class SearchConfiguration {
    /** Instance of Lucene Analyzer. */
    private Analyzer analyzer;
    /** Instance of Lucene Directory. */
    private Directory directory;
    /** Include invisible Fragments in the search index. Normally you want to set this to false. Default is false. */
    private boolean includingInvisible = false;
    private int maximumResults;
    private int hitsPerPage;
    private int maxNoInPagination;

    public SearchConfiguration(Analyzer analyzer, Directory directory, int hitsPerPage, int maxNoInPagination,
                               int maximumResults, boolean includingInvisible) {
        this.analyzer = analyzer;
        this.directory = directory;
        this.hitsPerPage = hitsPerPage;
        this.maxNoInPagination = maxNoInPagination;
        this.maximumResults = maximumResults;
        this.includingInvisible = includingInvisible;
    }

    public SearchConfiguration(Analyzer analyzer, Directory directory) {
        this(analyzer, directory, 10, 5, 10000, false);
    }

    public Analyzer getAnalyzer() {
        return analyzer;
    }

    public Directory getDirectory() {
        return directory;
    }

    public int getHitsPerPage() {
        return hitsPerPage;
    }

    public int getMaxNoInPagination() {
        return maxNoInPagination;
    }

    public int getMaximumResults() {
        return maximumResults;
    }

    public boolean getIncludingInvisible() {
        return includingInvisible;
    }
}
