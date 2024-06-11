package org.jsoup.parser;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Token;
import org.jsoup.nodes.Token.StartTag;
import org.jsoup.nodes.Token.EndTag;
import org.jsoup.parser.ParseErrorList;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;

/**
 * Test class for TreeBuilder
 *
 * @author John Doe
 */
public class TreeBuilderTest {

    private TreeBuilder treeBuilder;
    private Document doc;
    private ParseErrorList errors;

    @Before
    public void setUp() {
        treeBuilder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                return false;
            }
        };
        doc = null;
        errors = new ParseErrorList();
    }

    @After
    public void tearDown() {
        if (doc != null) {
            doc.detach();
            doc = null;
        }
    }

    @Test
    public void testInitialiseParse() {
        String input = "<html><head><title>Test</title></head><body><h1>Hello</h1></body></html>";
        String baseUri = "https://example.com";

        treeBuilder.initialiseParse(input, baseUri, errors);

        Validate.notNull(treeBuilder.doc, "Document should not be null");
        Validate.notNull(treeBuilder.tokeniser, "Tokeniser should not be null");
        Validate.notNull(treeBuilder.errors, "Error list should not be null");
        assertSame(baseUri, treeBuilder.baseUri, "Base URI should be set correctly");
        assertNull(treeBuilder.currentToken, "Current token should be null after initialisation");
        assertNull(doc, "Document should be null before parsing");
    }

    @Test(expected = NullPointerException.class)
    public void testInitialiseParseWithNullInput() {
        String baseUri = "https://example.com";
        treeBuilder.initialiseParse(null, baseUri, errors);
    }

    @Test(expected = NullPointerException.class)
    public void testInitialiseParseWithNullBaseUri() {
        String input = "<html><head><title>Test</title></head><body><h1>Hello</h1></body></html>";
        treeBuilder.initialiseParse(input, null, errors);
    }

    @Test(expected = NullPointerException.class)
    public void testInitialiseParseWithNullErrors() {
        String input = "<html><head><title>Test</title></head><body><h1>Hello</h1></body></html>";
        String baseUri = "https://example.com";
        treeBuilder.initialiseParse(input, baseUri, null);
    }

    @Test
    public void testParse() {
        String input = "<html><head><title>Test</title></head><body><h1>Hello</h1></body></html>";
        String baseUri = "https://example.com";

        doc = treeBuilder.parse(input, baseUri);

        Validate.notNull(doc, "Document should not be null after parsing");
        Validate.notEmpty(doc.body().children(), "Document body should not be empty");
        assertEquals("<html><head><title>Test</title></head><body><h1>Hello</h1></body></html>", doc.toString(), "Document should be parsed correctly");
    }

    @Test(expected = NullPointerException.class)
    public void testParseWithNullInput() {
        String baseUri = "https://example.com";
        treeBuilder.parse(null, baseUri);
    }

    @Test(expected = NullPointerException.class)
    public void testParseWithNullBaseUri() {
        String input = "<html><head><title>Test</title></head><body><h1>Hello</h1></body></html>";
        treeBuilder.parse(input, null);
    }

    @Test
    public void testParseWithErrors() {
        String input = "<html><head><title>Test<script>alert('Hello')</script></title></head><body><h1>Hello</h1></body></html>";
        String baseUri = "https://example.com";

        doc = treeBuilder.parse(input, baseUri, errors);

        Validate.notNull(doc, "Document should not be null after parsing");
        Validate.notEmpty(doc.body().children(), "Document body should not be empty");
        assertEquals("<html><head><title>Test</title></head><body><h1>Hello</h1></body></html>", doc.toString(), "Document should be parsed correctly without script tag");
        assertEquals(1, errors.size(), "Error list should contain one error");
        assertEquals("Error parsing start tag : script", errors.get(0).getMessage(), "Error message should be correct");
    }

    @Test
    public void testProcessStartTag() {
        treeBuilder.doc = new Document("https://example.com");
        treeBuilder.stack.clear();
        treeBuilder.currentToken = new Token.StartTag("html");

        boolean result = treeBuilder.processStartTag("html");
        assertTrue(result, "processStartTag should return true for valid start tag");

        Element html = treeBuilder.currentElement();
        assertSame(html.tagName(), "html", "Element tag name should be set correctly");
        assertSame(html.baseUri(), "https://example.com", "Element base URI should be set correctly");
    }

    @Test
    public void testProcessStartTagWithAttributes() {
        treeBuilder.doc = new Document("https://example.com");
        treeBuilder.stack.clear();
        treeBuilder.currentToken = new Token.StartTag("a", new Attributes().put("href", "https://example.com/link"));

        boolean result = treeBuilder.processStartTag("a", new Attributes().put("class", "link"));
        assertTrue(result, "processStartTag should return true for valid start tag with attributes");

        Element a = treeBuilder.currentElement();
        assertSame(a.tagName(), "a", "Element tag name should be set correctly");
        assertSame(a.baseUri(), "https://example.com", "Element base URI should be set correctly");
        assertEquals("https://example.com/link", a.attr("href"), "Element href attribute should be set correctly");
        assertEquals("link", a.attr("class"), "Element class attribute should be set correctly");
    }

    @Test
    public void testProcessEndTag() {
        treeBuilder.doc = new Document("https://example.com");
        treeBuilder.stack.clear();
        treeBuilder.currentToken = new Token.EndTag("a");

        Element parent = new Element("div");
        Element a = new Element("a");
        parent.appendChild(a);
        treeBuilder.stack.add(parent);

        boolean result = treeBuilder.processEndTag("a");
        assertTrue(result, "processEndTag should return true for valid end tag");

        Element current = treeBuilder.currentElement();
        assertNull(current, "Current element should be null after processing end tag");
    }

    @Test
    public void testProcessEndTagWrongTagName() {
        treeBuilder.doc = new Document("https://example.com");
        treeBuilder.stack.clear();
        treeBuilder.currentToken = new Token.EndTag("body");

        Element parent = new Element("html");
        Element body = new Element("body");
        parent.appendChild(body);
        treeBuilder.stack.add(parent);

        boolean result = treeBuilder.processEndTag("a");
        assertFalse(result, "processEndTag should return false for invalid end tag");

        Element current = treeBuilder.currentElement();
        assertSame(current, body, "Current element should be the body element after processing end tag");
    }

    @Test
    public void testProcessEndTagMultipleLevels() {
        treeBuilder.doc = new Document("https://example.com");
        treeBuilder.stack.clear();
        treeBuilder.currentToken = new Token.EndTag("html");

        Element html = new Element("html");
        Element head = new Element("head");
        Element body = new Element("body");
        Element h1 = new Element("h1");
        html.appendChild(head);
        html.appendChild(body);
        head.appendChild(h1);
        body.appendChild(h1);
        treeBuilder.stack.add(html);

        boolean result = treeBuilder.processEndTag("html");
        assertTrue(result, "processEndTag should return true for valid end tag at top level");

        Element current = treeBuilder.currentElement();
        assertNull(current, "Current element should be null after processing end tag");
    }
}