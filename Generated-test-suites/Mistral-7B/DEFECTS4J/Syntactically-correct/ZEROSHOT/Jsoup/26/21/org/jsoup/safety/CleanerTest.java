package org.jsoup.safety;

import static org.junit.Assert.*;

import org.jsoup.nodes.*;
import org.jsoup.parser.Tag;
import org.jsoup.helper.Validate;
import org.jsoup.Whitelist;
import org.jsoup.Jsoup;
import org.junit.Test;

/**
 * Test class for Cleaner.java
 */
public class CleanerTest {

 /**
 * Test constructor Cleaner.
 */
 @Test
 public void testCleanerConstructor() {
 Cleaner cleaner = new Cleaner(Whitelist.simpleText());
 assertNotNull(cleaner);
 }

 /**
 * Test method clean.
 */
 @Test
 public void testClean() {
 // Arrange
 Document dirtyDocument = Jsoup.parse("<html><body><script>alert('XSS')</script><h1>Hello</h1></body></html>");
 Cleaner cleaner = new Cleaner(Whitelist.simpleText());

 // Act
 Document cleanDocument = cleaner.clean(dirtyDocument);

 // Assert
 assertEquals("<html><body><h1>Hello</h1></body></html>", cleanDocument.outerHtml());
 }

 /**
 * Test method isValid.
 */
 @Test
 public void testIsValid() {
 // Arrange
 Document dirtyDocument = Jsoup.parse("<html><body><script>alert('XSS')</script><h1>Hello</h1></body></html>");
 Cleaner cleaner = new Cleaner(Whitelist.simpleText());

 // Act
 boolean isValid = cleaner.isValid(dirtyDocument);

 // Assert
 assertFalse("Document is not valid", isValid);
 }

 /**
 * Test method isValid with valid document.
 */
 @Test
 public void testIsValidValidDocument() {
 // Arrange
 Document dirtyDocument = Jsoup.parse("<html><body><h1>Hello</h1></body></html>");
 Cleaner cleaner = new Cleaner(Whitelist.simpleText());

 // Act
 boolean isValid = cleaner.isValid(dirtyDocument);

 // Assert
 assertTrue("Document is valid", isValid);
 }

 /**
 * Test method copySafeNodes.
 */
 @Test
 public void testCopySafeNodes() {
 // Arrange
 Document dirtyDocument = Jsoup.parse("<html><body><script>alert('XSS')</script><h1 id='test' class='test' onclick='click()'>Hello</h1></body></html>");
 Cleaner cleaner = new Cleaner(Whitelist.simpleText());
 Element dest = new Element(Tag.BODY);
 int numDiscarded = 0;

 // Act
 numDiscarded = cleaner.copySafeNodes(dirtyDocument.body(), dest);

 // Assert
 assertEquals("<html><body></body></html>", dirtyDocument.outerHtml());
 assertEquals("<html><body></body></html>", dest.outerHtml());
 assertEquals(0, numDiscarded);
 }

 /**
 * Test method copySafeNodes with unsafe document.
 */
 @Test
 public void testCopySafeNodesUnsafeDocument() {
 // Arrange
 Document dirtyDocument = Jsoup.parse("<html><body><script>alert('XSS')</script><h1 id='test' class='test' onclick='click()'>Hello</h1></body></html>");
 Cleaner cleaner = new Cleaner(Whitelist.simpleText());
 Element dest = new Element(Tag.BODY);
 int numDiscarded = 0;

 // Act
 numDiscarded = cleaner.copySafeNodes(dirtyDocument.body(), dest);

 // Assert
 assertFalse("Document is not empty after cleaning", dirtyDocument.body().text().isEmpty());
 assertFalse("Destination is not empty after cleaning", dest.text().isEmpty());
 // number of discarded elements should be equal to the number of unsafe elements in the document
 // in this case there is one script tag, one id attribute and one onclick attribute
 assertEquals(3, numDiscarded);
 }
}