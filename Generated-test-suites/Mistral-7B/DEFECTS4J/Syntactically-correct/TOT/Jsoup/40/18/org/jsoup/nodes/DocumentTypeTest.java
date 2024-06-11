import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Attributes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class DocumentTypeTest {

 @Test
 public void testDocumentTypeCreation() {
 Document document = Jsoup.parse("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">");
 DocumentType documentType = document.documentType();

 assertEquals("#doctype", documentType.nodeName());
 assertEquals("html", documentType.attr("name"));
 assertNotNull(documentType.attr("publicId"));
 assertEquals("\"-//W3C//DTD HTML 4.01 Transitional//EN\"", documentType.attr("publicId"));
 assertNotNull(documentType.attr("systemId"));
 assertEquals("http://www.w3.org/TR/html4/loose.dtd", documentType.attr("systemId"));
 }

 @Test
 public void testDocumentTypeCreationEmptyAttributes() {
 Document document = Jsoup.parse("<!DOCTYPE >");
 DocumentType documentType = document.documentType();

 assertEquals("#doctype", documentType.nodeName());
 assertNull(documentType.attr("name"));
 assertNull(documentType.attr("publicId"));
 assertNull(documentType.attr("systemId"));
 }

 @Test
 public void testDocumentTypeCreationNullName() {
 Document document = Jsoup.parse("<!DOCTYPE [ ]>");
 DocumentType documentType = document.documentType();

 assertEquals("#doctype", documentType.nodeName());
 assertNull(documentType.attr("name"));
 assertNull(documentType.attr("publicId"));
 assertNull(documentType.attr("systemId"));
 }

 @Test
 public void testDocumentTypeCreationNullAttributes() {
 Document document = Jsoup.parse("<!DOCTYPE html [ ]>");
 DocumentType documentType = document.documentType();

 assertEquals("#doctype", documentType.nodeName());
 assertEquals("html", documentType.attr("name"));
 assertNull(documentType.attr("publicId"));
 assertNull(documentType.attr("systemId"));
 }

 @Test
 public void testDocumentTypeCreationWithEmptyPublicId() {
 Document document = Jsoup.parse("<!DOCTYPE html PUBLIC \"\" \"http://www.w3.org/TR/html4/loose.dtd\">");
 DocumentType documentType = document.documentType();

 assertEquals("#doctype", documentType.nodeName());
 assertEquals("html", documentType.attr("name"));
 assertNull(documentType.attr("publicId"));
 assertNotNull(documentType.attr("systemId"));
 }

 @Test
 public void testDocumentTypeCreationWithEmptySystemId() {
 Document document = Jsoup.parse("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"\" >");
 DocumentType documentType = document.documentType();

 assertEquals("#doctype", documentType.nodeName());
 assertEquals("html", documentType.attr("name"));
 assertNotNull(documentType.attr("publicId"));
 assertNull(documentType.attr("systemId"));
 }
}