package org.jsoup.test;

import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;
import org.jsoup.helper.DataUtil;
import java.io.File;
import java.io.IOException;
import static org.junit.Assert.*;

public class JsoupTest {

 @Test
 public void testParse() throws IOException {
 String html = "<html><head></head><body><h1>Hello World</h1></body></html>";
 Document doc = Jsoup.parse(html);
 assertEquals("Hello World", doc.title());
assertEquals(1, doc.body().childNodeSize());
 assertEquals("h1", doc.body().child(0).tagName());
 }

 @Test
 public void testParse_BaseUri() throws IOException {
 String html = "<html><head></head><body><img src=\"image.jpg\"></body></html>";
 Document doc = Jsoup.parse(html, "http://example.com");
 Element img = doc.selectFirst("img");
 assertEquals("http://example.com/image.jpg", img.attr("src"));
 }

 @Test
 public void testParse_Parser() throws IOException {
 Parser parser = new Parser();
 String html = "<html><head></head><body><h1>Hello World</h1></body></html>";
 Document doc = Jsoup.parse(html, "", parser);
 assertEquals("Hello World", doc.title());
assertEquals(1, doc.body().childNodeSize());
 assertEquals("h1", doc.body().child(0).tagName());
 }

 @Test
 public void testParse_EmptyBaseUri() throws IOException {
 String html = "<html><head></head><body><base href=\"/test\"></body></html><html><head></head><body><a href=\"image.jpg\"></a></body></html>";
 Document doc1 = Jsoup.parse(html.substring(0, html.indexOf("</html>")), null);
 Document doc2 = Jsoup.parse(html.substring(html.indexOf("</html>") + "</html>".length()), null);
 assertEquals("/test/image.jpg", doc2.selectFirst("a").attr("href"));
 }

 @Test
 public void testConnect() throws IOException {
 String url = "http://example.com";
 Document doc = Jsoup.connect(url).get();
 assertEquals(200, doc.statusCode());
 assertEquals("Example Domain", doc.title());
 }

 @Test
 public void testParseFile() throws IOException {
 File file = new File("test.html");
 Document doc = Jsoup.parse(file, null);
 assertEquals("Test Page", doc.title());
 }

 @Test
 public void testParseFile_Charset() throws IOException {
 File file = new File("test.html");
 Document doc = Jsoup.parse(file, "UTF-8", null);
 assertEquals("Test Page", doc.title());
 }

 @Test
 public void testParseInputStream() throws IOException {
 InputStream in = getClass().getResourceAsStream("/test.html");
 Document doc = Jsoup.parse(in, "UTF-8", null);
 assertEquals("Test Page", doc.title());
 }

 @Test
 public void testParseInputStream_CharsetAndBaseUri() throws IOException {
 InputStream in = getClass().getResourceAsStream("/test.html");
 Document doc = Jsoup.parse(in, "UTF-8", "http://example.com");
 Element img = doc.selectFirst("img");
 assertEquals("http://example.com/image.jpg", img.attr("src"));
 }

 @Test
 public void testParseBodyFragment() throws IOException {
 String bodyHtml = "<h1>Hello World</h1>";
 Document doc = Jsoup.parseBodyFragment(bodyHtml, "");
 assertEquals("Hello World", doc.title());
 assertEquals(1, doc.body().childNodeSize());
 assertEquals("h1", doc.body().child(0).tagName());
 }

 @Test
 public void testParseUrl() throws IOException {
 URL url = new URL("http://example.com");
 Document doc = Jsoup.parse(url, 5000);
 assertEquals(200, doc.statusCode());
 assertEquals("Example Domain", doc.title());
 }

 @Test
 public void testClean() {
 String bodyHtml = "<html><body><script>alert('Hello');</script><p>Test</p></body></html>";
 Whitelist whitelist = Whitelist.simpleTagNames("p");
 String cleanedHtml = Jsoup.clean(bodyHtml, "", whitelist);
 assertFalse(cleanedHtml.contains("<script>"));
 assertEquals(1, DataUtil.getElementsByTagName(cleanedHtml, "p").size());
 }

 @Test
 public void testClean_OutputSettings() {
 String bodyHtml = "<html><body><script>alert('Hello');</script><p>Test</p></body></html>";
 Whitelist whitelist = Whitelist.simpleTagNames("p");
 Document.OutputSettings settings = new Document.OutputSettings();
 settings.prettyPrint(true);
 String cleanedHtml = Jsoup.clean(bodyHtml, "", whitelist, settings);
 assertFalse(cleanedHtml.contains("<script>"));
 assertEquals(1, DataUtil.getElementsByTagName(cleanedHtml, "p").size());
 assertEquals("\n<p>\n Test\n</p>\n", cleanedHtml);
 }

 @Test
 public void testIsValid() {
 String bodyHtml = "<html><body><script>alert('Hello');</script><p>Test<script>Hello</script></p></body></html>";
 Whitelist whitelist = Whitelist.simpleTagNames("p");
 assertFalse(Jsoup.isValid(bodyHtml, whitelist));
 }

 @Test
 public void testIsValid_OutputSettings() {
 String bodyHtml = "<html><body><script>alert('Hello');</script><p>Test<script>Hello</script></p></body></html>";
 Whitelist whitelist = Whitelist.simpleTagNames("p");
 Document.OutputSettings settings = new Document.OutputSettings();
 settings.prettyPrint(true);
 assertTrue(Jsoup.isValid(bodyHtml, whitelist, settings));
 }
}