package org.jsoup.nodes.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.attributes.Attribute;
import org.jsoup.nodes.attributes.Attributes;
import org.jsoup.nodes.AttributesImpl;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for DocumentType.
 */
public class DocumentTypeTest {

 private Document document;

 @Before
 public void setUp() {
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
 assertEquals(name, documentType.attr(DocumentType.NAME));
 assertEquals(publicId, documentType.attr(DocumentType.PUBLIC_ID));
 assertEquals(systemId, documentType.attr(DocumentType.SYSTEM_ID));
 assertEquals(baseUri, documentType.baseUri());
 }

 @Test
 public void testDocumentTypeCreationWithEmptyAttributes() {
 DocumentType documentType = new DocumentType("html", null, null, null);

 assertEquals("#doctype", documentType.nodeName());
 assertNull(documentType.attr(DocumentType.PUBLIC_ID));
 assertNull(documentType.attr(DocumentType.SYSTEM_ID));
 assertNull(documentType.baseUri());
 }

 @Test
 public void testDocumentTypeCreationWithAttributes() {
 String name = "html";
 String publicId = "publicId";
 String systemId = "systemId";
 String baseUri = "baseUri";

 DocumentType documentType = new DocumentType(name, publicId, systemId, baseUri);
 Attributes attributes = documentType.attributes();
 AttributesImpl expectedAttributes = new AttributesImpl();
 expectedAttributes.put(new Attribute(DocumentType.PUBLIC_ID, publicId));
 expectedAttributes.put(new Attribute(DocumentType.SYSTEM_ID, systemId));

 assertEquals(expectedAttributes, attributes);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDocumentTypeCreationWhenNameIsNull() {
 String publicId = "publicId";
 String systemId = "systemId";
 String baseUri = "baseUri";

 // This test will throw an IllegalArgumentException as name is null
 new DocumentType(null, publicId, systemId, baseUri);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDocumentTypeCreationWhenPublicIdIsNull() {
 String name = "html";
 String systemId = "systemId";
 String baseUri = "baseUri";

 // This test will throw an IllegalArgumentException as publicId is null
 new DocumentType(name, null, systemId, baseUri);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDocumentTypeCreationWhenSystemIdIsNull() {
 String name = "html";
 String publicId = "publicId";
 String baseUri = "baseUri";

 // This test will throw an IllegalArgumentException as systemId is null
 new DocumentType(name, publicId, null, baseUri);
 }

 @Test
 public void testDocumentTypeNodeName() {
 DocumentType documentType = new DocumentType("html", null, null, null);

 assertEquals("#doctype", documentType.nodeName());
 }

 @Test
 public void testDocumentTypeOuterHtmlHead() throws IOException {
 DocumentType documentType = new DocumentType("html", null, null, null);
 Appendable accumulator = new StringBuilder();
 documentType.outerHtmlHead(accumulator, 0, Document.OutputSettings.syntaxDefault());

 String expected = "<!DOCTYPE html>";
 assertEquals(expected, accumulator.toString());
 }

 @Test
 public void testDocumentTypeOuterHtmlHeadWithPublicIdAndSystemId() throws IOException {
 DocumentType documentType = new DocumentType("html", "publicId", "systemId", null);
 Appendable accumulator = new StringBuilder();
 documentType.outerHtmlHead(accumulator, 0, Document.OutputSettings.syntaxDefault());

 String expected = "<!DOCTYPE html PUBLIC \"publicId\" \"systemId\">";
 assertEquals(expected, accumulator.toString());
 }

 @Test
 public void testDocumentTypeOuterHtmlHeadWithPublicIdOnly() throws IOException {
 DocumentType documentType = new DocumentType("html", "publicId", null, null);
 Appendable accumulator = new StringBuilder();
 documentType.outerHtmlHead(accumulator, 0, Document.OutputSettings.syntaxDefault());

 String expected = "<!DOCTYPE html PUBLIC \"publicId\">";
 assertEquals(expected, accumulator.toString());
 }

 @Test
 public void testDocumentTypeOuterHtmlHeadWithSystemIdOnly() throws IOException {
 DocumentType documentType = new DocumentType("html", null, "systemId", null);
 Appendable accumulator = new StringBuilder();
 documentType.outerHtmlHead(accumulator, 0, Document.OutputSettings.syntaxDefault());

 String expected = "<!DOCTYPE html SYSTEM \"systemId\">";
 assertEquals(expected, accumulator.toString());
 }

 @Test
 public void testDocumentTypeOuterHtmlTailIsEmpty() {
 DocumentType documentType = new DocumentType("html", null, null, null);

 assertTrue(documentType.outerHtmlTail(new StringBuilder(), 0, Document.OutputSettings.syntaxDefault()).toString().isEmpty());
 }

 @Test
 public void testDocumentTypeHasAttribute() {
 DocumentType documentType = new DocumentType("html", null, null, null);

 assertTrue(documentType.has(DocumentType.NAME));
 assertTrue(documentType.has(DocumentType.PUBLIC_ID));
 assertTrue(documentType.has(DocumentType.SYSTEM_ID));
 }

 @Test
 public void testDocumentTypeDoesNotHaveAttribute() {
 DocumentType documentType = new DocumentType("html", null, null, null);

 assertFalse(documentType.has("NON_EXISTANT_ATTRIBUTE"));
 }

 @Test
 public void testDocumentTypeNodeWithTextNodeAsChild() {
 Document document = new Document();
 DocumentType documentType = new DocumentType("html", null, null, null);
 TextNode textNode = new TextNode("text");
 documentType.appendChild(textNode);
 document.appendChild(documentType);

 assertEquals("#doctype", documentType.nodeName());
 assertEquals("text", textNode.nodeValue());
 assertEquals(1, documentType.childNodes().size());
 }
}