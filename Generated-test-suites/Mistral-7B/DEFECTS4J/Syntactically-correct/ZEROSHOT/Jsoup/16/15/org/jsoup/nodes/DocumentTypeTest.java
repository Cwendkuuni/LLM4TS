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
 public void setUp() {
 document = new Document();
 }

 @Test
 public void testDocumentTypeCreation() {
 String name = "html";
 String publicId = "HTML";
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
 public void testOuterHtmlHead() {
 DocumentType docType = new DocumentType("html", "HTML", "http://www.w3.org/TR/html4/strict.dtd", "http://example.com");
 StringBuilder accum = new StringBuilder();
 docType.outerHtmlHead(accum, 0, null);

 assertEquals("<!DOCTYPE htmlPUBLIC \"HTML\" http://www.w3.org/TR/html4/strict.dtd>", accum.toString());
 }

 @Test
 public void testOuterHtmlTail() {
 DocumentType docType = new DocumentType("html", "", "", "");
 StringBuilder accum = new StringBuilder();
 docType.outerHtmlTail(accum, 0, null);

 assertEquals("", accum.toString());
 }

 @Test
 public void testCreateElement() {
 DocumentType docType = new DocumentType("html", "HTML", "http://www.w3.org/TR/html4/strict.dtd", "http://example.com");
 Element element = docType.createElement("html");

 assertEquals("#document-type-html", element.nodeName());
 assertEquals("html", element.tagName());
 }

 @Test
 public void testAttributes() {
 DocumentType docType = new DocumentType("html", "HTML", "http://www.w3.org/TR/html4/strict.dtd", "http://example.com");
 Attributes attributes = docType.attributes();

 assertNotNull(attributes);
 assertEquals(3, attributes.size());
 assertEquals("HTML", attributes.get("publicId"));
 assertEquals("http://www.w3.org/TR/html4/strict.dtd", attributes.get("systemId"));
 assertEquals("http://example.com", attributes.get("baseUri"));
 }
}