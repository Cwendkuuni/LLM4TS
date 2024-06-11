import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Attributes;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class DocumentTypeTest {

 @Test
 public void testDocumentTypeCreation() {
 Document document = new Document();
 DocumentType documentType = new DocumentType("html", "publicId", "systemId", document.baseUri());

 assertEquals("#doctype", documentType.nodeName());
 assertEquals("html", documentType.attr("name"));
 assertEquals("publicId", documentType.attr("publicId"));
 assertEquals("systemId", documentType.attr("systemId"));
 }

 @Test
 public void testEmptyDocumentTypeCreation() {
 Document document = new Document();
 DocumentType documentType = new DocumentType("", "", "", document.baseUri());

 assertEquals("#doctype", documentType.nodeName());
 assertNull(documentType.attr("name"));
 assertNull(documentType.attr("publicId"));
 assertNull(documentType.attr("systemId"));
 }

 @Test
 public void testDocumentTypeToString() {
 Document document = new Document();
 DocumentType documentType = new DocumentType("html", "publicId", "systemId", document.baseUri());

 StringBuilder expected = new StringBuilder();
 expected.append("<!DOCTYPE html>");

 StringBuilder actual = new StringBuilder();
 documentType.outerHtmlHead(actual, 0, null);

 assertEquals(expected.toString(), actual.toString());
 }

 @Test
 public void testDocumentTypeWithPublicIdAndSystemId() {
 Document document = new Document();
 DocumentType documentType = new DocumentType("html", "publicId", "systemId", document.baseUri());

 StringBuilder expected = new StringBuilder();
 expected.append("<!DOCTYPE html PUBLIC \"publicId\" systemId>\");");

 StringBuilder actual = new StringBuilder();
 documentType.outerHtmlHead(actual, 0, null);

 assertEquals(expected.toString(), actual.toString());
 }

 @Test
 public void testDocumentTypeWithNullBaseUri() {
 Document document = new Document();
 document.baseUri = null;
 DocumentType documentType = new DocumentType("html", "publicId", "systemId", document.baseUri);

 StringBuilder expected = new StringBuilder();
 expected.append("<!DOCTYPE html>");

 StringBuilder actual = new StringBuilder();
 documentType.outerHtmlHead(actual, 0, null);

 assertEquals(expected.toString(), actual.toString());
 }

 @Test
 public void testDocumentTypeWithEmptyName() {
 Document document = new Document();
 DocumentType documentType = new DocumentType("", "publicId", "systemId", document.baseUri);

 assertEquals("#doctype", documentType.nodeName());
 assertNull(documentType.attr("name"));
 }

 @Test
 public void testDocumentTypeWithEmptyPublicId() {
 Document document = new Document();
 DocumentType documentType = new DocumentType("html", "", "systemId", document.baseUri);

 StringBuilder expected = new StringBuilder();
 expected.append("<!DOCTYPE html>");

 StringBuilder actual = new StringBuilder();
 documentType.outerHtmlHead(actual, 0, null);

 assertEquals(expected.toString(), actual.toString());
 }

 @Test
 public void testDocumentTypeWithEmptySystemId() {
 Document document = new Document();
 DocumentType documentType = new DocumentType("html", "publicId", "", document.baseUri);

 StringBuilder expected = new StringBuilder();
 expected.append("<!DOCTYPE html PUBLIC \"publicId\" >");

 StringBuilder actual = new StringBuilder();
 documentType.outerHtmlHead(actual, 0, null);

 assertEquals(expected.toString(), actual.toString());
 }

 @Test
 public void testDocumentTypeWithNullName() {
 Document document = new Document();
 DocumentType documentType = new DocumentType(null, "publicId", "systemId", document.baseUri);

 assertEquals("#doctype", documentType.nodeName());
 assertNull(documentType.attr("name"));
 }
}