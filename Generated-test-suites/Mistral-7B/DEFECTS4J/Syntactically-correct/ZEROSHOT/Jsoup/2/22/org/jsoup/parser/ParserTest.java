package org.jsoup.parser;

import static org.junit.Assert.*;
import org.apache.commons.lang.Validate;
import org.jsoup.nodes.*;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Test class for Parser.java
 */
public class ParserTest {

    @Test
    public void testParse() {
        Parser parser = new Parser("<html><head><title>Test</title></head><body><h1>Hello</h1></body></html>", "http://example.com");
        Document doc = parser.parse();
        Element root = doc.body();
        assertEquals(1, root.childNodes().size());
        Element h1 = root.childNodes().get(0);
        assertEquals("Hello", h1.text());
        assertEquals("http://example.com", doc.baseUri());
    }

    @Test
    public void testParseBodyFragment() {
        Parser parser = new Parser("<p>Hello</p>", "http://example.com");
        Document doc = parser.parse();
        Element body = doc;
        assertEquals(1, body.childNodes().size());
        Element p = body.childNodes().get(0);
        assertEquals("Hello", p.text());
        assertEquals("http://example.com", doc.baseUri());
    }

    @Test
    public void testParseXmlDecl() {
        Parser parser = new Parser("<!DOCTYPE html SYSTEM \"about:legacy-compat\">", null);
        Document doc = parser.parse();
        assertNotNull(doc.xmlDeclaration());
        assertEquals("about:legacy-compat", doc.xmlDeclaration().systemIdentifier());
    }

    @Test
    public void testParseComment() {
        Parser parser = new Parser("<!DOCTYPE html><!-- This is a comment --><html><body><h1>Hello</h1></body></html>", null);
        Document doc = parser.parse();
        Element comment = doc.body().childNodes().get(0);
        assertEquals("This is a comment", comment.text());
    }

    @Test
    public void testParseAttribute() {
        Parser parser = new Parser("<img src='image.png' alt='Image' title='Title'>", null);
        Document doc = parser.parse();
        Element img = doc.body().childNodes().get(0);
        assertEquals("image.png", img.attr("src"));
        assertEquals("Image", img.attr("alt"));
        assertEquals("Title", img.attr("title"));
        assertNull(img.attr("unknown"));
    }

    @Test
    public void testParseTextNode() {
        Parser parser = new Parser("<p>Hello World</p>", null);
        Document doc = parser.parse();
        Element p = doc.body().childNodes().get(0);
        TextNode textNode = (TextNode) p.childNodes().get(0);
        assertEquals("Hello World", textNode.text());
    }

    @Test
    public void testParseCdata() {
        Parser parser = new Parser("<script><![CDATA[alert('Hello World');]]></script>", null);
        Document doc = parser.parse();
        Element script = doc.body().childNodes().get(0);
        TextNode textNode = (TextNode) script.childNodes().get(0);
        assertEquals("alert('Hello World');", textNode.text());
    }

    @Test(expected = IllegalStateException.class)
    public void testParseInvalidStartTag() {
        Parser parser = new Parser("<ka>", null);
        parser.parse();
        fail("Expected IllegalStateException");
    }

    @Test(expected = IllegalStateException.class)
    public void testParseUnmatchedEndTag() {
        Parser parser = new Parser("<html><body><p><div>", null);
        parser.parse();
        fail("Expected IllegalStateException");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseInvalidBaseUri() {
        Parser parser = new Parser("<html><head><base href='javascript:alert(\"Hello\")'>", null);
        try {
            parser.parse();
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid base URI: javascript:alert(\"Hello\")", e.getMessage());
        }
    }

    @Test
    public void testParseNestedElements() {
        Parser parser = new Parser("<html><head><title>Test</title></head><body><h1>Hello</h1><p><i>Italic</i></p></body></html>", "http://example.com");
        Document doc = parser.parse();
        Element root = doc;
        assertEquals(3, root.childNodes().size());
        Element title = root.childNodes().get(0);
        assertEquals("Test", title.text());
        Element h1 = root.childNodes().get(1);
        assertEquals("Hello", h1.text());
        Element p = root.childNodes().get(2);
        assertEquals(1, p.childNodes().size());
        Element i = p.childNodes().get(0);
        assertEquals("Italic", i.text());
    }

    @Test
    public void testParseEmptyElement() {
        Parser parser = new Parser("<img src='image.png'/>", null);
        Document doc = parser.parse();
        Element img = doc.body();
        assertEquals(1, img.childNodes().size());
        assertTrue(img.childNodes().get(0) instanceof Element);
        Element emptyImg = (Element) img.childNodes().get(0);
        assertTrue(emptyImg.tagName().equals("img"));
        assertTrue(emptyImg.childNodes().isEmpty());
    }

    @Test
    public void testParseBaseElement() {
        Parser parser = new Parser("<html><head><base href='/test'><title>Test</title></head><body><a href='/about'>About</a></body></html>", "http://example.com");
        Document doc = parser.parse();
        assertEquals("/test", doc.baseUri());
        assertEquals("/about", doc.createElement("a").attr("href"));
    }
}