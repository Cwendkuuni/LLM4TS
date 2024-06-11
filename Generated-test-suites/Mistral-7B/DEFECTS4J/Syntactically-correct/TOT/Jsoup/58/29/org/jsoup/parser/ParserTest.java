package org.jsoup.parser;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.ParseError;
import org.jsoup.parser.Parser;
import org.jsoup.parser.TreeBuilder;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.ParseSettings;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class ParserTest {

    private Parser parser;
    private TreeBuilder treeBuilder;
    private ParseSettings settings;

    @Before
    public void setup() {
        treeBuilder = new HtmlTreeBuilder();
        parser = new Parser(treeBuilder);
        settings = treeBuilder.defaultSettings();
    }

    @Test
    public void testParseInput_ValidHtmlWithBaseUri_ReturnsDocument() {
        String html = "<html><head><title>Test</title></head><body><p>Hello, World!</p></body></html>";
        String baseUri = "http://example.com";

        Document document = parser.parseInput(html, baseUri);

        assertNotNull(document);
        assertEquals("Test", document.title());
        assertEquals("Hello, World!", document.body().text());
    }

    @Test
    public void testParseInput_ValidHtmlWithoutBaseUri_ReturnsDocument() {
        String html = "<html><head><title>Test</title></head><body><p>Hello, World!</p></body></html>";

        Document document = parser.parseInput(html);

        assertNotNull(document);
        assertEquals("", document.baseUri());
        assertEquals("Test", document.title().text());
        assertEquals("Hello, World!", document.body().text());
    }

    @Test
    public void testParseInput_HtmlWithErrors_ReturnsDocumentWithErrors() {
        String html = "<html><head><title>Test</title></head><body><p>Hello, <script>World!</script></p></body></html>";
        String baseUri = "http://example.com";

        ParseErrorList errors = ParseErrorList.tracking(1);
        Document document = parser.parseInput(html, baseUri, errors, settings);

        assertNotNull(document);
        assertEquals(1, errors.size());
        ParseError error = errors.get(0);
        assertEquals("Error parsing start tag <script>", error.getMessage());
        assertEquals(11, error.getLine());
        assertEquals(16, error.getColumn());
    }

    @Test
    public void testParseInput_HtmlWithMaxErrorsReached_ThrowsException() {
        String html = "<html><head><title>Test</title></head><body>";
        String baseUri = "http://example.com";

        ParseErrorList errors = ParseErrorList.tracking(0);
        parser.setTrackErrors(1);

        try {
            parser.parseInput(html, baseUri, errors, settings);
            fail("Expected ParseException");
        } catch (ParseException e) {
            // expected
        }

        assertEquals(1, errors.size());
        ParseError error = errors.get(0);
        assertEquals("Error parsing start tag <body>", error.getMessage());
        assertEquals(5, error.getLine());
        assertEquals(9, error.getColumn());
    }

    @Test
    public void testParseInput_HtmlWithDisabledErrorTracking_ReturnsDocument() {
        String html = "<html><head><title>Test</title></head><body><p>Hello, World!</p></body></html>";

        ParseErrorList errors = ParseErrorList.noTracking();
        Document document = parser.parseInput(html, "", errors, settings);

        assertNotNull(document);
        assertNull(errors);
    }

    @Test
    public void testParseInput_MalformedHtml_ThrowsException() {
        String html = "<html><head><title>Test</title>";

        try {
            parser.parseInput(html, "");
            fail("Expected ParseException");
        } catch (ParseException e) {
            // expected
        }
    }

    @Test
    public void testParseInput_EmptyString_ReturnsEmptyDocument() {
        Document document = parser.parseInput("");
        assertEquals(0, document.body().childNodes().size());
    }

    @Test
    public void testParseInput_NullInput_ThrowsException() {
        try {
            parser.parseInput(null, "");
            fail("Expected ParseException");
        } catch (ParseException e) {
            // expected
        }
    }

    @Test
    public void testGetTreeBuilder_ReturnsTreeBuilder() {
        TreeBuilder treeBuilder = parser.getTreeBuilder();
        assertEquals(treeBuilder, parser.getTreeBuilder());
    }

    @Test
    public void testSetTreeBuilder_SetsTreeBuilder() {
        TreeBuilder newTreeBuilder = new HtmlTreeBuilder();
        parser.setTreeBuilder(newTreeBuilder);
        assertEquals(newTreeBuilder, parser.getTreeBuilder());
    }

    @Test
    public void testIsTrackErrors_EnablesErrorTracking_ReturnsTrue() {
        parser.setTrackErrors(1);
        assertTrue(parser.isTrackErrors());
    }

    @Test
    public void testIsTrackErrors_DisablesErrorTracking_ReturnsFalse() {
        parser.setTrackErrors(0);
        assertFalse(parser.isTrackErrors());
    }

    @Test
    public void testGetErrors_ReturnsErrors() {
        String html = "<html><head><title>Test</title></head><body><p>Hello, <script>World!</script></p></body></html>";
        String baseUri = "http://example.com";

        ParseErrorList errors = ParseErrorList.tracking(1);
        Document document = parser.parseInput(html, baseUri, errors, settings);

        List<ParseError> parseErrors = parser.getErrors();
        assertEquals(1, parseErrors.size());
        ParseError error = parseErrors.get(0);
        assertEquals("Error parsing start tag <script>", error.getMessage());
        assertEquals(11, error.getLine());
        assertEquals(16, error.getColumn());
    }

    @Test
    public void testGetErrors_NoErrors_ReturnsEmptyList() {
        Document document = parser.parseInput("<html><body></body></html>", "");
        List<ParseError> parseErrors = parser.getErrors();
        assertEquals(0, parseErrors.size());
    }

    @Test
    public void testSettings_SetsSettings() {
        ParseSettings newSettings = new ParseSettings();
        parser.settings(newSettings);
        assertEquals(newSettings, parser.settings());
    }

    @Test
    public void testSettings_GetsSettings() {
        ParseSettings settings = parser.settings();
        assertEquals(settings, parser.settings());
    }

    @Test
    public void testStaticParse_ValidHtml_ReturnsDocument() {
        String html = "<html><head><title>Test</title></head><body><p>Hello, World!</p></body></html>";
        String baseUri = "http://example.com";

        Document document = Parser.parse(html, baseUri);
        assertNotNull(document);
        assertEquals("Test", document.title().text());
        assertEquals("Hello, World!", document.body().text());
    }

    @Test
    public void testStaticParse_ValidXml_ReturnsDocument() {
        String xml = "<root><element>Hello, World!</element></root>";

        Document document = Parser.parseXmlFragment(xml, "http://example.com");
        assertNotNull(document);
        assertEquals("<root><element>Hello, World!</element></root>", document.outerHtml());
    }

    @Test
    public void testStaticParseFragment_ValidHtml_ReturnsListOfNodes() {
        String html = "<div><span>Hello</span><p>World!</p></div>";

        List<Node> nodes = Parser.parseFragment(html, null, "http://example.com");
        Node[] nodeArray = nodes.toArray(new Node[nodes.size()]);

        assertEquals(3, nodes.size());
        assertEquals("div", nodeArray[0].tagName());
        assertEquals("span", nodeArray[1].tagName());
        assertEquals("p", nodeArray[2].tagName());
    }

    @Test
    public void testStaticParseBodyFragment_ValidHtml_ReturnsDocument() {
        String html = "<body><p>Hello, World!</p></body>";
        String baseUri = "http://example.com";

        Document document = Parser.parseBodyFragment(html, baseUri);

        assertEquals("<html><head></head><body><p>Hello, World!</p></body></html>", document.outerHtml());
    }

    @Test
    public void testUnescapeEntities_ValidEntities_ReturnsUnescapedString() {
        String escapedString = "&lt;Hello&gt; &amp; World! &quot;With Quotes&quot;";
        String unescapedString = parser.unescapeEntities(escapedString, false);

        assertEquals("<Hello> & World! \"With Quotes\"", unescapedString);
    }

    @Test
    public void testUnescapeEntities_EntitiesInAttributes_ReturnsUnescapedString() {
        String escapedString = "<a href=\"&lt;http://example.com&gt;\">Link</a>";
        String unescapedString = parser.unescapeEntities(escapedString, true);

        assertEquals("<a href=\"<http://example.com>\">Link</a>", unescapedString);
    }
}