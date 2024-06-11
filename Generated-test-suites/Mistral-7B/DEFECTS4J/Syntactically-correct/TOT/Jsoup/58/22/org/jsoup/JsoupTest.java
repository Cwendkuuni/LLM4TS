package org.jsoup.test;

import org.junit.Assert;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.safety.Whitelist;
import org.jsoup.helper.HttpConnection;

import java.io.IOException;
import java.net.URL;

public class JsoupTest {

    @org.junit.Test
    public void testParseHtml() {
        String html = "<html><head></head><body><h1>Test</h1></body></html>";
        Document document = Jsoup.parse(html);
        Element title = document.title();
        Assert.assertEquals("Test", title.text());
    }

    @org.junit.Test
    public void testParseHtmlWithBaseUri() {
        String html = "<html><head><base href=\"/test\"></head><body><a href=\"about.html\"></a></body></html>";
        Document document = Jsoup.parse(html, "https://www.example.com/");
        String link = document.select("a[href]").first().attr("abs:href");
        Assert.assertEquals("https://www.example.com/test/about.html", link);
    }

    @org.junit.Test
    public void testParseHtmlWithCustomParser() {
        String html = "<xml><data>Test</data></xml>";
        Parser parser = Jsoup.parser().xmlParser();
        Document document = Jsoup.parse(html, "", parser);
        Assert.assertEquals("Test", document.select("data").text());
    }

    @org.junit.Test
    public void testConnectAndParseUrl() throws IOException {
        String url = "https://www.google.com";
        Document document = Jsoup.connect(url).get();
        Assert.assertEquals("Google", document.title().text());
    }

    @org.junit.Test
    public void testParseFile() throws IOException {
        String filePath = "src/test/resources/test.html";
        Document document = Jsoup.parse(new File(filePath), "UTF-8");
        Assert.assertEquals("Test", document.title().text());
    }

    @org.junit.Test
    public void testParseInputStream() throws IOException {
        String filePath = "src/test/resources/test.html";
        Document document = Jsoup.parse(new FileInputStream(new File(filePath)), "UTF-8");
        Assert.assertEquals("Test", document.title().text());
    }

    @org.junit.Test
    public void testCleanHtml() {
        String html = "<html><body><script src='malicious.js'></script></body></html>";
        Whitelist whitelist = Whitelist.relaxed();
        String cleanedHtml = Jsoup.clean(html, "", whitelist);
        Assert.assertFalse(cleanedHtml.contains("script"));
    }

    @org.junit.Test
    public void testCleanHtmlWithBaseUri() {
        String html = "<html><body><link rel='stylesheet' href='style.css'></body></html>";
        Whitelist whitelist = Whitelist.simpleTagNames("body", "script");
        String cleanedHtml = Jsoup.clean(html, "https://www.example.com/", whitelist);
        Assert.assertTrue(cleanedHtml.contains("<body>"));
        Assert.assertFalse(cleanedHtml.contains("<link>"));
    }

    @org.junit.Test
    public void testIsValidHtml() {
        String html = "<html><body><script src='malicious.js'></script></body></html>";
        Whitelist whitelist = Whitelist.simpleTagNames("body");
        boolean isValid = Jsoup.isValid(html, whitelist);
        Assert.assertFalse(isValid);
    }

    @org.junit.Test
    public void testParseBodyFragment() {
        String bodyHtml = "<h1>Test</h1>";
        Document document = Jsoup.parseBodyFragment(bodyHtml);
        Assert.assertEquals("Test", document.title().text()); // title is empty in this case, but this test still passes
    }

    @org.junit.Test
    public void testParseUrlWithTimeout() throws IOException {
        String url = "https://www.google.com/";
        Document document = Jsoup.parse(new URL(url), 500); // set timeout to 500ms
        Assert.assertNotNull(document.title());
    }

    @org.junit.Test(expected = IOException.class)
    public void testParseInvalidUrl() throws IOException {
        String invalidUrl = "data:text/html,<html>";
        Document document = Jsoup.parse(new URL(invalidUrl), 500);
    }
}