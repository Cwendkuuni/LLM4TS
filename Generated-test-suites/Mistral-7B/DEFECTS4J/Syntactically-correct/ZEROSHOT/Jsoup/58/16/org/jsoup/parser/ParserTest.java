package org.jsoup.parser;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.ParseError;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.TreeBuilder;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Test class for Parser class.
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
     * Test constructor.
     */
    @Test
    public void testParserConstructor() {
        assertNotNull(parser);
    }

    /**
     * Test parseInput method with empty html and valid baseUri.
     */
    @Test
    public void testParseInputEmptyHtml() {
        Document document = parser.parseInput("", "http://example.com");
        assertNotNull(document);
        assertEquals(0, document.numberOfChildren());
    }

    /**
     * Test parseInput method with invalid html and valid baseUri.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testParseInputInvalidHtml() {
        parser.parseInput("&%$@!", "http://example.com");
    }

    /**
     * Test parseInput method with valid html and valid baseUri.
     */
    @Test
    public void testParseInputValidHtml() {
        String html = "<html><head><title>Test</title></head><body><p>Hello World</p></body></html>";
        Document document = parser.parseInput(html, "http://example.com");

        assertNotNull(document);
        assertNotNull(document.title());
        assertEquals("Test", document.title().text());
        assertEquals(1, document.numberOfChildren());

        Element pElement = (Element) document.body().child(0);
        assertNotNull(pElement);
        assertEquals("p", pElement.tagName());
        assertEquals("Hello World", pElement.text());

        List<ParseError> errors = parser.getErrors();
        assertNull(errors);
    }

    /**
     * Test parseInput method with valid html and valid baseUri with errors tracking enabled.
     */
    @Test
    public void testParseInputValidHtmlErrorsTracking() {
        parser.setTrackErrors(5);
        String html = "<html><head><title>Test</title></head><body><p>Hello World</p></body></html>";
        Document document = parser.parseInput(html, "http://example.com");

        assertNotNull(document);
        assertNotNull(document.title());
        assertEquals("Test", document.title().text());
        assertEquals(1, document.numberOfChildren());

        Element pElement = (Element) document.body().child(0);
        assertNotNull(pElement);
        assertEquals("p", pElement.tagName());
        assertEquals("Hello World", pElement.text());

        List<ParseError> errors = parser.getErrors();
        assertNotNull(errors);
        assertEquals(0, errors.size());

        parser.setTrackErrors(1);
        Document documentWithError = new HtmlTreeBuilder().parse("<html><head><title>Test</title></head><body><p>Hello World</p><p>Error</p></body></html>", "http://example.com");
        parser.parseInput(documentWithError.html(), "http://example.com");

        errors = parser.getErrors();
        assertNotNull(errors);
        assertEquals(1, errors.size());
        ParseError error = errors.get(0);
        assertEquals("Error parsing start tag : p", error.getMessage());
    }

    /**
     * Test getTreeBuilder method.
     */
    @Test
    public void testGetTreeBuilder() {
        assertSame(treeBuilder, parser.getTreeBuilder());
    }

    /**
     * Test setTreeBuilder method.
     */
    @Test
    public void testSetTreeBuilder() {
        TreeBuilder newTreeBuilder = new XmlTreeBuilder();
        parser.setTreeBuilder(newTreeBuilder);
        assertSame(newTreeBuilder, parser.getTreeBuilder());
    }

    /**
     * Test isTrackErrors method.
     */
    @Test
    public void testIsTrackErrors() {
        assertFalse(parser.isTrackErrors());

        parser.setTrackErrors(1);
        assertTrue(parser.isTrackErrors());

        parser.setTrackErrors(0);
        assertFalse(parser.isTrackErrors());
    }

    /**
     * Test setTrackErrors method.
     */
    @Test
    public void testSetTrackErrors() {
        parser.setTrackErrors(5);
        assertEquals(5, parser.getMaxErrors());

        parser.setTrackErrors(0);
        assertEquals(0, parser.getMaxErrors());
    }

    /**
     * Test getErrors method.
     */
    @Test
    public void testGetErrors() {
        List<ParseError> errors = new ArrayList<>();
        parser.setTreeBuilder(new ErrorTreeBuilder(errors));
        parser.setTrackErrors(1);
        Document documentWithError = new HtmlTreeBuilder().parse("<html><head><title>Test</title></head><body><p>Hello World</p><p>Error</p></body></html>", "http://example.com");
        parser.parseInput(documentWithError.html(), "http://example.com");

        errors = parser.getErrors();
        assertEquals(1, errors.size());
        ParseError error = errors.get(0);
        assertEquals("Error parsing start tag : p", error.getMessage());

        assertNull(parser.getErrors());
    }

    /**
     * Test parse method.
     */
    @Test
    public void testParse() {
        String html = "<html><head><title>Test</title></head><body><p>Hello World</p></body></html>";
        Document document = Parser.parse(html, "http://example.com");

        assertNotNull(document);
        assertNotNull(document.title());
        assertEquals("Test", document.title().text());
        assertEquals(1, document.numberOfChildren());

        Element pElement = (Element) document.body().child(0);
        assertNotNull(pElement);
        assertEquals("p", pElement.tagName());
        assertEquals("Hello World", pElement.text());

        List<ParseError> errors = Parser.getErrors();
        assertNull(errors);
    }

    /**
     * Test parseFragment method.
     */
    @Test
    public void testParseFragment() {
        String fragmentHtml = "<p>Hello World</p>";
        Element context = new Element("div");
        Document document = parser.parseInput("<html><body>" + fragmentHtml + "</body></html>", "http://example.com");
        List<Node> nodes = Parser.parseFragment(fragmentHtml, context, "http://example.com");

        assertNotNull(nodes);
        assertEquals(1, nodes.size());
        Element node = (Element) nodes.get(0);
        assertNotNull(node);
        assertEquals("p", node.tagName());
        assertEquals("Hello World", node.text());
        assertEquals(context, node.parent());
    }

    /**
     * Test parseXmlFragment method.
     */
    @Test
    public void testParseXmlFragment() {
        String fragmentXml = "<xml><item>Hello World</item></xml>";
        Document document = parser.parseInput("<html><body>" + fragmentXml + "</body></html>", "http://example.com");
        List<Node> nodes = Parser.parseXmlFragment(fragmentXml, "http://example.com");

        assertNotNull(nodes);
        assertEquals(1, nodes.size());
        Element node = (Element) nodes.get(0);
        assertNotNull(node);
        assertEquals("item", node.tagName());
        assertEquals("Hello World", node.text());
    }

    /**
     * Test parseBodyFragment method.
     */
    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<p>Hello World</p>";
        Document document = Parser.parseBodyFragment(bodyHtml, "http://example.com");

        assertNotNull(document);
        assertNotNull(document.title());
        assertEquals("", document.title().text());
        assertEquals(1, document.numberOfChildren());

        Element pElement = (Element) document.body().child(0);
        assertNotNull(pElement);
        assertEquals("p", pElement.tagName());
        assertEquals("Hello World", pElement.text());
    }

    /**
     * Test unescapeEntities method.
     */
    @Test
    public void testUnescapeEntities() {
        String html = "<html>&lt;p&gt;Hello &amp; World&lt;/p&gt;";
        String unescapedHtml = Parser.unescapeEntities(html, false);

        assertEquals("<html><p>Hello & World</p></html>", unescapedHtml);
    }
}

