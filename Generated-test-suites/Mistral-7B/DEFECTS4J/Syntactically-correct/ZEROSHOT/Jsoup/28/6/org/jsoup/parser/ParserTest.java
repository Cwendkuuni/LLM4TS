package org.jsoup.parser;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.ParseError;
import org.jsoup.nodes.ParseErrorList;
import org.jsoup.nodes.TreeBuilder;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Test class for Parser.java
 */
public class ParserTest {

    private Parser parser;
    private TreeBuilder treeBuilder;

    /**
     * Setup method to create a new Parser instance with HtmlTreeBuilder
     */
    @Before
    public void setup() {
        treeBuilder = new HtmlTreeBuilder();
        parser = new Parser(treeBuilder);
    }

    /**
     * Test constructor with null TreeBuilder
     */
    @Test(expected = NullPointerException.class)
    public void testParserNullTreeBuilder() {
        new Parser(null);
    }

    /**
     * Test parseInput method with valid HTML
     */
    @Test
    public void testParseInputValidHtml() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        Document doc = parser.parseInput(html, "http://www.example.com");
        assertNull(parser.getErrors().size());
        assertEquals("<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>", doc.html());
    }

    /**
     * Test parseInput method with invalid HTML - missing end tag
     */
    @Test
    public void testParseInputInvalidHtmlMissingEndTag() {
        String html = "<html><head><title>Test</title><body><h1>Hello World";
        List<ParseError> errors = parser.getErrors();
        Document doc = parser.parseInput(html, "http://www.example.com");
        assertTrue(errors.size() > 0);
        assertEquals("Missing end tag h1", errors.get(0).getMessage());
        assertNull(doc.html());
    }

    /**
     * Test parseInput method with invalid HTML - malformed tag
     */
    @Test
    public void testParseInputInvalidHtmlMalformedTag() {
        String html = "<html> <body>";
        List<ParseError> errors = parser.getErrors();
        Document doc = parser.parseInput(html, "http://www.example.com");
        assertTrue(errors.size() > 0);
        assertEquals("Unexpected EOF while parsing element name", errors.get(0).getMessage());
        assertNull(doc.html());
    }

    /**
     * Test getTreeBuilder method
     */
    @Test
    public void testGetTreeBuilder() {
        assertEquals(treeBuilder, parser.getTreeBuilder());
    }

    /**
     * Test setTreeBuilder method
     */
    @Test
    public void testSetTreeBuilder() {
        TreeBuilder newTreeBuilder = new XmlTreeBuilder();
        parser.setTreeBuilder(newTreeBuilder);
        assertEquals(newTreeBuilder, parser.getTreeBuilder());
    }

    /**
     * Test isTrackErrors method
     */
    @Test
    public void testIsTrackErrorsDisabled() {
        assertFalse(parser.isTrackErrors());
    }

    /**
     * Test isTrackErrors method
     */
    @Test
    public void testIsTrackErrorsEnabled() {
        parser = new Parser(treeBuilder).setTrackErrors(1);
        assertTrue(parser.isTrackErrors());
    }

    /**
     * Test getErrors method with disabled error tracking
     */
    @Test
    public void testGetErrorsDisabled() {
        List<ParseError> errors = parser.getErrors();
        assertNull(errors);
    }

    /**
     * Test getErrors method with enabled error tracking
     */
    @Test
    public void testGetErrorsEnabled() {
        String invalidHtml = "<html><head><title>Test</title><body><h1>Hello<script>World</script></h1>";
        parser = new Parser(treeBuilder).setTrackErrors(2);
        List<ParseError> errors = parser.getErrors();
        assertEquals(2, errors.size());
        assertEquals("Unexpected '<' in tag name: script", errors.get(0).getMessage());
        assertEquals("Encountered text after document end", errors.get(1).getMessage());
    }

    /**
     * Test static parse method
     */
    @Test
    public void testStaticParse() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        Document doc = Parser.parse(html, "http://www.example.com");
        assertNull(Parser.getErrors().size());
        assertEquals("<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>", doc.html());
    }

    /**
     * Test static parseFragment method
     */
    @Test
    public void testStaticParseFragment() {
        String fragmentHtml = "<div><p>Test</p></div>";
        Element context = new Element("div");
        Document doc = Parser.parseFragment(fragmentHtml, context, "http://www.example.com");
        assertEquals(1, doc.size());
        assertEquals("div", doc.tagName());
    }

    /**
     * Test static parseBodyFragment method
     */
    @Test
    public void testStaticParseBodyFragment() {
        String bodyHtml = "<body><h1>Test</h1></body>";
        Document doc = Parser.parseBodyFragment(bodyHtml, "http://www.example.com");
        assertEquals("<html><head></head><body><h1>Test</h1></body></html>", doc.html());
    }

    /**
     * Test parseBodyFragmentRelaxed method (deprecated)
     */
    @Test
    public void testParseBodyFragmentRelaxed() {
        String bodyHtml = "<body><h1>Test</h1></body>";
        Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, "http://www.example.com");
        assertEquals("<html><head></head><body><h1>Test</h1></body></html>", doc.html());
    }
}