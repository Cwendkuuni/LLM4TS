package org.jsoup.safety;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.ParseError;
import org.jsoup.safety.Whitelist;
import org.jsoup.safety.Cleaner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class CleanerTest {

 private Cleaner cleaner;
 private Whitelist whitelist;

 @Before
 public void setup() {
 whitelist = Whitelist.simpleText();
 cleaner = new Cleaner(whitelist);
 }

 @Test
 public void testCleanValidDocument() {
 String html = "<html><body><p>Hello World</p></body></html>";
 Document dirtyDocument = Jsoup.parse(html);
 Document cleanDocument = cleaner.clean(dirtyDocument);

 assertEquals("<html><body><p></p></body></html>", cleanDocument.html());
 }

 @Test
 public void testCleanInvalidDocument() {
 String html = "<html><body><script>alert('XSS');</script></body></html>";
 Document dirtyDocument = Jsoup.parse(html);

 Document cleanDocument = cleaner.clean(dirtyDocument);

 assertEquals("<html><body></body></html>", cleanDocument.html());
 assertFalse(cleanDocument.body().text().contains("XSS"));
 }

 @Test
 public void testIsValidValidDocument() {
 String html = "<html><body><p>Hello World</p></body></html>";
 Document dirtyDocument = Jsoup.parse(html);

 assertTrue(cleaner.isValid(dirtyDocument));
 }

 @Test
 public void testIsValidInvalidDocument() {
 String html = "<html><body><script>alert('XSS');</script></body></html>";
 Document dirtyDocument = Jsoup.parse(html);

 assertFalse(cleaner.isValid(dirtyDocument));
 }

 @Test(expected = NullPointerException.class)
 public void testCleanNullDocument() {
 cleaner.clean(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCleanNullWhitelist() {
 Whitelist nullWhitelist = null;
 Cleaner cleaner = new Cleaner(nullWhitelist);
 cleaner.clean(Jsoup.parse("<html><body></body></html>"));
 }

 @Test
 public void testCopySafeNodesValidDocument() {
 String html = "<html><body><p id=\"test\" class=\"test\" title=\"Test Title\"><a href=\"http://example.com\">Link</a></p></body></html>";
 Element source = Jsoup.parse(html).body().getElementsByTag("p").first();
 Element destination = Document.createShell(source.baseUri()).body();
 int numDiscarded = cleaner.copySafeNodes(source, destination);

 assertEquals("<p class=\"test\" title=\"Test Title\"></p>", destination.html());
 assertEquals(0, numDiscarded);
 }

 @Test
 public void testCopySafeNodesInvalidDocument() {
 String html = "<html><body><script>alert('XSS');</script></body></html>";
 Element source = Jsoup.parse(html).body();
 Element destination = Document.createShell(source.baseUri()).body();
 int numDiscarded = cleaner.copySafeNodes(source, destination);

 assertEquals("<body></body>", destination.html());
 assertEquals(1, numDiscarded);
 }

 @Test
 public void testCreateSafeElementValidAttribute() {
 String html = "<div id=\"test\" class=\"test\" title=\"Test Title\">Test Attribute</div>";
 Element source = Jsoup.parse(html).body().getElementsByTag("div").first();

 ElementMeta meta = cleaner.createSafeElement(source);
 assertNotNull(meta.el);
 assertFalse(meta.el.attributes().toString().contains("style"));
 }

 @Test
 public void testCreateSafeElementInvalidAttribute() {
 String html = "<div id=\"test\" class=\"test\" title=\"Test Title\" style=\"color: red;\">Test Attribute</div>";
 Element source = Jsoup.parse(html).body().getElementsByTag("div").first();

 ElementMeta meta = cleaner.createSafeElement(source);
 assertNotNull(meta.el);
 assertFalse(meta.el.attributes().toString().contains("style"));
 }

 @Test
 public void testCreateSafeElementInvalidTag() {
 String html = "<script id=\"test\" class=\"test\" title=\"Test Title\">Test Attribute</script>";
 Element source = Jsoup.parse(html).body().getElementsByTag("script").first();

 ElementMeta meta = cleaner.createSafeElement(source);
 assertNull(meta.el);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCreateSafeElementNullElement() {
 Element nullElement = null;
 ElementMeta meta = cleaner.createSafeElement(nullElement);
 }

 @Test
 public void testIsValidWithParseErrors() {
 String html = "<html><body><script>alert('XSS');</script><p>Hello World</p></body></html>";
 Document dirtyDocument = Jsoup.parse(html);
 ParseErrorList parseErrors = dirtyDocument.outputSettings().parseErrorList();
 assertTrue(cleaner.isValid(dirtyDocument));
 assertFalse(parseErrors.isEmpty());
 }
}