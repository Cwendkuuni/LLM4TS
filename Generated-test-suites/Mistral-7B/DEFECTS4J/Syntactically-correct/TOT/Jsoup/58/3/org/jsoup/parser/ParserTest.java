package org.jsoup.parser;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.ParseError;
import org.jsoup.parser.Parser;
import org.jsoup.parser.TreeBuilder;
import org.jsoup.parser.XmlTreeBuilder;

import java.util.List;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class ParserTest {

    private Parser parser;
    private TreeBuilder treeBuilder;

    @Before
    public void setup() {
        treeBuilder = new HtmlTreeBuilder();
        parser = new Parser(treeBuilder);
    }

    @Test
    public void testParseInputValidHtml() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello, World!</h1></body></html>";
        Document doc = parser.parseInput(html, null);

        assertNotNull(doc);
        assertEquals("<html><head><title>Test</title></head><body><h1>Hello, World!</h1></body></html>", doc.html());
    }

    @Test
    public void testParseInputEmptyString() {
        Document doc = parser.parseInput("", null);
        assertNotNull(doc);
        assertEquals("", doc.html());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseInputNullInput() {
        parser.parseInput(null, null);
    }

    @Test
    public void testParseInputInvalidHtml() {
        String invalidHtml = "<html<title>Test</title>";
        List<ParseError> errors = new ArrayList<>();
        Document doc = parser.parseInput(invalidHtml, null, errors, treeBuilder.defaultSettings());

        assertNull(doc);
        assertTrue(errors.size() > 0);
    }

    @Test
    public void testParseInputTrackErrors() {
        Parser parserWithTrackErrors = new Parser(treeBuilder);
        parserWithTrackErrors.setTrackErrors(1);
        String html = "<html><title><script>alert('Hello World')</script></title></html>";
        Document doc = parserWithTrackErrors.parseInput(html, null);

        List<ParseError> errors = parserWithTrackErrors.getErrors();
        assertNotNull(doc);
        assertEquals(1, errors.size());
        assertEquals("Error parsing start tag : unexpected 'S' (can only find unexpected 'T' at this point)", errors.get(0).getMessage());
    }

    @Test
    public void testParseInputNoTrackErrors() {
        Parser parserWithNoTrackErrors = new Parser(treeBuilder);
        String html = "<html><title><script>alert('Hello World')</script></title></html>";
        Document doc = parserWithNoTrackErrors.parseInput(html, null);

        List<ParseError> errors = parserWithNoTrackErrors.getErrors();
        assertNotNull(doc);
        assertNull(errors);
    }

    @Test
    public void testParseInputWithBaseUri() {
        String html = "<img src='image.png'/>";
        Document doc = parser.parseInput(html, "http://example.com");

        Element img = doc.select("img").first();
        String imgSource = img.attr("src");
        assertEquals("image.png", imgSource);
    }

    @Test
    public void testSetTreeBuilder() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        TreeBuilder newTreeBuilder = new XmlTreeBuilder();
        parser.setTreeBuilder(newTreeBuilder);

        Document doc = parser.parseInput("<html><head><title>Test</title></head><body><h1>Hello, World!</h1></body></html>", null);

        assertNotNull(doc);
        assertEquals("xml", newTreeBuilder.factoryName());
    }

    @Test
    public void testParseXmlFragment() {
        String xmlFragment = "<xml><tag attr='value'><subtag/></tag></xml>";
        List<Node> nodes = Parser.parseXmlFragment(xmlFragment, null);

        assertEquals(3, nodes.size());
        Element tag = (Element) nodes.get(0);
        assertEquals("tag", tag.tagName());
        Element subtag = (Element) nodes.get(1);
        assertEquals("subtag", subtag.tagName());
    }
}