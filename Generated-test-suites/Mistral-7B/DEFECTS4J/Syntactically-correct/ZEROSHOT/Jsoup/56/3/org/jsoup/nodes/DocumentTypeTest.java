package org.jsoup.nodes.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;

public class DocumentTypeTest {

 @Test
 public void testDocumentTypeCreation() {
 Document doc = new Document();
 DocumentType docType = new DocumentType("html", "HTML", "HTML", doc.baseUri());

 assertEquals("#doctype", docType.nodeName());
 assertNotNull(docType.outerHtml());
 }

 @Test
 public void testDocumentTypeCreationWithAttributes() {
 Document doc = new Document();
 DocumentType docType = new DocumentType("html", "PUBLIC-ID:ID", "SYSTEM-ID:ID", doc.baseUri());

 assertEquals("#doctype", docType.nodeName());
 assertNotNull(docType.outerHtml());

 assertTrue(docType.has("PUBLIC-ID"));
 assertEquals("PUBLIC-ID:ID", docType.attr("PUBLIC_ID"));

 assertTrue(docType.has("SYSTEM-ID"));
 assertEquals("SYSTEM-ID:ID", docType.attr("SYSTEM_ID"));
 }

 @Test
 public void testDocumentTypeCreationWithoutAttributes() {
 Document doc = new Document();
 DocumentType docType = new DocumentType("html", null, null, doc.baseUri());

 assertEquals("#doctype", docType.nodeName());
 assertNotNull(docType.outerHtml());

 assertFalse(docType.has("PUBLIC_ID"));
 assertNull(docType.attr("PUBLIC_ID"));

 assertFalse(docType.has("SYSTEM_ID"));
 assertNull(docType.attr("SYSTEM_ID"));
 }

 @Test
 public void testDocumentTypeOuterHtmlHead() throws Exception {
 Document doc = new Document();
 DocumentType docType = new DocumentType("html", "PUBLIC-ID:ID", "SYSTEM-ID:ID", doc.baseUri());
 Document docWithDocType = doc.appendDocument(docType);

 StringBuilder expectedHtml = new StringBuilder();
 expectedHtml.append("<!DOCTYPE html PUBLIC \"PUBLIC-ID:ID\" \"SYSTEM-ID:ID\">");

 assertEquals(expectedHtml.toString(), docWithDocType.selectFirst("#doctype").outerHtml());
 }

 @Test
 public void testDocumentTypeOuterHtmlTail() throws Exception {
 Document doc = new Document();
 DocumentType docType = new DocumentType("html", null, null, doc.baseUri());
 Document docWithDocType = doc.appendDocument(docType);

 StringBuilder expectedHtml = new StringBuilder();
 // The outerHtmlTail method of DocumentType is empty

 assertEquals(expectedHtml.toString(), docWithDocType.selectFirst("#doctype").outerHtml());
 }
}