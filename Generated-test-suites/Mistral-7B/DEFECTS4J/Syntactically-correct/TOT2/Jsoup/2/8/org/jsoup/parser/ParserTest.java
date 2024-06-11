package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {
    @Test
    public void testParseValidHtml() {
        // ...
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseEmptyHtml() {
        // ...
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseInvalidHtml() {
        // ...
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseNullBaseUri() {
        String html = "<html><head><title>Test</title></head><body></body></html>";
        try {
            Parser.parse(html, null);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testParseBodyFragmentValidHtml() {
        // ...
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseBodyFragmentEmptyHtml() {
        // ...
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseBodyFragmentInvalidHtml() {
        // ...
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseBodyFragmentNullBaseUri() {
        String bodyHtml = "<p>Hello World</p>";
        try {
            Parser.parseBodyFragment(bodyHtml, null);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testParseComment() {
        String html = "<html><!-- This is a comment --></html>";
        Document doc = Parser.parse(html, "http://example.com");
        Element comment = doc.body().child(0);
        assertEquals("This is a comment", comment.text());
    }

    @Test
    public void testParseCdata() {
        String html = "<html><![CDATA[This is CDATA]]><body></body></html>";
        Document doc = Parser.parse(html, "http://example.com");
        Element cdata = doc.body().child(0);
        assertEquals("This is CDATA", cdata.text());
    }

    @Test
    public void testParseEndTag() {
        String html = "<html><body><p>Hello World</p></body></html>";
        Document doc = Parser.parse(html, "http://example.com");
        Element p = doc.body().child(0);
        assertEquals("p", p.tagName());
    }

    @Test
    public void testParseStartTag() {
        String html = "<html><body><p>Hello World</p></body></html>";
        Document doc = Parser.parse(html, "http://example.com");
        Element p = doc.body().child(0);
        Element child = p.child(0);
        assertEquals("text", child.tagName());
        assertEquals("Hello World", child.text());
    }

    @Test
    public void testParseTextNode() {
        String html = "<html><body><p>Hello World</p></body></html>";
        Document doc = Parser.parse(html, "http://example.com");
        Element p = doc.body().child(0);
        Element textNode = p.child(0);
        assertEquals("Hello World", textNode.text());
    }

    @Test
    public void testParseXmlDecl() {
        String html = "<html xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' xmlns='http://www.w3.org/1999/xhtml'><head><title>Test</title></head><body></body></html>";
        Document doc = Parser.parse(html, "http://example.com");
        XmlDeclaration xmlDecl = doc.head().child(0);
        assertEquals("xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns=\"http://www.w3.org/1999/xhtml\"", xmlDecl.xmlDecl().toString());
    }
}