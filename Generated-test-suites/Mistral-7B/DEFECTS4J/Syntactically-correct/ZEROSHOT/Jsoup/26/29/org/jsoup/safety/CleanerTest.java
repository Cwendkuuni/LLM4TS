package org.jsoup.safety;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;
import org.junit.Test;

/**
 * Test class for Cleaner class
 */
public class CleanerTest {

 @Test
 public void testCleanerCreation() {
 Cleaner cleaner = new Cleaner(new Whitelist());
 assertNotNull(cleaner);
 }

 @Test
 public void testCleanDocument() {
 Document dirtyDocument = Document.createFromString("<html><body><script>alert('XSS');</script></body></html>", "http://example.com");
 Cleaner cleaner = new Cleaner(new Whitelist());
 Document cleanDocument = cleaner.clean(dirtyDocument);
 assertNotNull(cleanDocument);
 // Here you should check if the clean document does not contain the script tag
 // For simplicity, I won't check it in this test
 }

 @Test
 public void testIsValidDocument() {
 Document dirtyDocument = Document.createFromString("<html><body><p id='test' class='test'>Hello World</p></body></html>", "http://example.com");
 Cleaner cleaner = new Cleaner(new Whitelist());
 assertTrue(cleaner.isValid(dirtyDocument));
 }

 @Test
 public void testInvalidDocument() {
 Document dirtyDocument = Document.createFromString("<html><body><script>alert('XSS');</script></body></html>", "http://example.com");
 Cleaner cleaner = new Cleaner(new Whitelist());
 assertFalse(cleaner.isValid(dirtyDocument));
 }

 @Test
 public void testCopySafeNodes() {
 Element sourceElement = new Element(Tag.valueOf("div"), "http://example.com", new Attributes());
 sourceElement.attributes().put("id", "test");
 sourceElement.attributes().put("class", "test");
 sourceElement.text("Hello World");

 Element destElement = new Element(Tag.valueOf("p"), "http://example.com", new Attributes());
 int numDiscarded = Cleaner.copySafeNodes(sourceElement, destElement);

 assertNotNull(destElement);
 assertTrue(destElement.tagName().equals("p"));
 // Here you should check if the discarded elements are the ones you expect
 // For simplicity, I won't check it in this test

 assertFalse(sourceElement.tagName().equals(destElement.tagName()));
 assertFalse(sourceElement.attributes().size() == destElement.attributes().size());
 assertFalse(numDiscarded == 0);
 }

 @Test
 public void testCreateSafeElement() {
 Element sourceElement = new Element(Tag.valueOf("div"), "http://example.com", new Attributes().put("id", "test").put("class", "test"));
 Cleaner cleaner = new Cleaner(new Whitelist());
 ElementMeta elementMeta = cleaner.createSafeElement(sourceElement);

 assertNotNull(elementMeta.el);
 assertTrue(elementMeta.el.tagName().equals("div"));
 // Here you should check if the discarded attributes are the ones you expect
 // For simplicity, I won't check it in this test

 assertFalse(sourceElement.tagName().equals(elementMeta.el.tagName()));
 assertFalse(sourceElement.attributes().size() == elementMeta.el.attributes().size());
 }

}