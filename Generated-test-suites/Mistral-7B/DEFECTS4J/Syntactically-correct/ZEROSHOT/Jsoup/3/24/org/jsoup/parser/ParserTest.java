package org.jsoup.parser;

import org.apache.commons.lang3.Validate;
import org.jsoup.nodes.*;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Test class for Parser.java
 */
public class ParserTest {

    private Parser parser;

    @Before
    public void setup() {
        String html = "<html><head><title>Title</title></head><body>" +
                "<p id=\"p1\">Hello world!</p>" +
                "<script src=\"script.js\"></script>" +
                "<div><textarea name=\"textarea\" cols=\"30\" rows=\"5\">Content</textarea></div>" +
                "</body></html>";
        String baseUri = "http://example.com";
        parser = new Parser(html, baseUri, false);
    }

    @Test
    public void testParse() {
        Document doc = parser.parse();
        assertEquals("Title", doc.title().text());
        assertEquals(5, doc.body().childNodeSize());

        Element p1 = doc.getElementById("p1");
        assertEquals("Hello world!", p1.text());

        Element script = doc.selectFirst("script");
        assertNotNull(script);
        assertEquals("script.js", script.attr("src"));

        Element textarea = doc.selectFirst("textarea");
        assertNotNull(textarea);
        assertEquals("Content", textarea.attr("name"));
        assertEquals(30, textarea.attrInt("cols"));
        assertEquals(5, textarea.attrInt("rows"));

        assertEquals(Arrays.asList("html", "head", "title", "body", "p", "script", "div", "textarea"),
                getTags(doc.rootElement()));
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<p id=\"p1\">Hello world!</p>";
        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);
        assertEquals("Title", doc.title().text());
        assertEquals(1, doc.body().childNodeSize());
        Element p1 = doc.getElementById("p1");
        assertEquals("Hello world!", p1.text());
    }

    @Test
    public void testParseWithRelaxedParsing() {
        String html = "<p id=\"p1\"><script>alert('Hello world!');</script>Hello world!</p>";
        Document doc = Parser.parseBodyFragment(html, baseUri, true);
        assertEquals(1, doc.body().childNodeSize());
        Element p1 = doc.getElementById("p1");
        assertEquals("Hello world!<script>alert('Hello world!');</script>", p1.text());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullHtml() {
        parser = new Parser(null, baseUri, false);
        fail("Illegal argument exception expected.");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullBaseUri() {
        parser = new Parser("", null, false);
        fail("Illegal argument exception expected.");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyBaseUri() {
        parser = new Parser("", "", false);
        fail("Illegal argument exception expected.");
    }

    @Test
    public void testEmptyDocument() {
        String html = "";
        Document doc = Parser.parse(html, baseUri);
        assertEquals(1, doc.body().childNodeSize());
        assertFalse(doc.body().hasChildTag("p"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyBodyFragment() {
        String bodyHtml = "";
        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);
        fail("Illegal argument exception expected.");
    }

    @Test
    public void testInvalidStartTag() {
        String html = "<p<script";
        try {
            Document doc = Parser.parse(html, baseUri);
            fail("IllegalStateException expected.");
        } catch (IllegalStateException e) {
            // expected
        }
    }

    @Test
    public void testInvalidEndTag() {
        String html = "<html><body><p>Hello world!</p></foot>";
        try {
            Document doc = Parser.parse(html, baseUri);
            fail("IllegalStateException expected.");
        } catch (IllegalStateException e) {
            // expected
        }
    }

    @Test
    public void testUnknownTag() {
        String html = "<html><body><unknown></body></html>";
        try {
            Document doc = Parser.parse(html, baseUri);
            fail("IllegalStateException expected.");
        } catch (IllegalStateException e) {
            // expected
        }
    }

    @Test
    public void testXmlDeclaration() {
        String html = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><html><head><title>Title</title></head><body>" +
                "<p id=\"p1\">Hello world!</p></body></html>";
        Document doc = parser.parse();
        assertEquals("UTF-8", doc.xmlCharset());
    }

    @Test
    public void testComment() {
        String html = "<!-- This is a comment --><html><head><title>Title</title></head><body>" +
                "<p id=\"p1\">Hello world!</p></body></html>";
        Document doc = parser.parse();
        Comment comment = doc.selectFirst("comment()");
        assertNotNull(comment);
        assertEquals("This is a comment", comment.text());
    }

    @Test
    public void testCdata() {
        String html = "<html><body><![CDATA[<script>alert('Hello world!');</script>]]><p id=\"p1\">Hello world!</p></body></html>";
        Document doc = parser.parse();
        DataNode cdata = doc.selectFirst("script[position()=1()][1]");
        assertNotNull(cdata);
        assertEquals("<script>alert('Hello world!');</script>", cdata.outerHtml());
    }

    private List<String> getTags(Element element) {
        LinkedList<String> tags = new LinkedList<>();
        getTagsRecursive(element, tags);
        return tags;
    }

    private void getTagsRecursive(Element element, LinkedList<String> tags) {
        if (element != null) {
            tags.addFirst(element.tagName().toString());
            for (Element child : element.childNodes()) {
                getTagsRecursive(child, tags);
            }
        }
    }
}