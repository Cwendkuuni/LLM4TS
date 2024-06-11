package org.jsoup.parser;

import static org.junit.Assert.*;

import org.apache.commons.lang.Validate;
import org.jsoup.nodes.*;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

public class ParserTest {

    private Parser parser;
    private Document doc;

    @Before
    public void setUp() {
        String html = "<html><head><title>Test Page</title></head>" +
                "<body><h1>Hello World</h1>" +
                "<p>This is a test.</p>" +
                "<script src=\"script.js\"></script>" +
                "</body></html>";
        String baseUri = "http://example.com/";
        parser = new Parser(html, baseUri, false);
        doc = parser.parse();
    }

    @Test
    public void testParse() {
        assertEquals("Test Page", doc.title().text());
        assertEquals(1, doc.body().childNodes().size());
        Element h1 = doc.body().child(0);
        assertEquals("Hello World", h1.text());
        assertEquals(1, h1.childNodes().size());
        Element p = h1.child(0);
        assertEquals("This is a test.", p.text());
        assertEquals(0, p.childNodes().size());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<p>This is a test.</p>";
        Document fragmentDoc = Parser.parseBodyFragment(bodyHtml, baseUri);
        assertEquals(1, fragmentDoc.body().childNodes().size());
        Element p = fragmentDoc.body().child(0);
        assertEquals("This is a test.", p.text());
        assertEquals(0, p.childNodes().size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullHtml() {
        Parser parser = new Parser(null, baseUri, false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullBaseUri() {
        Parser parser = new Parser(html, null, false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyHtml() {
        Parser parser = new Parser("", baseUri, false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseWithEmptyBodyFragment() {
        Parser parser = new Parser("", baseUri, true);
    }

    @Test
    public void testParseComment() {
        parser = new Parser("<!---->", baseUri, false);
        doc = parser.parse();
        Element comment = doc.body().child(0);
        assertEquals("", comment.text());
    }

    @Test
    public void testParseXmlDecl() {
        String xmlDecl = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
        parser = new Parser(xmlDecl, baseUri, false);
        doc = parser.parse();
        Element xmlDeclaration = doc.body().child(0);
        assertTrue(xmlDeclaration instanceof XmlDeclaration);
    }

    @Test
    public void testParseEndTag() {
        parser = new Parser("<p><h1>Hello World</h1></p>", baseUri, false);
        doc = parser.parse();
        Element p = doc.body().child(0);
        Element h1 = p.child(0);
        assertEquals(1, p.childNodes().size());
        assertEquals(1, h1.childNodes().size());
        assertEquals(0, h1.childNodes().get(0).childNodes().size());
    }

    @Test
    public void testParseStartTag() {
        parser = new Parser("<p><h1>Hello World</h1></p>", baseUri, false);
        doc = parser.parse();
        Element p = doc.body().child(0);
        Element h1 = p.child(0);
        assertEquals(1, p.childNodes().size());
        assertEquals(1, h1.childNodes().size());
        assertEquals("h1", h1.tagName());
    }

    @Test
    public void testParseAttribute() {
        String html = "<p class=\"test\" id=\"id\">Text</p>";
        parser = new Parser(html, baseUri, false);
        doc = parser.parse();
        Element p = doc.body().child(0);
        Map<String, String> attributes = p.attributes();
        assertEquals(2, attributes.size());
        assertEquals("test", attributes.get("class"));
        assertEquals("id", attributes.get("id"));
    }

    @Test
    public void testParseTextNode() {
        parser = new Parser("<p>Test</p>", baseUri, false);
        doc = parser.parse();
        Element p = doc.body().child(0);
        TextNode textNode = (TextNode) p.childNodes().get(0);
        assertEquals("Test", textNode.text());
    }

    @Test
    public void testParseCdata() {
        parser = new Parser("<![CDATA[ <script>function test() { alert('Test'); }](]>]", baseUri, false);
        doc = parser.parse();
        Element script = doc.body().child(0);
        DataNode dataNode = (DataNode) script.childNodes().get(0);
        assertEquals("<script>function test() { alert('Test'); }](>", dataNode.text());
    }

    @Test
    public void testParseBaseURI() {
        String html = "<base href=\"/test\"></base><p>Test</p>";
        parser = new Parser(html, baseUri, false);
        doc = parser.parse();
        assertEquals("/test", doc.baseUri());
    }
}