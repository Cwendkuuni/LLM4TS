package org.jsoup.safety;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.*;
import org.jsoup.parser.Tag;
import static org.junit.Assert.*;

/**
 * Test class for Cleaner class.
 */
public class CleanerTest {

 private Whitelist whitelist;
 private Cleaner cleaner;

 @Before
 public void setup() {
 whitelist = Whitelist.simpleTextOnly();
 cleaner = new Cleaner(whitelist);
 }

 /**
 * Test method for clean(Document) method with valid input.
 */
 @Test
 public void testCleanValidInput() {
 Document dirtyDocument = Document.fromHtml("<html><body><p>Hello World!</p></body></html>");
 Document cleanDocument = cleaner.clean(dirtyDocument);
 assertEquals("<html><body><p></p></body></html>", cleanDocument.html());
 }

 /**
 * Test method for clean(Document) method with invalid input.
 */
 @Test(expected = NullPointerException.class)
 public void testCleanInvalidInput() {
 cleaner.clean(null);
 }

 /**
 * Test method for isValid(Document) method with valid input.
 */
 @Test
 public void testIsValidValidInput() {
 Document dirtyDocument = Document.fromHtml("<html><body><p>Hello World!</p></body></html>");
 assertTrue(cleaner.isValid(dirtyDocument));
 }

 /**
 * Test method for isValid(Document) method with invalid input.
 */
 @Test
 public void testIsValidInvalidInput() {
 Document dirtyDocument = Document.fromHtml("<html><body><script>alert('XSS')</script></body></html>");
 assertFalse(cleaner.isValid(dirtyDocument));
 }

 /**
 * Test method for copySafeNodes(Element, Element) method with safe tags and attributes.
 */
 @Test
 public void testCopySafeNodesSafeTagsAndAttributes() {
 Element sourceElement = new Element(Tag.valueOf("div"), null, new Attributes());
 sourceElement.text("Safe Content");
 Element destElement = new Element(Tag.valueOf("p"), null, new Attributes());
 int numDiscarded = cleaner.copySafeNodes(sourceElement, destElement);
 assertEquals("Safe Content", destElement.text());
 assertEquals(0, numDiscarded);
 }

 /**
 * Test method for copySafeNodes(Element, Element) method with invalid tags.
 */
 @Test
 public void testCopySafeNodesInvalidTags() {
 Element sourceElement = new Element(Tag.valueOf("script"), null, new Attributes());
 sourceElement.text("XSS Content");
 Element destElement = new Element(Tag.valueOf("p"), null, new Attributes());
 int numDiscarded = cleaner.copySafeNodes(sourceElement, destElement);
 assertTrue(numDiscarded > 0);
 assertNull(destElement.text());
 }
}