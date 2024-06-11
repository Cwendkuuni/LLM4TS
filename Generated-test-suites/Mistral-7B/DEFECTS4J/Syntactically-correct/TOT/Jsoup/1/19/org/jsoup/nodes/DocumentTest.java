package org.jsoup.nodes;

import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;

public class DocumentTest {

 @Test
 public void testCreateDocument() {
 Document doc = Document.createShell("http://example.com");
 Assert.assertNotNull(doc);
 Assert.assertNotNull(doc.body());
 Assert.assertNull(doc.title());
 }

 @Test
 public void testCreateDocumentWithTitle() {
 Document doc = Document.createShell("http://example.com");
 doc.title("Test Title");
 Element title = doc.head().selectFirst("title");
 Assert.assertNotNull(title);
 Assert.assertEquals("Test Title", title.text());
 }

 @Test
 public void testGetDocumentTitle() {
 Document doc = Document.createShell("http://example.com");
 doc.title("Test Title");
 Assert.assertEquals("Test Title", doc.title());
 Assert.assertEquals("", doc.title(null));
 }

 @Test
 public void testSetDocumentTitle() {
 Document doc = Document.createShell("http://example.com");
 doc.title(null);
 doc.title("Test Title");
 Element title = doc.head().selectFirst("title");
 Assert.assertNotNull(title);
 Assert.assertEquals("Test Title", title.text());
 }

 @Test
 public void testCreateElement() {
 Document doc = Document.createShell("http://example.com");
 Element p = doc.createElement("p");
 Assert.assertNotNull(p);
 Assert.assertEquals("http://example.com", p.baseUri());
 }

 @Test
 public void testNormaliseDocument() {
 Document doc = Document.createShell("http://example.com");
 doc.title("Test Title");
 doc.body().appendText("Test Text");
 doc.head().appendElement("p").appendText("Head Text");
 doc.normalise();
 List<Node> bodyNodes = doc.body().childNodes();
 Assert.assertEquals(2, bodyNodes.size());
 Assert.assertTrue(bodyNodes.get(0) instanceof TextNode);
 Assert.assertEquals("Test Text", ((TextNode) bodyNodes.get(0)).text());
 Assert.assertTrue(bodyNodes.get(1) instanceof Element);
 Assert.assertEquals("", bodyNodes.get(1).nodeName());
 List<Node> headNodes = doc.head().childNodes();
 Assert.assertEquals(1, headNodes.size());
 Assert.assertTrue(headNodes.get(0) instanceof Element);
 Assert.assertEquals("p", headNodes.get(0).nodeName());
 }

 @Test
 public void testSetDocumentText() {
 Document doc = Document.createShell("http://example.com");
 doc.title("Test Title");
 doc.text("Test Text");
 Element body = doc.body();
 Assert.assertNotNull(body);
 List<Node> bodyNodes = body.childNodes();
 Assert.assertEquals(1, bodyNodes.size());
 Assert.assertTrue(bodyNodes.get(0) instanceof TextNode);
 Assert.assertEquals("Test Text", ((TextNode) bodyNodes.get(0)).text());
 }

 @Test(expected = NullPointerException.class)
 public void testNullBaseUri() {
 Document doc = new Document(null);
 }
}