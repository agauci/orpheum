package com.orpheum.benchmark.competitor.support;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * Utility class for fetching and cleaning HTML content from URLs using JSoup
 */
public class HtmlContentExtractor {

    private static final int TIMEOUT_MS = 5000;

    /**
     * Fetches content from the given URL and returns cleaned HTML
     *
     * @param urlString the URL to fetch
     * @return cleaned HTML string with structure preserved
     */
    public static String fetchAndClean(String urlString) {
        Document doc = fetchDocument(urlString);
        cleanDocument(doc);
        return doc.body().html();
    }

    /**
     * Fetches content and returns as a compact single-line HTML string
     *
     * @param urlString the URL to fetch
     * @return compact cleaned HTML string
     */
    public static String fetchAndCleanCompact(String urlString) {
        String html = fetchAndClean(urlString);
        return compactHtml(html);
    }

    /**
     * Fetches content and returns as formatted/indented HTML for readability
     *
     * @param urlString the URL to fetch
     * @return formatted cleaned HTML string
     */
    public static String fetchAndCleanFormatted(String urlString) {
        Document doc = fetchDocument(urlString);
        cleanDocument(doc);
        doc.outputSettings().indentAmount(2).prettyPrint(true);
        return doc.body().html();
    }

    /**
     * Fetches the HTML document from URL using JSoup
     */
    private static Document fetchDocument(String urlString) {
        try {
            return Jsoup.connect(urlString)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36")
                    .timeout(TIMEOUT_MS)
                    .get();
        } catch (IOException e) {
            throw new RuntimeException("Error fetching HTML from URL: " + urlString, e);
        }
    }

    /**
     * Cleans the document by removing non-content elements and attributes
     */
    private static void cleanDocument(Document doc) {
        // Remove non-content elements
        removeNonContentElements(doc);

        // Remove all attributes from all elements
        removeAllAttributes(doc);

        // Configure output settings for clean HTML
        doc.outputSettings()
                .prettyPrint(false)
                .charset("UTF-8");
    }

    // List of non-content elements to remove
    private static final String[] NON_CONTENT_ELEMENTS = {
            "script", "style", "nav", "header", "footer", "aside",
            "form", "iframe", "noscript", "svg", "button",
            "input", "select", "textarea"
    };

    /**
     * Removes non-content elements like scripts, styles, nav, header, footer, etc.
     */
    private static void removeNonContentElements(Document doc) {
        for (String tag : NON_CONTENT_ELEMENTS) {
            doc.select(tag).remove();
        }
    }

    /**
     * Removes all attributes from all elements in the document
     */
    private static void removeAllAttributes(Document doc) {
        Elements allElements = doc.getAllElements();

        for (Element element : allElements) {
            // Create a copy of attributes to avoid concurrent modification
            java.util.List<Attribute> attrs = new java.util.ArrayList<>(element.attributes().asList());

            // Remove each attribute
            for (Attribute attr : attrs) {
                element.removeAttr(attr.getKey());
            }
        }
    }

    /**
     * Compacts HTML by removing all newlines and extra whitespace
     */
    private static String compactHtml(String html) {
        // Remove all newlines
        html = html.replaceAll("\\r?\\n", " ");

        // Collapse multiple spaces into one
        html = html.replaceAll("\\s+", " ");

        // Remove spaces around tags
        html = html.replaceAll("\\s*<\\s*", "<");
        html = html.replaceAll("\\s*>\\s*", ">");

        return html.trim();
    }

    /**
     * Alternative method: Clean HTML from a string instead of URL
     *
     * @param html the HTML string to clean
     * @return cleaned HTML string
     */
    public static String cleanHtmlString(String html) {
        Document doc = Jsoup.parse(html);
        cleanDocument(doc);
        return doc.body().html();
    }

    /**
     * Alternative method: Clean HTML from a string and return compact version
     *
     * @param html the HTML string to clean
     * @return compact cleaned HTML string
     */
    public static String cleanHtmlStringCompact(String html) {
        String cleaned = cleanHtmlString(html);
        return compactHtml(cleaned);
    }

    /**
     * Example usage
     */
    public static void main(String[] args) {
        String url = "https://fbref.com/en/comps/9/Premier-League-Stats";

        // Get cleaned HTML (compact - single line)
        System.out.println("=== Cleaned HTML (Compact) ===");
        String compactHtml = fetchAndCleanCompact(url);
        System.out.println(compactHtml);
        System.out.println("\nLength: " + compactHtml.length() + " characters");

        // Get cleaned HTML (formatted for readability)
        System.out.println("\n\n=== Cleaned HTML (Formatted) ===");
        String formattedHtml = fetchAndCleanFormatted(url);
        System.out.println(formattedHtml);
    }
}