package org.jsoup.nodes;

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;
import static org.junit.Assert.*;

public class DocumentTest {

 private Document document;

 @Before
 public void setUp() {
 document = Document.createShell("http://example.com");
 }

 @Test
 public void testDocumentCreation() {
 Document doc = new Document("http://example.com");
 assertEquals(Tag.valueOf("#root"), doc.tagName());
 assertNotNull(doc.baseUri());
 }

 @Test
 public void testCreateShell() {
 Document doc = Document.createShell("http://example.com");
 assertEquals(Tag.valueOf("html"), doc.head().tagName());
 assertEquals(Tag.valueOf("head"), doc.head().head().tagName());
 assertEquals(Tag.valueOf("body"), doc.body().tagName());
 }

 @Test
 public void testHeadAccess() {
 document.title("Test Title");
 Element head = document.head();
 Element title = head.getElementsByTag("title").first();
 assertEquals("Test Title", title.text().trim());
 }

 @Test
 public void testBodyAccess() {
 Element body = document.body();
 body.appendText("Test Body");
 assertEquals("Test Body", body.text().trim());
 }

 @Test
 public void testTitleAccessAndSet() {
 document.title("Test Title");
 String title = document.title();
 assertEquals("Test Title", title);
 document.title("New Title");
 assertEquals("New Title", document.title());
 }

 @Test
 public void testCreateElement() {
 Element link = document.createElement("a");
 assertEquals(Tag.valueOf("a"), link.tagName());
 }

 @Test
 public void testNormalise() {
 document.text("Test text in document");
 document.title("Test title");
 document.appendElement("html").appendElement("head").appendElement("body");
 document.body().appendText("Test text in body");
 document.head().appendElement("title").text("Test title in head");
 document.normalise();
 List<Element> bodyElements = document.body().select("p");
 assertEquals(1, bodyElements.size());
 Element bodyElement = bodyElements.get(0);
 assertEquals("Test text in document", bodyElement.text().trim());
 }

 @Test(expected = NullPointerException.class)
 public void testTitleNullArgument() {
 document.title(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidBaseUri() {
 try {
 Document doc = new Document(null);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }
}