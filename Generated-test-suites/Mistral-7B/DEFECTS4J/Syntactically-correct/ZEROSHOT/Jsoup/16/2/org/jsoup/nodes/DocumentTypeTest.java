package org.jsoup.nodes.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.selenium.htmlunit.HtmlUnitJsoup;

import org.junit.Test;

public class DocumentTypeTest {

 @Test
 public void testDocumentTypeCreation() {
 String name = "html";
 String publicId = "publicId";
 String systemId = "systemId";
 String baseUri = "baseUri";

 DocumentType documentType = new DocumentType(name, publicId, systemId, baseUri);
 assertNotNull(documentType);
 assertEquals("#doctype", documentType.nodeName());
 assertEquals(name, documentType.attr("name"));
 assertEquals(publicId, documentType.attr("publicId"));
 assertEquals(systemId, documentType.attr("systemId"));
 assertEquals(baseUri, documentType.baseUri());
 }

 @Test
 public void testDocumentTypeOuterHtml() {
 String name = "html";
 String publicId = "publicId";
 String systemId = "systemId";
 String baseUri = "baseUri";

 Document document = Jsoup.parse("<html><body></body></html>", baseUri);
 DocumentType documentType = new DocumentType(name, publicId, systemId, baseUri);
 document.insertBefore(documentType, document.body());

 StringBuilder accum = new StringBuilder();
 documentType.outerHtmlHead(accum, 0, null);
 documentType.outerHtmlTail(accum, 0, null);

 String expected = "<!DOCTYPE html" +
         (StringUtil.isBlank(publicId) ? "" : " PUBLIC \"\"\"publicId\"\"\"") +
         (StringUtil.isBlank(systemId) ? "" : " " + systemId + "\"") +
         ">";

 assertEquals(expected, accum.toString());
 }

 @Test
 public void testDocumentTypeOuterHtmlWithPublicIdAndSystemId() {
 String name = "html";
 String publicId = "publicId";
 String systemId = "systemId";
 String baseUri = "baseUri";

 Document document = Jsoup.parse("<html><body></body></html>", baseUri);
 DocumentType documentType = new DocumentType(name, publicId, systemId, baseUri);
 document.insertBefore(documentType, document.body());

 StringBuilder accum = new StringBuilder();
 documentType.outerHtmlHead(accum, 0, null);
 documentType.outerHtmlTail(accum, 0, null);

 String expected = "<!DOCTYPE html" +
         " PUBLIC \"publicId\" \"systemId\"";

 assertEquals(expected, accum.toString());
 }

 @Test
 public void testDocumentTypeWithEmptyPublicIdAndSystemId() {
 String name = "html";
 String publicId = "";
 String systemId = "";
 String baseUri = "baseUri";

 Document document = Jsoup.parse("<html><body></body></html>", baseUri);
 DocumentType documentType = new DocumentType(name, publicId, systemId, baseUri);
 document.insertBefore(documentType, document.body());

 StringBuilder accum = new StringBuilder();
 documentType.outerHtmlHead(accum, 0, null);
 documentType.outerHtmlTail(accum, 0, null);

 String expected = "<!DOCTYPE html>";

 assertEquals(expected, accum.toString());
 }

 @Test
 public void testDocumentTypeOuterHtmlWithNullBaseUri() {
 String name = "html";
 String publicId = "publicId";
 String systemId = "systemId";
 String baseUri = null;

 Document document = Jsoup.parse("<html><body></body></html>");
 DocumentType documentType = new DocumentType(name, publicId, systemId, baseUri);
 document.insertBefore(documentType, document.body());

 StringBuilder accum = new StringBuilder();
 documentType.outerHtmlHead(accum, 0, null);
 documentType.outerHtmlTail(accum, 0, null);

 String expected = "<!DOCTYPE html" +
         (StringUtil.isBlank(publicId) ? "" : " PUBLIC \"\"\"publicId\"\"\"") +
         (StringUtil.isBlank(systemId) ? "" : " " + systemId + "\"") +
         ">";

 assertEquals(expected, accum.toString());
 }

 @Test
 public void testDocumentTypeOuterHtmlWithEmptyBaseUri() {
 String name = "html";
 String publicId = "publicId";
 String systemId = "systemId";
 String baseUri = "";

 Document document = Jsoup.parse("<html><body></body></html>", baseUri);
 DocumentType documentType = new DocumentType(name, publicId, systemId, baseUri);
 document.insertBefore(documentType, document.body());

 StringBuilder accum = new StringBuilder();
 documentType.outerHtmlHead(accum, 0, null);
 documentType.outerHtmlTail(accum, 0, null);

 String expected = "<!DOCTYPE html" +
         (StringUtil.isBlank(publicId) ? "" : " PUBLIC \"\"\"publicId\"\"\"") +
         (StringUtil.isBlank(systemId) ? "" : " " + systemId + "\"") +
         ">";

 assertEquals(expected, accum.toString());
 }

 @Test
 public void testDocumentTypeOuterHtmlWithBlankAttributes() {
 String name = "html";
 String publicId = "";
 String systemId = "";
 String baseUri = "baseUri";

 Document document = Jsoup.parse("<html><body></body></html>", baseUri);
 DocumentType documentType = new DocumentType(name, publicId, systemId, baseUri);
 document.insertBefore(documentType, document.body());

 StringBuilder accum = new StringBuilder();
 documentType.outerHtmlHead(accum, 0, null);
 documentType.outerHtmlTail(accum, 0, null);

 String expected = "<!DOCTYPE html>";

 assertEquals(expected, accum.toString());
 }

 @Test
 public void testDocumentTypeWithHtmlUnitSelenium() {
 String name = "html";
 String publicId = "publicId";
 String systemId = "systemId";
 String baseUri = "baseUri";

 Document document = HtmlUnitJsoup.parse("<html><body></body></html>", baseUri);
 DocumentType documentType = new DocumentType(name, publicId, systemId, baseUri);
 document.insertBefore(documentType, document.body());

 StringBuilder accum = new StringBuilder();
 documentType.outerHtmlHead(accum, 0, null);
 documentType.outerHtmlTail(accum, 0, null);

 String expected = "<!DOCTYPE html" +
         (StringUtil.isBlank(publicId) ? "" : " PUBLIC \"\"\"publicId\"\"\"") +
         (StringUtil.isBlank(systemId) ? "" : " " + systemId + "\"") +
         ">";

 assertEquals(expected, accum.toString());
 }
}