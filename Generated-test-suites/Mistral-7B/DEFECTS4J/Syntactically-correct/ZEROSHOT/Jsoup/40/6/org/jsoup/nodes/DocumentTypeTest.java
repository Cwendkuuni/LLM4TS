package org.jsoup.nodes.test;

import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.Attributes;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class DocumentTypeTest {

 @Test
 public void testDocumentTypeCreation() {
 DocumentType documentType = new DocumentType("html", "publicId", "systemId", null);

 assertEquals("#doctype", documentType.nodeName());
 assertEquals("html", documentType.attr("name"));
 assertEquals("publicId", documentType.attr("publicId"));
 assertEquals("systemId", documentType.attr("systemId"));
 assertNull(documentType.attr("id"));
 }

 @Test
 public void testDocumentTypeCreationWithEmptyAttributes() {
 DocumentType documentType = new DocumentType("html", null, null, null);

 assertEquals("#doctype", documentType.nodeName());
 assertNull(documentType.attr("name"));
 assertNull(documentType.attr("publicId"));
 assertNull(documentType.attr("systemId"));
 }

 @Test
 public void testDocumentTypeCreationWithNullName() {
 try {
 DocumentType documentType = new DocumentType(null, "publicId", "systemId", null);
 fail("Should have thrown an exception");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testDocumentTypeOuterHtmlHead() {
 Document document = new Document();
 DocumentType documentType = new DocumentType("html", "publicId", "systemId", null);
 document.appendChild(documentType);

 StringBuilder accum = new StringBuilder();
 documentType.outerHtmlHead(accum, 0, null);

 assertEquals("<!DOCTYPE html PUBLIC \"publicId\" \"systemId\">", accum.toString());
 }

 @Test
 public void testDocumentTypeOuterHtmlHeadWithEmptyPublicId() {
 Document document = new Document();
 DocumentType documentType = new DocumentType("html", "", "systemId", null);
 document.appendChild(documentType);

 StringBuilder accum = new StringBuilder();
 documentType.outerHtmlHead(accum, 0, null);

 assertEquals("<!DOCTYPE html>", accum.toString());
 }

 @Test
 public void testDocumentTypeOuterHtmlHeadWithEmptySystemId() {
 Document document = new Document();
 DocumentType documentType = new DocumentType("html", "publicId", "", null);
 document.appendChild(documentType);

 StringBuilder accum = new StringBuilder();
 documentType.outerHtmlHead(accum, 0, null);

 assertEquals("<!DOCTYPE html PUBLIC \"publicId\" \"\">", accum.toString());
 }

 @Test
 public void testDocumentTypeOuterHtmlTail() {
 Document document = new Document();
 DocumentType documentType = new DocumentType("html", "publicId", "systemId", null);
 document.appendChild(documentType);

 StringBuilder accum = new StringBuilder();
 documentType.outerHtmlTail(accum, 0, null);

 assertEquals("", accum.toString());
 }

 @Test
 public void testDocumentTypeNodeName() {
 Document document = new Document();
 DocumentType documentType = new DocumentType("html", "publicId", "systemId", null);
 document.appendChild(documentType);

 assertEquals("#doctype", documentType.nodeName());
 }

 @Test
 public void testDocumentTypeWithAttributes() {
 Document document = new Document();
 DocumentType documentType = new DocumentType("html", "publicId", "systemId", null);
 Attributes attrs = new Attributes();
 attrs.put("id", "htmlDoctype");
 documentType.setAttributeMap(attrs);
 document.appendChild(documentType);

 assertEquals("#doctype", documentType.nodeName());
 assertEquals("htmlDoctype", documentType.attr("id"));
 }

 @Test
 public void testDocumentTypeWithEmptyAttributesMap() {
 Document document = new Document();
 DocumentType documentType = new DocumentType("html", "publicId", "systemId", null);
 Attributes attrs = new Attributes();
 documentType.setAttributeMap(attrs);
 document.appendChild(documentType);

 assertEquals("#doctype", documentType.nodeName());
 assertNull(documentType.attr("id"));
 }

 @Test
 public void testDocumentTypeToString() {
 Document document = new Document();
 DocumentType documentType = new DocumentType("html", "publicId", "systemId", null);
 document.appendChild(documentType);

 assertEquals("<!DOCTYPE html PUBLIC \"publicId\" \"systemId\">", documentType.toString());
 }
}