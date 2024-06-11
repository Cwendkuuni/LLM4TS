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
    public void testParseHtmlString() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        Document doc = Jsoup.parse(html);
        Assert.assertEquals("Test", doc.title());
        Assert.assertEquals("Hello World", doc.select("h1").text());
    }

    @Test
    public void testParseHtmlStringWithBaseUri() {
        String html = "<img src=\"image.jpg\" />";
        String baseUri = "http://example.com";
        Document doc = Jsoup.parse(html, baseUri);
        Element img = doc.select("img").first();
        Assert.assertEquals("http://example.com/image.jpg", img.attr("src"));
    }

    @Test
    public void testParseHtmlStringWithParser() {
        String html = "<xml><data>Test</data></xml>";
        Parser parser = org.jsoup.parser.XmlParser.xmlParser();
        Document doc = Jsoup.parse(html, "", parser);
        Element data = doc.select("xml > data").first();
        Assert.assertEquals("Test", data.text());
    }

    @Test
    public void testParseFile() throws IOException {
        String filePath = "test.html";
        File file = new File(filePath);
        Document doc = Jsoup.parse(file, "UTF-8");
        Assert.assertEquals("Test", doc.title());
    }

    @Test
    public void testParseInputStream() throws IOException {
        String filePath = "test.html";
        File file = new File(filePath);
        InputStream inputStream = new FileInputStream(file);
        Document doc = Jsoup.parse(inputStream, "UTF-8");
        Assert.assertEquals("Test", doc.title());
    }

    @Test
    public void testConnect() throws IOException {
        String urlString = "http://www.google.com";
        Connection connection = Jsoup.connect(urlString);
        Document doc = connection.get();
        Assert.assertEquals("Google", doc.title());
    }

    @Test
    public void testClean() {
        String html = "<html><head><title>Test</title><script>alert('XSS')</script></head><body><h1>Hello World</h1></body></html>";
        Whitelist whitelist = Whitelist.simpleTextOnly();
        String cleanHtml = Jsoup.clean(html, "", whitelist);
        Document doc = Jsoup.parse(cleanHtml);
        Assert.assertEquals("", doc.select("script").text());
    }

    @Test
    public void testCleanWithBaseUri() {
        String html = "<img src=\"image.jpg\" />";
        Whitelist whitelist = Whitelist.relaxed();
        String cleanHtml = Jsoup.clean(html, "http://example.com", whitelist);
        Document doc = Jsoup.parse(cleanHtml);
        Element img = doc.select("img").first();
        Assert.assertEquals("image.jpg", img.attr("src"));
    }

    @Test
    public void testCleanWithOutputSettings() {
        String html = "<html><head><title>Test</title></head><body><div id=\"test\">&lt;script&gt;alert('XSS')&lt;/script&gt;</div></body></html>";
        Whitelist whitelist = Whitelist.relaxed();
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        outputSettings.prettyPrint();
        String cleanHtml = Jsoup.clean(html, "", whitelist, outputSettings);
        Document doc = Jsoup.parse(cleanHtml);
        Element div = doc.select("div#test").first();
        Assert.assertEquals("", div.text());
    }

    @Test
    public void testIsValid() {
        String html = "<html><head><title>Test</title><script>alert('XSS')</script></head><body><h1>Hello World</h1></body></html>";
        Whitelist whitelist = Whitelist.simpleTextOnly();
        boolean isValid = Jsoup.isValid(html, whitelist);
        Assert.assertFalse(isValid);
    }

    @Test
    public void testParseBodyFragment() {
        String bodyFragment = "<h1>Hello World</h1>";
        Document doc = Jsoup.parseBodyFragment(bodyFragment);
        Assert.assertEquals("Hello World", doc.select("h1").text());
    }

    @Test
    public void testParseBodyFragmentWithBaseUri() {
        String bodyFragment = "<img src=\"image.jpg\" />";
        String baseUri = "http://example.com";
        Document doc = Jsoup.parseBodyFragment(bodyFragment, baseUri);
        Element img = doc.select("img").first();
        Assert.assertEquals("image.jpg", img.attr("src"));
    }

    @Test
    public void testParseUrl() throws IOException {
        String urlString = "http://www.google.com";
        Document doc = Jsoup.parse(new URL(urlString), 5000);
        Assert.assertEquals("Google", doc.title());
    }
}