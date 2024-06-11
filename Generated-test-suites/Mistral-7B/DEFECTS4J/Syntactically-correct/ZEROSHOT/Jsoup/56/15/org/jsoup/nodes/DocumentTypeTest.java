package org.jsoup.nodes.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.attributes.Attributes;
import org.jsoup.nodes.attributes.Attribute;
import org.jsoup.helper.StringUtil;

import org.junit.Test;

public class DocumentTypeTest {

 @Test
 public void testDocumentTypeCreation() {
 Document document = new Document();
 DocumentType documentType = new DocumentType("html", "HTML", "HTML", document.baseURI());

 assertEquals("#doctype", documentType.nodeName());
 assertEquals("html", documentType.attr(DocumentType.NAME));
 assertFalse(documentType.has(DocumentType.PUBLIC_ID));
 assertFalse(documentType.has(DocumentType.SYSTEM_ID));
 assertSame(document, documentType.parent());
 }

 @Test
 public void testDocumentTypeCreationWithPublicId() {
 Document document = new Document();
 DocumentType documentType = new DocumentType("html", "public", "publicID", document.baseURI());

 assertEquals("#doctype", documentType.nodeName());
 assertEquals("html", documentType.attr(DocumentType.NAME));
 assertEquals("publicID", documentType.attr(DocumentType.PUBLIC_ID));
 assertFalse(documentType.has(DocumentType.SYSTEM_ID));
 assertSame(document, documentType.parent());
 }

 @Test
 public void testDocumentTypeCreationWithSystemId() {
 Document document = new Document();
 DocumentType documentType = new DocumentType("html", null, "systemID", document.baseURI());

 assertEquals("#doctype", documentType.nodeName());
 assertEquals("html", documentType.attr(DocumentType.NAME));
 assertFalse(documentType.has(DocumentType.PUBLIC_ID));
 assertEquals("systemID", documentType.attr(DocumentType.SYSTEM_ID));
 assertSame(document, documentType.parent());
 }

 @Test
 public void testDocumentTypeCreationWithPublicIdAndSystemId() {
 Document document = new Document();
 DocumentType documentType = new DocumentType("html", "public", "publicID", "systemID", document.baseURI());

 assertEquals("#doctype", documentType.nodeName());
 assertEquals("html", documentType.attr(DocumentType.NAME));
 assertEquals("publicID", documentType.attr(DocumentType.PUBLIC_ID));
 assertEquals("systemID", documentType.attr(DocumentType.SYSTEM_ID));
 assertSame(document, documentType.parent());
 }

 @Test
 public void testDocumentTypeOuterHtmlHead() throws Exception {
 Document document = new Document();
 DocumentType documentType = new DocumentType("html", null, null, document.baseURI());

 Appendable accumulator = new StringBuilder();
 documentType.outerHtmlHead(accumulator, 1, Document.OutputSettings.html());

 assertEquals("<!doctype html>", accumulator.toString());
 }

 @Test
 public void testDocumentTypeOuterHtmlHeadWithoutDeclaration() throws Exception {
 Document document = new Document();
 DocumentType documentType = new DocumentType("html", null, null, document.baseURI());

 Appendable accumulator = new StringBuilder();
 documentType.outerHtmlHead(accumulator, 1, Document.OutputSettings.html());

 assertEquals("<!doctype html>", accumulator.toString());
 }

 @Test
 public void testDocumentTypeOuterHtmlHeadWithPublicId() throws Exception {
 Document document = new Document();
 DocumentType documentType = new DocumentType("html", "public", "publicID", document.baseURI());

 Appendable accumulator = new StringBuilder();
 documentType.outerHtmlHead(accumulator, 1, Document.OutputSettings.html());

 assertEquals("<!DOCTYPE html PUBLIC \"publicID\">", accumulator.toString());
 }

