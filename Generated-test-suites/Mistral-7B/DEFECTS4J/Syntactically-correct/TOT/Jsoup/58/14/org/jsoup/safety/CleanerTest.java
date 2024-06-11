package org.jsoup.safety;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;
import org.jsoup.safety.Whitelist;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

public class CleanerTest {

 private Cleaner cleaner;
 private Whitelist whitelist;

 @Before
 public void setup() {
 whitelist = Whitelist.simpleText();
 cleaner = new Cleaner(whitelist);
 }

 @Test
 public void testCleanWithValidDocument() {
 Document dirtyDocument = Jsoup.parse("<html><body><p>Hello World!</p></body></html>");
 Document cleanDocument = cleaner.clean(dirtyDocument);

 assertNotNull(cleanDocument);
 assertEquals("<html><body><p>Hello World!</p></body></html>", cleanDocument.html());
 }

 @Test
 public void testIsValidWithValidDocument() {
 Document dirtyDocument = Jsoup.parse("<html><body><p>Hello World!</p></body></html>");
 assertTrue(cleaner.isValid(dirtyDocument));
 }

 @Test
 public void testIsValidWithInvalidDocument() {
 Document dirtyDocument = Jsoup.parse("<html><body><script>alert('XSS Attack!');</script></body></html>");
 assertFalse(cleaner.isValid(dirtyDocument));
 }

 @Test
 public void testCleanWithInvalidDocument() {
 Document dirtyDocument = Jsoup.parse("<html><body><script>alert('XSS Attack!');</script></body></html>");
 Document cleanDocument = cleaner.clean(dirtyDocument);

 assertNotNull(cleanDocument);
 assertEquals("<html><body></body></html>", cleanDocument.html());
 }

 @Test
 public void testCleanWithEmptyDocument() {
 Document dirtyDocument = Jsoup.parse("<html></html>");
 Document cleanDocument = cleaner.clean(dirtyDocument);

 assertNotNull(cleanDocument);
 assertEquals("<html></html>", cleanDocument.html());
 }

 @Test
 public void testCleanWithDocumentWithAttributes() {
 Document dirtyDocument = Jsoup.parse("<html><body><p id='test' class='highlight'><span>Test</span></p></body></html>");
 Document cleanDocument = cleaner.clean(dirtyDocument);

 assertNotNull(cleanDocument);
 assertEquals("<html><body><p><span>Test</span></p></body></html>", cleanDocument.html());
 assertNull(cleanDocument.getElementById("test"));
 assertNull(cleanDocument.getElementsByClass("highlight"));
 }

 @Test
 public void testCleanWithDocumentWithFrameset() {
 Document dirtyDocument = Jsoup.parse("<html><frameset><frame src='frame1.html'></frameset></html>");
 Document cleanDocument = cleaner.clean(dirtyDocument);

 assertNotNull(cleanDocument);
 assertEquals("<html></html>", cleanDocument.html());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCleanWithNullDocument() {
 cleaner.clean(null);
 }
}