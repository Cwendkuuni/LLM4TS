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
 Document document = Jsoup.parse("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01//EN\" \"http://www.w3.org/TR/html4/strict.dtd\">");
 DocumentType doctype = (DocumentType) document.child(0);
 assertEquals("#doctype", doctype.nodeName());
 assertEquals("html", doctype.attr(NAME));
 assertEquals("PUBLIC", doctype.attr(PUBLIC_ID));
 assertEquals("http://www.w3.org/TR/html4/strict.dtd", doctype.attr(SYSTEM_ID));
 }

 @Test
 public void testDocumentTypeCreationWithMissingAttributes() {
 Document document = Jsoup.parse("<!DOCTYPE html>");
 DocumentType doctype = (DocumentType) document.child(0);
 assertEquals("#doctype", doctype.nodeName());
 assertNull(doctype.attr(NAME));
 assertNull(doctype.attr(PUBLIC_ID));
 assertNull(doctype.attr(SYSTEM_ID));
 }

 @Test
 public void testDocumentTypeCreationWithOnlyNameAttribute() {
 Document document = Jsoup.parse("<!DOCTYPE html [<!ATTLIST html myAttribute CDATA #IMPLIED]>");
 DocumentType doctype = (DocumentType) document.child(0);
 assertEquals("#doctype", doctype.nodeName());
 assertEquals("html", doctype.attr(NAME));
 assertNull(doctype.attr(PUBLIC_ID));
 assertNull(doctype.attr(SYSTEM_ID));
 }

 @Test
 public void testDocumentTypeCreationWithAllAttributes() {
 Document document = Jsoup.parse("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01//EN\" \"http://www.w3.org/TR/html4/strict.dtd\" [<!ATTLIST html myAttribute CDATA #IMPLIED]>");
 DocumentType doctype = (DocumentType) document.child(0);
 assertEquals("#doctype", doctype.nodeName());
 assertEquals("html", doctype.attr(NAME));
 assertEquals("PUBLIC", doctype.attr(PUBLIC_ID));
 assertEquals("http://www.w3.org/TR/html4/strict.dtd", doctype.attr(SYSTEM_ID));
 }

 @Test
 public void testDocumentTypeCreationWithEmptyAttributes() {
 Document document = Jsoup.parse("<!DOCTYPE html SYSTEM>");
 DocumentType doctype = (DocumentType) document.child(0);
 assertEquals("#doctype", doctype.nodeName());
 assertNull(doctype.attr(NAME));
 assertNull(doctype.attr(PUBLIC_ID));
 assertNull(doctype.attr(SYSTEM_ID));
 }

 @Test
 public void testDocumentTypeCreationWithInvalidSyntax() {
 Document document = Jsoup.parse("<!DOCTYPE html SYSTEM \">"); // missing the final '>'
 DocumentType doctype = null;
 try {
 doctype = (DocumentType) document.child(0);
 } catch (Exception e) {
 // expected
 }
 assertNull(doctype);
 }
}