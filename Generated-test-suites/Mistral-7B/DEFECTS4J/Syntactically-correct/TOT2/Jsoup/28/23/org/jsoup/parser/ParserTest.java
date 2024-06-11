package org.jsoup.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {
    @Test
    public void testParseInput() {
        String html = "<html><body><h1>Hello World</h1></body></html>";
        String baseUri = "http://example.com";

        Parser parser = new Parser(new TreeBuilder());
        Document doc = parser.parseInput(html, baseUri);

        assertNotNull(doc);
        assertNotNull(doc.title());
        assertNotNull(doc.head());
        assertNotNull(doc.body());
        assertNotNull(doc.getElementsByTag("h1"));
    }

    @Test
    public void testIsTrackErrors() {
        Parser parser = new Parser(new TreeBuilder());
        assertFalse(parser.isTrackErrors());
    }

    @Test
    public void testParse() {
        String html = "<html><body><h1>Hello World</h1></body></html>";
        String baseUri = "http://example.com";

        Document doc = Parser.parse(html, baseUri);

        assertNotNull(doc);
        assertNotNull(doc.title());
        assertNotNull(doc.head());
        assertNotNull(doc.body());
        assertNotNull(doc.getElementsByTag("h1"));
    }

    @Test
    public void testParseEmptyHtml() {
        String html = "";
        String baseUri = "http://example.com";

        Document doc = Parser.parse(html, baseUri);

        assertNotNull(doc);
        assertNull(doc.title());
        assertNull(doc.head());
        assertNull(doc.body());
        assertTrue(doc.text().isEmpty());
    }

    @Test
    public void testParseNullHtml() {
        String html = null;
        String baseUri = "http://example.com";

        try {
            Document doc = Parser.parse(html, baseUri);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testParseMalformedHtml() {
        String html = "<html<p>";
        String baseUri = "http://example.com";

        try {
            Document doc = Parser.parse(html, baseUri);
            fail("Expected MalformedURLException or MalformedElementException");
        } catch (Exception e) {
            // Expected
        }
    }

    @Test
    public void testParseWithNullTreeBuilder() {
        String html = "<html><body><h1>Hello World</h1></body></html>";
        String baseUri = "http://example.com";
        TreeBuilder treeBuilder = null;

        try {
            Parser parser = new Parser(treeBuilder);
            Document doc = parser.parseInput(html, baseUri);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }
}