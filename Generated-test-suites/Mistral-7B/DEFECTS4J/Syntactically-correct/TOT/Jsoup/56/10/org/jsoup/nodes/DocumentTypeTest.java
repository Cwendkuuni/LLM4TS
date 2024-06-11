import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Attributes;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class DocumentTypeTest {

 private Document document;

 @Before
 public void setUp() {
 document = Jsoup.parse("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\" >");
 }

 @Test
 public void testDocumentTypeName() {
 DocumentType doctype = document.documentType();
 assertEquals("#doctype", doctype.nodeName());
 }

 @Test
 public void testDocumentTypePublicId() {
 DocumentType doctype = document.documentType();
 assertEquals("-//W3C//DTD XHTML 1.0 Strict//EN", doctype.attr(PUBLIC_ID));
 }

 @Test
 public void testDocumentTypeSystemId() {
 DocumentType doctype = document.documentType();
 assertEquals("http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd", doctype.attr(SYSTEM_ID));
 }

 @Test
 public void testDocumentTypeWithoutPublicIdAndSystemId() {
 Element html = document.getElementsByTag("html").first();
 DocumentType doctype = html.document().documentType();
 assertEquals("#doctype", doctype.nodeName());
 assertEquals(null, doctype.attr(PUBLIC_ID));
 assertEquals(null, doctype.attr(SYSTEM_ID));
 }

 @Test
 public void testDocumentTypeWithPublicIdButNoSystemId() {
 Element html = document.getElementsByTag("html").first();
 DocumentType doctype = html.document().documentType();
 assertEquals("#doctype", doctype.nodeName());
 assertEquals("DTD HTML 4.01 Transitional//EN", doctype.attr(PUBLIC_ID));
 assertEquals(null, doctype.attr(SYSTEM_ID));
 }

 @Test
 public void testDocumentTypeWithSystemIdButNoPublicId() {
 Element html = document.getElementsByTag("html").first();
 DocumentType doctype = html.document().documentType();
 assertEquals("#doctype", doctype.nodeName());
 assertEquals(null, doctype.attr(PUBLIC_ID));
 assertEquals("about:legacy-compat", doctype.attr(SYSTEM_ID));
 }

 @Test
 public void testDocumentTypeWithPublicIdAndSystemId() {
 Document doc = Jsoup.parse("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.1//EN\" \"http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd\" >");
 DocumentType doctype = doc.documentType();
 assertEquals("#doctype", doctype.nodeName());
 assertEquals("-//W3C//DTD XHTML 1.1//EN", doctype.attr(PUBLIC_ID));
 assertEquals("http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd", doctype.attr(SYSTEM_ID));
 }

 @Test
 public void testDocumentTypeQuirksMode() {
 Document doc = Jsoup.parse("<!DOCTYPE html [<!ENTITY % html_quirks SYSTEM \"about:legacy-compat\" >]>");
 DocumentType doctype = doc.documentType();
 assertEquals("#doctype", doctype.nodeName());
 assertEquals("html_quirks", doctype.attr(PUBLIC_ID));
 assertEquals(null, doctype.attr(SYSTEM_ID));
 }

 @Test(expected = NullPointerException.class)
 public void testDocumentTypeNullAttributes() {
 DocumentType doctype = new DocumentType(null, null, null, null);
 }
}