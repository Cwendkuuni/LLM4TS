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
 String baseUri = "https://example.com";
 // Test parsing with default parser
 Document doc1 = Jsoup.parse(html, baseUri);
 Assert.assertEquals("Hello World", doc1.select("h1").text());

 // Test parsing with custom parser
 Parser parser = Parser.xmlParser();
 Document doc2 = Jsoup.parse(html, baseUri, parser);
 Assert.assertEquals("Hello World", doc2.select("h1").text());

 // Test parsing without base URI
 Document doc3 = Jsoup.parse(html);
 Assert.assertEquals("Hello World", doc3.select("h1").text());
 }

 @Test
 public void testConnect() throws IOException {
 String url = "https://example.com";
 Connection connection = Jsoup.connect(url);
 Document doc = connection.get();
 Assert.assertNotNull(doc);
 }

 @Test
 public void testParseFile() throws IOException {
 String filePath = "path/to/your/file.html";
 Document doc = Jsoup.parse(new File(filePath), "UTF-8");
 Assert.assertNotNull(doc);
 }

 @Test
 public void testParseInputStream() throws IOException {
 InputStream inputStream = getClass().getResourceAsStream("/path/to/your/file.html");
 Document doc = Jsoup.parse(inputStream, "UTF-8");
 Assert.assertNotNull(doc);
 }

 @Test
 public void testParseBodyFragment() {
 String bodyHtml = "<h1>Hello World</h1>";
 Document doc = Jsoup.parseBodyFragment(bodyHtml);
 Assert.assertEquals("Hello World", doc.select("h1").text());
 }

 @Test
 public void testClean() {
 String bodyHtml = "<html><head><script>alert('XSS')</script></head><body><h1>Hello World</h1></body></html>";
 String baseUri = "https://example.com";
 Whitelist whitelist = Whitelist.simpleText();
 String cleanHtml = Jsoup.clean(bodyHtml, baseUri, whitelist);
 Assert.assertEquals("Hello World", cleanHtml);
 }

 @Test
 public void testIsValid() {
 String bodyHtml = "<html><head><script>alert('XSS')</script></head><body><h1 id='title' class='title' onclick='click()'>Hello World</h1></body></html>";
 Whitelist whitelist = Whitelist.simpleText();
 boolean isValid = Jsoup.isValid(bodyHtml, whitelist);
 Assert.assertFalse(isValid);
 }
}