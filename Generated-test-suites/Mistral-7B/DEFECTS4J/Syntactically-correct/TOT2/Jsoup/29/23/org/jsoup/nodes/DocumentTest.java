package org.jsoup.nodes;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.OutputSettings;
import org.jsoup.nodes.QuirksMode;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

public class DocumentTest {

 @Test
 public void testNewDocument() {
 Document doc = new Document("baseUri");
 assertEquals("#document", doc.nodeName());
 assertNull(doc.title());
 assertNull(doc.head());
 assertNull(doc.body());
 }

 @Test
 public void testCreateShell() {
 Document doc = Document.createShell("baseUri");
 Element html = doc.head();
 Element head = html.head();
 Element body = html.body();
 assertEquals("#document", doc.nodeName());
 assertNull(doc.title());
 assertSame(html, doc.head());
 assertSame(head, html.head());
 assertSame(body, html.body());
 }

 @Test
 public void testGetTitle() {
 Document doc = new Document("baseUri");
 doc.title("Test Title");
 assertEquals("Test Title", doc.title());
 }

 @Test(expected = NullPointerException.class)
 public void testSetTitleNull() {
 Document doc = new Document("baseUri");
 doc.title(null);
 }

 @Test
 public void testCreateElement() {
 Document doc = new Document("baseUri");
 Element p = doc.createElement("p");
 assertEquals("p", p.nodeName());
 }

 @Test
 public void testNormalise() {
 Document doc = new Document("baseUri");
 doc.appendText("Test text before normalise");
 Element p = doc.createElement("p");
 p.text("Test text inside p");
 doc.appendElement(p);
 doc.normalise();
 assertEquals("Test text inside pTest text before normalise", doc.outerHtml());
 }

 @Test
 public void testFindFirstElementByTagName() {
 Document doc = new Document("baseUri");
 Element html = doc.appendElement("html");
 Element head = doc.appendElement("head");
 Element body = doc.appendElement("body");
 Element title = doc.createElement("title");
 title.text("Test Title");
 doc.head().appendChild(title);
 Element p = doc.createElement("p");
 doc.body().appendChild(p);
 Element found = doc.findFirstElementByTagName("title", doc);
 assertEquals("title", found.nodeName());
 assertEquals("Test Title", found.text());
 }

 @Test
 public void testFindFirstElementByTagNameNull() {
 Document doc = new Document("baseUri");
 Element head = doc.head();
 Element body = doc.body();
 Element found = doc.findFirstElementByTagName("p", doc);
 assertNull(found);
 }

 @Test
 public void testOutputSettings() {
 OutputSettings outputSettings = new OutputSettings();
 OutputSettings clone = outputSettings.clone();
 assertEquals(outputSettings, clone);
 }

 @Test
 public void testQuirksMode() {
 Document doc = new Document("baseUri");
 doc.quirksMode(Document.QuirksMode.quirks);
 assertEquals(Document.QuirksMode.quirks, doc.quirksMode());
 }
}