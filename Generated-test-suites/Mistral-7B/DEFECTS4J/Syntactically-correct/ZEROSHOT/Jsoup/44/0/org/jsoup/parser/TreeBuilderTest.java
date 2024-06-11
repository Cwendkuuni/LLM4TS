package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.util.ArrayList;

import org.junit.Test;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Attributes;

/**
 * Test class for TreeBuilder.
 *
 * @author John Doe
 */
public class TreeBuilderTest {

    @Test
    public void testInitialiseParse() {
        String input = "<html><head><title>Test</title></head><body><h1>Hello</h1></body></html>";
        String baseUri = "http://example.com/";
        ParseErrorList errors = new ParseErrorList();

        TreeBuilder treeBuilder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                return true;
            }
        };

        treeBuilder.initialiseParse(input, baseUri, errors);

        Validate.notNull(treeBuilder.doc, "Document must not be null");
        Validate.notNull(treeBuilder.tokeniser, "Tokeniser must not be null");
        Validate.notNull(treeBuilder.stack, "Stack must not be null");
        Validate.notNull(treeBuilder.baseUri, "BaseURI must not be null");
        Validate.assertNull(treeBuilder.currentToken, "currentToken must be null");
        Validate.assertNull(treeBuilder.errors, "Errors must be null");
    }

    @Test
    public void testParse() {
        String input = "<html><head><title>Test</title></head><body><h1>Hello</h1></body></html>";
        String baseUri = "http://example.com/";
        ParseErrorList errors = new ParseErrorList();

        TreeBuilder treeBuilder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                return true;
            }

            @Override
            protected boolean processStartTag(Token token) {
                return true;
            }

            @Override
            protected boolean processEndTag(Token token) {
                return true;
            }
        };

        Document doc = treeBuilder.parse(input, baseUri);

        assertEquals("<html><head><title>Test</title></head><body><h1>Hello</h1></body></html>", doc.html());

        Element html = doc.body();
        Element title = html.head().title();
        Element h1 = html.selectFirst("h1");

        assertSame(html, doc.selectFirst("html"));
        assertSame(title, doc.selectFirst("title"));
        assertSame(h1, doc.selectFirst("h1"));
    }

    @Test
    public void testParseWithErrors() {
        String input = "<html><head><title>Test</title></head><body><h1>Hello</h1>"; // missing closing tag
        String baseUri = "http://example.com/";
        ParseErrorList errors = new ParseErrorList();

        TreeBuilder treeBuilder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                return true;
            }

            @Override
            protected boolean processStartTag(Token token) {
                return true;
            }

            @Override
            protected boolean processEndTag(Token token) {
                return true;
            }
        };

        Document doc = treeBuilder.parse(input, baseUri, errors);

        assertEquals("<html><head><title>Test</title></head><body><h1>Hello</h1>", doc.html());
        assertNull(doc.selectFirst("h2")); // should not be present due to missing closing h1 tag

        int errorSize = errors.size();
        assertEquals(1, errorSize);

        ParseError error = errors.get(0);
        assertEquals(ParseError.TagMismatch, error.getErrorCode());
        assertEquals("unexpected <", error.getMessage());
    }

    @Test
    public void testProcessStartTag() {
        TreeBuilder treeBuilder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                return true;
            }

            @Override
            protected boolean processStartTag(Token token) {
                return process(token);
            }
        };

        String input = "<html>";
        String baseUri = "http://example.com/";
        ParseErrorList errors = new ParseErrorList();

        treeBuilder.initialiseParse(input, baseUri, errors);

        Token startToken = new Token.StartTag().name("html");
        treeBuilder.processStartTag(startToken);

        Element html = treeBuilder.currentElement();
        assertEquals("html", html.tagName());
    }

    @Test
    public void testProcessStartTagWithAttributes() {
        TreeBuilder treeBuilder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                return true;
            }

            @Override
            protected boolean processStartTag(Token token) {
                return process(token);
            }

            @Override
            protected boolean processEndTag(Token token) {
                return true;
            }
        };

        String input = "<img src=\"image.jpg\" alt=\"test\">";
        String baseUri = "http://example.com/";
        ParseErrorList errors = new ParseErrorList();

        treeBuilder.initialiseParse(input, baseUri, errors);

        tokeniser = new Tokeniser(new CharacterReader(input), errors);
        tokeniser.read(); // <img

        Token startToken = new Token.StartTag().name("img");
        treeBuilder.processStartTag(startToken);

        assertEquals("img", treeBuilder.currentElement().tagName());

        tokeniser.read(); // src="image.jpg"
        treeBuilder.processStartTag(tokeniser.peek().type == Token.TokenType.TagStart ? tokeniser.read() : null);

        Attributes attributes = treeBuilder.currentElement().attributes();
        assertEquals(2, attributes.size());
        assertEquals("image.jpg", attributes.get("src"));
        assertEquals("test", attributes.get("alt"));

        tokeniser.read(); // /img
        treeBuilder.processEndTag("img");
    }
}