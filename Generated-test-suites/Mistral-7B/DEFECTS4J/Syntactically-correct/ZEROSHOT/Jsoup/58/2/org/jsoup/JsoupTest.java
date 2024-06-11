package org.jsoup.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

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

public class JsoupTest {

    @Test
    public void testParseStringWithBaseUri() {
        String html = "<html><head><base href=\"https://example.com\"></base></head><body><h1>Hello World</h1></body></html>";
        Document doc = Jsoup.parse(html, "https://example.com");
        assertNotNull(doc);
        assertNotNull(doc.title());
    }

    @Test
    public void testParseString() {
        String html = "<html><head><base href=\"/\"></base></head><body><h1>Hello World</h1></body></html>";
        Document doc = Jsoup.parse(html);
        assertNotNull(doc);
        assertNotNull(doc.title());
    }

    @Test
    public void testParseStringWithParser() {
        String html = "<html><head><base href=\"https://example.com\"></base></head><body><h1>Hello World</h1></body></html>";
        Parser parser = Parser.htmlParser();
        Document doc = Jsoup.parse(html, "https://example.com", parser);
        assertNotNull(doc);
        assertNotNull(doc.title());
    }

    @Test
    public void testParseFile() throws IOException {
        File file = new File("test.html");
        Document doc = Jsoup.parse(file, "UTF-8");
        assertNotNull(doc);
        assertNotNull(doc.title());
    }

    @Test
    public void testParseInputStream() throws IOException {
        String html = "<html><head><base href=\"https://example.com\"></base></head><body><h1>Hello World</h1></body></html>";
        InputStream stream = new java.io.ByteArrayInputStream(html.getBytes());
        Document doc = Jsoup.parse(stream, "UTF-8", "https://example.com");
        assertNotNull(doc);
        assertNotNull(doc.title());
    }

    @Test
    public void testConnect() {
        Document doc = Jsoup.connect("http://example.com").get();
        assertNotNull(doc);
        assertNotNull(doc.title());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConnectInvalidUrl() {
        Jsoup.connect("example");
    }

    @Test
    public void testParseBodyFragment() {
        String html = "<h1>Hello World</h1>";
        Document doc = Jsoup.parseBodyFragment(html);
        assertNotNull(doc);
        assertNotNull(doc.title()); // this should be null for body fragment
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseBodyFragmentNull() {
        Jsoup.parseBodyFragment(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseBodyFragmentEmpty() {
        Jsoup.parseBodyFragment("");
    }

    @Test
    public void testParseUrl() throws IOException {
        Document doc = Jsoup.parse(new URL("http://example.com"), 5000);
        assertNotNull(doc);
        assertNotNull(doc.title());
    }

    @Test
    public void testClean() {
        String html = "<html><head><script src=\"malware.js\"></script></head><body><h1>Hello World</h1></body></html>";
        Whitelist whitelist = Whitelist.simpleText();
        String cleanHtml = Jsoup.clean(html, "", whitelist);
        assertNotNull(cleanHtml);
        assertFalse(cleanHtml.contains("script"));
    }

    @Test
    public void testCleanWithBaseUri() {
        String html = "<html><head><script src=\"malware.js\"></script></head><body><img src=\"picture.jpg\" alt=\"Picture\"></body></html>";
        String baseUri = "https://example.com";
        Whitelist whitelist = Whitelist.simpleText();
        String cleanHtml = Jsoup.clean(html, baseUri, whitelist);
        assertNotNull(cleanHtml);
        assertFalse(cleanHtml.contains("script"));
        assertTrue(cleanHtml.contains("img src=\"picture.jpg\""));
    }

    @Test
    public void testIsValid() {
        String html = "<html><head><script src=\"malware.js\"></script></head><body><h1>Hello World</h1></body></html>";
        Whitelist whitelist = Whitelist.simpleText();
        boolean isValid = Jsoup.isValid(html, whitelist);
        assertFalse(isValid);
    }

    @Test
    public void testIsValidAllowed() {
        String html = "<html><head><script src=\"goodscript.js\"></script></head><body><h1>Hello World</h1></body></html>";
        Whitelist whitelist = Whitelist.simpleText().addTags("script");
        boolean isValid = Jsoup.isValid(html, whitelist);
        assertTrue(isValid);
    }
}