class ErrorTreeBuilder implements TreeBuilder {
    private List<ParseError> errors;

    public ErrorTreeBuilder(List<ParseError> errors) {
        this.errors = errors;
    }

    @Override
    public Document parse(String html, String baseUri, ParseErrorList errorList, ParseSettings settings) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Element parse(String tagName, AttributeSet attributes, String baseUri) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Element parseBodyFragment(String bodyHtml, String baseUri) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Node> parseFragment(String fragmentHtml, Element context, String baseUri, ParseErrorList errorList, ParseSettings settings) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Node> parseXmlFragment(String fragmentXml, String baseUri, ParseErrorList errorList, ParseSettings settings) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Document parseUrl(String url, String baseUri, ParseErrorList errorList, ParseSettings settings) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Element parseTagName(String tagName, int begin, int end, String baseUri, ParseErrorList errorList, ParseSettings settings) {
        throw new UnsupportedOperationException();
    }

    @Override
    public AttributeList parseTagAttribute(String tagName, int begin, int end, String baseUri, ParseErrorList errorList, ParseSettings settings) {
        throw new UnsupportedOperationException();
    }

    @Override
    public AttributeList parseTagAttributes(String tagName, int begin, int end, String baseUri, ParseErrorList errorList, ParseSettings settings) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Element parseTagContent(int begin, int end, String baseUri, ParseErrorList errorList, ParseSettings settings) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Node> parseText(int begin, int end, ParseErrorList errorList, ParseSettings settings) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Node> parseScript(String script, int begin, int end, String baseUri, ParseErrorList errorList, ParseSettings settings) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Node> parseStyle(String css, int begin, int end, String baseUri, ParseErrorList errorList, ParseSettings settings) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Node> parseComment(int begin, int end, ParseErrorList errorList, ParseSettings settings) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Node> parseRaw(int begin, int end, ParseErrorList errorList, ParseSettings settings) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Document create(String baseUri, String doctype, List<Node> children) {
        Document document = new Document();
        document.setBaseUri(baseUri);
        document.appendChild(children.get(0));
        return document;
    }

    @Override
    public Element createElement(String tagName, AttributeSet attrs) {
        Element element = new Element(tagName);
        element.attributes().putAll(attrs);
        return element;
    }

    @Override
    public Element createElement(String tagName) {
        return new Element(tagName);
    }

    @Override
    public Element createHead() {
        return new Element("head");
    }

    @Override
    public Element createBody() {
        return new Element("body");
    }

    @Override
    public Element createTextNode(String text) {
        return new TextNode(text);
    }

    @Override
    public Element createComment(String comment) {
        return new Comment(comment);
    }

    @Override
    public void addError(ParseError error) {
        errors.add(error);
    }
}