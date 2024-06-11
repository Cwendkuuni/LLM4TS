package org.jsoup.parser;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.ParseError;
import org.jsoup.nodes.TreeBuilder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

@RunWith(MockitoJUnitRunner.class)
public class ParserTest {

    @InjectMocks
    private Parser parser;
    @InjectMocks
    private TreeBuilder treeBuilder;
    private ParseErrorList errors;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        errors = Mockito.mock(ParseErrorList.class);
        parser = new Parser(treeBuilder);
    }

    @Test
    public void testParseInput() {
        String html = "<html><body><h1>Test</h1></body></html>";
        String baseUri = "https://example.com";

        Document doc = parser.parseInput(html, baseUri);
        assertNotNull(doc);
        assertSame(treeBuilder, parser.getTreeBuilder());
        assertNull(parser.getErrors());
        Element rootElement = doc.rootElement();
        assertEquals("html", rootElement.tagName());
        Element bodyElement = doc.body();
        assertEquals("h1", bodyElement.tagName());
        assertEquals("Test", bodyElement.text());
    }

    @Test
    public void testParseInputWithTrackErrors() {
        String html = "<html><body><h1>Test</h1><script>alert('Hello')</script></body></html>";
        String baseUri = "https://example.com";

        parser.setTrackErrors(1);
        Document doc = parser.parseInput(html, baseUri);
        assertNotNull(doc);
        List<ParseError> errorsList = parser.getErrors();
        assertNotNull(errorsList);
        assertEquals(1, errorsList.size());
        ParseError error = errorsList.get(0);
        assertEquals("Error parsing start tag : script", error.getMessage());
    }

    @Test
    public void testParseInputWithMaxErrors() {
        String html = "<html><body><h1>Test</h1><script>alert('Hello')</script><script>alert('World')</script></body></html>";
        String baseUri = "https://example.com";

        parser.setTrackErrors(2);
        Document doc = parser.parseInput(html, baseUri);
        assertNotNull(doc);
        List<ParseError> errorsList = parser.getErrors();
        assertNotNull(errorsList);
        assertEquals(2, errorsList.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseInputWithNullHtml() {
        String baseUri = "https://example.com";
        parser.parseInput(null, baseUri);
    }

    @Test
    public void testParseInputWithEmptyHtml() {
        String html = "";
        String baseUri = "https://example.com";

        Document doc = parser.parseInput(html, baseUri);
        assertNotNull(doc);
        assertNull(parser.getErrors());
        assertEquals(0, doc.childNodeSize());
    }

    @Test
    public void testParseInputWithBaseUri() {
        String html = "<img src=\"image.jpg\" />";
        String baseUri = "https://example.com/images";

        Document doc = Parser.parse(html, baseUri);
        Element imgElement = doc.select("img").first();
        assertEquals("image.jpg", imgElement.attr("src"));
        assertEquals("https://example.com/images/image.jpg", imgElement.absUrl("src"));
    }

    @Test
    public void testParseFragment() {
        String fragmentHtml = "<div><p>Test</p></div>";
        Element context = Mockito.mock(Element.class);
        String baseUri = "https://example.com";

        List<Node> nodes = Parser.parseFragment(fragmentHtml, context, baseUri);
        assertNotNull(nodes);
        assertEquals(2, nodes.size());
        Node divNode = nodes.get(0);
        assertEquals("DIV", divNode.tagName());
        Element pElement = (Element) nodes.get(1);
        assertEquals("P", pElement.tagName());
        assertEquals("Test", pElement.text());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<p>Test</p>";
        String baseUri = "https://example.com";

        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);
        assertNotNull(doc);
        assertNull(doc.head());
        Element bodyElement = doc.body();
        assertEquals("P", bodyElement.tagName());
        assertEquals("Test", bodyElement.text());
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        String bodyHtml = "<p>Test</p>";
        String baseUri = "https://example.com";

        Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);
        assertNotNull(doc);
        assertNull(doc.head());
        Element bodyElement = doc.body();
        assertEquals("P", bodyElement.tagName());
        assertEquals("Test", bodyElement.text());
    }
}