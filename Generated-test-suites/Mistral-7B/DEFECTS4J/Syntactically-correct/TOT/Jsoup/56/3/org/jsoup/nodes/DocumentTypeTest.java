import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Attributes;
import static org.junit.Assert.*;

public class DocumentTypeTest {

 private Document doc;

 @Before
 public void setUp() {
 doc = Jsoup.parse("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">");
 }

 @Test
 public void testDocumentTypeName() {
 DocumentType docType = doc.documentType();
 assertEquals("#doctype", docType.nodeName());
 assertEquals("html", docType.attr(DocumentType.NAME));
 }

 @Test
 public void testDocumentTypeWithPublicId() {
 DocumentType docType = Jsoup.parse("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"-//W3C//DTD XHTML 1.0 Strict//EN\" public \"DTD XHTML 1.0 Strict.dtd\">").documentType();
 assertEquals("#doctype", docType.nodeName());
 assertEquals("DTD XHTML 1.0 Strict", docType.attr(DocumentType.NAME));
 assertEquals("", docType.attr(DocumentType.PUBLIC_ID));
 assertEquals("DTD XHTML 1.0 Strict.dtd", docType.attr(DocumentType.SYSTEM_ID));
 }

 @Test
 public void testDocumentTypeWithSystemId() {
 DocumentType docType = Jsoup.parse("<!DOCTYPE html SYSTEM \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">").documentType();
 assertEquals("#doctype", docType.nodeName());
 assertEquals("html", docType.attr(DocumentType.NAME));
 assertEquals("", docType.attr(DocumentType.PUBLIC_ID));
 assertEquals("http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd", docType.attr(DocumentType.SYSTEM_ID));
 }

 @Test
 public void testDocumentTypeWithNamePublicIdAndSystemId() {
 DocumentType docType = Jsoup.parse("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">").documentType();
 assertEquals("#doctype", docType.nodeName());
 assertEquals("XHTML 1.0 Strict", docType.attr(DocumentType.NAME));
 assertEquals("DTD XHTML 1.0 Strict", docType.attr(DocumentType.PUBLIC_ID));
 assertEquals("http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd", docType.attr(DocumentType.SYSTEM_ID));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCreateDocumentTypeWithNullName() {
 DocumentType docType = new DocumentType(null, "", "", "");
 fail("Should throw IllegalArgumentException with null name");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCreateDocumentTypeWithNullPublicId() {
 DocumentType docType = new DocumentType("html", null, "", "");
 fail("Should throw IllegalArgumentException with null publicId");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCreateDocumentTypeWithNullSystemId() {
 DocumentType docType = new DocumentType("html", "", null, "");
 fail("Should throw IllegalArgumentException with null systemId");
 }

 @Test(expected = NullPointerException.class)
 public void testCreateDocumentTypeWithNullBaseUri() {
 DocumentType docType = new DocumentType("html", "", "", null);
 fail("Should throw NullPointerException with null baseUri");
 }

 @Test
 public void testDocumentTypeHasAttribute() {
 DocumentType docType = doc.documentType();
 assertTrue(docType.has(DocumentType.NAME));
 assertTrue(docType.has(DocumentType.PUBLIC_ID));
 assertTrue(docType.has(DocumentType.SYSTEM_ID));
 }

 @Test
 public void testDocumentTypeHasNoAttribute() {
 DocumentType docType = new DocumentType("html", "", "", "");
 assertFalse(docType.has(""));
 }
}