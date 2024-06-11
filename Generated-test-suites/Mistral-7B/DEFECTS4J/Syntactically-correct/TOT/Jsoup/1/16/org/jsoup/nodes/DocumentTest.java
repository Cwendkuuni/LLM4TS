package org.jsoup.nodes;

import org.apache.commons.lang3.StringUtils;
import org.junit.After;
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
 public void testCreateDocument() {
 Document doc = new Document("http://example.com");
 assertNotNull(doc);
 assertEquals(Tag.valueOf("#root"), doc.tagName());
 }

 @Test
 public void testCreateShell() {
 Document doc = Document.createShell("http://example.com");
 assertNotNull(doc);
 assertTrue(doc.head() != null);
 assertTrue(doc.body() != null);
 }

 @Test
 public void testGetHeadElement() {
 Element headElement = document.head();
 assertNotNull(headElement);
 assertEquals(Tag.HEAD, headElement.tagName());
 }

 @Test
 public void testGetBodyElement() {
 Element bodyElement = document.body();
 assertNotNull(bodyElement);
 assertEquals(Tag.BODY, bodyElement.tagName());
 }

 @Test
 public void testGetTitle() {
 String title = document.title();
 assertEquals(StringUtils.EMPTY, title);
 }

 @Test
 public void testSetTitle() {
 document.title("Test Title");
 String title = document.title();
 assertEquals("Test Title", title);
 }

 @Test
 public void testCreateElement() {
 Element newElement = document.createElement("p");
 assertNotNull(newElement);
 assertEquals(Tag.P, newElement.tagName());
 }

 @Test
 public void testNormalise() {
 Document doc = Jsoup.parse("<html><head><title>Test Title</title></head></html>");
 doc.title("New Title");
 doc.appendElement("body").appendText("Test text.");
 doc.normalise();
 assertEquals("Test Title", doc.title());
 assertEquals("Test text.\n", doc.body().text());
 }

 @Test(expected = NullPointerException.class)
 public void testSetTitleWithNullTitle() {
 document.title(null);
 }

 @Test
 public void testOuterHtml() {
 Document doc = Jsoup.parse("<html><head><title>Test Title</title></head><body></body></html>");
 doc.title("New Title");
 doc.body().appendText("Test text.");
 assertEquals("<html><head><title>New Title</title></head><body>Test text.</body></html>", doc.outerHtml());
 }

 @Test
 public void testText() {
 document.body().appendText("Test text.");
 document.text("New Text");
 assertEquals("New Text", document.body().text());
 }

 @After
 public void tearDown() {
 document = null;
 }

}