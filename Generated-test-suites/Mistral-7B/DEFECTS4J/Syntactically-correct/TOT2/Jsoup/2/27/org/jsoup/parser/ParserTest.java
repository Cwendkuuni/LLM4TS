package org.jsoup.parser; // import statements go here

import static org.junit.Assert.*; // for assertions
import static org.jsoup.Jsoup.*; // for convenience parsing
import static org.jsoup.nodes.Attributes.attribute;
import static org.jsoup.nodes.Attributes.id;

public class ParserTest {

    

    @Test
    public void testParseHtml() {
        String html = "<html><head><title>Test Page</title></head><body><h1>Hello</h1></body></html>";
        Document doc = Parser.parse(html, "http://example.com");
        Element root = doc.body();
        assertEquals("Hello", root.text());
        assertEquals("Test Page", doc.title().text());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<p>This is a body fragment.</p>";
        Document doc = Parser.parseBodyFragment(bodyHtml, "http://example.com");
        Element root = doc; // body doesn't have a tag, just an empty document
        assertEquals("This is a body fragment.", root.text());
    }

    @Test
    public void testParseComment() {
        String html = "<html><!-- This is a comment --></html>";
        Document doc = Parser.parse(html, "http://example.com");
        Element comment = doc.getElementsByTag("comment").first();
        assertEquals("This is a comment", comment.text());
    }

    @Test
    public void testParseXmlDecl() {
        String html = "<xml version='1.0' encoding='UTF-8'?>";
        Document doc = Parser.parse(html, "http://example.com");
        XmlDeclaration xmlDeclaration = doc.getElementsByTag("xml-declaration").first();
        assertEquals("1.0", xmlDeclaration.version());
        assertEquals("UTF-8", xmlDeclaration.encoding());
    }

    @Test
    public void testParseEndTag() {
        String html = "<html><body><p>This is a paragraph.</p></body></html>";
        Document doc = Parser.parse(html, "http://example.com");
        Element p = doc.getElementsByTag("p").first();
        assertEquals("This is a paragraph.", p.text());
        assertEquals(doc.body(), p.parent());
    }

    @Test
    public void testParseStartTag() {
        String html = "<html><head><title id='title'>Test Page</title></head><body><h1 id='header'>Header</h1></body></html>";
        Document doc = Parser.parse(html, "http://example.com");
        Element title = doc.getElementsByTag("title").first();
        assertEquals("Test Page", title.text());
        assertEquals("title", title.tagName());
        assertEquals("http://example.com/title", title.absUrl("href"));
        Element header = doc.getElementsByTag("h1").first();
        assertEquals("Header", header.text());
        assertEquals("h1", header.tagName());
        assertEquals(doc.body(), header.parent());
        assertEquals("http://example.com/header", header.absUrl("href"));
    }

    @Test
    public void testParseAttribute() {
        String html = "<img src='image.jpg' alt='An image'>";
        Document doc = Parser.parse(html, "http://example.com");
        Element img = doc.getElementsByTag("img").first();
        assertEquals("image.jpg", img.absUrl("src"));
        assertEquals("An image", img.attr("alt"));
    }

    @Test
    public void testParseTextNode() {
        String html = "<p>This is some text.</p>";
        Document doc = Parser.parse(html, "http://example.com");
        Element p = doc.getElementsByTag("p").first();
        assertEquals("This is some text.", p.text());
    }

    @Test
    public void testParseCdata() {
        String html = "<script><![CDATA[var a = 1;]]></script>";
        Document doc = Parser.parse(html, "http://example.com");
        Element script = doc.getElementsByTag("script").first();
        assertEquals("var a = 1;", script.text());
    }

    
}