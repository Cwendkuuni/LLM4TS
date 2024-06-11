package org.jsoup.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;
import org.jsoup.helper.DataUtil;
import org.jsoup.helper.HttpConnection;
import org.jsoup.Connection.Response;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class JsoupTest {

    @Test
    public void testParseHtmlString() {
        String html = "<html><head></head><body><h1>Hello World</h1></body></html>";
        Document document = Jsoup.parse(html);
        assertNotNull(document);
    }

    @Test
    public void testParseHtmlStringWithBaseUri() {
        String html = "<img src=\"image.png\" />";
        String baseUri = "http://example.com";
        Document document = Jsoup.parse(html, baseUri);
        assertNotNull(document);
    }

    @Test
    public void testParseHtmlStringWithParser() {
        String html = "<html><head></head><body><h1>Hello World</h1></body></html>";
        Parser parser = org.jsoup.parser.Jsoup.htmlParser();
        Document document = Jsoup.parse(html, "", parser);
        assertNotNull(document);
    }

    @Test
    public void testParseHtmlStringWithParserAndBaseUri() {
        String html = "<img src=\"image.png\" />";
        String baseUri = "http://example.com";
        Parser parser = org.jsoup.parser.Jsoup.htmlParser();
        Document document = Jsoup.parse(html, baseUri, parser);
        assertNotNull(document);
    }

    @Test
    public void testParseHtmlFile() throws IOException {
        File file = new File("test.html");
        Document document = Jsoup.parse(file, "UTF-8");
        assertNotNull(document);
    }

    @Test
    public void testParseHtmlFileWithBaseUri() throws IOException {
        File file = new File("test.html");
        Document document = Jsoup.parse(file, "UTF-8", "/base/url");
        assertNotNull(document);
    }

    @Test
    public void testParseInputStream() throws IOException {
        InputStream inputStream = this.getClass().getResourceAsStream("/test.html");
        Document document = Jsoup.parse(inputStream, "UTF-8");
        assertNotNull(document);
    }

    @Test
    public void testParseInputStreamWithBaseUri() throws IOException {
        InputStream inputStream = this.getClass().getResourceAsStream("/test.html");
        Document document = Jsoup.parse(inputStream, "UTF-8", "/base/url");
        assertNotNull(document);
    }

    @Test
    public void testParseInputStreamWithParser() throws IOException {
        InputStream inputStream = this.getClass().getResourceAsStream("/test.html");
        Parser parser = org.jsoup.parser.Jsoup.htmlParser();
        Document document = Jsoup.parse(inputStream, "UTF-8", parser);
        assertNotNull(document);
    }

    @Test
    public void testParseInputStreamWithParserAndBaseUri() throws IOException {
        InputStream inputStream = this.getClass().getResourceAsStream("/test.html");
        Parser parser = org.jsoup.parser.Jsoup.htmlParser();
        Document document = Jsoup.parse(inputStream, "UTF-8", "/base/url", parser);
        assertNotNull(document);
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<h1>Hello World</h1>";
        Document document = Jsoup.parseBodyFragment(bodyHtml);
        assertNotNull(document);
    }

    @Test
    public void testParseBodyFragmentWithBaseUri() {
        String bodyHtml = "<img src=\"image.png\" />";
        String baseUri = "http://example.com";
        Document document = Jsoup.parseBodyFragment(bodyHtml, baseUri);
        assertNotNull(document);
    }

    @Test
    public void testParseBodyFragmentWithParser() {
        String bodyHtml = "<h1>Hello World</h1>";
        Parser parser = org.jsoup.parser.Jsoup.htmlParser();
        Document document = Jsoup.parseBodyFragment(bodyHtml, parser);
        assertNotNull(document);
    }

    @Test
    public void testConnect() {
        String url = "http://example.com";
        Connection connection = Jsoup.connect(url);
        assertNotNull(connection);
    }

    @Test(expected = IOException.class)
    public void testInvalidUrl() {
        String url = "file://test.html";
        Jsoup.connect(url);
    }

    @Test
    public void testParseUrl() throws IOException {
        String url = "http://example.com";
        Document document = Jsoup.parse(url);
        assertNotNull(document);
    }

    @Test(expected = IOException.class)
    public void testInvalidUrlParse() throws IOException {
        String url = "file://test.html";
        Jsoup.parse(url);
    }

    @Test
    public void testClean() {
        String bodyHtml = "<script src=\"script.js\"></script><div id=\"test\">Hello World</div>";
        Whitelist whitelist = Whitelist.simpleText();
        String cleanHtml = Jsoup.clean(bodyHtml, "", whitelist);
        assertNotNull(cleanHtml);
        assertThrows(IllegalArgumentException.class, () -> Jsoup.clean(bodyHtml, "", null));
    }

    @Test
    public void testCleanWithBaseUri() {
        String bodyHtml = "<img src=\"image.png\" />";
        String baseUri = "http://example.com";
        Whitelist whitelist = Whitelist.simpleText();
        String cleanHtml = Jsoup.clean(bodyHtml, baseUri, whitelist);
        assertNotNull(cleanHtml);
    }

    @Test
    public void testCleanWithParser() {
        String bodyHtml = "<script src=\"script.js\"></script><div id=\"test\">Hello World</div>";
        Parser parser = org.jsoup.parser.Jsoup.htmlParser();
        Whitelist whitelist = Whitelist.simpleText();
        Document document = Jsoup.parseBodyFragment(bodyHtml, parser);
        String cleanHtml = Jsoup.clean(document.body(), "", whitelist).text();
        assertNotNull(cleanHtml);
    }

    @Test
    public void testCleanWithBaseUriAndParser() {
        String bodyHtml = "<img src=\"image.png\" />";
        String baseUri = "http://example.com";
        Parser parser = org.jsoup.parser.Jsoup.htmlParser();
        Whitelist whitelist = Whitelist.simpleText();
        Document document = Jsoup.parse(bodyHtml, baseUri, parser);
        String cleanHtml = Jsoup.clean(document.body(), "", whitelist).text();
        assertNotNull(cleanHtml);
    }

    @Test
    public void testCleanWithOutputSettings() {
        String bodyHtml = "<script src=\"script.js\"></script><div id=\"test\">Hello World</div>";
        Whitelist whitelist = Whitelist.simpleText();
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        outputSettings.prettyPrint(true);
        String cleanHtml = Jsoup.clean(bodyHtml, "", whitelist, outputSettings).text();
        assertNotNull(cleanHtml);
    }

    @Test
    public void testCleanWithBaseUriAndOutputSettings() {
        String bodyHtml = "<img src=\"image.png\" />";
        String baseUri = "http://example.com";
        Whitelist whitelist = Whitelist.simpleText();
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        outputSettings.prettyPrint(true);
        Document document = Jsoup.parse(bodyHtml, baseUri);
        String cleanHtml = Jsoup.clean(document.body(), "", whitelist, outputSettings).text();
        assertNotNull(cleanHtml);
    }

    @Test
    public void testCleanWithParserAndOutputSettings() {
        String bodyHtml = "<script src=\"script.js\"></script><div id=\"test\">Hello World</div>";
        Parser parser = org.jsoup.parser.Jsoup.htmlParser();
        Whitelist whitelist = Whitelist.simpleText();
        Document document = Jsoup.parseBodyFragment(bodyHtml, parser);
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        outputSettings.prettyPrint(true);
        String cleanHtml = Jsoup.clean(document.body(), "", whitelist, outputSettings).text();
        assertNotNull(cleanHtml);
    }

    @Test
    public void testCleanWithBaseUriParserAndOutputSettings() {
        String bodyHtml = "<img src=\"image.png\" />";
        String baseUri = "http://example.com";
        Parser parser = org.jsoup.parser.Jsoup.htmlParser();
        Whitelist whitelist = Whitelist.simpleText();
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        outputSettings.prettyPrint(true);
        Document document = Jsoup.parse(bodyHtml, baseUri, parser);
        String cleanHtml = Jsoup.clean(document.body(), "", whitelist, outputSettings).text();
        assertNotNull(cleanHtml);
    }

    @Test
    public void testIsValid() {
        String bodyHtml = "<html><head></head><body><h1 id=\"test\">Hello World</h1></body></html>";
        Whitelist whitelist = Whitelist.simpleText();
        boolean isValid = Jsoup.isValid(bodyHtml, whitelist);
        assertTrue(isValid);
    }

    @Test
    public void testIsValidWithInvalidTag() {
        String bodyHtml = "<html><head></head><body><script id=\"test\">Hello World</script></body></html>";
        Whitelist whitelist = Whitelist.simpleText();
        boolean isValid = Jsoup.isValid(bodyHtml, whitelist);
        assertFalse(isValid);
    }

    @Test
    public void testIsValidWithInvalidAttribute() {
        String bodyHtml = "<html><head></head><body><button onclick=\"function()\">Hello World</button></body></html>";
        Whitelist whitelist = Whitelist.simpleText().addTag("button").addAttribute("button", "type", "button");
        boolean isValid = Jsoup.isValid(bodyHtml, whitelist);
        assertFalse(isValid);
    }
}