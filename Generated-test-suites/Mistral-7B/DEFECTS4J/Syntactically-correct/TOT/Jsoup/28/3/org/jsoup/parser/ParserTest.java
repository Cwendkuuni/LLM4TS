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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Test class for Parser.
 */
public class ParserTest {

    private Parser parser;
    private TreeBuilder treeBuilder;

    @Before
    public void setup() {
        treeBuilder = new HtmlTreeBuilder();
        parser = new Parser(treeBuilder);
    }

    /**
     * Test parseInput method with valid HTML input.
     */
    @Test
    public void testParseInputValidHtml() {
        String html = "<html><head></head><body><h1>Hello World</h1></body></html>";
        Document doc = parser.parseInput(html, "http://example.com");
        assertNotNull(doc);
        assertEquals("<html><head></head><body><h1>Hello World</h1></body></html>", doc.html());
    }

    /**
     * Test parseInput method with invalid HTML input.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testParseInputInvalidHtml() {
        String html = "<html><body>"; // missing head tag
        parser.parseInput(html, "http://example.com");
    }

    /**
     * Test parseInput method with error tracking enabled.
     */
    @Test
    public void testParseInputTrackErrors() {
        Parser parserWithErrors = new Parser(new HtmlTreeBuilder());
        parserWithErrors.setTrackErrors(1);
        String htmlWithError = "<html><body><a href='#'>Link</a></body></html>"; // missing closing tag for body
        Document doc = parserWithErrors.parseInput(htmlWithError, "http://example.com");
        List<ParseError> errors = parserWithErrors.getErrors();
        assertEquals(1, errors.size());
        ParseError error = errors.get(0);
        assertEquals("unclosed tag: body[1]", error.getMessage());
        assertNull(doc);
    }

    /**
     * Test parseInput method with error tracking disabled.
     */
    @Test
    public void testParseInputNoTrackErrors() {
        Parser parserNoErrors = new Parser(new HtmlTreeBuilder());
        parserNoErrors.setTrackErrors(0);
        String htmlWithError = "<html><body><a href='#'>Link</a></body></html>"; // missing closing tag for body
        Document doc = parserNoErrors.parseInput(htmlWithError, "http://example.com");
        List<ParseError> errors = parserNoErrors.getErrors();
        assertEquals(0, errors.size());
        assertNotNull(doc);
    }

    /**
     * Test setTreeBuilder method.
     */
    @Test
    public void testSetTreeBuilder() {
        TreeBuilder htmlTreeBuilder = new HtmlTreeBuilder();
        Parser parserHtml = new Parser(htmlTreeBuilder);
        TreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Parser parserXml = new Parser(xmlTreeBuilder);

        parserHtml.setTreeBuilder(xmlTreeBuilder);
        assertTrue(parserHtml.getTreeBuilder() instanceof XmlTreeBuilder);

        parserXml.setTreeBuilder(htmlTreeBuilder);
        assertTrue(parserXml.getTreeBuilder() instanceof HtmlTreeBuilder);
    }

    /**
     * Test isTrackErrors method.
     */
    @Test
    public void testIsTrackErrors() {
        Parser parserWithErrors = new Parser(new HtmlTreeBuilder());
        parserWithErrors.setTrackErrors(1);
        assertTrue(parserWithErrors.isTrackErrors());

        Parser parserNoErrors = new Parser(new HtmlTreeBuilder());
        parserNoErrors.setTrackErrors(0);
        assertFalse(parserNoErrors.isTrackErrors());
    }

    /**
     * Test parse static methods.
     */
    @Test
    public void testParseStaticMethods() {
        String html = "<html><head></head><body><h1>Hello World</h1></body></html>";
        Document doc = Parser.parse(html, "http://example.com");
        assertNotNull(doc);
        assertEquals("<html><head></head><body><h1>Hello World</h1></body></html>", doc.html());

        Element context = new Element("div");
        List<Node> nodes = Parser.parseFragment("<ul><li>List Item 1</li><li>List Item 2</li></ul>", context, "http://example.com");
        Node[] nodeArray = nodes.toArray(new Node[nodes.size()]);
        assertEquals(2, nodes.size());
        for (Node node : nodeArray) {
            assertTrue(node instanceof Element);
            Element element = (Element) node;
            assertEquals("li", element.tagName());
        }

        Document docBody = Parser.parseBodyFragment("<p>This is a body fragment.</p>", "http://example.com");
        assertNotNull(docBody);
        assertEquals("<html><head></head><body><p>This is a body fragment.</p></body></html>", docBody.html());
    }
}