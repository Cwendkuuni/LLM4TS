package org.jsoup.safety;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.shelldoc.PromptFactory;
import org.jsoup.safety.Whitelist;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

/**
 * Test class for Cleaner class
 */
public class CleanerTest {

 private Document dirtyDocument;
 private Document cleanDocument;
 private Cleaner cleaner;
 private Whitelist whitelist;

 @Before
 public void setup() {
 whitelist = Whitelist.relaxed();
 cleaner = new Cleaner(whitelist);
 dirtyDocument = Jsoup.parse(PromptFactory.getPrompt("<html><body><script>alert('XSS')</script></body></html>"));
 cleanDocument = Document.createShell(dirtyDocument.baseUri());
 }

 @Test
 public void testClean() {
 cleaner.clean(dirtyDocument);
 assertNotNull(cleanDocument);
 assertNull(cleanDocument.body().select("script").first());
 }

 @Test
 public void testIsValid() {
 assertFalse(cleaner.isValid(dirtyDocument));
 }

 @Test
 public void testCleanValidDocument() {
 Document validDocument = Jsoup.parse(PromptFactory.getPrompt("<html><body><h1>Hello World!</h1></body></html>"));
 cleaner.clean(validDocument);
 assertNotNull(cleanDocument);
 assertSame(validDocument.body(), cleanDocument.body());
 }

 @Test
 public void testIsValidValidDocument() {
 assertTrue(cleaner.isValid(validDocument));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullWhitelist() {
 cleaner = new Cleaner(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullDocument() {
 cleaner.clean(null);
 }

 @Test
 public void testEmptyDocument() {
 Document emptyDocument = Document.createShell(null);
 cleaner.clean(emptyDocument);
 assertNotNull(cleanDocument);
 assertSame(emptyDocument.body(), cleanDocument.body());
 }

 @Test
 public void testCopySafeNodes() {
 Element root = new Element("html");
 root.appendElement("head"). appendElement("title").setText("Test Title");
 root.appendElement("body").appendElement("h1").setText("Test Header");
 Element destination = new Element("div");
 int numDiscarded = cleaner.copySafeNodes(root, destination);
 assertEquals(2, numDiscarded);
 assertEquals("Test Title", destination.select("title").text());
 assertEquals("Test Header", destination.select("h1").text());
 }
}