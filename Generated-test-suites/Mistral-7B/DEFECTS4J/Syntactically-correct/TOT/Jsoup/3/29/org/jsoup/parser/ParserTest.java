import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.parser.Parser;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Tag;

public class ParserTest {

    @Test
    public void testParseValidHtml() {
        String html = "<html><head><title>Test Page</title></head><body><h1>Test Heading</h1><p>Test Paragraph.</p></body></html>";
        Document doc = Parser.parse(html, "http://example.com");
        Element root = doc.body();

        Element title = root.getElementsByTag("title").first();
        assertEquals("Test Page", title.text());

        Element heading = root.getElementsByTag("h1").first();
        assertEquals("Test Heading", heading.text());

        Element paragraph = root.getElementsByTag("p").first();
        assertEquals("Test Paragraph.", paragraph.text());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<p>Test Paragraph.</p>";
        Document doc = Parser.parseBodyFragment(bodyHtml, "http://example.com");

        Element root = doc; // body is implicitly created
        assertEquals("Test Paragraph.", root.getElementsByTag("p").first().text());
    }

    @Test
    public void testParseEmptyBodyFragment() {
        String bodyHtml = "";
        Document doc = Parser.parseBodyFragment(bodyHtml, "http://example.com");

        Element root = doc; // body is implicitly created
        assertTrue(root.isEmpty());
    }

    @Test
    public void testParseXmlDecl() {
        String html = "<?xml version='1.0' encoding='UTF-8'?>\r\n<html><head><title>Test Page</title></head><body><p>Test Paragraph.</p></body></html>";
        Document doc = Parser.parse(html, "http://example.com");
        Element xmlDecl = doc.head().getElementsByTag("xml-declaration").first();

        assertEquals("1.0", xmlDecl.attr("version"));
        assertEquals("UTF-8", xmlDecl.attr("encoding"));
    }

    @Test
    public void testParseComment() {
        String html = "<!-- Test Comment -->\r\n<html><head><title>Test Page</title></head><body><p>Test Paragraph.</p></body></html>";
        Document doc = Parser.parse(html, "http://example.com");
        Element comment = doc.body().getElementsByTag("comment").first();

        assertEquals("Test Comment ", comment.text());
    }

    @Test
    public void testParseEmptyComment() {
        String html = "<!-- -->\r\n<html><head><title>Test Page</title></head><body><p>Test Paragraph.</p></body></html>";
        Document doc = Parser.parse(html, "http://example.com");
        Element comment = doc.body().getElementsByTag("comment").first();

        assertTrue(comment.text().isEmpty());
    }

    @Test
    public void testParseStartTagWithAttributes() {
        String html = "<img src='image.png' alt='Test Image' title='Test Title'/>";
        Document doc = Parser.parse(html, "http://example.com");

        Element img = doc.body().getElementsByTag("img").first();
        assertEquals("image.png", img.attr("src"));
        assertEquals("Test Image", img.attr("alt"));
        assertEquals("Test Title", img.attr("title"));
    }

    @Test
    public void testParseStartTagWithEmptyAttributes() {
        String html = "<input type='text' />";
        Document doc = Parser.parse(html, "http://example.com");

        Element input = doc.body().getElementsByTag("input").first();
        assertTrue(input.attributes().isEmpty());
    }

    @Test
    public void testParseCdata() {
        String html = "<html><body><![CDATA[Test Cdata]]></body></html>";
        Document doc = Parser.parse(html, "http://example.com");

        Element cdata = doc.body().getElementsByTag("cdata").first();
        assertEquals("Test Cdata", cdata.text());
    }

    @Test
    public void testParseInvalidHtml() {
        String html = "<html><head<title>Test Page</title></head><body<p>Test Paragraph.</p>"; // missing > in title and body tag

        try {
            Parser.parse(html, "http://example.com");
            fail("Expected ParserException");
        } catch (Parser.ParserException e) {
            // expected
        }
    }

    @Test
    public void testParseEmptyHtml() {
        String html = "<html></html>";
        Document doc = Parser.parse(html, "http://example.com");

        assertTrue(doc.isEmpty());
    }
}