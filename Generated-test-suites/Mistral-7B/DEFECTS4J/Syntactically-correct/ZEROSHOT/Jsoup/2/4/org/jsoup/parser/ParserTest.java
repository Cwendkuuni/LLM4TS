package org.jsoup.parser;

import static org.junit.Assert.*;

import org.apache.commons.lang.Validate;
import org.jsoup.nodes.*;
import org.jsoup.parser.Parser;

import java.util.LinkedList;

import org.junit.Test;

/**
 * Test class for Parser.java
 */
public class ParserTest {

    @Test
    public void testParse() {
        String html = "<html><head><title>Test Title</title></head>" +
                "<body><h1>Test Heading</h1></body></html>";
        String baseUri = "https://example.com";

        Parser parser = new Parser(html, baseUri, false);
        Document doc = parser.parse();

        assertEquals("Test Title", doc.title().text());
        assertEquals("Test Heading", doc.select("h1").text());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<body><h1>Test Heading</h1></body>";
        String baseUri = "https://example.com";

        Parser parser = new Parser(bodyHtml, baseUri, true);
        Document doc = parser.parse();

        assertEquals("Test Heading", doc.select("h1").text());
        assertTrue(doc.head() == null);
    }

    @Test(expected = NullPointerException.class)
    public void testParseWithNullHtml() {
        String html = null;
        String baseUri = "https://example.com";

        new Parser(html, baseUri, false);
    }

    @Test(expected = NullPointerException.class)
    public void testParseWithNullBaseUri() {
        String html = "<html><body></body></html>";
        String baseUri = null;

        new Parser(html, baseUri, false);
    }

    @Test
    public void testParseEmptyHtml() {
        String html = "";
        String baseUri = "https://example.com";

        Parser parser = new Parser(html, baseUri, false);
        Document doc = parser.parse();

        assertEquals(0, doc.childElementCount());
    }

    @Test
    public void testParseComment() {
        String html = "<html--><!-- This is a test comment -->" +
                "<body></body></html>";
        String baseUri = "https://example.com";

        Parser parser = new Parser(html, baseUri, false);
        Document doc = parser.parse();

        Comment comment = doc.select("html--").get(0).child(0);
        assertEquals("This is a test comment", comment.text());
    }

    @Test
    public void testParseCdata() {
        String html = "<html><![CDATA[ This is some test CDATA ]]>" +
                "<body></body></html>";
        String baseUri = "https://example.com";

        Parser parser = new Parser(html, baseUri, false);
        Document doc = parser.parse();

        Element cdata = doc.select("html > [CDATA[ This is some test CDATA ]]").get(0);
        assertNotNull(cdata);
        assertTrue(cdata.childNodeSize() == 0);
    }

    @Test
    public void testParseXmlDecl() {
        String html = "<html xmlns:foo=\"http://example.com/foo\" xml:lang=\"en\">" +
                "<body></body></html>";
        String baseUri = "https://example.com";

        Parser parser = new Parser(html, baseUri, false);
        Document doc = parser.parse();

        XmlDeclaration decl = doc.select("html > xml").get(0);
        assertEquals("http://example.com/foo", decl.xmlns("foo"));
        assertEquals("en", decl.xmlLang());
    }

    @Test
    public void testParseEndTag() {
        String html = "<html><body><p>Test<br></p></body></html>";
        String baseUri = "https://example.com";

        Parser parser = new Parser(html, baseUri, false);
        Document doc = parser.parse();

        Element p = doc.select("body > p").get(0);
        assertEquals(1, p.childElementCount());
        assertEquals("Test", p.child(0).text());
    }

    @Test
    public void testParseStartTag() {
        String html = "<html><body><a href=\"test.html\"><img src=\"image.jpg\" alt=\"Test Image\"></a></body></html>";
        String baseUri = "https://example.com";

        Parser parser = new Parser(html, baseUri, false);
        Document doc = parser.parse();

        Element a = doc.select("body > a").get(0);
        assertEquals("test.html", a.absUrl("href"));

        Element img = a.child(0);
        assertEquals("image.jpg", img.absUrl("src"));
        assertEquals("Test Image", img.attr("alt"));
    }

    @Test
    public void testParseAttribute() {
        String html = "<html><body><a href=\"test.html\" class=\"test\" title=\"Test Title\"></a></body></html>";
        String baseUri = "https://example.com";

        Parser parser = new Parser(html, baseUri, false);
        Document doc = parser.parse();

        Element a = doc.select("body > a").get(0);
        assertEquals("test", a.className());
        assertEquals("Test Title", a.attr("title"));
        assertEquals(0, a.attributeCount());
    }

    @Test
    public void testParseTextNode() {
        String html = "<html><body>Test Text</body></html>";
        String baseUri = "https://example.com";

        Parser parser = new Parser(html, baseUri, false);
        Document doc = parser.parse();

        assertEquals("Test Text", doc.select("body > text()").get(0).text());
    }
}