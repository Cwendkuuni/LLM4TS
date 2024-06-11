package org.jsoup.nodes;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test for Document class.
 */
public class DocumentTest {

 @Test
 public void testCreateDocument() {
 Document doc = Document.createShell("http://example.com");
 assertEquals("http://example.com", doc.baseUri());
 assertTrue(doc.head() != null);
 assertTrue(doc.body() != null);
 }

 @Test
 public void testGetTitle() {
 Document doc = Document.createShell("http://example.com");
 doc.title("Test Title");
 assertEquals("Test Title", doc.title());
 }

 @Test
 public void testSetTitle() {
 Document doc = Document.createShell("http://example.com");
 doc.title(null);
 assertNull(doc.title());
 doc.title("Test Title");
 assertEquals("Test Title", doc.title());
 }

 @Test
 public void testCreateElement() {
 Document doc = Document.createShell("http://example.com");
 Element p = doc.createElement("p");
 assertEquals("p", p.tagName());
 assertNull(p.baseUri());
 }

 @Test
 public void testNormalise() {
 Document doc = Jsoup.parse("<html><head><title>Test Title</title></head><body></body></html>");
 doc.normalise();
 Element titleEl = doc.head();
 assertNotNull(titleEl);
 assertEquals("Test Title", titleEl.text());
 }

 @Test(expected = NullPointerException.class)
 public void testTitleNullBaseUri() {
 Document doc = new Document(null);
 doc.title("Test Title");
 fail("Should throw NullPointerException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTitleEmptyString() {
 Document doc = Document.createShell("http://example.com");
 doc.title("");
 fail("Should throw IllegalArgumentException");
 }

 @Test
 public void testCreateDocumentWithEmptyBaseUri() {
 Document doc = new Document("");
 assertNull(doc.baseUri());
 }

 @Test
 public void testCreateDocumentNullBaseUri() {
 Document doc = new Document(null);
 assertNull(doc.baseUri());
 }

 @Test
 public void testCreateDocumentEmptyStringBaseUri() {
 Document doc = new Document("");
 assertNull(doc.baseUri());
 }

 @Test
 public void testCreateDocumentNullTagName() {
 Document doc = new Document("http://example.com");
 try {
 doc.createElement(null);
 fail("Should throw NullPointerException");
 } catch (NullPointerException e) {
 // expected
 }
 }

 @Test
 public void testTitleWithSpaces() {
 Document doc = Document.createShell("http://example.com");
 doc.title("Test Title with spaces");
 assertEquals("Test Title with spaces", doc.title());
 }

 @Test
 public void testTitleWithSpecialChars() {
 Document doc = Document.createShell("http://example.com");
 doc.title("Test Title<special>characters");
 String title = doc.title();
 assertTrue(StringUtils.contains(title, "&lt;special&gt;"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTitleWithLongString() {
 Document doc = Document.createShell("http://example.com");
 String longString = StringUtils.repeat("a", 256);
 doc.title(longString);
 fail("Should throw IllegalArgumentException");
 }
}