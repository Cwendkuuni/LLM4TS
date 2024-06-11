package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.apache.commons.lang.Validate;
import org.jsoup.nodes.*;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

/**
 * JUnit 4 test file for {@link Parser} class.
 */
public class ParserTest {

    @Test
    public void testParse() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        String baseUri = "http://example.com";

        Parser parser = new Parser(html, baseUri, false);
        Document doc = parser.parse();

        assertNotNull(doc);
        assertEquals("Test", doc.title().text());
        assertEquals("Hello World", doc.body().select("h1").text());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<h1>Hello World</h1>";
        String baseUri = "http://example.com";

        Parser parser = new Parser(bodyHtml, baseUri, true);
        Document doc = parser.parse();

        assertNotNull(doc);
        assertEquals("Hello World", doc.body().select("h1").text());
        assertEquals(0, doc.head().childNodeSize());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullHtml() {
        String html = null;
        String baseUri = "http://example.com";
        new Parser(html, baseUri, false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullBaseUri() {
        String html = "<html><body></body></html>";
        String baseUri = null;
        new Parser(html, baseUri, false);
    }

    @Test
    public void testParseXmlDecl() {
        String html = "<!DOCTYPE html SYSTEM \"about:legacy-compat\">";
        String baseUri = "http://example.com";

        Parser parser = new Parser(html, baseUri, false);
        Document doc = parser.parse();

        assertNotNull(doc);
        XmlDeclaration xmlDeclaration = doc.xmlDeclaration();
        assertNotNull(xmlDeclaration);
        assertNotNull(xmlDeclaration.version());
    }

    @Test
    public void testParseComment() {
        String html = "<!DOCTYPE html>\n<!-- This is a comment -->\n<html>";
        String baseUri = "http://example.com";

        Parser parser = new Parser(html, baseUri, false);
        Document doc = parser.parse();

        assertNotNull(doc);
        assertEquals("This is a comment", doc.body().select("script[0]").attr("text"));
    }

    @Test
    public void testParseEndTag() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        String baseUri = "http://example.com";

        Parser parser = new Parser(html, baseUri, false);
        Document doc = parser.parse();

        assertNotNull(doc);
        Element h1 = doc.body().select("h1").first();
        parser.parseEndTag();
        assertEquals(0, h1.childNodeSize());
    }

    @Test
    public void testParseStartTag() {
        String html = "<html><head><title>Test</title></head><body><h1 id='test'>Hello World</h1></body></html>";
        String baseUri = "http://example.com";

        Parser parser = new Parser(html, baseUri, false);
        Document doc = parser.parse();

        assertNotNull(doc);
        Element h1 = doc.body().select("h1").first();
        assertEquals("h1", h1.tagName());
        assertEquals("Hello World", h1.text());
        assertEquals("test", h1.id());
    }

    @Test
    public void testParseAttribute() {
        String html = "<div class='test' id='test' title='Test Title' style='color: red;'></div>";
        String baseUri = "http://example.com";

        Parser parser = new Parser(html, baseUri, false);
        Document doc = parser.parse();

        assertNotNull(doc);
        Element div = doc.body().select("div").first();
        List<Attribute> attributes = div.attributes();
        assertEquals(4, attributes.size());
        assertEquals("class", attributes.get(0).getKey());
        assertEquals("test", attributes.get(0).getValue());
        assertEquals("id", attributes.get(1).getKey());
        assertEquals("test", attributes.get(1).getValue());
        assertEquals("title", attributes.get(2).getKey());
        assertEquals("Test Title", attributes.get(2).getValue());
        assertEquals("style", attributes.get(3).getKey());
        assertEquals("color: red;", attributes.get(3).getValue());
    }

    @Test
    public void testParseTextNode() {
        String html = "<html><body>Hello World</body></html>";
        String baseUri = "http://example.com";

        Parser parser = new Parser(html, baseUri, false);
        Document doc = parser.parse();

        assertNotNull(doc);
        Element body = doc.body();
        TextNode textNode = (TextNode) body.childNodes().get(0);
        assertEquals("Hello World", textNode.text());
    }

    @Test
    public void testParseCdata() {
        String html = "<html><body><![CDATA[Hello World]]></body></html>";
        String baseUri = "http://example.com";

        Parser parser = new Parser(html, baseUri, false);
        Document doc = parser.parse();

        assertNotNull(doc);
        Element body = doc.body();
        TextNode textNode = (TextNode) body.childNodes().get(0);
        assertEquals("Hello World", textNode.text());
    }

    @Test
    public void testParseCommentWithTrailingCommentAndCdata() {
        String html = "<html><!-- This is a comment -->\n<![CDATA[ <script>alert('Hello World')</script> ]]>\n<body></body>\n<!-- Another comment --></html>";
        String baseUri = "http://example.com";

        Parser parser = new Parser(html, baseUri, false);
        Document doc = parser.parse();

        assertNotNull(doc);
        assertEquals(2, doc.getElementsByTag("comment").size());
    }
}