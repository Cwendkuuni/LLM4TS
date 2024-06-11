import org.TestCase;import org.jsoup.Jsoup;import org.jsoup.nodes.Document;import org.jsoup.nodes.DocumentType;import static org.junit.Assert.*;

public class DocumentTypeTest extends TestCase {

 @Test
 public void testDocumentTypeCreation() {
 DocumentType docType = new DocumentType("html", "publicId", "systemId", null);
 assertEquals("#doctype", docType.nodeName());
 assertEquals("html", docType.attr(DocumentType.NAME));
 assertEquals("publicId", docType.attr(DocumentType.PUBLIC_ID));
 assertEquals("systemId", docType.attr(DocumentType.SYSTEM_ID));
 }

 @Test
 public void testDocumentTypeOutputHtml() throws IOException {
 Document doc = Jsoup.parse("<!DOCTYPE html>\n<html>\n<body>\n<p>Test</p>\n</body>\n</html>");
 DocumentType docType = doc.getFirstMetaElement();
 String expected = "<!doctype html>\n";
 String actual = docType.outerHtml(null, Document.OutputSettings.syntax(Document.Syntax.html));
 assertEquals(expected, actual);
 }

 @Test
 public void testDocumentTypeOutputHtmlNoPublicIdNorSystemId() throws IOException {
 Document doc = Jsoup.parse("<!DOCTYPE html>\n<html>\n<body>\n<p>Test</p>\n</body>\n</html>");
 DocumentType docType = doc.getFirstMetaElement();
 String expected = "<!doctype html>\n";
 String actual = docType.outerHtml(null, Document.OutputSettings.syntax(Document.Syntax.html));
 assertEquals(expected, actual);
 }

 @Test
 public void testDocumentTypeOutputHtmlWithPublicId() throws IOException {
 Document doc = Jsoup.parse("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n<html xmlns=\"http://www.w3.org/1999/xhtml\">\n<body>\n<p>Test</p>\n</body>\n</html>");
 DocumentType docType = doc.getFirstMetaElement();
 String expected = "<!doctype html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n";
 String actual = docType.outerHtml(null, Document.OutputSettings.syntax(Document.Syntax.html));
 assertEquals(expected, actual);
 }

 @Test
 public void testDocumentTypeOutputHtmlWithSystemId() throws IOException {
 Document doc = Jsoup.parse("<!doctype html SYSTEM \"about:legacy-compat\">\n<html>\n<body>\n<p>Test</p>\n</body>\n</html>");
 DocumentType docType = doc.getFirstMetaElement();
 String expected = "<!doctype html SYSTEM \"about:legacy-compat\">\n";
 String actual = docType.outerHtml(null, Document.OutputSettings.syntax(Document.Syntax.html));
 assertEquals(expected, actual);
 }

 @Test
 public void testDocumentTypeOutputHtmlWithPublicIdAndSystemId() throws IOException {
 Document doc = Jsoup.parse("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\" [<!ENTITY % ent SYSTEM \"ent.dtd\">]\n<html xmlns=\"http://www.w3.org/1999/xhtml\">\n<body>\n<p>Test</p>\n</body>\n</html>");
 DocumentType docType = doc.getFirstMetaElement();
 String expected = "<!doctype html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\" [<!ENTITY % ent SYSTEM \"ent.dtd\">]\n";
 String actual = docType.outerHtml(null, Document.OutputSettings.syntax(Document.Syntax.html));
 assertEquals(expected, actual);
 }

 @Test (expected = IOException.class)
 public void testDocumentTypeOutputHtmlWithNullSettings() throws IOException {
 Document doc = Jsoup.parse("<!DOCTYPE html>\n<html>\n<body>\n<p>Test</p>\n</body>\n</html>");
 DocumentType docType = doc.getFirstMetaElement();
 docType.outerHtml(null, null);
 fail("Expected IOException but no exception was thrown.");
 }
}