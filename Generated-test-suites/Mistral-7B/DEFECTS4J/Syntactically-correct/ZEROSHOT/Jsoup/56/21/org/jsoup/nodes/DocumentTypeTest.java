package org.jsoup.nodes.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Parser;

public class DocumentTypeTest {

 private Document createDocument(String html) throws IOException {
 String baseUri = "file:/path/to/your/test.html"; // replace with your test file path
 Document doc = new Document(baseUri);
 Parser parser = new Parser(baseUri);
 parser.parse(html);
 doc.setParser(parser);
 return doc;
 }

 @Test
 public void testDocumentTypeCreation() throws IOException {
 String html = "<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">";
 Document doc = createDocument(html);
 DocumentType htmlDOCTYPE = (DocumentType) doc.body();

 assertEquals("#doctype", htmlDOCTYPE.nodeName());
 assertEquals("html", htmlDOCTYPE.attr(NAME));
 assertEquals("PUBLIC", htmlDOCTYPE.attr(PUBLIC_KEY));
 assertEquals("-//W3C//DTD HTML 4.01 Transitional//EN", htmlDOCTYPE.attr(PUBLIC_ID));
 assertEquals("http://www.w3.org/TR/html4/loose.dtd", htmlDOCTYPE.attr(SYSTEM_ID));
 }

 @Test
 public void testDocumentTypeCreationWithOnlyName() throws IOException {
 String html = "<!DOCTYPE html>";
 Document doc = createDocument(html);
 DocumentType htmlDOCTYPE = (DocumentType) doc.body();

 assertEquals("#doctype", htmlDOCTYPE.nodeName());
 assertEquals("html", htmlDOCTYPE.attr(NAME));
 assertNull(htmlDOCTYPE.attr(PUBLIC_ID));
 assertNull(htmlDOCTYPE.attr(SYSTEM_ID));
 }

 @Test
 public void testDocumentTypeCreationWithOnlyPublicId() throws IOException {
 String html = "<!DOCTYPE html PUBLIC \"MyDTD\" \"http://example.com/dtd.dtd\">";
 Document doc = createDocument(html);
 DocumentType htmlDOCTYPE = (DocumentType) doc.body();

 assertEquals("#doctype", htmlDOCTYPE.nodeName());
 assertEquals("html", htmlDOCTYPE.attr(NAME));
 assertEquals("MyDTD", htmlDOCTYPE.attr(PUBLIC_ID));
 assertNull(htmlDOCTYPE.attr(SYSTEM_ID));
 }

 @Test
 public void testDocumentTypeCreationWithOnlySystemId() throws IOException {
 String html = "<!DOCTYPE html SYSTEM \"http://example.com/dtd.dtd\">";
 Document doc = createDocument(html);
 DocumentType htmlDOCTYPE = (DocumentType) doc.body();

 assertEquals("#doctype", htmlDOCTYPE.nodeName());
 assertEquals("html", htmlDOCTYPE.attr(NAME));
 assertNull(htmlDOCTYPE.attr(PUBLIC_ID));
 assertEquals("http://example.com/dtd.dtd", htmlDOCTYPE.attr(SYSTEM_ID));
 }

 @Test
 public void testDocumentTypeCreationWithAllAttributes() throws IOException {
 String html = "<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\" SYSTEM>\r\n<html>\r\n<head>\r\n<title>Test</title>\r\n</head>\r\n<body>\r\n<p>Hello World!</p>\r\n</body>\r\n</html>";
 Document doc = createDocument(html);
 DocumentType htmlDOCTYPE = (DocumentType) doc.body();

 assertEquals("#doctype", htmlDOCTYPE.nodeName());
 assertEquals("html", htmlDOCTYPE.attr(NAME));
 assertEquals("PUBLIC", htmlDOCTYPE.attr(PUBLIC_KEY));
 assertEquals("-//W3C//DTD HTML 4.01 Transitional//EN", htmlDOCTYPE.attr(PUBLIC_ID));
 assertEquals("http://www.w3.org/TR/html4/loose.dtd", htmlDOCTYPE.attr(SYSTEM_ID));
 }

 @Test
 public void testHasPublicId() throws IOException {
 String html = "<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">";
 Document doc = createDocument(html);
 DocumentType htmlDOCTYPE = (DocumentType) doc.body();

 assertTrue(htmlDOCTYPE.has(PUBLIC_ID));
 assertFalse(htmlDOCTYPE.has(SYSTEM_ID));
 }

 @Test
 public void testHasSystemId() throws IOException {
 String html = "<!DOCTYPE html SYSTEM \"http://example.com/dtd.dtd\">";
 Document doc = createDocument(html);
 DocumentType htmlDOCTYPE = (DocumentType) doc.body();

 assertTrue(htmlDOCTYPE.has(SYSTEM_ID));
 assertFalse(htmlDOCTYPE.has(PUBLIC_ID));
 }

 @Test
 public void testHasNoAttributes() throws IOException {
 String html = "<!DOCTYPE html>";
 Document doc = createDocument(html);
 DocumentType htmlDOCTYPE = (DocumentType) doc.body();

 assertFalse(htmlDOCTYPE.has(PUBLIC_ID));
 assertFalse(htmlDOCTYPE.has(SYSTEM_ID));
 }
}