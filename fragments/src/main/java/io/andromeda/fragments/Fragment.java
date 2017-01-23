package io.andromeda.fragments;

import com.alibaba.fastjson.JSON;
import com.vladsch.flexmark.ast.Node;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import static ro.pippo.core.util.ClasspathUtils.locateOnClasspath;

/**
 * @author Alexander Brandt
 */
public class Fragment implements Comparable<Fragment> {
    /** The logger instance for this class. */
    private final static Logger LOGGER = LoggerFactory.getLogger(Fragments.class);

    public Map<String,Object> frontMatter = new HashMap<>();
    protected Map<String,Object> context = new HashMap<>();
    protected FrontMatterType frontMatterType;

    private String filename;
    private Path path;
    public boolean visible = false;
    public String template;
    public String title;
    public String slug;
    public String url;
    public String full_url;
    public String full_url_encoded;
    public String content;
    public String preview;
    public int order;
    public String defaultLanguage;
    public Map<String,String> languages = new TreeMap<>();
    public Map<String,String> languagesPreview = new TreeMap<>();
    public Map<String,String> languagesTitles = new TreeMap<>();

    public Fragment(String filename, String baseUrl, String template, String defaultLanguage) {
        this.filename = filename;
        this.template = template;
        this.url = baseUrl;
        this.full_url = baseUrl;
        this.defaultLanguage = defaultLanguage;
        try {
            readFile();
        } catch (Exception ex) {
            LOGGER.error("Error reading file (" + filename + "): " + ex.toString());
        }
        LOGGER.info("Loaded: " + filename);
    }

    public String getFilename() {
        return "";
    }

    public String getDirectory() {
        return "";
    }

    protected final void readFile() throws Exception {
        // First try the classpath
        URL url = locateOnClasspath(filename);
        if (url == null) {
            Path path = Paths.get(filename);
            if (Files.exists(path)) {
                url = path.toUri().toURL();
            } else {
                LOGGER.error("Cannot load file \"" + filename + "\"!");
                throw new Exception(filename + " (The system cannot find the file specified)");
            }
        }
        path = Paths.get(url.toURI());
        BufferedReader br = new BufferedReader(new FileReader(url.getFile()));
        final String delimiter;

        // detect YAML front matter
        String line = br.readLine();
        if (line == null) {
            LOGGER.warn("File \"" + filename + "\" is empty.");
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
        interpretFrontMatterSpecial();
        parseMarkdown2(br);
    }

    /**
     * Parses the YAML front-matter.
     * @param yamlString A string containing the complete YAML front-matter
     */
    protected void parseYamlFrontMatter(String yamlString) {
        Yaml yaml = new Yaml();
        frontMatter = (Map< String, Object>) yaml.load(yamlString);
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
            frontMatter.put("slug", slug);
        }
        url = url + slug;
        full_url = full_url + slug;

        // Overwrite the default template, when a template is defined in the front matter
        String tempTemplate = (String)frontMatter.get(Constants.TEMPLATE_ID);
        if (tempTemplate != null) {
            template = tempTemplate;
        }
        title = (String)frontMatter.get(Constants.TITLE_ID);
        String visible = (String) frontMatter.getOrDefault(Constants.VISIBLE_ID, "true");
        if (visible.equals("true")) {
            this.visible = true;
        }
        order = Integer.parseInt((String)frontMatter.getOrDefault(Constants.ORDER_ID, Integer.toString(Integer.MIN_VALUE)));

    }

    /**
     *  Special front matter entries. Needs to be taken care of in child class.
     */
    protected void interpretFrontMatterSpecial() {
        // empty for extension purposes
    }

    /**
     * Parses the JSON front-matter.
     * @param jsonString A string containing the complete YAML front-matter
     */
    protected void parseJsonFrontMatter(String jsonString) {

        frontMatter = (Map< String, Object>) JSON.parse(jsonString);
    }

    protected void parseMarkdown(BufferedReader br) throws IOException {
        String markdown = org.apache.commons.io.IOUtils.toString(br);
        Parser parser = Parser.builder().build();
        Node document = parser.parse(markdown);
        HtmlRenderer renderer = HtmlRenderer.builder().build();
        content = renderer.render(document);
    }

    protected void parseMarkdown2(BufferedReader br) throws IOException {
        String buffer = "";
        String currentLanguage = defaultLanguage;

        String line = br.readLine();
        while (line != null) {
            if (line.matches("--- \\w{2} ---")) {
                languages.put(currentLanguage, parseMarkdown3(buffer));
                languagesPreview.put(currentLanguage, parseMarkdown3(extractPreview(buffer)));
                currentLanguage = line.substring(4, 6);
                buffer = "";
            } else if (line.matches("--- \\w{2}-\\w{2} ---")) {
                languages.put(currentLanguage, parseMarkdown3(buffer));
                languagesPreview.put(currentLanguage, parseMarkdown3(extractPreview(buffer)));
                currentLanguage = line.substring(4, 9);
                buffer = "";
            } else {
                buffer = buffer + line;
            }
            line = br.readLine();
            if (line != null) {
                line = line + "\n";
            }
        }
        languages.put(currentLanguage, parseMarkdown3(buffer));
        languagesPreview.put(currentLanguage, parseMarkdown3(extractPreview(buffer)));
    }

    protected String parseMarkdown3(String content) {
        Parser parser = Parser.builder().build();
        Node document = parser.parse(content);
        HtmlRenderer renderer = HtmlRenderer.builder().build();
        return renderer.render(document);
    }

    public void update(String language) {
        if (language == null) {
            language = defaultLanguage;
        }
        content = languages.get(language);
        preview = languagesPreview.get(language);
        if (content == null) {
            content = languages.get(defaultLanguage);
            preview = languagesPreview.get(defaultLanguage);
            if (content == null) {
                content = "No content defined for this language: " + defaultLanguage;
                preview = "No content defined for this language: " + defaultLanguage;
            }
        }
    }

    private String extractPreview(String content) {
        //Check if preview is defined inside front Matter
        //A preview property on a post. The text of this property runs through the appropriate template and be saved as the preview for a post
        String result = (String)frontMatter.get(Constants.PREVIEW_ID);
        if (result != null) {
            return result;
        }
        //The last, and most likely easiest, is specifying a readMoreTag option in your Poet configuration, which by default is <!--more-->. Whenever the readMoreTag is found int he post, anything proceeding it becomes the preview. You can set this globally in your Poet config, or specify a readMoreTag property for each post individually
        result = content.trim();
        if (result.contains("<!--more-->")) {
            return result.substring(0, result.indexOf("<!--more-->"));
        }
        return "";
    }

    public int compareTo(Fragment other) {
        return this.order - other.order;
    }

}
