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
 private Document dirtyDocument;
 private Document cleanDocument;

 @Before
 public void setup() {
 whitelist = Whitelist.simpleTextOnly();
 cleaner = new Cleaner(whitelist);
 dirtyDocument = Jsoup.parse("<html><body><div id='test'><p>This is a test.</p><script>alert('XSS');</script></div></body></html>");
 cleanDocument = Jsoup.parse("<html><body></body></html>");
 }

 @Test
 public void testClean() {
 Document cleanedDocument = cleaner.clean(dirtyDocument);
 assertEquals(cleanDocument.body().text(), cleanedDocument.body().text());
 assertTrue(cleanedDocument.body().children().isEmpty());
 }

 @Test
 public void testIsValid() {
 assertTrue(cleaner.isValid(dirtyDocument));
 }

 @Test
 public void testIsInvalid() {
 Whitelist whitelistWithScript = Whitelist.simpleTextAndScripts();
 Cleaner cleanerWithScript = new Cleaner(whitelistWithScript);
 assertFalse(cleanerWithScript.isValid(dirtyDocument));
 }

 @Test
 public void testCopySafeNodes() {
 Document documentWithSafeTag = Jsoup.parse("<html><body><p class='test'>This is a test</p></body></html>");
 Element root = documentWithSafeTag.body();
 Element destination = cleanDocument.body();
 int numDiscarded = cleaner.copySafeNodes(root, destination);
 assertEquals("This is a test", destination.text());
 assertEquals(1, destination.childNodeSize());
 assertEquals(0, numDiscarded);
 }

 @Test
 public void testCopySafeNodesWithDiscardedAttribute() {
 Document documentWithDiscardedAttribute = Jsoup.parse("<html><body><p class='test' id='testId'>This is a test <script>alert('XSS');</script></p></body></html>");
 Element root = documentWithDiscardedAttribute.body();
 Element destination = cleanDocument.body();
 int numDiscarded = cleaner.copySafeNodes(root, destination);
 assertTrue(destination.childNodeSize() > 0);
 assertEquals(1, destination.childNodeSize());
 assertFalse(destination.getElementsByTag("script").isEmpty());
 assertEquals(2, numDiscarded);
 }

 @Test
 public void testCopySafeNodesWithDiscardedTag() {
 Document documentWithDiscardedTag = Jsoup.parse("<html><body><div><script>alert('XSS');</script></div></body></html>");
 Element root = documentWithDiscardedTag.body();
 Element destination = cleanDocument.body();
 int numDiscarded = cleaner.copySafeNodes(root, destination);
 assertTrue(destination.childNodeSize() > 0);
 assertEquals(0, destination.childNodeSize());
 assertEquals(1, numDiscarded);
 }
}