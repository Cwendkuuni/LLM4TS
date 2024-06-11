package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.LinkedList;

import org.junit.Test;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Parser;

/**
 * Test class for {@link Parser} class of JSoup library.
 *
 * @author JohnDo
 */
public class ParserTest {

    @Test
    public void testParse() {
        String html = "<html><head><title>Test Page</title></head><body><h1>Hello World</h1></body></html>";
        Document doc = Parser.parse(html, "http://test.com");

        assertNotNull(doc);
        assertEquals("Test Page", doc.title().text());
        assertEquals("Hello World", doc.body().getElementsByTag("h1").text());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<h1>Hello Body</h1>";
        Document doc = Parser.parseBodyFragment(bodyHtml, "http://test.com");

        assertNotNull(doc);
        assertEquals("http://test.com", doc.baseUri());
        assertNotNull(doc.body());
        assertEquals("Hello Body", doc.body().getElementsByTag("h1").text());
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        String bodyHtml = "<x:unknown></x:unknown>";
        Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, "http://test.com");

        assertNotNull(doc);
        assertEquals("http://test.com", doc.baseUri());
        assertNotNull(doc.body());
        assertEquals("", doc.body().child(0).tagName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullBaseUri() {
        String html = "<html><head><title>Test Page</title></head><body><h1>Hello World</h1></body></html>";
        Parser.parse(html, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyBaseUri() {
        String html = "<html><head><title>Test Page</title></head><body><h1>Hello World</h1></body></html>";
        Parser.parse(html, "");
    }

    @Test
    public void testParseWithScriptTag() {
        String html = "<html><head><title>Test Page</title></head><body><h1>Hello World</h1><script type=\"text/javascript\">console.log('JavaScript is parsed')</script></body></html>";
        Document doc = Parser.parse(html, "http://test.com");

        assertNotNull(doc);
        assertEquals("Test Page", doc.title().text());
        assertEquals("Hello World", doc.body().getElementsByTag("h1").text());
        DataNode scriptNode = (DataNode) doc.body().getElementsByTag("script").first();
        assertEquals("console.log('JavaScript is parsed')", scriptNode.text());
    }

    @Test
    public void testParseWithCommentTag() {
        String html = "<html><head><title>Test Page</title></head><body><h1>Hello World</h1><!-- This is a comment --></body></html>";
        Document doc = Parser.parse(html, "http://test.com");

        assertNotNull(doc);
        assertEquals("Test Page", doc.title().text());
        assertEquals("Hello World", doc.body().getElementsByTag("h1").text());
        assertEquals("This is a comment ", doc.body().child(1).text());
    }

    @Test
    public void testParseWithEmptyTag() {
        String html = "<html><head><title>Test Page</title></head><body><img src=\"image.jpg\"/></body></html>";
        Document doc = Parser.parse(html, "http://test.com");

        assertNotNull(doc);
        assertEquals("Test Page", doc.title().text());
        Element imgTag = doc.body().getElementsByTag("img").first();
        assertNotNull(imgTag);
        assertEquals("", imgTag.text());
    }

    @Test
    public void testParseWithBaseTag() {
        String html = "<html><head><title>Test Page</title></head><body><base href=\"/subfolder\"><h1>Hello World</h1></body></html>";
        Document doc = Parser.parse(html, "http://test.com");
        assertEquals("/subfolder", doc.baseUri());
    }

    @Test
    public void testParseWithSelfClosedTag() {
        String html = "<html><head><title>Test Page</title></head><body><img src=\"image.jpg\" alt=\"Image\"/></body></html>";
        Document doc = Parser.parse(html, "http://test.com");

        assertNotNull(doc);
        assertEquals("Test Page", doc.title().text());
        Element imgTag = doc.body().getElementsByTag("img").first();
        assertNotNull(imgTag);
        assertTrue(imgTag.tag().isSelfClosing());
        assertEquals("image.jpg", imgTag.absUrl("src"));
        assertEquals("Image", imgTag.attr("alt"));
    }

    @Test
    public void testParseWithUnknownTag() {
        String html = "<html><head><title>Test Page</title></head><body><x:unknown></x:unknown></body></html>";
        Document doc = Parser.parse(html, "http://test.com");

        assertNotNull(doc);
        assertEquals("Test Page", doc.title().text());
        Element unknownTag = doc.body().getElementsByTag("x:unknown").first();
        assertNotNull(unknownTag);
        assertEquals("", unknownTag.tagName());
    }

    @Test
    public void testParseWithImplicitParentTag() {
        String html = "<html><head><title>Test Page</title></head><body><p>Hello World<x:unknown></x:unknown></p></body></html>";
        Document doc = Parser.parse(html, "http://test.com");

        assertNotNull(doc);
        assertEquals("Test Page", doc.title().text());
        Element pTag = doc.body().getElementsByTag("p").first();
        assertNotNull(pTag);
        Element unknownTag = pTag.getElementsByTag("x:unknown").first();
        assertNotNull(unknownTag);
        assertEquals("", unknownTag.tagName());
    }

    @Test
    public void testParseWithNestedTags() {
        String html = "<html><head><title>Test Page</title></head><body><h1>Hello World<h2>Nested World</h2></h1></body></html>";
        Document doc = Parser.parse(html, "http://test.com");

        assertNotNull(doc);
        assertEquals("Test Page", doc.title().text());
        Element h1Tag = doc.body().getElementsByTag("h1").first();
        assertNotNull(h1Tag);
        Element h2Tag = h1Tag.getElementsByTag("h2").first();
        assertNotNull(h2Tag);
        assertEquals("Nested World", h2Tag.text());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseWithInvalidStartTag() {
        String html = "<html><head><title>Test Page</title></head><body><x:unknown></x:unknown><h1";
        Parser.parse(html, "http://test.com");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseWithInvalidEndTag() {
        String html = "<html><head><title>Test Page</title></head><body><h1>Hello World</h1></body><x:unknown>";
        Parser.parse(html, "http://test.com");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseWithMismatchedEndTag() {
        String html = "<html><head><title>Test Page</title></head><body><h1>Hello World</h1></body><div>";
        Parser.parse(html, "http://test.com");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseWithMalformedHtml() {
        String html = "<html><head><title>Test Page</title></head><body><h1>Hello World</h1</body>";
        Parser.parse(html, "http://test.com");
    }

    @Test
    public void testParseWithAttributes() {
        String html = "<html><head><title>Test Page</title></head><body><img src=\"image.jpg\" alt=\"Image\" id=\"imageId\" class=\"imageClass\"/></body></html>";
        Document doc = Parser.parse(html, "http://test.com");

        assertNotNull(doc);
        assertEquals("Test Page", doc.title().text());
        Element imgTag = doc.body().getElementsByTag("img").first();
        assertNotNull(imgTag);
        assertEquals("image.jpg", imgTag.absUrl("src"));
        assertEquals("Image", imgTag.attr("alt"));
        assertEquals("imageId", imgTag.attr("id"));
        assertEquals("imageClass", imgTag.attr("class"));
    }

    @Test
    public void testParseWithMultipleAttributes() {
        String html = "<html><head><title>Test Page</title></head><body><img src=\"image.jpg\" alt=\"Image 1\" id=\"imageId\" class=\"imageClass imageClass2\" title=\"Title\"/></body></html>";
        Document doc = Parser.parse(html, "http://test.com");

        assertNotNull(doc);
        assertEquals("Test Page", doc.title().text());
        Element imgTag = doc.body().getElementsByTag("img").first();
        assertNotNull(imgTag);
        assertEquals("image.jpg", imgTag.absUrl("src"));
        assertEquals("Image 1", imgTag.attr("alt"));
        assertEquals("imageId", imgTag.attr("id"));
        assertEquals("imageClass imageClass2", imgTag.attr("class"));
        assertEquals("Title", imgTag.attr("title"));
    }

    @Test
    public void testParseWithEmptyAttributes() {
        String html = "<html><head><title>Test Page</title></head><body><img src=\"image.jpg\"/></body></html>";
        Document doc = Parser.parse(html, "http://test.com");

        assertNotNull(doc);
        assertEquals("Test Page", doc.title().text());
        Element imgTag = doc.body().getElementsByTag("img").first();
        assertNotNull(imgTag);
        assertNull(imgTag.attr("alt"));
        assertNull(imgTag.attr("id"));
        assertNull(imgTag.attr("class"));
        assertNull(imgTag.attr("title"));
    }

    @Test
    public void testParseWithDuplicateAttributes() {
        String html = "<html><head><title>Test Page</title></head><body><img src=\"image.jpg\" alt=\"Image\" alt=\"Image 2\" id=\"imageId\" class=\"imageClass\" class=\"imageClass2\"/></body></html>";
        Document doc = Parser.parse(html, "http://test.com");

        assertNotNull(doc);
        assertEquals("Test Page", doc.title().text());
        Element imgTag = doc.body().getElementsByTag("img").first();
        assertNotNull(imgTag);
        assertEquals("image.jpg", imgTag.absUrl("src"));
        assertEquals("Image", imgTag.attr("alt")); // only the last alt attribute is kept
        assertEquals("imageId", imgTag.attr("id"));
        assertEquals("imageClass imageClass2", imgTag.attr("class")); // both classes are kept as a single string
    }
}