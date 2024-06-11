import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Attributes;
import static org.junit.Assert.*;

public class DocumentTypeTest {

 @Test
 public void testDocumentTypeCreation() {
 Document doc = Jsoup.parse("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">");
 DocumentType doctype = (DocumentType) doc.child(0);

 assertEquals("#doctype", doctype.nodeName());
 assertEquals("html", doctype.attr(NAME));
 assertEquals("-//W3C//DTD XHTML 1.0 Strict//EN", doctype.attr(PUBLIC_ID));
 assertEquals("http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd", doctype.attr(SYSTEM_ID));
 assertTrue(doctype.has(NAME));
 assertTrue(doctype.has(PUBLIC_ID));
 assertTrue(doctype.has(SYSTEM_ID));
 }

 @Test
 public void testDocumentTypeCreationWithoutPublicIdAndSystemId() {
 Document doc = Jsoup.parse("<!DOCTYPE html>");
 DocumentType doctype = (DocumentType) doc.child(0);

 assertEquals("#doctype", doctype.nodeName());
 assertEquals("html", doctype.attr(NAME));
 assertNull(doctype.attr(PUBLIC_ID));
 assertNull(doctype.attr(SYSTEM_ID));
 assertTrue(doctype.has(NAME));
 assertFalse(doctype.has(PUBLIC_ID));
 assertFalse(doctype.has(SYSTEM_ID));
 }

 @Test
 public void testDocumentTypeCreationWithOnlyPublicId() {
 Document doc = Jsoup.parse("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\">");
 DocumentType doctype = (DocumentType) doc.child(0);

 assertEquals("#doctype", doctype.nodeName());
 assertEquals("html", doctype.attr(NAME));
 assertEquals("-//W3C//DTD XHTML 1.0 Strict//EN", doctype.attr(PUBLIC_ID));
 assertNull(doctype.attr(SYSTEM_ID));
 assertTrue(doctype.has(NAME));
 assertTrue(doctype.has(PUBLIC_ID));
 assertFalse(doctype.has(SYSTEM_ID));
 }

 @Test
 public void testDocumentTypeCreationWithOnlySystemId() {
 Document doc = Jsoup.parse("<!DOCTYPE html SYSTEM \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">");
 DocumentType doctype = (DocumentType) doc.child(0);

 assertEquals("#doctype", doctype.nodeName());
 assertEquals("html", doctype.attr(NAME));
 assertNull(doctype.attr(PUBLIC_ID));
 assertEquals("http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd", doctype.attr(SYSTEM_ID));
 assertTrue(doctype.has(NAME));
 assertFalse(doctype.has(PUBLIC_ID));
 assertTrue(doctype.has(SYSTEM_ID));
 }

 @Test
 public void testDocumentTypeOuterHtml() throws IOException {
 Document doc = Jsoup.parse("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">");
 DocumentType doctype = (DocumentType) doc.child(0);
 Document newDoc = Jsoup.parse(doctype.outerHtml());

 assertEquals("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">", newDoc.html());
 }

 @Test
 public void testDocumentTypeOuterHtmlWithNoPublicIdAndSystemId() {
 Document doc = Jsoup.parse("<!DOCTYPE html>");
 DocumentType doctype = (DocumentType) doc.child(0);

 assertTrue(doctype.outerHtml().isEmpty());
 }
}