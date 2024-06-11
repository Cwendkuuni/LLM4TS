package org.jsoup.safety;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.*;
import org.jsoup.parser.Tag;
import static org.junit.Assert.*;

public class CleanerTest {

 private Document dirtyDocument;
 private Element dirtyBody;
 private Whitelist whitelist;
 private Cleaner cleaner;

 @Before
 public void setUp() {
 whitelist = Whitelist.simpleText();
 cleaner = new Cleaner(whitelist);

 String html = "<html><body><script>alert('XSS');</script><h1>Hello</h1></body></html>";
 dirtyDocument = Jsoup.parse(html);
 dirtyBody = dirtyDocument.body();
 }

 /**
 * Test isValid() method with a valid input document (no tags or attributes need to be removed)
 */
 @Test
 public void testIsValidWithValidDocument() {
 assertTrue(cleaner.isValid(dirtyDocument));
 }

 /**
 * Test isValid() method with an invalid input document (tags or attributes need to be removed)
 */
 @Test
 public void testIsValidWithInvalidDocument() {
 assertFalse(cleaner.isValid(dirtyDocument));
 }

 /**
 * Test clean() method with a valid input document (no tags or attributes need to be removed)
 */
 @Test
 public void testCleanWithValidDocument() {
 Document cleanDocument = cleaner.clean(dirtyDocument);
 assertEquals("<html><body><h1>Hello</h1></body></html>", cleanDocument.html());
 }

 /**
 * Test clean() method with an invalid input document (tags or attributes need to be removed)
 */
 @Test
 public void testCleanWithInvalidDocument() {
 Document cleanDocument = cleaner.clean(dirtyDocument);
 assertNotNull(cleanDocument);
 assertFalse(cleanDocument.body().hasText());
 }

 /**
 * Test copySafeNodes() method with a safe tag and its safe attributes
 */
 @Test
 public void testCopySafeNodesWithSafeTagAndSafeAttributes() {
 Element safeTag = new Element(Tag.H1, dirtyDocument.baseUri());
 safeTag.text("Hello");
 safeTag.classes("class1 class2");
 Element safeDestination = new Element(Tag.H1, dirtyDocument.baseUri());
 int numDiscarded = cleaner.copySafeNodes(safeTag, safeDestination);
 assertEquals(0, numDiscarded);
 assertEquals("class1 class2", safeDestination.classes().get(0));
 }

 /**
 * Test copySafeNodes() method with an unsafe tag
 */
 @Test
 public void testCopySafeNodesWithUnsafeTag() {
 Element unsafeTag = new Element(Tag.SCRIPT, dirtyDocument.baseUri());
 unsafeTag.text("alert('XSS')");
 int numDiscarded = cleaner.copySafeNodes(unsafeTag, null);
 assertEquals(1, numDiscarded);
 }

 /**
 * Test copySafeNodes() method with a safe tag but an unsafe attribute
 */
 @Test
 public void testCopySafeNodesWithSafeTagAndUnsafeAttribute() {
 Element safeTag = new Element(Tag.IMG, dirtyDocument.baseUri());
 safeTag.src("javascript:alert('XSS')");
 int numDiscarded = cleaner.copySafeNodes(safeTag, null);
 assertEquals(1, numDiscarded);
 }

 /**
 * Test copySafeNodes() method with a safe tag and enforced attributes
 */
 @Test
 public void testCopySafeNodesWithSafeTagAndEnforcedAttributes() {
 Element safeTag = new Element(Tag.INPUT, dirtyDocument.baseUri());
 safeTag.name("email");
 safeTag.attributes().put("type", "email");
 int numDiscarded = cleaner.copySafeNodes(safeTag, null);
 assertEquals(0, numDiscarded);
 }
}