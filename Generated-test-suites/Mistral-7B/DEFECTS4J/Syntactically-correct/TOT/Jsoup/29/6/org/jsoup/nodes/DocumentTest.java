package org.jsoup.nodes;

import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;

public class DocumentTest {

 @Test
 public void testCreateDocument() {
 Document doc = new Document("baseURI");
 Assert.assertEquals("#document", doc.nodeName());
 Assert.assertNull(doc.head());
 Assert.assertNull(doc.body());
 }

 @Test
 public void testCreateShell() {
 Document doc = Document.createShell("baseURI");
 Element html = doc.head();
 Assert.assertEquals("html", html.nodeName());
 Element head = html.getFirstElementChild();
 Assert.assertEquals("head", head.nodeName());
 Element body = head.getNextElementSibling();
 Assert.assertEquals("body", body.nodeName());
 }

 @Test
 public void testGetAndSetTitle() {
 Document doc = new Document("baseURI");
 doc.title("Test Title");
 Element title = doc.head().getFirstElementChild();
 Assert.assertEquals("Test Title", title.text());
 doc.title("");
 Assert.assertNull(title.text());
 }

 @Test
 public void testCreateElement() {
 Document doc = new Document("baseURI");
 Element element = doc.createElement("test");
 Assert.assertEquals("test", element.nodeName());
 }

 @Test
 public void testNormalise() {
 Document doc = new Document("baseURI");
 doc.text("Test text in document");
 Element p = doc.createElement("p");
 p.text("Test text in paragraph");
 doc.body().appendChild(p);
 doc.normalise();
 TextNode textInDocument = (TextNode) doc.getElementsByTag("body").first().child(0);
 TextNode textInParagraph = (TextNode) p.childNodes().get(0);
 Assert.assertEquals("Test text in document", textInDocument.text());
 Assert.assertEquals("Test text in paragraph", textInParagraph.text());
 }

 @Test(expected = NullPointerException.class)
 public void testNullBaseURI() {
 Document doc = new Document(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidTagName() {
 Document doc = new Document("baseURI");
 doc.createElement("invalidTagName");
 }
}