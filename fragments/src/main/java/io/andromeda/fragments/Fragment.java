/*
 * Copyright (C) 2017 the original author or authors.
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
package io.andromeda.fragments;

import com.alibaba.fastjson.JSON;
import com.vladsch.flexmark.Extension;
import com.vladsch.flexmark.ast.Node;
import com.vladsch.flexmark.ext.tables.TablesExtension;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.format.options.DiscretionaryText;
import com.vladsch.flexmark.util.mappers.CharWidthProvider;
import com.vladsch.flexmark.util.options.MutableDataSet;
import io.andromeda.fragments.types.FrontMatterType;
import io.andromeda.fragments.types.RouteType;
import org.apache.commons.io.FilenameUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Whitelist;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.representer.Representer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static ro.pippo.core.util.ClasspathUtils.locateOnClasspath;

/**
 * A Fragment representing one Markdown file (optionally containing multiple languages).
 *
 * @author Alexander Brandt
 */
public class Fragment implements Comparable<Fragment> {
    /**
     * The logger instance for this class.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(Fragments.class);

    private Map<String, Object> frontMatter = new TreeMap<>();
    private Map<String, Object> context = new TreeMap<>();
    private FrontMatterType frontMatterType;

    private Configuration configuration;
    private String filename;
    private Path path;
    private boolean visible = false;
    private String template;
    private String name;
    private String title;
    private String slug;
    private String url;
    private String fullUrl;
    private String fullUrlEncoded;
    private String content;
    private String preview;
    private String previewTextOnly;
    private int order;
    private String defaultLanguage;
    private ZonedDateTime dateTime;
    private Date date;
    private Map<String, String> languages = new TreeMap<>();
    private Map<String, String> languagesPreview = new TreeMap<>();
    private Map<String, String> languagesPreviewTextOnly = new TreeMap<>();
    private Map<String, String> languagesTitles = new TreeMap<>();
    private List<String> categories = new ArrayList<>();
    private List<String> tags = new ArrayList<>();

    /**
     * Constructor
     *
     * @param filename        The filename of the Markdown file.
     * @param defaultLanguage The default language for this fragment.
     * @param configuration   The Configuration object.
     */
    public Fragment(String filename, String defaultLanguage, Configuration configuration) {
        this.configuration = configuration;
        this.filename = filename;
        this.template = configuration.getDefaultTemplate();
        this.url = configuration.getUrlPath();
        this.fullUrl = configuration.getUrlPath();
        this.name = configuration.getName();
        this.defaultLanguage = defaultLanguage;
        try {
            boolean success = readFile();
            if (success) {
                LOGGER.info("Loaded: {}", filename);
            }
        } catch (Exception e) {
            LOGGER.error("Error reading file ({}): {}", filename, e.toString());
        }
    }

    protected final boolean readFile() throws Exception {
        // First try the classpath
        URL localUrl = locateOnClasspath(filename);
        if (localUrl == null) {
            Path localPath = Paths.get(filename);
            if (localPath.toFile().exists()) {
                localUrl = localPath.toUri().toURL();
            } else {
                LOGGER.error("Cannot load file \"{}\"!", filename);
                return false;
            }
        }
        path = Paths.get(localUrl.toURI());
        try (BufferedReader br = new BufferedReader(new FileReader(localUrl.getFile()))) {
            final String delimiter;

            // detect YAML front matter
            String line = br.readLine();
            if (line == null) {
                LOGGER.warn("File \"{}\" is empty.", filename);
                throw new Exception("File is empty: " + path.normalize().toAbsolutePath().toString());
            }
            while (line.isEmpty()) {
                line = br.readLine();
            }
            if (!line.matches("[-]{3,}")) { // use at least three dashes or opening curly braces
                if (!line.matches("[{]{3,}")) {
                    throw new IllegalArgumentException("YAML/JSON Front Matter is missing in file: " + path.normalize().toString());
                } else {
                    frontMatterType = FrontMatterType.JSON;
                    delimiter = "}}}";
                }
            } else {
                frontMatterType = FrontMatterType.YAML;
                delimiter = line;
            }

            // scan front matter
            StringBuilder sb = new StringBuilder();
            if (frontMatterType == FrontMatterType.JSON) {
                sb.append("{");
            }
            line = br.readLine();
            while (!line.equals(delimiter)) {
                sb.append(line);
                sb.append("\n");
                line = br.readLine();
            }
            if (frontMatterType == FrontMatterType.JSON) {
                sb.append("}");
            }

            // readFile data
            if (frontMatterType == FrontMatterType.YAML) {
                parseYamlFrontMatter(sb.toString());
            } else {
                parseJsonFrontMatter(sb.toString());
            }

            interpretFrontMatterGeneral();
            parseContent(br);
        } catch (IOException e) {
            LOGGER.error("Error: ", e);
        }
        return true;
    }

