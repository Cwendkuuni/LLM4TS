package org.jsoup.parser;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class ParserTest {

    @Test
    public void testParse_Basic() {
        String html = "<html><head></head><body><h1>Hello World</h1></body></html>";
        Document doc = Parser.parse(html, "http://example.com");
        Element htmlNode = doc.body();
        Element headNode = doc.head();
        Element h1Node = htmlNode.child(0);

        assertEquals("html", htmlNode.tagName());
        assertEquals("head", headNode.tagName());
        assertEquals("h1", h1Node.tagName());
        assertEquals("Hello World", h1Node.text());
    }

    @Test
    public void testParse_Comments() {
        String html = "<html><!-- test comment --><body></body></html>";
        Document doc = Parser.parse(html, "http://example.com");
        List<Comment> comments = doc.getElementsByTag("comment");
        assertEquals(1, comments.size());
        assertEquals("test comment", comments.get(0).data());
    }

    @Test
    public void testParse_Cdata() {
        String html = "<html><![CDATA[<script>function test() { alert('Hello World'); }]]><body></body></html>";
        Document doc = Parser.parse(html, "http://example.com");
        Node cdataNode = doc.body().child(0);
        assertEquals("function test() { alert('Hello World'); }", cdataNode.nodeValue());
    }

    @Test
    public void testParse_XmlDecl() {
        String html = "<html xmlns=\"http://example.com\" xml:lang=\"en\">";
        Document doc = Parser.parse(html, "http://example.com");
        XmlDeclaration xmlDecl = doc.head().child(0);
        assertEquals("http://example.com", xmlDecl.xmlNs());
        assertEquals("en", xmlDecl.xmlLang());
        assertNotNull(xmlDecl.processingInstructionHandler());
    }

    @Test
    public void testParse_EmptyTag() {
        String html = "<html><head><title></title></head><body><img src=\"image.jpg\"/></body></html>";
        Document doc = Parser.parse(html, "http://example.com");
        Element titleNode = doc.head().child(0);
        Element imgNode = doc.body().child(1);

        assertEquals(0, titleNode.childNodeSize());
        assertEquals("img", imgNode.tagName());
        assertNotNull(imgNode.absUrl("src"));
    }

    @Test
    public void testParse_SelfClosingTag() {
        String html = "<html><head><title>Hello World</title></head><body><img src=\"image.jpg\" /> </body></html>";
        Document doc = Parser.parse(html, "http://example.com");
        Element titleNode = doc.head().child(0);
        Element imgNode = doc.body().child(1);

        assertEquals("Hello World", titleNode.text());
        assertEquals("img", imgNode.tagName());
        assertNotNull(imgNode.absUrl("src"));
        assertEquals(0, imgNode.childNodeSize());
    }

    @Test
    public void testParse_TagWithAttributes() {
        String html = "<html><head><title id=\"title\" attr=\"value\">Hello World</title></head><body><a href=\"link.html\" id=\"link\">Link</a></body></html>";
        Document doc = Parser.parse(html, "http://example.com");
        Element titleNode = doc.head().child(0);
        Element linkNode = doc.body().child(0);

        assertEquals("Hello World", titleNode.text());
        assertEquals("title", titleNode.tagName());
        assertEquals("value", titleNode.attribute("attr").getValue());
        assertEquals("link.html", linkNode.absUrl("href"));
        assertEquals("a", linkNode.tagName());
    }

    @Test
    public void testParse_TextNode() {
        String html = "<html><body><p>Hello World</p></body></html>";
        Document doc = Parser.parse(html, "http://example.com");
        Element pNode = doc.body().child(0);

        assertEquals("Hello World", pNode.text());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParse_InvalidHtml() {
        String html = "<html><body<p>Hello World</p>";
        Parser.parse(html, "http://example.com");
    }

    @Test
    public void testParse_BaseTag() {
        String html = "<html><head><base href=\"/test\"/><title>Test</title></head><body><a href=\"page.html\">Page</a></body></html>";
        Document doc = Parser.parse(html, "http://example.com");
        String baseUri = doc.baseUri();

        assertEquals("/test", baseUri);
        assertEquals("page.html", doc.head().child(0).absUrl("a/@href"));
    }
}