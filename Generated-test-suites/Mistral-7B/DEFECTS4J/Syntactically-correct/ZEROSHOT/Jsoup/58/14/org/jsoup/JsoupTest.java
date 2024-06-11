package org.jsoup.test;

import static org.junit.Assert.*;

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

/**
 * JsoupTest class to test all methods in Jsoup class
 */
public class JsoupTest {

    @org.junit.Test
    public void testParse() throws IOException {
        String html = "<html><head></head><body><h1>Test</h1></body></html>";
        Document doc = Jsoup.parse(html);
        assertEquals("Test", doc.title());
        assertEquals(1, doc.select("h1").size());
    }

    @org.junit.Test
    public void testParseWithBaseUri() throws IOException {
        String html = "<html><head></head><body><img src=\"img.jpg\"></body></html>";
        String baseUri = "http://example.com";
        Document doc = Jsoup.parse(html, baseUri);
        assertEquals("http://example.com/img.jpg", doc.imageUrl("img.jpg").toExternalForm());
    }

    @org.junit.Test
    public void testParseWithParser() throws IOException {
        String html = "<html><head></head><body><h1>Test</h1></body></html>";
        Parser parser = new Parser.MalformedEncodingParser();
        Document doc = Jsoup.parse(html, new Parser[0], parser);
        assertEquals("Test", doc.title());
        assertEquals(1, doc.select("h1").size());
    }

    @org.junit.Test
    public void testConnectAndGet() throws IOException {
        Document doc = Jsoup.connect("https://www.google.com").get();
        assertEquals("Google", doc.title());
        assertEquals("https://www.google.com/", doc.location().toExternalForm());
    }

    @org.junit.Test
    public void testParseFile() throws IOException {
        String charsetName = "UTF-8";
        File file = new File("test.html");
        Document doc = Jsoup.parse(file, charsetName);
        assertEquals("Test", doc.title());
        assertEquals(1, doc.select("h1").size());
    }

    @org.junit.Test
    public void testParseInputStream() throws IOException {
        String charsetName = "UTF-8";
        File file = new File("test.html");
        InputStream in = new FileInputStream(file);
        Document doc = Jsoup.parse(in, charsetName);
        assertEquals("Test", doc.title());
        assertEquals(1, doc.select("h1").size());
        in.close();
    }

    @org.junit.Test
    public void testParseBodyFragment() throws IOException {
        String bodyHtml = "<h1>Test</h1>";
        Document doc = Jsoup.parseBodyFragment(bodyHtml);
        assertEquals("Test", doc.title());
        assertEquals(1, doc.select("h1").size());
    }

    @org.junit.Test
    public void testParseUrl() throws IOException {
        Document doc = Jsoup.parse(new URL("https://www.google.com"), 5000);
        assertEquals("Google", doc.title());
        assertEquals("https://www.google.com/", doc.location().toExternalForm());
    }

    @org.junit.Test
    public void testClean() throws IOException {
        String bodyHtml = "<html><body><script src=\"script.js\"></script><img src=\"image.jpg\" alt=\"Image\"><div id=\"div\"></div></body></html>";
        String baseUri = "http://example.com";
        Whitelist whitelist = Whitelist.standardWithImages();
        Document doc = Jsoup.parseBodyFragment(bodyHtml, baseUri);
        String cleanHtml = Jsoup.clean(doc.body().html(), whitelist);
        Document cleanDoc = Jsoup.parse(cleanHtml);
        assertEquals(0, cleanDoc.select("script").size());
        assertEquals(1, cleanDoc.select("img").size());
        assertEquals(1, cleanDoc.select("div").size());
    }

    @org.junit.Test
    public void testIsValid() throws IOException {
        String bodyHtml = "<html><body><script src=\"script.js\"></script><img src=\"image.jpg\" alt=\"Image\"><div id=\"div\"></div></body></html>";
        Whitelist whitelist = Whitelist.simpleTagNames("div");
        assertTrue(Jsoup.isValid(bodyHtml, whitelist));
    }
}