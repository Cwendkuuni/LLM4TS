package org.jsoup.safety;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.parser.Tag;
import static org.junit.Assert.*;

public class CleanerTest {

 private Cleaner cleaner;
 private Whitelist whitelist;

 @Before
 public void setUp() {
 whitelist = Whitelist.simpleTextOnly();
 cleaner = new Cleaner(whitelist);
 }

 @Test
 public void testCreateCleaner() {
 assertNotNull(cleaner);
 }

 @Test
 public void testCleanDocument() {
 Document dirtyDocument = Jsoup.parse("<html><body><script>alert('XSS')</script><p>Hello</p></body></html>");
 Document cleanDocument = cleaner.clean(dirtyDocument);
 Element body = cleanDocument.body();
 assertEquals(0, body.childNodeSize());
 }

 @Test
 public void testIsValidDocument() {
 Document dirtyDocument = Jsoup.parse("<html><body><p>Hello</p></body></html>");
 assertTrue(cleaner.isValid(dirtyDocument));
 }

 @Test
 public void testIsNotValidDocument() {
 Document dirtyDocument = Jsoup.parse("<html><body><script>alert('XSS')</script><p>Hello</p></body></html>");
 assertFalse(cleaner.isValid(dirtyDocument));
 }

 @Test
 public void testCopySafeNodes() {
 Document dirtyDocument = Jsoup.parse("<html><body><p id='test' class='test'>Hello <strong>World</strong></p></body></html>");
 Document cleanDocument = Document.createShell(dirtyDocument.baseUri());
 int numDiscarded = cleaner.copySafeNodes(dirtyDocument.body(), cleanDocument.body());
 assertEquals(2, numDiscarded);
 Element p = cleanDocument.body().getElementsByTag("p").first();
 assertTrue(p.hasAttr("id"));
 assertTrue(p.hasAttr("class"));
 assertNull(p.childNodes().get(1));
 }

 @Test
 public void testCopySafeNodesWithEmptyBody() {
 Document dirtyDocument = Jsoup.parse("<html><head></html>");
 Document cleanDocument = Document.createShell(dirtyDocument.baseUri());
 int numDiscarded = cleaner.copySafeNodes(dirtyDocument.body(), cleanDocument.body());
 assertEquals(0, numDiscarded);
 }
}