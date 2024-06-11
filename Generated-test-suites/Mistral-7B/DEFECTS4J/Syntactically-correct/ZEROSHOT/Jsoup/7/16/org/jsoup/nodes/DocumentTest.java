package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.nio.charset.StandardCharsets;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.junit.Test;

/**
 * Test for Document class
 *
 * @author JohnDoe
 */
public class DocumentTest {

 private String baseUri = "http://example.com";

 @Test
 public void testDocumentCreation() {
 Document document = new Document(baseUri);
 assertNotNull(document);
 }

 @Test
 public void testDocumentCreateShell() {
 Document document = Document.createShell(baseUri);
 Document expectedDocument = new Document(baseUri);
 expectedDocument.appendElement("html");
 expectedDocument.appendElement("head");
 expectedDocument.appendElement("body");

 assertEquals(expectedDocument, document);
 }

 @Test
 public void testDocumentGetHeadElement() {
 Document document = new Document(baseUri);
 Element headElement = document.head();
 assertNotNull(headElement);
 }

 @Test
 public void testDocumentGetBodyElement() {
 Document document = new Document(baseUri);
 Element bodyElement = document.body();
 assertNotNull(bodyElement);
 }

 @Test
 public void testDocumentGetTitle() {
 Document document = new Document(baseUri);
 document.title("Test Title");
 String title = document.title();
 assertEquals("Test Title", title);
 }

 @Test
 public void testDocumentSetTitle() {
 Document document = new Document(baseUri);
 document.title(null);
 assertNull(document.title());

 document.title("Test Title");
 String title = document.title();
 assertEquals("Test Title", title);
 }

 @Test
 public void testDocumentCreateElement() {
 Document document = new Document(baseUri);
 Element element = document.createElement("testElement");
 assertNotNull(element);
 }

 @Test
 public void testDocumentNormalise() {
 Document document = new Document(baseUri);
 document.appendElement("html").appendElement("head").appendText("Test 1");
 document.appendElement("body").appendText("Test 2");
 document.normalise();

 Element html = document.findFirstElementByTagName("html");
 Element head = html.findFirstElementByTagName("head");
 Element body = html.findFirstElementByTagName("body");
 TextNode headText = (TextNode) head.child(0);
 TextNode bodyText = (TextNode) body.child(0);

 assertEquals("Test 1", headText.text());
 assertEquals("Test 2", bodyText.text());
 }

 @Test
 public void testDocumentOutputSettings() {
 Document document = new Document(baseUri);
 OutputSettings outputSettings = document.outputSettings();

 assertEquals(StandardCharsets.UTF_8, outputSettings.charset());
 assertTrue(outputSettings.prettyPrint());
 }

 @Test
 public void testDocumentOutputSettingsUpdate() {
 Document document = new Document(baseUri);
 OutputSettings outputSettings = document.outputSettings();

 outputSettings.charset("UTF-16");
 outputSettings.prettyPrint(false);

 assertEquals("UTF-16", outputSettings.charset().name());
 assertFalse(outputSettings.prettyPrint());
 }

}