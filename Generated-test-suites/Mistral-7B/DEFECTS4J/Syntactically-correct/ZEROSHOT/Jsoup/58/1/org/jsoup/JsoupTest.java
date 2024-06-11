package org.jsoup.test;

import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;
import org.jsoup.helper.DataUtil;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class JsoupTest {

    @Test
    public void testParseHtmlString() {
        String html = "<html><body><h1>Hello World</h1></body></html>";
        Document doc = Jsoup.parse(html);
        Assert.assertNotNull(doc);
        Assert.assertEquals("Hello World", doc.title());
    }

    @Test
    public void testParseHtmlStringWithBaseUri() {
        String html = "<html><body><a href=\"/about.html\">About</a></body></html>";
        String baseUri = "http://example.com";
        Document doc = Jsoup.parse(html, baseUri);
        Element link = doc.selectFirst("a");
        String linkHref = link.attr("href");
        Assert.assertEquals("http://example.com/about.html", linkHref);
    }

    @Test
    public void testParseHtmlStringWithParser() {
        String html = "<html><body><h1>Hello World</h1></body></html>";
        Parser parser = Parser.xmlParser();
        Document doc = Jsoup.parse(html, "", parser);
        Assert.assertNotNull(doc);
        Assert.assertEquals("Hello World", doc.title());
    }

    @Test
    public void testParseHtmlFile() throws IOException {
        String filePath = "test.html";
        File inputFile = new File(filePath);
        String charsetName = "UTF-8";
        Document doc = Jsoup.parse(inputFile, charsetName);
        Assert.assertNotNull(doc);
        Assert.assertEquals(DataUtil.readFile(inputFile), doc.html());
    }

    @Test
    public void testParseHtmlFileWithBaseUri() throws IOException {
        String filePath = "test.html";
        File inputFile = new File(filePath);
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document doc = Jsoup.parse(inputFile, charsetName, baseUri);
        Element link = doc.selectFirst("a");
        String linkHref = link.attr("href");
        Assert.assertEquals("http://example.com/about.html", linkHref);
    }

    @Test
    public void testParseInputStream() throws IOException {
        String charsetName = "UTF-8";
        String inputData = "<html><body><h1>Hello World</h1></body></html>";
        InputStream inputStream = new java.io.ByteArrayInputStream(inputData.getBytes());
        Document doc = Jsoup.parse(inputStream, charsetName);
        Assert.assertNotNull(doc);
        Assert.assertEquals("Hello World", doc.title());
    }

    @Test
    public void testParseInputStreamWithBaseUri() throws IOException {
        String charsetName = "UTF-8";
        String inputData = "<html><body><a href=\"/about.html\">About</a></body></html>";
        InputStream inputStream = new java.io.ByteArrayInputStream(inputData.getBytes());
        String baseUri = "http://example.com";
        Document doc = Jsoup.parse(inputStream, charsetName, baseUri);
        Element link = doc.selectFirst("a");
        String linkHref = link.attr("href");
        Assert.assertEquals("http://example.com/about.html", linkHref);
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<h1>Hello World</h1>";
        Document doc = Jsoup.parseBodyFragment(bodyHtml);
        Assert.assertNotNull(doc);
        Assert.assertEquals("Hello World", doc.title());
    }

    @Test
    public void testParseUrl() throws IOException {
        String url = "http://example.com";
        Document doc = Jsoup.parse(url);
        Assert.assertNotNull(doc);
    }

    @Test
    public void testCleanHtml() {
        String bodyHtml = "<html><body><script>alert('Hello World');</script><h1>Hello World</h1></body></html>";
        Whitelist whitelist = Whitelist.relaxed();
        String cleanHtml = Jsoup.clean(bodyHtml, "", whitelist);
        Assert.assertNotNull(cleanHtml);
        Assert.assertFalse(cleanHtml.contains("script"));
        Assert.assertEquals("Hello World", cleanHtml.substring(cleanHtml.indexOf("<h1>") + 6, cleanHtml.indexOf("</h1>")));
    }

    @Test
    public void testCleanHtmlWithBaseUri() {
        String bodyHtml = "<html><body><img src=\"image.jpg\" alt=\"Image\"><script>alert('Hello World');</script><h1>Hello World</h1></body></html>";
        String baseUri = "http://example.com";
        Whitelist whitelist = Whitelist.relaxed();
        String cleanHtml = Jsoup.clean(bodyHtml, baseUri, whitelist);
        Assert.assertNotNull(cleanHtml);
        Assert.assertFalse(cleanHtml.contains("script"));
        Assert.assertEquals("Image", cleanHtml.substring(cleanHtml.indexOf("<img src=\"") + 6, cleanHtml.indexOf("\" alt=")));
    }

    @Test
    public void testCleanHtmlWithWhitelist() {
        String bodyHtml = "<html><body><img src=\"image.jpg\" alt=\"Image\"><script>alert('Hello World');</script><h1>Hello World</h1></body></html>";
        Whitelist whitelist = Whitelist.simpleText();
        String cleanHtml = Jsoup.clean(bodyHtml, "", whitelist);
        Assert.assertNotNull(cleanHtml);
        Assert.assertEquals("Image\nHello World", cleanHtml);
    }

    @Test
    public void testCleanHtmlWithWhitelistAndOutputSettings() {
        String bodyHtml = "<html><body><img src=\"image.jpg\" alt=\"Image\"><script>alert('Hello World');</script><h1>Hello World</h1></body></html>";
        Whitelist whitelist = Whitelist.simpleText();
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        outputSettings.prettyPrint(true);
        String cleanHtml = Jsoup.clean(bodyHtml, "", whitelist, outputSettings);
        Assert.assertNotNull(cleanHtml);
        Assert.assertEquals("Image\nHello World", cleanHtml);
    }

    @Test
    public void testIsValidHtml() {
        String bodyHtml = "<html><body><h1 id=\"title\">Hello World</h1></body></html>";
        Whitelist whitelist = Whitelist.simpleText();
        boolean isValid = Jsoup.isValid(bodyHtml, whitelist);
        assertTrue(isValid);
    }

    @Test
    public void testIsValidHtmlWithInvalidTag() {
        String bodyHtml = "<html><body><script>Hello World</script></body></html>";
        Whitelist whitelist = Whitelist.simpleText();
        boolean isValid = Jsoup.isValid(bodyHtml, whitelist);
        assertFalse(isValid);
    }
}