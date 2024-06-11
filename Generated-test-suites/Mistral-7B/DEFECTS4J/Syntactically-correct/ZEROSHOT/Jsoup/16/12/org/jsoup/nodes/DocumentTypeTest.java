import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.helper.StringUtil;

import static org.junit.Assert.*;

public class DocumentTypeTest {

 private Document document;

 @Before
 public void setUp() {
 document = new Document();
 }

 /**
 * Test constructor.
 */
 @Test
 public void testDocumentTypeConstructor() {
 String name = "html";
 String publicId = "HTML";
 String systemId = "http://www.w3.org/TR/html4/strict.dtd";
 String baseUri = "http://example.com";

 DocumentType documentType = new DocumentType(name, publicId, systemId, baseUri);

 assertEquals("#doctype", documentType.nodeName());
 assertEquals(name, documentType.attr("name"));
 assertEquals(publicId, documentType.attr("publicId"));
 assertEquals(systemId, documentType.attr("systemId"));
 assertEquals(baseUri, documentType.baseUri());
 }

 /**
 * Test nodeName method.
 */
 @Test
 public void testNodeNameMethod() {
 DocumentType documentType = new DocumentType("html", "", "", "");
 assertEquals("#doctype", documentType.nodeName());
 }

 /**
 * Test outerHtmlHead method.
 */
 @Test
 public void testOuterHtmlHeadMethod() {
 DocumentType documentType = new DocumentType("html", "HTML", "http://www.w3.org/TR/html4/strict.dtd", "");
 StringBuilder accumulator = new StringBuilder();

 documentType.outerHtmlHead(accumulator, 0, null);

 String expected = "<!DOCTYPE html PUBLIC \"HTML\" \"http://www.w3.org/TR/html4/strict.dtd\">";
 assertEquals(expected, accumulator.toString());
 }

 /**
 * Test outerHtmlHead method with empty publicId and systemId.
 */
 @Test
 public void testOuterHtmlHeadMethodWithEmptyIds() {
 DocumentType documentType = new DocumentType("html", "", "", "");
 StringBuilder accumulator = new StringBuilder();

 documentType.outerHtmlHead(accumulator, 0, null);

 String expected = "<!DOCTYPE html>";
 assertEquals(expected, accumulator.toString());
 }

 /**
 * Test outerHtmlTail method.
 */
 @Test
 public void testOuterHtmlTailMethod() {
 DocumentType documentType = new DocumentType("html", "", "", "");
 StringBuilder accumulator = new StringBuilder();

 documentType.outerHtmlTail(accumulator, 0, null);

 assertTrue("", accumulator.toString().isEmpty());
 }

 /**
 * Test attr method.
 */
 @Test
 public void testAttrMethod() {
 DocumentType documentType = new DocumentType("html", "HTML", "http://www.w3.org/TR/html4/strict.dtd", "");

 assertEquals("HTML", documentType.attr("name"));
 assertEquals("HTML", documentType.attr("publicId"));
 assertEquals("http://www.w3.org/TR/html4/strict.dtd", documentType.attr("systemId"));
 }

 /**
 * Test attr method with non-existent attribute.
 */
 @Test(expected = AssertionError.class)
 public void testAttrMethodWithNonexistentAttribute() {
 DocumentType documentType = new DocumentType("html", "", "", "");
 documentType.attr("nonexistent", "value");
 }

 /**
 * Test baseUri method.
 */
 @Test
 public void testBaseUriMethod() {
 DocumentType documentType = new DocumentType("html", "", "", "http://example.com");
 assertEquals("http://example.com", documentType.baseUri());
 }
}