    /**
     * Parses the YAML front-matter.
     *
     * @param yamlString A string containing the complete YAML front-matter
     */
    protected void parseYamlFrontMatter(String yamlString) {
        //Use custom resolver to prevent the default implicit Tags of SnakeYAML.
        Yaml yaml = new Yaml(new Constructor(), new Representer(), new DumperOptions(), new YAMLResolver());
        frontMatter = (Map<String, Object>) yaml.load(yamlString);
    }

    /**
     * General front matter entries, which should always be available.
     */
    private void interpretFrontMatterGeneral() {
        String fmSlug = (String) frontMatter.get(Constants.SLUG_ID);
        if (fmSlug != null) {
            slug = fmSlug;
        } else {
            String filename2 = FilenameUtils.getName(filename);
            slug = FilenameUtils.removeExtension(filename2);
            slug = Utilities.slugify(slug);
            frontMatter.put(Constants.SLUG_ID, slug);
        }
        //Date handling
        String localDate = (String) frontMatter.get(Constants.DATE_ID);
        dateHandling(localDate);
        if (configuration.getRouteType() == RouteType.ARTICLES) {
            url = url + slug;
        } else if (configuration.getRouteType() == RouteType.BLOG) {
            if (localDate == null) {
                LOGGER.error("Date is not available for a fragment of type Blog: {}", filename);
            }
            url = url + "/" + dateTime.getYear() + "/" + String.format("%02d", dateTime.getMonthValue())
                    + "/" + String.format("%02d", dateTime.getDayOfMonth()) + "/" + slug;
        }

        // Overwrite the default template, when a template is defined in the front matter
        String tempTemplate = (String) frontMatter.get(Constants.TEMPLATE_ID);
        if (tempTemplate != null) {
            template = tempTemplate;
        }
        title = (String) frontMatter.get(Constants.TITLE_ID);
        String localVisible = (String) frontMatter.getOrDefault(Constants.VISIBLE_ID, "true");
        if ("true".equals(localVisible)) {
            this.visible = true;
        }
        order = Integer.parseInt((String) frontMatter.getOrDefault(Constants.ORDER_ID, Integer.toString(Integer.MAX_VALUE)));

        tags = (List)frontMatter.get(Constants.TAGS_ID);
        categories = (List)frontMatter.get(Constants.CATEGORIES_ID);
    }

    /**
     * Parses the JSON front-matter.
     *
     * @param jsonString A string containing the complete JSON front-matter
     */
    protected void parseJsonFrontMatter(String jsonString) {

        frontMatter = (Map<String, Object>) JSON.parse(jsonString);
    }

    /**
     * Parses the Markdown content. Splits the different languages, if necessary. Also creates the preview (previews, in
     * case of multiple languages). Languages are separated either via "--- xx ---" or "--- xx-yy ---" meaning just the
     * language id or the language-country id.
     *
     * @param br BufferedReader of the Fragment file.
     * @throws IOException
     */
    protected void parseContent(BufferedReader br) throws IOException {
        StringBuilder buffer = new StringBuilder();
        String currentLanguage = defaultLanguage;

        String line = br.readLine();
        while (line != null) {
            if (line.matches("--- \\w{2} ---")) {
                languages.put(currentLanguage, parseMarkdown(buffer.toString()));
                String extractedPreview = parseMarkdown(extractPreview(buffer.toString()));
                languagesPreview.put(currentLanguage, extractedPreview);
                languagesPreviewTextOnly.put(currentLanguage, Jsoup.clean(extractedPreview, "", Whitelist.none(), new Document.OutputSettings().prettyPrint(true)));
                currentLanguage = line.substring(4, 6);
                buffer = new StringBuilder();
            } else if (line.matches("--- \\w{2}-\\w{2} ---")) {
                languages.put(currentLanguage, parseMarkdown(buffer.toString()));
                String extractedPreview = parseMarkdown(extractPreview(buffer.toString()));
                languagesPreview.put(currentLanguage, extractedPreview);
                languagesPreviewTextOnly.put(currentLanguage, Jsoup.clean(extractedPreview, "", Whitelist.none(), new Document.OutputSettings().prettyPrint(true)));
                currentLanguage = line.substring(4, 9);
                buffer = new StringBuilder();
            } else {
                buffer.append(line);
            }
            line = br.readLine();
            if (line != null) {
                line = line + "\n";
            }
        }
        languages.put(currentLanguage, parseMarkdown(buffer.toString()));
        String extractedPreview = parseMarkdown(extractPreview(buffer.toString()));
        languagesPreview.put(currentLanguage, extractedPreview);
        languagesPreviewTextOnly.put(currentLanguage, Jsoup.clean(extractedPreview, "", Whitelist.none(), new Document.OutputSettings().prettyPrint(true)));
        update(defaultLanguage);
    }

