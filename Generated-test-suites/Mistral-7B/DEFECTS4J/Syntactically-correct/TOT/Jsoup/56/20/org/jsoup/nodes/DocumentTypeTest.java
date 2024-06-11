import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Attributes;
import org.junit.Test;
import static org.junit.Assert.*;

public class DocumentTypeTest {

 @Test
 public void testDocumentTypeCreation() {
 Document doc = Jsoup.parse("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">");
 DocumentType doctype = (DocumentType) doc.head();

 assertEquals("#doctype", doctype.nodeName());
 assertEquals("html", doctype.attr(DocumentType.NAME));
 assertEquals("PUBLIC", doctype.attr(DocumentType.PUBLIC_KEY));
 assertEquals("-//W3C//DTD HTML 4.01 Transitional//EN", doctype.attr(DocumentType.PUBLIC_ID));
 assertEquals("http://www.w3.org/TR/html4/loose.dtd", doctype.attr(DocumentType.SYSTEM_ID));
 }

 @Test
 public void testDocumentTypeCreationWithEmptyAttributes() {
 Document doc = Jsoup.parse("<!DOCTYPE html>");
 DocumentType doctype = (DocumentType) doc.head();

 assertEquals("#doctype", doctype.nodeName());
 assertNull(doctype.attr(DocumentType.PUBLIC_ID));
 assertNull(doctype.attr(DocumentType.SYSTEM_ID));
 }

 @Test
 public void testDocumentTypeCreationWithPublicIdAndSystemId() {
 Document doc = Jsoup.parse("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">");
 DocumentType doctype = (DocumentType) doc.head();

 assertEquals("#doctype", doctype.nodeName());
 assertEquals("html", doctype.attr(DocumentType.NAME));
 assertEquals("PUBLIC", doctype.attr(DocumentType.PUBLIC_KEY));
 assertEquals("-//W3C//DTD XHTML 1.0 Strict//EN", doctype.attr(DocumentType.PUBLIC_ID));
 assertEquals("http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd", doctype.attr(DocumentType.SYSTEM_ID));
 }

 @Test
 public void testDocumentTypeCreationWithEmptyName() {
 Document doc = Jsoup.parse("<!DOCTYPE []>");
 DocumentType doctype = (DocumentType) doc.head();

 assertEquals("#doctype", doctype.nodeName());
 assertNull(doctype.attr(DocumentType.NAME));
 assertNull(doctype.attr(DocumentType.PUBLIC_ID));
 assertNull(doctype.attr(DocumentType.SYSTEM_ID));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDocumentTypeCreationWithNullName() {
 Document doc = Jsoup.parse("<!DOCTYPE null>");
 DocumentType doctype = (DocumentType) doc.head();

 fail("Expected an IllegalArgumentException but no exception was thrown.");
 }

 @Test
 public void testDocumentTypeOutput() throws IOException {
 Document doc = Jsoup.parse("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">");
 DocumentType doctype = (DocumentType) doc.head();

 String html = doc.outerHtml();
 String expected = "<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">";

 assertTrue(html.contains(expected));
 }

 @Test
 public void testDocumentTypeOutputWithEmptyAttributes() {
 Document doc = Jsoup.parse("<!DOCTYPE html>");
 DocumentType doctype = (DocumentType) doc.head();

 String html = doc.outerHtml();
 String expected = "<!DOCTYPE html>";

 assertTrue(html.contains(expected));
 }

 @Test
 public void testDocumentTypeOutputWithPublicIdAndSystemId() {
 Document doc = Jsoup.parse("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">");
 DocumentType doctype = (DocumentType) doc.head();

 String html = doc.outerHtml();
 String expected = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">";

 assertTrue(html.contains(expected));
 }

 @Test
 public void testDocumentTypeOutputWithEmptyName() {
 Document doc = Jsoup.parse("<!DOCTYPE []>");
 DocumentType doctype = (DocumentType) doc.head();

 String html = doc.outerHtml();
 String expected = "<!DOCTYPE>";

 assertTrue(html.contains(expected));
 }
}