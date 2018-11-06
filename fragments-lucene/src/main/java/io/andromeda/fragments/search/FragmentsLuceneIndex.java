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

import io.andromeda.fragments.Fragment;
import io.andromeda.fragments.Fragments;
import io.andromeda.fragments.Utilities;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.TopScoreDocCollector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static io.andromeda.fragments.Constants.CONTENT_ID;
import static io.andromeda.fragments.Constants.FULL_URL_ENCODED_ID;
import static io.andromeda.fragments.Constants.FULL_URL_ID;
import static io.andromeda.fragments.Constants.PREVIEW_ID;
import static io.andromeda.fragments.Constants.SLUG_ID;
import static io.andromeda.fragments.Constants.TITLE_ID;
import static io.andromeda.fragments.Utilities.PAGINATION_ID;

/**
 * Custom Lucene DB for Fragments data.
 * @author Alexander Brandt
 */
public class FragmentsLuceneIndex {
    public static final String PAGES_ID = "pages";
    public static final String RESULTS_ID = "results";

    private SearchConfiguration configuration;
    private List<Fragments> fragmentsList = new ArrayList<>();

    /**
     * The logger instance for this class.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(FragmentsLuceneIndex.class);

    public FragmentsLuceneIndex(SearchConfiguration configuration) {
        this.configuration = configuration;
        LOGGER.info("FragmentsLuceneIndex configuration:");
        LOGGER.info("Hits per page: {}", configuration.getHitsPerPage());
        LOGGER.info("Maximum no in Pagination: {}", configuration.getMaxNoInPagination());
        LOGGER.info("Maximum results: {}", configuration.getMaximumResults());
        LOGGER.info("Including invisible Fragments: {}", configuration.getIncludingInvisible());
    }

    public void addFragments(Fragments fragments) {
        fragmentsList.add(fragments);
        LOGGER.debug("Adding fragments [{}] to be indexed.", fragments.getName());
    }

    public boolean createIndex() {
        try {
            IndexWriterConfig config = new IndexWriterConfig(configuration.getAnalyzer());

            IndexWriter indexWriter = new IndexWriter(configuration.getDirectory(), config);
            for (Fragments fragments: fragmentsList) {
                LOGGER.info("Creating index for fragments: {}", fragments.getName());
                for (Fragment fragment: fragments.getFragments(configuration.getIncludingInvisible())) {
                    Document doc = new Document();
                    doc.add(new TextField(TITLE_ID, fragment.getTitle(), Field.Store.YES));
                    doc.add(new TextField(PREVIEW_ID, fragment.getPreviewTextOnly(), Field.Store.YES));
                    doc.add(new TextField(CONTENT_ID, fragment.getContent(), Field.Store.YES));
                    doc.add(new TextField(SLUG_ID, fragment.getSlug(), Field.Store.YES));
                    doc.add(new TextField(FULL_URL_ENCODED_ID, fragment.getFullUrlEncoded(), Field.Store.YES));
                    doc.add(new TextField(FULL_URL_ID, fragment.getFullUrl(), Field.Store.YES));

                    // use a string field for isbn because we don't want it tokenized
                    // doc.add(new StringField("isbn", isbn, Field.Store.YES));
                    indexWriter.addDocument(doc);
                }
            }
            indexWriter.close();
        } catch (IOException e) {
            LOGGER.error(e.toString());
        }
        return true;
    }

    /*
    public List<Document> feeds(int from, String queryString) {
        List<Document> result = new ArrayList<Document>();
        try {
            DirectoryReader reader = DirectoryReader.open(directory);
            IndexSearcher searcher = new IndexSearcher(reader);
            TopScoreDocCollector collector = TopScoreDocCollector.create(maximumResults);  // MAX_RESULTS is just an int limiting the total number of hits
            int startIndex = (from - 1) * hitsPerPage;  // our page is 1 based - so we need to convert to zero based
            Query query = new QueryParser("content", analyzer).parse(queryString);
            searcher.feeds(query, collector);
            TopDocs docs = collector.topDocs(startIndex, hitsPerPage);
            ScoreDoc[] hits = docs.scoreDocs;
            for (int i = 0; i < hitsPerPage; ++i) {
                int docId = hits[i].doc;
                Document document = searcher.doc(docId);
                LOGGER.info("{}. {} [{}] [{}]", (i + 1), document.get("title"), document.get("preview"), document.get("full_url"));
            }
        } catch (IOException|ParseException e) {
            LOGGER.error(e.toString());
        }
        return result;
    }
    */

    /*public TopDocs feeds(int from, String queryString) {
        TopDocs result = null;
        try {
            DirectoryReader reader = DirectoryReader.open(configuration.getDirectory());
            IndexSearcher searcher = new IndexSearcher(reader);
            TopScoreDocCollector collector = TopScoreDocCollector.create(configuration.getMaximumResults());  // MAX_RESULTS is just an int limiting the total number of hits
            int startIndex = (from - 1) * configuration.getHitsPerPage();  // our page is 1 based - so we need to convert to zero based
            Query query = new QueryParser(CONTENT_ID, configuration.getAnalyzer()).parse(queryString);
            searcher.feeds(query, collector);
            result = collector.topDocs(startIndex, configuration.getHitsPerPage());
        } catch (IOException|ParseException e) {
            LOGGER.error(e.toString());
        }
        return result;
    }*/

    public Map<String, Object> search2(int page, String queryString) {
        Map<String, Object> result = new TreeMap<>();
        Map<String, Object> resultMap = new TreeMap<>();
        try {
            DirectoryReader reader = DirectoryReader.open(configuration.getDirectory());
            IndexSearcher searcher = new IndexSearcher(reader);
            TopScoreDocCollector collector = TopScoreDocCollector.create(configuration.getMaximumResults());  // MAX_RESULTS is just an int limiting the total number of hits
            int startIndex = (page - 1) * configuration.getHitsPerPage();  // our page is 1 based - so we need to convert to zero based
            Query query = new QueryParser(CONTENT_ID, configuration.getAnalyzer()).parse(queryString);
            searcher.search(query, collector);
            TopDocs docs = collector.topDocs(startIndex, configuration.getHitsPerPage());
            ScoreDoc[] hits = docs.scoreDocs;
            result.put(PAGES_ID, (int)(Math.ceil(docs.totalHits/10.)));
            result.put(PAGINATION_ID, Utilities.calculatePagination(page, configuration.getMaxNoInPagination(), docs.totalHits));
            for (int i = 0; i < hits.length; ++i) {
                Map<String, Object> item = new TreeMap<>();
                int docId = hits[i].doc;
                Document document = searcher.doc(docId);
                item.put(TITLE_ID, document.get(TITLE_ID));
                item.put(PREVIEW_ID, document.get(PREVIEW_ID));
                item.put(FULL_URL_ID, document.get(FULL_URL_ID));
                resultMap.put(Integer.toString(i + 1), item);
                //LOGGER.info("{}. {} [{}] [{}]", (i + 1), document.get("title"), document.get("preview"), document.get("full_url"));
            }
            resultMap.put(RESULTS_ID, hits.length);
            result.put(RESULTS_ID, resultMap);
        } catch (IOException|ParseException e) {
            LOGGER.error(e.toString());
        }
        return result;
    }

}
