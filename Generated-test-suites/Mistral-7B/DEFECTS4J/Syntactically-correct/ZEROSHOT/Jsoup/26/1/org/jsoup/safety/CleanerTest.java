package org.jsoup.safety;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.jsoup.helper.ElementFinder;
import org.jsoup.nodes.*;
import org.jsoup.parser.Tag;
import org.jsoup.safety.Whitelist;
import org.jsoup.safety.Cleaner;

/**
 * Test file for Cleaner class
 */
public class CleanerTest {

 private Whitelist whitelist;
 private Document dirtyDocument;
 private Document cleanDocument;
 private Cleaner cleaner;

 @Before
 public void setUp() {
 whitelist = Whitelist.simpleTextOnly();
 dirtyDocument = Document.createFromString("<html><body><div id='test'><h1>Test</h1><script>alert('XSS');</script></div></body></html>", null);
 cleanDocument = Document.createEmpty();
 cleaner = new Cleaner(whitelist);
 }

 /**
 * Test constructor
 */
 @Test
 public void testConstructor() {
 Cleaner cleaner = new Cleaner(whitelist);
 assertNotNull(cleaner);
 }

 /**
 * Test clean method
 */
 @Test
 public void testClean() {
 cleanDocument = cleaner.clean(dirtyDocument);
 assertEquals("<html><body><div id='test'><h1>Test</h1></div></body></html>", cleanDocument.body().html());
 }

 /**
 * Test isValid method
 */
 @Test
 public void testIsValid() {
 assertTrue(cleaner.isValid(dirtyDocument));
 }

 /**
 * Test copySafeNodes method
 */
 @Test
 public void testCopySafeNodes() {
 ElementFinder finder = new ElementFinder();
 Element div = finder.findFirst(dirtyDocument, "div#test");
 int numDiscarded = cleaner.copySafeNodes(div.element(), cleanDocument.body());
 assertEquals("Unexpected number of discarded elements", 1, numDiscarded);
 }

 /**
 * Test isSafeTag method
 */
 @Test
 public void testIsSafeTag() {
 assertTrue(cleaner.whitelist.isSafeTag("h1"));
 assertFalse(cleaner.whitelist.isSafeTag("script"));
 }

 /**
 * Test isSafeAttribute method
 */
 @Test
 public void testIsSafeAttribute() {
 Element h1 = new Element("h1");
 h1.classes().add("class-name");
 assertFalse(cleaner.whitelist.isSafeAttribute("h1", h1, h1.attr("onclick")));
 assertTrue(cleaner.whitelist.isSafeAttribute("h1", h1, "class", "class-name"));
 }

 /**
 * Test getEnforcedAttributes method
 */
 @Test
 public void testGetEnforcedAttributes() {
 assertArrayEquals(new String[]{"style"}, cleaner.whitelist.getEnforcedAttributes("div"));
 }
}