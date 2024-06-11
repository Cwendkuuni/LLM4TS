import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class DocumentTypeTest {

 private Document document;

 @Before
 public void setup() {
 document = Jsoup.parse("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">");
 }

 @Test
 public void testDocumentTypeName() {
 DocumentType docType = document.documentType();
 assertEquals("#doctype", docType.nodeName());
 }

 @Test
 public void testDocumentTypePublicIdAndSystemId() {
 DocumentType docType = document.documentType();
 assertEquals("-//W3C//DTD HTML 4.01 Transitional//EN", docType.attr("name"));
 assertEquals("http://www.w3.org/TR/html4/loose.ddt", docType.attr("systemId"));
 assertEquals("", docType.attr("publicId"));
 }

 @Test
 public void testEmptyPublicIdAndSystemId() {
 DocumentType docType = new DocumentType("#doctype", "", "", "");
 assertEquals("#doctype", docType.nodeName());
 assertNull(docType.attr("publicId"));
 assertNull(docType.attr("systemId"));
 }

 @Test
 public void testNullArgument() {
 try {
 new DocumentType(null, "", "", "");
 } catch (NullPointerException e) {
 // expected
 }
 }
}