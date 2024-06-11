package org.jsoup.test;

import org.junit.Assert;import org.junit.Test;import org.jsoup.Jsoup;import org.jsoup.nodes.Document;import org.jsoup.nodes.Element;import org.jsoup.parser.Parser;import org.jsoup.safety.Whitelist;import org.jsoup.helper.DataUtil;import org.jsoup.helper.HttpConnection;

import java.io.File;import java.io.IOException;import java.io.InputStream;import java.net.URL;

/**
 * JsoupTest class to test Jsoup methods
 */
public class JsoupTest {

    @Test
    public void testParse() {
        String html = "<html><head></head><body><h1>Test</h1></body></html>";
        Document document1 = Jsoup.parse(html);
        Assert.assertNotNull(document1);

        String htmlWithBaseUri = "<html><head></head><body><h1>Test</h1><a href=\"/test\">Link</a></body></html>";
        Document document2 = Jsoup.parse(htmlWithBaseUri, "http://example.com");
        Element link = document2.selectFirst("a");
        Assert.assertNotNull(link);
        Assert.assertEquals("Link", link.text());
        Assert.assertEquals("http://example.com/test", link.absUrl(""));
    }

    @Test
    public void testParseWithParser() {
        String html = "<html><head></head><body><h1>Test</h1></body></html>";
        Parser parser = Jsoup.parser();
        Document document = parser.parseInput(html, "");
        Assert.assertNotNull(document);
    }

    @Test
    public void testParseEmpty() {
        Document document = Jsoup.parse("");
        Assert.assertNotNull(document);
    }

    @Test
    public void testConnect() throws IOException {
        String url = "http://example.com";
        Document document = Jsoup.connect(url).get();
        Assert.assertNotNull(document);
    }

    @Test
    public void testParseFile() throws IOException {
        String filePath = "src/test/resources/test.html";
        Document document = Jsoup.parse(new File(filePath), "UTF-8");
        Assert.assertNotNull(document);
    }

    @Test
    public void testParseInputStream() throws IOException {
        String fileName = "test.html";
        InputStream inputStream = getClass().getResourceAsStream(fileName);
        Document document = Jsoup.parse(inputStream, "UTF-8");
        Assert.assertNotNull(document);
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<h1>Test</h1>";
        Document document = Jsoup.parseBodyFragment(bodyHtml);
        Assert.assertNotNull(document);
        Assert.assertEquals("<h1>Test</h1>", document.body().html());
    }

    @Test
    public void testClean() {
        String bodyHtml = "<html><body><script>alert('XSS')</script><h1>Test</h1></body></html>";
        Whitelist whitelist = Whitelist.basic();
        String cleanHtml = Jsoup.clean(bodyHtml, "", whitelist);
        Assert.assertNotNull(cleanHtml);
        Assert.assertFalse(cleanHtml.contains("script"));
        Assert.assertTrue(cleanHtml.contains("h1"));
        Document document = Jsoup.parse(cleanHtml);
        Assert.assertNotNull(document);
        Assert.assertEquals(1, document.select("h1").size());
    }

    @Test
    public void testIsValid() {
        String bodyHtml = "<html><body><input type=\"text\" name=\"input\"><script>alert('XSS')</script></body></html>";
        Whitelist whitelist = Whitelist.simpleTagNames("html", "head", "body", "input");
        boolean isValid = Jsoup.isValid(bodyHtml, whitelist);
        Assert.assertFalse(isValid);
    }
}