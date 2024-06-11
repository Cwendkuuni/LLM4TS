package org.jsoup.test;

import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.jsoup.safety.Whitelist;
import org.jsoup.helper.DataUtil;
import org.jsoup.helper.HttpConnection;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class JsoupTest {

    @Test
    public void testParseHtmlString() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        Document doc = Jsoup.parse(html);
        assertNotNull(doc);
        assertEquals("Hello World", doc.select("h1").text());
    }

    @Test
    public void testParseHtmlStringWithBaseUri() {
        String html = "<html><head><title>Test</title></head><body><img src=\"image.jpg\"></body></html>";
        Document doc = Jsoup.parse(html, "http://example.com");
        assertNotNull(doc);
        String imgSrc = doc.select("img[src]").attr("src");
        assertEquals("http://example.com/image.jpg", imgSrc);
    }

    @Test
    public void testParseHtmlStringWithParser() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        Parser parser = Jsoup.parser();
        Document doc = parser.parseInput(html, "");
        assertNotNull(doc);
        assertEquals("Hello World", doc.select("h1").text());
    }

    @Test
    public void testParseHtmlStringWithParserAndBaseUri() {
        String html = "<html><head><title>Test</title></head><body><img src=\"image.jpg\"></body></html>";
        Parser parser = Jsoup.parser();
        Document doc = parser.parseInput(html, "http://example.com");
        assertNotNull(doc);
        String imgSrc = doc.select("img[src]").attr("src");
        assertEquals("http://example.com/image.jpg", imgSrc);
    }

    @Test
    public void testParseFile() throws IOException {
        String filePath = "test.html";
        File file = new File(filePath);
        Document doc = Jsoup.parse(file, "UTF-8", file.getAbsolutePath());
        assertNotNull(doc);
        String title = doc.title();
        assertEquals("Test", title);
    }

    @Test
    public void testParseInputStream() throws IOException {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        InputStream in = new java.io.ByteArrayInputStream(html.getBytes());
        Document doc = Jsoup.parse(in, "UTF-8", "");
        assertNotNull(doc);
        assertEquals("Hello World", doc.select("h1").text());
    }

    @Test
    public void testConnectUrl() {
        String url = "http://example.com";
        Document doc = Jsoup.connect(url).get();
        assertNotNull(doc);
        String title = doc.title();
        assertNotNull(title);
    }

    @Test(expected = IOException.class)
    public void testConnectUrlWithMalformedUrl() {
        String url = "file:///example.com";
        Document doc = Jsoup.connect(url).get();
    }

    @Test
    public void testParseBodyFragment() {
        String html = "<h1>Hello World</h1>";
        Document doc = Jsoup.parseBodyFragment(html);
        assertNotNull(doc);
        assertEquals("Hello World", doc.select("h1").text());
    }

    @Test
    public void testCleanHtml() {
        String html = "<html><head><script src=\"malicious.js\"></script></head><body><h1>Hello World</h1></body></html>";
        Whitelist whitelist = Whitelist.simpleText();
        String cleanedHtml = Jsoup.clean(html, "", whitelist);
        assertNotNull(cleanedHtml);
        assertEquals("Hello World", cleanedHtml);
        assertNull(cleanedHtml.contains("script"));
    }

    @Test
    public void testIsValidHtml() {
        String html = "<html><head><script src=\"malicious.js\"></script></head><body><h1>Hello World</h1></body></html>";
        Whitelist whitelist = Whitelist.simpleText();
        boolean isValid = Jsoup.isValid(html, whitelist);
        assertFalse(isValid);
    }
}