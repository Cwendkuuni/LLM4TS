import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import static org.junit.Assert.*;

public class DocumentTypeTest {

 @Test
 public void testDocumentTypeCreation() {
 DocumentType docType = new DocumentType("html", "DTD HTML 4.01 Transitional", "", "http://www.w3.org/TR/html4/loose.dtd");
 assertEquals("#doctype", docType.nodeName());
 assertEquals("html", docType.attr(DocumentType.NAME));
 assertEquals("DTD HTML 4.01 Transitional", docType.attr("publicId"));
 assertEquals("http://www.w3.org/TR/html4/loose.dtd", docType.attr("systemId"));
 }

 @Test
 public void testEmptyDocumentTypeCreation() {
 DocumentType docType = new DocumentType("html", "", "", "");
 assertEquals("#doctype", docType.nodeName());
 assertNull(docType.attr(DocumentType.NAME));
 assertNull(docType.attr(DocumentType.PUBLIC_ID));
 assertNull(docType.attr(DocumentType.SYSTEM_ID));
 }

 @Test
 public void testDocumentTypeOutputHTML() throws IOException {
 Document doc = Jsoup.parse("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">");
 DocumentType docType = doc.document().doctype();
 assertEquals("<!doctype html", docType.outerHtmlHead(new StringWriter(), 0, Document.OutputSettings.syntax(Document.OutputSettings.Syntax.html)).toString());
 }

 @Test
 public void testDocumentTypeOutputHTMLNoPublicIdNoSystemId() throws IOException {
 Document doc = Jsoup.parse("<!DOCTYPE html>");
 DocumentType docType = doc.document().doctype();
 assertEquals("<!doctype html", docType.outerHtmlHead(new StringWriter(), 0, Document.OutputSettings.syntax(Document.OutputSettings.Syntax.html)).toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDocumentTypeOutputHTMLInvalidSyntax() {
 Document doc = Jsoup.parse("<!DOCTYPE html SYNTAX=\"INVALID\">");
 DocumentType docType = doc.document().doctype();
 docType.outerHtmlHead(new StringWriter(), 0, Document.OutputSettings.syntax(Document.OutputSettings.Syntax.INVALID));
 }
}