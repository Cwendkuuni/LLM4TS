package org.jsoup.parser;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.ParseError;
import org.jsoup.nodes.TreeBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

/**
 * Tests for {@link Parser} class.
 */
public class ParserTest {

    private Parser parser;
    private TreeBuilder treeBuilder;
    private Document document;
    private List<ParseError> errors;

    @Before
    public void setUp() {
        treeBuilder = new HtmlTreeBuilder();
        parser = new Parser(treeBuilder);
    }

    @After
    public void tearDown() {
        parser = null;
        treeBuilder = null;
    }

    @Test
    public void testParseInput() {
        String html = "<html><body><p>Hello World</p></body></html>";
        String baseUri = "http://example.com";
        document = parser.parseInput(html, baseUri);

        assertNotNull(document);
        assertEquals("<html><body><p>Hello World</p></body></html>", document.html());

        Element body = document.body();
        Element p = body.child(0);
        assertEquals("Hello World", p.text());

        errors = parser.getErrors();
        assertNull(errors);
    }

    @Test
    public void testParseInputWithErrors() {
        String html = "<html><body><script>alert('Hello')</script></body></html>";
        String baseUri = "http://example.com";
        parser = new Parser(treeBuilder).setTrackErrors(1);
        document = parser.parseInput(html, baseUri);

        assertNotNull(document);
        assertEquals("<html><body></body></html>", document.html());

        errors = parser.getErrors();
        assertNotNull(errors);
        assertEquals(1, errors.size());
        ParseError error = errors.get(0);
        assertEquals(ErrorType.SCRIPT_IN_HEAD, error.getType());
        assertEquals(html, error.getInput());
        assertEquals("script", error.getTag());
        assertEquals(1, error.getLine());
        assertEquals(1, error.getColumn());
    }

    @Test
    public void testGetTreeBuilder() {
        TreeBuilder expectedTreeBuilder = new HtmlTreeBuilder();
        TreeBuilder actualTreeBuilder = parser.getTreeBuilder();

        assertSame(expectedTreeBuilder, actualTreeBuilder);
    }

    @Test
    public void testSetTreeBuilder() {
        TreeBuilder newTreeBuilder = new XmlTreeBuilder();
        parser.setTreeBuilder(newTreeBuilder);
        TreeBuilder actualTreeBuilder = parser.getTreeBuilder();

        assertSame(newTreeBuilder, actualTreeBuilder);
    }

    @Test
    public void testIsTrackErrors() {
        assertFalse(parser.isTrackErrors());

        parser = parser.setTrackErrors(1);
        assertTrue(parser.isTrackErrors());
    }

    @Test
    public void testSetTrackErrorsAndGetErrors() {
        String html = "<html><body><script>alert('Hello')</script></body></html>";
        String baseUri = "http://example.com";
        parser = new Parser(treeBuilder).setTrackErrors(1);
        document = parser.parseInput(html, baseUri);

        List<ParseError> errors = parser.getErrors();
        assertEquals(1, errors.size());

        parser.setTrackErrors(0);
        errors = parser.getErrors();
        assertNull(errors);
    }

    @Test
    public void testParseStaticMethods() {
        String html = "<html><body><p>Hello World</p></body></html>";
        String baseUri = "http://example.com";

        Document document = Parser.parse(html, baseUri);
        assertEquals("<html><body><p>Hello World</p></body></html>", document.html());

        List<Node> nodes = Parser.parseFragment("<p>Hello</p>", null, baseUri);
        assertEquals(1, nodes.size());
        Element p = (Element) nodes.get(0);
        assertEquals("Hello", p.text());
    }
}