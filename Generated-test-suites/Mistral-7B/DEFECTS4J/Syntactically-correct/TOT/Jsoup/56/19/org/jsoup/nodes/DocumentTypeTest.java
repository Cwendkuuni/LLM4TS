import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Attributes;
import static org.junit.Assert.*;

public class DocumentTypeTest {

 @Test
 public void testDocumentTypeCreation() {
 DocumentType docType = new DocumentType("html", "DTD HTML 5", null, null);
 assertEquals("#doctype", docType.nodeName());
 assertEquals("html", docType.attr(DocumentType.NAME));
 assertNull(docType.attr(DocumentType.PUBLIC_ID));
 assertNull(docType.attr(DocumentType.SYSTEM_ID));
 }

 @Test
 public void testDocumentTypeCreationWithPublicId() {
 DocumentType docType = new DocumentType("html", "DTD HTML 5", "PUBLIC", "public.dtd");
 assertEquals("#doctype", docType.nodeName());
 assertEquals("html", docType.attr(DocumentType.NAME));
 assertEquals("public.dtd", docType.attr(DocumentType.SYSTEM_ID));
 }

 @Test
 public void testDocumentTypeCreationWithSystemId() {
 DocumentType docType = new DocumentType("html", null, null, "http://example.com/dtd.dtd");
 assertEquals("#doctype", docType.nodeName());
 assertEquals("html", docType.attr(DocumentType.NAME));
 assertEquals("http://example.com/dtd.dtd", docType.attr(DocumentType.SYSTEM_ID));
 }

 @Test
 public void testDocumentTypeCreationWithPublicIdAndSystemId() {
 DocumentType docType = new DocumentType("html", "DTD HTML 5", "PUBLIC", "public.dtd", "http://example.com/dtd.dtd");
 assertEquals("#doctype", docType.nodeName());
 assertEquals("html", docType.attr(DocumentType.NAME));
 assertEquals("public.dtd", docType.attr(DocumentType.PUBLIC_ID));
 assertEquals("http://example.com/dtd.dtd", docType.attr(DocumentType.SYSTEM_ID));
 }

 @Test
 public void testEmptyDocumentType() {
 DocumentType docType = new DocumentType("", "", "", "");
 assertEquals("#doctype", docType.nodeName());
 assertFalse(docType.has(DocumentType.NAME));
 assertFalse(docType.has(DocumentType.PUBLIC_ID));
 assertFalse(docType.has(DocumentType.SYSTEM_ID));
 }

 @Test
 public void testDocumentTypeOuterHtmlHead() throws IOException {
 Document doc = Jsoup.parse("<!DOCTYPE html>");
 DocumentType docType = doc.documentType();
 Attributes attributes = docType.attributes();
 assertEquals("html", attributes.get(DocumentType.NAME));
 assertEquals(null, attributes.get(DocumentType.PUBLIC_ID));
 assertEquals(null, attributes.get(DocumentType.SYSTEM_ID));

 String expected = "<!doctype html>";
 String actual = docType.outerHtmlHead(new StringBuffer(), 0, null);
 assertEquals(expected, actual);
 }

 @Test
 public void testDocumentTypeOuterHtmlHeadWithPublicId() throws IOException {
 Document doc = Jsoup.parse("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">");
 DocumentType docType = doc.documentType();
 Attributes attributes = docType.attributes();
 assertEquals("html", attributes.get(DocumentType.NAME));
 assertEquals("http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd", attributes.get(DocumentType.SYSTEM_ID));

 String expected = "<!doctype html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">";
 String actual = docType.outerHtmlHead(new StringBuffer(), 0, null);
 assertEquals(expected, actual);
 }

 @Test
 public void testDocumentTypeOuterHtmlHeadWithPublicIdAndSystemId() throws IOException {
 Document doc = Jsoup.parse("<!DOCTYPE html PUBLIC \"My Company DTD\" \"http://example.com/mydtd.dtd\">");
 DocumentType docType = doc.documentType();
 Attributes attributes = docType.attributes();
 assertEquals("html", attributes.get(DocumentType.NAME));
 assertEquals("http://example.com/mydtd.dtd", attributes.get(DocumentType.SYSTEM_ID));

 String expected = "<!doctype html PUBLIC \"My Company DTD\" \"http://example.com/mydtd.dtd\">";
 String actual = docType.outerHtmlHead(new StringBuffer(), 0, null);
 assertEquals(expected, actual);
 }

 @Test
 public void testDocumentTypeOuterHtmlTail() throws IOException {
 Document doc = Jsoup.parse("<!DOCTYPE html>");
 DocumentType docType = doc.documentType();
 String actual = docType.outerHtmlTail(new StringBuffer(), 0, null);
 assertEquals("", actual);
 }

 @Test
 public void testHasAttribute() {
 DocumentType docType = new DocumentType("html", null, null, null);
 assertTrue(docType.has(DocumentType.NAME));
 assertFalse(docType.has("nonExistentAttribute"));
 }
}