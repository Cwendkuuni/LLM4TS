package org.jsoup.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

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

import org.junit.Test;

public class JsoupTest {

    @Test
    public void testParseValidHtml() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        Document doc = Jsoup.parse(html);
        assertNotNull(doc);
        assertEquals("Test", doc.title());
        assertEquals("Hello World", doc.select("h1").text());
    }

    @Test
    public void testParseHtmlWithBaseUri() {
        String html = "<html><head></head><body><img src=\"image.jpg\"></body></html>";
        String baseUri = "http://example.com";
        Document doc = Jsoup.parse(html, baseUri);
        Element imgElement = doc.select("img").first();
        assertNotNull(imgElement);
        assertEquals("image.jpg", imgElement.attr("src"));
        assertEquals("http://example.com/image.jpg", imgElement.absUrl("src"));
    }

    @Test
    public void testParseHtmlWithCustomParser() {
        Parser parser = Parser.xmlParser();
        String html = "<xml><tag>Test</tag></xml>";
        Document doc = Jsoup.parse(html, "", parser);
        Element tagElement = doc.select("tag").first();
        assertNotNull(tagElement);
        assertEquals("Test", tagElement.text());
    }

    @Test
    public void testParseInvalidHtml() {
        String html = "<html><body><script>alert('Hello')</script>";
        assertThrows(IllegalArgumentException.class, () -> Jsoup.parse(html));
    }

    @Test
    public void testParseFile() throws IOException {
        File file = new File("test.html");
        String html = DataUtil.readFile(file);
        Document doc = Jsoup.parse(html);
        assertNotNull(doc);
        assertEquals(DataUtil.readFile(new File("test.html")), doc.outerHtml());
    }

    @Test
    public void testParseInputStream() throws IOException {
        File file = new File("test.html");
        InputStream inputStream = new FileInputStream(file);
        Document doc = Jsoup.parse(inputStream, "");
        assertNotNull(doc);
        assertEquals(DataUtil.readFile(file), doc.outerHtml());
    }

    @Test
    public void testConnect() {
        Connection connection = Jsoup.connect("http://example.com");
        Document doc = connection.get();
        assertNotNull(doc);
        assertEquals("Hello World", doc.select("h1").text());
    }

    @Test
    public void testParseBodyFragment() {
        String html = "<h1>Hello World</h1>";
        Document doc = Jsoup.parseBodyFragment(html);
        assertEquals("Hello World", doc.select("h1").text());
    }

    @Test
    public void testCleanHtml() {
        String html = "<html><body><script>alert('Hello')</script><div id=\"test\">Test</div></body></html>";
        Whitelist whitelist = Whitelist.simpleTags();
        String cleanHtml = Jsoup.clean(html, "", whitelist);
        assertNotNull(cleanHtml);
        assertEquals("<div id=\"test\">Test</div>", cleanHtml);
    }

    @Test
    public void testCleanHtmlWithBaseUri() {
        String html = "<html><body><img src=\"image.jpg\"></body></html>";
        Whitelist whitelist = Whitelist.simpleTags();
        String cleanHtml = Jsoup.clean(html, "http://example.com", whitelist);
        assertNotNull(cleanHtml);
        assertEquals("<body></body>", cleanHtml);
    }

    @Test
    public void testCleanHtmlWithOutputSettings() {
        String html = "<html><body><img src=\"image.jpg\" alt=\"Test\"></body></html>";
        Whitelist whitelist = Whitelist.simpleTags();
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        outputSettings.prettyPrint(true);
        String cleanHtml = Jsoup.clean(html, "", whitelist, outputSettings);
        assertNotNull(cleanHtml);
        assertEquals("<html>\n<body>\n<!--\n<img alt=\"Test\" src=\"image.jpg\" />\n-->\n</body>\n</html>", cleanHtml);
    }

    @Test
    public void testIsValidHtml() {
        String html = "<html><body><script>alert('Hello')</script><div id=\"test\">Test</div></body></html>";
        Whitelist whitelist = Whitelist.simpleTags();
        assertFalse(Jsoup.isValid(html, whitelist));
    }

    @Test
    public void testParseUrl() {
        String urlString = "http://example.com";
        Document doc = Jsoup.parse(urlString);
        assertNull(doc);
    }

    @Test
    public void testParseUrlWithTimeout() {
        String urlString = "http://example.com";
        Connection connection = Jsoup.connect(urlString);
        connection.timeout(100);
        assertThrows(java.net.SocketTimeoutException.class, () -> connection.get());
    }

    @Test
    public void testParseUrlWithMalformedUri() {
        String urlString = "jsoup://example.com";
        assertThrows(MalformedURLException.class, () -> Jsoup.parse(urlString));
    }

    @Test
    public void testConnectWithInvalidUrl() {
        String urlString = "jsoup://example.com";
        assertThrows(MalformedURLException.class, () -> Jsoup.connect(urlString));
    }

    @Test
    public void testParseFileWithInvalidCharset() throws IOException {
        File file = new File("test.html");
        String charsetName = "invalid";
        try {
            Jsoup.parse(file, charsetName, "");
            fail();
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testParseInputStreamWithInvalidCharset() throws IOException {
        File file = new File("test.html");
        InputStream inputStream = new FileInputStream(file);
        String charsetName = "invalid";
        try {
            Jsoup.parse(inputStream, charsetName, "");
            fail();
        } catch (IOException e) {
            // Expected
        }
    }
}