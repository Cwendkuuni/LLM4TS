package org.jsoup.test;

import org.junit.Assert;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.safety.Whitelist;
import org.jsoup.helper.HttpConnection;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class JsoupTest {

    @org.junit.Test
    public void testParseHtmlWithBaseUri() {
        String html = "<html><head><base href=\"http://example.com\"></base></head><body><a href=\"about.html\">About</a></body></html>";
        Document document = Jsoup.parse(html, "http://example.com");
        Element link = document.select("a").first();
        String linkHref = link.attr("href");
        Assert.assertEquals("about.html", linkHref);
    }

    @org.junit.Test
    public void testParseHtmlWithCustomParser() {
        String html = "<xml><tag>Hello World</tag></xml>";
        Parser parser = Jsoup.parser().xmlParser();
        Document document = Jsoup.parse(html, "", parser);
        Element tag = document.select("tag").first();
        String tagText = tag.text();
        Assert.assertEquals("Hello World", tagText);
    }

    @org.junit.Test
    public void testParseHtmlWithoutBaseUri() {
        String html = "<html><base href=\"http://example.com\"></base><body><a href=\"about.html\">About</a></body></html>";
        Document document = Jsoup.parse(html);
        Element link = document.select("a").first();
        String linkHref = link.attr("href");
        Assert.assertEquals("about.html", linkHref);
    }

    @org.junit.Test
    public void testConnectAndParse() throws IOException {
        String url = "http://example.com";
        Document document = Jsoup.connect(url).get();
        String title = document.title();
        Assert.assertNotNull(title);
    }

    @org.junit.Test
    public void testParseFile() throws IOException {
        String fileName = "test.html";
        File file = new File(fileName);
        String charsetName = "UTF-8";
        Document document = Jsoup.parse(file, charsetName);
        String title = document.title();
        Assert.assertNotNull(title);
    }

    @org.junit.Test
    public void testParseInputStream() throws IOException {
        String fileName = "test.html";
        File file = new File(fileName);
        InputStream inputStream = new FileInputStream(file);
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document document = Jsoup.parse(inputStream, charsetName, baseUri);
        String title = document.title();
        Assert.assertNotNull(title);
    }

    @org.junit.Test
    public void testParseBodyFragment() {
        String bodyHtml = "<p>Hello World</p>";
        Document document = Jsoup.parseBodyFragment(bodyHtml);
        String text = document.text();
        Assert.assertEquals("Hello World", text);
    }

    @org.junit.Test
    public void testCleanHtml() {
        String bodyHtml = "<p><script>alert('XSS');</script>Hello World</p>";
        Whitelist whitelist = Whitelist.simpleTextOnly();
        String cleanHtml = Jsoup.clean(bodyHtml, "", whitelist);
        Assert.assertEquals("Hello World", cleanHtml);
    }

    @org.junit.Test
    public void testIsValidHtml() {
        String bodyHtml = "<p><script>alert('XSS');</script>Hello World</p>";
        Whitelist whitelist = Whitelist.simpleTextOnly();
        boolean isValid = Jsoup.isValid(bodyHtml, whitelist);
        Assert.assertFalse(isValid);
    }
}