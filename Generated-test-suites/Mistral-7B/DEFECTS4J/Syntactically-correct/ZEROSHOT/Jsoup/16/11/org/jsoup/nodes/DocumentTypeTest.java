package org.jsoup.nodes.test;

import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.junit.Test;
import org.junit.Before;
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
 String publicId = "HTML 4.01";
 String systemId = "http://www.w3.org/TR/html4/strict.dtd";
 String baseUri = "https://www.example.com/";
 DocumentType doctype = new DocumentType(name, publicId, systemId, baseUri);

 assertEquals("#doctype", doctype.nodeName());
 assertEquals(name, doctype.attr("name").getValue());
 assertEquals(publicId, doctype.attr("publicId").getValue());
 assertEquals(systemId, doctype.attr("systemId").getValue());
 assertEquals(baseUri, doctype.baseUri());
 }

 @Test
 public void testDocumentTypeOuterHtmlHead() {
 DocumentType doctype = new DocumentType("html", "HTML 4.01", "http://www.w3.org/TR/html4/strict.dtd", null);
 StringBuilder accum = new StringBuilder();
 doctype.outerHtmlHead(accum, 0, null);

 assertEquals("<!DOCTYPE html PUBLIC \"HTML 4.01\"", accum.toString());
 }

 @Test
 public void testDocumentTypeOuterHtmlHeadWithPublicIdOnly() {
 DocumentType doctype = new DocumentType("html", "HTML 4.01", null, null);
 StringBuilder accum = new StringBuilder();
 doctype.outerHtmlHead(accum, 0, null);

 assertEquals("<!DOCTYPE html PUBLIC \"HTML 4.01\"", accum.toString());
 }

 @Test
 public void testDocumentTypeOuterHtmlHeadWithSystemIdOnly() {
 DocumentType doctype = new DocumentType("html", null, "http://www.w3.org/TR/html4/strict.dtd", null);
 StringBuilder accum = new StringBuilder();
 doctype.outerHtmlHead(accum, 0, null);

 assertEquals("<!DOCTYPE html SYSTEM \"http://www.w3.org/TR/html4/strict.dtd\"", accum.toString());
 }

 @Test
 public void testDocumentTypeOuterHtmlHeadWithBothPublicIdAndSystemId() {
 DocumentType doctype = new DocumentType("html", "HTML 4.01", "http://www.w3.org/TR/html4/strict.dtd", null);
 StringBuilder accum = new StringBuilder();
 doctype.outerHtmlHead(accum, 0, null);

 assertEquals("<!DOCTYPE html PUBLIC \"HTML 4.01\" SYSTEM \"http://www.w3.org/TR/html4/strict.dtd\"", accum.toString());
 }

 @Test
 public void testDocumentTypeOuterHtmlTail() {
 DocumentType doctype = new DocumentType("html", null, null, null);
 StringBuilder accum = new StringBuilder();
 doctype.outerHtmlTail(accum, 0, null);

 assertEquals("", accum.toString());
 }

 @Test
 public void testDocumentTypeCloneNode() throws CloneNotSupportedException {
 DocumentType originalDoctype = new DocumentType("html", "HTML 4.01", "http://www.w3.org/TR/html4/strict.dtd", null);
 DocumentType clonedDoctype = (DocumentType) originalDoctype.cloneNode(true);

 assertEquals(originalDoctype.nodeName(), clonedDoctype.nodeName());
 assertEquals(originalDoctype.attr("name").getValue(), clonedDoctype.attr("name").getValue());
 assertEquals(originalDoctype.attr("publicId").getValue(), clonedDoctype.attr("publicId").getValue());
 assertEquals(originalDoctype.attr("systemId").getValue(), clonedDoctype.attr("systemId").getValue());
 assertEquals(originalDoctype.baseUri(), clonedDoctype.baseUri());
 }

 @Test
 public void testDocumentTypeEquals() {
 DocumentType doctype1 = new DocumentType("html", "HTML 4.01", "http://www.w3.org/TR/html4/strict.dtd", null);
 DocumentType doctype2 = new DocumentType("html", "HTML 4.01", "http://www.w3.org/TR/html4/strict.dtd", null);
 DocumentType doctype3 = new DocumentType("html", "HTML 5.0", "http://www.w3.org/TR/html5/strict.dtd", null);

 assertEquals(doctype1, doctype1);
 assertEquals(doctype1, doctype2);
 assertNotEquals(doctype1, doctype3);
 }

 @Test
 public void testDocumentTypeToString() {
 DocumentType doctype = new DocumentType("html", null, null, null);

 assertEquals("#doctype[name=html]", doctype.toString());
 }

 @Test
 public void testDocumentTypeHashCode() {
 DocumentType doctype1 = new DocumentType("html", "HTML 4.01", "http://www.w3.org/TR/html4/strict.dtd", null);
 DocumentType doctype2 = new DocumentType("html", "HTML 4.01", "http://www.w3.org/TR/html4/strict.dtd", null);
 DocumentType doctype3 = new DocumentType("html", "HTML 5.0", "http://www.w3.org/TR/html5/strict.dtd", null);

 assertEquals(doctype1.hashCode(), doctype1.hashCode());
 assertEquals(doctype1.hashCode(), doctype2.hashCode());
 assertNotEquals(doctype1.hashCode(), doctype3.hashCode());
 }
}