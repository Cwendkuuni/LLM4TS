package org.jsoup.test;

import static org.junit.Assert.assertNotNull;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.safety.Whitelist;
import org.junit.Test;

/**
 * JsoupTest class to test all methods in the Jsoup class
 */
public class JsoupTest {

 @Test
 public void testParse() {
 String html = "<html><head><title>Test Page</title></head><body><h1>Hello World</h1></body></html>";
 Document doc = Jsoup.parse(html);
 assertNotNull(doc);
 }

 @Test
 public void testParseWithBaseUri() {
 String html = "<img src=\"image.jpg\" alt=\"Image\">";
 String baseUri = "http://example.com";
 Document doc = Jsoup.parse(html, baseUri);
 Element img = doc.select("img").first();
 assertNotNull(img);
 assertNotNull(img.attr("src"));
 assertEquals("image.jpg", img.attr("src"));
 }

 @Test
 public void testParseWithParser() {
 Parser parser = new Parser.MalformedEncodingParser();
 String html = "<html><head><title>Test Page</title></head><body><h1>Hello World</h1></body></html>";
 Document doc = Jsoup.parse(html, "", parser);
 assertNotNull(doc);
 }

 @Test
 public void testConnect() {
 String url = "http://example.com";
 Document doc = Jsoup.connect(url).get();
 assertNotNull(doc);
 }

 @Test
 public void testParseFile() throws IOException {
 String filePath = "test.html"; // path to test file
 Document doc = Jsoup.parse(new File(filePath), null);
 assertNotNull(doc);
 }

 @Test
 public void testParseInputStream() throws IOException {
 String html = "<html><head><title>Test Page</title></head><body><h1>Hello World</h1></body></html>";
 InputStream inputStream = new ByteArrayInputStream(html.getBytes());
 Document doc = Jsoup.parse(inputStream, null);
 assertNotNull(doc);
 }

 @Test
 public void testParseBodyFragment() {
 String bodyHtml = "<h1>Hello World</h1>";
 Document doc = Jsoup.parseBodyFragment(bodyHtml);
 assertNotNull(doc);
 }

 @Test
 public void testParseUrl() throws IOException {
 String url = "http://example.com";
 Document doc = Jsoup.parse(new URL(url), 5000);
 assertNotNull(doc);
 }

 @Test
 public void testClean() {
 String html = "<html><head><title>Test Page</title><script>alert('Hello World');</script></head><body><h1 id='title'>Hello World</h1><script>alert('Hello World');</script></body></html>";
 Whitelist whitelist = Whitelist.relaxed();
 String cleanHtml = Jsoup.clean(html, "", whitelist);
 Document doc = Jsoup.parse(cleanHtml);
 Element title = doc.select("h1#title").first();
 assertNotNull(title);
 assertEquals("Hello World", title.text());
 }

 @Test
 public void testCleanWithBaseUri() {
 String html = "<img src=\"image.jpg\" alt=\"Image\">";
 String baseUri = "http://example.com";
 Whitelist whitelist = Whitelist.relaxed();
 String cleanHtml = Jsoup.clean(html, baseUri, whitelist);
 Document doc = Jsoup.parse(cleanHtml);
 Element img = doc.select("img").first();
 assertNotNull(img);
 assertEquals("image.jpg", img.attr("src"));
 }

 @Test
 public void testCleanWithOutputSettings() {
 String html = "<html><head><title>Test Page</title></head><body><h1 id='title'>Hello World</h1><script>alert('Hello World');</script></body></html>";
 Whitelist whitelist = Whitelist.relaxed();
 Document.OutputSettings outputSettings = new Document.OutputSettings();
 outputSettings.prettyPrint(true);
 String cleanHtml = Jsoup.clean(html, "", whitelist, outputSettings);
 Document doc = Jsoup.parse(cleanHtml);
 Element title = doc.select("h1#title").first();
 assertNotNull(title);
 assertEquals("Hello World", title.text());
 }

 @Test
 public void testIsValid() {
 String html = "<html><head><title>Test Page</title><script>alert('Hello World');</script></head><body><h1 id='title'>Hello World</h1><script>alert('Hello World');</script></body></html>";
 Whitelist whitelist = Whitelist.relaxed();
 boolean isValid = Jsoup.isValid(html, whitelist);
 assertTrue(isValid);
 }
}