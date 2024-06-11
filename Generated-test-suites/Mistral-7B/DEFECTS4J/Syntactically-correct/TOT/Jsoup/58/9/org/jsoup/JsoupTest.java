package org.jsoup.test;

import org.junit.Test;
import static org.junit.Assert.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.safety.Whitelist;
import org.jsoup.helper.HttpConnection;
import java.io.IOException;

public class JsoupTest {

 @Test
 public void testParseWithBaseUri() {
 String html = "<html><head><base href=\"https://example.com/\"></head><body><a href=\"about.html\">About</a></body></html>";
 Document doc = Jsoup.parse(html, "https://example.com/");
 Element link = doc.selectFirst("a");
 assertEquals("About", link.text());
 assertEquals("https://example.com/about.html", link.absUrl(""));
 }

 @Test
 public void testParseWithoutBaseUri() {
 String html = "<html><head><base href=\"https://example.com/\"></head><body><a href=\"about.html\">About</a></body></html>";
 Document doc = Jsoup.parse(html);
 Element link = doc.selectFirst("a");
 assertEquals("About", link.text());
 assertEquals("about.html", link.absUrl("/")); // Relative URLs are resolved based on <base href> tag in the HTML.
 }

 @Test
 public void testParseWithCustomParser() {
 String html = "<xml><tag attr=\"value\"/></xml>";
 Parser parser = new Parser.XmlParser();
 Document doc = Jsoup.parse(html, "", parser);
 Element tag = doc.selectFirst("tag");
 assertEquals("value", tag.attr("attr"));
 }

 @Test
 public void testConnectAndParse() throws IOException {
 String url = "http://example.com";
 Document doc = Jsoup.connect(url).get();
 assertEquals(200, doc.statusCode()); // Test status code is HTTP OK.
 }

 @Test(expected = IOException.class)
 public void testConnectAndParseWithMalformedUrl() {
 String url = "jsoup://example.com";
 try {
 Document doc = Jsoup.connect(url).get();
 } catch (IOException e) {
 // Expected exception.
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConnectWithNullUrl() {
 try {
 Document doc = Jsoup.connect(null).get();
 } catch (IllegalArgumentException e) {
 // Expected exception.
 }
 }

 @Test
 public void testParseFromFile() throws IOException {
 String charset = "UTF-8";
 String fileName = "input.html";
 Document doc = Jsoup.parse(new File(fileName), charset);
 assertEquals(200, doc.statusCode()); // Test status code is HTTP OK if file contains HTML with <statuscode> tag.
 }

 @Test(expected = FileNotFoundException.class)
 public void testParseFromFileNotFound() throws IOException {
 String fileName = "nonexistentfile.html";
 try {
 Document doc = Jsoup.parse(new File(fileName), "UTF-8");
 } catch (FileNotFoundException e) {
 // Expected exception.
 }
 }

 @Test
 public void testParseFromInputStream() throws IOException {
 String charset = "UTF-8";
 String fileName = "input.html";
 Document doc = Jsoup.parse(new FileInputStream(new File(fileName)), charset);
 assertEquals(200, doc.statusCode()); // Test status code is HTTP OK if file contains HTML with <statuscode> tag.
 }

 @Test
 public void testParseBodyFragment() {
 String bodyHtml = "<p>Hello world!</p>";
 Document doc = Jsoup.parseBodyFragment(bodyHtml);
 assertEquals("Hello world!", doc.body().text());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseBodyFragmentWithNull() {
 try {
 Document doc = Jsoup.parseBodyFragment(null);
 } catch (IllegalArgumentException e) {
 // Expected exception.
 }
 }

 @Test
 public void testClean() {
 String bodyHtml = "<script src=\"badscript.js\"></script><p>Hello world!</p>";
 Whitelist whitelist = new Whitelist(new TagNameFilter.AllowAllElementsFilter());
 Document doc = Jsoup.parseBodyFragment(bodyHtml, "");
 String cleanHtml = Jsoup.clean(bodyHtml, whitelist);
 assertEquals("<p>Hello world!</p>", cleanHtml);
 }

 @Test
 public void testCleanWithBaseUri() {
 String bodyHtml = "<script src=\"badscript.js\"></script><img src=\"image.jpg\" alt=\"Image\"><p>Hello world!</p>";
 Whitelist whitelist = new Whitelist(new TagNameFilter.AllowAllElementsFilter(), new AttributeFilter.AllowAllAttributesFilter());
 Document doc = Jsoup.parseBodyFragment(bodyHtml, "");
 String cleanHtml = Jsoup.clean(bodyHtml, whitelist, "https://example.com/");
 assertEquals("<p>Hello world!</p>", cleanHtml);
 assertEquals("https://example.com/image.jpg", doc.selectFirst("img").absUrl(""));
 }

 @Test
 public void testIsValid() {
 String bodyHtml = "<form action=\"submit.html\"><input type=\"text\" name=\"name\"/><input type=\"submit\"/></form>";
 Whitelist whitelist = new Whitelist(new TagNameFilter.AllowFormsFilter());
 assertTrue(Jsoup.isValid(bodyHtml, whitelist));
 }

 @Test
 public void testIsInvalid() {
 String bodyHtml = "<script src=\"badscript.js\"></script>";
 Whitelist whitelist = new Whitelist();
 assertFalse(Jsoup.isValid(bodyHtml, whitelist));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIsValidWithNull() {
 try {
 boolean isValid = Jsoup.isValid(null, new Whitelist());
 } catch (IllegalArgumentException e) {
 // Expected exception.
 }
 }
}