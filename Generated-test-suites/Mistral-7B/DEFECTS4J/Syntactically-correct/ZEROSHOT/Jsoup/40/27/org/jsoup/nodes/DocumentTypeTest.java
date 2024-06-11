import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Attributes;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class DocumentTypeTest {

 private Document document;

 @Before
 public void setup() {
 document = new Document();
 }

 @Test
 public void testDocumentTypeCreation() {
 String name = "html";
 String publicId = "{-//W3C//DTD HTML 4.01//EN}";
 String systemId = "http://www.w3.org/TR/html4/strict.dtd";
 String baseUri = "http://example.com";
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
 assertNotNull(docType.attr("name"));
 assertNotNull(docType.attr("publicId"));
 assertNotNull(docType.attr("systemId"));
 assertEquals("", docType.attr("name"));
 assertEquals("", docType.attr("publicId"));
 assertEquals("", docType.attr("systemId"));
 }

 @Test
 public void testDocumentTypeOuterHtmlHead() {
 DocumentType docType = new DocumentType("html", "{-//W3C//DTD HTML 4.01//EN}", "http://www.w3.org/TR/html4/strict.dtd", "http://example.com");
 StringBuilder accum = new StringBuilder();
 docType.outerHtmlHead(accum, 0, null);

 assertEquals("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01//EN\" \"http://www.w3.org/TR/html4/strict.dtd\">", accum.toString());
 }

 @Test
 public void testDocumentTypeOuterHtmlTail() {
 DocumentType docType = new DocumentType("html", "{-//W3C//DTD HTML 4.01//EN}", "http://www.w3.org/TR/html4/strict.dtd", "http://example.com");
 StringBuilder accum = new StringBuilder();
 docType.outerHtmlTail(accum, 0, null);

 assertEquals("", accum.toString());
 }

 @Test
 public void testDocumentTypeWithEmptyAttributes() {
 DocumentType docType = new DocumentType("html", "", "", "");

 assertEquals("<!DOCTYPE html>", docType.outerHtml(new Element.OutputSettings()));
 }

 @Test
 public void testDocumentTypeWithAllAttributes() {
 DocumentType docType = new DocumentType("html", "{-//W3C//DTD HTML 4.01//EN}", "http://www.w3.org/TR/html4/strict.dtd", "http://example.com");

 assertEquals("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01//EN\" \"http://www.w3.org/TR/html4/strict.dtd\">", docType.outerHtml(new Element.OutputSettings()));
 }
}