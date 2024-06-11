package org.jsoup.test;

import org.junit.Test;
import org.junit.Assert.*;
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
        String html = "<html><body><h1>Hello World</h1></body></html>";
        Document document = Jsoup.parse(html);
        Assert.assertNotNull(document);
        Assert.assertEquals("Hello World", document.title());
    }

    @Test
    public void testParseWithBaseUri() throws IOException {
        String html = "<html><head><base href='http://example.com'></base></html><body><a href='/foo'>Link</a></body></html>";
        Document document = Jsoup.parse(html);
        Assert.assertNotNull(document);
        String link = document.select("a[href]").first().attr("abs:href");
        Assert.assertEquals("http://example.com/foo", link);
    }

    @Test
    public void testParseWithParser() throws IOException {
        String html = "<html><body><h1>Hello World</h1></body></html>";
        Parser parser = new Parser();
        Document document = Jsoup.parse(html, "", parser);
        Assert.assertNotNull(document);
        Assert.assertEquals("Hello World", document.title());
    }

    @Test
    public void testParseEmpty() throws IOException {
        Document document = Jsoup.parse("");
        Assert.assertNull(document);
    }

    @Test
    public void testConnect() throws IOException {
        Document document = Jsoup.connect("https://www.google.com").get();
        Assert.assertNotNull(document);
        Assert.assertNotNull(document.title());
    }

    @Test
    public void testParseFile() throws IOException {
        File file = new File("test.html");
        Document document = Jsoup.parse(file, "UTF-8", "");
        Assert.assertNotNull(document);
        Assert.assertNotNull(document.title());
    }

    @Test
    public void testParseInputStream() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("/test.html");
        Document document = Jsoup.parse(inputStream, "UTF-8", "");
        Assert.assertNotNull(document);
        Assert.assertNotNull(document.title());
    }

    @Test
    public void testClean() {
        String html = "<html><body><script src='malicious.js'></script><h1>Hello World</h1></body></html>";
        Whitelist whitelist = Whitelist.simpleText();
        String cleanHtml = Jsoup.clean(html, "", whitelist);
        Document document = Jsoup.parse(cleanHtml);
        Assert.assertNotNull(document);
        Assert.assertEquals("Hello World", document.title());
        Assert.assertNull(document.select("script"));
    }

    @Test
    public void testIsValid() {
        String html = "<html><body><script src='malicious.js'></script><h1>Hello World</h1></body></html>";
        Whitelist whitelist = Whitelist.simpleText();
        boolean valid = Jsoup.isValid(html, whitelist);
        Assert.assertFalse(valid);
    }

    @Test
    public void testParseBodyFragment() throws IOException {
        String bodyHtml = "<h1>Hello World</h1>";
        Document document = Jsoup.parseBodyFragment(bodyHtml, "");
        Assert.assertNotNull(document);
        Assert.assertEquals("Hello World", document.title());
        Assert.assertNull(document.select("head"));
    }

    @Test
    public void testParseUrl() throws IOException {
        Document document = Jsoup.parse(new URL("https://www.google.com"), 5000);
        Assert.assertNotNull(document);
        Assert.assertNotNull(document.title());
    }
}