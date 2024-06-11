package org.jsoup.parser;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.ParseError;
import org.jsoup.parser.Parser;
import org.jsoup.parser.TreeBuilder;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Tests for the Parser class.
 */
public class ParserTest {
    private Parser parser;
    private TreeBuilder treeBuilder;

    @Before
    public void setUp() {
        treeBuilder = new HtmlTreeBuilder();
        parser = new Parser(treeBuilder);
    }

    /**
     * Tests the parseInput method with a valid HTML string and baseUri.
     */
    @Test
    public void testParseInputValidHtml() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        String baseUri = "http://example.com";
        Document doc = parser.parseInput(html, baseUri);

        assertNotNull(doc);
        Element title = doc.title();
        assertEquals("Test", title.text());
        Element h1 = doc.selectFirst("h1");
        assertEquals("Hello World", h1.text());
    }

    /**
     * Tests the parseInput method with a valid HTML string, empty baseUri, and error tracking enabled.
     */
    @Test
    public void testParseInputValidHtmlWithErrorsTracking() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World</h1><script>alert('XSS')</script></body></html>";
        String baseUri = "";
        parser.setTrackErrors(1);
        Document doc = parser.parseInput(html, baseUri);

        assertNotNull(doc);
        Element title = doc.title();
        assertEquals("Test", title.text());
        Element h1 = doc.selectFirst("h1");
        assertEquals("Hello World", h1.text());

        List<ParseError> errors = parser.getErrors();
        assertTrue(errors.size() > 0);
        ParseError error = errors.get(0);
        assertEquals(1, error.getLine());
        assertEquals(7, error.getColumn());
        assertEquals("Expected '<', but was 'S' (<script>)", error.getMessage());
    }

    /**
     * Tests the parseInput method with an invalid HTML string and baseUri.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testParseInputInvalidHtml() {
        String html = "<html><body>";
        String baseUri = "http://example.com";
        parser.parseInput(html, baseUri);
    }

    /**
     * Tests the setTreeBuilder method.
     */
    @Test
    public void testSetTreeBuilder() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        String baseUri = "http://example.com";

        TreeBuilder newTreeBuilder = new XmlTreeBuilder();
        parser.setTreeBuilder(newTreeBuilder);
        Document doc = parser.parseInput(html, baseUri);

        assertNotNull(doc);
        Element title = doc.title();
        assertNull(title);
    }

    /**
     * Tests the isTrackErrors method.
     */
    @Test
    public void testIsTrackErrors() {
        parser.setTrackErrors(1);
        assertTrue(parser.isTrackErrors());

        parser.setTrackErrors(0);
        assertFalse(parser.isTrackErrors());
    }

    /**
     * Tests the parseStaticMethods.
     */
    @Test
    public void testParseStaticMethods() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";

        Document doc = Parser.parse(html, "");
        Element title = doc.title();
        assertEquals("Test", title.text());
        Element h1 = doc.selectFirst("h1");
        assertEquals("Hello World", h1.text());

        List<Node> nodes = Parser.parseFragment("<p>Test</p>", null, "");
        Node node = nodes.get(0);
        assertEquals("p", node.tagName());
        assertEquals("Test", node.text());

        Document docBody = Parser.parseBodyFragment("<p>Test</p>", "");
        Element titleBody = docBody.title();
        assertNull(titleBody);
        Element p = docBody.selectFirst("p");
        assertEquals("Test", p.text());
    }
}