package org.jsoup.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.jsoup.safety.Whitelist;
import org.jsoup.helper.HttpConnection;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.junit.Test;

/**
 * JsoupTest.java - comprehensive test file for Jsoup class
 */
public class JsoupTest {

    @Test
    public void testParseHtmlWithBaseUri() {
        String html = "<html><head><base href='https://example.com'></base></head><body><a href='/about'>About</a></body></html>";
        Document doc = Jsoup.parse(html, "https://example.com");
        String linkHref = doc.select("a[href]").first().attr("href");
        assertNotNull(linkHref);
        assertNotNull(new URL(linkHref));
    }

    @Test
    public void testParseHtmlWithoutBaseUri() {
        String html = "<html><head></head><body><a href='/about'>About</a></body></html>";
        Document doc = Jsoup.parse(html);
        String linkHref = doc.select("a[href]").first().attr("href");
        assertNull(linkHref);
    }

    @Test
    public void testParseHtmlWithParser() {
        String html = "<html><body><p>Hello, World!</p></body></html>";
        Parser parser = new Parser();
        Document doc = Jsoup.parse(html, "", parser);
        assertNotNull(doc);
        assertNotNull(doc.body().text());
        assertNotNull(doc.select("p").text());
    }

    @Test
    public void testConnectToUrl() {
        Connection connection = Jsoup.connect("https://example.com");
        Document doc = connection.get();
        assertNotNull(doc);
        assertNotNull(doc.title());
    }

    @Test
    public void testParseFile() throws IOException {
        File file = new File("test.html");
        Document doc = Jsoup.parse(file, "UTF-8", "https://example.com");
        assertNotNull(doc);
        assertNotNull(doc.title());
    }

    @Test
    public void testParseInputStream() throws IOException {
        String html = "<html><head><title>Test</title></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes("UTF-8"));
        Document doc = Jsoup.parse(inputStream, "UTF-8", "https://example.com");
        assertNotNull(doc);
        assertNotNull(doc.title());
    }

    @Test
    public void testCleanHtml() {
        String html = "<html><head><title>Test</title><script src='malicious.js'></script></head><body><iframe src='malicious.com'></iframe></body></html>";
        Whitelist whitelist = Whitelist.simpleText();
        String cleanHtml = Jsoup.clean(html, "", whitelist);
        assertNotNull(cleanHtml);
        assertFalse(cleanHtml.contains("<script>"));
        assertFalse(cleanHtml.contains("<iframe>"));
    }

    @Test(expected = IOException.class)
    public void testParseFileWithInvalidCharset() throws IOException {
        File file = new File("test.html");
        Document doc = Jsoup.parse(file, "INVALID_CHARSET", "");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConnectToInvalidUrl() {
        Connection connection = Jsoup.connect("javascript:alert('Hello, World!')");
    }

    @Test
    public void testCleanHtmlWithOutputSettings() {
        String html = "<html><head><title>Test</title></head><body><p><script>document.write('Hello, World!')</script></p></body></html>";
        Whitelist whitelist = Whitelist.simpleText();
        Document.OutputSettings outputSettings = Document.OutputSettings.prettyPrint();
        String cleanHtml = Jsoup.clean(html, "", whitelist, outputSettings);
        assertNotNull(cleanHtml);
        assertFalse(cleanHtml.contains("document.write"));
        assertFalse(cleanHtml.contains("<script>"));
        assertTrue(cleanHtml.contains("<p>"));
        assertTrue(cleanHtml.contains("Hello, World!"));
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<p>Hello, World!</p>";
        Document doc = Jsoup.parseBodyFragment(bodyHtml);
        assertNotNull(doc);
        assertNotNull(doc.body().text());
        assertNotNull(doc.select("p").text());
    }

    @Test
    public void testParseBodyFragmentWithBaseUri() {
        String bodyHtml = "<a href='/about'>About</a>";
        Document doc = Jsoup.parseBodyFragment(bodyHtml, "https://example.com");
        String linkHref = doc.select("a[href]").first().attr("href");
        assertNotNull(linkHref);
        assertNotNull(new URL(linkHref));
    }

    @Test
    public void testParseUrl() throws IOException {
        Document doc = Jsoup.parse(new URL("https://example.com"), 5000);
        assertNotNull(doc);
        assertNotNull(doc.title());
    }

    @Test
    public void testCleanHtmlWithInvalidWhitelist() {
        Whitelist whitelist = new Whitelist("invalid-whitelist.xml");
        String html = "<html><head></head><body><script>alert('Hello, World!')</script></body></html>";
        assertThrows(IllegalArgumentException.class, () -> Jsoup.clean(html, "", whitelist));
    }
}