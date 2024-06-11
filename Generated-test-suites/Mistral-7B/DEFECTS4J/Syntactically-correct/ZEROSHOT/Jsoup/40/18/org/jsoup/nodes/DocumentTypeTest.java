package org.jsoup.nodes.test;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.helper.StringUtil;

import static org.junit.Assert.*;

public class DocumentTypeTest {

 private Document document;

 @Before
 public void setup() {
 document = new Document();
 }

 @Test
 public void testDocumentTypeCreation() {
 String name = "html";
 String publicId = "publicId";
 String systemId = "systemId";
 String baseUri = "baseUri";
 DocumentType documentType = new DocumentType(name, publicId, systemId, baseUri);

 assertEquals("#doctype", documentType.nodeName());
 assertEquals(name, documentType.attr("name"));
 assertEquals(publicId, documentType.attr("publicId"));
 assertEquals(systemId, documentType.attr("systemId"));
 assertEquals(baseUri, documentType.baseUri());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDocumentTypeCreationWithEmptyName() {
 String name = "";
 String publicId = "publicId";
 String systemId = "systemId";
 String baseUri = "baseUri";
 DocumentType documentType = new DocumentType(name, publicId, systemId, baseUri);
 fail("Should throw IllegalArgumentException with empty name");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDocumentTypeCreationWithNullName() {
 String name = null;
 String publicId = "publicId";
 String systemId = "systemId";
 String baseUri = "baseUri";
 DocumentType documentType = new DocumentType(name, publicId, systemId, baseUri);
 fail("Should throw IllegalArgumentException with null name");
 }

 @Test
 public void testDocumentTypeOuterHtmlHead() {
 DocumentType documentType = new DocumentType("html", "publicId", "systemId", "baseUri");
 StringBuilder accum = new StringBuilder();
 documentType.outerHtmlHead(accum, 0, null);
 String expected = "<!DOCTYPE html>";
 assertEquals(expected, accum.toString());
 }

 @Test
 public void testDocumentTypeOuterHtmlHeadWithPublicId() {
 DocumentType documentType = new DocumentType("html", "publicId", "systemId", "baseUri");
 StringBuilder accum = new StringBuilder();
 documentType.outerHtmlHead(accum, 0, null);
 String expected = "<!DOCTYPE html PUBLIC \"publicId\" \"systemId\">";
 assertEquals(expected, accum.toString());
 }

 @Test
 public void testDocumentTypeOuterHtmlHeadWithSystemId() {
 DocumentType documentType = new DocumentType("html", "publicId", "systemId", "baseUri");
 StringBuilder accum = new StringBuilder();
 documentType.outerHtmlHead(accum, 0, null);
 String expected = "<!DOCTYPE html SYSTEM \"systemId\">";
 assertEquals(expected, accum.toString());
 }

 @Test
 public void testDocumentTypeOuterHtmlHeadWithPublicIdAndSystemId() {
 DocumentType documentType = new DocumentType("html", "publicId", "systemId", "baseUri");
 StringBuilder accum = new StringBuilder();
 documentType.outerHtmlHead(accum, 0, null);
 String expected = "<!DOCTYPE html PUBLIC \"publicId\" \"systemId\">";
 assertEquals(expected, accum.toString());
 }

 @Test(expected = NullPointerException.class)
 public void testDocumentTypeOuterHtmlHeadWithNullAccumulator() {
 DocumentType documentType = new DocumentType("html", "publicId", "systemId", "baseUri");
 documentType.outerHtmlHead(null, 0, null);
 fail("Should throw NullPointerException with null accumulator");
 }

 @Test(expected = NullPointerException.class)
 public void testDocumentTypeOuterHtmlHeadWithNullDocument() {
 DocumentType documentType = new DocumentType("html", "publicId", "systemId", "baseUri");
 Document document = null;
 documentType.outerHtmlHead(accum, 0, document);
 fail("Should throw NullPointerException with null document");
 }

 @Test
 public void testDocumentTypeOuterHtmlTail() {
 DocumentType documentType = new DocumentType("html", "publicId", "systemId", "baseUri");
 StringBuilder accum = new StringBuilder();
 documentType.outerHtmlTail(accum, 0, null);
 assertEquals("", accum.toString());
 }

 @Test
 public void testCreateDocumentTypeWithAttributes() {
 String name = "html";
 String publicId = "publicId";
 String systemId = "systemId";
 String baseUri = "baseUri";
 String quirksMode = "quirks";
 DocumentType documentType = new DocumentType(name, publicId, systemId, baseUri, new Attribute("", quirksMode));
 assertEquals("#doctype", documentType.nodeName());
 assertEquals(name, documentType.attr("name"));
 assertEquals(publicId, documentType.attr("publicId"));
 assertEquals(systemId, documentType.attr("systemId"));
 assertEquals(baseUri, documentType.baseUri());
 assertEquals(quirksMode, documentType.attr("quirksMode").value());
 }
}