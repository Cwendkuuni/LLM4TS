package org.jsoup.test;

import org.junit.Assert; import org.jsoup.Jsoup; import org.jsoup.helper.HttpConnection;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.core.StringStartsWith;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class JsoupTest {

 @org.junit.Test public void testParseHtmlString() {
 String html = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
 Document doc = Jsoup.parse(html);
 assertNotNull(doc);
 assertEquals("Test", doc.title());
 assertEquals("Hello World", doc.select("h1").text());
 }

 @org.junit.Test public void testParseHtmlStringWithBaseUri() {
 String html = "<img src=\"image.jpg\" />";
 String baseUri = "http://example.com";
 Document doc = Jsoup.parse(html, baseUri);
 assertEquals("http://example.com/image.jpg", doc.select("img").attr("src"));
 }

 @org.junit.Test public void testParseHtmlStringWithParser() {
 String html = "<xml><data>Test</data></xml>";
 Parser parser = org.jsoup.parser.XmlParser.xmlParser();
 Document doc = Jsoup.parse(html, "", parser);
 assertEquals("Test", doc.select("xml > data").text());
 }

 @org.junit.Test public void testParseHtmlFile() throws IOException {
 File inputFile = new File("input.html");
 Document doc = Jsoup.parse(inputFile, "UTF-8");
 assertNotNull(doc);
 }

 @org.junit.Test public void testParseHtmlInputStream() throws IOException {
 InputStream inputStream = this.getClass().getResourceAsStream("/input.html");
 Document doc = Jsoup.parse(inputStream, "UTF-8");
 assertNotNull(doc);
 }

 @org.junit.Test public void testParseHtmlUrl() throws IOException {
 URL url = new URL("http://example.com/index.html");
 Document doc = Jsoup.parse(url, 3000);
 assertNotNull(doc);
 }

 @org.junit.Test public void testConnectUrl() {
 String url = "http://example.com";
 Connection connection = Jsoup.connect(url);
 assertNotNull(connection);
 }

 @org.junit.Test public void testConnectUrlWithUserAgent() {
 String url = "http://example.com";
 Connection connection = Jsoup.connect(url).userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3094.110 Safari/56.0");
 assertNotNull(connection);
 }

 @org.junit.Test public void testConnectUrlWithErrorHandler() {
 String url = "http://example.com/404";
 Connection connection = Jsoup.connect(url).errorHandler(new Connection.Response.ErrorHandler() {
 @Override public void handleErrorResponse(Response response) {
 assertEquals(HttpStatus.NOT_FOUND.getStatusCode(), response.statusCode());
 }
 });
 connection.get();
 }

 @org.junit.Test public void testCleanHtml() {
 String html = "<html><head><script src=\"malicious.js\"></script></head><body><h1>Hello World</h1></body></html>";
 Whitelist whitelist = Whitelist.relaxed();
 String cleanedHtml = Jsoup.clean(html, "", whitelist);
 assertFalse(cleanedHtml.contains("<script>"));
 }

 @org.junit.Test public void testCleanHtmlWithBaseUri() {
 String html = "<img src=\"image.jpg\" />";
 String baseUri = "http://example.com";
 String cleanedHtml = Jsoup.clean(html, baseUri, Whitelist.basic());
 assertEquals("", cleanedHtml.substring(cleanedHtml.indexOf("src=\"") + 5, cleanedHtml.length()).replaceAll("/", "%2F"));
 }

 @org.junit.Test public void testCleanHtmlWithOutputSettings() {
 String html = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
 Document dirty = Jsoup.parseBodyFragment(html, "");
 Document clean = Jsoup.clean(dirty.body().html(), Whitelist.relaxed(), new Document.OutputSettings().prettyPrint(true).escapeMode(Document.OutputSettings.EscapeMode.html));
 assertEquals(html, clean.body().html());
 }
}