    protected String parseMarkdown(final String content) {
        Extension tablesExtension = TablesExtension.create();
        List<Extension> extensions = new ArrayList<>();
        extensions.add(tablesExtension);

        MutableDataSet options = new MutableDataSet();
        options.set(Parser.EXTENSIONS, extensions);
        //options.set(TablesExtension.TRIM_CELL_WHITESPACE, true);
        // options.set(TablesExtension.HEADER_SEPARATOR_COLUMN_MATCH, false);
        //options.set(TablesExtension.FORMAT_LEFT_ALIGN_MARKER, DiscretionaryText.ADD);
        //options.set(TablesExtension.FORMAT_CHAR_WIDTH_PROVIDER, new CharWidthProvider());
        Parser parser = Parser.builder(options).build();
        Node document = parser.parse(content);
        HtmlRenderer.Builder builder = HtmlRenderer.builder(options);
        builder.set(HtmlRenderer.OBFUSCATE_EMAIL, true);
        HtmlRenderer renderer = builder.build();
        return renderer.render(document);
    }

    public void update(final String language) {
        String localLanguage = language;
        if (localLanguage == null) {
            localLanguage = defaultLanguage;
        }
        content = languages.get(localLanguage);
        preview = languagesPreview.get(localLanguage);
        previewTextOnly = languagesPreviewTextOnly.get(localLanguage);
        if (content == null) {
            content = languages.get(defaultLanguage);
            preview = languagesPreview.get(defaultLanguage);
            previewTextOnly = languagesPreviewTextOnly.get(defaultLanguage);
            if (content == null) {
                String text = "No content defined for this language: " + defaultLanguage;
                content = text;
                preview = text;
                previewTextOnly = text;
            }
        }
    }

    private String extractPreview(String content) {
        //Check if preview is defined inside front Matter
        //A preview property on a post. The text of this property runs through the appropriate template and be saved as the preview for a post
        String result = (String) frontMatter.get(Constants.PREVIEW_ID);
        if (result != null) {
            return result;
        }
        //The last, and most likely easiest, is specifying a readMoreTag option in your Poet configuration, which by default is <!--more-->. Whenever the readMoreTag is found int he post, anything proceeding it becomes the preview. You can set this globally in your Poet config, or specify a readMoreTag property for each post individually
        result = content.trim();
        if (result.contains("<!--more-->")) {
            return result.substring(0, result.indexOf("<!--more-->"));
        } else if (result.contains("<!-- more -->")) {
            return result.substring(0, result.indexOf("<!-- more -->"));
        } else if (result.contains("<!-- more-->")) {
            return result.substring(0, result.indexOf("<!-- more-->"));
        } else if (result.contains("<!--more -->")) {
            return result.substring(0, result.indexOf("<!--more -->"));
        }
        return "";
    }

    private void dateHandling(String localDate) {
        if (localDate != null) {
            try {
                DateTimeFormatter formatter =
                        new DateTimeFormatterBuilder().appendPattern("yyyy-MM-dd['T'HH:mm]")
                                .parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
                                .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
                                .toFormatter();
                LocalDateTime localDateTime = LocalDateTime.parse(localDate, formatter);
                dateTime = ZonedDateTime.of(localDateTime, ZoneId.of("UTC"));
                this.date = Date.from(dateTime.toInstant());
            } catch (DateTimeParseException e) {
                LOGGER.error(e.toString());
                dateTime = ZonedDateTime.parse("1970-01-01T00:00:00Z", DateTimeFormatter.ISO_DATE_TIME);
            }
    }

    }

    @Override
    public int compareTo(Fragment other) {
        return this.order - other.order;
    }

    /********** Getters ***********************************************************************************************/

    /**
     * Returns the context of the Fragment.
     * @return The context of the Fragment.
     */
    public Map<String, Object> getContext() {
        return context;
    }

    /**
     * Returns the directory in which the file is located.
     * @return The directory in which the file is located.
     */
    public String getDirectory() {
        return path.normalize().toAbsolutePath().toFile().getParent();
    }

    /**
     * Returns the content of this Fragment.
     * @return The content of this Fragment.
     */
    public String getContent() {
        // Fixes output of \u2027 characters from GFM tables
        return content.replace("\u2028", "");
    }

    /**
     * Returns the date of this Fragment.
     * @return The date of this Fragment.
     */
    public Date getDate() {
        return date;
    }

    /**
     * Returns the ZonedDateTime of this Fragment.
     * @return The ZonedDateTime of this Fragment.
     */
    public ZonedDateTime getDateTime() {
        return dateTime;
    }

