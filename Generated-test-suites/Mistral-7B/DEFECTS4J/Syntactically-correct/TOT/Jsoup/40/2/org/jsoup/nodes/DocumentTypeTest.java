package org.jsoup.nodes.test;

import org.junit.Test;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.helper.StringUtil;
import static org.junit.Assert.*;

public class DocumentTypeTest {

 // Test case for DocumentType constructor
 @Test
 public void testDocumentTypeConstructor() {
 Document document = new Document();
 DocumentType doctype = new DocumentType("html", "HTML", "IT-DTD-HTML401-fr", document.baseUri());

 assertEquals("#doctype", doctype.nodeName());
 assertEquals("html", doctype.attr("name"));
 assertEquals("IT-DTD-HTML401-fr", doctype.attr("publicId"));
 assertEquals("HTML", doctype.attr("systemId"));
 assertEquals(document.baseUri(), doctype.baseUri());
 }

 // Test case for nodeName method
 @Test
 public void testNodeName() {
 Document document = new Document();
 DocumentType doctype = new DocumentType("html", "", "", document.baseUri());

 assertEquals("#doctype", doctype.nodeName());
 }

 // Test case for outerHtmlHead method
 @Test
 public void testOuterHtmlHead() {
 Document document = new Document();
 DocumentType doctype = new DocumentType("html", "HTML", "IT-DTD-HTML401-fr", document.baseUri());
 StringBuilder expected = new StringBuilder();
 expected.append("<!DOCTYPE html PUBLIC \"IT-DTD-HTML401-fr\" \"HTML\">");

 String actual = doctype.outerHtmlHead(new StringBuilder(), 0, Document.OutputSettings.defaultSettings());

 assertEquals(expected.toString(), actual);
 }

 // Test case for outerHtmlHead with empty publicId and systemId
 @Test
 public void testOuterHtmlHeadWithEmptyPublicIdAndSystemId() {
 Document document = new Document();
 DocumentType doctype = new DocumentType("html", "", "", document.baseUri());
 StringBuilder expected = new StringBuilder();
 expected.append("<!DOCTYPE html>");

 String actual = doctype.outerHtmlHead(new StringBuilder(), 0, Document.OutputSettings.defaultSettings());

 assertEquals(expected.toString(), actual);
 }

 // Test case for outerHtmlHead with null Document.OutputSettings
 @Test(expected = NullPointerException.class)
 public void testOuterHtmlHeadWithNullOutputSettings() {
 Document document = new Document();
 DocumentType doctype = new DocumentType("html", "", "", document.baseUri());
 doctype.outerHtmlHead(null, 0, null);
 }

 // Test case for empty constructor
 @Test
 public void testEmptyConstructor() {
 Document document = new Document();
 DocumentType doctype = new DocumentType(document.baseUri());

 assertEquals("#doctype", doctype.nodeName());
 assertNull(doctype.attr("name"));
 assertNull(doctype.attr("publicId"));
 assertNull(doctype.attr("systemId"));
 assertEquals(document.baseUri(), doctype.baseUri());
 }

 // Test case for getAttribute method with null attribute name
 @Test(expected = NullPointerException.class)
 public void testGetAttributeWithNullAttributeName() {
 Document document = new Document();
 DocumentType doctype = new DocumentType("html", "", "", document.baseUri());
 doctype.getAttribute(null);
 }
}