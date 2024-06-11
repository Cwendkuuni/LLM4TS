package org.jsoup.nodes.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;

import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.junit.Test;

public class DocumentTypeTest {

 /**
 * Test constructor.
 */
 @Test
 public void testConstructor() {
 DocumentType docType = new DocumentType("html", "HTML_PUBLIC_ID", "HTML_SYSTEM_ID", "/baseUri");
 assertEquals("#doctype", docType.nodeName());
 assertEquals("html", docType.attr("name"));
 assertEquals("HTML_PUBLIC_ID", docType.attr("publicId"));
 assertEquals("HTML_SYSTEM_ID", docType.attr("systemId"));
 assertEquals("/baseUri", docType.baseUri());
 }

 /**
 * Test nodeName method.
 */
 @Test
 public void testNodeName() {
 DocumentType docType = new DocumentType("html", null, null, null);
 assertEquals("#doctype", docType.nodeName());
 }

 /**
 * Test outerHtmlHead method.
 */
 @Test
 public void testOuterHtmlHead() {
 Document doc = new Document();
 DocumentType docType = new DocumentType("html", "HTML_PUBLIC_ID", "HTML_SYSTEM_ID", "/baseUri");
 doc.appendChild(docType);
 StringBuilder accum = new StringBuilder();
 docType.outerHtmlHead(accum, 0, null);
 String expected = "<!DOCTYPE html PUBLIC \"HTML_PUBLIC_ID\" \"HTML_SYSTEM_ID\">";
 assertEquals(expected, accum.toString());
 }

 /**
 * Test outerHtmlHead with empty publicId and systemId.
 */
 @Test
 public void testOuterHtmlHeadEmpty() {
 Document doc = new Document();
 DocumentType docType = new DocumentType("html", null, null, "/baseUri");
 doc.appendChild(docType);
 StringBuilder accum = new StringBuilder();
 docType.outerHtmlHead(accum, 0, null);
 String expected = "<!DOCTYPE html>";
 assertEquals(expected, accum.toString());
 }

 /**
 * Test outerHtmlTail method.
 */
 @Test
 public void testOuterHtmlTail() {
 Document doc = new Document();
 DocumentType docType = new DocumentType("html", "HTML_PUBLIC_ID", "HTML_SYSTEM_ID", "/baseUri");
 doc.appendChild(docType);
 StringBuilder accum = new StringBuilder();
 docType.outerHtmlTail(accum, 0, null);
 assertNull(accum.toString());
 }

 /**
 * Test equals method.
 */
 @Test
 public void testEquals() {
 Document doc = new Document();
 DocumentType docType1 = new DocumentType("html", "HTML_PUBLIC_ID_1", "HTML_SYSTEM_ID_1", "/baseUri_1");
 DocumentType docType2 = new DocumentType("html", "HTML_PUBLIC_ID_1", "HTML_SYSTEM_ID_1", "/baseUri_1");
 DocumentType docType3 = new DocumentType("html", "HTML_PUBLIC_ID_2", "HTML_SYSTEM_ID_2", "/baseUri_2");
 assertEquals(docType1, docType1);
 assertEquals(docType1, docType2);
 assertNotSame(docType1, docType3);
 }

 /**
 * Test hashCode method.
 */
 @Test
 public void testHashCode() {
 Document doc = new Document();
 DocumentType docType1 = new DocumentType("html", "HTML_PUBLIC_ID_1", "HTML_SYSTEM_ID_1", "/baseUri_1");
 DocumentType docType2 = new DocumentType("html", "HTML_PUBLIC_ID_1", "HTML_SYSTEM_ID_1", "/baseUri_1");
 DocumentType docType3 = new DocumentType("html", "HTML_PUBLIC_ID_2", "HTML_SYSTEM_ID_2", "/baseUri_2");
 assertEquals(docType1.hashCode(), docType1.hashCode());
 assertEquals(docType1.hashCode(), docType2.hashCode());
 assertNotEquals(docType1.hashCode(), docType3.hashCode());
 }

 /**
 * Test cloneNode method.
 */
 @Test
 public void testCloneNode() {
 Document doc = new Document();
 DocumentType docType = new DocumentType("html", "HTML_PUBLIC_ID", "HTML_SYSTEM_ID", "/baseUri");
 Node clonedNode = docType.cloneNode(true);
 assertEquals(docType, clonedNode);
 }

 /**
 * Test toString method.
 */
 @Test
 public void testToString() {
 DocumentType docType = new DocumentType("html", "HTML_PUBLIC_ID", "HTML_SYSTEM_ID", "/baseUri");
 assertEquals("<html>", docType.toString());
 }

 /**
 * Test getElementByTagName method.
 */
 @Test
 public void testGetElementByTagName() {
 Document doc = new Document();
 DocumentType docType = new DocumentType("html", "HTML_PUBLIC_ID", "HTML_SYSTEM_ID", "/baseUri");
 doc.appendChild(docType);
 Element element = doc.getElementsByTagNames("#doctype").first();
 assertSame(docType, element);
 }

 /**
 * Test addAttribute method.
 */
 @Test
 public void testAddAttribute() {
 DocumentType docType = new DocumentType("html", null, null, null);
 docType.addAttribute(new Attribute("name", "html"));
 assertEquals("html", docType.attr("name"));
 }

 /**
 * Test removeAttribute method.
 */
 @Test
 public void testRemoveAttribute() {
 DocumentType docType = new DocumentType("html", "HTML_PUBLIC_ID", "HTML_SYSTEM_ID", "/baseUri");
 docType.attr("name", "html");
 docType.removeAttribute("name");
 assertNull(docType.attr("name"));
 }
}