import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.junit.Test;
import static org.junit.Assert.*;

public class DocumentTypeTest {

 @Test
 public void testDocumentTypeCreation() {
 DocumentType documentType = new DocumentType("html", "HTML PUBLIC", "W3C_HTML_DTD_HTML_401_TRANSITIONAL", "http://www.w3.org/TR/html4/loose.dtd");
 assertEquals("#doctype", documentType.nodeName());
 assertEquals("html", documentType.attr("name"));
 assertEquals("HTML PUBLIC", documentType.attr("publicId"));
 assertEquals("W3C_HTML_DTD_HTML_401_TRANSITIONAL", documentType.attr("systemId"));
 assertEquals("http://www.w3.org/TR/html4/loose.dtd", documentType.baseUri());
 }

 @Test
 public void testEmptyDocumentTypeCreation() {
 DocumentType documentType = new DocumentType("html");
 assertEquals("#doctype", documentType.nodeName());
 assertEquals("html", documentType.attr("name"));
 assertEquals("", documentType.attr("publicId"));
 assertEquals("", documentType.attr("systemId"));
 assertEquals("", documentType.baseUri());
 }

 @Test
 public void testDocumentTypeOuterHtmlHead() {
 DocumentType documentType = new DocumentType("html", "HTML PUBLIC", "W3C_HTML_DTD_HTML_401_TRANSITIONAL", "http://www.w3.org/TR/html4/loose.dtd");
 StringBuilder accumulator = new StringBuilder();
 documentType.outerHtmlHead(accumulator, 0, null);
 assertEquals("<!DOCTYPE html PUBLIC \"HTML PUBLIC\" W3C_HTML_DTD_HTML_401_TRANSITIONAL>\n", accumulator.toString());
 }

 @Test
 public void testDocumentTypeOuterHtmlTail() {
 DocumentType documentType = new DocumentType("html");
 Element parent = new Element("html");
 documentType.outerHtmlTail(accumulator, 0, null);
 assertTrue(accumulator.toString().isEmpty());
 }

 @Test
 public void testDocumentTypeWithPublicId() {
 DocumentType documentType = new DocumentType("html", "HTML_PUBLIC", "W3C_HTML_DTD_HTML_401_TRANSITIONAL", "http://www.w3.org/TR/html4/loose.dtd");
 StringBuilder accumulator = new StringBuilder();
 documentType.outerHtmlHead(accumulator, 0, null);
 assertEquals("<!DOCTYPE html PUBLIC \"HTML_PUBLIC\" W3C_HTML_DTD_HTML_401_TRANSITIONAL>\n", accumulator.toString());
 }

 @Test
 public void testDocumentTypeWithSystemId() {
 DocumentType documentType = new DocumentType("html", "", "W3C_HTML_DTD_HTML_401_TRANSITIONAL", "http://www.w3.org/TR/html4/loose.dtd");
 StringBuilder accumulator = new StringBuilder();
 documentType.outerHtmlHead(accumulator, 0, null);
 assertEquals("<!DOCTYPE html SYSTEM \"http://www.w3.org/TR/html4/loose.dtd\">\n", accumulator.toString());
 }

 @Test
 public void testDocumentTypeWithBothIds() {
 DocumentType documentType = new DocumentType("html", "HTML_PUBLIC", "W3C_HTML_DTD_HTML_401_TRANSITIONAL", "http://www.w3.org/TR/html4/loose.dtd");
 StringBuilder accumulator = new StringBuilder();
 documentType.outerHtmlHead(accumulator, 0, null);
 assertEquals("<!DOCTYPE html PUBLIC \"HTML_PUBLIC\" W3C_HTML_DTD_HTML_401_TRANSITIONAL>\n", accumulator.toString());
 }

 @Test
 public void testDocumentTypeWithEmptyAttributeValues() {
 DocumentType documentType = new DocumentType("html");
 Element parent = new Element("html");
 parent.appendChild(documentType);
 parent.outerHtml(accumulator, null);
 assertEquals("<html>\n<!DOCTYPE html>\n</html>\n", accumulator.toString());
 }
}