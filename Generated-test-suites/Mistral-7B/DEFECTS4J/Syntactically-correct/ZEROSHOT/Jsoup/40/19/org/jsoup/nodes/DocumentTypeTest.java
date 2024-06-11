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
 String publicId = " -//W3C//DTD XHTML 1.0 Strict//EN";
 String systemId = "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd";
 String baseUri = "http://www.example.com/";
 DocumentType docType = new DocumentType(name, publicId, systemId, baseUri);

 assertEquals("#doctype", docType.nodeName());
 assertEquals(name, docType.attr("name"));
 assertEquals(publicId, docType.attr("publicId"));
 assertEquals(systemId, docType.attr("systemId"));
 assertEquals(baseUri, docType.baseUri());
 }

 @Test
 public void testEmptyDocumentTypeCreation() {
 DocumentType docType = new DocumentType("html", "", "", "");

 assertEquals("#doctype", docType.nodeName());
 assertNull(docType.attr("name"));
 assertNull(docType.attr("publicId"));
 assertNull(docType.attr("systemId"));
 assertNull(docType.baseUri());
 }

 @Test
 public void testOuterHtmlHead() {
 DocumentType docType = new DocumentType("html", "DTD_PUBLIC", "DTD_SYSTEM", "");
 StringBuilder accum = new StringBuilder();
 document.body().append(docType);
 docType.outerHtmlHead(accum, 0, null);

 assertEquals("<!DOCTYPE html PUBLIC \"DTD_PUBLIC\" \"DTD_SYSTEM\">", accum.toString());
 }

 @Test
 public void testOuterHtmlTail() {
 DocumentType docType = new DocumentType("html", "DTD_PUBLIC", "DTD_SYSTEM", "");
 StringBuilder accum = new StringBuilder();
 Element element = document.createElement("html");
 element.appendChild(docType);
 docType.outerHtmlTail(accum, 0, null);

 assertEquals("", accum.toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidName() {
 String name = "";
 DocumentType docType = new DocumentType(name, "DTD_PUBLIC", "DTD_SYSTEM", "");

 fail("Expected IllegalArgumentException but was not thrown.");
 }
}