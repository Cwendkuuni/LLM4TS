import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Attributes;
import static org.junit.Assert.*;

public class DocumentTypeTest {

 private Document document;

 @Before
 public void setUp() {
 document = new Document();
 }

 @Test
 public void testDocumentTypeCreation() {
 String name = "html";
 String publicId = "HTML_DOC";
 String systemId = "about:html";
 String baseUri = "http://www.example.com/";
 DocumentType documentType = new DocumentType(name, publicId, systemId, baseUri);

 assertEquals("#doctype", documentType.nodeName());
 assertEquals(name, documentType.attr("name"));
 assertEquals(publicId, documentType.attr("publicId"));
 assertEquals(systemId, documentType.attr("systemId"));
 assertEquals(baseUri, documentType.baseUri());
 }

 @Test
 public void testEmptyDocumentTypeCreation() {
 DocumentType documentType = new DocumentType("html", "", "", "");

 assertEquals("#doctype", documentType.nodeName());
 assertEquals("html", documentType.attr("name"));
 assertNull(documentType.attr("publicId"));
 assertNull(documentType.attr("systemId"));
 assertEquals("", documentType.baseUri());
 }

 @Test
 public void testDocumentTypeOuterHtmlHead() {
 DocumentType documentType = new DocumentType("html", "HTML_DOC", "about:html", "");
 StringBuilder accum = new StringBuilder();
 documentType.outerHtmlHead(accum, 0, null);

 assertEquals("<!DOCTYPE htmlPUBLIC \"HTML_DOC\" about:html>", accum.toString());
 }

 @Test
 public void testDocumentTypeOuterHtmlHeadWithPublicIdAndSystemId() {
 DocumentType documentType = new DocumentType("html", "HTML_DOC", "about:html", "");
 StringBuilder accum = new StringBuilder();
 documentType.outerHtmlHead(accum, 0, null);

 assertEquals("<!DOCTYPE htmlPUBLIC \"HTML_DOC\" about:html>", accum.toString());
 }

 @Test
 public void testDocumentTypeOuterHtmlTail() {
 DocumentType documentType = new DocumentType("html", "", "", "");
 StringBuilder accum = new StringBuilder();
 documentType.outerHtmlTail(accum, 0, null);

 assertEquals("", accum.toString());
 }

 @Test
 public void testDocumentTypeCreationWithNullArguments() {
 try {
 DocumentType documentType = new DocumentType(null, null, null, null);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testDocumentTypeCreationWithEmptyStringArguments() {
 DocumentType documentType = new DocumentType("", "", "", "");

 assertEquals("#doctype", documentType.nodeName());
 assertEquals("", documentType.attr("name"));
 assertNull(documentType.attr("publicId"));
 assertNull(documentType.attr("systemId"));
 assertEquals("", documentType.baseUri());
 }

 @Test
 public void testDocumentTypeCreationWithNullBaseUri() {
 DocumentType documentType = new DocumentType("html", "", "", null);

 assertEquals("#doctype", documentType.nodeName());
 assertEquals("html", documentType.attr("name"));
 assertNull(documentType.attr("publicId"));
 assertNull(documentType.attr("systemId"));
 assertNull(documentType.baseUri());
 }

 @Test
 public void testDocumentTypeClone() {
 DocumentType originalDocumentType = new DocumentType("html", "HTML_DOC", "about:html", "");
 DocumentType clonedDocumentType = originalDocumentType.clone();

 assertEquals(originalDocumentType.nodeName(), clonedDocumentType.nodeName());
 assertEquals(originalDocumentType.attr("name"), clonedDocumentType.attr("name"));
 assertEquals(originalDocumentType.attr("publicId"), clonedDocumentType.attr("publicId"));
 assertEquals(originalDocumentType.attr("systemId"), clonedDocumentType.attr("systemId"));
 assertEquals(originalDocumentType.baseUri(), clonedDocumentType.baseUri());
 }

 @Test
 public void testDocumentTypeEquals() {
 DocumentType documentType1 = new DocumentType("html", "HTML_DOC", "about:html", "");
 DocumentType documentType2 = new DocumentType("html", "HTML_DOC", "about:html", "");
 DocumentType documentType3 = new DocumentType("html", "HTML_DOC", "about:html", "");
 DocumentType documentType4 = new DocumentType("html", "HTML_DOC", "about:html", "http://www.example.com/");
 DocumentType documentType5 = new Element("html");

 assertEquals(documentType1, documentType1);
 assertEquals(documentType1, documentType2);
 assertEquals(documentType1, documentType3);
 assertNotEquals(documentType1, documentType4);
 assertNotEquals(documentType1, documentType5);
 }

 @Test
 public void testDocumentTypeHashCode() {
 DocumentType documentType1 = new DocumentType("html", "HTML_DOC", "about:html", "");
 DocumentType documentType2 = new DocumentType("html", "HTML_DOC", "about:html", "");
 DocumentType documentType3 = new DocumentType("html", "HTML_DOC", "about:html", "");
 DocumentType documentType4 = new DocumentType("html", "HTML_DOC", "about:html", "http://www.example.com/");
 DocumentType documentType5 = new DocumentType("html", "HTML_DOC", "about:html", "");
 DocumentType documentType6 = new DocumentType("html", "HTML_DOC", "", "");

 assertEquals(documentType1.hashCode(), documentType1.hashCode());
 assertEquals(documentType1.hashCode(), documentType2.hashCode());
 assertEquals(documentType1.hashCode(), documentType3.hashCode());
 assertNotEquals(documentType1.hashCode(), documentType4.hashCode());
 assertNotEquals(documentType1.hashCode(), documentType5.hashCode());
 assertNotEquals(documentType1.hashCode(), documentType6.hashCode());
 }
}