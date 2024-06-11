package org.jsoup.nodes.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.helper.StringUtil;
import static org.junit.Assert.*;
import org.junit.runner.JUnitCore;

@RunWith(JUnitCore.class)
public class DocumentTypeTest {

 @Test
 public void testDocumentTypeCreation() {
 Document doc = new Document();
 DocumentType docType = new DocumentType("html", "HTML_PUBLIC_-_W3C_org_DTD_HTML_4_01_transitional_en", "", doc.baseUri());
 assertEquals("#doctype", docType.nodeName());
 assertEquals("html", docType.attr("name"));
 assertEquals("HTML_PUBLIC_-_W3C_org_DTD_HTML_4_01_transitional_en", docType.attr("publicId"));
 assertEquals("", docType.attr("systemId"));
 }

 @Test
 public void testDocumentTypeOuterHtmlHead() {
 Document doc = new Document();
 DocumentType docType = new DocumentType("html", "HTML_PUBLIC_-_W3C_org_DTD_HTML_4_01_transitional_en", "", doc.baseUri());
 StringBuilder accum = new StringBuilder();
 docType.outerHtmlHead(accum, 0, null);
 assertEquals("<!DOCTYPE html PUBLIC \"HTML_PUBLIC_-_W3C_org_DTD_HTML_4_01_transitional_en\"", accum.toString());
 }

 @Test
 public void testDocumentTypeOuterHtmlTail() {
 Document doc = new Document();
 DocumentType docType = new DocumentType("html", "HTML_PUBLIC_-_W3C_org_DTD_HTML_4_01_transitional_en", "", doc.baseUri());
 StringBuilder accum = new StringBuilder();
 docType.outerHtmlTail(accum, 0, null);
 assertEquals("", accum.toString());
 }

 @Test
 public void testDocumentTypeSetPublicId() {
 Document doc = new Document();
 DocumentType docType = new DocumentType("html", "", "", doc.baseUri());
 docType.attr("publicId", "HTML_PUBLIC_-_W3C_org_DTD_HTML_4_01_transitional_en");
 assertEquals("HTML_PUBLIC_-_W3C_org_DTD_HTML_4_01_transitional_en", docType.attr("publicId"));
 }

 @Test
 public void testDocumentTypeSetSystemId() {
 Document doc = new Document();
 DocumentType docType = new DocumentType("html", "", "", doc.baseUri());
 docType.attr("systemId", "http://www.w3.org/TR/html4/loose.dtd");
 assertEquals("http://www.w3.org/TR/html4/loose.dtd", docType.attr("systemId"));
 }

 @Test
 public void testDocumentTypeSetBaseUri() {
 Document doc = new Document();
 DocumentType docType = new DocumentType("html", "", "", "");
 doc.setBaseUri("http://www.example.com/");
 docType = (DocumentType) doc.body();
 assertEquals("http://www.example.com/", docType.baseUri().toString());
 }

 @Test
 public void testDocumentTypeToString() {
 Document doc = new Document();
 DocumentType docType = new DocumentType("html", "HTML_PUBLIC_-_W3C_org_DTD_HTML_4_01_transitional_en", "", doc.baseUri());
 assertEquals("<!DOCTYPE html PUBLIC \"HTML_PUBLIC_-_W3C_org_DTD_HTML_4_01_transitional_en\">", docType.toString());
 }

 @Test
 public void testDocumentTypeEquals() {
 Document doc1 = new Document();
 DocumentType docType1 = new DocumentType("html", "HTML_PUBLIC_-_W3C_org_DTD_HTML_4_01_transitional_en", "", doc1.baseUri());
 Document doc2 = new Document();
 DocumentType docType2 = new DocumentType("html", "HTML_PUBLIC_-_W3C_org_DTD_HTML_4_01_transitional_en", "", doc2.baseUri());
 DocumentType docType3 = new DocumentType("html", "", "", doc1.baseUri());
 assertEquals(docType1, docType1);
 assertEquals(docType1, docType2);
 assertNotEquals(docType1, docType3);
 }

 @Test
 public void testDocumentTypeHashCode() {
 Document doc = new Document();
 DocumentType docType1 = new DocumentType("html", "HTML_PUBLIC_-_W3C_org_DTD_HTML_4_01_transitional_en", "", doc.baseUri());
 DocumentType docType2 = new DocumentType("html", "HTML_PUBLIC_-_W3C_org_DTD_HTML_4_01_transitional_en", "", doc.baseUri());
 DocumentType docType3 = new DocumentType("html", "", "", doc.baseUri());
 assertEquals(docType1.hashCode(), docType2.hashCode());
 assertNotEquals(docType1.hashCode(), docType3.hashCode());
 }

 @Test
 public void testDocumentTypeGetAttributes() {
 Document doc = new Document();
 DocumentType docType = new DocumentType("html", "HTML_PUBLIC_-_W3C_org_DTD_HTML_4_01_transitional_en", "", doc.baseUri());
 Attribute[] attributes = docType.attributes();
 assertEquals(3, attributes.length);
 assertEquals("publicId", attributes[0].name());
 assertEquals("HTML_PUBLIC_-_W3C_org_DTD_HTML_4_01_transitional_en", attributes[0].value());
 assertEquals("systemId", attributes[1].name());
 assertEquals("", attributes[1].value());
 assertEquals("name", attributes[2].name());
 assertEquals("html", attributes[2].value());
 }

 @Test
 public void testDocumentTypeGetChildren() {
 Document doc = new Document();
 DocumentType docType = new DocumentType("html", "HTML_PUBLIC_-_W3C_org_DTD_HTML_4_01_transitional_en", "", doc.baseUri());
 Node child = docType.childNodes().get(0);
 assertEquals("#text", child.nodeName());
 assertEquals("", ((TextNode) child).text());
 }

 @Test
 public void testDocumentTypeGetParentNode() {
 Document doc = new Document();
 DocumentType docType = new DocumentType("html", "HTML_PUBLIC_-_W3C_org_DTD_HTML_4_01_transitional_en", "", doc.baseUri());
 Element root = doc.createElement("html");
 doc.appendChild(root);
 root.appendChild(docType);
 Node parent = docType.parentNode();
 assertEquals("html", parent.nodeName());
 }

 @Test
 public void testDocumentTypeGetPreviousSibling() {
 Document doc = new Document();
 DocumentType docType1 = new DocumentType("html", "HTML_PUBLIC_-_W3C_org_DTD_HTML_4_01_transitional_en", "", doc.baseUri());
 DocumentType docType2 = new DocumentType("head", "", "", doc.baseUri());
 Node sibling = docType2.previousSibling();
 assertEquals(null, sibling);
 }

 @Test
 public void testDocumentTypeGetNextSibling() {
 Document doc = new Document();
 DocumentType docType1 = new DocumentType("html", "HTML_PUBLIC_-_W3C_org_DTD_HTML_4_01_transitional_en", "", doc.baseUri());
 DocumentType docType2 = new DocumentType("head", "", "", doc.baseUri());
 Node sibling = docType1.nextSibling();
 assertEquals(docType2, sibling);
 }
}