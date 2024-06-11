package org.jsoup.safety;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.parser.Tag;
import org.jsoup.safety.Whitelist;
import static org.junit.Assert.*;

public class CleanerTest {

 private Cleaner cleaner;
 private Whitelist whitelist;
 private Document dirtyDocument;
 private Document cleanDocument;

 @Before
 public void setUp() {
 whitelist = Whitelist.basic();
 cleaner = new Cleaner(whitelist);
 dirtyDocument = Jsoup.parse("<html><body><h1 id='title'>Test Title</h1>" +
 "<script src='malicious.js'>Alert('XSS');</script>" +
 "<a href='http://example.com'>Link</a></body></html>");
 cleanDocument = Document.createShell(dirtyDocument.baseUri());
 }

 @Test
 public void testCleanDocument() {
 Document cleanedDocument = cleaner.clean(dirtyDocument);
 assertEquals("<html><body><h1 id='title'>Test Title</h1><a href='http://example.com'>Link</a></body></html>", cleanedDocument.html());
 }

 @Test
 public void testIsValidDocument() {
 boolean isValid = cleaner.isValid(dirtyDocument);
 assertFalse(isValid);
 }

 @Test
 public void testIsValidCleanDocument() {
 Document cleanValidDocument = Jsoup.parse("<html><body><h1 id='title'>Test Title</h1><a href='http://example.com'>Link</a></body></html>");
 boolean isValid = cleaner.isValid(cleanValidDocument);
 assertTrue(isValid);
 }

 @Test
 public void testCopySafeNodes() {
 assertEquals(3, cleaner.copySafeNodes(dirtyDocument.body(), cleanDocument.body()).intValue());
 }

 @Test
 public void testCreateSafeElement() {
 Element h1 = dirtyDocument.getElementById("title");
 Element metaElement = cleaner.createSafeElement(h1);
 assertEquals("h1", metaElement.tagName());
 assertEquals("Test Title", metaElement.text());
 }

 @Test(expected = NullPointerException.class)
 public void testCleanNullDocument() {
 cleaner.clean(null);
 }

 @Test(expected = NullPointerException.class)
 public void testIsValidNullDocument() {
 cleaner.isValid(null);
 }
}