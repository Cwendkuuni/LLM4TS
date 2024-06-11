package org.jsoup.test;

import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.jsoup.safety.Whitelist;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

/**
 * JsoupTest class to test all methods in Jsoup class
 */
public class JsoupTest {

 @Test
 public void testParseString() {
 String html = "<html><head><title>Test</title></head><body><h1>Hello, World!</h1></body></html>";
 Document doc = Jsoup.parse(html);
 assertNotNull(doc);
 assertEquals("Hello, World!", doc.select("h1").text());
 }

 @Test
 public void testParseStringWithBaseUri() {
 String html = "<img src=\"image.jpg\" />";
 String baseUri = "http://example.com";
 Document doc = Jsoup.parse(html, baseUri);
 assertEquals("image.jpg", doc.select("img[src]").attr("src"));
 }

 @Test
 public void testParseStringWithParser() {
 String html = "<html><head><title>Test</title></head><body><h1>Hello, World!</h1></body></html>";
 Parser parser = Jsoup.parser();
 Document doc = parser.parseInput(html, "");
 assertNotNull(doc);
 assertEquals("Hello, World!", doc.select("h1").text());
 }

 @Test
 public void testParseStringWithBaseUriAndParser() {
 String html = "<img src=\"image.jpg\" />";
 String baseUri = "http://example.com";
 Parser parser = Jsoup.parser();
 Document doc = parser.parseInput(html, baseUri);
 assertEquals("image.jpg", doc.select("img[src]").attr("src"));
 }

 @Test
 public void testParseFile() throws Exception {
 File file = new File("test.html");
 Document doc = Jsoup.parse(file, "UTF-8");
 assertNotNull(doc);
 }

 @Test
 public void testParseFileWithBaseUri() throws Exception {
 File file = new File("test.html");
 Document doc = Jsoup.parse(file, "UTF-8", "/baseurl");
 }

 @Test
 public void testParseInputStream() throws Exception {
 InputStream in = getClass().getResourceAsStream("/test.html");
 Document doc = Jsoup.parse(in, "UTF-8");
 assertNotNull(doc);
 }

 @Test
 public void testParseInputStreamWithBaseUri() throws Exception {
 InputStream in = getClass().getResourceAsStream("/test.html");
 Document doc = Jsoup.parse(in, "UTF-8", "/baseurl");
 }

 @Test
 public void testParseInputStreamWithParser() throws Exception {
 InputStream in = getClass().getResourceAsStream("/test.html");
 Parser parser = Jsoup.parser();
 Document doc = parser.parseInput(in, "UTF-8");
 assertNotNull(doc);
 }

 @Test
 public void testParseInputStreamWithBaseUriAndParser() throws Exception {
 InputStream in = getClass().getResourceAsStream("/test.html");
 Parser parser = Jsoup.parser();
 Document doc = parser.parseInput(in, "UTF-8", "/baseurl");
 }

 @Test
 public void testParseBodyFragment() {
 String html = "<h1>Hello, World!</h1>";
 Document doc = Jsoup.parseBodyFragment(html);
 assertNotNull(doc);
 assertEquals("Hello, World!", doc.select("h1").text());
 }

 @Test
 public void testParseBodyFragmentWithBaseUri() {
 String html = "<img src=\"image.jpg\" />";
 String baseUri = "http://example.com";
 Document doc = Jsoup.parseBodyFragment(html, baseUri);
 assertEquals("image.jpg", doc.select("img[src]").attr("src"));
 }

 @Test
 public void testParseUrl() throws IOException {
 Document doc = Jsoup.parse(new URL("http://example.com"), 5000);
 }

 @Test
 public void testClean() throws IOException {
 String html = "<script>alert('XSS');</script><p>Hello, World!</p>";
 String baseUri = "http://example.com";
 Whitelist whitelist = Whitelist.simpleTagNames("p");
 Document doc = Jsoup.parseBodyFragment(html, baseUri);
 String cleanHtml = Jsoup.clean(doc.body().html(), baseUri, whitelist);
 assertNotNull(cleanHtml);
 assertEquals("Hello, World!", cleanHtml);
 }

 @Test
 public void testCleanWithWhitelist() throws IOException {
 String html = "<script>alert('XSS');</script><p>Hello, World!</p>";
 Whitelist whitelist = Whitelist.simpleTagNames("p");
 String cleanHtml = Jsoup.clean(html, whitelist);
 assertNotNull(cleanHtml);
 assertEquals("Hello, World!", cleanHtml);
 }

 @Test
 public void testCleanWithWhitelistAndBaseUri() throws IOException {
 String html = "<script src=\"script.js\"></script><p>Hello, World!</p>";
 String baseUri = "http://example.com";
 Whitelist whitelist = Whitelist.simpleTagNames("p");
 Document doc = Jsoup.parseBodyFragment(html, baseUri);
 String cleanHtml = Jsoup.clean(doc.body().html(), baseUri, whitelist);
 assertNotNull(cleanHtml);
 assertEquals("Hello, World!", cleanHtml);
 }

 @Test
 public void testIsValid() throws IOException {
 String html = "<script>alert('XSS');</script><p>Hello, World!</p>";
 Whitelist whitelist = Whitelist.simpleTagNames("p");
 boolean isValid = Jsoup.isValid(html, whitelist);
 assertFalse(isValid);
 }
}