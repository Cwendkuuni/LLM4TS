package org.jsoup.safety;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.jsoup.safety.Whitelist;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class CleanerTest {

 private Whitelist whitelist;
 private Cleaner cleaner;

 @Before
 public void setUp() {
 whitelist = Whitelist.simpleText();
 cleaner = new Cleaner(whitelist);
 }

 @Test
 public void testEmptyDocument() {
 Document dirtyDocument = Jsoup.parseBodyElements("<html></html>");
 Document cleanDocument = cleaner.clean(dirtyDocument);
 assertEquals("<html></html>", cleanDocument.outerHtml());
 }

 @Test
 public void testValidDocument() {
 String html = "<html><body><p>Hello world!</p></body></html>";
 Document dirtyDocument = Jsoup.parse(html);
 Document cleanDocument = cleaner.clean(dirtyDocument);
 assertEquals("<html><body><p></p></body></html>", cleanDocument.outerHtml());
 }

 @Test
 public void testInvalidDocumentWithUnsafeTag() {
 String html = "<html><body><script>alert('xss');</script></body></html>";
 Document dirtyDocument = Jsoup.parse(html);
 Document cleanDocument = cleaner.clean(dirtyDocument);
 assertNull(cleanDocument);
 }

 @Test
 public void testInvalidDocumentWithUnsafeAttribute() {
 String html = "<html><body><a href='javascript:alert(\"xss\")'></a></body></html>";
 Document dirtyDocument = Jsoup.parse(html);
 Document cleanDocument = cleaner.clean(dirtyDocument);
 assertNotNull(cleanDocument);
 assertEquals("<html><body></body></html>", cleanDocument.outerHtml());
 }

 @Test
 public void testValidDocumentWithAttributes() {
 String html = "<html><body><p id='test' class='test' title='test'></p></body></html>";
 Document dirtyDocument = Jsoup.parse(html);
 Document cleanDocument = cleaner.clean(dirtyDocument);
 assertEquals("<html><body><p id='test' class='test' title='test'></p></body></html>", cleanDocument.outerHtml());
 }

 @Test
 public void testInvalidDocumentWithUnsafeTagAndAttribute() {
 String html = "<html><body><script id='xss'></script></body></html>";
 Document dirtyDocument = Jsoup.parse(html);
 Document cleanDocument = cleaner.clean(dirtyDocument);
 assertNull(cleanDocument);
 }

 @Test
 public void testWhitelistWithCustomTags() {
 Whitelist whitelist = Whitelist.simpleText();
 whitelist.addTags("script", "style");
 Cleaner cleaner = new Cleaner(whitelist);
 String html = "<html><body><script>alert('xss');</script></body></html>";
 Document dirtyDocument = Jsoup.parse(html);
 Document cleanDocument = cleaner.clean(dirtyDocument);
 assertNotNull(cleanDocument);
 assertEquals("<html><body></body></html>", cleanDocument.outerHtml());
 }

 @Test
 public void testIsValidDocumentWithEmptyDocument() {
 Document dirtyDocument = Jsoup.parseBodyElements("<html></html>");
 boolean isValid = cleaner.isValid(dirtyDocument);
 assertTrue(isValid);
 }

 @Test
 public void testIsValidDocumentWithValidDocument() {
 String html = "<html><body><p>Hello world!</p></body></html>";
 Document dirtyDocument = Jsoup.parse(html);
 boolean isValid = cleaner.isValid(dirtyDocument);
 assertTrue(isValid);
 }

 @Test
 public void testIsValidDocumentWithInvalidDocumentWithUnsafeTag() {
 String html = "<html><body><script>alert('xss');</script></body></html>";
 Document dirtyDocument = Jsoup.parse(html);
 boolean isValid = cleaner.isValid(dirtyDocument);
 assertFalse(isValid);
 }

 @Test
 public void testIsValidDocumentWithInvalidDocumentWithUnsafeAttribute() {
 String html = "<html><body><a href='javascript:alert(\"xss\")'></a></body></html>";
 Document dirtyDocument = Jsoup.parse(html);
 boolean isValid = cleaner.isValid(dirtyDocument);
 assertFalse(isValid);
 }

 @Test
 public void testIsValidDocumentWithValidDocumentWithAttributes() {
 String html = "<html><body><p id='test' class='test' title='test'></p></body></html>";
 Document dirtyDocument = Jsoup.parse(html);
 boolean isValid = cleaner.isValid(dirtyDocument);
 assertTrue(isValid);
 }

 @Test
 public void testIsValidDocumentWithInvalidDocumentWithUnsafeTagAndAttribute() {
 String html = "<html><body><script id='xss'></script></body></html>";
 Document dirtyDocument = Jsoup.parse(html);
 boolean isValid = cleaner.isValid(dirtyDocument);
 assertFalse(isValid);
 }
}