import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.XmlTreeBuilder;
import org.jsoup.parser.Parser;
import org.jsoup.helper.Validate;
import static org.junit.Assert.*;

public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder;
 private Document parsedDocument;

 @Before
 public void setUp() {
 xmlTreeBuilder = new XmlTreeBuilder();
 }

 @Test
 public void testParseValidXml() {
 String xmlInput = "<root><element>Hello World</element></root>";
 parsedDocument = xmlTreeBuilder.parse(new StringReader(xmlInput), "http://example.com");
 assertNotNull(parsedDocument);
 assertEquals("root", parsedDocument.tagName());
 Element rootElement = parsedDocument.getElementsByTag("root").first();
 assertNotNull(rootElement);
 Element element = rootElement.getElementsByTag("element").first();
 assertNotNull(element);
 assertEquals("Hello World", element.text());
 }

 @Test
 public void testParseEmptyXml() {
 String xmlInput = "<root/>";
 parsedDocument = xmlTreeBuilder.parse(new StringReader(xmlInput), "http://example.com");
 assertNotNull(parsedDocument);
 assertEquals("root", parsedDocument.tagName());
 assertFalse(parsedDocument.childNodes().isEmpty());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidInput() {
 String invalidXmlInput = "<root><element>Hello <script>World</script></element>";
 try {
 parsedDocument = xmlTreeBuilder.parse(new StringReader(invalidXmlInput), "http://example.com");
 } catch (IllegalArgumentException expected) {
 // expected exception
 }
 assertNull(parsedDocument);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseNullInput() {
 try {
 parsedDocument = xmlTreeBuilder.parse(null, "http://example.com");
 } catch (IllegalArgumentException expected) {
 // expected exception
 }
 assertNull(parsedDocument);
 }

 @Test
 public void testParseFragment() {
 String xmlFragment = "<root><element>Hello</element></root>";
 List<Node> fragmentNodes = xmlTreeBuilder.parseFragment(xmlFragment, "http://example.com", ParseErrorList.noTracking(), ParseSettings.preserveCase);
 Document fragmentDocument = new Document();
 fragmentDocument.setDocType(new DocumentType("root", "", ""));
 for (Node node : fragmentNodes) {
 fragmentDocument.appendChild(node);
 }
 assertNotNull(fragmentDocument);
 assertEquals("root", fragmentDocument.tagName());
 Element fragmentRootElement = fragmentDocument.getElementsByTag("root").first();
 assertNotNull(fragmentRootElement);
 Element fragmentElement = fragmentRootElement.getElementsByTag("element").first();
 assertNotNull(fragmentElement);
 assertEquals("Hello", fragmentElement.text());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseFragmentWithInvalidInput() {
 String fragmentInput = "<root><element><script>Hello</script></element>";
 try {
 xmlTreeBuilder.parseFragment(fragmentInput, "http://example.com", ParseErrorList.noTracking(), ParseSettings.preserveCase);
 } catch (IllegalArgumentException expected) {
 // expected exception
 }
 }
}