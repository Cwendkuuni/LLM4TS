package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.nio.charset.Charset;

public class DocumentTest {

 @Test
 public void testEmptyDocumentCreation() {
 Document doc = new Document("http://example.com");
 assertEquals("#document", doc.nodeName());
 assertNotNull(doc.outputSettings());
 assertNotNull(doc.head());
 assertNotNull(doc.body());
 }

 @Test
 public void testCreateShell() {
 Document doc = Document.createShell("http://example.com");
 Element html = doc.head();
 assertEquals("html", html.tagName());
 assertEquals("head", html.firstChild().tagName());
 assertEquals("body", html.lastChild().tagName());
 }

 @Test
 public void testGetTitle() {
 Document doc = new Document("http://example.com");
 doc.title("Test Title");
 assertEquals("Test Title", doc.title());
 }

 @Test(expected = NullPointerException.class)
 public void testNullTitle() {
 Document doc = new Document("http://example.com");
 doc.title(null);
 }

 @Test
 public void testCreateElement() {
 Document doc = new Document("http://example.com");
 Element p = doc.createElement("p");
 assertEquals("p", p.tagName());
 }

 @Test
 public void testNormalise() {
 Document doc = new Document("http://example.com");
 doc.body().appendText(" <html><head><title>Test</title></head>");
 assertEquals(2, doc.childNodes().size());
 doc.normalise();
 assertEquals(1, doc.childNodes().size());
 }

 @Test
 public void testSetText() {
 Document doc = new Document("http://example.com");
 doc.text("Test Text");
 assertEquals("Test Text", doc.outerHtml());
 }

 @Test
 public void testClone() {
 Document doc = new Document("http://example.com");
 Document clone = doc.clone();
 assertEquals("#document", clone.nodeName());
 }

 @Test
 public void testOutputSettings() {
 Document doc = new Document("http://example.com");
 doc.outputSettings().prettyPrint(false);
 Charset charset = Charset.forName("UTF-16BE");
 doc.outputSettings().charset(charset);
 assertEquals(false, doc.outputSettings().prettyPrint());
 assertEquals("UTF-16BE", doc.outputSettings().charset().name());
 }
}