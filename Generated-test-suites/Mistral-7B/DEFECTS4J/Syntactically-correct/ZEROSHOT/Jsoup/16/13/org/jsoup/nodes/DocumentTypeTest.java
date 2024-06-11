import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Attributes;
import org.junit.Test;
import static org.junit.Assert.*;

public class DocumentTypeTest {

 @Test
 public void testDocumentTypeCreation() {
 Document document = new Document();
 DocumentType documentType = new DocumentType("html", "publicID", "systemID", "/baseUri");

 assertEquals("#doctype", documentType.nodeName());
 assertEquals("html", documentType.attr("name"));
 assertEquals("publicID", documentType.attr("publicId"));
 assertEquals("systemID", documentType.attr("systemId"));
 assertEquals("/baseUri", documentType.baseUri());
 }

 @Test
 public void testDocumentTypeOuterHtmlHead() {
 Document document = new Document();
 DocumentType documentType = new DocumentType("html", "publicID", "systemID", "/baseUri");
 StringBuilder accum = new StringBuilder();
 documentType.outerHtmlHead(accum, 0, null);

 assertEquals("<!DOCTYPE htmlPUBLIC \"publicID\" systemID>\n", accum.toString());
 }

 @Test
 public void testDocumentTypeOuterHtmlTail() {
 Document document = new Document();
 DocumentType documentType = new DocumentType("html", "publicID", "systemID", "/baseUri");
 StringBuilder accum = new StringBuilder();
 documentType.outerHtmlTail(accum, 0, null);

 assertEquals("", accum.toString());
 }

 @Test
 public void testDocumentTypeWithEmptyPublicIdAndSystemId() {
 Document document = new Document();
 DocumentType documentType = new DocumentType("html", "", "", "/baseUri");

 assertEquals("#doctype", documentType.nodeName());
 assertEquals("html", documentType.attr("name"));
 assertEquals("", documentType.attr("publicId"));
 assertEquals("", documentType.attr("systemId"));
 assertEquals("/baseUri", documentType.baseUri());
 }

 @Test
 public void testDocumentTypeWithNullValues() {
 Document document = new Document();
 DocumentType documentType = new DocumentType("html", null, null, null);

 assertEquals("#doctype", documentType.nodeName());
 assertEquals("html", documentType.attr("name"));
 assertEquals(null, documentType.attr("publicId"));
 assertEquals(null, documentType.attr("systemId"));
 assertEquals(null, documentType.baseUri());
 }

 @Test
 public void testDocumentTypeWithAttributes() {
 Document document = new Document();
 DocumentType documentType = new DocumentType("html", "publicID", "systemID", "/baseUri");
 documentType.attributes().put("customAttribute", "customValue");

 assertEquals("#doctype", documentType.nodeName());
 assertEquals("html", documentType.attr("name"));
 assertEquals("publicID", documentType.attr("publicId"));
 assertEquals("systemID", documentType.attr("systemId"));
 assertEquals("/baseUri", documentType.baseUri());
 assertEquals("customValue", documentType.attributes().get("customAttribute"));
 }

 @Test
 public void testDocumentTypeWithCustomNodeName() {
 Document document = new Document();
 DocumentType documentType = new DocumentType("customName", "publicID", "systemID", "/baseUri");

 assertEquals("customName", documentType.nodeName());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDocumentTypeWithBlankName() {
 Document document = new Document();
 DocumentType documentType = new DocumentType("", "publicID", "systemID", "/baseUri");
 }

 @Test
 public void testDocumentTypeToString() {
 Document document = new Document();
 DocumentType documentType = new DocumentType("html", "publicID", "systemID", "/baseUri");

 assertEquals("<html xmlns:xml=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en\">\n<!DOCTYPE htmlPUBLIC \"publicID\" systemID>\n</html>", documentType.toString());
 }

 @Test
 public void testDocumentTypeAsElement() {
 Document document = new Document();
 DocumentType documentType = new DocumentType("html", "publicID", "systemID", "/baseUri");
 Element element = documentType.asElement();

 assertEquals("#doctype", element.nodeName());
 assertEquals("html", element.attr("name"));
 assertEquals("publicID", element.attr("publicId"));
 assertEquals("systemID", element.attr("systemId"));
 assertEquals("/baseUri", element.baseUri());
 }

 @Test
 public void testDocumentTypeWithQuirksMode() {
 Document document = new Document();
 DocumentType documentType = new DocumentType("html", "publicID", "systemID", "/baseUri");
 Attributes attributes = documentType.attributes();
 attributes.put("quirksMode", "true");

 assertEquals("#doctype", documentType.nodeName());
 assertEquals("html", documentType.attr("name"));
 assertEquals("publicID", documentType.attr("publicId"));
 assertEquals("systemID", documentType.attr("systemId"));
 assertEquals("/baseUri", documentType.baseUri());
 assertEquals("true", documentType.attr("quirksMode"));
 }
}