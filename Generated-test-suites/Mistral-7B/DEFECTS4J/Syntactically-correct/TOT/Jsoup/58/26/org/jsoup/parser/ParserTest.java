package org.jsoup.parser;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.ParseError;
import org.jsoup.parser.Parser;
import org.jsoup.parser.TreeBuilder;
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.ParseErrorList;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Test class for Parser.
 */
public class ParserTest {

    private Parser parser;

    @Before
    public void setUp() {
        parser = new Parser(new HtmlTreeBuilder());
    }

    /**
     * Test parseInput method with valid HTML and no error tracking.
     */
    @Test
    public void testParseInputValidHtmlNoErrorTracking() {
        String html = "<html><head><title>Test</title></head><body><p>Hello World!</p></body></html>";
        Document doc = parser.parseInput(html, "http://example.com");
        assertNotNull(doc);
        assertEquals("Test", doc.title().text());
        assertEquals("Hello World!", doc.body().text());
    }

    /**
     * Test parseInput method with valid HTML and error tracking.
     */
    @Test
    public void testParseInputValidHtmlWithErrorTracking() {
        String html = "<html><head><title>Test</title></head><body><p>Hello World!</p></body></html>";
        Parser parserWithErrorTracking = new Parser(new HtmlTreeBuilder()).setTrackErrors(1);
        Document doc = parserWithErrorTracking.parseInput(html, "http://example.com");
        assertNotNull(doc);
        assertEquals("Test", doc.title().text());
        assertEquals("Hello World!", doc.body().text());

        List<ParseError> errors = parserWithErrorTracking.getErrors();
        assertNotNull(errors);
        assertTrue(errors.isEmpty());
    }

    /**
     * Test parseInput method with invalid HTML and no error tracking.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testParseInputInvalidHtmlNoErrorTracking() {
        String html = "<html><head><title>Test</title></head><body><p>Hello World!</p></body></html>";
        html = html.substring(0, html.length() - 1); // remove the final closing body tag
        parser.parseInput(html, "http://example.com");
    }

    /**
     * Test parseInput method with invalid HTML and error tracking.
     */
    @Test
    public void testParseInputInvalidHtmlWithErrorTracking() {
        String html = "<html><head><title>Test</title></head><body><p>Hello World!</p></body></html>";
        html = html.substring(0, html.length() - 1); // remove the final closing body tag
        Parser parserWithErrorTracking = new Parser(new HtmlTreeBuilder()).setTrackErrors(1);
        Document doc = parserWithErrorTracking.parseInput(html, "http://example.com");
        assertNull(doc);

        List<ParseError> errors = parserWithErrorTracking.getErrors();
        assertNotNull(errors);
        assertEquals(1, errors.size());
        ParseError error = errors.get(0);
        assertEquals("Error parsing start tag : body", error.getMessage());
    }

    /**
     * Test setTreeBuilder method.
     */
    @Test
    public void testSetTreeBuilder() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        TreeBuilder newTreeBuilder = new XmlTreeBuilder();
        parser.setTreeBuilder(newTreeBuilder);
        assertEquals(newTreeBuilder, parser.getTreeBuilder());
    }

    /**
     * Test isTrackErrors method.
     */
    @Test
    public void testIsTrackErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        assertFalse(parser.isTrackErrors());

        Parser parserWithErrorTracking = new Parser(new HtmlTreeBuilder()).setTrackErrors(1);
        assertTrue(parserWithErrorTracking.isTrackErrors());
    }

    /**
     * Test setTrackErrors method.
     */
    @Test
    public void testSetTrackErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.setTrackErrors(1);
        assertTrue(parser.isTrackErrors());

        parser.setTrackErrors(0);
        assertFalse(parser.isTrackErrors());
    }

    /**
     * Test getErrors method.
     */
    @Test
    public void testGetErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        List<ParseError> errors = parser.getErrors();
        assertNull(errors);

        Parser parserWithErrors = new Parser(new HtmlTreeBuilder()).setTrackErrors(1);
        Document doc = parserWithErrors.parseInput("<html><body></body>", "http://example.com");
        List<ParseError> errorsWithErrors = parserWithErrors.getErrors();
        assertNotNull(errorsWithErrors);
        assertEquals(1, errorsWithErrors.size());
        ParseError error = errorsWithErrors.get(0);
        assertEquals("Error parsing start tag : body", error.getMessage());
    }

    /**
     * Test parse method.
     */
    @Test
    public void testParse() {
        String html = "<html><head><title>Test</title></head><body><p>Hello World!</p></body></html>";
        Document doc = Parser.parse(html, "http://example.com");
        assertNotNull(doc);
        assertEquals("Test", doc.title().text());
        assertEquals("Hello World!", doc.body().text());
    }

    /**
     * Test parseFragment method.
     */
    @Test
    public void testParseFragment() {
        String fragmentHtml = "<p>Hello World!</p>";
        Element context = Jsoup.parse("<html><body></body></html>").body();
        List<Node> nodes = Parser.parseFragment(fragmentHtml, context, "http://example.com");
        assertNotNull(nodes);
        assertEquals(1, nodes.size());
        assertTrue(nodes.get(0) instanceof Element);
        Element parsedElement = (Element) nodes.get(0);
        assertEquals("Hello World!", parsedElement.text());
    }

    /**
     * Test parseXmlFragment method.
     */
    @Test
    public void testParseXmlFragment() {
        String fragmentXml = "<root><element>Hello World!</element></root>";
        List<Node> nodes = Parser.parseXmlFragment(fragmentXml, "http://example.com");
        assertNotNull(nodes);
        assertEquals(1, nodes.size());
        assertTrue(nodes.get(0) instanceof Element);
        Element parsedElement = (Element) nodes.get(0);
        assertEquals("Hello World!", parsedElement.text());
    }
}