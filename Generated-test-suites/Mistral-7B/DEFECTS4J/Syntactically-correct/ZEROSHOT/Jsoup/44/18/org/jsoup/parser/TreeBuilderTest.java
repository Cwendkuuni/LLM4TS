package org.jsoup.parser;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.helper.Validate;

import java.util.ArrayList;

/**
 * Test class for TreeBuilder.
 */
public class TreeBuilderTest {

    private TreeBuilder treeBuilder;
    private String input;
    private String baseUri;
    private ParseErrorList errors;

    @Before
    public void setUp() {
        input = "<html><head><title>Test</title></head><body><h1>Hello</h1></body></html>";
        baseUri = "http://test.com";
        errors = new ParseErrorList();
    }

    @Test
    public void testParse() {
        treeBuilder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                return true;
            }
        };
        Document doc = treeBuilder.parse(input, baseUri);
        assertNotNull(doc);
        assertEquals("http://test.com", doc.baseUri());
    }

    @Test
    public void testParseWithErrors() {
        treeBuilder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                if (token.type == Token.TokenType.EOF) {
                    errors.add(new ParseError("Expected closing tag for 'head'", token.line, token.column));
                    return false;
                }
                return true;
            }
        };

        Document doc = treeBuilder.parse(input, baseUri, errors);
        assertNotNull(doc);
        assertEquals(1, errors.size());
        assertEquals("Expected closing tag for 'head'", errors.get(0).getMessage());
    }

    @Test
    public void testProcessStartTag() {
        treeBuilder = new TreeBuilder() {
            @Override
            protected boolean processTokenStartTag(TokenStartTag token) {
                String name = token.name();
                if ("html".equals(name)) {
                    Element html = new Element("html");
                    stack.add(html);
                    doc.setRootElement(html);
                    return true;
                }
                return super.process(token);
            }
        };

        treeBuilder.initialiseParse(input, baseUri, errors);
        treeBuilder.runParser();

        Element html = treeBuilder.currentElement();
        assertNotNull(html);
        assertEquals("html", html.tagName());
    }

    @Test
    public void testProcessStartTagWithAttributes() {
        treeBuilder = new TreeBuilder() {
            @Override
            protected boolean processStartTag(TokenStartTag token) {
                String name = token.name();
                if ("title".equals(name)) {
                    Element title = new Element("title");
                    title.attributes().put("id", "test-title");
                    stack.add(title);
                    doc.titleElement().setElementParent(title);
                    return true;
                }
                return super.process(token);
            }
        };

        treeBuilder.initialiseParse(input, baseUri, errors);
        treeBuilder.runParser();

        Element title = treeBuilder.currentElement();
        assertNotNull(title);
        assertEquals("title", title.tagName());
        assertEquals("test-title", title.attributes().get("id"));
    }

    @Test
    public void testProcessEndTag() {
        treeBuilder = new TreeBuilder() {
            @Override
            protected boolean processTokenEndTag(TokenEndTag token) {
                String name = token.name();
                if ("html".equals(name)) {
                    popStack();
                    return true;
                }
                return super.process(token);
            }

            private void popStack() {
                if (stack.size() > 0) {
                    stack.remove(stack.size() - 1);
                }
            }
        };

        treeBuilder.initialiseParse(input, baseUri, errors);
        treeBuilder.runParser();

        assertEquals(0, stack.size());
    }
}