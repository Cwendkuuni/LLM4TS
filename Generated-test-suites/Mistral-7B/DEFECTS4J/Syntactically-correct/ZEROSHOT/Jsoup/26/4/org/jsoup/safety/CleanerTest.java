package org.jsoup.safety;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;
import org.junit.Before;
import org.junit.Test;

public class CleanerTest {

 @Before
 public void setup() {
 whitelist = new Whitelist.SimpleTagFilterElementWhitelist();
 cleaner = new Cleaner(whitelist);
 }

 private Whitelist whitelist;
 private Cleaner cleaner;

 @Test
 public void testCreateCleaner() {
 Cleaner cleaner = new Cleaner(whitelist);
 assertTrue(cleaner instanceof Cleaner);
 }

 @Test(expected = NullPointerException.class)
 public void testCreateCleanerWithNullWhitelist() {
 new Cleaner(null);
 }

 @Test
 public void testCleanDocument() {
 Document dirtyDocument = Document.createFromString("<html><body><div id='test' onmouseover='alert(\"test\")'><p>Hello World!</p></div></body></html>", null);
 Document cleanDocument = cleaner.clean(dirtyDocument);

 assertEquals("<html><body><p>Hello World!</p></body></html>", cleanDocument.html());
 assertFalse(cleanDocument.body().childNodes().hasTextNodeWithText("test"));
 }

 @Test
 public void testCleanDocumentWithEmptyDocument() {
 Document dirtyDocument = Document.createEmpty();
 Document cleanDocument = cleaner.clean(dirtyDocument);
 assertSame(dirtyDocument, cleanDocument);
 }

 @Test
 public void testCleanDocumentWithNullDocument() {
 Document cleanDocument = cleaner.clean(null);
 assertNull(cleanDocument);
 }

 @Test
 public void testIsValidDocument() {
 Document dirtyDocument = Document.createFromString("<html><body><div id='test' onmouseover='alert(\"test\")'><p>Hello World!</p></div></body></html>", null);
 assertTrue(cleaner.isValid(dirtyDocument));
 }

 @Test
 public void testIsValidDocumentWithInvalidDocument() {
 Document dirtyDocument = Document.createFromString("<html><body><script>alert(\"test\")</script></body></html>", null);
 assertFalse(cleaner.isValid(dirtyDocument));
 }

 @Test
 public void testIsValidDocumentWithEmptyDocument() {
 Document dirtyDocument = Document.createEmpty();
 assertTrue(cleaner.isValid(dirtyDocument));
 }

 @Test
 public void testIsValidDocumentWithNullDocument() {
 assertFalse(cleaner.isValid(null));
 }

 @Test
 public void testCopySafeNodes() {
 Element dirtyElement = new Element(Tag.P, null, new Attributes().put("id", "test"));
 Element cleanElement = new Element(Tag.P, null, new Attributes());
 int numDiscarded = cleaner.copySafeNodes(dirtyElement, cleanElement);
 assertEquals(0, numDiscarded);
 assertSame(dirtyElement.attribute("id"), cleanElement.attribute("id"));
 }

 @Test
 public void testCopySafeNodesWithUnsafeAttribute() {
 Element dirtyElement = new Element(Tag.P, null, new Attributes().put("onmouseover", "alert(\"test\")"));
 Element cleanElement = new Element(Tag.P, null, new Attributes());
 int numDiscarded = cleaner.copySafeNodes(dirtyElement, cleanElement);
 assertEquals(1, numDiscarded);
 assertNull(cleanElement.attribute("onmouseover"));
 }

 @Test
 public void testCopySafeNodesWithSafeAttribute() {
 Element dirtyElement = new Element(Tag.P, null, new Attributes().put("id", "test"));
 Element cleanElement = new Element(Tag.P, null, new Attributes().put("id", "test"));
 int numDiscarded = cleaner.copySafeNodes(dirtyElement, cleanElement);
 assertEquals(0, numDiscarded);
 assertSame(dirtyElement.attribute("id"), cleanElement.attribute("id"));
 }

 @Test
 public void testCopySafeNodesWithNullSourceElement() {
 Element cleanElement = new Element(Tag.P, null, new Attributes());
 int numDiscarded = cleaner.copySafeNodes(null, cleanElement);
 assertEquals(0, numDiscarded);
 assertNull(cleanElement);
 }

 @Test
 public void testCopySafeNodesWithNullDestinationElement() {
 Element dirtyElement = new Element(Tag.P, null, new Attributes());
 int numDiscarded = cleaner.copySafeNodes(dirtyElement, null);
 assertEquals(0, numDiscarded);
 assertNull(dirtyElement);
 }

 @Test
 public void testCreateSafeElement() {
 Element dirtyElement = new Element(Tag.DIV, null, new Attributes().put("id", "test").put("onmouseover", "alert(\"test\")"));
 ElementMeta meta = cleaner.createSafeElement(dirtyElement);
 assertEquals(Tag.P, meta.el.tagName());
 assertEquals(1, meta.numAttribsDiscarded);
 assertNull(meta.el.attribute("onmouseover"));
 }

 @Test
 public void testCreateSafeElementWithEmptyElement() {
 Element dirtyElement = new Element(Tag.P, null, new Attributes());
 ElementMeta meta = cleaner.createSafeElement(dirtyElement);
 assertEquals(Tag.P, meta.el.tagName());
 assertEquals(0, meta.numAttribsDiscarded);
 }

 @Test
 public void testCreateSafeElementWithSafeElement() {
 Element dirtyElement = new Element(Tag.DIV, null, new Attributes().put("id", "test"));
 ElementMeta meta = cleaner.createSafeElement(dirtyElement);
 assertEquals(Tag.DIV, meta.el.tagName());
 assertEquals(0, meta.numAttribsDiscarded);
 }

 @Test
 public void testCreateSafeElementWithNullElement() {
 ElementMeta meta = cleaner.createSafeElement(null);
 assertNull(meta.el);
 assertEquals(0, meta.numAttribsDiscarded);
 }

 private static class ElementMeta {
 Element el;
 int numAttribsDiscarded;

 ElementMeta(Element el, int numAttribsDiscarded) {
 this.el = el;
 this.numAttribsDiscarded = numAttribsDiscarded;
 }
 }
}