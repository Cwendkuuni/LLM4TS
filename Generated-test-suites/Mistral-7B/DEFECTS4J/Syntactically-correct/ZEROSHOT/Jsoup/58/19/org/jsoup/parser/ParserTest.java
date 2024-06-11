package org.jsoup.parser;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.ParseError;
import org.jsoup.parser.TreeBuilder;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * ParserTest class for testing Parser class methods
 */
public class ParserTest {

    private Parser parser;
    private TreeBuilder treeBuilder;

    @Before
    public void setUp() {
        treeBuilder = new HtmlTreeBuilder();
        parser = new Parser(treeBuilder);
    }

    @Test
    public void testParseInput() {
        String html = "<html><body><h1>Hello</h1></body></html>";
        String baseUri = "http://example.com";
        Document document = parser.parseInput(html, baseUri);

        assertNotNull(document);
        assertEquals("<html><head></head><body><h1>Hello</h1></body></html>", document.html());

        Element bodyElement = document.body();
        Element h1Element = bodyElement.child(0);
        assertEquals("Hello", h1Element.text());
    }

    @Test
    public void testParseInputWithTrackErrors() {
        String html = "<html><body><h1>Hello</h1><script>alert('Hello World')</script></body></html>";
        String baseUri = "http://example.com";
        parser.setTrackErrors(1);

        Document document = parser.parseInput(html, baseUri);

        assertNotNull(document);
        assertEquals("<html><head></head><body><h1>Hello</h1></body></html>", document.html());

        List<ParseError> errors = parser.getErrors();
        assertNotNull(errors);
        assertEquals(1, errors.size());
        ParseError error = errors.get(0);
        assertEquals("Unexpected token SCRIPT_TAG opening script or style tag", error.getMessage());
        assertEquals(1, error.getLine());
        assertEquals(4, error.getColumn());

        // check if parse error tracking is disabled
        parser.setTrackErrors(0);
        document = parser.parseInput(html, baseUri);
        errors = parser.getErrors();
        assertNull(errors);
    }

    @Test
    public void testParseInputWithEmptyInput() {
        String html = "";
        String baseUri = "http://example.com";
        Document document = parser.parseInput(html, baseUri);

        assertNotNull(document);
        assertEquals("", document.html());
    }

    @Test
    public void testParseInputWithNullInput() {
        String baseUri = "http://example.com";
        parser.setTreeBuilder(new NullTreeBuilder());

        Document document = parser.parseInput(null, baseUri);

        assertNull(document);
    }

    @Test
    public void testGetTreeBuilder() {
        TreeBuilder treeBuilder = parser.getTreeBuilder();
        assertEquals(treeBuilder, parser.getTreeBuilder());
    }

    @Test
    public void testSetTreeBuilder() {
        TreeBuilder newTreeBuilder = new HtmlTreeBuilder();
        parser.setTreeBuilder(newTreeBuilder);
        assertEquals(newTreeBuilder, parser.getTreeBuilder());
    }

    @Test
    public void testIsTrackErrors() {
        assertFalse(parser.isTrackErrors());
        parser.setTrackErrors(1);
        assertTrue(parser.isTrackErrors());
        parser.setTrackErrors(0);
        assertFalse(parser.isTrackErrors());
    }

    @Test
    public void testSetTrackErrorsWithNegativeNumber() {
        parser.setTrackErrors(-1);
        assertEquals(0, parser.getMaxErrors());
    }

    @Test
    public void testSetTrackErrorsWithZero() {
        parser.setTrackErrors(0);
        assertFalse(parser.isTrackErrors());
    }

    @Test
    public void testGetErrors() {
        String html = "<html><body><h1>Hello</h1><script>alert('Hello World')</script></body></html>";
        String baseUri = "http://example.com";
        parser.setTrackErrors(1);

        Document document = parser.parseInput(html, baseUri);

        List<ParseError> errors = parser.getErrors();
        assertNotNull(errors);
        assertEquals(1, errors.size());

        assertNull(parser.getErrors());
    }

    @Test
    public void testSettings() {
        ParseSettings settings = new ParseSettings();
        parser.settings(settings);
        assertEquals(settings, parser.settings());
    }

    @Test
    public void testStaticParseMethod() {
        String html = "<html><body><h1>Hello</h1></body></html>";
        String baseUri = "http://example.com";

        Document document = Parser.parse(html, baseUri);

        assertNotNull(document);
        assertEquals("<html><head></head><body><h1>Hello</h1></body></html>", document.html());
    }

    @Test
    public void testStaticParseFragment() {
        String html = "<div>Hello World</div>";
        Element context = new Element("html");
        String baseUri = "http://example.com";

        List<Node> nodes = Parser.parseFragment(html, context, baseUri);

        assertNotNull(nodes);
        assertEquals(1, nodes.size());
        Node node = nodes.get(0);
        assertInstanceOf(Element.class, node);
        Element divElement = (Element) node;
        assertEquals("Hello World", divElement.text());
    }

    @Test
    public void testStaticParseXmlFragment() {
        String xml = "<person><name>John</name></person>";

        List<Node> nodes = Parser.parseXmlFragment(xml, "");

        assertNotNull(nodes);
        assertEquals(1, nodes.size());
        Node node = nodes.get(0);
        assertInstanceOf(Element.class, node);
        Element personElement = (Element) node;
        assertEquals("person", personElement.tagName());

        List<Element> children = personElement.children();
        assertEquals(1, children.size());
        Element nameElement = children.get(0);
        assertEquals("name", nameElement.tagName());
        assertEquals("John", nameElement.text());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<div>Hello World</div>";
        String baseUri = "http://example.com";

        Document document = Parser.parseBodyFragment(bodyHtml, baseUri);

        assertNotNull(document);
        assertEquals("<html><head></head><body><div>Hello World</div></body></html>", document.html());
    }

    @Test
    public void testUnescapeEntities() {
        String html = "&lt;html&gt;&lt;head&gt;&lt;title&gt;Test&amp;lt;amp;&gt;</title&gt;&lt;/head&gt;&lt;body&gt;&lt;p&gt;Hello &amp; World&lt;/p&gt;&lt;/body&gt;&lt;/html&gt;";

        String unescapedHtml = Parser.unescapeEntities(html, false);

        assertEquals("<html><head><title>Test&lt;amp;&gt;</title></head><body><p>Hello &amp; World</p></body></html>", unescapedHtml);
    }

    @Test(expected = NullPointerException.class)
    public void testUnescapeEntitiesWithNullInput() {
        Parser.unescapeEntities(null, false);
    }

    private static class NullTreeBuilder implements TreeBuilder {
        @Override
        public Document parse(String html, String baseUri, ParseErrorList errorList, ParseSettings settings) {
            return null;
        }

        @Override
        public List<Node> parseFragment(String fragmentHtml, Element context, String baseUri, ParseErrorList errorList, ParseSettings settings) {
            return new ArrayList<>();
        }

        @Override
        public ParseSettings defaultSettings() {
            return null;
        }
    }
}