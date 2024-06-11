import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Attributes;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class DocumentTypeTest {

 @Test
 public void testDocumentTypeCreation() {
 DocumentType documentType = new DocumentType("html", "HTML TF", "HTML", "/baseUri");

 assertEquals("#doctype", documentType.nodeName());
 assertEquals("html", documentType.attr(NAME));
 assertNotNull(documentType.attr(PUBLIC_ID));
 assertNotNull(documentType.attr(SYSTEM_ID));
 assertEquals("/baseUri", documentType.baseUri());
 }

 @Test
 public void testDocumentTypeCreationWithoutAttributes() {
 DocumentType documentType = new DocumentType("html", "", "", "/baseUri");

 assertEquals("#doctype", documentType.nodeName());
 assertNull(documentType.attr(NAME));
 assertNull(documentType.attr(PUBLIC_ID));
 assertNull(documentType.attr(SYSTEM_ID));
 assertEquals("/baseUri", documentType.baseUri());
 }

 @Test
 public void testDocumentTypeCreationWithAllAttributes() {
 DocumentType documentType = new DocumentType("html", "HTML TF", "HTML", "/baseUri", "publicID", "systemID");

 assertEquals("#doctype", documentType.nodeName());
 assertEquals("html", documentType.attr(NAME));
 assertNotNull(documentType.attr(PUBLIC_ID));
 assertEquals("publicID", documentType.attr(PUBLIC_ID));
 assertNotNull(documentType.attr(SYSTEM_ID));
 assertEquals("systemID", documentType.attr(SYSTEM_ID));
 assertEquals("/baseUri", documentType.baseUri());
 }

 @Test
 public void testDocumentTypeOuterHtmlHead() throws IOException {
 Document doc = Jsoup.parse("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">");
 DocumentType documentType = doc.docType();

 Appendable accumulator = new StringBuilder();
 documentType.outerHtmlHead(accumulator, 0, Document.OutputSettings.syntax(Document.OutputSettings.Syntax.html));

 assertEquals("<!doctype html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">", accumulator.toString());
 }

 @Test
 public void testDocumentTypeOuterHtmlHeadWithoutPublicIdAndSystemId() throws IOException {
 Document doc = Jsoup.parse("<!DOCTYPE html>");
 DocumentType documentType = doc.docType();

 Appendable accumulator = new StringBuilder();
 documentType.outerHtmlHead(accumulator, 0, Document.OutputSettings.syntax(Document.OutputSettings.Syntax.html));

 assertEquals("<!doctype html>", accumulator.toString());
 }
}