import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Attributes;
import org.junit.Test;
import static org.junit.Assert.*;

public class DocumentTypeTest {

 @Test
 public void testDocumentTypeCreation() {
 DocumentType documentType = new DocumentType("html", "HTML", "html", null);
 assertEquals("#doctype", documentType.nodeName());
 assertTrue(documentType.has("html"));
 assertFalse(documentType.has("SYSTEM_ID"));
 assertFalse(documentType.has("PUBLIC_ID"));
 }

 @Test
 public void testDocumentTypeCreationWithPublicId() {
 DocumentType documentType = new DocumentType("html", "PUBLIC -123456789", "html", null);
 assertEquals("#doctype", documentType.nodeName());
 assertTrue(documentType.has("html"));
 assertTrue(documentType.has("PUBLIC_ID"));
 assertEquals("PUBLIC -123456789", documentType.attr("PUBLIC_ID"));
 assertFalse(documentType.has("SYSTEM_ID"));
 }

 @Test
 public void testDocumentTypeCreationWithSystemId() {
 DocumentType documentType = new DocumentType("html", null, "http://example.com/DTD/html1.dtd", null);
 assertEquals("#doctype", documentType.nodeName());
 assertTrue(documentType.has("html"));
 assertFalse(documentType.has("PUBLIC_ID"));
 assertTrue(documentType.has("SYSTEM_ID"));
 assertEquals("http://example.com/DTD/html1.dtd", documentType.attr("SYSTEM_ID"));
 }

 @Test
 public void testDocumentTypeCreationWithPublicIdAndSystemId() {
 DocumentType documentType = new DocumentType("html", "PUBLIC -123456789", "http://example.com/DTD/html1.dtd", null);
 assertEquals("#doctype", documentType.nodeName());
 assertTrue(documentType.has("html"));
 assertTrue(documentType.has("PUBLIC_ID"));
 assertEquals("PUBLIC -123456789", documentType.attr("PUBLIC_ID"));
 assertTrue(documentType.has("SYSTEM_ID"));
 assertEquals("http://example.com/DTD/html1.dtd", documentType.attr("SYSTEM_ID"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDocumentTypeCreationWithNullName() {
 DocumentType documentType = new DocumentType(null, null, null, null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDocumentTypeCreationWithEmptyName() {
 DocumentType documentType = new DocumentType("", null, null, null);
 }

 @Test
 public void testDocumentTypeToString() throws IOException {
 Document document = Document.parse("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">");
 Element doctypeElement = document.selectFirst("!DOCTYPE");
 DocumentType documentType = (DocumentType) doctypeElement;
 StringBuilder expected = new StringBuilder();
 expected.append("<!DOCTYPE");
 if (documentType.has("PUBLIC_ID"))
 expected.append(" PUBLIC ");
 expected.append("\"");
 expected.append(documentType.attr("PUBLIC_ID"));
 expected.append('"');
 if (documentType.has("SYSTEM_ID"))
 expected.append(" SYSTEM \"");
 expected.append(documentType.attr("SYSTEM_ID"));
 expected.append('"');
 expected.append('>');
 StringBuilder actual = new StringBuilder();
 documentType.outerHtmlHead(actual, 0, Document.OutputSettings.syntax(Document.OutputSettings.Syntax.html));
 assertEquals(expected.toString(), actual.toString());
 }

 @Test
 public void testDocumentTypeWithNoAttributes() throws IOException {
 DocumentType documentType = new DocumentType("html5", null, null, null);
 StringBuilder expected = new StringBuilder();
 expected.append("<!DOCTYPE");
 assertEquals(expected.toString(), documentType.outerHtmlHead(new StringBuilder(), 0, Document.OutputSettings.syntax(Document.OutputSettings.Syntax.html)).toString());
 }
}