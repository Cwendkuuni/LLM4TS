package org.jsoup.nodes;

import org.junit.Assert; import org.junit.Test; import org.jsoup.nodes.Document; import org.jsoup.nodes.Element; import org.jsoup.nodes.OutputSettings;

public class DocumentTest {

 @Test
 public void testCreateDocument() {
 Document doc = new Document("http://example.com");
 Assert.assertEquals("http://example.com", doc.baseUri());
 Assert.assertNull(doc.head());
 Assert.assertNull(doc.body());
 }

 @Test
 public void testCreateShellDocument() {
 Document doc = Document.createShell("http://example.com");
 Assert.assertEquals("http://example.com", doc.baseUri());
 Assert.assertNotNull(doc.head());
 Assert.assertNotNull(doc.body());
 }

 @Test
 public void testAccessHeadElement() {
 Document doc = Document.createShell("http://example.com");
 Element head = doc.head();
 Assert.assertNotNull(head);
 }

 @Test
 public void testAccessBodyElement() {
 Document doc = Document.createShell("http://example.com");
 Element body = doc.body();
 Assert.assertNotNull(body);
 }

 @Test
 public void testGetDocumentTitle() {
 Document doc = Document.createShell("http://example.com");
 doc.title("Test Title");
 String title = doc.title();
 Assert.assertEquals("Test Title", title);
 }

 @Test
 public void testSetDocumentTitle() {
 Document doc = Document.createShell("http://example.com");
 doc.title("Test Title");
 String originalTitle = doc.title();
 doc.title("New Title");
 String newTitle = doc.title();
 Assert.assertEquals("New Title", newTitle);
 Assert.assertNotEquals(originalTitle, newTitle);
 }

 @Test
 public void testCreateElement() {
 Document doc = Document.createShell("http://example.com");
 Element element = doc.createElement("div");
 Assert.assertNotNull(element);
 }

 @Test
 public void testNormaliseDocument() {
 Document doc = Document.createShell("http://example.com");
 doc.normalise();
 Element body = doc.body();
 List<Node> textNodes = body.childNodes();
 for (Node node : textNodes) {
 if (node instanceof TextNode) {
 TextNode textNode = (TextNode) node;
 Assert.assertFalse(textNode.isBlank());
 }
 }
 }

 @Test
 public void testFindFirstElementByTagName() {
 Document doc = Document.createShell("http://example.com");
 Element head = doc.findFirstElementByTagName("head", doc);
 Assert.assertNotNull(head);
 }

 @Test
 public void testFindFirstElementByTagNameNull() {
 Document doc = Document.createShell("http://example.com");
 Element element = doc.findFirstElementByTagName("nonexistentTag", doc);
 Assert.assertNull(element);
 }

 @Test
 public void testOuterHtml() {
 Document doc = Document.createShell("http://example.com");
 String html = doc.outerHtml();
 // Add assertions for the expected HTML output
 }

 @Test
 public void testText() {
 Document doc = Document.createShell("http://example.com");
 doc.text("Test Text");
 String text = doc.text();
 Assert.assertEquals("Test Text", text);
 }

 @Test
 public void testNodeName() {
 Document doc = Document.createShell("http://example.com");
 String nodeName = doc.nodeName();
 Assert.assertEquals("#document", nodeName);
 }

 @Test
 public void testOutputSettings() {
 Document doc = Document.createShell("http://example.com");
 OutputSettings outputSettings = doc.outputSettings();
 // Add assertions for the expected output settings
 }
}