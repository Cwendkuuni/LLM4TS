package org.jsoup.parser;

import org.apache.commons.lang.Validate;
import org.jsoup.nodes.*;
import org.junit.Test;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Test class for Parser class.
 */
public class ParserTest {

    @Test
    public void testParse() {
        String html = "<html>\n" +
                "<head>\n" +
                "<title>Test Page</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "<p>Hello World!</p>\n" +
                "</body>\n" +
                "</html>";

        String baseUri = "http://example.com/";
        Parser parser = new Parser(html, baseUri, false);
        Document doc = parser.parse();

        Element htmlNode = doc.rootElement();
        assertEquals("html", htmlNode.tagName());

        Element headNode = htmlNode.getFirstChild();
        assertEquals("head", headNode.tagName());

        Element titleNode = headNode.getFirstChild();
        assertEquals("title", titleNode.tagName());
        assertEquals("Test Page", titleNode.text());

        Element bodyNode = htmlNode.getLastChild();
        assertEquals("body", bodyNode.tagName());

        Element pNode = bodyNode.getFirstChild();
        assertEquals("p", pNode.tagName());
        assertEquals("Hello World!", pNode.text());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<p>Fragment Text</p>";
        String baseUri = "http://example.com/";

        Parser parser = new Parser(bodyHtml, baseUri, true);
        Document doc = parser.parse();

        Element htmlNode = doc.rootElement();
        assertEquals("html", htmlNode.tagName());

        Element bodyNode = htmlNode.getFirstChild();
        assertEquals("body", bodyNode.tagName());

        Element pNode = bodyNode.getFirstChild();
        assertEquals("p", pNode.tagName());
        assertEquals("Fragment Text", pNode.text());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseNullHtml() {
        String html = null;
        String baseUri = "http://example.com/";
        new Parser(html, baseUri, false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseNullBaseUri() {
        String html = "<html>\n" +
                "<head>\n" +
                "<title>Test Page</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "<p>Hello World!</p>\n" +
                "</body>\n" +
                "</html>";
        String baseUri = null;
        new Parser(html, baseUri, false);
    }

    @Test
    public void testParseEmptyHtml() {
        String html = "";
        String baseUri = "http://example.com/";
        Parser parser = new Parser(html, baseUri, false);
        Document doc = parser.parse();
        assertTrue(doc.isEmpty());
    }

    @Test
    public void testParseStartTag() {
        String html = "<html>\n" +
                "<head>\n" +
                "<title>Test Page</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "<p id=\"p1\" class=\"class1\">Hello World!</p>\n" +
                "</body>\n" +
                "</html>";

        String baseUri = "http://example.com/";
        Parser parser = new Parser(html, baseUri, false);
        Document doc = parser.parse();

        Element pNode = findElementById("p1", doc);
        assertEquals("p", pNode.tagName());
        assertEquals("1", pNode.id());
        assertEquals("class1", pNode.className());
        assertEquals("Hello World!", pNode.text());
    }

    @Test
    public void testParseEndTag() {
        String html = "<html>\n" +
                "<head>\n" +
                "<title>Test Page</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "<p id=\"p1\">Hello World!</p>\n" +
                "</body>\n" +
                "</html>";

        String baseUri = "http://example.com/";
        Parser parser = new Parser(html, baseUri, false);
        Document doc = parser.parse();

        Element pNode = findElementById("p1", doc);
        assertEquals("p", pNode.tagName());
        assertEquals("1", pNode.id());
        assertEquals("Hello World!", pNode.text());

        List<Element> children = pNode.children();
        assertEquals(0, children.size());
    }

    @Test
    public void testParseComment() {
        String html = "<html>\n" +
                "<!-- This is a comment -->\n" +
                "<head>\n" +
                "<title>Test Page</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "</body>\n" +
                "</html>";

        String baseUri = "http://example.com/";
        Parser parser = new Parser(html, baseUri, false);
        Document doc = parser.parse();

        Comment comment = findCommentByText("This is a comment", doc);
        assertNotNull(comment);
        assertEquals("This is a comment", comment.text());
    }

    @Test
    public void testParseXmlDecl() {
        String html = "<html xmlns=\"http://www.w3.org/1999/xhtml\">\n" +
                "<head>\n" +
                "<title>Test Page</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "</body>\n" +
                "</html>";

        String baseUri = "http://example.com/";
        Parser parser = new Parser(html, baseUri, false);
        Document doc = parser.parse();

        XmlDeclaration xmlDeclaration = doc.xmlDeclaration();
        assertNotNull(xmlDeclaration);
        assertEquals("http://www.w3.org/1999/xhtml", xmlDeclaration.namespace());
        assertEquals("1.0", xmlDeclaration.version());
        assertEquals("yes", xmlDeclaration.standalone());
    }

    @Test
    public void testParseCdata() {
        String html = "<html>\n" +
                "<head>\n" +
                "<title>Test Page</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "<p><![CDATA[This is CDATA text]]></p>\n" +
                "</body>\n" +
                "</html>";

        String baseUri = "http://example.com/";
        Parser parser = new Parser(html, baseUri, false);
        Document doc = parser.parse();

        Element pNode = doc.selectFirst("p > text()");
        assertEquals("This is CDATA text", pNode.text());
    }

    @Test
    public void testParseAttribute() {
        String html = "<p id=\"p1\" class=\"class1\" title=\"Title\"></p>";

        Parser parser = new Parser(html, "", false);
        Element pNode = parser.parse().selectFirst("p");

        Map<String, String> attributes = pNode.attributes();
        assertEquals(3, attributes.size());
        assertEquals("1", attributes.get("id"));
        assertEquals("class1", attributes.get("class"));
        assertEquals("Title", attributes.get("title"));
    }

    @Test
    public void testParseTextNode() {
        String html = "<html>\n" +
                "<head>\n" +
                "<title>Test Page</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "<p>Test text</p>\n" +
                "</body>\n" +
                "</html>";

        String baseUri = "http://example.com/";
        Parser parser = new Parser(html, baseUri, false);
        Document doc = parser.parse();

        Element pNode = doc.selectFirst("p");
        TextNode textNode = (TextNode) pNode.child(0);
        assertEquals("Test text", textNode.text());
    }

    private Element findElementById(String id, Document doc) {
        return doc.selectFirst("#" + id);
    }

    private Comment findCommentByText(String text, Document doc) {
        return (Comment) doc.selectFirst("comment() [@*[text() = '" + text + "']]");
    }
}