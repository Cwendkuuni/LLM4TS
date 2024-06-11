package org.jsoup.nodes.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Document.OutputSettings;

public class DocumentTypeTest {

 @Test
 public void testDocumentTypeCreation() {
 Document doc = new Document();
 DocumentType docType = new DocumentType("html", "HTML", "html", doc.baseUri());

 assertEquals("#doctype", docType.nodeName());
 assertEquals("html", docType.attr(DocumentType.NAME));
 assertNull(docType.attr(DocumentType.PUBLIC_ID));
 assertNull(docType.attr(DocumentType.SYSTEM_ID));
 assertNotNull(docType.attributes());
 }

 @Test
 public void testDocumentTypeCreationWithPublicId() {
 Document doc = new Document();
 DocumentType docType = new DocumentType("html", "PUBLIC -1 -//W3C//DTD HTML 4.01//EN", "html.dtd", doc.baseUri());

 assertEquals("#doctype", docType.nodeName());
 assertEquals("html", docType.attr(DocumentType.NAME));
 assertEquals("PUBLIC -1 -//W3C//DTD HTML 4.01//EN", docType.attr(DocumentType.PUBLIC_ID));
 assertNull(docType.attr(DocumentType.SYSTEM_ID));
 assertNotNull(docType.attributes());
 }

 @Test
 public void testDocumentTypeCreationWithSystemId() {
 Document doc = new Document();
 DocumentType docType = new DocumentType("html", "", "system.dtd", doc.baseUri());

 assertEquals("#doctype", docType.nodeName());
 assertEquals("html", docType.attr(DocumentType.NAME));
 assertNull(docType.attr(DocumentType.PUBLIC_ID));
 assertEquals("system.dtd", docType.attr(DocumentType.SYSTEM_ID));
 assertNotNull(docType.attributes());
 }

 @Test
 public void testDocumentTypeCreationWithPublicIdAndSystemId() {
 Document doc = new Document();
 DocumentType docType = new DocumentType("html", "PUBLIC -1 -//W3C//DTD HTML 4.01//EN", "system.dtd", doc.baseUri());

 assertEquals("#doctype", docType.nodeName());
 assertEquals("html", docType.attr(DocumentType.NAME));
 assertEquals("PUBLIC -1 -//W3C//DTD HTML 4.01//EN", docType.attr(DocumentType.PUBLIC_ID));
 assertEquals("system.dtd", docType.attr(DocumentType.SYSTEM_ID));
 assertNotNull(docType.attributes());
 }

 @Test
 public void testHasAttribute() {
 Document doc = new Document();
 DocumentType docType = new DocumentType("html", "PUBLIC -1 -//W3C//DTD HTML 4.01//EN", "system.dtd", doc.baseUri());

 assertTrue(docType.has(DocumentType.NAME));
 assertFalse(docType.has("someOtherAttribute"));
 }

 @Test
 public void testOuterHtmlHead() throws IOException {
 Document doc = new Document();
 DocumentType docType = new DocumentType("html", "PUBLIC -1 -//W3C//DTD HTML 4.01//EN", "system.dtd", doc.baseUri());

 Appendable accumulator = new StringBuilder();
 docType.outerHtmlHead(accumulator, 0, OutputSettings.html5());

 String expected = "<!doctype html PUBLIC \"-//W3C//DTD HTML 4.01//EN\" \"system.dtd\">";
 assertEquals(expected, accumulator.toString());
 }

 @Test
 public void testOuterHtmlTail() throws IOException {
 Document doc = new Document();
 DocumentType docType = new DocumentType("html", "PUBLIC -1 -//W3C//DTD HTML 4.01//EN", "system.dtd", doc.baseUri());

 Element root = new Element("html");
 doc.appendChild(root);
 docType.outerHtmlHead(doc.bodyAppendable(), 0, OutputSettings.html5());
 docType.outerHtmlTail(doc.bodyAppendable(), 0, OutputSettings.html5());

 String expected = "<!doctype html PUBLIC \"-//W3C//DTD HTML 4.01//EN\" \"system.dtd\">\n<html>\n<body>\n";
 assertEquals(expected, doc.body().html());
 }
}