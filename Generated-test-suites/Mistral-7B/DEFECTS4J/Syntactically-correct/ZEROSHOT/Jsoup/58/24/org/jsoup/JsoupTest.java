package org.jsoup.test;

import static org.junit.Assert.assertNotNull;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.safety.Whitelist;
import org.jsoup.helper.DataUtil;
import org.jsoup.helper.HttpConnection;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class JsoupTest {

    @Test
    public void testParse() throws IOException {
        String html = "<html><head></head><body><h1>Hello World</h1></body></html>";
        Document doc = Jsoup.parse(html);
        assertNotNull(doc);
        Element heading = doc.select("h1").first();
        assertNotNull(heading);
        assertNotNull(heading.text());
        assertNotNull(heading.html());
    }

    @Test
    public void testParseWithBaseUri() throws IOException {
        String html = "<img src=\"image.jpg\" alt=\"Image\">";
        Document doc = Jsoup.parse(html, "http://example.com");
        String imgSrc = doc.select("img[src]").attr("src");
        assertEquals("http://example.com/image.jpg", imgSrc);
    }

    @Test
    public void testParseWithParser() throws IOException {
        String html = "<xml><data/></xml>";
        Document doc = Jsoup.parse(html, Parser.xmlParser());
        assertNotNull(doc);
        assertEquals("xml", doc.tagName());
        assertEquals(1, doc.childNodes().size());
        assertEquals("data", doc.childNodes().get(0).tagName());
    }

    @Test
    public void testConnect() throws IOException {
        Document doc = Jsoup.connect("https://www.google.com").get();
        assertNotNull(doc);
        assertNotNull(doc.title());
        assertNotNull(doc.select("h1").first());
    }

    @Test
    public void testParseFile() throws IOException {
        Document doc = Jsoup.parse(new File("test.html"), "UTF-8");
        assertNotNull(doc);
        assertNotNull(doc.title());
        assertNotNull(doc.select("h1").first());
    }

    @Test
    public void testParseInputStream() throws IOException {
        Document doc = Jsoup.parse(new java.io.FileInputStream("test.html"), "UTF-8");
        assertNotNull(doc);
        assertNotNull(doc.title());
        assertNotNull(doc.select("h1").first());
    }

    @Test
    public void testParseBodyFragment() throws IOException {
        String bodyHtml = "<p>Hello World</p>";
        Document doc = Jsoup.parseBodyFragment(bodyHtml);
        assertNotNull(doc);
        assertNotNull(doc.body());
        assertNotNull(doc.body().text());
        assertNotNull(doc.body().html());
    }

    @Test
    public void testClean() throws IOException {
        String bodyHtml = "<p><script>alert('XSS')</script>Hello World</p>";
        Whitelist whitelist = Whitelist.simpleText();
        String cleanBodyHtml = Jsoup.clean(bodyHtml, whitelist);
        assertNotNull(cleanBodyHtml);
        assertEquals("Hello World", cleanBodyHtml);
    }

    @Test
    public void testCleanWithBaseUri() throws IOException {
        String bodyHtml = "<img src=\"image.jpg\" alt=\"Image\">";
        Whitelist whitelist = Whitelist.relaxed();
        String cleanBodyHtml = Jsoup.clean(bodyHtml, whitelist, "http://example.com");
        assertEquals("<img>", cleanBodyHtml);
    }

    @Test
    public void testCleanWithBaseUriAndOutputSettings() throws IOException {
        String bodyHtml = "<p><script>alert('XSS')</script>Hello World</p>";
        Whitelist whitelist = Whitelist.relaxed();
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        outputSettings.prettyPrint(true);
        String cleanBodyHtml = Jsoup.clean(bodyHtml, whitelist, "", outputSettings);
        assertNotNull(cleanBodyHtml);
        assertEquals("Hello World\n", cleanBodyHtml);
    }

    @Test
    public void testIsValid() throws IOException {
        String bodyHtml = "<p><script>alert('XSS')</script>Hello World</p>";
        Whitelist whitelist = Whitelist.simpleText();
        boolean isValid = Jsoup.isValid(bodyHtml, whitelist);
        assertFalse(isValid);
    }
}