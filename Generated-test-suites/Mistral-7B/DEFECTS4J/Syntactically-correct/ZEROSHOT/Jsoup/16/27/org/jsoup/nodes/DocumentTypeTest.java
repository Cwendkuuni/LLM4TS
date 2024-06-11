import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.helper.StringUtil;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class DocumentTypeTest {

 @Test
 public void testDocumentTypeCreation() {
 Document document = new Document();
 DocumentType documentType = new DocumentType("html", "publicId", "systemId", document.baseUri());

 assertEquals("#doctype", documentType.nodeName());
 assertEquals("html", documentType.attr("name"));
 assertEquals("publicId", documentType.attr("publicId"));
 assertEquals("systemId", documentType.attr("systemId"));
 assertEquals(document.baseUri().toString(), documentType.baseUri().toString());
 }

 @Test
 public void testEmptyDocumentTypeCreation() {
 Document document = new Document();
 DocumentType documentType = new DocumentType("html", "", "", document.baseUri());

 assertEquals("#doctype", documentType.nodeName());
 assertNull(documentType.attr("publicId"));
 assertNull(documentType.attr("systemId"));
 assertEquals(document.baseUri().toString(), documentType.baseUri().toString());
 }

 @Test
 public void testDocumentTypeOuterHtmlHead() {
 Document document = new Document();
 DocumentType documentType = new DocumentType("html", "publicId", "systemId", document.baseUri());
 StringBuilder accum = new StringBuilder();
 documentType.outerHtmlHead(accum, 0, null);

 assertEquals("<!DOCTYPE htmlPUBLIC \"publicId\" systemId>\n", accum.toString());
 }

 @Test
 public void testDocumentTypeOuterHtmlTail() {
 Document document = new Document();
 DocumentType documentType = new DocumentType("html", "publicId", "systemId", document.baseUri());
 StringBuilder accum = new StringBuilder();
 documentType.outerHtmlTail(accum, 0, null);

 assertEmpty(accum);
 }

 @Test
 public void testDocumentTypeWithEmptyPublicIdAndSystemId() {
 Document document = new Document();
 DocumentType documentType = new DocumentType("html", "", "", document.baseUri());

 assertEquals("#doctype", documentType.nodeName());
 assertNull(documentType.attr("publicId"));
 assertNull(documentType.attr("systemId"));
 assertEquals(document.baseUri().toString(), documentType.baseUri().toString());

 StringBuilder accum = new StringBuilder();
 documentType.outerHtmlHead(accum, 0, null);

 assertEquals("<!DOCTYPE html>\n", accum.toString());
 }

 @Test
 public void testDocumentTypeWithOnlyPublicId() {
 Document document = new Document();
 DocumentType documentType = new DocumentType("html", "publicId", "", document.baseUri());

 assertEquals("#doctype", documentType.nodeName());
 assertEquals("publicId", documentType.attr("publicId"));
 assertNull(documentType.attr("systemId"));
 assertEquals(document.baseUri().toString(), documentType.baseUri().toString());

 StringBuilder accum = new StringBuilder();
 documentType.outerHtmlHead(accum, 0, null);

 assertEquals("<!DOCTYPE html PUBLIC \"publicId\" \"\">\n", accum.toString());
 }

 @Test
 public void testDocumentTypeWithOnlySystemId() {
 Document document = new Document();
 DocumentType documentType = new DocumentType("html", "", "systemId", document.baseUri());

 assertEquals("#doctype", documentType.nodeName());
 assertNull(documentType.attr("publicId"));
 assertEquals("systemId", documentType.attr("systemId"));
 assertEquals(document.baseUri().toString(), documentType.baseUri().toString());

 StringBuilder accum = new StringBuilder();
 documentType.outerHtmlHead(accum, 0, null);

 assertEquals("<!DOCTYPE html SYSTEM \"systemId\" >\n", accum.toString());
 }

 @Test
 public void testDocumentTypeWithBothPublicIdAndSystemId() {
 Document document = new Document();
 DocumentType documentType = new DocumentType("html", "publicId", "systemId", document.baseUri());

 assertEquals("#doctype", documentType.nodeName());
 assertEquals("publicId", documentType.attr("publicId"));
 assertEquals("systemId", documentType.attr("systemId"));
 assertEquals(document.baseUri().toString(), documentType.baseUri().toString());

 StringBuilder accum = new StringBuilder();
 documentType.outerHtmlHead(accum, 0, null);

 assertEquals("<!DOCTYPE html PUBLIC \"publicId\" systemId>\n", accum.toString());
 }

 @Test
 public void testDocumentTypeToString() {
 Document document = new Document();
 DocumentType documentType = new DocumentType("html", "publicId", "systemId", document.baseUri());

 assertEquals("<!DOCTYPE html PUBLIC \"publicId\" systemId>\n", documentType.toString());
 }

 @Test
 public void testDocumentTypeWithQuirksMode() {
 Document document = new Document();
 DocumentType documentType = new DocumentType("html", "publicId", "systemId", document.baseUri());
 documentType.attr("quirksMode", "true");

 StringBuilder accum = new StringBuilder();
 documentType.outerHtmlHead(accum, 0, null);

 assertEquals("<!DOCTYPE html PUBLIC \"publicId\" systemId\" [<!ENTITY % html SYSTEM \"about:legacy-compat\">]\n", accum.toString());
 }

 private void assertEmpty(StringBuilder accum) {
 assertEquals("", accum.toString());
 }
}