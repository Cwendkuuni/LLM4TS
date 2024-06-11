package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.*;
import org.jsoup.parser.Parser;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.junit.Test;

/**
 * Test class for Parser.java
 *
 * @author [Your Name]
 */
public class ParserTest {

    @Test
    public void testParse() {
        String html = "<html><head><title>Test Page</title></head>" +
                      "<body>" +
                      "<h1>Welcome to our website</h1>" +
                      "<p>Some <strong>important</strong> text.</p>" +
                      "<p>A <a href=\"http://www.example.com\">link</a>.</p>" +
                      "</body></html>";

        Parser parser = new Parser(html, "http://example.com");
        Document doc = parser.parse();

        assertNotNull(doc);
        assertEquals("Test Page", doc.title());
        assertEquals(4, doc.numHeadElements());
        assertEquals(2, doc.body().numChildElements());

        Element h1 = doc.select("h1").first();
        assertNotNull(h1);
        assertEquals("Welcome to our website", h1.text());

        Element p1 = doc.select("p").get(0);
        assertNotNull(p1);
        assertEquals("Some ", p1.text());
        Element strong = p1.child(0);
        assertNotNull(strong);
        assertEquals("important", strong.text());

        Element p2 = doc.select("p").get(1);
        assertNotNull(p2);
        Element link = p2.select("a").first();
        assertNotNull(link);
        assertEquals("http://www.example.com", link.attr("href"));
        assertEquals("link", link.tagName());

        assertEquals(1, doc.numChildren());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<p>Hello, <strong>World</strong></p>";

        Parser parser = new Parser(bodyHtml, "http://example.com");
        Document doc = parser.parseBodyFragment(bodyHtml, "http://example.com");

        assertNotNull(doc);
        assertEquals(1, doc.numHeadElements());
        assertEquals(1, doc.body().numChildElements());

        Element p = doc.body().child(0);
        assertNotNull(p);
        assertEquals("Hello, ", p.text());

        Element strong = p.child(0);
        assertNotNull(strong);
        assertEquals("World", strong.text());
    }

    @Test
    public void testParseWithBaseUri() {
        String html = "<link rel=\"icon\" href=\"images/favicon.ico\">";

        Parser parser = new Parser("<html><head>" + html + "</head><body></body></html>", "http://example.com");
        Document doc = parser.parse();

        Element link = doc.select("link[rel=icon]").first();
        assertNotNull(link);
        assertEquals("http://example.com/images/favicon.ico", link.absUrl("href"));
    }

    @Test
    public void testParseXmlDecl() {
        String xmlDecl = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";

        Parser parser = new Parser("<html>" + xmlDecl + "</html>", "http://example.com");
        Document doc = parser.parse();

        XmlDeclaration xmlDeclaration = doc.head().child(0);
        assertNotNull(xmlDeclaration);
        assertEquals("UTF-8", xmlDeclaration.encoding());
        assertEquals("1.0", xmlDeclaration.version());
    }

    @Test
    public void testParseEndTag() {
        String html = "<html><body><p>Hello<p>World</p></body></html>";

        Parser parser = new Parser(html, "http://example.com");
        Document doc = parser.parse();

        Element p1 = doc.body().child(0);
        assertNotNull(p1);
        assertEquals("Hello", p1.text());

        Element p2 = doc.body().child(1);
        assertNotNull(p2);
        assertEquals("World", p2.text());

        assertEquals(2, doc.body().numChildElements());
    }

    @Test
    public void testParseStartTag() {
        String html = "<html><body><h1>Test</h1></body></html>";

        Parser parser = new Parser(html, "http://example.com");
        Document doc = parser.parse();

        Element h1 = doc.body().child(0);
        assertNotNull(h1);
        assertEquals("Test", h1.text());

        assertEquals("h1", h1.tagName());
    }

    @Test
    public void testParseAttribute() {
        String html = "<img src=\"logo.png\" alt=\"Logo\">";

        Parser parser = new Parser(html, "http://example.com");
        Document doc = parser.parse();

        Element img = doc.select("img").first();
        assertNotNull(img);
        assertEquals("logo.png", img.absUrl("src"));
        assertEquals("Logo", img.attr("alt"));
    }

    @Test
    public void testParseCdata() {
        String html = "<html><body><![CDATA[Test <script> alert('Hello'); </script>]]></body></html>";

        Parser parser = new Parser(html, "http://example.com");
        Document doc = parser.parse();

        Element body = doc.body();
        assertNotNull(body);
        TextNode textNode = (TextNode) body.child(0);
        assertNotNull(textNode);
        String text = textNode.text();
        assertEquals("Test <script> alert('Hello'); </script>", text);
    }

    @Test
    public void testParseComment() {
        String html = "<html><!-- Test Comment --><body></body></html>";

        Parser parser = new Parser(html, "http://example.com");
        Document doc = parser.parse();

        Comment comment = doc.head().child(0);
        assertNotNull(comment);
        assertEquals("Test Comment", comment.text());
    }

    @Test(expected = AssertionError.class)
    public void testInvalidHtml() {
        String html = "<html><body><h1<script> alert('Hello'); </script>";

        Parser parser = new Parser(html, "http://example.com");
        parser.parse();
    }
}