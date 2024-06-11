package org.jsoup.safety;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.jsoup.safety.Whitelist;
import org.jsoup.safety.Cleaner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class CleanerTest {

 private Cleaner cleaner;
 private Whitelist whitelist;

 @Before
 public void setUp() {
 whitelist = Whitelist.simpleText();
 cleaner = new Cleaner(whitelist);
 }

 @Test
 public void testCreateCleaner() {
 assertNotNull(cleaner);
 }

 @Test
 public void testCleanDocument() {
 Document dirtyDocument = Jsoup.parse("<html><body><script>alert('XSS')</script></body></html>");
 Document cleanDocument = cleaner.clean(dirtyDocument);
assertFalse(cleanDocument.getElementsByTag("script").isEmpty());
 assertEquals("", cleanDocument.getElementsByTag("script").first().text());
 }

 @Test
 public void testIsValidDocument() {
 Document validDocument = Jsoup.parse("<html><body><p>Safe Content</p></body></html>");
 assertTrue(cleaner.isValid(validDocument));
 }

 @Test
 public void testInvalidDocument() {
 Document invalidDocument = Jsoup.parse("<html><body><script>alert('XSS')</script></body></html>");
 assertFalse(cleaner.isValid(invalidDocument));
 }

 @Test
 public void testCopySafeNodes() {
 Element root = new Element(Tag.BODY, "http://localhost");
 Element destination = new Element(Tag.BODY, "http://localhost");
 Element sourceElement = new Element(Tag.P, "http://localhost");
 sourceElement.text("Safe Content");
 root.appendChild(sourceElement);
 destination.appendChild(sourceElement);
 int numDiscarded = cleaner.copySafeNodes(root, destination);
 assertEquals(0, numDiscarded);
 }

 @Test
 public void testCopySafeNodesWithDiscardedAttributes() {
 Element root = new Element(Tag.DIV, "http://localhost");
 root.attr("class", "disallowed");
 int numDiscarded = cleaner.copySafeNodes(root, null);
 assertEquals(1, numDiscarded);
 }

 @Test
 public void testCopySafeNodesWithDiscardedScriptTag() {
 Element root = new Element(Tag.SCRIPT, "http://localhost");
 root.attr("src", "malicious.js");
 int numDiscarded = cleaner.copySafeNodes(root, null);
 assertEquals(1, numDiscarded);
 }
}