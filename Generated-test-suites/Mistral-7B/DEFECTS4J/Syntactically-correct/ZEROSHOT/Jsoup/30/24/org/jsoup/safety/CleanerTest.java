package org.jsoup.safety;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.jsoup.parser.Tag;

import java.util.List;

public class CleanerTest {

 private Whitelist createWhitelist() {
 return new Whitelist.SimpleTextWhitelist();
 }

 @Test
 public void testCreateCleaner() {
 Cleaner cleaner = new Cleaner(createWhitelist());
 assert null != cleaner;
 }

 @Test
 public void testCleanDocument() {
 String dirtyHtml = "<html><body><div id='test'><script>alert('XSS')</script></div></body></html>";
 Document dirtyDocument = Jsoup.parseBodyFragment(dirtyHtml);
 Cleaner cleaner = new Cleaner(createWhitelist());
 Document cleanDocument = cleaner.clean(dirtyDocument);

 Assertions.assertNotNull(cleanDocument);
 Assertions.assertEquals("<html><body></body></html>", cleanDocument.outerHtml());
 }

 @Test
 public void testIsValidDocument() {
 String dirtyHtml = "<html><body><div id='test'><script>alert('XSS')</script></div></body></html>";
 Document dirtyDocument = Jsoup.parseBodyFragment(dirtyHtml);
 Cleaner cleaner = new Cleaner(createWhitelist());
 boolean isValid = cleaner.isValid(dirtyDocument);

 Assertions.assertFalse(isValid);
 }

 @Test
 public void testIsValidValidDocument() {
 String dirtyHtml = "<html><body><p>Valid HTML</p></body></html>";
 Document dirtyDocument = Jsoup.parseBodyFragment(dirtyHtml);
 Cleaner cleaner = new Cleaner(createWhitelist());
 boolean isValid = cleaner.isValid(dirtyDocument);

 Assertions.assertTrue(isValid);
 }

 @Test
 public void testCopySafeNodesTextNode() {
 String dirtyHtml = "<html><body><p>Text: Hello World!</p></body></html>";
 Document dirtyDocument = Jsoup.parseBodyFragment(dirtyHtml);
 Element root = dirtyDocument.body();
 Element destination = new Element(Tag.valueOf("p"), root.baseUri());
 int numDiscarded = CleanerTest.copySafeNodes(root, destination);

 Assertions.assertEquals(0, numDiscarded);
 Assertions.assertEquals("Text: Hello World!", destination.text());
 }

 @Test
 public void testCopySafeNodesElement() {
 String dirtyHtml = "<html><body><p id='test'><strong>Text: Hello World!</strong></p></body></html>";
 Document dirtyDocument = Jsoup.parseBodyFragment(dirtyHtml);
 Element root = dirtyDocument.body().getElementsByTag("p").first();
 Element destination = new Element(Tag.valueOf("p"), root.baseUri());
 int numDiscarded = CleanerTest.copySafeNodes(root, destination);

 Assertions.assertEquals(0, numDiscarded);
 Assertions.assertEquals("<p><strong>Text: Hello World!</strong></p>", destination.outerHtml());
 }

 @Test
 public void testCopySafeNodesInvalidElement() {
 String dirtyHtml = "<html><body><script id='test'>alert('XSS')</script></body></html>";
 Document dirtyDocument = Jsoup.parseBodyFragment(dirtyHtml);
 Element root = dirtyDocument.body();
 Element destination = new Element(Tag.valueOf("div"), root.baseUri());
 int numDiscarded = CleanerTest.copySafeNodes(root, destination);

 Assertions.assertEquals(1, numDiscarded);
 Assertions.assertEquals("", destination.outerHtml());
 }

 private static int copySafeNodes(Element root, Element destination) {
 return new Cleaner(createWhitelist()).copySafeNodes(root, destination);
 }
}