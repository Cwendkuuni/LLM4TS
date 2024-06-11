import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import static org.junit.Assert.*;

public class DocumentTypeTest {

 @Test
 public void testDocumentTypeCreation() {
 Document document = Jsoup.parse("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">");
 DocumentType documentType = document.documentType();

 assertEquals("#doctype", documentType.nodeName());
 assertEquals("html", documentType.name());
 assertNotNull(documentType.publicId());
 assertNotNull(documentType.systemId());
 }

 @Test
 public void testDocumentTypeCreationWithEmptyString() {
 Document document = Jsoup.parse("<!DOCTYPE html>");
 DocumentType documentType = document.documentType();

 assertEquals("#doctype", documentType.nodeName());
 assertEquals("html", documentType.name());
 assertNull(documentType.publicId());
 assertNull(documentType.systemId());
 }

 @Test
 public void testDocumentTypeCreationWithPublicId() {
 Document document = Jsoup.parse("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\" >");
 DocumentType documentType = document.documentType();

 assertEquals("#doctype", documentType.nodeName());
 assertEquals("html", documentType.name());
 assertEquals("-//W3C//DTD HTML 4.01 Transitional//EN", documentType.publicId());
 assertNotNull(documentType.systemId());
 }

 @Test
 public void testDocumentTypeCreationWithSystemId() {
 Document document = Jsoup.parse("<!DOCTYPE html SYSTEM \"http://www.w3.org/TR/html4/strict.dtd\">");
 DocumentType documentType = document.documentType();

 assertEquals("#doctype", documentType.nodeName());
 assertEquals("html", documentType.name());
 assertNull(documentType.publicId());
 assertEquals("http://www.w3.org/TR/html4/strict.dtd", documentType.systemId());
 }

 @Test
 public void testDocumentTypeCreationWithBothPublicIdAndSystemId() {
 Document document = Jsoup.parse("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\" SYSTEM >");
 DocumentType documentType = document.documentType();

 assertEquals("#doctype", documentType.nodeName());
 assertEquals("html", documentType.name());
 assertEquals("-//W3C//DTD HTML 4.01 Transitional//EN", documentType.publicId());
 assertEquals("http://www.w3.org/TR/html4/loose.dtd", documentType.systemId());
 }

 @Test
 public void testDocumentTypeToString() {
 Document document = Jsoup.parse("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">");
 DocumentType documentType = document.documentType();

 assertEquals("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">", documentType.toString());
 }

 @Test
 public void testOuterHtmlHead() {
 Document document = Jsoup.parse("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">");
 DocumentType documentType = document.documentType();
 StringBuilder accumulator = new StringBuilder();
 documentType.outerHtmlHead(accumulator, 0, null);

 assertEquals("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">", accumulator.toString());
 }

 @Test
 public void testOuterHtmlTail() {
 Document document = Jsoup.parse("<!DOCTYPE html>");
 DocumentType documentType = document.documentType();
 StringBuilder accumulator = new StringBuilder();
 documentType.outerHtmlTail(accumulator, 0, null);

 assertEquals("", accumulator.toString());
 }

 @Test
 public void testOuterHtmlHeadWithPublicId() {
 Document document = Jsoup.parse("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">");
 DocumentType documentType = document.documentType();
 StringBuilder accumulator = new StringBuilder();
 documentType.outerHtmlHead(accumulator, 0, null);

 assertEquals("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">", accumulator.toString());
 }

 @Test
 public void testOuterHtmlHeadWithSystemId() {
 Document document = Jsoup.parse("<!DOCTYPE html SYSTEM \"http://www.w3.org/TR/html4/strict.dtd\">");
 DocumentType documentType = document.documentType();
 StringBuilder accumulator = new StringBuilder();
 documentType.outerHtmlHead(accumulator, 0, null);

 assertEquals("<!DOCTYPE html SYSTEM \"http://www.w3.org/TR/html4/strict.dtd\">", accumulator.toString());
 }

 @Test
 public void testOuterHtmlHeadWithBothPublicIdAndSystemId() {
 Document document = Jsoup.parse("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\" SYSTEM >");
 DocumentType documentType = document.documentType();
 StringBuilder accumulator = new StringBuilder();
 documentType.outerHtmlHead(accumulator, 0, null);

 assertEquals("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\" SYSTEM >", accumulator.toString());
 }

 @Test
 public void testAddAttribute() {
 Document document = Jsoup.parse("<!DOCTYPE html>");
 DocumentType documentType = document.documentType();
 documentType.attr("test", "testValue");

 assertEquals(1, documentType.attributes().size());
 assertEquals("test", documentType.attributes().keySet().iterator().next());
 assertEquals("testValue", documentType.attributes().get("test").toString());
 }

 @Test
 public void testRemoveAttribute() {
 Document document = Jsoup.parse("<!DOCTYPE html>");
 DocumentType documentType = document.documentType();
 documentType.attr("test", "testValue");
 documentType.removeAttribute("test");

 assertEquals(0, documentType.attributes().size());
 }
}