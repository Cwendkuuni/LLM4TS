package org.jsoup.nodes.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Parser;

import org.junit.Test;

public class DocumentTypeTest {

 @Test
 public void testDocumentTypeCreation() {
 Document document = new Document();
 DocumentType documentType = new DocumentType("html", "HTML", "", document);
 assertNotNull(documentType);
 assertEquals("#doctype", documentType.nodeName());
 }

 @Test
 public void testDocumentTypeCreationWithPublicIdAndSystemId() {
 Document document = new Document();
 DocumentType documentType = new DocumentType("html", "HTML PUBLIC", "DTD HTML 1.0", document);
 assertNotNull(documentType);
 assertEquals("#doctype", documentType.nodeName());
 assertEquals("HTML PUBLIC", documentType.attr(DocumentType.PUBLIC_ID));
 assertEquals("DTD HTML 1.0", documentType.attr(DocumentType.SYSTEM_ID));
 }

 @Test
 public void testDocumentTypeCreationWithBaseUri() {
 Document document = new Document();
 document.baseUri("http://example.com");
 DocumentType documentType = new DocumentType("html", "HTML", "", document);
 assertEquals("http://example.com", documentType.baseUri().originalString());
 }

 @Test
 public void testDocumentTypeOuterHtmlHead() throws Exception {
 Document document = new Document();
 DocumentType documentType = new DocumentType("html", "HTML", "", document);

 Appendable appendable = new StringBuilder();
 documentType.outerHtmlHead(appendable, 0, Document.OutputSettings.syntax(Document.OutputSettings.Syntax.html));

 assertEquals("<!doctype html>", appendable.toString());
 }

 @Test
 public void testDocumentTypeOuterHtmlHeadWithPublicIdAndSystemId() throws Exception {
 Document document = new Document();
 DocumentType documentType = new DocumentType("html", "HTML PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\", \"" + Document.OutputSettings.HTML_DTD_URL + "\"", "", document);

 Appendable appendable = new StringBuilder();
 documentType.outerHtmlHead(appendable, 0, Document.OutputSettings.syntax(Document.OutputSettings.Syntax.html));

 assertEquals("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">", appendable.toString());
 }

 @Test
 public void testDocumentTypeOuterHtmlTail() throws Exception {
 Document document = new Document();
 DocumentType documentType = new DocumentType("html", "HTML", "", document);

 Appendable appendable = new StringBuilder();
 documentType.outerHtmlTail(appendable, 0, Document.OutputSettings.syntax(Document.OutputSettings.Syntax.html));

 assertEquals("", appendable.toString());
 }

 @Test
 public void testDocumentTypeParse() throws Exception {
 Parser parser = new Parser();
 Document document = parser.parse("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">");
 Node node = document.childNodes().get(0);

 assertEquals(DocumentType.class, node.getClass());
 DocumentType documentType = (DocumentType) node;
 assertEquals("html", documentType.nodeName());
 assertEquals("HTML PUBLIC", documentType.attr(DocumentType.PUBLIC_ID));
 assertEquals("DTD HTML 1.0", documentType.attr(DocumentType.SYSTEM_ID));
 }
}