    /**
     * Returns the default Language of the Fragment.
     * @return The default Language of the Fragment.
     */
    public String getDefaultLanguage() {
        return defaultLanguage;
    }

    /**
     * Returns the filename of the Fragment.
     * @return The filename of the Fragment.
     */
    public String getFilename() {
        return filename;
    }

    /**
     * Returns the front matter of the Fragment.
     * @return The front matter of the Fragment.
     */
    public Map<String, Object> getFrontMatter() {
        return frontMatter;
    }

    /**
     * Returns the FrontMatterType of the Fragment.
     * @return The FrontMatterType of the Fragment.
     */
    public FrontMatterType getFrontMatterType() {
        return frontMatterType;
    }

    /**
     * Returns the full URL of the Fragment.
     * @return The full URL of the Fragment.
     */
    public String getFullUrl() {
        return fullUrl;
    }

    /**
     * Returns the encoded full URL of the Fragment.
     * @return The encoded full URL of the Fragment.
     */
    public String getFullUrlEncoded() {
        return fullUrlEncoded;
    }

    /**
     * Returns the languages of the Fragment.
     * @return The languages of the Fragment.
     */
    public Map<String, String> getLanguages() {
        return languages;
    }

    /**
     * Returns the languages preview of the Fragment.
     * @return The languages preview of the Fragment.
     */
    public Map<String, String> getLanguagesPreview() {
        return languagesPreview;
    }

    /**
     * Returns the languages preview (text only, no HTML markup) of the Fragment.
     * @return The languages preview (text only, no HTML markup) of the Fragment.
     */
    public Map<String, String> getLanguagesPreviewTextOnly() {
        return languagesPreviewTextOnly;
    }

    /**
     * Returns the languages titles of the Fragment.
     * @return The languages titles of the Fragment.
     */
    public Map<String, String> getLanguagesTitles() {
        return languagesTitles;
    }

    /**
     * Returns the name of the Fragments class this Fragment belongs to.
     * @return The name of the Fragments class this Fragment belongs to.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the order of the Fragment.
     * @return The order of the Fragment.
     */
    public int getOrder() {
        return order;
    }

    /**
     * Returns the preview of the Fragment.
     * @return The preview of the Fragment.
     */
    public String getPreview() {
        return preview;
    }

    /**
     * Returns the preview (text only, no HTML markup) of the Fragment.
     * @return The preview (text only, no HTML markup) of the Fragment.
     */
    public String getPreviewTextOnly() {
        return previewTextOnly;
    }

    /**
     * Returns the slug of the Fragment.
     * @return The slug of the Fragment.
     */
    public String getSlug() {
        return slug;
    }

    /**
     * Returns the template of the Fragment.
     * @return The template of the Fragment.
     */
    public String getTemplate() {
        return template;
    }

    /**
     * Returns the title of the Fragment in the current language.
     * @return The title of the Fragment in the current language.
     */
    public String getTitle() {
        if (title == null) {
            return "";
        } else {
            return title;
        }
    }

    /**
     * Returns the url of the Fragment.
     * @return The url of the Fragment.
     */
    public String getUrl() {
        return url;
    }

    /**
     * Returns the visibility of the Fragment.
     * @return The visibility of the Fragment.
     */
    public boolean getVisible() {
        return visible;
    }

    /**
     * Gets the list of all tags of this Fragment.
     * @return The list of all tags of this Fragment.
     */
    public List<String> getTags() {
        return tags;
    }

    /**
     * Gets the list of all categories of this Fragment.
     * @return The list of all categories of this Fragment.
     */
    public List<String> getCategories() {
        return categories;
    }

    /********** Setters ***********************************************************************************************/

    /**
     * Set the context of the Fragment.
     * @param context The new context of this Fragment.
     * @param replaceExisting When true, the whole context Map will be replaced, otherwise the new context will be
     *                        added to the existing context.
     */
    public void setContext(Map<String, Object> context, boolean replaceExisting) {
        if (replaceExisting) {
            this.context = context;
        } else {
            this.context.putAll(context);
        }
    }

    /**
     * Set the full URL of the Fragment.
     * @param fullUrl The new full URL of this Fragment.
     */
    public void setFullUrl(String fullUrl) {
        this.fullUrl = fullUrl;
    }

    /**
     * Set the encoded full URL of the Fragment.
     * @param fullUrlEncoded The new encoded full URL of this Fragment.
     */
    public void setFullUrlEncoded(String fullUrlEncoded) {
        this.fullUrlEncoded = fullUrlEncoded;
    }

    /**
     * Set the new order of this Fragment.
     * @param order The new order of this Fragment.
     */
    public void setOrder(int order) {
        this.order = order;
    }

}