 @Test
 public void testDocumentTypeOuterHtmlHeadWithSystemId() throws Exception {
 Document document = new Document();
 DocumentType documentType = new DocumentType("html", null, "systemID", document.baseURI());

 Appendable accumulator = new StringBuilder();
 documentType.outerHtmlHead(accumulator, 1, Document.OutputSettings.html());

 assertEquals("<!DOCTYPE html SYSTEM \"systemID\">", accumulator.toString());
 }

 @Test
 public void testDocumentTypeOuterHtmlHeadWithPublicIdAndSystemId() throws Exception {
 Document document = new Document();
 DocumentType documentType = new DocumentType("html", "public", "publicID", "systemID", document.baseURI());

 Appendable accumulator = new StringBuilder();
 documentType.outerHtmlHead(accumulator, 1, Document.OutputSettings.html());

 assertEquals("<!DOCTYPE html PUBLIC \"publicID\" SYSTEM \"systemID\">", accumulator.toString());
 }

 @Test
 public void testDocumentTypeOuterHtmlTail() {
 Document document = new Document();
 DocumentType documentType = new DocumentType("html", null, null, document.baseURI());

 Appendable accumulator = new StringBuilder();
 documentType.outerHtmlTail(accumulator, 1, Document.OutputSettings.html());

 assertNull(accumulator.toString());
 }

 @Test
 public void testDocumentTypeHasAttribute() {
 Document document = new Document();
 DocumentType documentTypePublic = new DocumentType("html", "public", null, null, document.baseURI());
 DocumentType documentTypeSystem = new DocumentType("html", null, null, null, document.baseURI());
 DocumentType documentTypePublicWithSystem = new DocumentType("html", "public", null, "systemID", document.baseURI());

 assertTrue(documentTypePublic.has(DocumentType.NAME));
 assertFalse(documentTypePublic.has(DocumentType.PUBLIC_ID));
 assertFalse(documentTypePublic.has(DocumentType.SYSTEM_ID));

 assertTrue(documentTypeSystem.has(DocumentType.NAME));
 assertFalse(documentTypeSystem.has(DocumentType.PUBLIC_ID));
 assertFalse(documentTypeSystem.has(DocumentType.SYSTEM_ID));

 assertTrue(documentTypePublicWithSystem.has(DocumentType.NAME));
 assertFalse(documentTypePublicWithSystem.has(DocumentType.PUBLIC_ID));
 assertTrue(documentTypePublicWithSystem.has(DocumentType.SYSTEM_ID));
 }

 @Test
 public void testDocumentTypeWithAttributes() {
 Document document = new Document();
 DocumentType documentType = new DocumentType("html", "public", "publicID", "systemID", document.baseURI());

 assertEquals("publicID", documentType.attr(DocumentType.PUBLIC_ID));
 assertEquals("systemID", documentType.attr(DocumentType.SYSTEM_ID));

 Attributes attributes = documentType.attributes();
 Attribute publicIdAttribute = attributes.get("public-id");
 Attribute systemIdAttribute = attributes.get("system-id");

 assertEquals("publicID", publicIdAttribute.value());
 assertEquals("systemID", systemIdAttribute.value());
 }

 @Test
 public void testDocumentTypeToString() {
 Document document = new Document();
 DocumentType documentType = new DocumentType("html", "public", "publicID", "systemID", document.baseURI());

 assertEquals("<!DOCTYPE html PUBLIC \"publicID\" SYSTEM \"systemID\">", documentType.toString());
 }

 @Test
 public void testDocumentTypeCreateWithTextNodeChild() {
 Document document = new Document();
 DocumentType documentType = new DocumentType("html", "public", "publicID", document.baseURI());
 Element element = new Element("html");
 documentType.appendChild(new TextNode("test"));
 element.appendChild(documentType);

 assertEquals("#doctype", documentType.nodeName());
 assertEquals("html", documentType.attr(DocumentType.NAME));
 assertEquals("publicID", documentType.attr(DocumentType.PUBLIC_ID));
 assertEquals("systemID", documentType.attr(DocumentType.SYSTEM_ID));
 assertEquals(document, documentType.parent());
 assertEquals("test", element.text());
 }
}