package org.jsoup.test;

import org.junit.Test;
import org.junit.Assert;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.safety.Whitelist;
import org.jsoup.helper.DataUtil;
import java.io.IOException;
import static org.junit.Assert.*;

public class JsoupTest {

 @Test
 public void testParse() {
 String html = "<html><head></head><body><h1>Hello World</h1></body></html>";
 Document doc = Jsoup.parse(html);
 assertEquals("Hello World", doc.title());
 assertEquals("Hello World", doc.select("h1").text());
 }

 @Test
 public void testParseWithBaseUri() {
 String html = "<img src='image.jpg' alt='Image'>";
 String baseUri = "http://example.com";
 Document doc = Jsoup.parse(html, baseUri);
 Assert.assertEquals("image.jpg", doc.select("img[src]").attr("src"));
 }

 @Test
 public void testParseWithParser() {
 String html = "<html><head></head><body><h1>Hello World</h1></body></html>";
 Parser parser = new Parser.MalformedEncodingParser();
 Document doc = Jsoup.parse(html, "", parser);
 assertEquals("Hello World", doc.title());
 assertEquals("Hello World", doc.select("h1").text());
 }

 @Test
 public void testParseWithEmptyBaseUri() {
 String html = "<html><head></head><body><h1>Hello World</h1></body></html>";
 Document doc = Jsoup.parse(html);
 assertEquals("", doc.baseUri());
 }

 @Test
 public void testConnect() throws IOException {
 String url = "http://example.com";
 Document doc = Jsoup.connect(url).get();
 assertEquals(url, doc.baseUri());
 }

 @Test
 public void testParseFileWithCharset() throws IOException {
 String charsetName = "UTF-8";
 File file = new File("test.html");
 Document doc = Jsoup.parse(file, charsetName);
 assertEquals("Test", doc.title());
 }

 @Test
 public void testParseFile() throws IOException {
 File file = new File("test.html");
 Document doc = Jsoup.parse(file);
 assertEquals("Test", doc.title());
 }

 @Test
 public void testParseInputStreamWithCharset() throws IOException {
 InputStream inputStream = getClass().getResourceAsStream("/test.html");
 String charsetName = "UTF-8";
 Document doc = Jsoup.parse(inputStream, charsetName);
 assertEquals("Test", doc.title());
 }

 @Test
 public void testParseInputStream() throws IOException {
 InputStream inputStream = getClass().getResourceAsStream("/test.html");
 Document doc = Jsoup.parse(inputStream);
 assertEquals("Test", doc.title());
 }

 @Test
 public void testParseBodyFragment() {
 String bodyHtml = "<h1>Hello World</h1>";
 Document doc = Jsoup.parseBodyFragment(bodyHtml);
 assertEquals("Hello World", doc.title());
 assertEquals("Hello World", doc.select("h1").text());
 }

 @Test
 public void testParseBodyFragmentWithBaseUri() {
 String bodyHtml = "<img src='image.jpg' alt='Image'>";
 String baseUri = "http://example.com";
 Document doc = Jsoup.parseBodyFragment(bodyHtml, baseUri);
 assertEquals("image.jpg", doc.select("img[src]").attr("src"));
 }

 @Test
 public void testParseUrl() throws IOException {
 String url = "http://example.com";
 Document doc = Jsoup.parse(url);
 assertEquals(url, doc.baseUri());
 }

 @Test
 public void testClean() {
 String html = "<script src='script.js'></script><h1>Hello World<img src='image.jpg' alt='Image'> <a href='link.html'>Link</a></h1>";
 String baseUri = "http://example.com";
 Whitelist whitelist = Whitelist.relaxed();
 String cleanHtml = Jsoup.clean(html, baseUri, whitelist);
 assertEquals("<h1>Hello World<a href=\"link.html\">Link</a></h1>", cleanHtml);
 }

 @Test
 public void testCleanWithEmptyBaseUri() {
 String html = "<script src='script.js'></script><h1>Hello World<img src='image.jpg' alt='Image'> <a href='link.html'>Link</a></h1>";
 Whitelist whitelist = Whitelist.relaxed();
 String cleanHtml = Jsoup.clean(html, whitelist);
 assertEquals("<h1></h1>", cleanHtml);
 }

 @Test
 public void testCleanWithWhitelist() {
 String html = "<script src='script.js'></script><h1>Hello World<img src='image.jpg' alt='Image'> <a href='link.html'>Link</a></h1>";
 Whitelist whitelist = Whitelist.simpleText();
 String cleanHtml = Jsoup.clean(html, whitelist);
 assertEquals("", cleanHtml);
 }

 @Test
 public void testIsValid() {
 String html = "<script src='script.js'></script><h1>Hello World<img src='image.jpg' alt='Image'> <a href='link.html'>Link</a></h1>";
 Whitelist whitelist = Whitelist.relaxed();
 boolean valid = Jsoup.isValid(html, whitelist);
 assertFalse(valid);
 }

 @Test
 public void testIsValidWithWhitelist() {
 String html = "<h1>Hello World<img src='image.jpg' alt='Image'> <a href='link.html'>Link</a></h1>";
 Whitelist whitelist = Whitelist.simpleText();
 boolean valid = Jsoup.isValid(html, whitelist);
 assertTrue(valid);
 }
}