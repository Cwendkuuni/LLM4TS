import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Attributes;
import static org.junit.Assert.*;

public class DocumentTypeTest {

 private Document document;

 @Before
 public void setUp() {
 document = Jsoup.parse("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">");
 }

 @Test
 public void testDocumentTypeConstructor() {
 DocumentType documentType = new DocumentType("html", "\"-//W3C//DTD HTML 4.01 Transitional//EN\"", "http://www.w3.org/TR/html4/loose.dtd\"", document.baseUri());
 assertEquals("#doctype", documentType.nodeName());
 assertEquals("html", documentType.attr("name"));
 assertEquals("-//W3C//DTD HTML 4.01 Transitional//EN", documentType.attr("publicId"));
 assertEquals("http://www.w3.org/TR/html4/loose.dtd\"", documentType.attr("systemId"));
 assertEquals(document.baseUri(), documentType.getBaseUri().toString());
 }

 @Test
 public void testDocumentTypeNodeName() {
 DocumentType documentType = new DocumentType("html", "", "", document.baseUri());
 assertEquals("#doctype", documentType.nodeName());
 }

 @Test
 public void testOuterHtmlHead() {
 StringBuilder accumulator = new StringBuilder();
 DocumentType documentType = new DocumentType("html", "\"-//W3C//DTD HTML 4.01 Transitional//EN\"", "http://www.w3.org/TR/html4/loose.dtd\"", document.baseUri());
 documentType.outerHtmlHead(accumulator, 0, null);
 String expected = "<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">";
 assertEquals(expected, accumulator.toString());
 }

 @Test
 public void testOuterHtmlTail() {
 DocumentType documentType = new DocumentType("html", "", "", document.baseUri());
 StringBuilder accumulator = new StringBuilder();
 documentType.outerHtmlTail(accumulator, 0, null);
 assertTrue("", accumulator.toString().isEmpty());
 }

 @Test
 public void testGetAttributes() {
 DocumentType documentType = new DocumentType("html", "", "", document.baseUri());
 Attributes attributes = documentType.attributes();
 assertTrue("There should be an 'name' attribute", attributes.hasKey("name"));
 assertEquals("html", attributes.get("name"));
 assertTrue("There should be a 'publicId' attribute", attributes.hasKey("publicId"));
 assertEquals("\"-//W3C//DTD HTML 4.01 Transitional//EN\"", attributes.get("publicId"));
 assertTrue("There should be a 'systemId' attribute", attributes.hasKey("systemId"));
 assertEquals("http://www.w3.org/TR/html4/loose.dtd\"", attributes.get("systemId"));
 }
}